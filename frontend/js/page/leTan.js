showAllRoom();
function nextPage(ele, classEle) {
    menu_items = document.getElementsByClassName('menu--item')
    for (menu_item of menu_items) {
        menu_item.classList.remove('menu--item__active')
    }
    ele.classList.add('menu--item__active')
    for (content_main of document.getElementsByClassName('content--main')) {
        content_main.style.display = 'none'
    }
    for (var content_item of document.getElementsByClassName("content--item")) {
        content_item.style.display = 'none';
    }
    document.getElementsByClassName(classEle).item(0).style.display = 'block';
    if (classEle == 'content--home') {
        localStorage.removeItem("IDCLIENT");
        showAllRoom();
    }
    if (classEle == 'content--booking') {
        showPageBooking();
    }
    if (classEle == 'content--client') {
        showAllClient();
    }
    if (classEle == 'content--useService') {
        showPageUseService();
        showAllService();
    }
    if (classEle == 'content--contact') {
        showAllContact();
    }
}



//Js cho phan home ==============================================
function showAllRoom() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-room?val", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            for (var r of result) {
                html += `<div class="room--item" ondblclick="openPopupLichSuThue(` + r.roomId + `)">
                <div class="room--name">`+ r.roomName + `</div>
                <div class="room--image"><img src="../images/home.png" alt=""></div>
                <div class="room--type--name">`+ r.roomType.nameRoomType + `</div>
                <div class="room--rate">`+ formatMoneyVND(r.roomType.roomRate) + `</div>`;
                if (r.roomStatus == "Phòng trống")
                    html += `<div class="room--status t__green"><i class="fa-solid fa-circle"></i></div>`;
                else if (r.roomStatus == "Phòng bận")
                    html += `<div class="room--status t__red"><i class="fa-solid fa-circle"></i></div>`;
                else
                    html += `<div class="room--status t__gray"><i class="fa-solid fa-circle"></i></div>`;
                html += `</div>`;
            }
            document.querySelector(".room--items").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
function openPopupLichSuThue(idRoom) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/get-list-booking-by-room?id=" + idRoom, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var b of result) {
                count++;
                html += `<tr>
                <td scope="row">`+ count + `</td>
                <td>`+ formatDate(b.checkInDate) + `</td>
                <td>`+ formatDate(b.checkOutDate) + `</td>
                <td>Họ Tên: `+ b.client.name + `<br>Số điện thoại: ` + b.client.phoneNumber + `</td>
                <td>`
                if (b.bookingStatus == 'Đã nhận')
                    html += `<button type="button" class="btn btn-primary" onclick="pickClientUseService(` + b.user.userId + `)">Sử dụng dịch vụ</button>`
                html += `</td>
                </tr>`;
            }
            document.querySelector("#data-lichSuThue").innerHTML = html;
            openPopup("popup-lichSuThue");
        })
        .catch(error => console.log('error', error));
}




