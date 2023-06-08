showInfoUser();
showInfoBooking();
showInfoRoom();
function showInfoUser() {
    var username = localStorage.getItem("username");
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/account?username=" + username, requestOptions)
        .then(response => response.json())
        .then(result => {
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
            document.querySelector("#cccd").value = result.user.cccd;
            document.querySelector("#passport").value = result.user.passport;
        })
        .catch(error => console.log('error', error));
}
function showInfoBooking() {
    document.querySelector("#timeCheckIn").value = localStorage.getItem("timeCheckIn");
    document.querySelector("#timeCheckOut").value = localStorage.getItem("timeCheckOut");
    document.querySelector("#numberPeople").value = localStorage.getItem("numberPeople");
}
function showInfoRoom() {
    var urlParams = new URLSearchParams(window.location.search);
    var room = urlParams.get('room');
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/room-type-dto?id=" + room, requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result.listImage.length > 0) {
                document.querySelector("#image-room").src = result.listImage[0];
            }
            document.querySelector("#name-room").innerHTML = result.nameRoomType;
            document.querySelector("#room-rate").innerHTML = `Giá: ` + formatMoneyVND(result.roomRate) + `/đêm`;
            timeCheckIn = localStorage.getItem("timeCheckIn");
            timeCheckOut = localStorage.getItem("timeCheckOut");
            diffDays = Math.floor((new Date(timeCheckOut) - new Date(timeCheckIn)) / (1000 * 60 * 60 * 24));
            document.querySelector("#bookingDeposit").value = diffDays * result.roomRate;
            document.querySelector("#tienCoc").innerHTML = `Số tiền cần đặt cọc: ` + formatMoneyVND(result.roomRate * diffDays);
        })
        .catch(error => console.log('error', error));
}
function saveBooking() {
    var urlParams = new URLSearchParams(window.location.search);
    var room = urlParams.get('room');
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
                setTimeout(() => {
                    window.location.replace("/page/qlThongTinCaNhan.html?page=lsp");
                }, 2000);
            } else {
                showAlertError("Lỗi! Vui lòng thử lại");
            }
        })
        .catch(error => console.log('error', error));
}
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