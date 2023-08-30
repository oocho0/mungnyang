function addFacility(){
    var addFacility = $(event.target).siblings("input").val();
    var facilityNum = $(event.target).siblings("input").attr("id");
    var num = Number(facilityNum[facilityNum.length -1]);
    $(event.target).closest("div").attr('class', 'form-check form-switch');
    $(event.target).closest("div").append($(
    '   <input class="form-check-input facility" type="checkbox" role="switch" id="facility' + facilityNum + '"/>' +
    '   <label class="form-check-label" for="facility' + facilityNum + '">' + addFacility + '</label>'
    ));
    $(event.target).closest("div").children(".form-control, button").detach();
    var next = null;
    if(num % 2 == 0 ){
        next = "#odd";
    }else{
        next = "#even";
    }
    $(next).append($(
    '<div class="input-group mb-3">' +
    '   <input type="text" class="form-control" aria-label="숙소 시설" id="facility' + (num+1) + '" placeholder="추가 숙소 시설">' +
    '   <button class="btn btn-outline-secondary" type="button" onclick="addFacility();">추가</button>' +
    '</div>'
    ));
}

function initFacility(i){
    var lastId = $("input[type='text']").attr("id");
    var lastIndex = Number(lastId[lastId.length -1]);
    for(var j = 1; j < 8; j++){
        $("#room"+ i + "-facility" + j).prop("checked", false);
    }
    if(lastIndex == 8){
        return false;
    }
    $("#room"+ i + "-facility8").closest("div").attr('class', 'input-group mb-3');
    $("#room"+ i + "-facility8").closest("div").append($(
    '    <input type="text" class="form-control" aria-label="숙소 시설" th:id="|room' + i + '-facility8|" placeholder="추가 숙소 시설">' +
    '    <button class="btn btn-outline-secondary" type="button" onclick="addFacility();">추가</button>'
    ));
    $("#room"+ i + "-facility8").closest("div").children(".form-check-input, label").detach();
    for(var j = 9; j < lastIndex+1; j++){
        $("#room" + i + "-facility" + j).closest("div").detach();
    }

}