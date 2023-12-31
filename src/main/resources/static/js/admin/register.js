function request(){
    let result = checkStoreDtoForm();
    if(result == false){
        return false;
    }
    var url = "/admin/store";

    var formData = new FormData($("form")[0]);
    addFormData(formData);

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
            location.href='/admin/stores';
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

let resultObjectKeys = [];

function checkStoreDtoForm(){
    let resultObject = new Object();
    const checkLabels = ["#storeName", "#addressZipcode", "#addressMain", "#productAddressLat", "#productAddressLon"];

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

    if($("#smallCategory").is(":disabled") == true || $("#smallCategory option:selected").text() == "소분류 선택"){
        resultObject["#smallCategory"] = $("#smallCategory").data('error') + " 입력되지 않았습니다.";
    }

    if($("#storeName").val().length > 50){
        resultObject["#storeName"] = "50자 이내로 작성해주세요.";
    }

    var files = getFileArray();
    var count = 0;
    for(var i = 0; i < files.length; i++){
        if(files[i].is_delete == true){
            count++;
        }
    };
    if(files == null || files.length == count){
        resultObject["#input-image"] = "이미지를 1개 이상 업로드해주세요."
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
    if(Object.keys(resultObject).length == 0){
        return true;
    }
    resultObjectKeys = Object.keys(resultObject);
    return false;
}