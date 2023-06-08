function clickSearch() {
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
        window.location.replace("/page/listRoom.html?type=search");
    }
}
function setTimeEnd(d) {
    var date = new Date(d);
    date.setHours(23);
    date.setMinutes(59);
    date.setSeconds(59);
    return date;
}

var form_contact = document.querySelector("#form-contact");
form_contact.addEventListener("submit", function (event) {
    event.preventDefault();
    var name = document.querySelector("#Full-Name").value;
    var email = document.querySelector("#Email").value;
    var phoneNumber = document.querySelector("#Phone-Number").value;
    var MesContact = document.querySelector("#MesContact").value;
    var check = true;
    if (MesContact == '') {
        showAlertError("Chưa nhập nội dung cần hỗ trợ"); check = false;
    }
    if (phoneNumber == '' && email == '') {
        showAlertError("Cần nhập số điện thoại hoặc email!"); check = false;
    }
    if (name == '') {
        showAlertError("Không để trống họ tên"); check = false;
    }
    if (check) {
        var myHeaders = new Headers();
        myHeaders.append("Content-Type", "application/json");

        var raw = JSON.stringify({
            "name": name,
            "email": email,
            "phoneNumber": phoneNumber,
            "message": MesContact
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
                    showAlertSuccess("Gửi thông tin thành công");
                } else {
                    showAlertError("Lỗi! Vui lòng thử lại");
                }
            })
            .catch(error => console.log('error', error));
    }
})