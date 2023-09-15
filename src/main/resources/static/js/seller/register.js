function request(){
    let result = checkForm();
    if(result == false){
        return false;
    }

    var form = $("form")[0];
    var formData = new FormData(form);
    makeFormData(formData);

    var url = "/seller/accommodation";
    $.ajax({
        url      : url,
        type     : "POST",
        processData : false,
        contentType : false,
        data : formData,
        encType : "multipart/form-data",
        cache   : false,
        success  : function(result, status){
            alert('등록이 완료되었습니다.');
            location.href='/seller/accommodations';
        },
        error : function(status, error){
            if(status.status == '401' || status.status == '403' || status.status == '404'){
                alert('로그인 후 이용해주세요');
                location.href='/member/login';
            } else{
                alert(status.responseText);
            }
        }
    });
}

function makeFormData(formData){
    addFormData(formData);
    addFacilityData(formData);
    var roomAmount = Number($("#roomAmount").val());
    var k = 0;
    $(".accordion-item").each(function(j, element){
        var i = $(element).attr("data-roomIndex");
        formData.append("roomList[" + k + "].roomName", $("#roomName" + i).val());
        formData.append("roomList[" + k + "].roomPrice", $("#roomPrice" + i).val());
        formData.append("roomList[" + k + "].headCount", $("#headCount" + i).val());
        formData.append("roomList[" + k + "].roomDetail", $("#roomDetail" + i).val());
        formData.append("roomList[" + k + "].roomStatus", $("[name='roomStatus" + i + "']:checked").val());
        addFormDataWithRoom(formData, i, k);
        addRoomFacilityData(formData, i, k);
        addReservationList(formData, i, k);
        k++;
    });
}

let resultObjectKeys = [];

function checkForm(){
    let resultObject = {};
    const checkLabels = ["#accommodationName", "#addressZipcode",
    "#addressMain", "#productAddressLat", "#productAddressLon", "#checkInTime", "#checkOutTime"];

    if(resultObjectKeys.length != 0){
        $.each(resultObjectKeys, function(index, value){
            if($(value).closest("div").parent("div").next("div").find(".error").text().length != 0){
                $(value).closest("div").parent("div").next("div").remove();
            }
        })
    }
    $.each(checkLabels, function(index, value){
        if($(value).val() == "" || $(value).val() == null){
            resultObject[value] = $(value).data('error') + " 입력되지 않았습니다.";
        }
    });

    if($("#accommodationName").val().length > 50){
        resultObject["#accommodationName"] = "50자 이내로 작성해주세요.";
    }
    if($("#smallCategoryId option:selected").text() == "소분류 선택"){
        resultObject["#smallCategoryId"] = $("#smallCategoryId").data('error') + " 입력되지 않았습니다.";
    }
    singleImageCheck(resultObject);
    checkFacility(resultObject);

    $(".accordion-item").each(function(j, element){
        var i = $(element).attr("data-roomIndex");
        const checkRoomLabels = ["#roomName", "#roomPrice", "#headCount"];
        $.each(checkRoomLabels, function(index, value){
            if($(value + i).val() == "" || $(value + i).val() == null){
                resultObject[value + i] = $(value + i).data('error') + " 입력되지 않았습니다.";
            }
        });

        if($("#roomName" + i).val().length > 50){
            resultObject["#roomName" + i] = "50자 이내로 작성해주세요.";
        }

        roomImageCheck(i, resultObject);
        checkRoomFacility(resultObject, i);
    });


    $.each(resultObject, function(key, value){
        if($(key).closest(".row").next("div").find(".error").text().length == 0){
            $(key).closest(".row").after($(
            '<div class="row g-3 align-items-center">' +
            '    <div class="col-3"></div>' +
            '    <div class="col-6 text-start">' +
            '        <span class="error">'+ value +'</span>' +
            '    </div>' +
            '</div>'));
        }
    });
    if(Object.keys(resultObject).length == 0){
        return true;
    }
    resultObjectKeys = Object.keys(resultObject);
    return false;
}

