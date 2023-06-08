if (localStorage.getItem('username') == null) {
    window.location.replace('/page/login.html')
} else if (localStorage.getItem('accountType') != 'admin') {
    window.location.replace('/page/404.html')
}
// window.addEventListener('load', function(){

// })
showHomePage();
function nextPage(ele, classEle) {
    menu_items = document.getElementsByClassName('menu--item')
    for (menu_item of menu_items) {
        menu_item.classList.remove('menu--item__active')
    }
    ele.classList.add('menu--item__active')
    for (content_main of document.getElementsByClassName('content--main')) {
        content_main.style.display = 'none'
    }
    document.getElementsByClassName(classEle).item(0).style.display = 'block';
    if (classEle == 'content--home') {
        showHomePage();
    }
    if (classEle == 'content--roomtype') {
        showAllRoomType();
    } else if (classEle == 'content--room') {
        showAllRoom();
        getRoomType();
    } else if (classEle == 'content--amenity') {
        showAllAmenity();
        getRoomTypeInFormAmenity();
    } else if (classEle == 'content--service') {
        showAllService();
    } else if (classEle == 'content--staff') {
        showAllStaff();
        getAllPosition();
    } else if (classEle == 'content--client') {
        showAllClient();
    } else if (classEle == 'content--report') {
        showBieuDoDoanhThu();
    }
}

