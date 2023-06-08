/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AccountEntity;
import dv.quang.hotel.entity.BillBookingEntity;
import dv.quang.hotel.entity.BookingEntiity;
import dv.quang.hotel.entity.RoomEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import dv.quang.hotel.entity.UserEntity;
import dv.quang.hotel.repository.AccountRepository;
import dv.quang.hotel.repository.BillBookingRepository;
import dv.quang.hotel.repository.BookingRepository;
import dv.quang.hotel.repository.ClientRepository;
import dv.quang.hotel.repository.RoomRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
import dv.quang.hotel.repository.UserRepository;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dovan
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class BookingController {

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomTypeRepositore roomTypeRepositore;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    BillBookingRepository billBookingRepository;

    @Autowired
    SendMail sendMail;

    @PostMapping("/booking")
    public BookingEntiity saveBooking(@RequestBody BookingEntiity booking) {
        try {
            if (booking.getBookingId() != 0) {
                int roomId = 0;
                if (booking.getRoom() != null) {
                    roomId = booking.getRoom().getRoomId();
                }
                BookingEntiity bk = bookingRepository.findById(booking.getBookingId()).get();
                bk.setBookingStatus(booking.getBookingStatus());
                booking = bookingRepository.save(bk);
                if (booking.getBookingStatus().equalsIgnoreCase("Huỷ")) {
                    BillBookingEntity bill = new BillBookingEntity();
                    bill.setBillDate(new Date());
                    bill.setBooking(booking);
                    Date now = new Date();
                    long diffInMillies = Math.abs(booking.getCheckInDate().getTime() - now.getTime());
                    long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                    if (diffInDays >= 7) {
                        bill.setTotalAmount(0);
                    } else if (diffInDays >= 3) {
                        bill.setTotalAmount(booking.getBookingDeposit() / 2);
                    } else {
                        bill.setTotalAmount(booking.getBookingDeposit());
                    }
                    bill.setBillNote("Đơn đặt phòng huỷ");
                    billBookingRepository.save(bill);
                    sendMailBooking(booking, "Huỷ phòng");
                } else if (booking.getBookingStatus().equalsIgnoreCase("Đã nhận")) {
                    booking.setCheckInDate(new Date());
                    RoomEntity r = roomRepository.findById(roomId).get();
                    booking.setRoom(r);
                    booking = bookingRepository.save(booking);
                    r.setRoomStatus("Phòng bận");
                    roomRepository.save(r);
                }
            } else {
                booking.setBookingDate(new Date());
                booking.setBookingStatus("Đã đặt");
                long diffInMillies = Math.abs(booking.getCheckOutDate().getTime() - booking.getCheckInDate().getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                RoomTypeEntity roomType = roomTypeRepositore.findById(booking.getRoomTypeId()).get();
                booking.setBookingDeposit(roomType.getRoomRate() * diffInDays);
                booking.setRoomPrice(roomType.getRoomRate());
                booking.setRoomTypeId(roomType.getRoomTypeId());
                booking.setClient(clientRepository.save(booking.getClient()));
                UserEntity user = userRepository.findById(booking.getUser().getUserId()).get();
                booking.setUser(user);
                booking = bookingRepository.save(booking);
                sendMailBooking(booking, "Đặt phòng");
            }
            return booking;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/list-booking")
    public List<BookingEntiity> getListBooking(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String type = request.getHeader("type");
            if (type.equalsIgnoreCase("user")) {
                AccountEntity accountEntity = accountRepository.findByUsername(val);
                List<BookingEntiity> listBooking = bookingRepository.findAllByUser(accountEntity.getUser(), Sort.by(Sort.Direction.DESC, "bookingId"));
                return listBooking;
            }
            if (type.equalsIgnoreCase("client")) {
                UserEntity ue = userRepository.findById(Integer.parseInt(val)).get();
                List<BookingEntiity> listBooking = bookingRepository.findAllByUser(ue, Sort.by(Sort.Direction.DESC, "bookingId"));
                return listBooking;
            }
            if (type.equalsIgnoreCase("use-service")) {
                UserEntity ue = userRepository.findById(Integer.parseInt(val)).get();
                List<BookingEntiity> listBooking = bookingRepository.findAllByUser(ue, Sort.by(Sort.Direction.DESC, "bookingId"));
                List<BookingEntiity> lbk = new ArrayList<>();
                for (BookingEntiity b : listBooking) {
                    if (b.getBookingStatus().equalsIgnoreCase("Đã đặt") || b.getBookingStatus().equalsIgnoreCase("Đã nhận")) {
                        lbk.add(b);
                    }
                }
                return lbk;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @PostMapping("/get-list-room-by-booking")
    public List<RoomEntity> getListRoomByBooking(@RequestBody BookingEntiity booking) {
        try {
            BookingEntiity bk = bookingRepository.findById(booking.getBookingId()).get();
            RoomTypeEntity roomType = roomTypeRepositore.findById(bk.getRoomTypeId()).get();
            List<RoomEntity> listRoom = roomRepository.findAllByRoomType(roomType);
            List<RoomEntity> list = new ArrayList<>();
            for (RoomEntity r : listRoom) {
                if (r.getRoomStatus().equalsIgnoreCase("Phòng trống")) {
                    list.add(r);
                }
            }
            return list;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    @GetMapping("/get-list-booking-by-room")
    public List<BookingEntiity> getListBookingByRoom(@RequestParam("id") int id){
        try {
            RoomEntity room = roomRepository.findById(id).get();
            List<BookingEntiity> listBooking = bookingRepository.findAllByRoom(room, Sort.by(Sort.Direction.DESC, "bookingId"));
            return listBooking;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/number-booking-month")
    public int getNumberBookingMonth(@RequestParam("month") int month){
        try {
            return bookingRepository.getNumberBookingInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    
    
    
    
    
    
    private void sendMailBooking(BookingEntiity booking, String type) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat formatdob = new SimpleDateFormat("dd/MM/yyyy");
            RoomTypeEntity roomType = roomTypeRepositore.findById(booking.getRoomTypeId()).get();
            String header = "<div class=\"gmail-header\" style=\"color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                    + "    <p\n"
                    + "        style=\"margin: 0px; font-size: 24px; font-weight: 700; color: rgb(255, 255, 255); background-color: rgb(218, 103, 23); padding: 24px;\">\n"
                    + "        Felicity</p>\n"
                    + "</div>";
            String content = "";
            String footer = "<div class=\"gmail-footer\"\n"
                    + "    style=\"color: rgb(255, 255, 255); background-color: rgb(0, 0, 0); padding: 24px; text-align: center; font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                    + "    <p class=\"gmail-footer--title\" style=\"margin: 0px; font-size: 24px; font-weight: 700;\">Felicity</p>\n"
                    + "    <p class=\"gmail-footer--content\" style=\"margin: 4px 4px 12px; font-size: 18px; font-weight: 600;\">Nơi nghỉ dưỡng\n"
                    + "        tuyệt vời để tìm thấy sự yên bình và sự thư thái</p>\n"
                    + "    <p style=\"margin: 4px;\">Address: 100 Giảng Võ, Ba Đình, Hà Nội</p>\n"
                    + "    <p style=\"margin: 4px;\">Tel: (+84) 24 6666 8888 | Fax: (+84) 24 9999 8888</p>\n"
                    + "    <p style=\"margin: 4px;\">Mail: reservation@felicity.com</p>\n"
                    + "</div>";
            if (type.equalsIgnoreCase("Đặt phòng")) {
                content = "<div class=\"gmail-content\"\n"
                        + "    style=\"padding: 12px 24px; color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                        + "    <p style=\"margin: 4px;\">Xin chào " + booking.getUser().getName() + ",</p>\n"
                        + "    <p style=\"margin: 4px;\">Chúc mừng, bạn đã đặt phòng thành công tại Felicity</p>\n"
                        + "    <p style=\"margin: 4px;\">Thông tin khách hàng:</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Họ và Tên: " + booking.getClient().getName() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Giới tính: " + booking.getClient().getGender() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày sinh: " + formatdob.format(booking.getClient().getDob()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số điện thoại: " + booking.getClient().getPhoneNumber() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số CCCD: " + booking.getClient().getCccd() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số hộ chiếu: " + booking.getClient().getPassport() + "</p>\n"
                        + "    <p style=\"margin: 4px;\">Thông tin đặt phòng:</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày đặt phòng: " + formatter.format(booking.getBookingDate()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày check-in: " + formatter.format(booking.getCheckInDate()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày check-out: " + formatter.format(booking.getCheckOutDate()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Phòng: " + roomType.getNameRoomType() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Giá phòng: " + booking.getRoomPrice() + "vnđ</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số tiền đã cọc: " + booking.getBookingDeposit() + "vnđ</p>\n"
                        + "    <p style=\"margin: 4px;\">Chính sách nhận/trả phòng:</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Nhận phòng sau 14:00</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Trả phòng trước 12:00</p>\n"
                        + "    <p style=\"margin: 4px;\">Chính sách huỷ phòng:</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Huỷ phòng trước 7 ngày hoàn 100% đặt cọc</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Huỷ phòng trước 3 ngày hoàn 50% đặt cọc</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Huỷ phòng sau 3 ngày không hoàn cọc</p>\n"
                        + "    <p style=\"margin: 4px; text-align: center;\">Cảm ơn bạn đã đặt phòng tại khách sạn chúng tôi. Chúc bạn có một kỳ nghỉ dưỡng tốt đẹp.</p>\n"
                        + "</div>";
                String mes = header + content + footer;
                sendMail.send(booking.getClient().getEmail(), "Felicity - Xác nhận đặt phòng thành công", mes);
            } else if (type.equalsIgnoreCase("Huỷ phòng")) {
                Date now = new Date();
                long diffInMillies = Math.abs(booking.getCheckInDate().getTime() - now.getTime());
                long diffInDays = TimeUnit.DAYS.convert(diffInMillies, TimeUnit.MILLISECONDS);
                String m = "";
                if (diffInDays >= 7) {
                    m = "    <p style=\"margin: 8px;\">Bạn được hoàn 100% phí đặt cọc (" + booking.getBookingDeposit() + "vnđ)</p>\n";
                } else if (diffInDays >= 3) {
                    m = "    <p style=\"margin: 8px;\">Bạn được hoàn 50% phí đặt cọc (" + booking.getBookingDeposit() / 2 + "vnđ)</p>\n";
                } else {
                    m = "    <p style=\"margin: 8px;\">Bạn không được hoàn phí đặt cọc</p>\n";
                }
                content = "<div class=\"gmail-content\"\n"
                        + "    style=\"padding: 12px 24px; color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                        + "    <p style=\"margin: 4px;\">Xin chào " + booking.getUser().getName() + ",</p>\n"
                        + "    <p style=\"margin: 4px;\">Bạn đã huỷ đặt phòng tại Felicity</p>\n"
                        + "    <p style=\"margin: 4px;\">Thông tin khách hàng:</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Họ và Tên: " + booking.getClient().getName() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Giới tính: " + booking.getClient().getGender() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày sinh: " + formatdob.format(booking.getClient().getDob()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số điện thoại: " + booking.getClient().getPhoneNumber() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số CCCD: " + booking.getClient().getCccd() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số hộ chiếu: " + booking.getClient().getPassport() + "</p>\n"
                        + "    <p style=\"margin: 4px;\">Thông tin đặt phòng:</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày đặt phòng: " + formatter.format(booking.getBookingDate()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày check-in: " + formatter.format(booking.getCheckInDate()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Ngày check-out: " + formatter.format(booking.getCheckOutDate()) + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Phòng: " + roomType.getNameRoomType() + "</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Giá phòng: " + booking.getRoomPrice() + "vnđ</p>\n"
                        + "    <p style=\"margin-left: 16px;\">Số tiền đã cọc: " + booking.getBookingDeposit() + "vnđ</p>\n"
                        + "    <p style=\"margin: 4px;\">Chính sách huỷ phòng</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Huỷ phòng trước 7 ngày hoàn 100% đặt cọc</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Huỷ phòng trước 3 ngày hoàn 50% đặt cọc</p>\n"
                        + "    <p style=\"margin-left: 16px;\">- Huỷ phòng sau 3 ngày không hoàn cọc</p>\n"
                        + "    <p style=\"margin: 4px;\">Hoàn tiền:</p>\n"
                        + m
                        + "    <p style=\"margin: 4px;\">(* Tiền cọc được hoàn trong vòng 30 ngày kể từ ngày huỷ)</p>\n"
                        + "    <p style=\"margin: 4px; text-align: center;\"></p>\n"
                        + "</div>";
                String mes = header + content + footer;
                sendMail.send(booking.getClient().getEmail(), "Felicity - Xác nhận huỷ đặt phòng", mes);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
