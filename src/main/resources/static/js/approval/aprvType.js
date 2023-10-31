/**
 * aprvType.js
 * 결재 문서 작성
 * **/

let selFormUrl = urlList.selFormUrl;
let deleteFormUrl = urlList.deleteFormUrl;
let addFolderUrl = urlList.addFolderUrl;
let udtFolderUrl = urlList.udtFolderUrl;
let selFolderUrl = urlList.selFolderUrl;

let attachFileNo;
let docFormNo;

function fileClick(fileNo) {

    let data = { fileNo: fileNo }

    formInfoAjax(data, selFormUrl)

}

/** 문서 양식 정보 조회 ajax **/
function formInfoAjax(data, url) {

    $.ajax({
        url: url,                                  // 요청이 전송될 URL 주소
        type: "GET",                            // http 요청 방식 (default: ‘GET’)
        data: data,              // 요청 시 포함되어질 데이터
        processData: true,                         // 데이터를 컨텐트 타입에 맞게 변환 여부
        contentType: "application/json",           // 요청 컨텐트 타입
        dataType: "json",
        success: function (result) {
            setValue(result.data);
        },
        error: function (error) {
            alert_pop(1, "서버 ERROR 관리자에게 문의 하세요");
        }
    });
}

// 수정할 파일 정보값 세팅
function setValue(data) {
    let result = data["result"];
    let formHtml = data["formHtml"];

    attachFileNo = result.attachFileNo;
    docFormNo = result.docFormNo;

    $('#folderNo').val(result.typeFolderNo);
    $('#docFormUseFlag').val(result.typeFlag);
    $('#docFormCode').val(result.docFormCode);
    $('#docFormName').val(result.docFormName);

    setData(formHtml);
    saveBtnHidden();

}

function formReset(){

    $('#folderNo').val(0);
    $('#folderPtag').text("선택");

    $('#docFormUseFlag').val("Y")
    $('#flagPtag').text("사용");

    $('#docFormCode').val("");
    $('#docFormName').val("");

    setNewForm();

}


// 파일 수정시 양식 저장버튼 숨김, 수정버튼 보이기
function saveBtnHidden() {
    let saveBtn = document.getElementById('submitBtn');
    let udtBtn = document.getElementById('udtBtn');

    if (saveBtn.style.display !== 'none') {
        saveBtn.style.display = 'none';
    }
    if (udtBtn.style.display !== 'block') {
        udtBtn.style.display = 'block';
    }
}
// 파일 삭제 버튼 클릭
function fileDelete(formNo) {

    Swal.fire({
        title: "삭제 하시겠습니까?",
        icon: 'warning',
        showCancelButton: true,

        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소',

    }).then((result) => {
        if (result.isConfirmed) {
            deleteAjax(deleteFormUrl + "/" + formNo)
        }
    })
}

// 삭제 ajax 통신
function deleteAjax(url) {

    $.ajax({
        url: url,
        dataType: 'html',
        type: "DELETE",
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        success: function (result) {
            console.log("통신 성공");
            treeChange(result);

        },
        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");
            console.log(xhr);
            console.log(status);
            console.log(error);
        },
    });
}



//폴더 추가 버튼
function addFolder() {

    console.log("들어옴");
    let folderName = document.getElementById('folderName').value;
    let data = { typeFolderName: folderName };

    $.ajax({
        url: addFolderUrl,
        contentType: 'application/json',    // 데이터 형식 설정
        dataType: 'html',
        data: JSON.stringify(data),         // JSON 형식으로 변환
        type: "POST",
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        success: function (result) {
            console.log("통신 성공");
            modal_onoff(3, 2);
            treeChange(result);

        },
        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");

        },
    });
}
// ajax 성공 후 왼쪽메뉴 재랜더링 메소드
function treeChange(result) {
    let folderTreeNav = $('#folderTreeRespList')
    folderTreeNav.children().remove();
    folderTreeNav.append(result);
    $('#folderName').val('');
}