//Js cho phan khach hang ==============================================
//Mo form add client
function openAddClient() {
    document.querySelector("#idClient").value = '';
    document.querySelector("#nameClient").value = '';
    document.querySelector("#genderClient1").checked = true;
    document.querySelector("#dobClient").value = '';
    document.querySelector("#phoneNumberClient").value = '';
    document.querySelector("#emailClient").value = '';
    document.querySelector("#cccdClient").value = '';
    document.querySelector("#passportClient").value = '';
    document.querySelector("#addressClient").value = '';
    openPopup('popup-Client');
}
//Luu client
function saveClient() {
    var idClient = document.querySelector("#idClient").value;
    var nameClient = document.querySelector("#nameClient").value;
    var dobClient = document.querySelector("#dobClient").value;
    var phoneNumberClient = document.querySelector("#phoneNumberClient").value;
    var emailClient = document.querySelector("#emailClient").value;
    var cccdClient = document.querySelector("#cccdClient").value;
    var passportClient = document.querySelector("#passportClient").value;
    var addressClient = document.querySelector("#addressClient").value;
    var genderClient = document.getElementsByName("genderClient");
    var gender = "Nam";
    for (var g of genderClient) {
        if (g.checked) {
            gender = g.value;
        }
    }

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "userId": idClient,
        "name": nameClient,
        "gender": gender,
        "dob": dobClient,
        "phoneNumber": phoneNumberClient,
        "email": emailClient,
        "cccd": cccdClient,
        "passport": passportClient,
        "address": addressClient
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/client", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                closePopup('popup-Client');
                // Show all client
                showAllClient();
            }
        })
        .catch(error => console.log('error', error));
}
//Su kien submit form
form_add_client = document.querySelector("#form-add-client")
form_add_client.addEventListener("submit", function (event) {
    event.preventDefault();
    saveClient();
})
//Show all client
function showAllClient() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-client?val", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var c of result) {
                // <td>`+ c.address + `</td>
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ c.name + `</td>
                <td>`+ c.account + `</td>
                <td>`+ formatDate(c.dob) + `</td>
                
                <td>`+ c.cccd + `</td>
                <td>`+ c.phoneNumber + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateClient(`+ c.userId + `)">Sửa</button>
                    <button type="button" class="btn btn-primary" onclick="pickClientBooking(`+ c.userId + `)">Thuê phòng</button>
                    <button type="button" class="btn btn-primary" onclick="pickClientUseService(`+ c.userId + `)">SDDV</button>
                </td>
                </tr>`;
            }
            document.querySelector("#data-client").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
//update client
function updateClient(idClient) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/client?id=" + idClient, requestOptions)
        .then(response => response.json())
        .then(result => {
            openAddClient();
            document.querySelector("#idClient").value = result.userId;
            document.querySelector("#nameClient").value = result.name;
            if (result.gender == 'Nam')
                document.querySelector("#genderClient1").checked = true;
            else
                document.querySelector("#genderClient2").checked = true;
            document.querySelector("#dobClient").value = result.dob;
            document.querySelector("#phoneNumberClient").value = result.phoneNumber;
            document.querySelector("#emailClient").value = result.email;
            document.querySelector("#cccdClient").value = result.cccd;
            document.querySelector("#passportClient").value = result.passport;
            document.querySelector("#addressClient").value = result.address;
        })
        .catch(error => console.log('error', error));
}
// search client
function searchClient(val) {
    var myHeaders = new Headers();
    myHeaders.append("type", "search");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-client?val=" + val, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var c of result) {
                // <td>`+ c.address + `</td>
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ c.name + `</td>
                <td>`+ c.account + `</td>
                <td>`+ formatDate(c.dob) + `</td>

                <td>`+ c.cccd + `</td>
                <td>`+ c.phoneNumber + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateClient(`+ c.userId + `)">Sửa</button>
                    <button type="button" class="btn btn-primary" onclick="pickClientBooking(`+ c.userId + `)">Thuê phòng</button>
                    <button type="button" class="btn btn-primary" onclick="pickClientUseService(`+ c.userId + `)">SDDV</button>
                </td>
                </tr>`;
            }
            document.querySelector("#data-client").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// Sự kiện nhấn enter nút search
input_search_client = document.querySelector("#input-search-client")
input_search_client.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        searchClient(input_search_client.value);
    }
})

function pickClientBooking(idClient) {
    localStorage.setItem("IDCLIENT", idClient);
    nextPage(document.getElementsByClassName("menu--item").item(1), "content--booking");
}
function pickClientUseService(idClient) {
    localStorage.setItem("IDCLIENT", idClient);
    closePopup("popup-lichSuThue");
    nextPage(document.getElementsByClassName("menu--item").item(2), "content--useService");
}


