$(document).ready(function(){
    checkPW();
});
function checkPW(){
    $("#password, #checkPW").keyup(function(){
        var password = $("#password").val();
        var checkPW = $("#checkPW").val();
        var init = $("#checkPW").attr("placeholder");
        if(checkPW != null && checkPW != "" && checkPW != init){
            if(password == checkPW){
                $("#resultPW").css('color','blue');
                $("#resultPW").text('비밀번호가 확인 되었습니다.');
                $("#checkPW").data('ok', 'Y');
            }else{
                $("#resultPW").css('color','red');
                $("#resultPW").text('비밀번호가 틀립니다.');
                $("#checkPW").data('ok', 'N');
            }
        }
        if(checkPW == "" || checkPW == init){
            $("#resultPW").text('');
            $("#checkPW").data('ok', 'N');
        }
    });
}
function checkForm(){
    if($("#checkPW").data('ok') == 'N'){
        alert("비밀번호를 확인하세요!");
        return false;
    }
    return true;
}