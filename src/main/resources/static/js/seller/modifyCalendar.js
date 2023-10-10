class book {
    constructor(id, title, checkInDate, checkOutDate) {
        this.id = id;
        this.is_delete = "N";
        this.title = title;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }
}
var bookArray = [];

document.addEventListener('DOMContentLoaded', function() {
    var eventCount = 1;
    var calendarEl = document.getElementById("calendar");
    var calendar = new FullCalendar.Calendar(calendarEl, {
        themeSystem: 'bootstrap5',
        timeZone: 'KST',
        initialView: 'multiMonthYear',
        multiMonthMaxColumns: 1,
        editable: false,
        locale: 'ko',
        selectable: true,
        displayEventTime: false,
        eventSources : [
            {
                url: "/seller/accommodations/" + $("#accommodationId").val() + "/" + $("#roomId").val() + "/init",
                method: 'GET',
                success: function(content, response) {
                    $(content).each(function(index, value){
                        var checkInDate = moment(value.start);
                        checkInDate.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
                        var checkOutDate = moment(value.end);
                        checkOutDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
                        bookArray.push(new book(value.id, value.id, checkInDate, checkOutDate));
                    });
                },
                failure: function(error) {
                    if(error.response.status != 200){
                        alert('예약 정보를 가져오는데 실패했습니다.');
                    }
                },
                color : 'blue',
                editable : true,
                overlap : false
            },
            {
                url: "/seller/accommodations/" + $("#accommodationId").val() + "/" + $("#roomId").val() + "/reserv",
                method: 'GET',
                success: function(content, response) {
                   $(content).each(function(index, value){
                        var checkInDate = moment(value.start);
                        checkInDate.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
                        var checkOutDate = moment(value.end);
                        checkOutDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
                        bookArray.push(new book(value.id, value.id, checkInDate, checkOutDate));
                    });
                },
                failure: function(error) {
                    if(error.response.status != 200){
                        alert('예약 정보를 가져오는데 실패했습니다.');
                    }
                },
                color : 'yellow',
                textColor: 'black',
                editable : false,
                overlap : false
            }
        ],
        select: function(info){
            var now = moment();
            var checkInDate = moment(info.start);
            checkInDate.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
            var checkOutDate = moment(info.end);
            checkOutDate.subtract(1, "days");
            checkOutDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
            if(checkInDate.date() != checkOutDate.date() && checkInDate.isSameOrAfter(now, "day") && checkOutDate.isSameOrAfter(now, "day")){
                if(!validate(checkInDate, checkOutDate)){
                    return false;
                }
                var description = "예약" + eventCount++;
                bookArray.push(new book(null, description, checkInDate, checkOutDate));
                calendar.addEvent({
                    title: '신규 예약',
                    id: description,
                    start: checkInDate.toDate(),
                    end: checkOutDate.toDate()
                });
                console.log(bookArray);
            }
        },
        eventClick: function(info){
            if(info.event.title == '예약됨' || info.event.title == '신규 예약'){
                info.event.remove();
                for(var i = 0; i < bookArray.length; i++){
                    if(bookArray[i].title == info.event.id){
                        bookArray[i].is_delete = "Y";
                    }
                }
                console.log(bookArray);
            }
        },
        events : []
    });
    calendar.setOption('locale','ko');
    calendar.render();
});

function validate(checkInDate, checkOutDate){
    if(bookArray.length > 0){
        for(var i=0; i<bookArray.length; i++){
            if(bookArray[i].is_delete == "N"){
                if(checkInDate.isSame(bookArray[i].checkInDate, "day")){
                    return false;
                }
                if(checkOutDate.isSame(bookArray[i].checkOutDate, "day")){
                    return false;
                }
                if(checkInDate.isBetween(bookArray[i].checkInDate, bookArray[i].checkOutDate, "day")){
                    return false;
                }
                if(checkOutDate.isBetween(bookArray[i].checkInDate, bookArray[i].checkOutDate, "day")){
                    return false;
                }
                if(bookArray[i].checkInDate.isBetween(checkInDate, checkOutDate, "day")){
                    return false;
                }
                if(bookArray[i].checkOutDate.isBetween(checkInDate, checkOutDate, "day")){
                    return false;
                }
            }
        }
        return true;
    }
    return true;
}

function addReservationList(formData){
    var k = 0;
    if(bookArray.length == 0){
        return;
    }
    for(var i = 0; i < bookArray.length; i++){
        if(bookArray[i].id != null) {
            formData.append("reservationList[" + k + "].reservationRoomId", bookArray[i].id);
            formData.append("reservationList[" + k + "].isDelete", bookArray[i].is_delete);
            k++;
            continue;
        }
        if(bookArray[i].is_delete == "N"){
            var parsedCheckInDate = bookArray[i].checkInDate.format("YYYY-MM-DD[T]HH:mm:ss");
            var parsedCheckOutDate = bookArray[i].checkOutDate.format("YYYY-MM-DD[T]HH:mm:ss");
            formData.append("reservationList[" + k + "].checkInDate", parsedCheckInDate);
            formData.append("reservationList[" + k + "].checkOutDate", parsedCheckOutDate);
            formData.append("reservationList[" + k + "].isDelete", bookArray[i].is_delete);
            k++;
        }
    }
}