//Js cho phan booking ==============================================
function showPageBooking() {
    var idClient = localStorage.getItem("IDCLIENT");
    if (idClient == null) {
        document.querySelector(".booking--infoUser__N").style.display = "block";
        document.querySelector(".booking--infoUser__Y").style.display = "none";
        document.querySelector(".booking--table").style.display = "none";
    } else {
        document.querySelector(".booking--infoUser__N").style.display = "none";
        document.querySelector(".booking--infoUser__Y").style.display = "block";
        document.querySelector(".booking--table").style.display = "block";
        showInfoUser();
        showBookingByClient()
    }
}
function showInfoUser() {
    var idClient = localStorage.getItem("IDCLIENT");
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/client?id=" + idClient, requestOptions)
        .then(response => response.json())
        .then(result => {
            document.querySelector('#name').value = result.name;
            document.querySelector("#dob").value = result.dob;
            if (result.gender == 'Nam') {
                document.querySelector("#gender1").checked = true;
            }
            if (result.gender == 'Nữ') {
                document.querySelector("#gender2").checked = true;
            }
            document.querySelector("#id-user").value = result.userId;
            document.querySelector("#phoneNumber").value = result.phoneNumber;
            document.querySelector("#email").value = result.email;
            document.querySelector("#address").value = result.address;
            document.querySelector("#cccd").value = result.cccd;
            document.querySelector("#passport").value = result.passport;
        })
        .catch(error => console.log('error', error));
}
function showBookingByClient() {
    var idClient = localStorage.getItem("IDCLIENT");
    var Enum = ['Phòng đơn', 'Phòng đôi', 'Phòng gia đình', 'Suite', 'Deluxe', 'Executive'];
    var myHeaders = new Headers();
    myHeaders.append("type", "client");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-booking?val=" + idClient, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var b of result) {
                count++;
                var roomname = Enum[b.roomTypeId];
                if (b.bookingStatus == 'Đã nhận' || b.bookingStatus == 'Đã trả'){
                    roomname = b.room.roomName
                }
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ formatDate(b.bookingDate) + `</td>
                <td>`+ formatDate(b.checkInDate) + `</td>
                <td>`+ formatDate(b.checkOutDate) + `</td>
                <td>`+ roomname + `</td>
                <td>`+ formatMoneyVND(b.roomPrice) + `</td>
                <td>`+ formatMoneyVND(b.bookingDeposit) + `</td>
                <td>`+ b.bookingStatus + `</td>
                <td>`;
                if (b.bookingStatus == 'Đã đặt') {
                    html += `<button type="button" class="btn btn-light"
                        onclick="huyPhong(`+ b.bookingId + `)">Huỷ</button>
                    <button type="button" class="btn btn-success" onclick="nhanPhong(`+ b.bookingId + `)">Nhận
                        phòng</button>`}
                if (b.bookingStatus == 'Đã nhận') {
                    html += `<button type="button" class="btn btn-warning" onclick="traPhong(` + b.bookingId + `)">Trả
                    phòng</button>`
                }
                html += `</td>
                </tr>`;
            }
            document.querySelector(".booking--table--roomtype").style.display = 'none';
            document.querySelector("#data-booking").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}

function thuePhong(room) {
    var idClient = localStorage.getItem("IDCLIENT");
    if (idClient == null) {
        showAlertError("Chưa chọn khách hàng");
    } else {
        var numberPeople = document.querySelector("#numberPeople").value;
        var timeCheckIn = document.querySelector("#timeCheckIn").value;
        var timeCheckOut = document.querySelector("#timeCheckOut").value;
        var name = document.querySelector('#name').value;
        var dob = document.querySelector("#dob").value;
        var gender = "Nam";
        if (document.querySelector("#gender2").checked) {
            gender = "Nữ";
        }
        var id_user = document.querySelector("#id-user").value;
        var phoneNumber = document.querySelector("#phoneNumber").value;
        var email = document.querySelector("#email").value;
        var address = document.querySelector("#address").value;
        var cccd = document.querySelector("#cccd").value;
        var passport = document.querySelector("#passport").value;
        var bookingNote = document.querySelector("#bookingNote").value;

        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "bookingId": 0,
            "checkInDate": setTimeCheckIn(timeCheckIn),
            "checkOutDate": setTimeCheckOut(timeCheckOut),
            "bookingNote": bookingNote,
            "roomTypeId": room,
            "numberPeople": numberPeople,
            "user": {
                "userId": id_user
            },
            "client": {
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
        console.log(raw);
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
                    showAlertSuccess("Đặt phòng thành công!");
                    localStorage.removeItem("timeCheckIn");
                    localStorage.removeItem("timeCheckOut");
                    localStorage.removeItem("numberPeople");
                    showPageBooking();
                } else {
                    showAlertError("Lỗi! Vui lòng thử lại");
                }
            })
            .catch(error => console.log('error', error));
    }
}

