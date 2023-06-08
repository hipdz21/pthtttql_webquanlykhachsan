/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.BillBookingEntity;
import dv.quang.hotel.entity.BookingEntiity;
import dv.quang.hotel.entity.RoomEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import dv.quang.hotel.repository.BillBookingRepository;
import dv.quang.hotel.repository.BookingRepository;
import dv.quang.hotel.repository.RoomRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dovan
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class BillBookingController {

    @Autowired
    BillBookingRepository billBookingRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomTypeRepositore roomTypeRepositore;
    
    @Autowired
    RoomRepository roomRepository;

    @Autowired
    SendMail sendMail;

    @PostMapping("/bill-booking")
    public BillBookingEntity saveBillBooking(@RequestBody BillBookingEntity billBooking) {
        try {
            BookingEntiity booking = bookingRepository.findById(billBooking.getBooking().getBookingId()).get();
            booking.setCheckOutDate(new Date());
            booking.setBookingStatus("Đã trả");
            RoomEntity room = booking.getRoom();
            room.setRoomStatus("Phòng trống");
            room = roomRepository.save(room);
            booking.setRoom(room);
            booking = bookingRepository.save(booking);
            billBooking.setBooking(booking);
            billBooking.setBillDate(new Date());
            billBooking.setTotalAmount(booking.getBookingDeposit() + billBooking.getPenaltyFee());
            billBooking = billBookingRepository.save(billBooking);
            sendMailBooking(booking, billBooking);
            return billBooking;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    private void sendMailBooking(BookingEntiity booking, BillBookingEntity bill) {
        try {
            SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
            SimpleDateFormat formatdob = new SimpleDateFormat("dd/MM/yyyy");
            RoomTypeEntity roomType = roomTypeRepositore.findById(booking.getRoomTypeId()).get();
            String header = "<div class=\"gmail-header\" style=\"color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                    + "    <p\n"
                    + "        style=\"margin: 0px; font-size: 24px; font-weight: 700; color: rgb(255, 255, 255); background-color: rgb(218, 103, 23); padding: 24px;\">\n"
                    + "        Felicity</p>\n"
                    + "</div>";
            String footer = "<div class=\"gmail-footer\"\n"
                    + "    style=\"color: rgb(255, 255, 255); background-color: rgb(0, 0, 0); padding: 24px; text-align: center; font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                    + "    <p class=\"gmail-footer--title\" style=\"margin: 0px; font-size: 24px; font-weight: 700;\">Felicity</p>\n"
                    + "    <p class=\"gmail-footer--content\" style=\"margin: 4px 4px 12px; font-size: 18px; font-weight: 600;\">Nơi nghỉ dưỡng\n"
                    + "        tuyệt vời để tìm thấy sự yên bình và sự thư thái</p>\n"
                    + "    <p style=\"margin: 4px;\">Address: 100 Giảng Võ, Ba Đình, Hà Nội</p>\n"
                    + "    <p style=\"margin: 4px;\">Tel: (+84) 24 6666 8888 | Fax: (+84) 24 9999 8888</p>\n"
                    + "    <p style=\"margin: 4px;\">Mail: reservation@felicity.com</p>\n"
                    + "</div>";
            String content = "<div class=\"gmail-content\"\n"
                    + "    style=\"padding: 12px 24px; color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                    + "    <p style=\"margin: 4px;\">Xin chào " + booking.getUser().getName() + ",</p>\n"
                    + "    <p style=\"margin: 4px;\">Bạn đã trả phòng tại Felicity</p>\n"
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
                    + "    <p style=\"margin: 4px;\">Thông tin thanh toán:</p>\n"
                    + "    <p style=\"margin-left: 16px;\">Giá phòng: " + booking.getRoomPrice() + "đ</p>\n"
                    + "    <p style=\"margin-left: 16px;\">Tổng tiền phòng: " + booking.getBookingDeposit() + "đ</p>\n"
                    + "    <p style=\"margin-left: 16px;\">Tiền đặt cọc: -" + booking.getBookingDeposit() + "đ</p>\n"
                    + "    <p style=\"margin-left: 16px;\">Tiền phạt: " + bill.getPenaltyFee() + "đ</p>\n"
                    + "    <p style=\"margin-left: 16px;\">Tổng tiền thanh toán: " + (bill.getTotalAmount() - booking.getBookingDeposit()) + "đ</p>\n"
                    + "    <p style=\"margin-left: 4px;\">Ghi chú: " + bill.getBillNote() + "</p>\n"
                    + "    <p style=\"margin: 4px; text-align: center;\">Cảm ơn bạn đã đặt phòng tại khách sạn chúng tôi.</p>\n"
                    + "</div>";
            String mes = header + content + footer;
            sendMail.send(booking.getClient().getEmail(), "Felicity - Xác nhận check-out", mes);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
}
