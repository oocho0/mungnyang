$(document).ready(function(){
    setting();
});
function setting(){
    $("#roomAmount").on("change", function(){
        var roomAmount = Number($("#roomAmount option:selected").val());
        var id = $(".accordion-item:last input:first ").attr('id');
        var currentMaxNumber = Number(id[id.length -1]);
        if(roomAmount == currentMaxNumber){
            return false;
        }
        var fa = true;
        if(roomAmount < currentMaxNumber){
            var result = confirm("입력해둔 방 정보가 사라집니다.\n진행하시려면 '확인'버튼을 누르시고\n유지하시려면 '취소'버튼을 누르세요.");
            if(result){
                for(var i = roomAmount; i<currentMaxNumber+1; i++){
                    $("#room" + i).parent("div").remove();
                }
            }else{
                fa = false;
            }
        }
        if(fa == false){
            return false;
        }
        for(var i = currentMaxNumber+1; i < roomAmount+1; i++){
            $(".accordion").append($(
            '<div class="accordion-item">' +
            '    <h2 class="accordion-header">' +
            '        <button class="accordion-button" type="button" data-bs-toggle="collapse" data-bs-target="#room' + i + '" aria-expanded="true" aria-controls="room' + i + '"> 방 '+ i + ' </button>' +
            '    </h2>' +
            '    <div id="room'+ i +'" class="accordion-collapse collapse show">' +
            '        <div class="accordion-body">' +
            '            <!--방 이름-->' +
            '            <div class="row g-3 align-items-center py-2">' +
            '                <div class="col-3 text-end">' +
            '                    <label class="col-form-label" for="roomName' + i + '">방 이름</label>' +
            '                </div>' +
            '                <div class="col-5">' +
            '                    <input type="text" class="form-control" id="roomName' + i + '" name="roomName" aria-describedby="nameWarn' + i + '" data-error="방 이름이" aria-label="방 이름" placeholder="방 이름을 입력하세요."/>' +
            '                </div>' +
            '                <div class="col-4 text-start">' +
            '                    <span class="form-text" id="nameWarn' + i + '">최대 50자</span>' +
            '                </div>' +
            '            </div>' +
            '            <!--방 가격-->' +
            '            <div class="row g-3 align-items-center py-2">' +
            '                <div class="col-3 text-end">' +
            '                    <label class="col-form-label" for="roomPrice' + i + '">방 가격</label>' +
            '                </div>' +
            '                <div class="col-5">' +
            '                    <input type="number" class="form-control" id="roomPrice' + i + '" name="roomPrice" data-error="방 가격이" aria-label="방 가격" placeholder="방 가격을 입력하세요."/>' +
            '                </div>' +
            '                <div class="col-4 text-start">' +
            '                   원'  +
            '                </div>' +
            '            </div>' +
            '            <!--방 상세 정보-->' +
            '            <div class="row g-3 align-items-center py-2">' +
            '                <div class="col-3 text-end">' +
            '                    <label class="col-form-label" for="roomDetail' + i + '">방 상세 정보</label>' +
            '                </div>' +
            '                <div class="col-8">' +
            '                    <textarea class="form-control" id="roomDetail' + i + '" name="roomDetail" aria-label="방 상세 정보" placeholder="방 상세 정보를 입력하세요."></textarea>' +
            '                </div>' +
            '            </div>' +
            '            <!--방 사진 첨부-->' +
            '            <div class="row g-3 py-2">' +
            '                <div class="col-3 text-end">' +
            '                    <label class="col-form-label" for="roomImage' + i + '">방 사진 첨부</label>' +
            '                </div>' +
            '                <div class="col-5">' +
            '                    <input type="file" multiple="multiple" id="roomImage' + i + '" class="form-control" aria-label="사진" aria-describedby="explain-image" onchange="addRoomFile(this);"/>' +
            '                </div>' +
            '                <div class="col-4 text-start" id="roomImageWarn' + i + '">' +
            '                    <span class="form-text" id="explain-roomImageWarn' + i + '">.gif, .jpg, .png 형식만 최대 10장까지 가능<br>최대 파일 용량은 100MB<br>파일명 100자 이하 가능</span>' +
            '                </div>' +
            '            </div>' +
            '        </div>' +
            '    </div>' +
            '</div>'
            ));
        };
    });
}