window.addEventListener("load", function () {
    var urlParams = new URLSearchParams(window.location.search);
    var room = urlParams.get('room');
    showRoom(room);
    showAmenity(room);
})

function showRoom(room) {
    if (localStorage.getItem("timeCheckIn") == null)
        document.querySelector("#btn-dat-phong").style.display = 'none';
    else
        document.querySelector("#btn-dat-phong").style.display = 'block';
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/room-type-dto?id=" + room, requestOptions)
        .then(response => response.json())
        .then(result => {
            var room_images_item = document.getElementsByClassName('room--images--item');
            var i = 0;
            while (true) {
                var check = false;
                if (result.listImage.length == 0)
                    break;
                for (var im of result.listImage) {
                    room_images_item[i].src = im;
                    i++;
                    if (i >= 4) {
                        check = true;
                        break;
                    }
                }
                if (check) {
                    break;
                }
            }

            document.querySelector("#nameRoom").innerHTML = result.nameRoomType;
            if (result.roomRate == result.roomRateOld) {
                document.querySelector(".roomOldPrice").innerHTML = '';
            } else {
                document.querySelector(".roomOldPrice").innerHTML = formatMoneyVND(result.roomRateOld);
            }
            document.querySelector(".roomPrice").innerHTML = formatMoneyVND(result.roomRate);
            document.querySelector("#description-room").innerHTML = result.description;
        })
        .catch(error => console.log('error', error));
}

function showAmenity(room) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/list-amenity-room?room=" + room, requestOptions)
        .then(response => response.json())
        .then(result => {
            var chung = [];
            var rieng = [];
            for (var a of result) {
                if (a.amenity.amenityType == 'Tiện ích chung') {
                    chung.push(a.amenity.amenityIcon + " " + a.amenity.amenityName);
                } else {
                    rieng.push(a.amenity.amenityIcon + " " + a.amenity.amenityName);
                }
            }
            var htc1 = '';
            var htc2 = '';
            var htc3 = '';
            var htr1 = '';
            var htr2 = '';
            var htr3 = '';
            for (var i = 0; i < chung.length; i++) {
                if (i < chung.length / 3) {
                    htc1 += `<div class="room--utilities--item">` + chung[i] + `</div>`;
                } else if (i < 2 * chung.length / 3) {
                    htc2 += `<div class="room--utilities--item">` + chung[i] + `</div>`;
                } else {
                    htc3 += `<div class="room--utilities--item">` + chung[i] + `</div>`;
                }
            }
            for (var i = 0; i < rieng.length; i++) {
                if (i < rieng.length / 3) {
                    htr1 += `<div class="room--utilities--item">` + rieng[i] + `</div>`;
                } else if (i < 2 * rieng.length / 3) {
                    htr2 += `<div class="room--utilities--item">` + rieng[i] + `</div>`;
                } else {
                    htr3 += `<div class="room--utilities--item">` + rieng[i] + `</div>`;
                }
            }
            document.querySelector("#tnc-1").innerHTML = htc1;
            document.querySelector("#tnc-2").innerHTML = htc2;
            document.querySelector("#tnc-3").innerHTML = htc3;
            document.querySelector("#tnr-1").innerHTML = htr1;
            document.querySelector("#tnr-2").innerHTML = htr2;
            document.querySelector("#tnr-3").innerHTML = htr3;
        })
        .catch(error => console.log('error', error));
}
function bookingRoom(id) {
    window.location.replace("/page/booking.html?room=" + id);
}
function datPhong() {
    var urlParams = new URLSearchParams(window.location.search);
    var room = urlParams.get('room');
    bookingRoom(room);
}