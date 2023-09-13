function request(){
    let result = checkDtoForm();
    if(result == false){
        return false;
    }
    var url = "/seller/accommodations/"+$("#accommodationId").val() + "/" + $("#roomId").val();

    var formData = new FormData($("form")[0]);
    addImageToFormData(formData);
    addFacilityData(formData);
    addReservationList(formData);

    $.ajax({
        url      : url,
        type     : "PUT",
        processData : false,
        contentType : false,
        data : formData,
        encType : "multipart/form-data",
        cache   : false,
        success  : function(result, status){
            alert('수정이 완료되었습니다.');
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

let resultObjectKeys = [];

function checkDtoForm(){
    let resultObject = new Object();
    const checkLabels = ["#roomName", "#roomPrice", "#roomPeople"];

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

    checkImages(resultObject);
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

function deleteRoom(){

    var result = confirm("정말 삭제하시겠습니까?\n삭제하시려면 '확인'버튼을 누르시고\n취소하시려면 '취소'버튼을 누르세요.")
    if(!result){
        return false;
    }
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/seller/accommodations/"+$("#accommodationId").val() + "/" + $("#roomId").val();

    $.ajax({
        url      : url,
        type     : "DELETE",
        cache   : false,
        beforeSend : function(xhr){
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader(header, token);
        },
        success  : function(result, status){
            alert('삭제가 완료되었습니다.');
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