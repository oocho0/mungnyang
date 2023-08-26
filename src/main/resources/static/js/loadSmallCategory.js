function loadSmallCategory() {
    var bigCategoryId = $("#bigCategory option:selected").val();
    if(bigCategoryId == "대분류 선택"){
        return;
    }
    var data = null;
    var url = "/category/" + bigCategoryId;
    $.ajax({
        url : url,
        type : "GET",
        async : false,
        dataType : "json",
        cache : false,
        success : function(result, status){
            console.log("success");
            data = result;
        },
        error : function(status, error){
                console.log("fail");
            alert(status.responseText);
        }
    });
    console.log(data);

}