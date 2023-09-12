var lat = $("#lat").val();
var lon = $("#lon").val();

var container = document.getElementById('map'),
    options = {
          center: new kakao.maps.LatLng(lat, lon),
          level: 3
    };

var map = new kakao.maps.Map(container, options);

    var marker = new kakao.maps.Marker({
        position : new kakao.maps.LatLng(lat, lon)
    });
    marker.setMap(map);
    var infoWindow = new kakao.maps.InfoWindow({
       position : new kakao.maps.LatLng(lat, lon),
       content : '<div style="font-size: 8px; width: max-content; padding:5px;">' + $("#name").text() + '</div>'
    });
    infoWindow.open(map, marker);
    map.relayout();
