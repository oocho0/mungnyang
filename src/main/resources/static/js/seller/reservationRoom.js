document.addEventListener('DOMContentLoaded', function() {
    var calendarEl = document.getElementById("calendar");
    var calendar = new FullCalendar.Calendar(calendarEl, {
        themeSystem: 'bootstrap5',
        timeZone: 'KST',
        height: 600,
        aspectRatio: 0.9,
        initialView: 'multiMonthYear',
        multiMonthMaxColumns: 1,
        editable: false,
        locale: 'ko',
        displayEventTime: false,
        eventSources : [
            {
                url : "/seller/accommodations/reservations/" + $("#room").attr("data-room-id") + "/calendar",
                method : 'GET',
                success : function(content, status){
                },
                failure : function(error){
                   if(error.response.status != 200){
                       alert('예약 정보를 가져오는데 실패했습니다.');
                   }
                },
                color : 'yellow',
                textColor : 'black',
                editable : false
            }
        ]
    });
    calendar.setOption('locale','ko');
    calendar.render();
});

function page(page){
    var tab = "";
    var tabName = $(".nav-link.active").attr("id");
    if (tabName == "current-tab"){
        tab = "current";
    }else{
        tab = "past";
    }
    var url = "/accommodations/reservations/" + $("#room").attr("data-room-id") + "/" + tab + "?page=" + page;
    $.ajax({
        url : url,
        type : "GET",
        cache   : false,
        success  : function(result, status){
            $(".tab-pane[aria-labelledby='" + tabName + "']").find("tbody").empty();
            $(".tab-pane[aria-labelledby='" + tabName + "']").find(".paging").empty();
            var startPage = 1;
            if(result.totalPages > 5 ) {
                startPage = result.number + 1;
            }
            var endPage = (result.totalPages == 0) ? 1 : (startPage + result.size - 1 < result.totalPages ? (startPage + result.size - 1) : result.totalPages);
            var firstAppendClass = (result.number <= result.size) ? 'disabled' : '';
            var prevAppendClass = (result.first) ? 'disabled' : '';
            var nextAppendClass = (result.last) ? 'disabled' : '';
            var lastAppendClass = ((result.totalPages / result.size) <= startPage) ? 'disabled' : '';
            var pages = "";
            for(var i = startPage; i <= endPage; i++){
                if(result.number == (i -1)){
                    pages += '            <li class="page-item active">'
                }else{
                    pages += '            <li class="page-item">'
                }
                pages += '                <a class="page-link" onclick="page(' + (i -1) + ');">' + i + '</a>'
                pages += '            </li>'
            }
            $(".tab-pane[aria-labelledby='" + tabName + "']").find(".paging").append($(
                '    <nav>' +
                '        <ul class="pagination justify-content-center">' +
                '            <li class="page-item ' + firstAppendClass + '" >' +
                '                <a class="page-link" onclick="page(0);" aria-label="first">' +
                '                    <span aria-hidden="true">&laquo;</span>' +
                '                </a>' +
                '            </li>' +
                '            <li class="page-item ' + prevAppendClass + '">' +
                '                <a class="page-link" onclick="page(' + (result.number - 1) + ');" aria-label="Previous">' +
                '                    <span aria-hidden="true">&lt;</span>' +
                '                </a>' +
                '            </li>' +
                pages +
                '            <li class="page-item ' + nextAppendClass + '">' +
                '                <a class="page-link" onclick="page(' + (result.number + 1) + ');" aria-label="Next">' +
                '                    <span aria-hidden="true">&gt;</span>' +
                '                </a>' +
                '            </li>' +
                '            <li class="page-item ' + lastAppendClass + '">' +
                '                <a class="page-link" onclick="page(' + (result.totalPages -1) + ');" aria-label="last">' +
                '                    <span aria-hidden="true">&raquo;</span>' +
                '                </a>' +
                '            </li>' +
                '        </ul>' +
                '    </nav>'
            ));
            var contents = result.content;
            $.each(contents, function(key, value){
                var checkInDate = moment(value.checkInDate);
                var checkInDateStr = checkInDate.format('YYYY[년] MM[월] DD[일]');
                var checkOutDate = moment(value.checkOutDate);
                var checkOutDateStr = checkOutDate.format('YYYY[년] MM[월] DD[일]');
                $(".tab-pane[aria-labelledby='" + tabName + "']").find("tbody").append($(
                    '<tr>' +
                    '    <th scope="row">' + value.reservationRoomId + '</th>' +
                    '    <td>' + value.memberName + '</td>' +
                    '    <td>' + value.headCount + '</td>' +
                    '    <td>' + value.days + '</td>' +
                    '    <td>' + checkInDateStr + '</td>' +
                    '    <td>' + checkOutDateStr + '</td>' +
                    '</tr>'
                ));
            });
        },
        error : function(status, error){
            alert(status.responseText);
        }
    });
}