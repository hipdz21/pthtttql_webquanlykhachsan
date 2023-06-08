/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AmenityEntity;
import dv.quang.hotel.entity.RoomAmenitiesEntity;
import dv.quang.hotel.entity.RoomTypeEntity;
import dv.quang.hotel.repository.AmenityRepository;
import dv.quang.hotel.repository.RoomAmenitiesRepository;
import dv.quang.hotel.repository.RoomTypeRepositore;
import java.util.ArrayList;
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
public class AmenityController {
    
    @Autowired
    AmenityRepository amenityRepository;
    
    @Autowired
    RoomAmenitiesRepository roomAmenitiesRepository;
    
    @Autowired
    RoomTypeRepositore roomTypeRepositore;

    /*
        get list amenity
     */
    @GetMapping("/list-amenity")
    public List<AmenityEntity> getListAmenity(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String typeList = request.getHeader("type");
            if (typeList.equalsIgnoreCase("all")) {
                return amenityRepository.findAll();
            } else if(typeList.equalsIgnoreCase("search")){
                return amenityRepository.findAllByAmenityName("%"+val+"%");
            }
            return amenityRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Luu amenity
     */
    @PostMapping("/amenity")
    public AmenityEntity saveEmenity(@RequestBody AmenityDTO amenityDTO) {
        try {
            AmenityEntity amenity = new AmenityEntity();
            amenity.setAmenityId(amenityDTO.getAmenityId());
            amenity.setAmenityIcon(amenityDTO.getAmenityIcon());
            amenity.setAmenityName(amenityDTO.getAmenityName());
            amenity.setAmenityType(amenityDTO.getAmenityType());
            //Xoa toan bo amenity cho loai phong cu trong truong hop sua
            if (amenity.getAmenityId() != 0) {
                List<RoomAmenitiesEntity> listRoomAmenities = roomAmenitiesRepository.findAllByAmenity(amenity);
                for (RoomAmenitiesEntity roomAmenities : listRoomAmenities) {
                    roomAmenitiesRepository.delete(roomAmenities);
                }
            }
            
            amenity = amenityRepository.save(amenity);
            //Neu tien ich chung add cho tat ca loai phong
            if (amenity.getAmenityType().equalsIgnoreCase("Tiện ích chung")) {
                List<RoomTypeEntity> listRoomType = roomTypeRepositore.findAll();
                for (RoomTypeEntity rt : listRoomType) {
                    RoomAmenitiesEntity ra = new RoomAmenitiesEntity();
                    ra.setAmenity(amenity);
                    ra.setRoomType(rt);
                    roomAmenitiesRepository.save(ra);
                }
            } else {//Tien ich rieng: add cho danh sach dua len
                List<RoomTypeEntity> listRoomType = roomTypeRepositore.findAllById(amenityDTO.getListIdRoomType());
                for (RoomTypeEntity rt : listRoomType) {
                    RoomAmenitiesEntity ra = new RoomAmenitiesEntity();
                    ra.setAmenity(amenity);
                    ra.setRoomType(rt);
                    roomAmenitiesRepository.save(ra);
                }
            }
            return amenity;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get amenity
     */
    @GetMapping("/amenity")
    public AmenityDTO getAmenityById(@RequestParam("id") int id) {
        try {
            AmenityEntity amenity = amenityRepository.findById(id).get();
            AmenityDTO amenityDTO = new AmenityDTO();
            amenityDTO.setAmenityId(amenity.getAmenityId());
            amenityDTO.setAmenityName(amenity.getAmenityName());
            amenityDTO.setAmenityType(amenity.getAmenityType());
            amenityDTO.setAmenityIcon(amenity.getAmenityIcon());
            List<RoomAmenitiesEntity> listRoomAmenities = roomAmenitiesRepository.findAllByAmenity(amenity);
            List<Integer> listIdRoomType = new ArrayList<>();
            for (RoomAmenitiesEntity ra : listRoomAmenities) {
                listIdRoomType.add(ra.getRoomType().getRoomTypeId());
            }
            amenityDTO.setListIdRoomType(listIdRoomType);
            return amenityDTO;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Xoa amenity
     */
    @DeleteMapping("/amenity")
    public String deleteAmenity(@RequestParam("id") int id) {
        try {
            AmenityEntity amenity = amenityRepository.findById(id).get();
            List<RoomAmenitiesEntity> listRoomAmenities = roomAmenitiesRepository.findAllByAmenity(amenity);
            for (RoomAmenitiesEntity ra : listRoomAmenities) {
                roomAmenitiesRepository.delete(ra);
            }
            amenityRepository.delete(amenity);
            return "OK";
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
}

class AmenityDTO {

    private int amenityId;
    private String amenityName;
    private String amenityType;
    private String amenityIcon;
    private List<Integer> listIdRoomType;
    
    public int getAmenityId() {
        return amenityId;
    }
    
    public void setAmenityId(int amenityId) {
        this.amenityId = amenityId;
    }
    
    public String getAmenityName() {
        return amenityName;
    }
    
    public void setAmenityName(String amenityName) {
        this.amenityName = amenityName;
    }
    
    public String getAmenityType() {
        return amenityType;
    }
    
    public void setAmenityType(String amenityType) {
        this.amenityType = amenityType;
    }
    
    public String getAmenityIcon() {
        return amenityIcon;
    }
    
    public void setAmenityIcon(String amenityIcon) {
        this.amenityIcon = amenityIcon;
    }
    
    public List<Integer> getListIdRoomType() {
        return listIdRoomType;
    }
    
    public void setListIdRoomType(List<Integer> listIdRoomType) {
        this.listIdRoomType = listIdRoomType;
    }
};
