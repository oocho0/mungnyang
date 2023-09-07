var num = 0;
var roomNum = new Map();
roomNum.set("room0", 0);

function makeRoomFacility(i){
    roomNum.set("room"+i, 0);
}

function deleteFacility(i){
    roomNum.delete("room"+i);
}

function addFacility(){
    var addFacility = $("#facilityInput").val();
    if(addFacility == "" || addFacility == null){
        return false;
    }
    $("#facilityList").append($(
        '   <span class="form-text facility" id="facility' + num + '" data-facilityIndex="' + num + '">' + addFacility +
        '       <a class="delete" style="cursor:pointer; text-decoration : none; color:inherit;" onclick="deleteFacility();">✖</a>' +
        '   </span>'
    ));
    num++;
    $("#facilityInput").val("");
}
function addRoomFacility(){
    var roomIndex = Number($(event.target).closest(".accordion-item").attr('data-roomIndex'));
    var addFacility = $("#room"+ roomIndex + "-facilityInput").val();
    if(addFacility == "" || addFacility == null){
        return false;
    }
    var num = roomNum.get("room"+roomIndex);
    $("#room" + roomIndex + "-facilityList").append($(
    '   <span class="form-text roomFacility' + roomIndex + '" id="room' + roomIndex + '-facility' + num + '" data-facilityIndex="' + num + '">' + addFacility +
    '       <a class="delete" style="cursor:pointer; text-decoration : none; color:inherit;" onclick="deleteFacility();">✖</a>' +
    '   </span>'
    ));
    roomNum.set("room"+roomIndex, num++);
    $("#room" + roomIndex + "-facilityInput").val("");
    console.log(roomNum);
}

function deleteFacility(){
    $(event.target).closest("span").remove();
    console.log(roomNum);
}

function checkFacility(resultObject){
    if($(".facility").length == 0){
        resultObject["#facilityInput"] = $("#facilityInput").data('error') + " 입력되지 않았습니다.";
    }
}

function checkRoomFacility(resultObject, i){
   if($(".roomFacility" + i).length == 0){
       resultObject["#room" + i + "-facilityInput"] = $("#room" + i + "-facilityInput").data('error') + " 입력되지 않았습니다.";
   }
}

function addFacilityData(formData){
    $(".facility").each(function(i, facility){
        formData.append("facilityList[" + i + "]", $(facility).text());
    });
}

function addRoomFacilityData(formData, i, k){
    $(".roomFacility"+ i).each(function(j, facility){
        formData.append("roomList["+ k + "].facilityList[" + j + "]", $(facility).text());
    });
}