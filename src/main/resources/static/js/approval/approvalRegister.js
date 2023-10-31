let docTypeNo = '';
let docFormCode = '';
let fileList = new Array();
let fileIndex = 0;


/*  문서타입 select onchange 핸들러    */
function handleSelectChange() {
    docTypeNo = document.getElementById("docType").value;

    if(docTypeNo === '') {
        document.getElementById("document_box").style.display = "none";
        document.getElementById("doc_submit_btn").style.display = "none";
    } else {
        let url = "/aprv/selAprvRegDocTypeDetail/" + docTypeNo;
        requestDocType(url);
    }
}

/*  신규작성 - 단일 양식 정보 요청  */
function requestDocType(url){
    $.ajax ({
        url	: url,                                          // 요청이 전송될 URL 주소
        type	: "GET",                                    // http 요청 방식 (default: ‘GET’)
        processData : true,                                 // 데이터를 컨텐트 타입에 맞게 변환 여부
        dataType    : "json",
        success: function(response) {
            setDocumentBox(response.doctype);
            docFormCode = response.doctype.docFormCode;
            //연차 종류 select에 리스트 넣기
            if(response.doctype.docFormCode === 'ANU') {
                removeAllListItems();
                setAnnualTypeCodeList(response.typeCodes);
                crear_select();
            }
            numberSet();
        },
        error: function(error){
            alert_pop(1, "서버 ERROR 관리자에게 문의 하세요");
        }
    });
}

/*  문서 양식 보이기   */
function setDocumentBox(documentType) {
    document.getElementById("document_box").style.display = "";
    document.getElementById("doc_submit_btn").style.display = "";
    document.getElementById("document_header_title").textContent = documentType.docFormName;
    document.getElementById("document_content").innerHTML = documentType.docFormContents;
    setReferenceRow();

    fileInput = document.getElementById("doc_file_input");

    /** input 태그에서 'change' 이벤트가 발생했을 때 실행될 함수를 정의합니다 **/
    fileInput.addEventListener('change', function (event) {

        appendFile(event.target.files);
        // event.target.files에는 input에서 선택된 파일들이 FileList의 형태로 저장

        for (let i = 0; i < event.target.files.length; i++) {
            fileList.push(event.target.files[i]);
        }
    });
}

