class reservation {
    constructor(checkInDate, checkOutDate, title) {
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.title = title;
    }
}

var reservationArray = new Map();

document.addEventListener('DOMContentLoaded', function() {
    makeCalendar(0);
});

function deleteCalendar(i){
    reservationArray.delete("room" + i);
}

function makeCalendar(i){
    var eventCount = 1;
    $('.calendar').each(function(j, calendar){
        if ( j == $('.calendar').length-1 ){
            var calendarEl = $(calendar);
            var arrayName = "room" + i;
            reservationArray.set(arrayName, new Array());
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
                        if(!validate(checkInDate, checkOutDate, arrayName)){
                            return false;
                        }
                        var description = "예약" + eventCount++;
                        reservationArray.get(arrayName).push(new reservation(checkInDate, checkOutDate, description));
                        calendar.addEvent({
                            title: '예약됨',
                            id: description,
                            start: checkInDate.toDate(),
                            end: checkOutDate.toDate()
                        });
                        console.log(reservationArray);
                    }
                },
                eventClick: function(info){
                    info.event.remove();
                    for(var i = 0; i < reservationArray.get(arrayName).length; i++){
                        if(reservationArray.get(arrayName)[i].title == info.event.id){
                            reservationArray.get(arrayName).splice(i, 1);
                            i--;
                        }
                    }
                    console.log(reservationArray);
                },
                events : []
            });
            calendar.setOption('locale','ko');
            calendar.render();
        }
    });
}


function validate(checkInDate, checkOutDate, arrayName){
    if(reservationArray.get(arrayName).length > 0){
        for(var i=0; i<reservationArray.get(arrayName).length; i++){
            if(checkInDate.isSame(reservationArray.get(arrayName)[i].checkInDate, "day")){
                return false;
            }
            if(checkOutDate.isSame(reservationArray.get(arrayName)[i].checkOutDate, "day")){
                return false;
            }
            if(checkInDate.isBetween(reservationArray.get(arrayName)[i].checkInDate, reservationArray.get(arrayName)[i].checkOutDate, "day")){
                return false;
            }
            if(checkOutDate.isBetween(reservationArray.get(arrayName)[i].checkInDate, reservationArray.get(arrayName)[i].checkOutDate, "day")){
                return false;
            }
            if(reservationArray.get(arrayName)[i].checkInDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
            if(reservationArray.get(arrayName)[i].checkOutDate.isBetween(checkInDate, checkOutDate, "day")){
                return false;
            }
        }
        return true;
    }
    return true;
}

function addReservationList(formData, i, k){
    var roomIndex = "room"+ i;
    for(var j = 0; j < reservationArray.get(roomIndex).length; j++){
        var parsedCheckInDate = reservationArray.get(roomIndex)[j].checkInDate.add("9","hours");
        var parsedCheckOutDate = reservationArray.get(roomIndex)[j].checkOutDate.add("9","hours");
        formData.append("roomList[" + k + "].reservationList[" + j + "].checkInDate", parsedCheckInDate.format("YYYY-MM-DD[T]HH:mm:ss"));
        formData.append("roomList[" + k + "].reservationList[" + j + "].checkOutDate", parsedCheckOutDate.format("YYYY-MM-DD[T]HH:mm:ss"));
    }
}