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
    $("#accommodationCategoryAll").on("click", function(){
        if (!$(this).hasClass("active")) {
            $("#accommodationCategory .multi1").each(function(index, element){
                if($(element).hasClass("active")){
                }else{
                    $(element).trigger("click");
                }
            });
            $(this).addClass("active");
        }else{
            $("#accommodationCategory .multi1").each(function(index, element){
                $(element).trigger("click");
            });
            $(this).removeClass("active");
        }
    });
    $("#accommodationCategory .multi1").on("click", function(){
        var id = $(this).attr("data-id");
        if ($(this).hasClass("active")) {
            $("#accommodationSelectedOptions span[data-bigId='분류'][data-spanId='" + id + "']").remove();
            $(this).removeClass("active");
            $("#accommodationCategoryAll").removeClass("active");
        } else {
            $("#accommodationSelectedOptions").append($(
                '<span class="px-2" data-bigId="분류" data-spanId="' + id + '">' + $(this).find("span:first").text() + '</span>'
            ));
            $(this).addClass("active");
            findAccommodationState();
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
            $("#smallCategoryAll").removeClass("active");
        } else {
            $("#storeSelectedOptions").append($(
                '<span class="px-2" data-bigId="분류" data-spanId="' + id + '">' + parent.find("span:first").text() + ' / ' + $(this).find("span:first").text() + '</span>'
            ));
            $(this).addClass("active");
            findStoreState();
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
            $("#storeCityAll").removeClass("active");
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
            $("#smallCategory").append($(
                '<a id="smallCategoryAll" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">' +
                '   <span>소분류 전체</span>' +
                '</a>'
            ));
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
            $("#smallCategoryAll").on("click", function(){
                if (!$(this).hasClass("active")) {
                    $("#smallCategory .multi1").each(function(index, element){
                        if($(element).hasClass("active")){
                        }else{
                            $(element).trigger("click");
                        }
                    });
                    $(this).addClass("active");
                }else{
                    $("#smallCategory .multi1").each(function(index, element){
                        $(element).trigger("click");
                    });
                    $(this).removeClass("active");
                }
            });
            selectStoreCategory();
       },
       error : function(status, error){
           alert(status.responseText);
       }
    });
}

