class book {
    constructor(checkInDate, checkOutDate){
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}

var now = moment();
now.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
var endDate = moment(now).add(1, "d");
endDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
$(function(){
    setCalendar();
    var start = now;
    var end = endDate;
    if($("#dataRange").attr("data-checkIn") != ""){
        start = moment($("#dataRange").attr("data-checkIn"));
        start.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
        end = moment($("#dataRange").attr("data-checkOut"));
        end.add(1, "d");
        end.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
    }
    $("#dateRange").val(start.format('YYYY[년] MM[월] DD[일]') + ' - ' + end.format('YYYY[년] MM[월] DD[일]'));
    $("#dateRange").attr("data-checkIn", start.format("YYYY-MM-DD[T]HH:mm:ss"));
    $("#dateRange").attr("data-checkOut", end.format("YYYY-MM-DD[T]HH:mm:ss"));
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
        "startDate": start,
        "endDate": end,
        "minDate": now,
        "opens": "center"
    }, function(start, end, label) {
    });
    $('#dateRange').on('apply.daterangepicker', function(ev, picker) {
        if(picker.startDate.format('DD/MM/YYYY') == picker.endDate.format('DD/MM/YYYY')){
            ev.preventDefault();
            alert("1박 이상을 선택해야한댜야옹");
        }else{
            $(this).val(picker.startDate.format('YYYY[년] MM[월] DD[일]') + ' - ' + picker.endDate.format('YYYY[년] MM[월] DD[일]'));
            var checkInDate = picker.startDate.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
            var checkOutDate = picker.endDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
            $(this).attr("data-checkIn", checkInDate.format("YYYY-MM-DD[T]HH:mm:ss"));
            $(this).attr("data-checkOut", checkOutDate.format("YYYY-MM-DD[T]HH:mm:ss"));
        }
    });
});

function setCalendar(){
    $(".accordion-button").on("click", function(){
        var allEvent = [];
        var newCheckIn = null;
        var newCheckOut = null;
        var calendarEl = $(event.target).closest(".accordion").find(".calendar");
        var roomId = calendarEl.closest(".accordion").attr("data-id");
        var accommodationId = $("#id").val();
        var calendar = new FullCalendar.Calendar(calendarEl[0], {
            themeSystem: 'bootstrap5',
            timeZone: 'KST',
            height: 500,
            aspectRatio: 0.9,
            initialView: 'multiMonthYear',
            multiMonthMaxColumns: 2,
            editable: false,
            locale: 'ko',
            selectable: true,
            displayEventTime: false,
            customButtons: {
                myCustomButton: {
                    text: '선택한 기간을 적용하세냥!',
                    click: function(){
                        if(newCheckIn == null || newCheckOut == null){
                            alert("기간을 선택해야한댜야옹");
                            return;
                        }
                        $("#dateRange").attr("data-checkIn", newCheckIn.format("YYYY-MM-DD[T]HH:mm:ss"));
                        $("#dateRange").attr("data-checkOut", newCheckOut.format("YYYY-MM-DD[T]HH:mm:ss"));
                        $("#dateRange").val(newCheckIn.format('YYYY[년] MM[월] DD[일]') + ' - ' + newCheckOut.format('YYYY[년] MM[월] DD[일]'));
                    }
                }
            },
            headerToolbar: {
               right: 'myCustomButton today prev next'
            },
            eventSources : [
                {
                   url: "/accommodation/" + accommodationId + "/" + roomId + "/reservations",
                   method: 'GET',
                   success: function(content, response) {
                       $(content).each(function(index, value){
                           var checkInDate = moment(value.start);
                           checkInDate.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
                           var checkOutDate = moment(value.end);
                           checkOutDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
                           allEvent.push(new book(checkInDate, checkOutDate));
                       });
                   },
                   failure: function(error) {
                       if(error.response.status != 200){
                           alert('예약 정보를 가져오는데 실패했습니다.');
                       }
                   },
                   color : 'gray',
                   editable : true,
                   overlap : false
                },
            ],
            select : function(info){
                newCheckIn = null;
                newCheckOut = null;
                var curEvent = calendar.getEventById('new');
                if (curEvent != null){
                    curEvent.remove();
                }
                var now = moment();
                var checkInDate = moment(info.start);
                checkInDate.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
                var checkOutDate = moment(info.end);
                checkOutDate.subtract(1, "days");
                checkOutDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
                if(checkInDate.date() != checkOutDate.date() && checkInDate.isSameOrAfter(now, "day") && checkOutDate.isSameOrAfter(now, "day")){
                    if(!validate(allEvent, checkInDate, checkOutDate)){
                        return false;
                    }
                    calendar.addEvent({
                        title: '신규 예약',
                        id: "new",
                        start: checkInDate.toDate(),
                        end: checkOutDate.toDate()
                    });
                    newCheckIn = checkInDate;
                    newCheckOut = checkOutDate;
                }
            }
        });
        calendar.setOption('locale', 'ko');
        calendar.render();
    });
}

function validate(allEvent, checkInDate, checkOutDate){
    if(allEvent.length > 0){
        for(var i=0; i<allEvent.length; i++){
            if(checkInDate.isSame(allEvent[i].checkInDate, "day")){
                return false;
            }
            if(checkOutDate.isSame(allEvent[i].checkOutDate, "day")){
                return false;
            }
            if(checkInDate.isBetween(allEvent[i].checkInDate, allEvent[i].checkOutDate, "day")){
                return false;
            }
            if(checkOutDate.isBetween(allEvent[i].checkInDate, allEvent[i].checkOutDate, "day")){
                return false;
            }
            if(allEvent[i].checkInDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
            if(allEvent[i].checkOutDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
        }
        return true;
    }
    return true;
}