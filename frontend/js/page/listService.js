showAllService();
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
            for (var s of result) {
                html += `<div class="service--item">
                <div class="service--item--title">`+ s.serviceName + `</div>
                <div class="row">
                    <div class="col-md-4">
                        <div class="service--item--image row">
                            <img src="`+ s.linkImage + `"
                                alt="">
                        </div>
                        <div class="service--item--price row">
                            <p>Giá chỉ:</p>
                            <div class="service--price">`+ formatMoneyVND(s.servicePrice) + `</div>
                        </div>
                        <div class="row">
                            <p class="service--item--price--txt">(* Liên hệ lễ tân để sử dụng dịch vụ)</p>
                        </div>
                    </div>
                    <div class="col-md-8">
                        <div class="service--item-description">
                            <p>`+ s.description + `</p>
                        </div>
                    </div>
                </div>
                </div>`
            }
            document.querySelector(".service--items").innerHTML = html;
        })
        .catch(error => console.log('error', error));
}