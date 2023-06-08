/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AccountEntity;
import dv.quang.hotel.entity.ContactEntity;
import dv.quang.hotel.entity.StaffEntity;
import dv.quang.hotel.entity.UserEntity;
import dv.quang.hotel.repository.AccountRepository;
import dv.quang.hotel.repository.ContactRepository;
import dv.quang.hotel.repository.StaffRepository;
import dv.quang.hotel.repository.UserRepository;
import java.util.Date;
import java.util.List;
import javax.servlet.http.HttpServletRequest;
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
public class ContactController {
    @Autowired
    ContactRepository contactRepository;
    
    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;
    
    @Autowired
    StaffRepository staffRepository;
    
    @GetMapping("/list-contact")
    public List<ContactEntity> getListContact(){
        try {
            List<ContactEntity> listContact = contactRepository.findAll(Sort.by(Sort.Direction.DESC, "contactId"));
            return listContact;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    @PostMapping("/contact")
    public ContactEntity saveConteact(@RequestBody ContactEntity contact, HttpServletRequest request){
        try {
            if(contact.getContactId() == 0){
                contact.setContactStatus("Chưa tiếp nhận");
                contact.setDate(new Date());
                return contactRepository.save(contact);
            } else {
                ContactEntity ce = contactRepository.findById(contact.getContactId()).get();
                ce.setContactStatus("Đã tiếp nhận");
                String username = request.getHeader("username");
                AccountEntity account = accountRepository.findByUsername(username);
                UserEntity user = account.getUser();
                StaffEntity staff = staffRepository.findByUser(user);
                ce.setStaff(staff);
                return contactRepository.save(ce);
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
