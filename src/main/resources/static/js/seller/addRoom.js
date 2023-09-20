function request(){
    let result = checkForm();
    if(result == false){
        return false;
    }

    var form = $("form")[0];
    var formData = new FormData(form);
    makeFormData(formData);

    var url = "/seller/accommodations/" + $("#accommodationId").val() + "/room";
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
    addReservationList(formData);
}

let resultObjectKeys = [];

function checkForm(){
    let resultObject = {};
    const checkLabels = ["#roomName", "#roomPrice", "#headCount"];

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

    if($("#roomName").val().length > 50){
        resultObject["#roomName"] = "50자 이내로 작성해주세요.";
    }
    singleImageCheck(resultObject);
    checkFacility(resultObject);

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
