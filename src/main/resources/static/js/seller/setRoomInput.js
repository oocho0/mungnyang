$(document).ready(function(){
    changeName("#roomName0");
})

function changeName(tag){
    $(tag).on("change", function(){
        var roomName = $(tag).val();
        var roomIndex = Number($(tag).closest(".accordion-item").attr("data-roomIndex"));
        $("[data-bs-target='#roomInfo" + roomIndex + "']").text(roomName + " 정보 -- 여기를 눌러 닫으세요");
        $("#roomDelete"+roomIndex).text(roomName + " 삭제하기");
    })
}

function addRoom(){
    var lastRoomIndex = Number($("#roomInfo").attr("data-maxRoomIndex"));
    var newRoomIndex = lastRoomIndex + 1;
    $("#roomInfo").append($(
    '<div class="accordion-item" id="room' + newRoomIndex + '" data-roomIndex="' + newRoomIndex + '">' +
    '    <h2 class="accordion-header">' +
    '        <button class="accordion-button bg-warning-subtle" type="button" data-bs-toggle="collapse"' +
    '                data-bs-target="#roomInfo' + newRoomIndex + '" aria-expanded="true"' +
    '                aria-controls="roomInfo' + newRoomIndex + '">방 정보 -- 여기를 눌러 닫으세요</button>' +
    '    </h2>' +
    '    <div id="roomInfo' + newRoomIndex + '" class="accordion-collapse collapse show">' +
    '        <div class="accordion-body border border-light-subtle">' +
    '            <!--방 상태-->' +
    '            <div class="row g-3 align-items-center py-2">' +
    '                <div class="col-3 text-end">' +
    '                    <label class="col-form-label">방 상태</label>' +
    '                </div>' +
    '                <div class="col-8 text-start">' +
    '                    <div class="form-check form-check-inline">' +
    '                        <input class="form-check-input roomAvailable" type="radio" value="OPEN" name="roomStatus' + newRoomIndex + '" id="roomStatus' + newRoomIndex + '-Open" checked>' +
    '                        <label class="form-check-label" for="roomStatus' + newRoomIndex + '-Open">영업중</label>' +
    '                    </div>' +
    '                    <div class="form-check form-check-inline">' +
    '                        <input class="form-check-input roomAvailable" type="radio" value="CLOSED" name="roomStatus' + newRoomIndex + '" id="roomStatus' + newRoomIndex + '-Close">' +
    '                        <label class="form-check-label" for="roomStatus' + newRoomIndex + '-Close">영구 폐쇄</label>' +
    '                    </div>' +
    '                    <div class="form-check form-check-inline">' +
    '                        <input class="form-check-input roomAvailable" type="radio" value="PAUSE" name="roomStatus' + newRoomIndex + '" id="roomStatus' + newRoomIndex + '-Pause">' +
    '                        <label class="form-check-label" for="roomStatus' + newRoomIndex + '-Pause">임시 폐쇄</label>' +
    '                    </div>' +
    '                </div>' +
    '            </div>' +
    '            <!--방 이름-->' +
    '            <div class="row g-3 align-items-center py-2">' +
    '                <div class="col-3 text-end">' +
    '                    <label class="col-form-label" for="roomName' + newRoomIndex + '">방 이름</label>' +
    '                </div>' +
    '                <div class="col-5">' +
    '                   <input type="text" class="form-control" id="roomName' + newRoomIndex + '"' +
    '                          aria-describedby="nameWarn' + newRoomIndex + '"' +
    '                           data-error="방 이름이" aria-label="방 이름" placeholder="방 이름을 입력하세요."/>' +
    '                </div>' +
    '                <div class="col-4 text-start">' +
    '                    <span class="form-text" id="nameWarn' + newRoomIndex + '">최대 50자</span>' +
    '                </div>' +
    '            </div>' +
    '            <!--방 가격-->' +
    '            <div class="row g-3 align-items-center py-2">' +
    '                <div class="col-3 text-end">' +
    '                    <label class="col-form-label" for="roomPrice' + newRoomIndex + '">방 가격</label>' +
    '                </div>' +
    '                <div class="col-5">' +
    '                   <input type="number" class="form-control" id="roomPrice' + newRoomIndex + '"' +
    '                           data-error="방 가격이" aria-label="방 가격" placeholder="방 가격을 입력하세요."/>' +
    '                </div>' +
    '                <div class="col-4 text-start">' +
    '                   원' +
    '                </div>' +
    '            </div>' +
    '            <!--방 상세 정보-->' +
    '            <div class="row g-3 align-items-center py-2">' +
    '                <div class="col-3 text-end">' +
    '                    <label class="col-form-label" for="roomDetail' + newRoomIndex + '">방 상세 정보</label>' +
    '                </div>' +
    '                <div class="col-8">' +
    '                    <textarea class="form-control" id="roomDetail' + newRoomIndex + '"' +
    '                              aria-label="방 상세 정보" placeholder="방 상세 정보를 입력하세요."></textarea>' +
    '                </div>' +
    '            </div>' +
    '            <!--방 사진 첨부-->' +
    '            <div class="row g-3 py-2">' +
    '                <div class="col-3 text-end">' +
    '                    <label class="col-form-label" for="roomImage' + newRoomIndex + '">방 사진 첨부</label>' +
    '                </div>' +
    '                <div class="col-5">' +
    '                    <input type="file" multiple="multiple" id="roomImage' + newRoomIndex + '" class="form-control"' +
    '                           aria-label="사진" data-imageIndex="0"' +
    '                           aria-describedby="explain-roomImageWarn' + newRoomIndex + '" onchange="addRoomFile(this);"/>' +
    '                </div>' +
    '                <div class="col-4 text-start" id="roomImageWarn' + newRoomIndex + '">' +
    '                    <span class="form-text" id="explain-roomImageWarn' + newRoomIndex + '">.gif, .jpg, .png 형식만 최대 10장까지 가능<br>최대 파일 용량은 100MB<br>파일명 100자 이하 가능</span>' +
    '                </div>' +
    '            </div>' +
    '            <!--방 시설-->' +
    '            <div class="row g-3 py-2">' +
    '                <div class="col-3 text-end">' +
    '                    <label class="col-form-label">방 시설</label>' +
    '                </div>' +
    '                <div class="col-4 text-start">' +
    '                    <div class="input-group mb-3">' +
    '                        <input type="text" class="form-control" data-error="방 시설이" aria-label="방 시설" id="room' + newRoomIndex + '-facilityInput" placeholder="방 시설을 추가하세요.">' +
    '                        <button class="btn btn-outline-secondary" type="button" onclick="addRoomFacility();">추가</button>' +
    '                    </div>' +
    '                </div>' +
    '                <div class="col-4 text-start" id="room' + newRoomIndex + '-facilityList">' +
    '                </div>' +
    '            </div>' +
    '            <!--방 예약-->' +
    '            <div class="row g-3 py-2">' +
    '                <div class="col-3 text-end">' +
    '                    <label class="col-form-label">방 예약</label>' +
    '                </div>' +
    '                <div class="col-8 text-start">' +
    '                    <div class="calendar"></div>' +
    '                    <span class="form-text">- 방을 등록하기 전, 해당 방의 현재 예약 상태를 등록하세요.' +
    '                        <br>- 예약 기간만큼을 드래그해서 입력하세요.' +
    '                        <br>- 오늘을 기준으로 이전의 예약은 추가할 수 없습니다.' +
    '                        <br>- 등록된 예약을 클릭하시면 취소됩니다.</span>' +
    '                </div>' +
    '            </div>' +
    '            <!--방 삭제-->' +
    '            <button class= "btn btn-danger" id="roomDelete' + newRoomIndex + '" type="button" onclick="deleteRoom();">방 삭제하기</button>' +
    '        </div>' +
    '    </div>' +
    '</div>'
    ));
    makeCalendar(newRoomIndex);
    makeRoomFile(newRoomIndex);
    makeRoomFacility(newRoomIndex);
    changeName("#roomName" + newRoomIndex);
    $("#roomInfo").attr("data-maxRoomIndex", newRoomIndex);
}

function deleteRoom(){
    if($(".accordion-item").length == 1){
        alert("한 개 이상의 방을 등록하세요.");
        return false;
    }
    var result = confirm("입력해둔 방 정보가 사라집니다.\n진행하시려면 '확인'버튼을 누르시고\n유지하시려면 '취소'버튼을 누르세요.");
    if(result){
        var roomIndex = $(event.target).closest(".accordion-item").attr("data-roomIndex");
        deleteCalendar(roomIndex);
        deleteRoomFile(roomIndex);
        deleteFacility(roomIndex);
        $("#room" + roomIndex).remove();
    }
}