// 폴더 변경 모달
function udtFolder(obj) {
    modal_onoff(4, 1);
    let name = obj.dataset.name;
    let no = obj.dataset.no;

    $('#folder').val(name);
    $('#folderNo').val(no);
}
// 폴더 명 변경
function udtFolderName() {

    let folderName = $('#folder').val();
    let folderNo = $('#folderNo').val();

    let data = { folderNo: folderNo, folderName: folderName };

    $.ajax({
        url: udtFolderUrl,
        contentType: 'application/json',    // 데이터 형식 설정
        dataType: 'html',                    // 데이터 반환 타입 설정
        data: JSON.stringify(data),         // JSON 형식으로 변환
        type: "POST",
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        success: function (result) {
            console.log("통신 성공");
            modal_onoff(4, 2);
            treeChange(result);
            location.replace("/admin/aprv/aprvType")

        },
        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");

        },
    });
}

function udtSelectBox() {

    console.log("여기는 어때")
    $.ajax({
        url: selFolderUrl,
        type: "POST",
        dataType: "html",

        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success: function (result) {
            console.log("통신 성공");

            console.log(result)
            let select = document.getElementById('folderNo');

            console.log(select);
            while (select.childNodes.length != 0) {
                select.childNodes[0].remove();
            }

            select.append(result)




        },


        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");

        },
    });

}
//폴더 순서 선택 버튼 활성화
function orderFolder() {

    let buttons = document.querySelectorAll('.orderBtn');
    buttons.forEach((btn) => {

        if (btn.style.display === 'none') {
            btn.style.display = 'block';
        } else {
            btn.style.display = 'none';
        }
    });
    console.log("버튼 활성화");
}


// 선택 폴더 순서 위로 올림
function upFolderOrderBtn(element) {
    let folderNo = element.dataset.no;
    console.log(folderNo);

    ajax("/admin/aprv/upFolderOrder", folderNo, "PUT", "html")
        .then((responseData) => {
            console.log("성공")
            treeChange(responseData);
        })
        .catch((error) => {
            console.error("실패");
        });
}

// 선택 폴더 순서 아래로 내림
function downFolderOrderBtn(element) {
    let folderNo = element.dataset.no;
    console.log(folderNo);

    ajax("/admin/aprv/downFolderOrder", folderNo, "PUT", "html")
        .then((responseData) => {
            console.log("성공")
            treeChange(responseData);
        })
        .catch((error) => {
            console.error("실패");
        });
}

// 문서양식 리스트 정보 조회
function docFormList() {
    let data;
    ajax("/admin/aprv/selDocFormList", data, "GET", "html")
        .then((responseData) => {
            console.log("성공");
            console.log(responseData)
            let listBox = $('#docForm-listbox');
            listBox.children().remove();
            listBox.append(responseData)
        })
        .catch((error) => {
            console.error("실패");
        });
}


function ajax(url, data, type, returnType) {
    return new Promise((resolve, reject) => {
        $.ajax({
            url: url,
            data: JSON.stringify(data),
            type: type,
            dataType: returnType,
            beforeSend: function (xhr) {
                /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },
            success: resolve,
            error: reject,
            contentType: "application/json"
        });
    });
}


function moveUp(button) {
    const listItem = button.closest('li');
    const previousItem = listItem.previousElementSibling;

    if (previousItem) {
        listItem.parentNode.insertBefore(listItem, previousItem);
        updateOrderValues();
    }
}

function moveDown(button) {
    const listItem = button.closest('li');
    const nextItem = listItem.nextElementSibling;

    if (nextItem) {
        nextItem.parentNode.insertBefore(nextItem, listItem);
        updateOrderValues();
    }
}

function updateOrderValues() {
    const listItems = document.querySelectorAll('.list-dept');
    listItems.forEach((item, index) => {
        const orderValueSpan = item.querySelector('span:first-child');
        orderValueSpan.textContent = index + 1;
    });
}

