function request(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/member/withdraw-kakao";

    $.ajax({
        url      : url,
        type     : "DELETE",
        contentType : "application/json",
        beforeSend : function(xhr){
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader(header, token);
        },
        dataType : "text",
        cache   : false,
        success  : function(result, status){
            alert('탈퇴가 완료되었습니다.');
            location.href='/';
        },
        error : function(status, error){
                if(status.status == '401'){
                alert('로그인 후 이용해주세요');
                location.href='/member/login';
            } else{
                alert(status.responseText);
            }
        }
    });
}