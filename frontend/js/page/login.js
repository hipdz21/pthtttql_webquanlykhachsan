form_login = document.querySelector("#form-login")
form_login.addEventListener("submit", function (event) {
    event.preventDefault();
    var username = document.querySelector("#username").value;
    var password = document.querySelector("#password").value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "username": username,
        "password": password
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/login", requestOptions)
        .then(response => response.json())
        .then(result => {
            console.log(result);
            if (result.code == '200') {
                let [usern, accountType] = result.data.split('|')
                localStorage.setItem("username", usern);
                localStorage.setItem("accountType", accountType);
                showAlertSuccess("Đăng nhập thành công!")
                setTimeout(function () {
                    if (accountType == 'admin') {
                        window.location.replace("/page/admin.html");
                    } else if (accountType == 'front_desk_staff') {
                        window.location.replace("/page/leTan.html");
                    }
                    else {
                        window.location.replace("/index.html");
                    }
                }, 2000);
            } else {
                showAlertError("Lỗi! Kiểm tra lại tài khoản và mật khẩu");
            }
        })
        .catch(error => console.log('error', error));
})