document.querySelector("#form-email-forgotpassword").style.display = "block";
function sendMail() {
    var email = document.querySelector("#email").value;
    localStorage.setItem("emailForgotPassword", email);
    var myHeaders = new Headers();
    myHeaders.append("type", "send");
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "email": email
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/forgot-password", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result.code == '200') {
                showAlertSuccess(result.data);
                setTimeout(function () {
                    document.querySelector("#btn-re-send-mail").disabled = true;
                    document.querySelector("#form-email-forgotpassword").style.display = "none";
                    document.querySelector("#form-otp-forgot-password").style.display = "block";
                    document.querySelector('#form-password-forgot-password').style.display = "none";
                }, 1500);
                setTimeout(function () {
                    document.querySelector("#btn-re-send-mail").disabled = false;
                }, 180000);
            } else {
                showAlertError(result.data);
            }
        })
        .catch(error => console.log('error', error));
}
function sendOTP() {
    var otp = document.querySelector("#otp1").value;
    var email = localStorage.getItem("emailForgotPassword");

    var myHeaders = new Headers();
    myHeaders.append("type", "otp");
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "email": email,
        "otp": otp
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/forgot-password", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result.code == '200') {
                localStorage.setItem("accountId", result.data);
                localStorage.removeItem("emailForgotPassword");
                document.querySelector("#form-email-forgotpassword").style.display = "none";
                document.querySelector("#form-otp-forgot-password").style.display = "none";
                document.querySelector('#form-password-forgot-password').style.display = "block";
            } else {
                showAlertError(result.data);
            }
        })
        .catch(error => console.log('error', error));
}

function sendPassword() {
    var password = document.querySelector("#password").value;
    var repassword = document.querySelector("#repassword").value;
    if (password != repassword) {
        showAlertError("Mật khẩu không trùng khớp!");
    } else {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "accountId": localStorage.getItem("accountId"),
            "password": password
        });

        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };

        fetch(url + "/password", requestOptions)
            .then(response => response.json())
            .then(result => {
                if (result.code == '200') {
                    localStorage.removeItem("accountId");
                    showAlertSuccess(result.data);
                    setTimeout(function () {
                        window.location.replace("/page/login.html");
                    }, 2000)
                } else {
                    showAlertError(result.data);
                }
            })
            .catch(error => console.log('error', error));
    }
}




function sendMail2() {
    var email = localStorage.getItem("emailForgotPassword");
    var myHeaders = new Headers();
    myHeaders.append("type", "send");
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "email": email
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/forgot-password", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result.code == '200') {
                showAlertSuccess(result.data);
                setTimeout(function () {
                    document.querySelector("#btn-re-send-mail").disabled = true;
                    document.querySelector("#form-email-forgotpassword").style.display = "none";
                    document.querySelector("#form-otp-forgot-password").style.display = "block";
                    document.querySelector('#form-password-forgot-password').style.display = "none";
                }, 1500);
                setTimeout(function () {
                    document.querySelector("#btn-re-send-mail").disabled = false;
                }, 180000);
            } else {
                showAlertError(result.data);
            }
        })
        .catch(error => console.log('error', error));
}