/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AccountEntity;
import dv.quang.hotel.entity.PositionEntity;
import dv.quang.hotel.entity.StaffEntity;
import dv.quang.hotel.entity.UserEntity;
import dv.quang.hotel.repository.AccountRepository;
import dv.quang.hotel.repository.PositionRepository;
import dv.quang.hotel.repository.StaffRepository;
import dv.quang.hotel.repository.UserRepository;
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
public class StaffController {

    @Autowired
    StaffRepository staffRepository;

    @Autowired
    PositionRepository positionRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    /*
        get list staff
     */
    @GetMapping("/list-staff")
    public List<StaffEntity> getListStaff(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String typeList = request.getHeader("type");
            if (typeList.equalsIgnoreCase("all")) {
                return staffRepository.findAll();
            } else if (typeList.equalsIgnoreCase("search")) {
                String regex = "^NV\\d{9}$";
                if (val.matches(regex)) {
                    return staffRepository.findAllByStaffCode(val);
                } else {
                    List<UserEntity> listUser = userRepository.findAllByName("%" + val + "%");
                    List<StaffEntity> listStaff = new ArrayList<>();
                    for (UserEntity u : listUser) {
                        listStaff.add(staffRepository.findByUser(u));
                    }
                    return listStaff;
                }
            }
            return staffRepository.findAll();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Luu staff
     */
    @PostMapping("/staff")
    public StaffEntity saveStaff(@RequestBody StaffEntity staff) {
        try {
            boolean check = false;
            PositionEntity position = positionRepository.findById(staff.getPosition().getPositionId()).get();
            staff.setPosition(position);
            UserEntity user = staff.getUser();
            user.setUserType("staff");
            user = userRepository.save(user);
            staff.setUser(user);
            if (staff.getStaffId() == 0) {
                check = true;
            }
            staff = staffRepository.save(staff);
            if (check) {
                String staffCode = Integer.toString(staff.getStaffId());
                while (staffCode.length() < 9) {
                    staffCode = "0" + staffCode;
                }
                staffCode = "NV" + staffCode;
                staff.setStaffCode(staffCode);
                staff = staffRepository.save(staff);

                AccountEntity account = new AccountEntity();
                account.setPassword(MD5Encryption.encrypt(staffCode));
                account.setUsername(staffCode);
                account.setUser(user);
                account.setImage("../images/user.png");
                if (position.getPositionName().equalsIgnoreCase("Quản lý")) {
                    account.setAccountType("admin");
                }
                if (position.getPositionName().equalsIgnoreCase("Nhân viên lễ tân")) {
                    account.setAccountType("front_desk_staff");
                }
                if (position.getPositionName().equalsIgnoreCase("Nhân viên dịch vụ")) {
                    account.setAccountType("service_staff");
                }
                if (position.getPositionName().equalsIgnoreCase("Nhân viên buồng phòng")) {
                    account.setAccountType("housekeeper");
                }
                accountRepository.save(account);
            }
            return staff;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get staff
     */
    @GetMapping("/staff")
    public StaffEntity getStaff(@RequestParam("id") int id) {
        try {
            return staffRepository.findById(id).get();
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        delete staff
     */
    @DeleteMapping("/staff")
    public String deleteStaff(@RequestParam("id") int id) {
        try {
            StaffEntity staff = staffRepository.findById(id).get();
            AccountEntity account = accountRepository.findByUser(staff.getUser());
            if (account != null) {
                accountRepository.delete(account);
            }
            staffRepository.delete(staff);
            return "OK";
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
}