function timPhong() {
    var timeCheckIn = document.querySelector("#timeCheckIn").value;
    var timeCheckOut = document.querySelector("#timeCheckOut").value;
    var numberPeople = document.querySelector("#numberPeople").value;
    if (numberPeople == '') {
        numberPeople = 2;
    }
    var now = new Date();
    if (timeCheckIn > timeCheckOut) {
        showAlertError("Ngày đi phải lớn hơn ngày đến");
    } else if (timeCheckIn == timeCheckOut) {
        showAlertError("Phải chọn tối thiểu 1 ngày");
    } else if (setTimeEnd(timeCheckIn) < now) {
        showAlertError("Ngày đến không được trong quá khứ");
    } else {
        localStorage.setItem("timeCheckIn", timeCheckIn);
        localStorage.setItem("timeCheckOut", timeCheckOut);
        localStorage.setItem("numberPeople", numberPeople);
        searchRoom(timeCheckIn, timeCheckOut, numberPeople);
    }
}
function searchRoom(timeCheckIn, timeCheckOut, numberPeople) {
    timeCheckIn = setTimeCheckIn(timeCheckIn);
    timeCheckOut = setTimeCheckOut(timeCheckOut);
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "checkInDate": timeCheckIn,
        "checkOutDate": timeCheckOut,
        "numberPeople": numberPeople
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/search-list-room-type", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var r of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ r.nameRoomType + `</td>
                <td>` + formatMoneyVND(r.roomRate) + `</td>
                <td>`+ r.numberAdult + `</td>
                <td>`+ r.numberChild + `</td>
                <td>`+ r.bed + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="thuePhong(`+ r.roomTypeId + `)">Thuê phòng</button>
                </td>
                </tr>`;
            }
            document.querySelector(".booking--table--roomtype").style.display = 'block';
            document.querySelector(".booking--table").style.display = 'none';
            document.querySelector("#dataRoomType").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}

function huyPhong(idBooking) {
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
            if (result != '') {
                showAlertSuccess("Đã huỷ đơn đặt phòng!");
                showBookingByClient();
            } else {
                showAlertError("Lỗi! Vui lòng thử lại")
            }
        })
        .catch(error => console.log('error', error));
}

function nhanPhong(idBooking) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "bookingId": idBooking
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/get-list-room-by-booking", requestOptions)
        .then(response => response.json())
        .then(result => {

            var html = '';
            for (var r of result) {
                html += `<option value="` + r.roomId + `">Phòng ` + r.roomName + `</option>`
            }
            document.querySelector("#idBookingNhanPhong").value = idBooking;
            document.querySelector("#roomTypeName").value = result[0].roomType.nameRoomType;
            document.querySelector("#pickRoomNhanPhong").innerHTML = html;
            openPopup("popup-nhanPhong");
        })
        .catch(error => console.log('error', error));
}
var form_nhanPhong = document.querySelector("#form-nhanPhong");
form_nhanPhong.addEventListener("submit", function (event) {
    event.preventDefault();
    var idBooking = document.querySelector("#idBookingNhanPhong").value;
    var idRoom = document.querySelector("#pickRoomNhanPhong").value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "bookingId": idBooking,
        "bookingStatus": "Đã nhận",
        "room": {
            "roomId": idRoom
        }
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
            if (result != '') {
                showAlertSuccess("Đã nhận phòng");
                closePopup("popup-nhanPhong");
                showBookingByClient();
            } else {
                showAlertError("Lỗi! Vui lòng thử lại");
            }
        })
        .catch(error => console.log('error', error));
})

