class book {
    constructor(startDate, endDate, title) {
        this.startDate = startDate;
        this.endDate = endDate;
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
        editable: true,
        locale: 'ko',
        selectable: true,
        displayEventTime: false,
        select: function(info){
            var now = moment();
            var startDate = moment(info.start);
            startDate.set({'hour': 15, 'minute': 0, 'second': 0, 'millisecond': 0});
            var endDate = moment(info.end);
            endDate.subtract(1, "days");
            endDate.set({'hour': 11, 'minute': 0, 'second': 0, 'millisecond': 0});
            if(startDate.date() != endDate.date() && startDate.isSameOrAfter(now, "day") && endDate.isSameOrAfter(now, "day")){
                if(!validate(startDate, endDate)){
                    return false;
                }
                var description = "예약" + eventCount++;
                initialBooked.push(new book(startDate, endDate, description));
                calendar.addEvent({
                    title: '예약됨',
                    id: description,
                    start: startDate.toDate(),
                    end: endDate.toDate()
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

function validate(startDate, endDate){
    if(initialBooked.length > 0){
        for(var i=0; i<initialBooked.length; i++){
            if(startDate.isSame(initialBooked[i].startDate, "day")){
                return false;
            }
            if(endDate.isSame(initialBooked[i].endDate, "day")){
                return false;
            }
            if(startDate.isBetween(initialBooked[i].startDate, initialBooked[i].endDate, "day")){
                return false;
            }
            if(endDate.isBetween(initialBooked[i].startDate, initialBooked[i].endDate, "day")){
                return false;
            }
            if(initialBooked[i].startDate.isBetween(startDate, endDate, "day")){
                return false;
            }
            if(initialBooked[i].endDate.isBetween(startDate, endDate, "day")){
                return false;
            }
        }
        return true;
    }
    return true;
}

function addBookingDate(formData){
    for(var i = 0; i < initialBooked.length; i++){
        formData.append("bookedList[" + i + "].startDate", initialBooked[i].startDate);
        formData.append("bookedList[" + i + "].endDate", initialBooked[i].endDate);
    }
}