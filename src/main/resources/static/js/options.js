$(document).ready(function(){
    init();
});

function init(){
    $("#bigCategory .radio1").on("click", function(){
        if (!$(this).hasClass("active")) {
            var id = $(this).attr("data-id");
            $("#storeSelectedOptions span[data-bigId='분류']").each(function(index, element){
                $(element).remove();
            });
            $(this).closest(".card-body").find(".active").removeClass("active");
            $(this).addClass("active");
            findCategory(id);
        }
    });
    $("#storeState .radio1").on("click", function(){
        if (!$(this).hasClass("active")) {
            var id = $(this).attr("data-id");
            $("#storeSelectedOptions span[data-bigId='주소']").each(function(index, element){
                $(element).remove();
            });
            $(this).closest(".card-body").find(".active").removeClass("active");
            $(this).addClass("active");
            findCityForStore(id);
        }
    });
    $("#accommodationState .radio1").on("click", function(){
        if (!$(this).hasClass("active")) {
            var id = $(this).attr("data-id");
            $("#accommodationSelectedOptions span[data-bigId='주소']").each(function(index, element){
                $(element).remove();
            });
            $(this).closest(".card-body").find(".active").removeClass("active");
            $(this).addClass("active");
            findCity(id);
        }
    });
    $("#accommodationCategory .multi1").on("click", function(){
        var id = $(this).attr("data-id");
        if ($(this).hasClass("active")) {
            $("#accommodationSelectedOptions span[data-bigId='분류'][data-spanId='" + id + "']").remove();
            $(this).removeClass("active");
        } else {
            $("#accommodationSelectedOptions").append($(
                '<span class="px-2" data-bigId="분류" data-spanId="' + id + '">' + $(this).find("span:first").text() + '</span>'
            ));
            $(this).addClass("active");
        }
    });
}
function selectStoreCategory(){
    $("#smallCategory .multi1").on("click", function(){
        var parent = $(this).closest(".card-group").find("#bigCategory .active");
        var id = $(this).attr("data-id");
        if ($(this).hasClass("active")) {
            $("#storeSelectedOptions span[data-bigId='분류'][data-spanId='" + id + "']").remove();
            $(this).removeClass("active");
        } else {
            $("#storeSelectedOptions").append($(
                '<span class="px-2" data-bigId="분류" data-spanId="' + id + '">' + parent.find("span:first").text() + ' / ' + $(this).find("span:first").text() + '</span>'
            ));
            $(this).addClass("active");
        }
    });
}
function selectStoreCity(){
    $("#storeCity .multi1").on("click", function(){
        var parent = $(this).closest(".card-group").find("#storeState .active");
        var id = $(this).attr("data-id");
        if ($(this).hasClass("active")) {
            $("#storeSelectedOptions span[data-bigId='주소'][data-spanId='" + id + "']").remove();
            $(this).removeClass("active");
        } else {
            $("#storeSelectedOptions").append($(
                '<span class="px-2" data-bigId="주소" data-spanId="' + id + '">' + parent.find("span:first").text() + ' / ' + $(this).find("span:first").text() + '</span>'
            ));
            $(this).addClass("active");
        }
    });
}
function selectAccommodationCity(){
    $("#accommodationCity .multi1").on("click", function(){
        var parent = $(this).closest(".card-group").find("#accommodationState .active");
        var id = $(this).attr("data-id");
        if ($(this).hasClass("active")) {
            $("#accommodationSelectedOptions span[data-bigId='주소'][data-spanId='" + id + "']").remove();
            $(this).removeClass("active");
        } else {
            $("#accommodationSelectedOptions").append($(
                '<span class="px-2" data-bigId="주소" data-spanId="' + id + '">' + parent.find("span:first").text() + ' / ' + $(this).find("span:first").text() + '</span>'
            ));
            $(this).addClass("active");
        }
    });
}

function findCategory(id){
    var url = "/search/category/" + id;
    $.ajax({
       url : url,
       type : "GET",
       async : false,
       dataType : "json",
       cache : false,
       success : function(result, status){
            $("#smallCategory").empty();
            $.each(result, function(key, value){
                $("#smallCategory").append($(
                    '<a data-id="' + value.smallCategoryId + '" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center multi1">' +
                    '   <span>' + value.name + '</span>' +
                    '</a>'
                ));
                if(value.productCount != 0){
                    $("#smallCategory [data-id='" + value.smallCategoryId + "']").append($('<span class="badge bg-primary rounded-pill">' + value.productCount + '</span>'));
                }
            });
            selectStoreCategory();
       },
       error : function(status, error){
           alert(status.responseText);
       }
    });
}

function findCityForStore(id){
    var url = "/search/store/" + id;
    $.ajax({
       url : url,
       type : "GET",
       async : false,
       dataType : "json",
       cache : false,
       success : function(result, status){
            $("#storeCity").empty();
            $.each(result, function(key, value){
                $("#storeCity").append($(
                    '<a data-id="' + value.cityId + '" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center multi1">' +
                    '   <span>' + value.name + '</span>' +
                    '</a>'
                ));
                if(value.productCount != 0){
                    $("#storeCity [data-id='" + value.cityId + "']").append($('<span class="badge bg-primary rounded-pill">' + value.productCount + '</span>'));
                }
            });
            selectStoreCity();
       },
       error : function(status, error){
           alert(status.responseText);
       }
    });
}

function findCity(id){
    var url = "/search/accommodation/" + id;
    $.ajax({
       url : url,
       type : "GET",
       async : false,
       dataType : "json",
       cache : false,
       success : function(result, status){
            $("#accommodationCity").empty();
            $.each(result, function(key, value){
                $("#accommodationCity").append($(
                    '<a data-id="' + value.cityId + '" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center multi1">' +
                    '   <span>' + value.name + '</span>' +
                    '</a>'
                ));
                if(value.productCount != 0){
                     $("#accommodationCity [data-id='" + value.cityId + "']").append($('<span class="badge bg-primary rounded-pill">' + value.productCount + '</span>'));
                }
            });
            selectAccommodationCity();
       },
       error : function(status, error){
           alert(status.responseText);
       }
    });
}

function searchStore(){
    if($(".accordion-header").attr("hidden") == "hidden"){
        $(".accordion-header").removeAttr("hidden");
    }
    if($("#searchResult").attr("hidden") == "hidden"){
        $("#searchResult").removeAttr("hidden");
    }
    $("button.accordion-button").text("편의 시설 검색 결과");
}
function searchAccommodation(){
    if($(".accordion-header").attr("hidden") == "hidden"){
        $(".accordion-header").removeAttr("hidden");
    }
    if($("#searchResult").attr("hidden") == "hidden"){
        $("#searchResult").removeAttr("hidden");
    }
    $("button.accordion-button").text("숙소 검색 결과");
}