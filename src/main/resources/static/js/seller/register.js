function request(){
    let result = checkStoreDtoForm();
    if(result == false){
        return false;
    }
    var url = "/seller/accommodation";

    var form = $("form")[0];
    var formData = new FormData(form);
    formData = addFormData(formData);
     var roomAmount = Number($("#roomAmount").val());
    formData = addFormDataWithRoom(roomAmount, formData);

    $.ajax({
        url      : url,
        type     : "POST",
        processData : false,
        contentType : false,
        data : formData,
        encType : "multipart/form-data",
        cache   : false,
        beforeSend : function(){

        },
        success  : function(result, status){
            alert('등록이 완료되었습니다.');
            location.href='/seller/accommodations';
        },
        error : function(jqXHR, status, error){
                if(jqXHR.status == '401'){
                alert('로그인 후 이용해주세요');
                location.href='/member/login';
            } else{
                alert(error);
            }
        }
    });
}

function checkStoreDtoForm(){
    let resultObject = new Object();
    const checkLabels = ["#accommodationName", "#smallCategoryId", "#addressZipcode",
    "#addressMain", "#productAddressLat", "#productAddressLon"];

    $.each(checkLabels, function(index, value){
        if($(value).closest("div").parent("div").next("div").find(".error").text().length != 0){
            $(value).closest("div").parent("div").next("div").remove();
        }

        if($(value).val() == "" || $(value).val() == null){
            resultObject[value] = $(value).data('error') + " 입력되지 않았습니다.";
        }
    });
    if($("#input-image").closest("div").parent("div").next("div").find(".error").text().length != 0){
        $("#input-image").closest("div").parent("div").next("div").remove();
    }

    if($("#accommodationName").val().length > 50){
        resultObject["#accommodationName"] = "50자 이내로 작성해주세요.";
    }
    accommodationImageCheck(resultObject);

    var roomAmount = Number($("#roomAmount").val());
    const checkRoomLabels = ["#roomName", "#roomPrice"];
    for (var i = 1; i < roomAmount+1; i++){
        $.each(checkRoomLabels, function(index, value){
            if($(value + i).val() == "" || $(value + i).val() == null){
                resultObject[value + i] = $(value + i).data('error') + " 입력되지 않았습니다.";
            }
        });

        if($("#roomName" + i).val().length > 50){
            resultObject["#roomName" + i] = "50자 이내로 작성해주세요.";
        }

        roomImageCheck(i, resultObject);
    }


    $.each(resultObject, function(key, value){
        if($(key).closest("div").parent("div").next("div").find(".error").text().length == 0){
            $(key).closest("div").parent("div").after($(
            '<div class="row g-3 align-items-center">' +
            '    <div class="col-3"></div>' +
            '    <div class="col-6 text-start">' +
            '        <span class="error">'+ value +'</span>' +
            '    </div>' +
            '</div>'));
        }
    });
    if(resultObject != undefined && resultObject != null){
        return false;
    }
    return true;
}

