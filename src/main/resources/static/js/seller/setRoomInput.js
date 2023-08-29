$(document).ready(function(){
    setting();
});
function setting(){
    $("#roomAmount").on("change", function(){
        var roomAmount = $("#roomAmount option:selected").val();
        $.each(roomAmount, function(index, value)){
            $("#roomAmount").closest("div").parent("div").after($(
            '<div class="row g-3 py-2" id="room' + index + ''">' +
            '   <div class="col-3 text-end">' +
            '       <label class="col-form-label" for="roomName'+ index +'">방' + index + ' 이름</label>'
            '   </div>' +
            '   <div class="col-3">' +
            '       <input' +
            '   </div>' +
            '</div>'
            ));
        });
    });
}