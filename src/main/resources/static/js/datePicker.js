var now = moment();
var endDate = moment(now).add(1, "d");
$(function(){
    $("#dateRange").val(now.format('YYYY[년] MM[월] DD[일]') + ' - ' + endDate.format('YYYY[년] MM[월] DD[일]'));
    $("#dateRange").attr("data-checkIn", now.format("YYYY-MM-DD[T]HH:mm:ss"));
    $("#dateRange").attr("data-checkOut", endDate.format("YYYY-MM-DD[T]HH:mm:ss"));
    $('#dateRange').daterangepicker({
        "locale": {
            "format": "YYYY년 MM월 DD일",
            "separator": " - ",
            "applyLabel": "변경",
            "cancelLabel": "취소",
            "fromLabel": "From",
            "toLabel": "To",
            "daysOfWeek": ["일", "월", "화", "수", "목", "금", "토"],
            "monthNames": ["1월", "2월", "3월", "4월", "5월", "6월", "7월", "8월", "9월", "10월", "11월", "12월"],
            "firstDay": 1
        },
        "showDropdowns": true,
        "autoUpdateInput": false,
        "startDate": now,
        "endDate": endDate,
        "minDate": now,
        "opens": "center"
    }, function(start, end, label) {
    });
    $('#dateRange').on('apply.daterangepicker', function(ev, picker) {
        if(picker.startDate.format('DD/MM/YYYY') == picker.endDate.format('DD/MM/YYYY')){
            ev.preventDefault();
            alert("1박 이상을 선택해야한댜옹");
        }else{
            $(this).val(picker.startDate.format('YYYY[년] MM[월] DD[일]') + ' - ' + picker.endDate.format('YYYY[년] MM[월] DD[일]'));
            $(this).attr("data-checkIn", picker.startDate.format("YYYY-MM-DD[T]HH:mm:ss"));
            $(this).attr("data-checkOut", picker.endDate.format("YYYY-MM-DD[T]HH:mm:ss"));
        }
    });
});
