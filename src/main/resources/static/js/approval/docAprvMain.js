let documentNo = 0;
let spanTxt = [];   //값 더해줄 거
let spanId = [];    //span id


// 취소
function progressD() {

}




// 진행
function progressB() {
}

// 완료
function progressC() {

    let url = "/aprv/selDocAprv/completed";
    $.ajax({
        url: url,
        type: "Post",
        dataType: "html",

        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },


        success: function (data) {

            // 응답 HTML을 파싱하기 위해 임시 div를 생성합니다.
            let tempDiv = $('<div>');
            tempDiv.html(data);

            // #documentAprvCard와 #location_box의 내용을 추출합니다.
            let newDocumentAprvCard = tempDiv.find('#documentAprvCard').html();
            let newLocationBox = tempDiv.find('#location_box').html();
            let newPageCard = tempDiv.find('#pagination').html();

            // console.log(newPageCard);


            // 현재 페이지의 #documentAprvCard와 #location_box의 내용을 바꿉니다.
            $('#documentAprvCard').html(newDocumentAprvCard);
            $('#location_box').html(newLocationBox);
            $('#pagination').html(newPageCard);

        },
        error: function (err) {
            console.log(err)
        }
    });

}

// 대기
function progressA() {
    // console.log("AA");
}



/** 23-8-28
 *  검색 및 페이징 html
 * **/

//검색기간 n개월 버튼
function dateBtnHandler(month) {
    let date = new Date();
    document.getElementById("endDate").value = formatDate(date);
    date.setMonth(date.getMonth() - month);
    document.getElementById("startDate").value = formatDate(date);
}

//date format하기
function formatDate(date) {
    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');
    return `${year}-${month}-${day}`;
}

//form submit하기
function submitForm() {
    document.getElementById("docAprvForm").submit();

    // console.log(document.getElementById("docAprvForm").submit());
}

// 결재문서 상세 페이지 모달
function docAprvDetail(docNo){

    $.ajax ({
        url	: "/aprv/selDocDetail/" + docNo,                    // 요청이 전송될 URL 주소
        type	: "GET",                                        // http 요청 방식 (default: ‘GET’)
        success: function(result) {
            setDocAprvDetail(result.data);
            documentNo = docNo;
            modal_onoff(6,1);
        },
        error: function(error){
            alert_pop(1, "서버 ERROR 관리자에게 문의 하세요");
        }
    });
}
/*  결재라인 본인 서명 추가   */
function addApproveSign(drafterSignNo) {
    let element = document.getElementById("drafter-box");
    let imgUrl = drafterSignNo == null ? "" : "/files/images/" + drafterSignNo;

    //버튼 없애기
    element.innerHTML = '';
    element.style.padding = "0";
    // element.appendChild(imgElement);
    //서명 보이기
    element.style.backgroundImage = `url(${imgUrl})`;
    element.style.width = "100px";
    element.style.height = "100px";
    element.style.backgroundSize = "cover";
}

function setDocAprvDetail(data) {
    document.getElementById('docContent').innerHTML = data.docCn;
    let hiddenSpans = document.querySelectorAll(".hidden-approver");
    let docContentElement = document.getElementById('docContent');

    // 기안자 사인 넣기
    addApproveSign(data.drafterSignNo);

    // 버튼 없애기
    let buttonElements = docContentElement.querySelectorAll("button");
    for(let i = 0; i < buttonElements.length; i++)
        buttonElements[i].parentNode.removeChild(buttonElements[i]);

    //input -> span으로 교환
    let inputElements = docContentElement.querySelectorAll("input");
    let values = [];
    let ids = [];
    for(let i = 0; i < inputElements.length; i++) {
        let inputElement = inputElements[i];

        // 새로운 <span> 요소 생성 및 값 설정
        let spanElement = document.createElement("span");
        let inputElementValue = inputElement.value;
        let inputElementId = inputElement.id;
        spanElement.textContent = inputElementValue;
        spanElement.id = inputElementId;
        values.push(inputElementValue);
        ids.push(inputElementId);

        // <input> 요소 대신 <span> 요소로 교체
        inputElement.parentNode.replaceChild(spanElement, inputElement);
    }
    spanTxt = values;
    spanId = ids;

    //select -> span으로 교환
    const selectMateElements = docContentElement.getElementsByClassName("select_mate");
    Array.from(selectMateElements).forEach(function(selectMate) {
        const selectElement = selectMate.querySelector("select");
        const selectedOption = selectElement.options[selectElement.selectedIndex];
        const selectedText = selectedOption.value;
        const selectedId = selectElement.id;

        // span 요소 생성 및 텍스트 설정
        const spanElement = document.createElement("span");
        spanElement.textContent = selectedText;
        spanElement.id = selectedId;

        // 기존 div 요소 대체
        const tdParentElement = selectMate.parentNode;
        tdParentElement.replaceChild(spanElement, selectMate);
    });


    // 파일 다운로드 링크 생성
    const spanElements = document.querySelectorAll(".file_tit");

// 각 span 요소에 링크를 추가
    for (let i = 0; i < spanElements.length; i++) {
        const span = spanElements[i];

        const link = document.createElement("a");
        link.href = "/files/download/" + data.fileNo[i];
        link.textContent = span.textContent; // span 요소의 텍스트를 링크 텍스트로 설정
        span.innerHTML = ""; // span 요소 내용을 비우기
        span.appendChild(link); // span 요소에 링크 추가
    }

    // 로그인한 사용자의 no와 일치하는 값을 가진 span과 그 부모 td 요소 찾기
    hiddenSpans.forEach(span => {
        if (Number(span.textContent) === Number(userNo)) {
            let td = span.closest("td");
            if (td) {
                td.id = 'user-sign-box';
                let btn = '<button type="button" class="btn_md gray" onclick="modal_onoff(7,1)">\n' +
                    '<span class="material-symbols-outlined"> add </span>\n' +
                    '</button>';
                td.innerHTML = btn;
            }
        }
    });

    // 기안자가 본인이고 상태가 대기인 경우, 회수 버튼 출력
    document.getElementById('withdrawal-btn').style.display = data.memberNo === userNo && data.docState === '대기' ? '' : 'none';

    // 기안자가 본인이고 상태가 회수인 경우, 수정 버튼 출력
    document.getElementById('doc-modify-btn').style.display = data.memberNo === userNo && data.docState === '회수' ? '' : 'none';

    // 기안자가 본인이고 상태가 회수인 경우, 삭제 버튼 출력
    document.getElementById('doc-delete-btn').style.display = data.memberNo === userNo && data.docState === '회수' ? '' : 'none';
}