function findStoreState(){
    var url = "/search/store/state?";
    var categoryParam = "smallCategoryId=";
    $("#smallCategory .multi1.active").each(function(index, element){
        if(index == 0){
            categoryParam += $(element).attr("data-id");
        }else{
            categoryParam += "," + $(element).attr("data-id");
        }
    });
    url += categoryParam;
    $.ajax({
        url : url,
        type : "GET",
        async : false,
        dataType : "json",
        cache : false,
        success : function(result, status){
            $("#storeState").empty();
            $.each(result, function(key, value){
                $("#storeState").append($(
                    '<a data-id="' + value.stateId + '" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center radio1">'+
                    '    <span>' + value.name + '</span>'+
                    '</a>'
                ));
                if(value.productCount != 0){
                    $("#storeState [data-id='" + value.stateId + "']").append($('<span class="badge bg-primary rounded-pill">' + value.productCount + '</span>'));
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
        },
        error : function(status, error){
           alert(status.responseText);
        }
    });
}

function findCityForStore(id){
    var url = "/search/store/" + id;
    var cityParam = "?smallCategoryId=";
    $("#smallCategory .multi1.active").each(function(index, element){
        if(index == 0){
            cityParam += $(element).attr("data-id");
        }else{
            cityParam += "," + $(element).attr("data-id");
        }
    });
    url += cityParam;
    $.ajax({
       url : url,
       type : "GET",
       async : false,
       dataType : "json",
       cache : false,
       success : function(result, status){
            $("#storeCity").empty();
            $("#storeCity").append($(
                '<a id="storeCityAll" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">' +
                '   <span>시/군/구 전체</span>' +
                '</a>'
            ));
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
            $("#storeCityAll").on("click", function(){
                if (!$(this).hasClass("active")) {
                    $("#storeCity .multi1").each(function(index, element){
                        if($(element).hasClass("active")){
                        }else{
                            $(element).trigger("click");
                        }
                    });
                    $(this).addClass("active");
                }else{
                    $("#storeCity .multi1").each(function(index, element){
                        $(element).trigger("click");
                    });
                    $(this).removeClass("active");
                }
            });
            selectStoreCity();
       },
       error : function(status, error){
           alert(status.responseText);
       }
    });
}


function findAccommodationState(){
    var url = "/search/accommodation/state?";
    var categoryParam = "accommodationCategoryId=";
    $("#accommodationCategory .multi1.active").each(function(index, element){
        if(index == 0){
            categoryParam += $(element).attr("data-id");
        }else{
            categoryParam += "," + $(element).attr("data-id");
        }
    });
    url += categoryParam;
    $.ajax({
        url : url,
        type : "GET",
        async : false,
        dataType : "json",
        cache : false,
        success : function(result, status){
            $("#accommodationState").empty();
            $.each(result, function(key, value){
                $("#accommodationState").append($(
                    '<a data-id="' + value.stateId + '" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center radio1">'+
                    '    <span>' + value.name + '</span>'+
                    '</a>'
                ));
                if(value.productCount != 0){
                    $("#accommodationState [data-id='" + value.stateId + "']").append($('<span class="badge bg-primary rounded-pill">' + value.productCount + '</span>'));
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
        },
        error : function(status, error){
           alert(status.responseText);
        }
    });
}

function findCity(id){
    var url = "/search/accommodation/" + id;
    var cityParam = "?accommodationCategoryId=";
    $("#accommodationCategory .multi1.active").each(function(index, element){
        if(index == 0){
            cityParam += $(element).attr("data-id");
        }else{
            cityParam += "," + $(element).attr("data-id");
        }
    });
    url += cityParam;
    $.ajax({
       url : url,
       type : "GET",
       async : false,
       dataType : "json",
       cache : false,
       success : function(result, status){
            $("#accommodationCity").empty();
            $("#accommodationCity").append($(
                '<a id="accommodationCityAll" class="list-group-item list-group-item-action d-flex justify-content-between align-items-center">' +
                '   <span>시/군/구 전체</span>' +
                '</a>'
            ));
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
            $("#accommodationCityAll").on("click", function(){
                if (!$(this).hasClass("active")) {
                    $("#accommodationCity .multi1").each(function(index, element){
                        if($(element).hasClass("active")){
                        }else{
                            $(element).trigger("click");
                        }
                    });
                    $(this).addClass("active");
                }else{
                    $("#accommodationCity .multi1").each(function(index, element){
                        $(element).trigger("click");
                    });
                    $(this).removeClass("active");
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
    var url = "/search/store?";
    if($("#storeSelectedOptions span").length == 0){
        alert("검색 조건을 선택하세멍!")
    }
    if($("#smallCategory a").length == 0){
        alert("대분류를 선택하세멍!");
        return;
    }
    if($("#smallCategory .active").length == 0){
        alert("소분류를 선택하세멍!");
        return;
    }
    if($("#storeCity a").length == 0){
        alert("시/도를 선택하세멍!");
        return;
    }
    if($("#storeCity .active").length == 0){
        alert("시/군/구를 선택하세멍!");
        return;
    }
    var categoryParam = "categoryId=";
    $("#smallCategory .multi1.active").each(function(index, element){
        if(index == 0){
            categoryParam += $(element).attr("data-id");
        }else{
            categoryParam += "," + $(element).attr("data-id");
        }
    });
    var cityParam = "&cityId=";
    $("#storeCity .multi1.active").each(function(index, element){
        if(index == 0){
            cityParam += $(element).attr("data-id");
        }else{
            cityParam += "," + $(element).attr("data-id");
        }
    })
    url += categoryParam + cityParam;
    $.ajax({
        url : url,
        type : "GET",
        async : false,
        dataType : "json",
        cache : false,
        success : function(result, status){
            if($(".accordion-header").attr("hidden") == "hidden"){
                $(".accordion-header").removeAttr("hidden");
            }
            if($("#searchResult").attr("hidden") == "hidden"){
                $("#searchResult").removeAttr("hidden");
            }
            $("button.accordion-button").text("편의 시설 검색 결과");
            $("#resultList").empty();
            if(result.length == 0){
                $("#resultList").append($(
                    '<li style="height: 500px; line-height: 500px;" class="text-center">검색 결과가 없습니다.</li>'
                ));
                map.relayout();
                return false;
            }
            var positions = [];
            $.each(result, function(key, value){
                var status = '';
                if(value.status != "OPEN"){
                    status = '<span class="badge bg-secondary rounded-pill" style="font-size: 9px">휴업중</span>';
                }
                $("#resultList").append($(
                    '<li class="list-group-item list-group-item-action result1 py-2 px-4" id="' + value.id + '">' +
                    '    <div class="row g-3">' +
                    '        <div class="col-3">' +
                    '            <img style="width: 100%; height: 120px; object-fit: cover;" src="' + value.repImageUrl + '"/>' +
                    '        </div>' +
                    '        <div class="col-9">' +
                    '            <div class="d-flex justify-content-between align-items-center py-2">' +
                    '                <h5 pe-1>' + value.name + status + '</h5>' +
                    '                <button class="btn btn-warning rounded-pill" onclick="location.href=\'/store/' + value.id + '\'">자세히 보기</button>' +
                    '            </div>' +
                    '            <div class="d-flex justify-content-between align-items-center py-1">' +
                    '                <span>' + value.category + '</span>' +
                    '                <span>평점 <span>' + value.rate + ' 점</span> 후기 <span>' + value.commentCount + ' 개</span></span>' +
                    '            </div>' +
                    '            <p>' + value.address + '</p>' +
                    '        </div>' +
                    '    </div>' +
                    '</li>'
                ));
                var points = {};
                points.id = value.id;
                points.content = '<div style="text-align: center; font-size: 8px; width: max-content; padding:5px;">' + value.name + '</div>';
                points.latlng = new kakao.maps.LatLng(value.lat, value.lon);
                positions.push(points);
            });
            var markerList = reload(positions);
            $.each(markerList, function(key, value){
               var id = value.id;
               var thisMarker = value.marker;
               $("#"+id).click(function(){
                  kakao.maps.event.trigger(thisMarker, "click");
               });
            });
            $("#resultBtn").attr("aria-expanded", "true");
            $("#searchResult").addClass("show");
        },
        error : function(status, error){
            alert(status.responseText);
        }
    });
}
function searchAccommodation(){
    var url = "/search/accommodation?";
    if($("#accommodationSelectedOptions span").length == 0){
        alert("검색 조건을 선택하세냥!")
    }
    if($("#accommodationCategory .active").length == 0){
        alert("숙소 분류를 선택하세냥!");
        return;
    }
    if($("#accommodationCity a").length == 0){
        alert("시/도를 선택하세냥!");
        return;
    }
    if($("#accommodationCity .active").length == 0){
        alert("시/군/구를 선택하세냥!");
        return;
    }
    var categoryParam = "categoryId=";
    $("#accommodationCategory .multi1.active").each(function(index, element){
        if(index == 0){
            categoryParam += $(element).attr("data-id");
        }else{
            categoryParam += "," + $(element).attr("data-id");
        }
    });
    var cityParam = "&cityId=";
    $("#accommodationCity .multi1.active").each(function(index, element){
        if(index == 0){
            cityParam += $(element).attr("data-id");
        }else{
            cityParam += "," + $(element).attr("data-id");
        }
    })
    url += categoryParam + cityParam;
    var headCount = "&headCount=";
    if($("#headCount").val() == null || $("#headCount").val() == ""){
        alert("같이 갈 인원 수를 입력하세냥!");
        return;
    }
    headCount += $("#headCount").val();
    var checkIn = $("#dateRange").attr("data-checkIn");
    var checkOut = $("#dateRange").attr("data-checkOut");
    if ($("#dateRange").val() == "" || $("#dateRange").val() == null){
        alert("기간을 입력하세냥!");
        return;
    }
    url += headCount + "&checkInDate=" + checkIn + "&checkOutDate=" + checkOut;
    var days = Math.ceil(moment.duration(moment(checkOut).diff(moment(checkIn))).asDays());
    href = "?headCount=" + $("#headCount").val() + "&days=" + days + "&checkInDate=" + checkIn + "&checkOutDate=" + checkOut;

    $.ajax({
        url : url,
        type : "GET",
        async : false,
        dataType : "json",
        cache : false,
        success : function(result, status){
            if($(".accordion-header").attr("hidden") == "hidden"){
                $(".accordion-header").removeAttr("hidden");
            }
            if($("#searchResult").attr("hidden") == "hidden"){
                $("#searchResult").removeAttr("hidden");
            }
            $("button.accordion-button").text("숙소 검색 결과");
            $("#resultList").empty();
            if(result.length == 0){
                $("#resultList").append($(
                    '<li style="height: 500px; line-height: 500px;" class="text-center">검색 결과가 없습니다.</li>'
                ));
                map.relayout();
                return false;
            }
            var positions = [];
            $.each(result, function(key, value){
                var status = '';
                var link = '';
                if(value.status == "OPEN"){
                    link = '                <button class="btn btn-warning rounded-pill" onclick="location.href=\'/accommodation/' + value.id + href + '\'">예약 하러 가기</button>';
                }else{
                    status = '<span class="badge bg-secondary rounded-pill" style="font-size: 9px">휴업중</span>';
                    link = '                <button class="btn btn-warning rounded-pill" onclick="location.href=\'/accommodation/' + value.id + href + '\'">자세히 보기</button>';
                }
                $("#resultList").append($(
                    '<li class="list-group-item list-group-item-action result1 py-2 px-4" id="' + value.id + '">' +
                    '    <div class="row g-3">' +
                    '        <div class="col-3">' +
                    '            <img style="width: 100%; height: 120px; object-fit: cover;" src="' + value.repImageUrl + '"/>' +
                    '        </div>' +
                    '        <div class="col-9">' +
                    '            <div class="d-flex justify-content-between align-items-center py-2">' +
                    '                <h5 pe-1>' + value.name + status + '</h5>' +
                    link +
                    '            </div>' +
                    '            <div class="d-flex justify-content-between align-items-center py-1">' +
                    '                <span>' + value.category + '</span>' +
                    '                <span>평점 <span>' + value.rate + ' 점</span> 후기 <span>' + value.commentCount + ' 개</span></span>' +
                    '            </div>' +
                    '            <p>' + value.address + '</p>' +
                    '        </div>' +
                    '    </div>' +
                    '</li>'
                ));
                var points = {};
                points.id = value.id;
                points.content = '<div style="text-align: center; font-size: 8px; width: max-content; padding:5px;">' + value.name + '</a></div>';
                points.latlng = new kakao.maps.LatLng(value.lat, value.lon);
                positions.push(points);
            });
            var markerList = reload(positions);
            $.each(markerList, function(key, value){
               var id = value.id;
               var thisMarker = value.marker;
               $("#"+id).click(function(){
                  kakao.maps.event.trigger(thisMarker, "click");
               });
            });
            $("#resultBtn").attr("aria-expanded", "true");
            $("#searchResult").addClass("show");
        },
        error : function(status, error){
            alert(status.responseText);
        }
    });
}