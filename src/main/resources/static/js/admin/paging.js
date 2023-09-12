$(document).ready(function(){
    $("#searchBtn").on("click",function(e) {
        e.preventDefault();
        page(0);
    });
});

function page(page){
    var byBigCategory = $("#bigCategory").val();
    var bySmallCategory = $("#smallCategory").val();
    var byState = $("#state").val();
    var byCity = $("#city").val();
    var byStoreName = $("#storeName").val();
    var byStoreStatus = $("#status").val();

    location.href="/admin/stores" +
    "?page=" + page +
    "&byBigCategory=" + byBigCategory +
    "&bySmallCategory=" + bySmallCategory +
    "&byState=" + byState +
    "&byCity=" + byCity +
    "&byStoreName=" + byStoreName +
    "&byStoreStatus=" + byStoreStatus;
}