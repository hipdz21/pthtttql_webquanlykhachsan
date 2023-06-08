/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.PositionEntity;
import dv.quang.hotel.repository.PositionRepository;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dovan
 */
@CrossOrigin
@RestController
@RequestMapping("/api")
public class PositionController {
    @Autowired
    PositionRepository positionRepository;
    
    /*
        get list position
    */
    @GetMapping("/list-position")
    public List<PositionEntity> getListPosition(HttpServletRequest request){
        try {
            String typeList = request.getHeader("type");
            if (typeList.equalsIgnoreCase("all")){
                return positionRepository.findAll();
            }
            return positionRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
