var check = false;
function checkUsername(ele) {
    var username = ele.value;
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/check-account?username=" + username, requestOptions)
        .then(response => response.json())
        .then(result => {
            if (!result) {
                check = false;
                showAlertError("Tài khoản đã tồn tại!");
            } else {
                check = true;
            }
        })
        .catch(error => console.log('error', error));
}

var form_signup = document.querySelector("#form-signup");
form_signup.addEventListener("submit", function (event) {
    event.preventDefault();
    var name = document.querySelector("#name").value;
    var phonenumber = document.querySelector("#phonenumber").value;
    var email = document.querySelector("#email").value;
    var username = document.querySelector("#username").value;
    var password = document.querySelector("#password").value;
    var repassword = document.querySelector("#repassword").value;
    var sendEmail = document.querySelector("#sendEmail").checked;
    if (!check) {
        showAlertError("Tài khoản đã tồn tại!");
    } else if (password != repassword) {
        showAlertError("Mật khẩu không khớp!")
    } else {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "username": username,
            "password": password,
            "user": {
                "name": name,
                "gender": "Nam",
                "phoneNumber": phonenumber,
                "email": email,
                "sendMail": sendEmail
            }
        });

        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };

        fetch(url + "/signup", requestOptions)
            .then(response => response.json())
            .then(result => {
                if (result.code == '200') {
                    showAlertSuccess(result.data);
                    setTimeout(() => {
                        window.location.replace("/page/login.html");
                    }, 2000);
                } else {
                    showAlertError(result.data);
                }
            })
            .catch(error => console.log('error', error));
    }
})