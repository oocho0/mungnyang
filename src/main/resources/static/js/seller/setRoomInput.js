$(document).ready(function(){
    setting();
});
function setting(){
    $("#roomAmount").on("change", function(){
        var roomAmount = Number($("#roomAmount").val());
        var id = $(".accordion-item:visible").last().children("input:first").attr('id');
        var currentMaxNumber = Number(id[id.length -1]);
        if(roomAmount == currentMaxNumber){
            return false;
        }
        var fa = true;
        if(roomAmount < currentMaxNumber){
            var result = confirm("입력해둔 방 정보가 사라집니다.\n진행하시려면 '확인'버튼을 누르시고\n유지하시려면 '취소'버튼을 누르세요.");
            if(result){
                for(var i = roomAmount; i<currentMaxNumber+1; i++){
                    $("#roomName" + i).val('');
                    $("#roomPrice$" + i).val('');
                    $("#roomDetail$" + i).val('');
                    deleteAllImages(i);
                    initFacility(i);
                    $("#room" + i).hide();
                }
            }else{
                fa = false;
            }
        }
        if(fa == false){
            return false;
        }

        for(var i = 2; i < roomAmount; i++){
            $("#room"+i).show();
        }
    });
}