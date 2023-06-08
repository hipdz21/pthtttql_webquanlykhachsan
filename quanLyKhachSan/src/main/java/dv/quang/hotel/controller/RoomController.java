/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.RoomEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import dv.quang.hotel.repository.RoomRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
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
public class RoomController {

    @Autowired
    RoomRepository roomRepository;

    @Autowired
    RoomTypeRepositore roomTypeRepositore;

    /*
        get list room
     */
    @GetMapping("list-room")
    public List<RoomEntity> getListRoom(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String typeList = request.getHeader("type");
            if (typeList.equalsIgnoreCase("all")) {
                return roomRepository.findAll();
            } else if (typeList.equalsIgnoreCase("search")) {
                List<RoomEntity> listRoom = roomRepository.findAllByRoomName("%" + val + "%");
                return listRoom;
            }
            return roomRepository.findAll();

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Lưu room
     */
    @PostMapping("/room")
    public RoomEntity saveRoom(@RequestBody RoomEntity room) {
        try {
            RoomTypeEntity roomType = roomTypeRepositore.findById(room.getRoomType().getRoomTypeId()).get();
            if (room.getRoomId() != 0) {
                RoomEntity roomOld = roomRepository.findById(room.getRoomId()).get();
                RoomTypeEntity roomTypeOld = roomOld.getRoomType();
                if (roomTypeOld.getRoomTypeId() != roomType.getRoomTypeId()) {
                    roomTypeOld.setNumberRoom(roomTypeOld.getNumberRoom() - 1);
                    roomType.setNumberRoom(roomType.getNumberRoom() + 1);
                    roomTypeRepositore.save(roomTypeOld);
                }

            } else {
                roomType.setNumberRoom(roomType.getNumberRoom() + 1);
            }
            roomTypeRepositore.save(roomType);
            room.setRoomType(roomType);
            room = roomRepository.save(room);
            return room;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get room by id
     */
    @GetMapping("/room")
    public RoomEntity getRoomById(@RequestParam("id") int id) {
        try {
            return roomRepository.findById(id).get();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Xoá room
     */
    @DeleteMapping("/room")
    public String deleteRoom(@RequestParam("id") int id) {
        try {
            RoomEntity room = roomRepository.findById(id).get();;
            RoomTypeEntity roomType = room.getRoomType();
            roomType.setNumberRoom(roomType.getNumberRoom() - 1);
            roomTypeRepositore.save(roomType);
            roomRepository.delete(room);
            return "OK";
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
