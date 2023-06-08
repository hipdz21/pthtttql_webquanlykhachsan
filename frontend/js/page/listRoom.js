var urlParams = new URLSearchParams(window.location.search);
var type = urlParams.get('type');
if (type == 'search') {
    var timeCheckIn = localStorage.getItem("timeCheckIn");
    var timeCheckOut = localStorage.getItem("timeCheckOut");
    var numberPeople = localStorage.getItem("numberPeople");
    searchRoom(timeCheckIn, timeCheckOut, numberPeople);
    document.querySelector("#timeCheckIn").value = timeCheckIn;
    document.querySelector("#timeCheckOut").value = timeCheckOut;
    document.querySelector("#numberPeople").value = numberPeople;
} else {
    showAllListRoom();
}

function showAllListRoom() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-room-type-dto?val", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            for (var r of result) {
                html += `<div class="room-item">
                <div class="room--item--title">`+ r.nameRoomType + `</div>
                <div class="row">
                    <div class="col-md-3">
                        <div class="room--image">`;

                if (r.listImage.length > 0) {
                    html += `<img src="` + r.listImage[0] + `" alt="Ảnh ` + r.nameRoomType + `">`;
                } else {
                    html += `<img src="https://cdn2.vietnambooking.com/wp-content/uploads/hotel_pro/hotel_353355/ab47d8b24d903c37aabc89445a9ff105.jpg" alt="Ảnh ` + r.nameRoomType + `">`;
                }
                html += `</div>
                        <div class="room--utilities">
                            <div class="room--utilities--item"><i class="fa-regular fa-house"></i>`+ r.acreage + ` m²</div>
                            <div class="room--utilities--item"><i class="fa-regular fa-bed-front"></i>`+ r.bed + `</div>
                            <div class="room--utilities--item"><i class="fa-regular fa-ban-smoking"></i>Không hút thuốc</div>
                            <div class="room--utilities--item"><i class="fa-solid fa-wifi"></i>Wifi miễn phí</div>
                            <div class="room--utilities--item"><i class="fa-light fa-door-open"></i>Ban công/Sân hiên</div>
                            <div class="room--utilities--item"><i class="fa-solid fa-bath"></i>Bồn tắm/Vòi sen riêng</div>
                        </div>
                    </div>
                    <div class="col-md-9">
                        <div class="row room--numberPeople">
                            <div class="room--numberPeople--item"><i class="fa-solid fa-person"></i> x`+ r.numberAdult + ` Người lớn</div>
                            <div class="room--numberPeople--item"><i class="fa-solid fa-child"></i> x`+ r.numberChild + ` Trẻ em</div>`;
                if (r.numberAdult >= 2)
                    html += `<div class="room--numberPeople--item">2 bé dưới 5 tuổi được ở miễn phí</div>`;
                html += `</div>
                        <div class="row">
                            <div class="col room--deals">
                                <div class="h5">Ưu đãi trong phòng</div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Bữa sáng miễn phí</div>
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Dọn phòng hàng ngày</div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Miễn phí Internet</div>
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Miễn phí nước suối trong phòng</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row room--item--footer">`;
                if (r.roomRateOld != r.roomRate) {
                    html += `<div class="room--discount">Giảm ` + Math.ceil(r.roomRate / r.roomRateOld * 100) + `% hôm nay
                                <div class="room--discount__triangle"></div>
                            </div>`;
                }
                html += `<div class="col">`
                if (r.roomRateOld != r.roomRate)
                    html += `<div class="room--oldPrice">` + formatMoneyVND(r.roomRateOld) + `</div>`
                html += `<div class="room--price">` + formatMoneyVND(r.roomRate) + `</div>
                                <div class="room--unit">/đêm</div>
                            </div>
                            <div class="col-md-3">`
                if (localStorage.getItem("timeCheckIn") != null)
                    html += `<button type="button" class="btn btn-primary btn-lg btn--orderRoom" onclick="bookingRoom(` + r.roomTypeId + `)">Đặt ngay</button>`
                html += `<button type="button" class="btn btn-light" onclick="showRoomDetail(` + r.roomTypeId + `)">Xem chi tiết</button>
                            </div>
                        </div>
                    </div>
                </div>
                </div>`;
            }
            document.querySelector(".room--items").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
