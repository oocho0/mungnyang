class book {
    constructor(checkInDate, checkOutDate, title) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.title = title;
    }
}

var initialBooked = [[],[],[],[],[],[],[],[],[],[]];

document.addEventListener('DOMContentLoaded', function() {
    var eventCount = 1;
    $('.rangeCalendar').each(function(i, rangeCalendar){
        var calendarEl = $(rangeCalendar);
        var calendar = new FullCalendar.Calendar(calendarEl[0], {
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
                    if(!validate(checkInDate, checkOutDate, i)){
                        return false;
                    }
                    var description = "예약" + eventCount++;
                    initialBooked[i].push(new book(checkInDate, checkOutDate, description));
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
                for(var j = 0; j < initialBooked[i].length; j++){
                    if(initialBooked[i][j].title == info.event.id){
                        initialBooked[i].splice(j, 1);
                        j--;
                    }
                }
                console.log(initialBooked);
            },
            events : []
        });
        calendar.setOption('locale','ko');
        calendar.render();
    });
    for(var i = 2; i < 11; i++){
        $("#room"+i).hide();
    }
    setting();
});

function validate(checkInDate, checkOutDate, i){
    if(initialBooked[i].length > 0){
        for(var j=0; j<initialBooked[i].length; j++){
            if(checkInDate.isSame(initialBooked[i][j].checkInDate, "day")){
                return false;
            }
            if(checkOutDate.isSame(initialBooked[i][j].checkOutDate, "day")){
                return false;
            }
            if(checkInDate.isBetween(initialBooked[i][j].checkInDate, initialBooked[i][j].checkOutDate, "day")){
                return false;
            }
            if(checkOutDate.isBetween(initialBooked[i][j].checkInDate, initialBooked[i][j].checkOutDate, "day")){
                return false;
            }
            if(initialBooked[i][j].checkInDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
            if(initialBooked[i][j].checkOutDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
        }
        return true;
    }
    return true;
}

function addBookingDate(formData, i){
    for(var j = 0; j < initialBooked[i].length; j++){
        formData.append("roomList[" + i + "].reservationList[" + j + "].checkInDate", initialBooked[i][j].checkInDate.format("YYYY-MM-DD[T]HH:mm:ss"));
        formData.append("roomList[" + i + "].reservationList[" + j + "].checkOutDate", initialBooked[i][j].checkOutDate.format("YYYY-MM-DD[T]HH:mm:ss"));
    }
}