function traPhong(idBooking) {
    document.querySelector("#idBookingTraPhong").value = idBooking;
    openPopup("popup-traPhong");
}
function updateTienPhat(ele) {
    var num = Number(ele.value)
    document.querySelector(".tienPhat").innerHTML = `Tổng tiền cần thanh toán: ` + (formatMoneyVND(num));
}
var form_traPhong = document.querySelector("#form-traPhong");
form_traPhong.addEventListener("submit", function (event) {
    event.preventDefault();
    var idBookingTraPhong = document.querySelector("#idBookingTraPhong").value;
    var inputTienPhat = document.querySelector("#inputTienPhat").value;
    var noteTraPhong = document.querySelector("#noteTraPhong").value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "penaltyFee": inputTienPhat,
        "billNote": noteTraPhong,
        "booking": {
            "bookingId": idBookingTraPhong
        }
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/bill-booking", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result != '') {
                showAlertSuccess("Check-out thành công!");
                closePopup("popup-traPhong");
                showBookingByClient();

            } else {
                showAlertError("Lỗi! Vui lòng thử lại");
            }
        })
        .catch(error => console.log('error', error));
})

function setTimeCheckIn(d) {
    var date = new Date(d);
    date.setHours(14);
    date.setMinutes(0);
    date.setSeconds(0);
    return date;
}
function setTimeCheckOut(d) {
    var date = new Date(d);
    date.setHours(12);
    date.setMinutes(0);
    date.setSeconds(0);
    return date;
}
function setTimeEnd(d) {
    var date = new Date(d);
    date.setHours(23);
    date.setMinutes(59);
    date.setSeconds(59);
    return date;
}




//Js cho phan use service ==============================================
function showPageUseService() {
    var idClient = localStorage.getItem("IDCLIENT");
    if (idClient == null) {
        document.querySelector(".useService--infoUser__N").style.display = "block";
        document.querySelector(".useService--infoUser__Y").style.display = "none";
        document.querySelector(".useService--tableBooking").style.display = "none";
        document.querySelector(".useService--infoUseService").style.display = "none";
    } else {
        document.querySelector(".useService--infoUser__N").style.display = "none";
        document.querySelector(".useService--infoUser__Y").style.display = "block";
        document.querySelector(".useService--tableBooking").style.display = "block";
        document.querySelector(".useService--infoUseService").style.display = "block";
        showBookingInPageUseService();
        showAllUseServiceByUser();
    }
}
function showBookingInPageUseService() {
    var idClient = localStorage.getItem("IDCLIENT");
    var myHeaders = new Headers();
    myHeaders.append("type", "use-service");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-booking?val=" + idClient, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var b of result) {
                count++;
                html += `<tr>
                <td scope="row">`+ count + `</td>
                <td>Ngày đặt: `+ formatDate(b.bookingDate) + `<br>Ngày nhận: ` + formatDate(b.checkInDate) + ` <br>Ngày trả: ` + formatDate(b.checkOutDate) + `</td>
                <td>Họ tên: `+ b.client.name + `<br>Ngày sinh: ` + formatDate(b.client.dob) + `<br>CCCD: ` + b.client.cccd + `</td>
                <td>`+ b.bookingStatus + `</td>`;
                if (b.bookingStatus == 'Đã đặt')
                    html += `<td></td>`;
                else
                    html += `<td>Phòng ` + b.room.roomName + `</td>`
                html += `<td><input type="radio" name="pickBookingUseService" id="" value="` + b.bookingId + `"></td>
                </tr>`;
            }
            document.querySelector("#data-booking-use-service").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
