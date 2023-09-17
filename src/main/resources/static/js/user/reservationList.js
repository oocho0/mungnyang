function deleteReservation(id){
    var result = confirm("예약을 취소 하시겠습니까?\n계속 진행하시려면 '확인'버튼을 누르시고\n취소하시려면 '취소'버튼을 누르세요.")
    if(!result){
        return false;
    }
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/user/reservation/"+ id;

    $.ajax({
        url      : url,
        type     : "DELETE",
        cache   : false,
        beforeSend : function(xhr){
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader(header, token);
        },
        success  : function(result, status){
            alert('예약 취소가 완료되었습니다.');
            location.href='/user/reservations';
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