function showRoomDetail(idRoomType) {
    window.location.replace("/page/roomDetail.html?room=" + idRoomType);
}
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
            for (var r of result) {
                html += `<div class="room-item">
                <div class="room--item--title">`+ r.nameRoomType + `</div>
                <div class="row">
                    <div class="col-md-3">
                        <div class="room--image">`;

                if (r.listImage.length > 0) {
                    html += `<img src="` + r.listImage[0] + `" alt="Ảnh ` + r.nameRoomType + `">`;
                } else {
                    html += `<img src="https://cdn2.vietnambooking.com/wp-content/uploads/hotel_pro/hotel_353355/ab47d8b24d903c37aabc89445a9ff105.jpg" alt="Ảnh ` + r.nameRoomType + `">`;
                }
                html += `</div>
                        <div class="room--utilities">
                            <div class="room--utilities--item"><i class="fa-regular fa-house"></i>`+ r.acreage + ` m²</div>
                            <div class="room--utilities--item"><i class="fa-regular fa-bed-front"></i>`+ r.bed + `</div>
                            <div class="room--utilities--item"><i class="fa-regular fa-ban-smoking"></i>Không hút thuốc</div>
                            <div class="room--utilities--item"><i class="fa-solid fa-wifi"></i>Wifi miễn phí</div>
                            <div class="room--utilities--item"><i class="fa-light fa-door-open"></i>Ban công/Sân hiên</div>
                            <div class="room--utilities--item"><i class="fa-solid fa-bath"></i>Bồn tắm/Vòi sen riêng</div>
                        </div>
                    </div>
                    <div class="col-md-9">
                        <div class="row room--numberPeople">
                            <div class="room--numberPeople--item"><i class="fa-solid fa-person"></i> x`+ r.numberAdult + ` Người lớn</div>
                            <div class="room--numberPeople--item"><i class="fa-solid fa-child"></i> x`+ r.numberChild + ` Trẻ em</div>`;
                if (r.numberAdult >= 2)
                    html += `<div class="room--numberPeople--item">2 bé dưới 5 tuổi được ở miễn phí</div>`;
                html += `</div>
                        <div class="row">
                            <div class="col room--deals">
                                <div class="h5">Ưu đãi trong phòng</div>
                                <div class="row">
                                    <div class="col-md-6">
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Bữa sáng miễn phí</div>
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Dọn phòng hàng ngày</div>
                                    </div>
                                    <div class="col-md-6">
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Miễn phí Internet</div>
                                        <div class="room--deals--item"><i class="fa-solid fa-check"></i>Miễn phí nước suối trong phòng</div>
                                    </div>
                                </div>
                            </div>
                        </div>
                        <div class="row room--item--footer">`;
                if (r.roomRateOld != r.roomRate) {
                    html += `<div class="room--discount">Giảm ` + Math.ceil(r.roomRate / r.roomRateOld * 100) + `% hôm nay
                                <div class="room--discount__triangle"></div>
                            </div>`;
                }
                html += `<div class="col">`
                if (r.roomRateOld != r.roomRate)
                    html += `<div class="room--oldPrice">` + formatMoneyVND(r.roomRateOld) + `</div>`
                html += `<div class="room--price">` + formatMoneyVND(r.roomRate) + `</div>
                                <div class="room--unit">/đêm</div>
                            </div>
                            <div class="col-md-3">
                                <button type="button" class="btn btn-primary btn-lg btn--orderRoom" onclick="bookingRoom(`+ r.roomTypeId + `)">Đặt ngay</button>
                                <button type="button" class="btn btn-light" onclick="showRoomDetail(`+ r.roomTypeId + `)">Xem chi tiết</button>
                            </div>
                        </div>
                    </div>
                </div>
                </div>`;
            }
            document.querySelector(".room--items").innerHTML = html;
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
function setTimeEnd(d) {
    var date = new Date(d);
    date.setHours(23);
    date.setMinutes(59);
    date.setSeconds(59);
    return date;
}
function bookingRoom(id) {
    window.location.replace("/page/booking.html?room=" + id);
}