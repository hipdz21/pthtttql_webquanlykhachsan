/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.ImageRoomEntity;
import dv.quang.hotel.repository.ImageRoomRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
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
public class ImageRoomController {
    @Autowired
    ImageRoomRepository imageRoomRepository;
    
    @Autowired
    RoomTypeRepositore roomTypeRepository;
    /*
        Lưu ảnh loại phòng
    */
    @PostMapping("/image-room")
    public ImageRoomEntity saveImageRoom(@RequestBody ImageRoomEntity imageRoom){
        try {
            imageRoom.setRoomType(roomTypeRepository.findById(imageRoom.getRoomType().getRoomTypeId()).get());
            imageRoom = imageRoomRepository.save(imageRoom);
            return imageRoom;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
