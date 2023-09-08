var fileArray = [];
var maxFileAmount = 10;

class image {
    constructor(id, imageFile) {
        this.id = id;
        this.is_delete = "N";
        this.imageFile = imageFile;
    }
}

$(document).ready(function(){
    $("#explain-image").hide();
    var lastIndex = Number($("#input-image").attr("data-imageIndex"));
    for(var i = 0; i < lastIndex+1; i++){
        fileArray.push(new image($("#image-file" + i).attr("data-id"), null));
    }
});

function deleteFile(index){
    fileArray[index].is_delete = "Y";
    console.log(fileArray);
    $("#image-file"+index).remove();
    var currentFileAmount = $("#image-list .image-file-list").length;
    if(currentFileAmount == 0 || currentFileAmount == null){
        $("#explain-image").show();
    }
}

function addFile(selectedFiles) {
    var currentFileAmount = $("#image-list .image-file-list").length;
    var remainFileAmount = maxFileAmount - currentFileAmount;
    var selectedFileAmount = selectedFiles.files.length;
    var numberForFile = Number($("#image-list").attr("data-imageIndex"));

    if(selectedFileAmount > remainFileAmount){
        alert("이미지는 최대" + maxFileAmount +"개 까지 첨부 가능합니다.");
    }

    $("#explain-image").hide();

    for(var i = 0; i < Math.min(selectedFileAmount, remainFileAmount); i++){
        const file = selectedFiles.files[i];
        if(validation(file)) {
            numberForFile++;
            var reader = new FileReader();
            reader.onload = function () {
                fileArray.push(new image(null, file));
            };
            reader.readAsDataURL(file);
            let imageList = '';
            imageList += '<div id="image-file' + numberForFile + '" class="image-file-list">' + file.name;
            imageList += '   <a class="delete"  style="cursor:pointer; text-decoration : none; color:inherit;" onclick="deleteFile(' + numberForFile + ');">✖</a>';
            imageList += '</div>';
            $("#image-list").append(imageList);
        }else{
            continue;
        }
    }
    $("#input-image").attr("data-imageIndex", numberForFile);
    $("#input-image").val("");
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

function checkImages(resultObject){
    var count = 0;
    for(var i = 0; i < fileArray.length; i++){
        if(fileArray[i].is_delete == "Y"){
            count++;
        }
    }
    if(fileArray.length == count || fileArray.length == 0){
        resultObject["#input-image"] = "이미지를 1개 이상 업로드해주세요.";
    }
}

function addImageToFormData(formData){
    for(var i = 0; i < fileArray.length; i++){
        if(fileArray[i].id != null ){
            formData.append("imageList["+i+"].imageId", fileArray[i].id);
        }
        formData.append("imageList["+i+"].isDelete", fileArray[i].is_delete);
        if(fileArray[i].imageFile != null){
            formData.append("imageList["+i+"].imageFile", fileArray[i].imageFile);
        }
    }
}