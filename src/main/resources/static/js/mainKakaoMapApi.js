var container = document.getElementById('map'),
    options = {
          center: new kakao.maps.LatLng(33.450701, 126.570667),
          level: 3
    };

var map = new kakao.maps.Map(container, options);

function reload(positions){
    var newMap = new kakao.maps.Map(container, options);
    var markerList = [];
    var bounds = new kakao.maps.LatLngBounds();
    for(var i=0; i < positions.length; i++){
        var marker = new kakao.maps.Marker({
            position : positions[i].latlng
        });
        var infoWindow = new kakao.maps.InfoWindow({
            content : positions[i].content
        });
        marker.setMap(newMap);
        bounds.extend(positions[i].latlng);
        kakao.maps.event.addListener(marker, 'mouseover', makeOverListener(newMap, marker, infoWindow));
        kakao.maps.event.addListener(marker, 'mouseout', makeOutListener(infoWindow));
        kakao.maps.event.addListener(marker, 'click', clickMarker(newMap, marker, infoWindow, positions[i].latlng, positions[i].id));
        var markerInfo = {};
        markerInfo.id = positions[i].id;
        markerInfo.marker = marker;
        markerList.push(markerInfo);
    }
    newMap.setBounds(bounds);
    newMap.relayout();
    return markerList;
}
var openWindow = [];
function clickMarker(newMap, marker, infoWindow, markPoint, id){
    return function(){
        if(openWindow.length == 0){
            infoWindow.open(newMap, marker);
            newMap.panTo(markPoint);
            location.href="#" + id;
            openWindow.push(infoWindow);
        }else{
            openWindow[0].close();
            openWindow.splice(0, 1);
        }
    };
}

function makeOverListener(newMap, marker, infoWindow){
    return function(){
        infoWindow.open(newMap, marker);
    };
}
function makeOutListener(infoWindow){
    return function(){
        infoWindow.close();
    };
}