function showAllService() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-service?val", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var s of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ s.serviceName + `</td>
                <td>`+ formatMoneyVND(s.servicePrice) + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="pickService(`+ s.serviceId + `)">Đặt dịch vụ</button>
                </td>
                </tr>`
                document.querySelector("#data-service").innerHTML = html;
            }
        })
        .catch(error => console.log('error', error));
}
function pickService(idService) {
    var booking = '';
    for (var b of document.getElementsByName("pickBookingUseService")) {
        if (b.checked) {
            booking = b.value;
            break;
        }
    }
    if (booking == '') {
        showAlertError("Chưa chọn booking!");
    } else {
        var noteUseService = document.querySelector("#noteUseService").value;
        var serviceQuantity = document.querySelector("#serviceQuantity").value;
        if (serviceQuantity == '') {
            serviceQuantity = 1;
        }
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "serviceQuantity": serviceQuantity,
            "useServiceNote": noteUseService,
            "useServiceStatus": "Đã đặt",
            "service": {
                "serviceId": idService
            },
            "booking": {
                "bookingId": booking
            }
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
                    showAlertSuccess("Đặt dịch vụ thành công!");
                    showAllUseServiceByUser();
                } else {
                    showAlertError("Lỗi! Vui lòng thử lại!");
                }
            })
            .catch(error => console.log('error', error));
    }
}
function showAllUseServiceByUser() {
    var idClient = localStorage.getItem("IDCLIENT");
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/get-list-use-service-by-user?id=" + idClient, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var us of result) {
                count++;
                html += `<tr>
                <td scope="row">`+ count + `</td>
                <td>`+ formatDate(us.dateUse) + `</td>
                <td>`+ us.service.serviceName + `</td>
                <td>`+ formatMoneyVND(us.servicePrice) + `</td>
                <td>`+ us.serviceQuantity + `</td>
                <td>`+ formatMoneyVND(us.servicePrice * us.serviceQuantity) + `</td>`;
                if (us.useServiceStatus == 'Đã đặt')
                    html += `<td>
                    <button type="button" class="btn btn-secondary" onclick="huyUseService(`+ us.useServiceId + `)">Huỷ</button>
                    <button type="button" class="btn btn-success" onclick="thanhToanUseService(`+ us.useServiceId + `)">Thanh toán</button>
                </td>`;
                else if (us.useServiceStatus == 'Đã thanh toán')
                    html += `<td>Đã thanh toán</td>`;
                else
                    html += `<td>Đã huỷ</td>`;
                html += `</tr>`;
            }
            document.querySelector("#data-use-service-by-user").innerHTML = html;
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
                showAlertSuccess("Đã huỷ đơn đặt dịch vụ!");
                showAllUseServiceByUser();
            } else {
                showAlertError("Lỗi! Vui lòng thử lại")
            }
        })
        .catch(error => console.log('error', error));
}
function thanhToanUseService(idUseService) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "useServiceId": idUseService,
        "useServiceStatus": "Đã thanh toán"
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
                showAlertSuccess("Thanh toán thành công!");
                showAllUseServiceByUser();
            } else {
                showAlertError("Lỗi! Vui lòng thử lại")
            }
        })
        .catch(error => console.log('error', error));
}

//Js cho phan contact ==============================================
function showAllContact() {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/list-contact", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var c of result) {
                count++;
                html += `<tr>
                <td scope="row">`+ count + `</td>
                <td>`+ formatDateTime(c.date) + `</td>
                <td>`+ c.name + `</td>
                <td>`+ c.email + `</td>
                <td>`+ c.phoneNumber + `</td>
                <td>`+ c.message + `</td>
                <td>`;
                if (c.contactStatus == 'Chưa tiếp nhận')
                    html += `<button type="button" class="btn btn-success" onclick="tiepNhanContact(` + c.contactId + `)">Tiếp nhận</button>`
                else
                    html += c.staff.staffCode + `<br>` + c.staff.user.name;
                html += `</td>
                </tr>`
            }
            document.querySelector("#data-contact").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
function tiepNhanContact(idContact) {
    var username = localStorage.getItem("username");
    var myHeaders = new Headers();
    myHeaders.append("username", username);
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "contactId": idContact
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/contact", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result != '') {
                showAlertSuccess("Tiếp nhận thành công");
                showAllContact();
            } else {
                showAlertError("Lỗi! Vui lòng thử lại");
            }
        })
        .catch(error => console.log('error', error));
}

function formatDateTime(date) {
    date = new Date(date);
    var hours = date.getHours();
    var minutes = date.getMinutes();
    var seconds = date.getSeconds();
    var day = date.getDate();
    var month = date.getMonth() + 1;
    var year = date.getFullYear();

    // Thêm số 0 vào trước các giá trị thời gian và ngày tháng nếu cần thiết
    hours = hours < 10 ? '0' + hours : hours;
    minutes = minutes < 10 ? '0' + minutes : minutes;
    seconds = seconds < 10 ? '0' + seconds : seconds;
    day = day < 10 ? '0' + day : day;
    month = month < 10 ? '0' + month : month;

    // Trả về chuỗi định dạng
    return hours + ':' + minutes + ':' + seconds + ' ' + day + '/' + month + '/' + year;
}