var urlParams = new URLSearchParams(window.location.search);
var page = urlParams.get('page');
if (page == 'lsp') {
    nextPage(document.getElementsByClassName("menu--item").item(1), "content--infoBookedRoom");
}
showInfoUser();
function showInfoUser() {
    var username = localStorage.getItem("username");
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/account?username=" + username, requestOptions)
        .then(response => response.json())
        .then(result => {
            document.querySelector("#user-image-sidebar").src = result.image;
            document.querySelector("#user-name-sidebar").innerHTML = result.user.name;
            document.querySelector('#name').value = result.user.name;
            document.querySelector("#dob").value = result.user.dob;
            if (result.user.gender == 'Nam') {
                document.querySelector("#gender1").checked = true;
            }
            if (result.user.gender == 'Nữ') {
                document.querySelector("#gender2").checked = true;
            }
            document.querySelector("#id-user").value = result.user.userId;
            document.querySelector("#phoneNumber").value = result.user.phoneNumber;
            document.querySelector("#email").value = result.user.email;
            document.querySelector("#address").value = result.user.address;
            document.querySelector("#imageUser").src = result.image;
            document.querySelector("#username").value = result.username;
            document.querySelector("#cccd").value = result.user.cccd;
            document.querySelector("#passport").value = result.user.passport;
            document.querySelector("#accountId").value = result.accountId;
        })
        .catch(error => console.log('error', error));
}
function saveUser() {
    var imageUser = document.querySelector("#fileImage");
    if (imageUser.files == undefined) {
        saveInfoUser("");
    } else {
        var fReader = new FileReader();
        fReader.readAsDataURL(imageUser.files[0]);
        fReader.onload = function (e) {
            linkImage = e.target.result;
            saveInfoUser(linkImage);
        }
    }
}
function saveInfoUser(image) {
    var name = document.querySelector('#name').value;
    var dob = document.querySelector("#dob").value;
    var gender = "Nam";
    if (document.querySelector("#gender2").checked) {
        gender = "Nữ";
    }
    var id = document.querySelector("#id-user").value;
    var phoneNumber = document.querySelector("#phoneNumber").value;
    var email = document.querySelector("#email").value;
    var address = document.querySelector("#address").value;
    var username = document.querySelector("#username").value;
    var cccd = document.querySelector("#cccd").value;
    var passport = document.querySelector("#passport").value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "username": username,
        "image": image,
        "user": {
            "userId": id,
            "name": name,
            "gender": gender,
            "dob": dob,
            "phoneNumber": phoneNumber,
            "email": email,
            "cccd": cccd,
            "passport": passport,
            "address": address
        }
    });
    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/user", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError("Không thể lưu thông tin! vui lòng thử lại")
            } else {
                showAlertSuccess("Lưu thông tin thành công!")
                showInfoUser();
            }
        })
        .catch(error => console.log('error', error));
}
function changeImage(ele) {
    var fReader = new FileReader();
    fReader.readAsDataURL(ele.files[0]);
    fReader.onload = function (e) {
        linkImage = e.target.result;
        document.querySelector("#imageUser").src = linkImage;
    }
}
var check = true;
function checkPass() {
    var password = document.querySelector("#password").value;
    var username = localStorage.getItem("username");
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
            if (result.code == '200') {
                check = false;
            } else {
                showAlertError("Mật khẩu không chính xác");
                check = true;
            }
        })
        .catch(error => console.log('error', error));
}
function saveAccount() {
    var username = localStorage.getItem("username");
    var newPassword = document.querySelector("#newPassword").value;
    var rePassword = document.querySelector("#rePassword").value;
    var accountId = document.querySelector("#accountId").value;
    if (check) {
        showAlertError("Mật khẩu không chính xác");
    } else if (newPassword != rePassword) {
        showAlertError("Mật khẩu không khớp");
    } else {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "accountId": accountId,
            "username": username,
            "password": newPassword
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
                    showAlertSuccess(result.data);
                } else {
                    showAlertError(result.data);
                }
            })
            .catch(error => console.log('error', error));
    }

}
function nextPage(ele, item) {
    console.log(ele.value);
    for (var e of document.querySelectorAll(".menu--item")) {
        e.classList.remove("menu--item__active");
    }
    for (var el of document.querySelectorAll(".content-i")) {
        el.style.display = 'none';
    }
    ele.classList.add("menu--item__active");
    document.querySelector("." + item).style.display = 'block';
    if (item == 'content--infoBookedRoom') {
        showAllBooking();
    }
    if (item == 'content--infoUseService') {
        showAllUseService();
    }
}
function showAllBooking() {
    var username = localStorage.getItem("username");
    var myHeaders = new Headers();
    myHeaders.append("type", "user");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-booking?val=" + username, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var b of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ formatDate(b.bookingDate) + `</td>
                <td>`+ formatDate(b.checkInDate) + `</td>
                <td>`+ formatDate(b.checkOutDate) + `</td>
                <td>`+ formatMoneyVND(b.roomPrice) + `</td>
                <td>`+ formatMoneyVND(b.bookingDeposit) + `</td>
                <td>`+ b.bookingStatus + `</td>
                <td>`;
                if (b.bookingStatus == 'Đã đặt')
                    html += `<button type="button" class="btn btn-light" onclick="cancelBooking(` + b.bookingId + `)">Huỷ</button>`;
                if (b.bookingStatus == 'Đã trả')
                    html += `<button type="button" class="btn btn-warning" onclick="danhgiaBooking(` + b.bookingId + `)">Đánh giá</button>`;
                html += `</td>
                </tr>`;
            }
            document.querySelector("#data-booking").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
function cancelBooking(idBooking) {
    var mes = `Bạn muốn huỷ đặt phòng?
    Lưu ý: chính sách huỷ phòng:
    - Trước 7 ngày hoàn 100% đặt cọc
    - Trước 3 ngày hoàn 50% đặt cọc
    - Sau 3 ngày không hoàn cọc`;
    if (confirm(mes)) {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "bookingId": idBooking,
            "bookingStatus": "Huỷ"
        });

        var requestOptions = {
            method: 'POST',
            headers: myHeaders,
            body: raw,
            redirect: 'follow'
        };

        fetch(url + "/api/booking", requestOptions)
            .then(response => response.json())
            .then(result => {
                if (result != null) {
                    showAlertSuccess("Huỷ đặt phòng thành công");
                    showAllBooking();
                } else {
                    showAlertError("Lỗi! Vui lòng thử lại");
                }
            })
            .catch(error => console.log('error', error));
    }
}
function showAllUseService() {
    var username = localStorage.getItem("username");
    var myHeaders = new Headers();
    myHeaders.append("type", "username");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/get-list-use-service-by-user?id=" + username, requestOptions)
        .then(response => response.json())
        .then(result => {
            var count = 0;
            var html = '';
            for (var us of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ formatDate(us.dateUse) + `</td>
                <td>`+ us.service.serviceName + `</td>
                <td>`+ formatMoneyVND(us.servicePrice) + `</td>
                <td>`+ us.serviceQuantity + `</td>
                <td>`+ formatMoneyVND(us.servicePrice * us.serviceQuantity) + `</td>
                <td>`+ us.useServiceStatus + `</td>
                <td>`
                if (us.useServiceStatus == 'Đã đặt')
                    html += `<button type="button" class="btn btn-light" onclick="huyUseService(` + us.useServiceId + `)">Huỷ</button>`
                html += `</td>
                </tr>`;
            }
            document.querySelector("#data-use-service").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
function huyUseService(idUseService) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "useServiceId": idUseService,
        "useServiceStatus": "Huỷ"
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/use-service", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result != '') {
                showAlertSuccess("Huỷ đặt dịch vụ thành công!");
                showAllUseService();
            } else {
                showAlertError("Lỗi! Vui lòng thử lại");
            }
        })
        .catch(error => console.log('error', error));
}

function clickStar(star) {
    var starParent = star.parentElement.parentElement;
    var listStar = starParent.getElementsByClassName('point__star');
    starParent.getElementsByClassName('evaluate--item--main--point--num').item(0).value = star.title;
    switch (star.title) {
        case "1":
            starParent.getElementsByClassName('evaluate--item--main--point--text').item(0).innerHTML = "Tệ";
            break;
        case "2":
            starParent.getElementsByClassName('evaluate--item--main--point--text').item(0).innerHTML = "Không hài lòng";
            break;
        case "3":
            starParent.getElementsByClassName('evaluate--item--main--point--text').item(0).innerHTML = "Bình thường";
            break;
        case "4":
            starParent.getElementsByClassName('evaluate--item--main--point--text').item(0).innerHTML = "Hài lòng";
            break;
        case "5":
            starParent.getElementsByClassName('evaluate--item--main--point--text').item(0).innerHTML = "Tuyệt vời";
            break;
    }
    for (var s of listStar) {
        if (s.title <= star.title) {
            s.classList.add('point__star__yellow')
        } else {
            s.classList.remove('point__star__yellow')
        }
    }
}

function danhgiaBooking(idBooking) {
    document.querySelector("#idBookingFormDanhGia").value = idBooking;
    document.querySelector("#mesDanhGia").value = '';
    clickStar(document.getElementsByClassName("point__star").item(4));
    openPopup("popup-danhGia");
}
var form_danhGia = document.querySelector("#form-danhGia")
form_danhGia.addEventListener("submit", function (event) {
    event.preventDefault();
    var idBooking = document.querySelector("#idBookingFormDanhGia").value;
    var mesDanhGia = document.querySelector("#mesDanhGia").value;
    var point = document.querySelector(".evaluate--item--main--point--num").value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "ratingPoint": point,
        "message":mesDanhGia,
        "booking": {
            "bookingId": idBooking
        }
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url+"/api/rating", requestOptions)
        .then(response => response.json())
        .then(result => {
            if(result != ''){
                showAlertSuccess("Đã đánh giá");
                closePopup('popup-danhGia');
                showAllBooking();
            } else {
                showAlertError("Lỗi! Vui lòng thử lại");
            }
        })
        .catch(error => console.log('error', error));
})