//////////////////////////////////////////////////미리보기 모달 관련//////////////////////////////////////////////////
/*  모달 호출   */
function clickPreviewBtn() {
    setPreviewBox();
    modal_onoff(6, 1);
}

/*  문서 양식 보이기   */
function setPreviewBox() {
    document.getElementById("document_header_title").textContent = document.getElementById('docFormName').value;
    document.getElementById("document_content").innerHTML = getFormContent();
    setReferenceRow();
}

/*  참조자 & 첨부파일 세팅하기 - 기존 양식에 테이블 있는지 여부   */
function setReferenceRow() {
    // 기존 참조자 & 첨부파일 테이블 제거
    let referenceRows = document.getElementsByClassName('reference_row');
    while (referenceRows[0]) {
        referenceRows[0].parentNode.removeChild(referenceRows[0]);
    }

    let contentElement = document.getElementById("document_content");
    let tableElement = contentElement.querySelector("table");

    if (!tableElement) {
        tableElement = document.getElementById("document_default_top").querySelector("table");
    }

    tableElement.insertAdjacentHTML('beforeend', getReferenceRow());
}

/*  참조자 & 첨부파일 세팅하기   */
function getReferenceRow() {
    return `<tr class="reference_row">
        <th>참조자</th>
        <td id="referrerTd">
        </td>
    </tr>
    <tr class="reference_row">
        <th>첨부파일</th>
        <td>
            <input type="file" style="display: none" id="doc_file_input" multiple/>
            <button type="button" class="btn_md navy file" style="width: 130px; float: right"">파일첨부</button>
            <ul class="upload_box">
            </ul>
        </td>
    </tr>`;
}

function changeDocFormOrder() {
    let data = setDocFormList();

    Swal.fire({
        title: "변경 하시겠습니까?",
        icon: 'warning',
        showCancelButton: true,
        confirmButtonColor: '#3085d6',
        cancelButtonColor: '#d33',
        confirmButtonText: '확인',
        cancelButtonText: '취소',

    }).then((result) => {
        if (result.isConfirmed) {
            ajax("/admin/aprv/udtDocFormOrder", { data: data }, 'PUT', 'json')
                .then((responseData) => {
                    console.log("성공");
                    Swal.fire({
                        title: "변경 성공",
                        icon: "success",
                        confirmButtonColor: "#3085d6",
                        confirmButtonText: "확인"
                    }).then(() => {
                        modal_onoff(5, 2);
                    });
                })
                .catch((error) => {
                    console.error("실패");
                    Swal.fire({
                        title: "변경 실패",
                        text: "오류가 발생했습니다.",
                        icon: "error",
                        confirmButtonColor: "#3085d6",
                        confirmButtonText: "확인"
                    });
                });
        }
    });
}


function setDocFormList() {
    let listItems = document.querySelectorAll('.list-dept');
    let liInfoList = [];

    listItems.forEach(function (item) {
        let orderValue = item.querySelector('span:first-child').textContent;
        let docFormTypeNo = item.querySelector('span:nth-child(2)').getAttribute('value');

        liInfoList.push({
            orderValue: orderValue,
            docFormTypeNo: docFormTypeNo,
        });
    });
    return liInfoList;
}
//////////////////////////////////양식 폴더 삭제 관련//////////////////////////////////
function deleteFolderBtn(item) {
    confirm_pop('양식 폴더를 삭제하시겠습니까?', function () {
        deleteAjax("/admin/aprv/delDocTypeFolder/" + item.value);
    });
}

document.getElementById("star").addEventListener("click", function() {
    console.log("즐겨찾기")

    let icon = document.querySelector("#star svg");

    if (icon.classList.contains("bi-pin-angle-fill")) {
        icon.classList.remove("bi-pin-angle-fill");
        icon.classList.add("bi-pin-angle");
    } else {
        icon.classList.remove("bi-pin-angle");
        icon.classList.add("bi-pin-angle-fill");
    }
});