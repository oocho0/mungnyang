class book {
    constructor(checkInDate, checkOutDate, title) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.title = title;
    }
}
var initialBooked = [];

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
                initialBooked.push(new book(checkInDate, checkOutDate, description));
                calendar.addEvent({
                    title: '예약됨',
                    id: description,
                    start: checkInDate.toDate(),
                    end: checkOutDate.toDate()
                });
                console.log(initialBooked);
            }
        },
        eventClick: function(info){
            info.event.remove();
            for(var i = 0; i < initialBooked.length; i++){
                if(initialBooked[i].title == info.event.id){
                    initialBooked.splice(i, 1);
                    i--;
                }
            }
            console.log(initialBooked);
        },
        events : []
    });
    calendar.setOption('locale','ko');
    calendar.render();
});

function validate(checkInDate, checkOutDate){
    if(initialBooked.length > 0){
        for(var i=0; i<initialBooked.length; i++){
            if(checkInDate.isSame(initialBooked[i].checkInDate, "day")){
                return false;
            }
            if(checkOutDate.isSame(initialBooked[i].checkOutDate, "day")){
                return false;
            }
            if(checkInDate.isBetween(initialBooked[i].checkInDate, initialBooked[i].checkOutDate, "day")){
                return false;
            }
            if(checkOutDate.isBetween(initialBooked[i].checkInDate, initialBooked[i].checkOutDate, "day")){
                return false;
            }
            if(initialBooked[i].checkInDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
            if(initialBooked[i].checkOutDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
        }
        return true;
    }
    return true;
}

function addReservationList(formData){
    for(var i = 0; i < initialBooked.length; i++){
        var parsedCheckInDate = bookArray[i].checkInDate.format("YYYY-MM-DD[T]HH:mm:ss");
        var parsedCheckOutDate = bookArray[i].checkOutDate.format("YYYY-MM-DD[T]HH:mm:ss");
        formData.append("reservationList[" + k + "].checkInDate", parsedCheckInDate);
        formData.append("reservationList[" + k + "].checkOutDate", parsedCheckOutDate);
    }
}