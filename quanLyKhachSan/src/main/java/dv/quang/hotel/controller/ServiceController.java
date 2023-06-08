/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.ServiceEntity;
import dv.quang.hotel.repository.ServiceRepository;
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
public class ServiceController {

    @Autowired
    ServiceRepository serviceRepository;

    /*
        get list service
     */
    @GetMapping("/list-service")
    public List<ServiceEntity> getListService(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String typeList = request.getHeader("type");
            if (typeList.equalsIgnoreCase("all")) {
                return serviceRepository.findAll();
            } else if(typeList.equalsIgnoreCase("search")){
                return serviceRepository.findAllByServiceName("%"+val+"%");
            }
            return serviceRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Luu dich vu
     */
    @PostMapping("/service")
    public ServiceEntity saveService(@RequestBody ServiceEntity service) {
        try {
            if (service.getServiceId() != 0) {
                if (service.getLinkImage().isEmpty()) {
                    ServiceEntity serviceEntity = serviceRepository.findById(service.getServiceId()).get();
                    service.setLinkImage(serviceEntity.getLinkImage());
                }
            }
            service = serviceRepository.save(service);
            return service;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get dich vu
     */
    @GetMapping("/service")
    public ServiceEntity getServiceById(@RequestParam("id") int id) {
        try {
            return serviceRepository.findById(id).get();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    /*
        xoa service
    */
    @DeleteMapping("/service")
    public String deleteService(@RequestParam("id") int id){
        try {
            ServiceEntity service = serviceRepository.findById(id).get();
            serviceRepository.delete(service);
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
}
