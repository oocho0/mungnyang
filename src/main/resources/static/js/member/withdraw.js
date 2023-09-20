function request(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/member/withdraw";
    var paramData = {
        inputPassword : $("#password").val(),
    };

    var param = JSON.stringify(paramData);

    var deleteParamData = {
        deleteAccount : "deleteYes",
    };
    var deleteParam = JSON.stringify(deleteParamData);

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
            $.ajax({
                url : "/member/delete",
                type : "POST",
                contentType : "application/json",
                data : deleteParam,
                beforeSend : function(xhr){
                    /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
                    xhr.setRequestHeader(header, token);
                }
            });
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