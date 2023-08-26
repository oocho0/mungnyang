$(document).ready(function() {
    telInputEvent();
});

function telInputEvent(){
    $("#tel1, #tel2, #tel3").keyup(function(){
        var telA = $("#tel1").val();
        var telB = $("#tel2").val() + "-";
        var telC = $("#tel3").val();
        var telAll = telA + telB + telC;
        $("#tel").val(telAll);
    });
}