/*  참조자 & 첨부파일 세팅하기 - 기존 양식에 테이블 있는지 여부   */
function setReferenceRow() {
    // 기존 참조자 & 첨부파일 테이블 제거
    let referenceRows = document.getElementsByClassName('reference_row');
    while(referenceRows[0]) {
        referenceRows[0].parentNode.removeChild(referenceRows[0]);
    }

    let contentElement = document.getElementById("document_content");
    let tableElement = contentElement.querySelector("table");

    if(!tableElement) {
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
            <button type="button" class="btn_md navy file" style="width: 130px; float: right" onclick="fileBtn()">파일첨부</button>
            <ul class="upload_box">
            </ul>
        </td>
    </tr>`;
}

/*  문서 등록 버튼    */
function docRegBtn() {
    let approverList = getHidden('approver');
    if(approverList.length < 1) {
        alert_pop(1, "결재자를 추가해주세요.");
    } else {
        confirm_pop("문서를 등록하시겠습니까?", function () {
            let formData = new FormData();
            const currentDate = new Date();
            const year = currentDate.getFullYear().toString().slice(-2);  // 년도에서 마지막 두 자리만 가져오기
            const month = (currentDate.getMonth() + 1).toString().padStart(2, '0');  // 월은 0부터 시작하므로 1을 더해줌
            const day = currentDate.getDate().toString().padStart(2, '0');
            let docTitle = docFormCode + '-' + year + month + day;

            formData.append('docTTL', docTitle);
            formData.append('docFormNo', docTypeNo);
            formData.append('docCn', getDocumentContent());
            formData.append('approver', approverList);
            formData.append('referrer', getHidden('referrer'));
            addFiles(formData, fileList);
            docSendData(formData, "/aprv/insDocAprvMain");
        });
    }
}

/*  결재자/참조자 정보 배열형태로 받기   */
function getHidden(name) {
    let list = [];
    let hiddenSpans = document.querySelectorAll(".hidden-" + name);
    hiddenSpans.forEach(span => {
        list.push(span.textContent);
    });
    return list;
}

/* 결재 문서 임시작성 버튼 */
function docTempSaveBtn() {
    confirm_pop("작성 중인 문서를 임시로 저장하시겠습니까?", function () {
        docTempReg();
        // alert_pop(1, "임시저장되었습니다.");
    });
}

/*  문서 등록 ajax  */
function docSendData(data, url) {
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
        data: data,
        type: "POST",
        success: function (result) {
            alert_pop2(1, result.msg, function () {
                window.location.replace("/aprv/selDocAprv/main");
            });
        },
        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");
            alert_pop(1, error);
        },
    });
}

/*  문서 내용 읽기    */
function getDocumentContent() {
    removeAllListItems();
    let documentElement = document.getElementById("document_body");
    let inputElements = documentElement.getElementsByTagName("input");

    //id 리스트
    let idList = [];
    for (let i = 0; i < inputElements.length; i++) {
        if (!inputElements[i].id) {
            inputElements[i].id = "doc-input-" + i;
        }
        idList.push(inputElements[i].id);
    }

    //value 리스트
    let values = [];
    for (let i = 0; i < inputElements.length; i++) {
        let value = inputElements[i].value;
        values.push(value);
    }

    //값 넣기
    let content = document.getElementById("document_box").innerHTML;
    for (let i = 0; i < values.length; i++) {
        const inputId = idList[i];
        const inputValue = values[i];

        content = content.replace('id="' + inputId + '"', 'id="' + inputId + '" value="' + inputValue +'"');
    }

    //select 값 넣기
    let selectElement = documentElement.querySelector("select");
    if (selectElement) {
        let selectedOptionValue = selectElement.value;
        let search = '<option value="' + selectedOptionValue + '">';
        let result = '<option value="' + selectedOptionValue + '" selected>';
        content = content.replace(search, result);
    }

    return content;
}

/*  결재 담당자 add 버튼   */
function addBtn() {
    setMemberModal('approver');
    setMemberModal('referrer');
    modal_onoff(1, 1);
}

function setMemberModal(idName) {
    //모달 리셋
    let ulElement = document.getElementById(idName + "-listbox");
    let liElements = ulElement.getElementsByTagName('li');
    while(liElements.length > 0) {
        clickListDeleteBtn(liElements[0].querySelector('button'));
    }

    //li 세팅해주기
    let elements = document.getElementsByClassName("hidden-" + idName);
    for (let i = 0; i < elements.length; i++) {
        const item = document.getElementById('member-li-' + elements[i].textContent);
        moveList(idName, item);
    }
}

/* 임직원 지정 모달 등록 버튼 */
function memberHandlerBtn() {
    confirm_pop('결재자 및 참조자를 지정하시겠습니까?', function () {
        addApprover();
        addReferrer()
        modal_onoff(1, 2);
    });
}

/*  참조자 지정 모달 등록  */
function addReferrer() {
    const ul = document.getElementById("referrer-listbox");
    const liElements = ul.querySelectorAll("li");
    let referrerTxt = "";
    let referrerHtml = "";

    // 참조자 고유번호 정보 list에 저장
    let referrer = Array.from(liElements).map(li => li.getAttribute("value"));

    // 참조자 직책 / 이름 추가
    for(let i = 0; i < referrer.length; i++) {
        const staff = document.getElementById("memberStaff-" + referrer[i]).textContent;
        const name = document.getElementById("memberName-" + referrer[i]).textContent;
        const result = name + ' ' + staff;
        referrerTxt += i === 0 ? result :  ', ' + result;

        referrerHtml += '\n<span class="hidden-referrer">' + referrer[i] + '</span>';
    }
    document.getElementById('referrerTd').innerHTML = referrerTxt + referrerHtml;
}

/*  결재자 지정 모달 등록 버튼  */
function addApprover() {
    const ulElement = document.getElementById("approver-listbox");
    const liElements = ulElement.querySelectorAll("li");

    if(liElements.length === 0) {
        alert_pop(1, "결재자가 선택되지 않았습니다.");
    } else {
        resetApproverTbl();

        // 결재자 고유번호 정보 list에 저장
        let approver = Array.from(liElements).map(li => li.getAttribute("value"));
        let referrers = Array.from(document.querySelectorAll('#referrerTd .hidden-referrer')).map(e => e.textContent);
        for (let i = 0; i < approver.length; i++) {
            if (referrers.includes(approver[i])) {
                alert_pop(1, "결재자로 선택하려는 사람이 이미 참조자로 지정되어 있습니다: " + document.getElementById("memberName-" + approver[i]).textContent + " " + document.getElementById("memberStaff-" + approver[i]).textContent);
                return;
            }
        }

        // 결재자 이름 / 직책 결재자 - 결재자 컬럼에 추가
        for(let i = 0; i < approver.length; i++) {
            addColumn({
                memberNo: approver[i],
                staff: document.getElementById("memberStaff-" + approver[i]).textContent,
                member: document.getElementById("memberName-" + approver[i]).textContent });
        }
    }
}

/*  결재자 서명칸 리셋  */
function resetApproverTbl() {
    const table = document.getElementById('approver-tbl');
    // 테이블 내용 모두 지우기
    while (table.firstChild) {
        table.removeChild(table.firstChild);
    }

// 새로운 행(tr) 요소 생성
    const newRow1 = document.createElement('tr');
    newRow1.id = 'approver-staff';

    const newRow2 = document.createElement('tr');
    newRow2.id = 'approver-blank';

// 첫 번째 행(tr)에 셀(td) 추가
    const cell1Row1 = document.createElement('td');
    cell1Row1.setAttribute('rowspan', '2');
    cell1Row1.style.lineHeight = '2em';
    cell1Row1.innerHTML = '결<br>재';
    newRow1.appendChild(cell1Row1);

    const cell2Row1 = document.createElement('td');
    cell2Row1.textContent = '담　당';
    newRow1.appendChild(cell2Row1);

// 두 번째 행(tr)에 셀(td) 추가
    const cellForRow2 = document.createElement('td');
    cellForRow2.id = 'drafter-box';

    const buttonElementForRow2 = document.createElement('button');
    buttonElementForRow2.type = 'button';
    buttonElementForRow2.className ='btn_md gray';
    buttonElementForRow2.setAttribute("onclick", "addBtn()");

    const spanElementForButtonInSecondTr= document.createElement("span");
    spanElementForButtonInSecondTr.className="material-symbols-outlined";
    spanElementForButtonInSecondTr.textContent="add";

    buttonElementForRow2.appendChild(spanElementForButtonInSecondTr);
    cellForRow2.appendChild(buttonElementForRow2);
    newRow2.appendChild(cellForRow2);

// 테이블에 새로운 행(tr) 추가
    table.appendChild(newRow1);
    table.appendChild(newRow2);
}

/*  결재라인 열 추가   */
function addColumn(memberInfo) {
    const table = document.getElementById('approver-tbl');
    const newCell = [];
    for(let i = 0; i < table.rows.length; i++) {
        newCell[i] = table.rows[i].insertCell(-1);
        newCell[i].classList.add('approver-box');
    }
    newCell[0].innerText = memberInfo.staff;
    newCell[1].innerHTML = '<span class="hidden-approver">' + memberInfo.memberNo + '</span>';
}


////////////////////////////////// 셀렉트 박스 관련 //////////////////////////////////
let dropListId;

function handleList(item) {
    if (item.classList.contains('list-selected')) {
        item.addEventListener('mouseup', function() {
            item.classList.remove('list-selected');
        }, { once: true });
    } else {
        item.classList.toggle('list-selected');
    }
}

function dropList(id, e) {
    dropListId = id;

    if(id === 'approver') {
        swapListItems(e);
    }
}

/* 드래그앤드롭 발생 시 지정된 li 이동 */
function dragoverEvent() {
    const selectedItems = document.querySelectorAll('.list-selected');
    selectedItems.forEach(item => {
        moveList(dropListId, item);
    });
}

/*  더블클릭 이벤트    */
function doubleClickList(item) {
    moveList('approver', item)
}

/*  리스트 이동  */
function moveList(listboxId, item) {
    const ulElement = document.getElementById(listboxId + '-listbox');
    const text = item.textContent;
    const value = item.value;
    const liElement = document.createElement('li');
    liElement.textContent = text;
    liElement.value = value;
    liElement.className = listboxId + '-li';
    if(listboxId === 'approver') {
        liElement.setAttribute('draggable', 'true');
        liElement.addEventListener('dragstart', () => {
            liElement.classList.add('dragging');
        });
        liElement.addEventListener('dragend', () => {
            liElement.classList.remove('dragging')
        });
    }

    const deleteButton = document.createElement('button');
    deleteButton.className = 'delete-button';
    deleteButton.textContent = 'X';
    deleteButton.onclick = function() {
        clickListDeleteBtn(this);
    };

    liElement.appendChild(deleteButton);
    ulElement.appendChild(liElement);
    item.className = 'disabled-li';
    item.removeAttribute('onmousedown');
    item.removeAttribute('draggable');
    item.removeAttribute('ondragend');
}

function getDragAfterElement(container, y) {
    const draggableElements = [...container.querySelectorAll('.approver-li:not(.dragging)')]

    return draggableElements.reduce((closest, child) => {
        const box = child.getBoundingClientRect() //해당 엘리먼트에 top값, height값 담겨져 있는 메소드를 호출해 box변수에 할당
        const offset = y - box.top - box.height / 2 //수직 좌표 - top값 - height값 / 2의 연산을 통해서 offset변수에 할당
        if (offset < 0 && offset > closest.offset) { // (예외 처리) 0 이하 와, 음의 무한대 사이에 조건
            return { offset: offset, element: child } // Element를 리턴
        } else {
            return closest
        }
    }, { offset: Number.NEGATIVE_INFINITY }).element;
}

function swapListItems(event) {
    const list = document.getElementById('approver-listbox')
    const afterElement = getDragAfterElement(list, event.clientY);
    const draggable = document.querySelector('.dragging');

    // 만약 동일한 ul 내에서 이동하는 경우에만 위치 변경
    if (afterElement && afterElement.nextSibling) {
        list.insertBefore(draggable, afterElement.nextSibling);
    } else {
        list.appendChild(draggable); // 다음 형제 요소가 없는 경우 맨 끝으로 이동
    }
}

function clickListDeleteBtn(item) {
    const parentElement = item.parentElement;

    //임직원 li 복구
    const itemId = parentElement.value;
    const memberId = 'member-li-' + itemId;
    const liElement = document.getElementById(memberId);
    liElement.className = 'list';
    liElement.draggable = true;
    liElement.onmousedown = function() {
        handleList(this);
    };
    liElement.ondragend = function() {
        dragoverEvent();
    };

    //결재자&참조자 li 제거
    parentElement.parentElement.removeChild(parentElement);
}

///// 파일 첨부 기능 ////////////////////////////////////////////////////////////////////////////////////////////

/** input 태그를 참조합니다 **/
let fileInput = document.getElementById("doc_file_input");
/** input 태그에서 'change' 이벤트가 발생했을 때 실행될 함수를 정의합니다 **/
fileInput.addEventListener('change', function (event) {

    appendFile(event.target.files);
    // event.target.files에는 input에서 선택된 파일들이 FileList의 형태로 저장

    for (let i = 0; i < event.target.files.length; i++) {
        fileList.push(event.target.files[i]);
    }
});

/** 파일 첨부 요소 **/


/**  파일 첨부 버튼  **/
function fileBtn() {
    fileInput.click(); // 숨겨진 파일 입력 요소 클릭
}

/** upload_box에 첨부 파일에 추가 **/
function appendFile(files){
    let documentContainer = document.getElementById("document_container");
    let uploadBox = documentContainer.querySelector(".upload_box");

    for (let i = 0; i < files.length; i++) {
        let file = files[i];
        /*formData.append(file.name, file);*/

        let  fileLi = document.createElement('li');
        fileLi.classList.add('fileLi');

        let fileNameSpan = document.createElement('span');
        fileNameSpan.classList.add('file_tit');
        fileNameSpan.textContent = file.name ;


        let fileBtn = document.createElement('button');
        fileBtn.classList.add('btn_del');
        fileBtn.setAttribute('type','button');
        fileBtn.setAttribute('title','삭제하기');
        fileBtn.setAttribute('onclick','fileDelete(event,'+fileIndex+')');
        fileBtn.style.marginLeft = '5px';
        fileBtn.style.float = 'left';

        let btnSpan = document.createElement('span');
        btnSpan.classList.add('material-symbols-outlined');
        btnSpan.textContent = 'close';

        fileBtn.appendChild(btnSpan);
        fileLi.appendChild(fileNameSpan);
        fileLi.appendChild(fileBtn);
        uploadBox.appendChild(fileLi);

        fileIndex++;
    }
}

/** 첨부 등록 파일 삭제 버튼 **/
function fileDelete(event, fileIndex) {

    fileList[fileIndex] = "x";

    // 자기 자신을 없애기
    let fileLi = event.target.closest('li');
    fileLi.remove();

}

/** formData에 업로드 파일 추가 **/
function addFiles(formData,files){

    for(let i = 0; i < files.length; i++){

        if(files[i] !== 'x'){
            formData.append('files', files[i]);
        }
    }

}

function inputChange(){
    const inputs = document.querySelectorAll('input');

    // 각 input 요소에 대해 onchange 이벤트 리스너를 추가합니다.
    inputs.forEach(function(input) {
        input.addEventListener('change', function(e) {
            // 입력값이 변경되면, 그 값을 해당 input 요소의 value로 설정합니다.
            e.target.value = e.target.value;
        });
    });
}


function numberSet() {

    const numInput = document.querySelectorAll('input[name=number]');

    for (let i = 0; i < numInput.length; i++) {
        numInput[i].addEventListener('keyup', function (e) {
            let value = e.target.value;
            value = Number(value.replaceAll(',', ''));
            if (isNaN(value)) {
                numInput[i].value = 0;
            } else {
                const formatValue = value.toLocaleString('ko-KR');
                numInput[i].value = formatValue;
            }
        })
    }
}

//////////////////////////////연차 신청서 관련//////////////////////////////
/*  연차 종류 코드 리스트 넣기   */
function setAnnualTypeCodeList(codeList) {
    const selectElement = document.getElementById('annualType');
    for(let i = 0; i < codeList.length; i++) {
        const code = codeList[i];
        const optionElement = document.createElement("option");
        optionElement.text = code.codeAccount;
        optionElement.value = code.codeAccount;
        selectElement.appendChild(optionElement);
    }
}

/*  연차 종류 select onchange 핸들러   */
function annualTypeOnchangeHandler(selectElement) {
    let selectedValue = selectElement.value;
    let diffDays = selectedValue === '반반차' ? 0.25 : 0.5;
    if(selectedValue === '반반차' || selectedValue === '반차') {
        document.getElementById("annualStartDate").onchange = annualStartOnchangeHandler;
        document.getElementById("annualEndDate").onchange = annualEndOnchangeHandler;
        document.getElementById('annualLeavePeriod').textContent = '총 ' + diffDays + '일';
        annualStartOnchangeHandler();
    } else {
        document.getElementById("annualStartDate").onchange = annualDateOnchangeHandler;
        document.getElementById("annualEndDate").onchange = annualDateOnchangeHandler;
        document.getElementById('annualLeavePeriod').textContent = '총 0일';
    }
}
/*  반차/반반차 연차 기간 date input 핸들러    */
function annualStartOnchangeHandler() {
    document.getElementById("annualEndDate").value = document.getElementById("annualStartDate").value;
}

function annualEndOnchangeHandler() {
    document.getElementById("annualStartDate").value = document.getElementById("annualEndDate").value;
}

/*  기본 연차 기간 date input 핸들러    */
function annualDateOnchangeHandler() {
    let startDateValue = document.getElementById('annualStartDate').value;
    let endDateValue = document.getElementById('annualEndDate').value;

    if (startDateValue && endDateValue) {
        let startDate = new Date(startDateValue);
        let endDate = new Date(endDateValue);

        // 날짜 차이 계산
        let timeDiff = Math.abs(endDate.getTime() - startDate.getTime());
        let diffDays = Math.ceil(timeDiff / (1000 * 3600 * 24)) + 1;
        if(startDate > endDate) {
            alert_pop(1, '시작일이 종료일보다 뒤에 있습니다.');
            endDateValue = '';
            endDate = '';
        }

        document.getElementById('annualLeavePeriod').textContent = '총 ' + diffDays + '일';
    }
}

//////////////////////////////셀렉트 박스 관련//////////////////////////////
function crear_select() {
    let div_cont_select = document.querySelectorAll(
        "[data-mate-select='active']"
    );
    let select_ = "";
    for (let e = 0; e < div_cont_select.length; e++) {
        div_cont_select[e].setAttribute("data-indx-select", e);
        div_cont_select[e].setAttribute("data-selec-open", "false");
        let ul_cont = document.querySelectorAll(
            "[data-indx-select='" + e + "'] > .cont_list_select_mate > ul"
        );
        select_ = document.querySelectorAll(
            "[data-indx-select='" + e + "'] >select"
        )[0];
        if (isMobileDevice()) {
            select_.addEventListener("change", function () {
                _select_option(select_.selectedIndex, e);
            });
        }
        let select_optiones = select_.options;
        document
            .querySelectorAll(
                "[data-indx-select='" + e + "']  > .selecionado_opcion "
            )[0]
            .setAttribute("data-n-select", e);
        document
            .querySelectorAll(
                "[data-indx-select='" + e + "']  > .icon_select_mate "
            )[0]
            .setAttribute("data-n-select", e);
        for (let i = 0; i < select_optiones.length; i++) {
            li[i] = document.createElement("li");
            if (
                select_optiones[i].selected == true ||
                select_.value == select_optiones[i].innerHTML
            ) {
                li[i].className = "active";
                document.querySelector(
                    "[data-indx-select='" + e + "']  > .selecionado_opcion "
                ).innerHTML = select_optiones[i].innerHTML;
            }
            li[i].setAttribute("data-index", i);
            li[i].setAttribute("data-selec-index", e);
            // funcion click al selecionar
            li[i].addEventListener("click", function () {
                _select_option(
                    this.getAttribute("data-index"),
                    this.getAttribute("data-selec-index")
                );
            });

            li[i].innerHTML = select_optiones[i].innerHTML;
            ul_cont[0].appendChild(li[i]);
        } // Fin For select_optiones
    } // fin for divs_cont_select
} // Fin Function

//select reset
function removeAllListItems() {
    let div_cont_select = document.querySelectorAll(
        "[data-mate-select='active']"
    );

    for (let e = 0; e < div_cont_select.length; e++) {
        div_cont_select[e].setAttribute("data-indx-select", e);
        div_cont_select[e].setAttribute("data-selec-open", "false");
        let ul_cont = document.querySelectorAll(
            "[data-indx-select='" + e + "'] > .cont_list_select_mate > ul"
        );

        ul_cont.forEach(ul => {
            ul.innerHTML = ''; // Removes all li elements inside ul
        });
    }
}