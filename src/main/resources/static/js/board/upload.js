/**
 * upload.js
 * **/

let uploadUrl = urlList.uploadUrl != null ? urlList.uploadUrl : null;
let udtUrl = urlList.udtUrl != null ? urlList.udtUrl : null;

let formData = new FormData();

/** 공지 설정 숨김 기능  **/
function checkboxEventHandler() {
    const checkbox = document.getElementById('notice');
    const noticeDateLabel = document.querySelector('label[for="noticeDate"]');
    const noticeDateInput = document.getElementById('noticeDate');
    let noticeDateStart = document.getElementById('noticeDateStart');
    let noticeDateEnd = document.getElementById('noticeDateEnd');

    if (checkbox.checked) {
        noticeDateLabel.style.display = 'inline-block';
        noticeDateInput.style.display = 'block';
        checkbox.value = 'Y';
    } else {
        noticeDateLabel.style.display = 'none';
        noticeDateInput.style.display = 'none';
        noticeDateStart.value = '';
        noticeDateEnd.value = '';
        checkbox.value = 'N';
    }
}


/** 게시글 등록 **/
function upload(content) {

    confirm_pop("게시글을 등록하시겠습니까?", function () {
        let title = document.getElementById('title').value;
        let notice = document.getElementById('notice').value;
        let noticeDateStart = moment(document.getElementById('noticeDateStart').value).format('YYYY-MM-DD');
        let noticeDateEnd = moment(document.getElementById('noticeDateEnd').value).format('YYYY-MM-DD');
        let files = fileList;

        formData.append('title', title);
        if (noticeDateStart !== 'Invalid date') { // noticeDate 값이 유효한 경우에만 추가
            formData.append('noticeDateStart', noticeDateStart);
        }
        if (noticeDateEnd !== 'Invalid date') { // noticeDate 값이 유효한 경우에만 추가
            formData.append('noticeDateEnd', noticeDateEnd);
        }
        formData.append('content', content);
        formData.append('notice', notice);

        console.log(noticeDate)

        addFiles(files);

        formAjax(formData, uploadUrl);
    });

    alert_pop(1, "등록되었습니다.");
}

/** 게시글 수정 **/
function update(content) {

    confirm_pop("게시글을 수정하시겠습니까?", function () {
        let title = document.getElementById('title').value;
        // let content = contents;
        let notice = document.getElementById('notice').value;
        let noticeDateStart = moment(document.getElementById('noticeDateStart').value).format('YYYY-MM-DD');
        let noticeDateEnd = moment(document.getElementById('noticeDateEnd').value).format('YYYY-MM-DD');
        let files = fileList;
        let delFiles = deleteFileList;

        formData.append('boardNo', boardNo);
        formData.append('title', title);
        if (noticeDateStart !== 'Invalid date') { // noticeDate 값이 유효한 경우에만 추가
            formData.append('noticeDateStart', noticeDateStart);
        }
        if (noticeDateEnd !== 'Invalid date') { // noticeDate 값이 유효한 경우에만 추가
            formData.append('noticeDateEnd', noticeDateEnd);
        }
        formData.append('content', content);
        formData.append('notice', notice);
        formData.append('delFiles', delFiles);

        addFiles(files);

        formAjax(formData, udtUrl);
    });

    alert_pop(1, "수정되었습니다.");
}


/** formData에 업로드 파일 추가 **/
function addFiles(files){

    for(let i = 0; i < files.length; i++){

        if(files[i] !== 'x'){
            formData.append('files', files[i]);
        }
    }

}

function formAjax(data, url) {

    $.ajax({
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        url: url,
        enctype : 'multipart/form-data',
        processData: false,
        contentType: false,
        data: data, // 수정된 부분
        type: "POST",
        success: function (result, status, xhr) {
            console.log("통신 성공");
            if(xhr.status === 200) { // HTTP 상태 코드가 200일 때
                let currentUrl = window.location.href; // 현재 URL 가져오기
                if(currentUrl.includes('/admin/')) { // 만약 현재 URL이 '/admin/'을 포함한다면
                    if(result.data == "Y"){
                        location.replace("/admin/board/selNoticeBoard");
                    }else if(result.data == "N") {
                        location.replace("/admin/board/selBoard");
                    }
                } else {
                    if(result.data == "Y"){
                        location.replace("/board/selNoticeBoard");
                    }else if(result.data == "N"){
                        location.replace("/board/selBoard");
                    }
                }
            } else if(xhr.status === 400) { // HTTP 상태 코드가 400일 때
                alert_pop(1, result); // result에 담긴 에러 메시지 출력
            }
        },
        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");
            alert_pop(1, error);
        },
    });
}