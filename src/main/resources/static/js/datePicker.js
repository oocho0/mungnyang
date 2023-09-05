$(function() {
    $('#calendar').daterangepicker({
        "showDropdowns": true,
        "locale": {
            "format": "YYYY/MM/DD",
            "separator": " - ",
            "applyLabel": "Apply",
            "cancelLabel": "Clear",
            "fromLabel": "From",
            "toLabel": "To",
            "customRangeLabel": "Custom",
            "weekLabel": "W",
            "daysOfWeek": ["일", "월", "화", "수", "목", "금", "토"],
            "monthNames": ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
            "firstDay": 1
        },
        "alwaysShowCalendars": true,
    }, function(start, end) {
        validate(start, end);
        ("#calendar").closest("div").append($(
        "<div class="
        ))
      console.log('New date range selected: ' + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD') + ' (predefined range: ' + label + ')');
    });
});

function validate(startDate, endDate){
    var now = moment();
    if(startDate <= now || endDate <= now) {
        alert("예약된 날짜가 오늘보다 이전일 수 없습니다.")
    }
}

function appendDate(start, end){

}