// 결재 서류 프린트
function pagePrint() {
    const html = document.querySelector('html');
    const printContents = document.querySelector('.doc_box').innerHTML;
    const printDiv = document.createElement('DIV');
    printDiv.className = 'print-div';
    html.appendChild(printDiv);
    printDiv.innerHTML = printContents;
    document.body.style.display = 'none';
    window.print();
    document.body.style.display = 'block';
    printDiv.style.display = 'none';
}

// 결재 승인/반려 버튼
function approveBtn(result) {
    confirm_pop("해당 결재 건을 " + result + "하시겠습니까?", function () {
        addApproverSign(result);  //사인 넣기
        let contents = document.getElementById("docContent").innerHTML;
        // console.log(contents);
        for(let i = 0; i < spanId.length; i++) {
            const searchId = 'id="' + spanId[i] + '"';
            const replaceResult = searchId + ' text="' + spanTxt + '"';
            contents.replace(searchId, replaceResult);
        }
        let formData = new FormData();
        formData.append("docNo", documentNo);
        formData.append("aprvOpinion", document.getElementById('aprv_opinion').value);
        formData.append("aprvResult", result);
        formData.append("memberNo", userNo);
        formData.append("aprvContent", contents);

        // console.log("data:", formData);
        aprvSendData(formData);
    });
}

// 결재 Ajax
function aprvSendData(data) {
    $.ajax({
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        url: "/aprv/udtDocAprvState",
        enctype : 'multipart/form-data',
        processData: false,
        contentType: false,
        data: data,
        type: "POST",
        success: function (result) {
            alert_pop2(1, result.msg, function () {
                modal_onoff(7,2);
                location.reload();
            });
        },
        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");
            alert_pop(1, error);
        },
    });
    modal_onoff(7,2);
    document.getElementById("aprv_opinion").value='';
}

//td에 사인 넣기
function addApproverSign(result) {
    let element = document.getElementById("user-sign-box");

    //기존 child 모두 없애기
    element.innerHTML = '';

    if(result === '승인') {
    //서명 보이기
    let imgUrl = signNo == null ? "" : "/files/images/" + signNo;
    element.style.backgroundImage = `url(${imgUrl})`;
    element.style.width = "100px";
    element.style.height = "100px";
    element.style.backgroundSize = "cover";
    } else {
        element.innerHTML = "<span style='color: #de1629'>반려</span>";
    }
    element.removeAttribute("id");
}

// 회수 버튼 클릭
function clickWithdrawalBtn() {
    confirm_pop('해당 문서를 회수하시겠습니까?', function () {
        let url = "/aprv/udtWithdrawalAprv/" + documentNo;
        // console.log('url=', url);

        $.ajax({
            beforeSend: function (xhr) {
                /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },
            url: url,
            type: "PUT", // HTTP 메서드 설정
            data: JSON.stringify({ docNo: documentNo }), // 요청 본문 데이터
            contentType: "application/json", // 요청 데이터 타입
            dataType: "json", // 응답 데이터 타입
            success: function(data) {
                alert_pop2(1, data.msg, function () {
                    console.log("Response:", data); // 서버 응답 데이터 출력
                    location.reload();
                });
            },
            error: function(error) {
                alert_pop(1, "문서 회수에 실패하였습니다.");
                console.error("Error:", error); // 에러 처리
            }
        });
    });
}

// 회수 후 수정 버튼
function modifyBtn() {
    confirm_pop('회수된 문서를 수정 하시겠습니까?', function () {
        window.location.replace("/aprv/aprvReg/udt/"+documentNo);

    });
}

/** 23-9-1
 *  회수 후 삭제
 * **/
function deleteBtn() {
    let url = "/aprv/selDocAprv/cancel/" + documentNo;

    confirm_pop('회수된 문서를 삭제 하시겠습니까?', function () {

        $.ajax({
            url: url,
            type: "DELETE",

            beforeSend: function (xhr) {
                /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },

            success : function (data){
                alert_pop2(1, "삭제 성공", function () {
                    location.replace("/aprv/selDocAprv/cancel");
                });

            },

            error : function (err){

                console.log("실패")
            }





        })



    });

}