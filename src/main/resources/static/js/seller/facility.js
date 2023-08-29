function addFacility(){
    var addFacility = $("#addButton").siblings("input").val();
    var facilityNum = $("#addButton").siblings("input").attr("id");
    var num = Number(facilityNum[facilityNum.length -1]);
    $("#addButton").closest("div").attr('class', 'form-check form-switch');
    $("#addButton").closest("div").append($(
    '   <input class="form-check-input facility" type="checkbox" role="switch" id="facility' + facilityNum + '"/>' +
    '   <label class="form-check-label" for="facility' + facilityNum + '">' + addFacility + '</label>'
    ));
    $("#addButton").closest("div").children(".form-control, button").detach();
    var next = null;
    if(num % 2 == 0 ){
        next = "#odd";
    }else{
        next = "#even";
    }
    $(next).append(
    '<div class="input-group mb-3">' +
    '   <input type="text" class="form-control" aria-label="숙소 시설" id="facility' + (num+1) + '"' +
    '       placeholder="추가 숙소 시설" aria-describedby="button-addon2">' +
    '   <button id="addButton" class="btn btn-outline-secondary" type="button" onclick="addFacility();">추가</button>' +
    '</div>'
    )
}