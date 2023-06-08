/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AccountEntity;
import dv.quang.hotel.entity.BillServiceEntity;
import dv.quang.hotel.entity.BookingEntiity;
import dv.quang.hotel.entity.UseServiceEntity;
import dv.quang.hotel.entity.UserEntity;
import dv.quang.hotel.repository.AccountRepository;
import dv.quang.hotel.repository.BillServiceRepository;
import dv.quang.hotel.repository.BookingRepository;
import dv.quang.hotel.repository.ServiceRepository;
import dv.quang.hotel.repository.UseServiceRepository;
import dv.quang.hotel.repository.UserRepository;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
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

public class UseServiceController {

    @Autowired
    UseServiceRepository useServiceRepository;

    @Autowired
    ServiceRepository serviceRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    BillServiceRepository billServiceRepository;

    @Autowired
    AccountRepository accountRepository;

    @PostMapping("/use-service")
    public UseServiceEntity saveUseService(@RequestBody UseServiceEntity useService) {
        try {
            if (useService.getUseServiceId() == 0) {
                useService.setDateUse(new Date());
                useService.setService(serviceRepository.findById(useService.getService().getServiceId()).get());
                useService.setServicePrice(useService.getService().getServicePrice());
                useService.setBooking(bookingRepository.findById(useService.getBooking().getBookingId()).get());
                useService = useServiceRepository.save(useService);
                return useService;
            } else {
                UseServiceEntity use = useServiceRepository.findById(useService.getUseServiceId()).get();
                if (useService.getUseServiceStatus().equalsIgnoreCase("Huỷ")) {
                    use.setUseServiceStatus(useService.getUseServiceStatus());
                    use = useServiceRepository.save(use);
                    return use;
                } else {
                    BillServiceEntity billService = new BillServiceEntity();
                    billService.setBillDate(new Date());
                    billService.setTotalAmount(use.getServicePrice() * use.getServiceQuantity());
                    billService.setUseService(use);
                    billServiceRepository.save(billService);
                    use.setUseServiceStatus(useService.getUseServiceStatus());
                    use = useServiceRepository.save(use);
                    return use;
                }
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/get-list-use-service-by-user")
    public List<UseServiceEntity> getListUseServiceByUser(@RequestParam("id") String id, HttpServletRequest request) {
        try {
            UserEntity user = new UserEntity();
            String type = request.getHeader("type");
            if (type != null) {
                AccountEntity account = accountRepository.findByUsername(id);
                user = account.getUser();
            } else {
                user = userRepository.findById(Integer.parseInt(id)).get();
            }
            List<BookingEntiity> listBooking = bookingRepository.findAllByUser(user, Sort.by(Sort.Direction.DESC, "bookingId"));
            List<UseServiceEntity> listUseService = new ArrayList<>();
            for (BookingEntiity b : listBooking) {
                List<UseServiceEntity> lus = useServiceRepository.findAllByBooking(b, Sort.by(Sort.Direction.DESC, "useServiceId"));
                listUseService.addAll(lus);
            }
            return listUseService;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
