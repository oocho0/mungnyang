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
    $("#smallCategory .active").each(function(index, element){
        if(index == 0){
            categoryParam += $(element).attr("data-id");
        }else{
            categoryParam += "," + $(element).attr("data-id");
        }
    });
    var cityParam = "&cityId=";
    $("#storeCity .active").each(function(index, element){
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
            var positions = [];
            $.each(result, function(key, value){
                var status = value.status;
                var badgeColor = "";
                if(status == "OPEN"){
                    status = "영업중";
                    badgeColor = "bg-warning";
                }else{
                    status = "휴업";
                    badgeColor = "bg-secondary";
                }
                $("#resultList").append($(
                    '<li class="list-group-item list-group-item-action result1 py-2 px-4" id="' + value.id + '">' +
                    '    <div class="row g-3">' +
                    '        <div class="col-3">' +
                    '            <img style="width: 100%; height: 120px; object-fit: cover;" src=' + value.repImageUrl + '/>' +
                    '        </div>' +
                    '        <div class="col-9">' +
                    '            <div class="d-flex justify-content-between align-items-center py-2">' +
                    '                <h5>' + value.name + '<span class="badge ' + badgeColor + ' rounded-pill" style="font-size: 8px">' + status + '</span></h5>' +
                    '                <a class="link-dark link-offset-2 link-offset-3-hover link-underline link-underline-opacity-0 link-underline-opacity-75-hover"' +
                    '                   href="/store/' + value.id + '">더 알아보기</a>' +
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
                  thisMarker.trigger("click");
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
    $("#accommodationCategory .active").each(function(index, element){
        if(index == 0){
            categoryParam += $(element).attr("data-id");
        }else{
            categoryParam += "," + $(element).attr("data-id");
        }
    });
    var cityParam = "&cityId=";
    $("#accommodationCity .active").each(function(index, element){
        if(index == 0){
            cityParam += $(element).attr("data-id");
        }else{
            cityParam += "," + $(element).attr("data-id");
        }
    })
    url += categoryParam + cityParam;
    var people = "&roomPeople=";
    if($("#roomPeople").val() == null || $("#roomPeople").val() == ""){
        alert("같이 갈 인원 수를 입력하세냥!");
        return;
    }
    people += $("#roomPeople").val();
    var checkIn = "&checkInDate=" + $("#dateRange").attr("data-checkIn");
    var checkOut = "&checkOutDate=" + $("#dateRange").attr("data-checkOut");
    if ($("#dateRange").val() == "" || $("#dateRange").val() == null){
        alert("기간을 입력하세냥!");
        return;
    }
    url += people + checkIn + checkOut;

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
            var positions = [];
            $.each(result, function(key, value){
                var status = value.status;
                if(status == "OPEN"){
                    status = "영업중";
                }else{
                    status = "휴업";
                }
                $("#resultList").append($(
                    '<li class="list-group-item list-group-item-action result1 py-2 px-4" id="' + value.id + '">' +
                    '    <div class="row g-3">' +
                    '        <div class="col-3">' +
                    '            <img style="width: 100%; height: 120px; object-fit: cover;" src=' + value.repImageUrl + '/>' +
                    '        </div>' +
                    '        <div class="col-9">' +
                    '            <div class="d-flex justify-content-between align-items-center py-2">' +
                    '                <h5>' + value.name + '<span class="badge bg-warning rounded-pill" style="font-size: 8px">' + status + '</span></h5>' +
                    '                <a class="link-dark link-offset-2 link-offset-3-hover link-underline link-underline-opacity-0 link-underline-opacity-75-hover"' +
                    '                   href="/accommodation/' + value.id + '">더 알아보기</a>' +
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
                  thisMarker.click();
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