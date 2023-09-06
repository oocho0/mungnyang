function addFacility(){
    var facilityId = $(event.target).siblings("input").attr("id");
    var facilityName = adding(facilityId, "facility", "facility", "addFacility();", "deleteFacility();", "숙소");
}
function addRoomFacility(){
    var facilityId = $(event.target).siblings("input").attr("id");
    var roomIndex = Number($(event.target).closest(".accordion-item").attr('data-roomIndex'));
    var classFacility = "roomFacility"+ roomIndex;
    var idName = "room" + roomIndex + "-facility";
    var facilityName = adding(facilityId, classFacility, idName, "addRoomFacility();", "deleteRoomFacility();", "방");
}

function adding(facilityId, classFacility, idName, funcName, deleteFuncName, name){
    var addFacility = $("#"+facilityId).val();
    if(addFacility == "" || addFacility == null){
        return false;
    }
    var num = Number($("#"+facilityId).attr("data-facilityIndex"));

    $("#"+facilityId).closest(".col-4").append($(
    '   <span class="form-text ' + classFacility + '" id="' + idName + num + '" data-facilityIndex="' + num + '">' + addFacility +
    '       <a class="delete" style="cursor:pointer; text-decoration : none; color:inherit;" onclick="' + deleteFuncName + '">✖</a>' +
    '   </span>'
    ));
    var next = null;
    if(num % 2 == 0 ){
        next = $("#"+facilityId).closest(".row").find(".col-4:first");
    }else{
        next = $("#"+facilityId).closest(".row").find(".col-4:last");
    }
    $(next).append($(
    '<div class="input-group mb-3">' +
    '   <input type="text" class="form-control" data-error="' + name + '시설이" aria-label="' + name + ' 시설"" id="' + idName + (num+1) + '" placeholder="' + name + ' 시설을 추가하세요" data-facilityIndex="' + (num+1) + '">' +
    '   <button class="btn btn-outline-secondary" type="button" onclick="' + funcName + '">추가</button>' +
    '</div>'
    ));
    $("#"+facilityId).closest("div").detach();
    return addFacility;
}

function initFacility(i){
    var lastId = $("#room"+i+" .row:eq(-2)").find("input[type='text']:last").attr("id");
    var lastIndex = Number($("#"+lastId).attr('data-facilityIndex'));
    if(lastIndex == 0){
        return false;
    }
    $("#room" + i + " .row:eq(-2) .col-4").empty();
    $("#room" + i + " .row:eq(-2) .col-4:first").append($(
    '<div class="input-group mb-3">' +
    '    <input type="text" class="form-control" data-error="방 시설이" aria-label="방 시설" id="|room' + i + '-facility0|" placeholder="방 시설을 추가하세요." data-facilityIndex="0">' +
    '    <button class="btn btn-outline-secondary" type="button" onclick="addRoomFacility();">추가</button>' +
    '</div>'
    ));
}

function checkFacility(resultObject){
    var num = Number($("[onclick='addFacility();']").closest("div").find("input").attr("data-facilityIndex"));
    if(num == 0){
        resultObject["#facility0"] = $("#facility0").data('error') + " 입력되지 않았습니다.";
    }
}

function checkRoomFacility(resultObject, i){
   var num = Number($("#room" + i + " button[onclick='addRoomFacility();']").closest("div").find("input").attr("data-facilityIndex"));
   if(num == 0){
       resultObject["#room" + i + "-facility0"] = $("#room" + i + "-facility0").data('error') + " 입력되지 않았습니다.";
   }

}

function addFacilityData(formData){
    $(".facility").each(function(i, facility){
        formData.append("facilityList[" + i + "].name", $(facility).text());
        }
    }
}

function addRoomFacilityData(formData, i){
    $(".roomFacility"+ i).each(function(j, facility){
        formData.append("roomList["+ i + "].facilityList[" + j + "].name", $(facility).text());
    }
}