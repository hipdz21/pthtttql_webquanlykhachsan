/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AmenityEntity;
import dv.quang.hotel.entity.BookingEntiity;
import dv.quang.hotel.entity.ImageRoomEntity;
import dv.quang.hotel.entity.RoomAmenitiesEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import dv.quang.hotel.repository.AmenityRepository;
import dv.quang.hotel.repository.BookingRepository;
import dv.quang.hotel.repository.ImageRoomRepository;
import dv.quang.hotel.repository.RoomAmenitiesRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
import java.util.ArrayList;
import java.util.Date;
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
public class RoomTypeController {

    @Autowired
    RoomTypeRepositore roomTypeRepositore;

    @Autowired
    ImageRoomRepository imageRoomRepository;

    @Autowired
    AmenityRepository amenityRepository;

    @Autowired
    BookingRepository bookingRepository;

    @Autowired
    RoomAmenitiesRepository roomAmenitiesRepository;

    /*
        get all loại phòng
     */
    @GetMapping("/list-room-type")
    public List<RoomTypeEntity> getListRoomType(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String typeList = request.getHeader("type");
            if (typeList.equalsIgnoreCase("all")) {
                return roomTypeRepositore.findAll();
            } else if (typeList.equalsIgnoreCase("search")) {
                return roomTypeRepositore.findAllByNameRoomType("%" + val + "%");
            }
            return roomTypeRepositore.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    @GetMapping("/room-type")
    public RoomTypeEntity getRoomTypeById(@RequestParam("id") int id) {
        try {
            return roomTypeRepositore.findById(id).get();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Lưu loại phòng
     */
    @PostMapping("/room-type")
    public RoomTypeEntity saveRoomType(@RequestBody RoomTypeEntity roomType) {
        try {
            boolean check = false;
            if (roomType.getRoomTypeId() == 0) {
                check = true;
            }
            roomType = roomTypeRepositore.save(roomType);
            //Neu loai phong them moi, them cac tien ich chung vao loai phong
            if (check) {
                List<AmenityEntity> listAmenity = amenityRepository.findAll();
                for (AmenityEntity a : listAmenity) {
                    if (a.getAmenityType().equalsIgnoreCase("Tiện ích chung")) {
                        RoomAmenitiesEntity roomAmenities = new RoomAmenitiesEntity();
                        roomAmenities.setAmenity(a);
                        roomAmenities.setRoomType(roomType);
                        roomAmenitiesRepository.save(roomAmenities);
                    }
                }
            }
            return roomType;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        xoa loai phong
     */
    @DeleteMapping("/room-type")
    public String deleteRoomType(@RequestParam("id") int id) {
        try {
            RoomTypeEntity roomType = roomTypeRepositore.findById(id).get();
            //Xoa anh phong cho loai phong
            List<ImageRoomEntity> listImageRoom = imageRoomRepository.findAllByRoomType(roomType);
            for (ImageRoomEntity image : listImageRoom) {
                imageRoomRepository.delete(image);
            }
            //Xoa tien ich cho loai phong
            List<RoomAmenitiesEntity> listRoomAmenities = roomAmenitiesRepository.findAllByRoomType(roomType);
            for (RoomAmenitiesEntity rae : listRoomAmenities) {
                roomAmenitiesRepository.delete(rae);
            }
            roomTypeRepositore.delete(roomType);
            return "OK";
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get list room type dto
     */
    @GetMapping("/list-room-type-dto")
    public List<RoomTypeDTO> getListRoomTypeDto(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String typeList = request.getHeader("type");
            List<RoomTypeEntity> listRoomType = new ArrayList<>();
            if (typeList.equalsIgnoreCase("all")) {
                listRoomType = roomTypeRepositore.findAll();
            }
            List<RoomTypeDTO> listRoomTypeDTO = new ArrayList<>();
            for (RoomTypeEntity rt : listRoomType) {
                RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
                roomTypeDTO.setRoomTypeId(rt.getRoomTypeId());
                roomTypeDTO.setNameRoomType(rt.getNameRoomType());
                roomTypeDTO.setAcreage(rt.getAcreage());
                roomTypeDTO.setBed(rt.getBed());
                roomTypeDTO.setNumberAdult(rt.getNumberAdult());
                roomTypeDTO.setNumberChild(rt.getNumberChild());
                roomTypeDTO.setRoomRate(rt.getRoomRate());
                roomTypeDTO.setRoomRateOld(rt.getRoomRateOld());
                roomTypeDTO.setDescription(rt.getDescription());
                List<ImageRoomEntity> listImageRoom = imageRoomRepository.findAllByRoomType(rt);
                List<String> listImage = new ArrayList<>();
                for (ImageRoomEntity i : listImageRoom) {
                    listImage.add(i.getImageRoom());
                }
                roomTypeDTO.setListImage(listImage);
                listRoomTypeDTO.add(roomTypeDTO);
            }
            return listRoomTypeDTO;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get room type dto
     */
    @GetMapping("/room-type-dto")
    public RoomTypeDTO getRoomTypeDTOByYId(@RequestParam("id") int id) {
        try {
            RoomTypeEntity rt = roomTypeRepositore.findById(id).get();
            RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
            roomTypeDTO.setRoomTypeId(rt.getRoomTypeId());
            roomTypeDTO.setNameRoomType(rt.getNameRoomType());
            roomTypeDTO.setAcreage(rt.getAcreage());
            roomTypeDTO.setBed(rt.getBed());
            roomTypeDTO.setNumberAdult(rt.getNumberAdult());
            roomTypeDTO.setNumberChild(rt.getNumberChild());
            roomTypeDTO.setRoomRate(rt.getRoomRate());
            roomTypeDTO.setRoomRateOld(rt.getRoomRateOld());
            roomTypeDTO.setDescription(rt.getDescription());
            List<ImageRoomEntity> listImageRoom = imageRoomRepository.findAllByRoomType(rt);
            List<String> listImage = new ArrayList<>();
            for (ImageRoomEntity i : listImageRoom) {
                listImage.add(i.getImageRoom());
            }
            roomTypeDTO.setListImage(listImage);
            return roomTypeDTO;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Search list room type dto
     */
    @PostMapping("/search-list-room-type")
    public List<RoomTypeDTO> searchListRoomType(@RequestBody InputRoomType ipt) {
        try {
            List<BookingEntiity> listBooking = bookingRepository.findAllByDate(ipt.getCheckInDate(), ipt.getCheckOutDate());
            List<RoomTypeEntity> listRoomType = roomTypeRepositore.findAll();
            List<RoomTypeEntity> listRt = new ArrayList<>();
            for (RoomTypeEntity rt : listRoomType) {
                int count = 0;
                if ((rt.getNumberAdult() + rt.getNumberChild()) >= ipt.getNumberPeople()) {
                    for (BookingEntiity bk : listBooking) {
                        if (bk.getRoom().getRoomType().getRoomTypeId() == rt.getRoomTypeId()) {
                            count++;
                        }
                    }
                    if (count < rt.getNumberRoom()) {
                        listRt.add(rt);
                    }
                }
            }
            List<RoomTypeDTO> listRoomTypeDTO = new ArrayList<>();
            for (RoomTypeEntity rt : listRt) {
                RoomTypeDTO roomTypeDTO = new RoomTypeDTO();
                roomTypeDTO.setRoomTypeId(rt.getRoomTypeId());
                roomTypeDTO.setNameRoomType(rt.getNameRoomType());
                roomTypeDTO.setAcreage(rt.getAcreage());
                roomTypeDTO.setBed(rt.getBed());
                roomTypeDTO.setNumberAdult(rt.getNumberAdult());
                roomTypeDTO.setNumberChild(rt.getNumberChild());
                roomTypeDTO.setRoomRate(rt.getRoomRate());
                roomTypeDTO.setRoomRateOld(rt.getRoomRateOld());
                roomTypeDTO.setDescription(rt.getDescription());
                List<ImageRoomEntity> listImageRoom = imageRoomRepository.findAllByRoomType(rt);
                List<String> listImage = new ArrayList<>();
                for (ImageRoomEntity i : listImageRoom) {
                    listImage.add(i.getImageRoom());
                }
                roomTypeDTO.setListImage(listImage);
                listRoomTypeDTO.add(roomTypeDTO);
            }
            return listRoomTypeDTO;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}

class RoomTypeDTO {

    private int roomTypeId;
    private String nameRoomType;
    private float acreage;
    private String bed;
    private int numberAdult;
    private int numberChild;
    private float roomRate;
    private float roomRateOld;
    private String description;
    private List<String> listImage;

    public int getRoomTypeId() {
        return roomTypeId;
    }

    public void setRoomTypeId(int roomTypeId) {
        this.roomTypeId = roomTypeId;
    }

    public String getNameRoomType() {
        return nameRoomType;
    }

    public void setNameRoomType(String nameRoomType) {
        this.nameRoomType = nameRoomType;
    }

    public float getAcreage() {
        return acreage;
    }

    public void setAcreage(float acreage) {
        this.acreage = acreage;
    }

    public String getBed() {
        return bed;
    }

    public void setBed(String bed) {
        this.bed = bed;
    }

    public int getNumberAdult() {
        return numberAdult;
    }

    public void setNumberAdult(int numberAdult) {
        this.numberAdult = numberAdult;
    }

    public int getNumberChild() {
        return numberChild;
    }

    public void setNumberChild(int numberChild) {
        this.numberChild = numberChild;
    }

    public float getRoomRate() {
        return roomRate;
    }

    public void setRoomRate(float roomRate) {
        this.roomRate = roomRate;
    }

    public float getRoomRateOld() {
        return roomRateOld;
    }

    public void setRoomRateOld(float roomRateOld) {
        this.roomRateOld = roomRateOld;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<String> getListImage() {
        return listImage;
    }

    public void setListImage(List<String> listImage) {
        this.listImage = listImage;
    }
}

class InputRoomType {

    private Date checkInDate;
    private Date checkOutDate;
    private int numberPeople;

    public Date getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(Date checkInDate) {
        this.checkInDate = checkInDate;
    }

    public Date getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(Date checkOutDate) {
        this.checkOutDate = checkOutDate;
    }

    public int getNumberPeople() {
        return numberPeople;
    }

    public void setNumberPeople(int numberPeople) {
        this.numberPeople = numberPeople;
    }
}
