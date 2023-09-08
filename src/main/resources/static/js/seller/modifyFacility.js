var facilityArray = [];

class facility {
    constructor(id, facilityName){
        this.id = id;
        this.is_delete = "N";
        this.facilityName = facilityName;
    }
}

$(document).ready(function(){
   var lastIndex = Number($(".facility:last").attr("data-facilityIndex"));
   for(var i = 0; i < lastIndex+1; i++){
       var facilityId = $("#facility" + i).attr("data-id");
       var facilityName = $("#facility" + i).find("span").text();
       facilityArray.push(new facility(facilityId, facilityName));
   }
});

function deleteFacility(){
    var index = Number($(event.target).closest("span").attr("data-facilityIndex"));
    facilityArray[index].is_delete = "Y";
    console.log(facilityArray);
    $("#facility" + index).remove();
}

function addFacility(){
    var addFacility = $("#facilityInput").val();
    if(addFacility == "" || addFacility == null){
        return false;
    }
    var num = facilityArray.length;
    $("#facilityList").append($(
        '   <span class="form-text facility" id="facility' + num + '" data-facilityIndex="' + num + '">' +
        '       <span>' + addFacility + '</span>' +
        '       <a class="delete" style="cursor:pointer; text-decoration : none; color:inherit;" onclick="deleteFacility();">✖</a>' +
        '   </span>'
    ));
    facilityArray.push(new facility(null, addFacility));
    $("#facilityInput").val("");
}

function checkFacility(resultObject){
    if($(".facility").length == 0){
        resultObject["#facilityInput"] = $("#facilityInput").data('error') + " 입력되지 않았습니다.";
    }
}

function addFacilityData(formData){
    for(var i = 0 ; i < facilityArray.length; i++){
        if(facilityArray[i].id != null){
            formData.append("facilityList[" + i + "].facilityId", facilityArray[i].id);
        }
        formData.append("facilityList[" + i + "].isDelete", facilityArray[i].is_delete);
        if(facilityArray[i].facilityName != null){
            formData.append("facilityList[" + i + "].facilityName", facilityArray[i].facilityName);
        }
    }
}