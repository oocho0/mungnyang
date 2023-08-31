function addFacility(){
    var facilityId = $(event.target).siblings("input").attr("id");
    adding(facilityId, "facility", "facility", "addFacility();");
}
function addRoomFacility(){
    var facilityId = $(event.target).siblings("input").attr("id");
    var roomIndex = Number($(event.target).closest(".accordion-item").attr('data-roomIndex'));
    var classFacility = "roomFacility"+ roomIndex;
    var idName = "room" + roomIndex + "-facility";
    adding(facilityId, classFacility, idName, "addRoomFacility();");
}

function adding(facilityId, classFacility, idName, funcName){
    var addFacility = $("#"+facilityId).val();
    if(addFacility == "" || addFacility == null){
        return false;
    }
    var num = Number($("#"+facilityId).attr("data-facilityIndex"));

    $("#"+facilityId).closest("div").attr('class', 'form-check form-switch');
    $("#"+facilityId).closest("div").append($(
    '   <input class="form-check-input ' + classFacility + '" type="checkbox" role="switch" id="' + idName + num + '" data-facilityIndex="' + num + '"/>' +
    '   <label class="form-check-label" for="' + idName + num + '">' + addFacility + '</label>'
    ));
    var next = null;
    if(num % 2 == 0 ){
        next = $("#"+facilityId).closest("div").parent("div").parent("div").find(".col-4:first");
    }else{
        next = $("#"+facilityId).closest("div").parent("div").parent("div").find(".col-4:last");
    }
    $(next).append($(
    '<div class="input-group mb-3">' +
    '   <input type="text" class="form-control" aria-label="숙소 시설" id="' + idName + (num+1) + '" placeholder="추가 숙소 시설" data-facilityIndex="' + (num+1) + '">' +
    '   <button class="btn btn-outline-secondary" type="button" onclick="' + funcName + '">추가</button>' +
    '</div>'
    ));
    $("#"+facilityId).closest("div").children(".form-control, button").detach();
}

function initFacility(i){
    var lastId = $("#room"+i+" .row:last").find("input[type='text']:last").attr("id");
    var lastIndex = Number($("#"+lastId).attr('data-facilityIndex'));
    for(var j = 1; j < 8; j++){
        $("#room"+ i + "-facility" + j).prop("checked", false);
    }
    if(lastIndex == 8){
        return false;
    }
    $("#room"+ i + "-facility8").closest("div").attr('class', 'input-group mb-3');
    $("#room"+ i + "-facility8").closest("div").append($(
    '    <input type="text" class="form-control" aria-label="숙소 시설" th:id="|room' + i + '-facility8|" placeholder="추가 숙소 시설" data-facilityIndex="8">' +
    '    <button class="btn btn-outline-secondary" type="button" onclick="addFacility();">추가</button>'
    ));
    $("#room"+ i + "-facility8").closest("div").children(".form-check-input, label").detach();


    for(var j = 9; j < lastIndex+1; j++){
        $("#room" + i + "-facility" + j).closest("div").detach();
    }

}

function addFacilityData(formData){
    var facilityObject = new Object();
    var num = Number($("[onclick='addFacility();']").attr("data-facilityIndex"));

    for(var i = 1; i < num; i++){
        facilityObject["facilityName"] = $("label [for='facility'"+i+"]").val();
        if($("#facility"+i).prop("checked")){
            facilityObject["facilityIsExist"] = "YES";
        }else{
            facilityObject["facilityIsExist"] = "NO";
        }
        formData.append("facility", facilityObject);
    }
}

function addRoomFacilityData(roomObject, roomNo){
    var facilityIdPrefix = "room" + roomNo + "-facility";
    var lastId = $("#room" + roomNo + " .row:last").find("input[type='text']:last").attr("id");
    var lastIndex = Number($("#"+lastId).attr('data-facilityIndex'));
    var roomFacility = new Object();
    var array = new Array();
    for(var i = 1; i < lastIndex; i++){

        roomFacility["facilityName"] = $("label [for='"+facilityIdPrefix+i+"']").val();
        if($("#"+facilityIdPrefix + i).prop("checked")){
            roomFacility["facilityIsExist"] = "YES";
        }else{
            roomFacility["facilityIsExist"] = "NO";
        }
        array.push(roomFacility);
    }
    roomObject["roomFacilityList"] = array;
}