// JS cho phan home ******************************************************************************
function showHomePage() {
    showRevenueMonth();
    showNumberBookingMonth();
    showNumberClientMonth();
    showAllRating();
}
function showRevenueMonth() {
    var date = new Date();
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/revenue-month?month=" + (date.getMonth() + 1), requestOptions)
        .then(response => response.text())
        .then(result => {
            document.querySelector("#revenueMonth").innerHTML = formatMoney(Number(result));
        })
        .catch(error => console.log('error', error));
}
function showNumberBookingMonth() {
    var date = new Date();
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/number-booking-month?month=" + (date.getMonth() + 1), requestOptions)
        .then(response => response.text())
        .then(result => {
            document.querySelector("#numberBookingMonth").innerHTML = result;
        })
        .catch(error => console.log('error', error));
}
function showNumberClientMonth() {
    var date = new Date();
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/number-client-month?month=" + (date.getMonth() + 1), requestOptions)
        .then(response => response.text())
        .then(result => {
            document.querySelector("#numberClientMonth").innerHTML = result;
        })
        .catch(error => console.log('error', error));
}
function showAllRating() {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/list-rating", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var r of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ r.booking.user.name + `</td>
                <td>`+ r.ratingPoint + `</td>
                <td>
                    <p>`+ r.message + `</p>
                </td>
                </tr>`
            }
            document.querySelector("#data-rating").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}



// JS cho room type ******************************************************************************
//show all room type
function showAllRoomType() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-room-type?val=", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var rt of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ rt.nameRoomType + `</td>
                <td>`+ formatMoneyVND(rt.roomRateOld) + `</td>
                <td>`+ rt.numberRoom + `</td>
                <td>`+ rt.numberAdult + `</td>
                <td>`+ rt.numberChild + `</td>
                <td>`+ rt.bed + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateRoomType(`+ rt.roomTypeId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteRoomType(`+ rt.roomTypeId + `)">Xoá</button>
                </td>
                </tr>`;
            }
            document.querySelector("#dataRoomType").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
//Nhấn nút sửa
function updateRoomType(idRoomType) {
    openAddRoomType();
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/room-type?id=" + idRoomType, requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                document.querySelector('#idRoomType').value = result.roomTypeId;
                document.querySelector('#nameRoomType').value = result.nameRoomType;
                document.querySelector('#acreage').value = result.acreage;
                document.querySelector('#bed').value = result.bed;
                document.querySelector('#numberAdult').value = result.numberAdult;
                document.querySelector('#numberChild').value = result.numberChild;
                document.querySelector('#roomRate').value = result.roomRateOld;
                document.querySelector('#descriptionRoomType').value = result.description.replace(/<br>/g, '\n');
            }
        })
        .catch(error => console.log('error', error));
}
//Xoá room type
function deleteRoomType(idRoomType) {
    if (confirm("Chắc chắn muốn xoá loại phòng?")) {
        var requestOptions = {
            method: 'DELETE',
            redirect: 'follow'
        };

        fetch(url + "/api/room-type?id=" + idRoomType, requestOptions)
            .then(response => response.text())
            .then(result => {
                if (result == null) {
                    showAlertError();
                } else {
                    showAllRoomType();
                }
            })
            .catch(error => console.log('error', error));
    }
}
//Mở form add room type
function openAddRoomType() {
    idRoomType = document.querySelector('#idRoomType').value = ''
    nameRoomType = document.querySelector('#nameRoomType').value = ''
    acreage = document.querySelector('#acreage').value = ''
    bed = document.querySelector('#bed').value = ''
    numberAdult = document.querySelector('#numberAdult').value = ''
    numberChild = document.querySelector('#numberChild').value = 0
    roomRate = document.querySelector('#roomRate').value = ''
    description = document.querySelector('#descriptionRoomType').value = ''
    roomTypeImages = document.querySelector("#roomTypeImage").value = ''
    openPopup('popup-RoomType');
}
// Khi submit form add room type
form_add_roomtype = document.querySelector('#form-add-roomtype')
form_add_roomtype.addEventListener("submit", function (event) {
    event.preventDefault();
    saveRoomType();
})
// Lưu room type
function saveRoomType() {
    idRoomType = document.querySelector('#idRoomType').value
    nameRoomType = document.querySelector('#nameRoomType').value
    acreage = document.querySelector('#acreage').value
    bed = document.querySelector('#bed').value
    numberAdult = document.querySelector('#numberAdult').value
    numberChild = document.querySelector('#numberChild').value
    roomRate = document.querySelector('#roomRate').value
    description = document.querySelector('#descriptionRoomType').value
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "roomTypeId": idRoomType,
        "nameRoomType": nameRoomType,
        "acreage": acreage,
        "bed": bed,
        "numberAdult": numberAdult,
        "numberChild": numberChild,
        "roomRate": roomRate,
        "roomRateOld": roomRate,
        "description": description.replace(/\n/g, '<br>')
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/room-type", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                roomTypeImages = document.querySelector("#roomTypeImage")
                for (roomTypeImage of roomTypeImages.files) {
                    var fReader = new FileReader();
                    fReader.readAsDataURL(roomTypeImage);
                    fReader.onload = function (e) {
                        linkImage = e.target.result;
                        saveImageRoom(result.roomTypeId, linkImage);
                    }
                }
                closePopup('popup-RoomType');
                showAllRoomType();
            }
        })
        .catch(error => console.log('error', error));
}
//Lưu image room cho loại phòng
function saveImageRoom(idRoomType, image) {
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "imageRoom": image,
        "roomType": {
            "roomTypeId": idRoomType
        }
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/image-room", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            }
        })
        .catch(error => console.log('error', error));
}
// Tìm loại phòng theo tên
function searchRoomType(val) {
    var myHeaders = new Headers();
    myHeaders.append("type", "search");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-room-type?val=" + val, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var rt of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ rt.nameRoomType + `</td>
                <td>`+ formatMoneyVND(rt.roomRateOld) + `</td>
                <td>`+ rt.numberRoom + `</td>
                <td>`+ rt.numberAdult + `</td>
                <td>`+ rt.numberChild + `</td>
                <td>`+ rt.bed + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateRoomType(`+ rt.roomTypeId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteRoomType(`+ rt.roomTypeId + `)">Xoá</button>
                </td>
                </tr>`;
            }
            document.querySelector("#dataRoomType").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
//  Sự kiện nhập ô search
input_search_roomType = document.querySelector("#input-search-room-type")
input_search_roomType.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        searchRoomType(input_search_roomType.value);
    }
})






// JS cho room ******************************************************************************
// Show all room
function showAllRoom() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-room?val=", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var r of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ r.roomName + `</td>
                <td>`+ r.roomType.nameRoomType + `</td>
                <td>`+ r.bed + `</td>
                <td>`+ r.roomStatus + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateRoom(`+ r.roomId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteRoom(`+ r.roomId + `)">Xoá</button>
                </td>
                </tr>`;
            }
            document.querySelector("#dataRoom").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
//Mở form add room
function openAddRoom() {
    document.querySelector("#idRoom").value = '';
    document.querySelector("#nameRoom").value = '';
    document.querySelector("#bedRoom").value = '';
    document.querySelector("#roomStatus").value = 'Phòng trống';
    document.querySelector('#descriptionRoom').value = '';
    openPopup('popup-Room');
}
function getRoomType() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch("http://localhost:8080/api/list-room-type", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            for (var rt of result) {
                html += `<option value="` + rt.roomTypeId + `">` + rt.nameRoomType + `</option>`;
            }
            document.querySelector("#roomType").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// Khi submit form add room
form_add_room = document.querySelector("#form-add-room")
form_add_room.addEventListener("submit", function (event) {
    event.preventDefault();
    saveRoom();
})
// Lưu room
function saveRoom() {
    idRoom = document.querySelector("#idRoom").value;
    nameRoom = document.querySelector("#nameRoom").value;
    bedRoom = document.querySelector("#bedRoom").value;
    roomStatus = document.querySelector("#roomStatus").value;
    roomType = document.querySelector('#roomType').value;
    descriptionRoom = document.querySelector('#descriptionRoom').value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "roomId": idRoom,
        "roomName": nameRoom,
        "roomStatus": roomStatus,
        "bed": bedRoom,
        "description": descriptionRoom,
        "roomType": {
            "roomTypeId": roomType
        }
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/room", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                closePopup('popup-Room');
                // show all room
                showAllRoom()
            }
        })
        .catch(error => console.log('error', error));
}
// Nhấn sửa room
function updateRoom(idRoom) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/room?id=" + idRoom, requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                openAddRoom();
                document.querySelector("#idRoom").value = result.roomId;
                document.querySelector("#nameRoom").value = result.roomName;
                document.querySelector("#bedRoom").value = result.bed;
                document.querySelector("#roomStatus").value = result.roomStatus;
                document.querySelector('#descriptionRoom').value = result.description;
                document.querySelector('#roomType').value = result.roomType.roomTypeId;
            }
        })
        .catch(error => console.log('error', error));
}
// Xoá room
function deleteRoom(idRoom) {
    if (confirm("Chắc chắn muốn xoá phòng?")) {
        var requestOptions = {
            method: 'DELETE',
            redirect: 'follow'
        };

        fetch(url + "/api/room?id=" + idRoom, requestOptions)
            .then(response => response.text())
            .then(result => {
                if (result == null) {
                    showAlertError();
                } else {
                    showAllRoom();
                    getRoomType();
                }
            })
            .catch(error => console.log('error', error));
    }
}
// Tìm kiếm room
function searchRoom(val) {
    var myHeaders = new Headers();
    myHeaders.append("type", "search");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-room?val=" + val, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var r of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ r.roomName + `</td>
                <td>`+ r.roomType.nameRoomType + `</td>
                <td>`+ r.bed + `</td>
                <td>`+ r.roomStatus + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateRoom(`+ r.roomId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteRoom(`+ r.roomId + `)">Xoá</button>
                </td>
                </tr>`;
            }
            document.querySelector("#dataRoom").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// Sự kiện nhập ô search
input_search_room = document.querySelector("#input-search-room")
input_search_room.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        searchRoom(input_search_room.value);
    }
})

// JS cho amenity ******************************************************************************
function showAllAmenity() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-amenity?val", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var a of result) {
                count += 1;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ a.amenityName + `</td>
                <td>`+ a.amenityType + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateAmenity(`+ a.amenityId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteAmenity(`+ a.amenityId + `)">Xoá</button>
                </td>
                </tr>`;
            }
            document.querySelector("#data-amenity").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// Mở form add amenity
function openAddAmenity() {
    document.querySelector("#idAmenity").value = '';
    document.querySelector("#nameAmenity").value = '';
    document.querySelector("#iconAmenity").value = '';
    document.querySelector("#amenityType").value = 'Tiện ích chung';
    room_type_checkboxs = document.getElementsByClassName('room-type-checkbox');
    for (var room_type_checkbox of room_type_checkboxs) {
        room_type_checkbox.checked = true;
    }
    openPopup('popup-Amenity');
}
function getRoomTypeInFormAmenity() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-room-type?val", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html1 = '';
            var html2 = '';
            var i = 0;
            for (var rt of result) {
                if (i < result.length / 2) {
                    html1 += `<div class="form-check">
                    <input class="form-check-input room-type-checkbox" type="checkbox" value="`+ rt.roomTypeId + `">
                    <label class="form-check-label">
                    `+ rt.nameRoomType + `
                    </label>
                    </div>`;
                } else {
                    html2 += `<div class="form-check">
                    <input class="form-check-input room-type-checkbox" type="checkbox" value="`+ rt.roomTypeId + `">
                    <label class="form-check-label">
                    `+ rt.nameRoomType + `
                    </label>
                    </div>`;
                }
                i++;
            }
            document.querySelector("#room-type-checkboxAmenity-left").innerHTML = html1;
            document.querySelector("#room-type-checkboxAmenity-right").innerHTML = html2;
        })
        .catch(error => console.log('error', error));
}
// Sự kiện chọn loại tiện ích
amenityType = document.querySelector("#amenityType");
amenityType.addEventListener("change", function () {
    room_type_checkboxs = document.getElementsByClassName('room-type-checkbox');
    if (amenityType.value == 'Tiện ích chung') {
        for (var room_type_checkbox of room_type_checkboxs) {
            room_type_checkbox.checked = true;
        }
    } else {
        for (var room_type_checkbox of room_type_checkboxs) {
            room_type_checkbox.checked = false;
        }
    }
})
// Lưu Amenity
function saveAmenity() {
    idAmenity = document.querySelector("#idAmenity").value;
    nameAmenity = document.querySelector("#nameAmenity").value;
    iconAmenity = document.querySelector("#iconAmenity").value;
    if (iconAmenity == '') {
        iconAmenity = '<i class="fa-solid fa-check"></i>';
    }
    amenityType = document.querySelector("#amenityType").value;
    room_type_checkboxs = document.getElementsByClassName('room-type-checkbox');
    listIdRoomType = [];
    if (amenityType != 'Tiện ích chung') {
        for (var room_type_checkbox of room_type_checkboxs) {
            if (room_type_checkbox.checked) {
                listIdRoomType.push(Number(room_type_checkbox.value));
            }
        }
    }
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "amenityId": idAmenity,
        "amenityName": nameAmenity,
        "amenityIcon": iconAmenity,
        "amenityType": amenityType,
        "listIdRoomType": listIdRoomType
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/amenity", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                closePopup('popup-Amenity');
                showAllAmenity();
            }
        })
        .catch(error => console.log('error', error));
}
//Su kien nhan luu
form_add_amenity = document.querySelector("#form-add-amenity")
form_add_amenity.addEventListener("submit", function (event) {
    event.preventDefault();
    saveAmenity();
})
// Nhan sua amenity
function updateAmenity(idAmenity) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/amenity?id=" + idAmenity, requestOptions)
        .then(response => response.json())
        .then(result => {
            openAddAmenity();
            document.querySelector("#idAmenity").value = result.amenityId;
            document.querySelector("#nameAmenity").value = result.amenityName;
            document.querySelector("#iconAmenity").value = result.amenityIcon;
            document.querySelector("#amenityType").value = result.amenityType;
            room_type_checkboxs = document.getElementsByClassName('room-type-checkbox');
            for (var room_type_checkbox of room_type_checkboxs) {
                room_type_checkbox.checked = false;
            }
            for (var room_type_checkbox of room_type_checkboxs) {
                if (result.listIdRoomType.includes(Number(room_type_checkbox.value))) {
                    room_type_checkbox.checked = true;
                }
            }
        })
        .catch(error => console.log('error', error));
}
//Nhan xoa amenity
function deleteAmenity(idAmenity) {
    if (confirm("Chắc chắn muốn xoá tiện ích?")) {
        var requestOptions = {
            method: 'DELETE',
            redirect: 'follow'
        };

        fetch(url + "/api/amenity?id=" + idAmenity, requestOptions)
            .then(response => response.text())
            .then(result => {
                if (result == null) {
                    showAlertError();
                } else {
                    showAllAmenity();
                }
            })
            .catch(error => console.log('error', error));
    }
}
//Tim amenity bang ten
function searchAmenity(val) {
    var myHeaders = new Headers();
    myHeaders.append("type", "search");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-amenity?val=" + val, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var a of result) {
                count += 1;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ a.amenityName + `</td>
                <td>`+ a.amenityType + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateAmenity(`+ a.amenityId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteAmenity(`+ a.amenityId + `)">Xoá</button>
                </td>
                </tr>`;
            }
            document.querySelector("#data-amenity").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// Sự kiện nhấn enter nút search
input_search_amenity = document.querySelector("#input-search-amenity")
input_search_amenity.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        searchAmenity(input_search_amenity.value);
    }
})



// JS cho service ******************************************************************************
// luu service
function saveService() {
    serviceImage = document.querySelector("#serviceImage");
    if (serviceImage.value == '') {
        saveServiceS('');
    } else {
        var fReader = new FileReader();
        fReader.readAsDataURL(serviceImage.files[0]);
        fReader.onload = function (e) {
            linkImage = e.target.result;
            saveServiceS(linkImage);
        }
    }
}
function saveServiceS(linkImage) {
    idService = document.querySelector("#idService").value;
    nameService = document.querySelector("#nameService").value;
    servicePrice = document.querySelector("#servicePrice").value;
    descriptionService = document.querySelector("#descriptionService").value;
    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "serviceId": idService,
        "serviceName": nameService,
        "servicePrice": servicePrice,
        "description": descriptionService.replace(/\n/g, '<br>'),
        "linkImage": linkImage
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/service", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                closePopup('popup-Service');
                showAllService();
            }
        })
        .catch(error => console.log('error', error));
}
//Su kien nhan nut luu
form_add_service = document.querySelector("#form-add-service")
form_add_service.addEventListener("submit", function (event) {
    event.preventDefault();
    saveService();
})
//mo form add service
function openAddService() {
    document.querySelector("#idService").value = '';
    document.querySelector("#nameService").value = '';
    document.querySelector("#serviceImage").value = '';
    document.querySelector("#servicePrice").value = '';
    document.querySelector("#descriptionService").value = '';
    openPopup('popup-Service');
}
//show all service
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
                    <button type="button" class="btn btn-warning" onclick="updateService(`+ s.serviceId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteService(`+ s.serviceId + `)">Xoá</button>
                </td>
                </tr>`
            }
            document.querySelector("#data-service").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// update service
function updateService(idService) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/service?id=" + idService, requestOptions)
        .then(response => response.json())
        .then(result => {
            openAddService();
            document.querySelector("#idService").value = result.serviceId;
            document.querySelector("#nameService").value = result.serviceName;
            document.querySelector("#servicePrice").value = result.servicePrice;
            document.querySelector("#descriptionService").value = result.description.replace(/<br>/g, '\n');
        })
        .catch(error => console.log('error', error));
}
// xoa service
function deleteService(idService) {
    if (confirm("Chắc chắn muốn xoá dịch vụ?")) {
        var requestOptions = {
            method: 'DELETE',
            redirect: 'follow'
        };

        fetch(url + "/api/service?id=" + idService, requestOptions)
            .then(response => response.text())
            .then(result => {
                if (result == null) {
                    showAlertError();
                } else {
                    showAllService();
                }
            })
            .catch(error => console.log('error', error));
    }
}
// search service
function searchService(val) {
    var myHeaders = new Headers();
    myHeaders.append("type", "search");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-service?val=" + val, requestOptions)
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
                    <button type="button" class="btn btn-warning" onclick="updateService(`+ s.serviceId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteService(`+ s.serviceId + `)">Xoá</button>
                </td>
                </tr>`
            }
            document.querySelector("#data-service").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// Sự kiện nhấn enter nút search
input_search_service = document.querySelector("#input-search-service")
input_search_service.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        searchService(input_search_service.value);
    }
})





// JS cho service ******************************************************************************
// get all position
function getAllPosition() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-position", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = ''
            for (var p of result) {
                html += `<option value="` + p.positionId + `">` + p.positionName + `</option>`
            }
            document.querySelector("#position").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// open form add staff
function openAddStaff() {
    document.querySelector("#idStaff").value = '';
    document.querySelector("#idUser").value = '';
    document.querySelector("#staffCode").value = '';
    document.querySelector("#nameStaff").value = '';
    document.querySelector("#genderStaff1").checked = true;
    document.querySelector("#dobStaff").value = '';
    document.querySelector("#position").value = '1';
    document.querySelector("#phoneNumberStaff").value = '';
    document.querySelector("#emailStaff").value = '';
    document.querySelector("#cccdStaff").value = '';
    document.querySelector("#passportStaff").value = '';
    document.querySelector("#salary").value = '';
    document.querySelector("#addressStaff").value = '';
    openPopup('popup-Staff');
}
// Luu staff
function saveStaff() {
    var idStaff = document.querySelector("#idStaff").value;
    var staffCode = document.querySelector("#staffCode").value;
    var idUser = document.querySelector("#idUser").value;
    var nameStaff = document.querySelector("#nameStaff").value;
    var genderStaff = document.getElementsByName("genderStaff");
    var dobStaff = document.querySelector("#dobStaff").value;
    var position = document.querySelector("#position").value;
    var phoneNumberStaff = document.querySelector("#phoneNumberStaff").value;
    var emailStaff = document.querySelector("#emailStaff").value;
    var cccdStaff = document.querySelector("#cccdStaff").value;
    var passportStaff = document.querySelector("#passportStaff").value;
    var salary = document.querySelector("#salary").value;
    var addressStaff = document.querySelector("#addressStaff").value;

    var gender = "Nam";
    for (var g of genderStaff) {
        if (g.checked) {
            gender = g.value;
        }
    }

    var myHeaders = new Headers();
    myHeaders.append("Content-Type", "application/json");

    var raw = JSON.stringify({
        "staffId": idStaff,
        "salary": salary,
        "staffCode": staffCode,
        "position": {
            "positionId": position
        },
        "user": {
            "userId": idUser,
            "name": nameStaff,
            "gender": gender,
            "dob": dobStaff,
            "phoneNumber": phoneNumberStaff,
            "email": emailStaff,
            "cccd": cccdStaff,
            "passport": passportStaff,
            "address": addressStaff
        }
    });

    var requestOptions = {
        method: 'POST',
        headers: myHeaders,
        body: raw,
        redirect: 'follow'
    };

    fetch(url + "/api/staff", requestOptions)
        .then(response => response.json())
        .then(result => {
            if (result == null) {
                showAlertError();
            } else {
                closePopup('popup-Staff');
                showAllStaff();
            }
        })
        .catch(error => console.log('error', error));
}
//Su kien nhan nut luu
form_add_staff = document.querySelector("#form-add-staff");
form_add_staff.addEventListener("submit", function (event) {
    event.preventDefault();
    saveStaff();
})
// Show all staff
function showAllStaff() {
    var myHeaders = new Headers();
    myHeaders.append("type", "all");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-staff?val", requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var s of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ s.staffCode + `</td>
                <td>`+ s.user.name + `</td>
                <td>`+ s.position.positionName + `</td>
                <td>`+ formatDate(s.user.dob) + `</td>
                <td>`+ s.user.cccd + `</td>
                <td>`+ s.user.phoneNumber + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateStaff(`+ s.staffId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteStaff(`+ s.staffId + `)">Xoá</button>
                </td>
                </tr>`
            }
            document.querySelector("#data-staff").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// update staff
function updateStaff(idStaff) {
    var requestOptions = {
        method: 'GET',
        redirect: 'follow'
    };

    fetch(url + "/api/staff?id=" + idStaff, requestOptions)
        .then(response => response.json())
        .then(result => {
            openAddStaff();
            document.querySelector("#idStaff").value = result.staffId;
            document.querySelector("#idUser").value = result.user.userId;
            document.querySelector("#staffCode").value = result.staffCode;
            document.querySelector("#nameStaff").value = result.user.name;
            if (result.user.gender == 'Nam')
                document.querySelector("#genderStaff1").checked = true;
            else
                document.querySelector("#genderStaff2").checked = true;
            document.querySelector("#dobStaff").value = result.user.dob;
            document.querySelector("#position").value = result.position.positionId;
            document.querySelector("#phoneNumberStaff").value = result.user.phoneNumber;
            document.querySelector("#emailStaff").value = result.user.email;
            document.querySelector("#cccdStaff").value = result.user.cccd;
            document.querySelector("#passportStaff").value = result.user.passport;
            document.querySelector("#salary").value = result.salary;
            document.querySelector("#addressStaff").value = result.user.address;
        })
        .catch(error => console.log('error', error));
}
// delete staff
function deleteStaff(idStaff) {
    if (confirm("Chắc chắn muốn xoá nhân viên?")) {
        var requestOptions = {
            method: 'DELETE',
            redirect: 'follow'
        };

        fetch(url + "/api/staff?id=" + idStaff, requestOptions)
            .then(response => response.text())
            .then(result => {
                if (result == null) {
                    showAlertError();
                } else {
                    showAllStaff();
                }
            })
            .catch(error => console.log('error', error));
    }
}
// Search staff
function searchStaff(val) {
    var regex = /^NV\d+$/;
    var regex1 = /^nv\d+$/;
    if (regex.test(val) || regex1.test(val)) {
        if (val.length < 11) {
            val = val.substring(2,);
            while (val.length < 9) {
                val = "0" + val;
            }
        }
        val = "NV" + val;
    }
    var myHeaders = new Headers();
    myHeaders.append("type", "search");

    var requestOptions = {
        method: 'GET',
        headers: myHeaders,
        redirect: 'follow'
    };

    fetch(url + "/api/list-staff?val=" + val, requestOptions)
        .then(response => response.json())
        .then(result => {
            var html = '';
            var count = 0;
            for (var s of result) {
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ s.staffCode + `</td>
                <td>`+ s.user.name + `</td>
                <td>`+ s.position.positionName + `</td>
                <td>`+ formatDate(s.user.dob) + `</td>
                <td>`+ s.user.cccd + `</td>
                <td>`+ s.user.phoneNumber + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateStaff(`+ s.staffId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteStaff(`+ s.staffId + `)">Xoá</button>
                </td>
                </tr>`
            }
            document.querySelector("#data-staff").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}
// Sự kiện nhấn enter nút search
input_search_staff = document.querySelector("#input-search-staff")
input_search_staff.addEventListener("keyup", function (event) {
    if (event.keyCode === 13) {
        searchStaff(input_search_staff.value);
    }
})



// JS cho client ******************************************************************************
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
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ c.name + `</td>
                <td>`+ c.account + `</td>
                <td>`+ formatDate(c.dob) + `</td>
                <td>`+ c.address + `</td>
                <td>`+ c.cccd + `</td>
                <td>`+ c.phoneNumber + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateClient(`+ c.userId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteClient(`+ c.userId + `)">Xoá</button>
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
//delete client
function deleteClient(idClient) {
    if (confirm("Chắc chắn muốn xoá khách hàng?")) {
        var requestOptions = {
            method: 'DELETE',
            redirect: 'follow'
        };

        fetch(url + "/api/client?id=" + idClient, requestOptions)
            .then(response => response.text())
            .then(result => {
                if (result == null) {
                    showAlertError();
                } else {
                    showAllClient();
                }
            })
            .catch(error => console.log('error', error));
    }
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
                count++;
                html += `<tr>
                <th scope="row">`+ count + `</th>
                <td>`+ c.name + `</td>
                <td>`+ c.account + `</td>
                <td>`+ formatDate(c.dob) + `</td>
                <td>`+ c.address + `</td>
                <td>`+ c.cccd + `</td>
                <td>`+ c.phoneNumber + `</td>
                <td>
                    <button type="button" class="btn btn-warning" onclick="updateClient(`+ c.userId + `)">Sửa</button>
                    <button type="button" class="btn btn-danger" onclick="deleteClient(`+ c.userId + `)">Xoá</button>
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



// Js cho alert lỗi
function showAlertError() {
    document.querySelector("#alert-error").classList.remove("alert-error-hide");
    document.querySelector("#alert-error").classList.add("alert-error-show");
    setTimeout(hideAlertError, 3000)
}
function hideAlertError() {
    document.querySelector("#alert-error").classList.remove("alert-error-show");
    document.querySelector("#alert-error").classList.add("alert-error-hide");
}