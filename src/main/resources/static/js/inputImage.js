var fileNo = 0;
var fileArray = new Array();
function addFile(selectedFiles) {
    var maxFileAmount = 10;
    var currentFileAmount = $(".image-file").length;
    var remainFileAmount = maxFileAmount - currentFileAmount;
    var selectedFileAmount = selectedFiles.files.length;

    if(selectedFileAmount > remainFileAmount){
        alert("이미지는 최대" + maxFileAmount + "개 까지 첨부 가능합니다.");
    }
    $("#explain-image").hide();
    for(var i = 0; i < Math.min(selectedFileAmount, remainFileAmount); i++){
        const file = selectedFiles.files[i];
        if(validation(file)) {
            var reader = new FileReader();
            reader.onload = function () {
                fileArray.push(file);
            };
            reader.readAsDataURL(file);
            let imageList = '';
            imageList += '<div id="file' + fileNo + '" class="image-file">' + file.name;
            imageList += '   <a class="delete"  style="cursor:pointer; text-decoration : none; color:inherit;" onclick="deleteFile(' + fileNo + ');">✖<i class="far fa-minus-square"></i></a>';
            imageList += '</div>';
            $("#image-list").append(imageList);
            fileNo++;
        }else{
            continue;
        }
    }
    $("input [type='file']").val("");
    console.log(fileArray);
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
    $("#file" + fileNo).remove();
    fileArray[fileNo].is_delete = true;
}

function addFormData(formData){
    for(var i = 0; i < fileArray.length; i++){
        if(fileArray[i].is_delete == true){
            continue;
        }
        formData.append("imageFile", fileArray[i]);
    }
    if(fileArray.length == 0){
        formData.append("imageFile", null);
    }
    return formData;
}

function getFileArray(){
    return fileArray;
}