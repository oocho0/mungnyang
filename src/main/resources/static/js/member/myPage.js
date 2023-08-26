$(document).ready(function(){
    checkPW();
});
function checkPW(){
    $("#newPassword, #checkPW").keyup(function(){
        var password = $("#newPassword").val();
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
    var curPw = $("#curPassword").val();
    var newPw = $("#newPassword").val();
    var checkPw = $("#checkPW").val();
    var initCur = $("#curPassword").attr("placeholder");
    var initNew = $("#newPassword").attr("placeholder");
    var initCheck = $("#checkPW").attr("placeholder");
    if((curPw == '' || curPw == initCur) && (newPw == '' || newPw == initNew) && (checkPw == '' || checkPw == initCheck)){
        $("#checkPW").data('ok', 'Y');
    }else{
        $("form").attr("action","/member/modify/Y");
    }
    if($("#checkPW").data('ok') == 'N'){
        alert("새 비밀번호를 확인하세요!");
        return false;
    }
    return true;
}