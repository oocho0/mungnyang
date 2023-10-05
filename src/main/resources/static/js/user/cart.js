$(document).ready(function(){
   check();
});

function check(){
    $("#checkAll").on("change", function(){
        if ($("#checkAll").is(":checked")){
            $(".cartRoom").prop("checked", true);
        } else {
            $(".cartRoom").prop("checked", false);
        }
        totalPrice();
    });
    $(".cartRoom").on("change", function(){
        if ($(".cartRoom:checked").length == $(".cartRoom").length){
            $("#checkAll").prop("checked", true);
        } else {
            $("#checkAll").prop("checked", false);
        }
        totalPrice();
    });
}

function totalPrice(){
    var price = 0;
    $(".cartRoom:checked").each(function(index, element){
        if ($(element).attr("data-status") != "YES"){
            price += $(element).attr("data-price") * $(element).attr("data-days");
        }
    });
    $("#totalPrice").text(price.toString().replace(/\B(?=(\d{3})+(?!\d))/g, ','));
}

function deleteAll(){
    var result = confirm("전체 예약을 삭제 하시겠습니까?");
    if (!result){
        return;
    }
    var url = "/user/cart?cartRoomId=";
    var deletedCartRoomId = [];
    $(".cartRoom").each(function(index, element){
        if(index > 0){
            url += ",";
        }
        var cartRoomId = $(element).val();
        url += cartRoomId;
        deletedCartRoomId.push(cartRoomId);
    });
    deleteFunction(url, deletedCartRoomId);
}

function deleteCartRoom(cartRoomId){
    var result = confirm("예약을 삭제 하시겠습니까?");
    if (!result){
        return;
    }
    var url = "/user/cart?cartRoomId=" + cartRoomId;
    var deletedCartRoomId = [cartRoomId];
    deleteFunction(url, deletedCartRoomId);
}

function deleteSelect(){
    var result = confirm("선택한 예약들을 삭제 하시겠습니까?");
    if (!result){
        return;
    }
    var url = "/user/cart?cartRoomId=";
    var deletedCartRoomId = [];
    $(".cartRoom:checked").each(function(index, element){
        if(index > 0){
            url += ",";
        }
        var cartRoomId = $(element).val();
        url += cartRoomId;
        deletedCartRoomId.push(cartRoomId);
    });
    deleteFunction(url, deletedCartRoomId);
}

function deleteFunction(url, deletedCartRoomId){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    $.ajax({
        url : url,
        type : "DELETE",
        cache   : false,
        beforeSend : function(xhr){
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader(header, token);
        },
        success : function(result, status){
            alert("삭제가 완료되었습니다.");
            $.each(deletedCartRoomId, function(index, element){
                $("#cartRoom"+element).remove();
            });
            if ($(".card").length == 0){
                $("#contents").append($(
                    '<div class="card mb-3 pb-3">' +
                    '    <div class="card-body pt-5 pb-4">' +
                    '        아직 예약 바구니에 넣은 예약이 없습니멍!<br>추가해보세냥!' +
                    '    </div>' +
                    '</div>'
                ));
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

function reservation(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/user/reservation";

    var formData = new FormData();
    formData.append("reservationDate", moment().format("YYYY-MM-DD[T]HH:mm:ss"));
    var totalPrice = 0;
    if($(".cartRoom:checked").length == 0){
        alert("선택한 예약 상품이 없습니다.");
        return;
    }
    $(".cartRoom:checked").each(function(index, element){
        if ($(element).attr("data-status") == "YES"){
            alert("예약 불가한 상품이 있습니다.");
            return;
        }
        var checkIn = $(element).attr("data-checkin");
        var now = moment();
        checkIn = moment(checkIn);
        if (checkIn.isBefore(now)) {
            alert("예약 불가한 상품이 있습니다.");
            return;
        }
        checkIn = checkIn.format("YYYY-MM-DD[T]HH:mm:ss");
        var accommodationId = $(element).attr("id");
        var roomId = $(element).attr("name");
        var cartRoomId = $(element).val();
        var price = $(element).attr("data-price");
        var days = $(element).attr("data-days");
        var headCount = $(element).attr("data-headCount");
        var checkOut = $(element).attr("data-checkout");
        checkOut = moment(checkOut).format("YYYY-MM-DD[T]HH:mm:ss");
        totalPrice += price * days;
        formData.append("reservationRoomList[" + index + "].accommodationId", accommodationId);
        formData.append("reservationRoomList[" + index + "].roomId", roomId);
        formData.append("reservationRoomList[" + index + "].cartRoomId", cartRoomId);
        formData.append("reservationRoomList[" + index + "].days", days);
        formData.append("reservationRoomList[" + index + "].headCount", headCount);
        formData.append("reservationRoomList[" + index + "].checkInDate", checkIn);
        formData.append("reservationRoomList[" + index + "].checkOutDate", checkOut);
    });
    formData.append("reservationTotalPrice", totalPrice);
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