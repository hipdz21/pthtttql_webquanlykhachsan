/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package dv.quang.hotel.controller;

import dv.quang.hotel.entity.AccountEntity;
import dv.quang.hotel.entity.UserEntity;
import dv.quang.hotel.repository.AccountRepository;
import dv.quang.hotel.repository.UserRepository;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ConcurrentHashMap;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 *
 * @author dovan
 */
@CrossOrigin
@RestController
public class HomeController {

    private Map<String, String> cache = new ConcurrentHashMap<>();
    private Timer timer = new Timer();

    @Autowired
    AccountRepository accountRepository;

    @Autowired
    SendMail sendMail;

    @Autowired
    UserRepository userRepository;

//    @GetMapping("/send")
//    public void sendmailll() {
//        String header = "<div class=\"gmail-header\" style=\"color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
//                + "    <p\n"
//                + "        style=\"margin: 0px; font-size: 24px; font-weight: 700; color: rgb(255, 255, 255); background-color: rgb(218, 103, 23); padding: 24px;\">\n"
//                + "        Felicity</p>\n"
//                + "</div>";
//        String content = "<div class=\"gmail-content\"\n"
//                + "    style=\"padding: 12px 24px; color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
//                + "    <p style=\"margin: 4px;\">Xin chào,</p>\n"
//                + "    <p style=\"margin: 4px;\">Mã OTP của bạn là:&nbsp;<strong>123456</strong></p>\n"
//                + "    <p style=\"margin: 4px;\">Mã OTP có hiệu lực 5 phút</p>\n"
//                + "    <p style=\"margin: 4px;\">Nếu bạn không thực hiện cấp lại mật khẩu, vui lòng bỏ qua mail này.</p>\n"
//                + "</div>";
//        String footer = "<div class=\"gmail-footer\"\n"
//                + "    style=\"color: rgb(255, 255, 255); background-color: rgb(0, 0, 0); padding: 24px; text-align: center; font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
//                + "    <p class=\"gmail-footer--title\" style=\"margin: 0px; font-size: 24px; font-weight: 700;\">Felicity</p>\n"
//                + "    <p class=\"gmail-footer--content\" style=\"margin: 4px 4px 12px; font-size: 18px; font-weight: 600;\">Nơi nghỉ dưỡng\n"
//                + "        tuyệt vời để tìm thấy sự yên bình và sự thư thái</p>\n"
//                + "    <p style=\"margin: 4px;\">Address: 100 Giảng Võ, Ba Đình, Hà Nội</p>\n"
//                + "    <p style=\"margin: 4px;\">Tel: (+84) 24 6666 8888 | Fax: (+84) 24 9999 8888</p>\n"
//                + "    <p style=\"margin: 4px;\">Mail: reservation@felicity.com</p>\n"
//                + "</div>";
//        String mes = header + content + footer;
//        sendMail.send("saokhong2k1@gmail.com", "Thu gui html", mes);
//    }

