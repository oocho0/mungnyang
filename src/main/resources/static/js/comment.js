function page(page){
    var url = "/" + $("#type").val() + "/" + $("#id").val() + "/comment?page=" + page;
    $.ajax({
        url : url,
        type : "GET",
        cache   : false,
        success  : function(result, status){
            var startPage = (result.comments.number / result.comments.size) * result.comments.size + 1;
            var endPage = (result.comments.totalPages == 0) ? 1 : (startPage + result.comments.size - 1 < result.comments.totalPages ? (startPage + result.comments.size - 1) : result.comments.totalPages);
            var firstAppendClass = (result.comments.number <= result.comments.size) ? 'disabled' : '';
            var prevAppendClass = (result.comments.first) ? 'disabled' : '';
            var nextAppendClass = (result.comments.last) ? 'disabled' : '';
            var lastAppendClass = ((result.comments.totalPages / result.comments.size) <= startPage) ? 'disabled' : '';
            var pages = "";
            for(var i = startPage; i <= endPage; i++){
                if(result.comments.number == (i -1)){
                    pages += '            <li class="page-item active">' +
                }else{
                    pages += '            <li class="page-item">' +
                }
                pages += '                <a class="page-link" onclick="page(' + (i -1) + ');">' + i + '</a>' +
                pages += '            </li>' +
            }
            if($("#emptyComment").length){
                $("#emptyComment").remove();
                $("#commentList").append($(
                    '<div id="existComment">' +
                    '</div>' +
                    '<div class="mb-5">' +
                    '    <nav>' +
                    '        <ul class="pagination justify-content-center">' +
                    '            <li class="page-item" ' + firstAppendClass + ' >' +
                    '                <a class="page-link" onclick="page(0);" aria-label="first">' +
                    '                    <span aria-hidden="true">&laquo;</span>' +
                    '                </a>' +
                    '            </li>' +
                    '            <li class="page-item" ' + prevAppendClass + '>' +
                    '                <a class="page-link" onclick="page(' + (result.comments.number - 1) + ');" aria-label="Previous">' +
                    '                    <span aria-hidden="true">&lt;</span>' +
                    '                </a>' +
                    '            </li>' +
                    pages +
                    '            <li class="page-item" ' + nextAppendClass + '>' +
                    '                <a class="page-link" onclick="page(' + (result.comments.number + 1) + ');" aria-label="Next">' +
                    '                    <span aria-hidden="true">&gt;</span>' +
                    '                </a>' +
                    '            </li>' +
                    '            <li class="page-item" ' + lastAppendClass + '>' +
                    '                <a class="page-link" onclick="page(' + (result.comments.totalPages -1) + ');" aria-label="last">' +
                    '                    <span aria-hidden="true">&raquo;</span>' +
                    '                </a>' +
                    '            </li>' +
                    '        </ul>' +
                    '    </nav>' +
                    '</div>'
                ));
            }
            if($("#existComment").length){
                $("#existComment").empty();
                var contents = result.comments.content;
                $.each(contents, function(key, value){
                    var deleteBtn = "";
                    var date = moment(value.createDate);
                    var dateStr = date.format('YYYY-MM-DD');
                    if(result.email == value.email){
                        deleteBtn =  '        <button class="btn btn-outline-secondary btn-sm" onclick="deleteComment(' + value.commentId + ');">삭제 하기</button>';
                    }
                    $("#existComment").append($(
                        '<div class="card mb-2">' +
                        '    <div class="card-header bg-body d-flex justify-content-between align-items-center" data-id="' + value.commentId + '">' +
                        '        <div>' +
                        '            <span>' + value.email + '</span> / <span>' + dateStr + '</span>' +
                        '        </div>' +
                        deleteBtn +
                        '    </div>' +
                        '    <div class="card-body row">' +
                        '        <fieldset class="col-auto rating">' +
                        '            <input type="radio" id="' + value.commentId + '-rating10" name="' + value.commentId + '-rate" value="5.0" readonly><label for="' + value.commentId + '-rating10" title="5점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating9" name="' + value.commentId + '-rate" value="4.5" readonly><label class="half" for="' + value.commentId + '-rating9" title="4.5점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating8" name="' + value.commentId + '-rate" value="4.0" readonly><label for="' + value.commentId + '-rating8" title="4점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating7" name="' + value.commentId + '-rate" value="3.5" readonly><label class="half" for="' + value.commentId + '-rating7" title="3.5점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating6" name="' + value.commentId + '-rate" value="3.0" readonly><label for="' + value.commentId + '-rating6" title="3점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating5" name="' + value.commentId + '-rate" value="2.5" readonly><label class="half" for="' + value.commentId + '-rating5" title="2.5점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating4" name="' + value.commentId + '-rate" value="2.0" readonly><label for="' + value.commentId + '-rating4" title="2점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating3" name="' + value.commentId + '-rate" value="1.5" readonly><label class="half" for="' + value.commentId + '-rating3" title="1.5점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating2" name="' + value.commentId + '-rate" value="1.0" readonly><label for="' + value.commentId + '-rating2" title="1점"></label>' +
                        '            <input type="radio" id="' + value.commentId + '-rating1" name="' + value.commentId + '-rate" value="0.5" readonly><label class="half" for="' + value.commentId + '-rating1" title="0.5점"></label>' +
                        '        </fieldset>' +
                        '        <div class="col-auto">' + value.content + '</div>' +
                        '    </div>' +
                        '</div>'
                    ));
                    $("input[name='" + value.commentId + "-rate'][value='" + value.rate + "']").prop("checked", true);
                })
            }
        },
        error : function(status, error){
            alert(status.responseText);
        }
    });
}

function addComment(){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/" + $("#type").val() + "/" + $("#id").val() + "/comment";
    if($("[name='rate']:checked").length == 0){
        alert("평점을 선택하라냥!");
        return false;
    }
    if($("#content").val() == "" || $("#content").val() == null || $("#content").val() == "후기를 남겨주라냥!"){
        $("#content").attr("disabled", true);
    }
     var formData = new FormData();
     formData.append("rate", $("[name='rate']:checked").val());
     formData.append("content", $("#content").val());
     $.ajax({
         url : "/user" + url,
         type : "POST",
         processData : false,
         contentType : false,
         data : formData,
         encType : "multipart/form-data",
         cache   : false,
         beforeSend : function(xhr){
             /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
             xhr.setRequestHeader(header, token);
         },
         success : function(result, status){
             alert("댓글이 등록되었습니다.")
             page(0);
             $("#content").removeAttr("disabled");
             $("[name='rate']:checked").removeAttr("checked");
             $("#content").val("");
         },
         error : function(status, error){
             if(status.status == '401' || status.status == '403'){
                 alert('로그인 후 이용해주세요');
                 location.href='/member/login';
             } else{
                 alert(status.responseText);
             }
             $("#content").removeAttr("disabled");
         }
     });
}

function deleteComment(id){
    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");
    var url = "/" + $("#type").val() + "/" + $("#id").val() + "/comment";
    $.ajax({
        url : "/user" + url + "/" + id,
        type : "DELETE",
        cache   : false,
        beforeSend : function(xhr){
            /* 데이터를 전송하기 전에 헤더에 csrf값을 설정 */
            xhr.setRequestHeader(header, token);
        },
        success : function(result, status){
            alert("댓글 삭제가 완료되었습니다.")
            page(0);
        },
        error : function(status, error){
            if(status.status == '401' || status.status == '403'){
                alert('로그인 후 이용해주세요');
                location.href='/member/login';
            } else{
                alert(status.responseText);
            }
        }
    });
}