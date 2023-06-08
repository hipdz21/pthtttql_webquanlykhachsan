/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.repository.BillBookingRepository;
import dv.quang.hotel.repository.BillServiceRepository;
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
public class RevenueController {

    @Autowired
    BillBookingRepository billBookingRepository;

    @Autowired
    BillServiceRepository billServiceRepository;

    @GetMapping("/revenue-month")
    public Double getRevennueByMonth(@RequestParam("month") int month) {
        try {
            double revenueBooking = billBookingRepository.getTotalRevenueForMonth(month);
            double revenueUseService = billServiceRepository.getTotalRevenueForMonth(month);
            return revenueBooking + revenueUseService;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/revenue-day-month")
    public List<Object[]> getTotalRevenueByDayInMonth(@RequestParam("month") int month) {
        try {
            return billBookingRepository.getTotalRevenueByDayInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/revenue-month-year")
    public List<Object[]> getTotalRevenueByMonthInYear(@RequestParam("year") int year) {
        try {
            return billBookingRepository.getTotalRevenueByMonthInYear(year);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/revenue-year")
    public List<Object[]> getTotalRevenueByYear() {
        try {
            return billBookingRepository.getTotalRevenueByYear();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/revenue-booking-useservice-month")
    public List<Object[]> getTotalRevenueBookingAndUseServiceInMonth(@RequestParam("month") int month) {
        try {
            return billBookingRepository.getTotalRevenueBookingAndUseServiceInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/revenue-booking-month-year")
    public List<Object[]> getTotalRevenueBookingByMonthInYear(@RequestParam("year") int year) {
        try {
            return billBookingRepository.getTotalRevenueBookingByMonthInYear(year);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    @GetMapping("/revenue-useservice-month-year")
    public List<Object[]> getTotalRevenueUseServiceByMonthInYear(@RequestParam("year") int year) {
        try {
            return billServiceRepository.getTotalRevenueUseServiceByMonthInYear(year);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    
}
