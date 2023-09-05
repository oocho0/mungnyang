var fileArray = new Array();
var maxFileAmount = 10;

function addFile(selectedFiles) {
    var eventId = $(event.target).attr("id");
    fileWorks(eventId, "image-file", selectedFiles, "#explain-image", fileArray, "#image-list", "#input-image", "deleteFile");
}

var roomFileArray = [[],[],[],[],[],[],[],[],[],[]];

function addRoomFile(selectedFiles){
    var eventId = $(event.target).attr("id");
    var roomNumber = Number($(event.target).closest(".accordion-item").attr("data-roomIndex"));
    var inputElement = "room-image-file" + roomNumber;
    var explainElement = "#explain-roomImageWarn" + roomNumber;
    var fileListElement = "#roomImageWarn" + roomNumber;
    fileWorks(eventId, inputElement, selectedFiles, explainElement, roomFileArray[roomNumber - 1], fileListElement, "#"+eventId, "deleteRoomImage");
}

function fileWorks(eventId, inputElement, selectedFiles, explainElement, arrayForFile, fileListElement, inputTagId, deleteFunction){
    var currentFileAmount = $("#"+eventId).closest("div").parent("div").find(".image-file-list").length;
    var remainFileAmount = maxFileAmount - currentFileAmount;
    var selectedFileAmount = selectedFiles.files.length;
    var numberForFile = $("#"+eventId).attr("data-imageIndex");

    if(selectedFileAmount > remainFileAmount){
        alert("이미지는 최대" + maxFileAmount +"개 까지 첨부 가능합니다.");
    }

    $(explainElement).hide();

    for(var i = 0; i < Math.min(selectedFileAmount, remainFileAmount); i++){
        const file = selectedFiles.files[i];
        if(validation(file)) {
            var reader = new FileReader();
            reader.onload = function () {
                arrayForFile.push(file);
            };
            reader.readAsDataURL(file);
            let imageList = '';
            imageList += '<div id="' + inputElement + numberForFile + '" class="image-file-list">' + file.name;
            imageList += '   <a class="delete"  style="cursor:pointer; text-decoration : none; color:inherit;" onclick="' + deleteFunction + '(' + numberForFile + ');">✖</a>';
            imageList += '</div>';
            $(fileListElement).append(imageList);
            numberForFile++;
        }else{
            continue;
        }
    }
    $("#"+eventId).attr("data-imageIndex", numberForFile);
    $(inputTagId).val("");
    console.log(arrayForFile);
}

function validation(file){
    const fileTypes = ['image/jpeg', 'image/gif', 'image/png'];
    if(file.name.length > 100){
        alert("이미지의 파일명이 100자 이상인 파일은 제외되었습니다.");
        return false;
    }
    if(file.size > (100 * 1024 * 1024)){
        alert("최대 파일 용량인 100MB를 초과한 파일은 제외되었습니다.");
        return false;
    }
    if(file.name.lastIndexOf(".") == -1){
        alert("확장자가 없는 파일은 제외되었습니다.");
        return false;
    }
    if(! fileTypes.includes(file.type)){
        alert("첨부가 불가능한 파일은 제외되었습니다.")
        return false;
    }
    return true;
}

function deleteFile(fileNo){
    var idName = $(event.target).closest("div").attr("id");
    fileArray[fileNo].is_delete = true;
    console.log(fileArray);
    $("#" + idName).remove();
    var currentFileAmount = $("#image-list .image-file-list").length;
    if(currentFileAmount == 0 || currentFileAmount == null){
        $("#explain-image").show();
    }
}

function deleteRoomImage(fileNo){
    var idName = $(event.target).closest("div").attr("id");
    var roomNo = Number($("#"+idName).closest(".accordion-item").attr("data-roomIndex"));
    roomFileArray[roomNo-1][fileNo].is_delete = true;
    console.log(roomFileArray);
    console.log(roomFileArray[roomNo-1]);
    $("#" + idName).remove();
    var currentFileAmount = $("#room"+ roomNo + " .image-file-list").length;
    if(currentFileAmount == 0 || currentFileAmount == null){
        $("#explain-roomImageWarn"+roomNo).show();
    }
}

function addFormData(formData){
    for(var i = 0; i < fileArray.length; i++){
        if(fileArray[i].is_delete == true){
            continue;
        }
        formData.append("imageFile", fileArray[i]);
    }
}

function addFormDataWithRoom(formData, i){
    var array = {};
    var k = 0;
    for(var j = 0 ; j < roomFileArray[i].length; j ++){
        if(roomFileArray[i][j].is_delete == true){
            continue;
        }
        formData.append("roomList[" + i + "].imageFile["+k+"]", roomFileArray[i][j]);
        k++;
    }
}


function getFileArray(){
    return fileArray;
}

function accommodationImageCheck(resultObject){
    imageCheck(fileArray, resultObject, "#input-image");
}

function roomImageCheck(i, resultObject){
    var inputTagId = "#roomImage"+ i
    imageCheck(roomFileArray[i - 1], resultObject, inputTagId)
}

function imageCheck(arrayForFile, resultObject, tagName){
    var count = 0;
    for(var i = 0; i < arrayForFile.length; i++){
        if(arrayForFile[i].is_delete == true){
            count++;
        }
    };
    if(arrayForFile == null || arrayForFile.length == count){
        resultObject[tagName] = "이미지를 1개 이상 업로드해주세요.";
    }
}

function deleteAllImages(i){
    roomFileArray[i-1] = [];
    $("#roomImage"+i).val('');
    $("#room-image-file"+i).hide();
    $("#explain-roomImageWarn"+i).show();
}