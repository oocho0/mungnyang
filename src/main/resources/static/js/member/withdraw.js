function request(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/member/withdraw";
    var paramData = {
        inputPassword : $("#password").val(),
    };

    var param = JSON.stringify(paramData);

    $.ajax({
        url      : url,
        type     : "POST",
        contentType : "application/json",
        data     : param,
        beforeSend : function(xhr){
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader(header, token);
        },
        dataType : "json",
        cache   : false,
        success  : function(result, status){
            console.log(result);
            alert('탈퇴가 완료되었습니다.');
            location.href='/';
        },
        error : function(jqXHR, status, error){
            console.log(status);
            if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요');
                location.href='/member/login';
            } else{
                alert(jqXHR.responseText);
            }
        }
    });
}