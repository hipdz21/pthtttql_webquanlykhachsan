/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.RoomAmenitiesEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import dv.quang.hotel.repository.AmenityRepository;
import dv.quang.hotel.repository.RoomAmenitiesRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
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
public class RoomAmenitiesController {

    @Autowired
    RoomAmenitiesRepository roomAmenitiesRepository;

    @Autowired
    RoomTypeRepositore roomTypeRepositore;

    @Autowired
    AmenityRepository amenityRepository;

    /*
        get list tien ich theo phong
     */
    @GetMapping("/list-amenity-room")
    public List<RoomAmenitiesEntity> getListRoomAmenitiesByRoomType(@RequestParam("room") int room) {
        try {
            RoomTypeEntity roomType = roomTypeRepositore.findById(room).get();
            List<RoomAmenitiesEntity> listRoomAmenities = roomAmenitiesRepository.findAllByRoomType(roomType);
            return listRoomAmenities;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
