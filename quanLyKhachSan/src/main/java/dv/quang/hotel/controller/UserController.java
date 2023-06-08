/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AccountEntity;
import dv.quang.hotel.entity.UserEntity;
import dv.quang.hotel.repository.AccountRepository;
import dv.quang.hotel.repository.UserRepository;
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
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    AccountRepository accountRepository;

    /*
        Luu user voi vai tro client
     */
    @PostMapping("/client")
    public UserEntity saveClient(@RequestBody UserEntity user) {
        try {
            user.setUserType("client");
            boolean check = false;
            if (user.getUserId() == 0) {
                check = true;
            }
            user = userRepository.save(user);
            AccountEntity account = new AccountEntity();
            if (check) {
                account.setAccountType("client");
                account.setImage("../images/user.png");
                account.setUser(user);
                account.setPassword(MD5Encryption.encrypt("12345678"));
            } else {
                account = accountRepository.findByUser(user);
            }
            account.setUsername(user.getEmail());
            accountRepository.save(account);
            return user;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get list user co vai tro client
     */
    @GetMapping("list-client")
    public List<UserDTO> getListClient(HttpServletRequest request, @RequestParam("val") String val) {
        try {
            String typeList = request.getHeader("type");
            List<UserEntity> listUser = new ArrayList<>();
            listUser = userRepository.findAllByUserType("client");
            if (typeList.equalsIgnoreCase("all")) {
                listUser = userRepository.findAllByUserType("client");
            } else if (typeList.equalsIgnoreCase("search")) {
                String regex = "^\\d+$";
                if (val.matches(regex)) {
                    listUser = userRepository.findAllByCccd(val);
                } else {
                    listUser = userRepository.findAllByName("%" + val + "%");
                }
            }
            List<UserDTO> listUserDTO = new ArrayList<>();
            for (UserEntity u : listUser) {
                if (!u.getUserType().equalsIgnoreCase("client")) {
                    continue;
                }
                AccountEntity account = accountRepository.findByUser(u);
                UserDTO udto = new UserDTO();
                udto.setUserId(u.getUserId());
                udto.setName(u.getName());
                udto.setGender(u.getGender());
                udto.setDob(u.getDob());
                udto.setPhoneNumber(u.getPhoneNumber());
                udto.setEmail(u.getEmail());
                udto.setCccd(u.getCccd());
                udto.setPassport(u.getPassport());
                udto.setAccount(account.getUsername());
                udto.setAddress(u.getAddress());
                udto.setUserType(u.getUserType());
                listUserDTO.add(udto);
            }
            return listUserDTO;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        get user có vai trò client bởi id
     */
    @GetMapping("/client")
    public UserDTO getClientById(@RequestParam("id") int id) {
        try {
            UserEntity u = userRepository.findById(id).get();
            AccountEntity account = accountRepository.findByUser(u);
            UserDTO udto = new UserDTO();
            udto.setUserId(u.getUserId());
            udto.setName(u.getName());
            udto.setGender(u.getGender());
            udto.setDob(u.getDob());
            udto.setPhoneNumber(u.getPhoneNumber());
            udto.setEmail(u.getEmail());
            udto.setCccd(u.getCccd());
            udto.setPassport(u.getPassport());
            udto.setAccount(account.getUsername());
            udto.setAddress(u.getAddress());
            udto.setUserType(u.getUserType());
            return udto;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Xoa client
     */
    @DeleteMapping("/client")
    public String deleteClient(@RequestParam("id") int id) {
        try {
            UserEntity u = userRepository.findById(id).get();
            AccountEntity account = accountRepository.findByUser(u);
            if (account != null) {
                accountRepository.delete(account);
            }
            userRepository.delete(u);
            return "OK";

        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        luu user
     */
    @PostMapping("/user")
    public UserEntity saveUser(@RequestBody AccountEntity account) {
        try {
            UserEntity user = account.getUser();
            UserEntity u = userRepository.findById(user.getUserId()).get();
            AccountEntity ac = accountRepository.findByUsername(account.getUsername());
            if (account.getImage().equals("")) {
                ac.setImage(ac.getImage());
            } else {
                ac.setImage(account.getImage());
            }
            user.setUserType("client");
            user.setSendMail(u.isSendMail());
            user = userRepository.save(user);
            ac.setUser(user);
            accountRepository.save(ac);
            return user;
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }
    
    @GetMapping("/number-client-month")
    public int getNumberClientInMonth(@RequestParam("month") int month){
        try {
            return accountRepository.getNumberClientInMonth(month);
        } catch (Exception e) {
            System.out.println(e);
        }
        return 0;
    }
    
    
    
}

class UserDTO {

    private int userId;
    private String name;
    private String gender;
    private Date dob;
    private String phoneNumber;
    private String email;
    private String cccd;
    private String passport;
    private String Address;
    private String userType;
    private String account;

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getCccd() {
        return cccd;
    }

    public void setCccd(String cccd) {
        this.cccd = cccd;
    }

    public String getPassport() {
        return passport;
    }

    public void setPassport(String passport) {
        this.passport = passport;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String Address) {
        this.Address = Address;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    public String getAccount() {
        return account;
    }

    public void setAccount(String account) {
        this.account = account;
    }
};
