$(document).ready(function(){
    searchAvailable();
    var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
    var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
      return new bootstrap.Tooltip(tooltipTriggerEl)
    });
});

function searchAvailable(){
    $("#searchAvailableBtn").on("click", function(e){
        e.preventDefault();
        var newHeadCount = $("#headCount").val();
        if (newHeadCount == "" || newHeadCount == 0 || newHeadCount == null){
            alert("인원을 입력해주라냥!");
            return;
        }
        if ($("#dateRange").val() == ""){
            alert("기간을 선택해주라냥!");
            return;
        }
        var newCheckIn = $("#dateRange").attr("data-checkin");
        var urlNewCheckIn = moment(newCheckIn).format("YYYY-MM-DD[T]HH:mm:ss");
        var newCheckOut = $("#dateRange").attr("data-checkout");
        var urlNewCheckOut = moment(newCheckOut).format("YYYY-MM-DD[T]HH:mm:ss");
        var days = Math.ceil(moment.duration(moment(newCheckOut).diff(moment(newCheckIn))).asDays());
        var url = "/accommodation/" + $("#id").val() + "/reservations?headCount=" + newHeadCount + "&days=" + days + "&checkInDate=" + urlNewCheckIn + "&checkOutDate=" + urlNewCheckOut;
        $.ajax({
            url : url,
            type : "GET",
            cache   : false,
            success  : function(result, status){
                for(var i = 0; i<result.length; i++){
                    var roomId = result[i].roomId;
                    var isHeadCountCapable = result[i].isHeadCountCapable;
                    var isAvailable = result[i].isAvailable;
                    var position = $("#room" + roomId).find("#available"+roomId);
                    var price = position.attr("data-price");
                    var totalPrice = price * days;
                    totalPrice = totalPrice.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ",");
                    position.find(".fw-bold span:first").text(days + "박");
                    position.find(".fw-bold span:last").text(totalPrice + " 원");
                    position.find("#btn" + roomId).empty();
                    position.find("#btn" + roomId).attr("data-days", days);
                    position.find("#btn" + roomId).attr("data-headCount", newHeadCount);
                    position.find("#btn" + roomId).attr("data-checkin", urlNewCheckIn);
                    position.find("#btn" + roomId).attr("data-checkout", urlNewCheckOut);
                    if (isHeadCountCapable == "YES" && isAvailable == "YES"){
                        position.find("#btn" + roomId).append($(
                            '<div class="d-grid gap-2 d-md-flex justify-content-md-end">' +
                            '    <button class="btn btn-secondary me-md-2" type="button" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" data-bs-title="총 <b>' + newHeadCount + '</b>명, 총 <b>' + days + '</b>박<br><b>' + moment(newCheckIn).format('YYYY[년] MM[월] DD[일]') + ' ~<br>' + moment(newCheckOut).format('YYYY[년] MM[월] DD[일]') + '</b>" onclick="cart(' + roomId + ');">예약 바구니에 넣개</button>' +
                            '    <button class="btn btn-warning" type="button" data-bs-toggle="tooltip" data-bs-placement="bottom" data-bs-html="true" data-bs-title="총 <b>' + newHeadCount + '</b>명, 총 <b>' + days + '</b>박<br><b>' + moment(newCheckIn).format('YYYY[년] MM[월] DD[일]') + ' ~<br>' + moment(newCheckOut).format('YYYY[년] MM[월] DD[일]') + '</b>" onclick="reservation(' + roomId + ');">바로 예약하개</button>' +
                            '</div>'
                        ));
                    }else{
                        position.find("#btn" + roomId).append($(
                            '<div class="d-grid gap-2 d-md-flex justify-content-md-end">' +
                            '    <button class="btn btn-secondary" type="button" disabled>예약 조건이 맞지 않다냥</button>' +
                            '</div>'
                        ));
                    }
                }
                var tooltipTriggerList = [].slice.call(document.querySelectorAll('[data-bs-toggle="tooltip"]'))
                var tooltipList = tooltipTriggerList.map(function (tooltipTriggerEl) {
                  return new bootstrap.Tooltip(tooltipTriggerEl)
                });
            },
            error : function(status, error){
                alert(status.responseText);
            }
        });
    });
}

function cart(id){
    var days = $("#btn" + id).attr("data-days");
    var headCount = $("#btn" + id).attr("data-headCount");
    var checkIn = $("#btn" + id).attr("data-checkin");
    checkIn = moment(checkIn).format("YYYY-MM-DD[T]HH:mm:ss");
    var checkOut = $("#btn" + id).attr("data-checkout");
    checkOut = moment(checkOut).format("YYYY-MM-DD[T]HH:mm:ss");
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/user/cart/" + $("#id").val() + "/" + id;
    var formData = new FormData();
    formData.append("days", days);
    formData.append("headCount", headCount);
    formData.append("checkInDate", checkIn);
    formData.append("checkOutDate", checkOut);
    $.ajax({
        url : url,
        type : "POST",
        processData : false,
        contentType : false,
        data : formData,
        encType : "multipart/form-data",
        cache   : false,
        beforeSend : function(xhr){
           xhr.setRequestHeader(header, token);
        },
        success  : function(result, status){
           var result = confirm("장바구니에 담겼습니다.\n장바구니로 이동하시려면 '확인'을 누르시고, 계속 진행하시려면 '취소'를 누르세요.");
           if(result){
                location.href = "/user/cart";
           }
        },
        error : function(status, error){
            if(status.status == '401' || status.status == '403' || status.status == '404'){
                alert('로그인 후 이용해주세요');
                location.href='/member/login';
            } else{
                alert(status.responseText);
            }
        }
    });
}

function reservation(id){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var headCount = $("#btn" + id).attr("data-headCount");
    var checkIn = $("#btn" + id).attr("data-checkin");
    checkIn = moment(checkIn).format("YYYY-MM-DD[T]HH:mm:ss");
    var checkOut = $("#btn" + id).attr("data-checkout");
    checkOut = moment(checkOut).format("YYYY-MM-DD[T]HH:mm:ss");
    var days = $("#btn" + id).attr("data-days");
    var price = $("#available" + id).attr("data-price");
    var totalPrice = price * days;
    var url = "/user/reservation";
    var formData = new FormData();
    formData.append("reservationDate", moment().format("YYYY-MM-DD[T]HH:mm:ss"));
    formData.append("reservationTotalPrice", totalPrice);
    formData.append("reservationRoomList[0].accommodationId", $("#id").val());
    formData.append("reservationRoomList[0].roomId", id);
    formData.append("reservationRoomList[0].days", days);
    formData.append("reservationRoomList[0].headCount", headCount);
    formData.append("reservationRoomList[0].checkInDate", checkIn);
    formData.append("reservationRoomList[0].checkOutDate", checkOut);
    $.ajax({
        url : url,
        type : "POST",
        processData : false,
        contentType : false,
        data : formData,
        encType : "multipart/form-data",
        cache   : false,
        beforeSend : function(xhr){
            xhr.setRequestHeader(header, token);
        },
        success  : function(result, status){
            alert("예약이 완료되었습니다. 예약 내역 페이지로 이동합니다.");
            location.href = "/user/reservations";
        },
        error : function(status, error){
            if(status.status == '401' || status.status == '403' || status.status == '404'){
                alert('로그인 후 이용해주세요');
                location.href='/member/login';
            } else{
                alert(status.responseText);
            }
        }
    });
}