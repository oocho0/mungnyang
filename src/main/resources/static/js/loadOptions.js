$(document).ready(function() {
    loadSmallCategory();
    loadCity();
});

function loadSmallCategory() {
    $("#bigCategory").on("change", function(){
        $("#smallCategory").attr('disabled', true);
        $("#smallCategory").empty();
        if($("#bigCategory option:first").text() == "대분류 전체"){
            $("#smallCategory").append(`<option selected>소분류 전체</option>`);
        }
        if($("#bigCategory option:first").text() == "대분류 선택"){
            $("#smallCategory").append(`<option selected>소분류 선택</option>`);
        }
        var bigCategoryId = $("#bigCategory option:selected").val();
        if(bigCategoryId == "대분류 선택" || bigCategoryId == "대분류 전체"){
            return;
        }
        var data = requestCategory(bigCategoryId);
        $.each(data, function(key, value){
            var option = `<option value="${value.smallCategoryId}">${value.name}</option>`;
            $("#smallCategory").append(option);
        });
    });
}
function requestCategory(bigCategoryId){
    var url = "/category/" + bigCategoryId;
    var data = null;
    $.ajax({
        url : url,
        type : "GET",
        async : false,
        dataType : "json",
        cache : false,
        success : function(result, status){
            $("#smallCategory").removeAttr('disabled');
            data = result;
        },
        error : function(status, error){
            alert(status.responseText);
        }
    });
    return data;
}

function loadCity() {
    $("#state").on("change", function(){
        $("#city").attr('disabled', true);
        $("#city").empty();
        $("#city").append(`<option selected>시/군/구 전체</option>`);
        var stateId = $("#state option:selected").val();
        if(stateId == "시/도 전체"){
            return;
        }
        var data = request(stateId);
        $.each(data, function(key, value){
            var option = `<option value="${value.cityId}">${value.name}</option>`;
            $("#city").append(option);
        });
    });

}
function request(stateId){
    var url = "/state/" + stateId;
    var data = null;
    $.ajax({
        url : url,
        type : "GET",
        async : false,
        dataType : "json",
        cache : false,
        success : function(result, status){
            $("#city").removeAttr('disabled');
            data = result;
        },
        error : function(status, error){
            alert(status.responseText);
        }
    });
    return data;
}