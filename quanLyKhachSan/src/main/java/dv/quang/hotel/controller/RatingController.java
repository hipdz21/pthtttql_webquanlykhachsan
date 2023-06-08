/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.BookingEntiity;
import dv.quang.hotel.entity.RatingEntity;
import dv.quang.hotel.repository.BookingRepository;
import dv.quang.hotel.repository.RatingRepository;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
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
public class RatingController {

    @Autowired
    RatingRepository ratingRepository;

    @Autowired
    BookingRepository bookingRepository;

    @PostMapping("/rating")
    public RatingEntity saveRating(@RequestBody RatingEntity rating) {
        try {
            BookingEntiity booking = bookingRepository.findById(rating.getBooking().getBookingId()).get();
            booking.setBookingStatus("Đã đánh giá");
            booking = bookingRepository.save(booking);
            rating.setBooking(booking);
            return ratingRepository.save(rating);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    @GetMapping("/list-rating")
    public List<RatingEntity> getAllRating(){
        try {
            return ratingRepository.findAll(Sort.by(Sort.Direction.DESC, "ratingId"));
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
