function request(){
    let result = checkStoreDtoForm();
    if(result == false){
        return false;
    }
    var url = "/seller/accommodation";

    var form = $("form")[0];
    var formData = new FormData(form);
    formData = addFormData(formData);

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
    if(resultObject.length != undefined && resultObject.length != null){
        return false;
    }
    return true;
}

