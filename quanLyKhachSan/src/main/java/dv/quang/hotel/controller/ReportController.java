/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.repository.BillBookingRepository;
import dv.quang.hotel.repository.BillServiceRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
import dv.quang.hotel.repository.UserRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
public class ReportController {

    @Autowired
    BillBookingRepository billBookingRepository;

    @Autowired
    BillServiceRepository billServiceRepository;

    @Autowired
    RoomTypeRepositore roomTypeRepositore;

    @Autowired
    UserRepository userRepository;

    @GetMapping("/report-number-roomtype-year")
    public List<Object[]> getNumberRoomTypeBookingInYear(@RequestParam("year") int year) {
        try {
            return billBookingRepository.getNumberRoomTypeBookingInYear(year);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-revenue-roomtype-year")
    public List<Object[]> getTotalRevenueRoomTypeBookingInYear(@RequestParam("year") int year) {
        try {
            return billBookingRepository.getTotalRevenueRoomTypeBookingInYear(year);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-revenue-roomtype-month")
    public List<Object[]> getTotalRevenueRoomTypeBookingInMonth(@RequestParam("month") int month) {
        try {
            return billBookingRepository.getTotalRevenueRoomTypeBookingInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-number-roomtype-month")
    public List<Object[]> getNumberRoomTypeBookingInMonth(@RequestParam("month") int month) {
        try {
            return billBookingRepository.getNumberRoomTypeBookingInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-number-room")
    public List<Object[]> getNumberRoomByRoomType() {
        try {
            return roomTypeRepositore.getNumberRoomByRoomType();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-number-service-year")
    public List<Object[]> getNumberServiceUsedInYear(@RequestParam("year") int year) {
        try {
            return billServiceRepository.getNumberServiceUsedInYear(year);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-revenue-service-year")
    public List<Object[]> getTotalRevenueServiceInYear(@RequestParam("year") int year) {
        try {
            return billServiceRepository.getTotalRevenueServiceInYear(year);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-number-service-month")
    public List<Object[]> getNumberServiceInMonth(@RequestParam("month") int month) {
        try {
            return billServiceRepository.getNumberServiceInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/report-revenue-service-month")
    public List<Object[]> getTotalRevenueServiceInMonth(@RequestParam("month") int month) {
        try {
            return billServiceRepository.getTotalRevenueServiceInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/top-10-client")
    public List<Object[]> getTop10ClientRevenueInMonth(@RequestParam("month") int month) {
        try {
            return billBookingRepository.getTop10ClientRevenueInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/client-number-age")
    public List<Object[]> getNumberClientByGroupAge() {
        try {
            return userRepository.getNumberClientByGroupAge();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    @GetMapping("/client-evenue-age")
    public List<Object[]> getEvenueClientByGroupAge(@RequestParam("month") int month) {
        try {
            return userRepository.getEvenueClientByGroupAge(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