    /*
        Dang nhap
     */
    @PostMapping("/login")
    public Response login(@RequestBody AccountEntity account) {
        try {
            Response response = new Response();
            response.setCode("404");
            response.setData("Tài khoản hoặc mật khẩu không hợp lệ!");
            AccountEntity accountSearch = accountRepository.findByUsername(account.getUsername());
            if (accountSearch == null) {
                return response;
            } else {
                if (accountSearch.getPassword().equals(MD5Encryption.encrypt(account.getPassword()))) {
                    response.setCode("200");
                    response.setData(accountSearch.getUsername() + "|" + accountSearch.getAccountType());
                }
                return response;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        return null;
    }

    /*
        Gui mail lay otp va xac nhan otp
     */
    @PostMapping("/forgot-password")
    public Response forgotPassword(HttpServletRequest request, @RequestBody ForgotPass forgotPass) {
        Response response = new Response();
        response.setCode("404");
        try {
            String type = request.getHeader("type");
            if (type.equalsIgnoreCase("send")) {
                UserEntity user = userRepository.findByEmail(forgotPass.getEmail());
                if (user == null) {
                    response.setData("Không tìm thấy tài khoản!");
                    return response;
                }
                String otp = generateOtp();
                while (true) {
                    if (cache.get(otp) == null) {
                        break;
                    }
                    otp = generateOtp();
                }
                put(otp, forgotPass.getEmail(), 5 * 60 * 1000);
                String header = "<div class=\"gmail-header\" style=\"color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                        + "    <p\n"
                        + "        style=\"margin: 0px; font-size: 24px; font-weight: 700; color: rgb(255, 255, 255); background-color: rgb(218, 103, 23); padding: 24px;\">\n"
                        + "        Felicity</p>\n"
                        + "</div>";
                String content = "<div class=\"gmail-content\"\n"
                        + "    style=\"padding: 12px 24px; color: rgb(0, 0, 0); font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                        + "    <p style=\"margin: 4px;\">Xin chào,</p>\n"
                        + "    <p style=\"margin: 4px;\">Mã OTP của bạn là:&nbsp;<strong>" + otp + "</strong></p>\n"
                        + "    <p style=\"margin: 4px;\">Mã OTP có hiệu lực 5 phút</p>\n"
                        + "    <p style=\"margin: 4px;\">Nếu bạn không thực hiện cấp lại mật khẩu, vui lòng bỏ qua mail này.</p>\n"
                        + "</div>";
                String footer = "<div class=\"gmail-footer\"\n"
                        + "    style=\"color: rgb(255, 255, 255); background-color: rgb(0, 0, 0); padding: 24px; text-align: center; font-family: &quot;Times New Roman&quot;; font-size: medium;\">\n"
                        + "    <p class=\"gmail-footer--title\" style=\"margin: 0px; font-size: 24px; font-weight: 700;\">Felicity</p>\n"
                        + "    <p class=\"gmail-footer--content\" style=\"margin: 4px 4px 12px; font-size: 18px; font-weight: 600;\">Nơi nghỉ dưỡng\n"
                        + "        tuyệt vời để tìm thấy sự yên bình và sự thư thái</p>\n"
                        + "    <p style=\"margin: 4px;\">Address: 100 Giảng Võ, Ba Đình, Hà Nội</p>\n"
                        + "    <p style=\"margin: 4px;\">Tel: (+84) 24 6666 8888 | Fax: (+84) 24 9999 8888</p>\n"
                        + "    <p style=\"margin: 4px;\">Mail: reservation@felicity.com</p>\n"
                        + "</div>";
                String mes = header + content + footer;
                sendMail.send(forgotPass.getEmail(), "Felicity - Mã xác nhận lấy lại mật khẩu", mes);
                response.setCode("200");
                response.setData("Gửi mã xác nhận thành công!");
                return response;
            } else if (type.equalsIgnoreCase("otp")) {
                String mail = get(forgotPass.getOtp());
                if (mail != null && mail.equals(forgotPass.getEmail())) {
                    remove(forgotPass.getOtp());
                    UserEntity user = userRepository.findByEmail(forgotPass.getEmail());
                    AccountEntity account = accountRepository.findByUser(user);
                    response.setCode("200");
                    response.setData(account.getAccountId() + "");
                    return response;
                }
                response.setData("Mã xác nhận không chính xác! Vui lòng thử lại");
                return response;
            }
        } catch (Exception e) {
            System.out.println(e);
        }
        response.setData("Lỗi! Vui lòng thử lại");
        return response;
    }

    /*
        Doi mat khau
     */
    @PostMapping("/password")
    public Response changePassword(@RequestBody AccountEntity account) {
        Response response = new Response();
        response.setCode("404");
        response.setData("Lỗi! Vui lòng thử lại");
        try {
            AccountEntity accountEntity = accountRepository.findById(account.getAccountId()).get();
            accountEntity.setPassword(MD5Encryption.encrypt(account.getPassword()));
            accountRepository.save(accountEntity);
            response.setCode("200");
            response.setData("Đổi mật khẩu thành công");
            return response;
        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }

    /*
        Kiểm tra tài khoản đã tồn tại chưa
     */
    @GetMapping("/check-account")
    public Boolean checkAccount(@RequestParam("username") String username) {
        try {
            AccountEntity account = accountRepository.findByUsername(username);
            if (account == null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }

    @PostMapping("/signup")
    public Response signup(@RequestBody AccountEntity account) {
        Response response = new Response();
        response.setCode("404");
        response.setData("Lỗi! Vui lòng thử lại");
        try {
            UserEntity user = account.getUser();
            user.setUserType("client");
            user = userRepository.save(user);
            account.setUser(user);
            account.setPassword(MD5Encryption.encrypt(account.getPassword()));
            account.setCreationDate(new Date());
            account.setAccountType("client");
            account.setImage("../images/user.png");
            accountRepository.save(account);
            response.setCode("200");
            response.setData("Đăng ký thành công!");
            return response;
        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }


    /*
        Tạo OTP 6 số
     */
    private String generateOtp() {
        Random random = new Random();
        int otpLength = 6;
        StringBuilder otp = new StringBuilder();

        for (int i = 0; i < otpLength; i++) {
            otp.append(random.nextInt(10));
        }

        return otp.toString();
    }

    private void put(String key, String value, long expireTimeMs) {
        cache.put(key, value);
        timer.schedule(new RemoveTask(key), expireTimeMs);
    }

    private String get(String key) {
        return cache.get(key);
    }

    private void remove(String key) {
        cache.remove(key);
    }

    private class RemoveTask extends TimerTask {

        private String key;

        public RemoveTask(String key) {
            this.key = key;
        }

        @Override
        public void run() {
            remove(key);
        }
    }
}

class Response {

    private String code;
    private String data;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}

class ForgotPass {

    private String email;
    private String otp;

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    @Override
    public String toString() {
        return "ForgotPass{" + "email=" + email + ", otp=" + otp + '}';
    }
}
