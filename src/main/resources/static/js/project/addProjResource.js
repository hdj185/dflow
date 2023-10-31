let focusRowIdx = 0;
//프로젝트 드롭다운 선택 이벤트
function projHandler() {
    // 선택된 option 엘리먼트 가져오기
    const selectedOption = document.getElementById("project").options[document.getElementById("project").selectedIndex];

    // data-manhour와 data-resources 값을 가져오기
    const manhour = selectedOption.getAttribute('data-manhour');
    const resources = selectedOption.getAttribute('data-resources');

    // 값 넣기
    document.getElementById("plannedManhourTd").textContent = manhour;
    document.getElementById("plannedResourceTd").textContent = resources;
    document.getElementById("sum").textContent = '';

    resetTable(resources);

    if(selectedOption.value === '0') {
        document.getElementById("resourceBtn").style.display = "none";
        const table = document.getElementById("resourceTbl");
        const newRow = table.insertRow(1);
        const newCell = newRow.insertCell(0);
        newCell.colSpan = 7;
        newCell.innerText = "공수인력을 등록할 프로젝트를 선택해주세요.";
    } else {
        document.getElementById("resourceBtn").style.display = "";
    }
}

//테이블 칸 리셋
function resetTable(resources) {
    console.log('resetTable!');
    const table = document.getElementById("resourceTbl");
    const tbody = table.querySelector("tbody");
    let trs = tbody.querySelectorAll("tr");

    console.log('trs length:', trs.length);


    //첫 번째 tr을 제외한 모든 tr 삭제
    for(let i = 1; i < trs.length - 1; i++)
        tbody.removeChild(trs[i]);

    //resources만큼 tr 추가
    for(let i = 0; i < resources; i++)
        addRow();

    //addRow();
    trs = tbody.querySelectorAll("tr");
    console.log('resources만큼 tr 추가', trs.length);
}

//Row 추가
function addRow() {
    const table = document.getElementById("resourceTbl");
    const tbody = table.querySelector("tbody");
    const trs = tbody.querySelectorAll("tr");
    const newRowIdx = trs.length - 1;
    const newRow = table.insertRow(newRowIdx);
    const colCnt = 8;
    let newCell = [];

    for (let i = 0; i < colCnt; i++)
        newCell[i] = newRow.insertCell(i);

    newCell[0].innerHTML = getCheckboxHtml(newRowIdx);
    newCell[1].innerText = newRowIdx;
    newCell[2].innerHTML = getNameInputHtml('memberName', newRowIdx);
    newCell[3].innerHTML = getDateInputHtml('resourceStartDate', newRowIdx);
    newCell[4].innerHTML = getDateInputHtml('resourceEndDate', newRowIdx);
    newCell[5].innerHTML = getSpanHtml('resourceDuration-' + newRowIdx);
    newCell[6].innerHTML = getTxtInputHtml('resourceProgress', newRowIdx, 'manhourHandler(' + newRowIdx + ')');
    newCell[7].innerHTML = getTxtInputHtml('manhour', newRowIdx, 'setSum()');
    // newCell[7].innerHTML = getSpanHtml('manhour-' + newRowIdx);
}

function deleteBtn() {
    confirm_pop("정말 행을 삭제하시겠습니까?", function () {
        //선택된 체크박스 모두 받아오기
        const selectedCheckboxes = document.querySelectorAll('.cb_container.custom_cb_container input[type="checkbox"]:checked');

        //삭제하는 반복문
        for(let i = 0; i < selectedCheckboxes.length; i++) {
            let checkbox = selectedCheckboxes[selectedCheckboxes.length - i - 1];
            checkbox.checked = false;
            deleteRow(Number(checkbox.value));
        }

        alert_pop(1, '선택하신 행이 삭제되었습니다!');
    });
}

function deleteRow(idx) {
    const table = document.getElementById("resourceTbl");
    const tbody = table.querySelector("tbody");
    const trs = tbody.querySelectorAll("tr");

    for (let i = idx + 1; i < trs.length - 1; i++) {
        const currentRow = trs[i];
        const previousRow = trs[i - 1];
        const currentCells = currentRow.cells;
        const previousCells = previousRow.cells;

        previousCells[2].querySelector("input").value = currentCells[2].querySelector("input").value;
        previousCells[3].querySelector("input").value = currentCells[3].querySelector("input").value;
        previousCells[4].querySelector("input").value = currentCells[4].querySelector("input").value;
        previousCells[5].querySelector("span").textContent = currentCells[5].querySelector("span").textContent;
        previousCells[6].querySelector("input").value = currentCells[6].querySelector("input").value;
        previousCells[7].querySelector("input").value = currentCells[7].querySelector("input").value;
    }
    trs[trs.length - 2].remove();
}

//Cell HTML 관리
function getCheckboxHtml(idx) {
    return '<label class="cb_container custom_cb_container" for="checkbox-' + idx + '">' +
        '<input type="checkbox" id="checkbox-' + idx + '" value="' + idx + '">' +
        '<span class="cb_checkmark"></span>' +
        '</label>';
}
function getNameInputHtml(name, idx) {
    const resultId = name + '-' + idx;
    return '<input type="text" id="' + resultId + '" ' +
        'onfocus="modal_pop(' + idx + ')" ' +
        'class="input_txt" style="width: 100%;" autocomplete="off"/>';
}

function getTxtInputHtml(name, idx, onchange) {
    const resultId = name + '-' + idx;
    return '<input type="text" id="' + resultId + '" ' +
        'onchange="' + onchange + '" ' +
        'class="input_txt" style="width: 100%;" />';
}
function getDateInputHtml(name, idx) {
    const resultId = name + '-' + idx;
    return '<input type="date" id="' + resultId + '" ' +
        'onchange="dateHandler(' + idx + ')" ' +
        'style="width: 90%" class="date_choice" />';
}
function getSpanHtml(id) {
    return '<span id="' + id + '"></span>';
}

//name input 핸들러
function nameHandler() {
    let ul = document.getElementById("listbox-3");
    let selectedLi = ul.querySelector(".list.list-selected");

    if (selectedLi) {
        let selectedValue = selectedLi.getAttribute("value");   //선택한 값
        const nameInput = document.getElementById("memberName-" + focusRowIdx); //값 넣을 input
        console.log("Selected value: " + selectedValue);
        nameInput.value = selectedValue;    //값 넣기
        modal_on_off(1, 2);  //모달 닫기
    } else {
        confirm_pop('직원을 선택하지 않으셨습니다. 이대로 모달창을 닫습니까?', function () {
            document.getElementById("memberName-" + focusRowIdx).value = '';
            modal_on_off(1, 2);
        });
    }
}

//date 핸들러
function dateHandler(idx) {
    //날짜 받아오기
    const resourceStartDate = document.getElementById("resourceStartDate-" + idx).value;
    const resourceEndDate = document.getElementById("resourceEndDate-" + idx).value;

    //투입시작, 투입종료 모두 값이 있을 경우
    if(resourceStartDate !== '' && resourceEndDate !== '') {
        const startDate = new Date(resourceStartDate);
        const endDate = new Date(resourceEndDate);

        //투입기간 계산
        const duration = (endDate.getTime() - startDate.getTime()) / (1000 * 60 * 60 * 24) + 1;

        //투입기간 칸에 text 값 지정
        document.getElementById('resourceDuration-' + idx).innerText = duration;
        manhourHandler(idx);
    }
}

//text input 핸들러
function manhourHandler(idx) {
    const resourceProgress = document.getElementById("resourceProgress-" + idx);
    if(resourceProgress !== '') {
        let num = Number(resourceProgress.value);
        if(isNaN(num) || num > 100 || num < 0) {
            alert_pop(1, '0부터 100 사이의 숫자를 입력하세요.');
            resourceProgress.value = '';
        } else {
            //투입기간
            const resourceDuration = document.getElementById("resourceDuration-" + idx).textContent;
            const manhour = Number(resourceDuration) * num / 100 / month;
            const roundedManhour = manhour.toFixed(2);
            const roundedManhourNumber = parseFloat(roundedManhour);
            document.getElementById("manhour-" + idx).value = roundedManhourNumber;
            setSum();
        }
    }

}

//manhour 합계
function setSum() {
    const rowLen = getRowLen();
    let sum = 0;

    for(let i = 1; i < rowLen; i++) {
        const manhour = Number(document.getElementById("manhour-" + i).value);
        sum += manhour;
    }

    document.getElementById("sum").textContent = sum;
}

//현재 테이블 row 개수 구하기
function getRowLen() {
    const table = document.getElementById("resourceTbl");
    const tbody = table.querySelector("tbody");
    const trs = tbody.querySelectorAll("tr");
    return trs.length - 1;
}

function submitData(seq) {
    let msg = "투입공수를 " + (seq % 2 === 1 ? "등록" : "수정") + "하시겠습니까?";
    confirm_pop(msg, function () {
        sendData(seq)
    });
}
//form submit
function sendData(seq) {
    console.log('submitData 버튼 클릭');
    let resources = [];
    let flag = true;
    let isNameDuplicate = false;

    //테이블 정보 받아오기
    const rowLen = getRowLen();
    for(let i = 1; i < rowLen; i++) {
        const memberName = document.getElementById("memberName-" + i).value;
        const resourceStartDate = document.getElementById("resourceStartDate-" + i).value;
        const resourceEndDate = document.getElementById("resourceEndDate-" + i).value;
        const resourceDuration = document.getElementById("resourceDuration-" + i).textContent;
        const resourceProgress = document.getElementById("resourceProgress-" + i).value;
        const resourceHours = document.getElementById("manhour-" + i).value;

        console.log('resourceDuration=', resourceDuration);
        console.log('resourceHours=', resourceHours);
        for(let j = 0; j < resources.length; j++) {
            if(memberName === resources[j].memberName) {
                isNameDuplicate = true;
                break;
            }
        }
        if(isNameDuplicate) break;

        let isAllBlank = memberName === '' && resourceStartDate === '' && resourceEndDate === '' && resourceProgress === '';
        let isNotAllBlank = memberName !== '' && resourceStartDate !== '' && resourceEndDate !== '' && resourceProgress !== '';
        flag = isAllBlank || isNotAllBlank;

        if(isNotAllBlank) {
            let resourceData = {
                memberName: memberName,
                resourceStartDate: resourceStartDate,
                resourceEndDate: resourceEndDate,
                resourceDuration: resourceDuration,
                resourceProgress: resourceProgress,
                resourceHours: resourceHours
            };
            resources.push(resourceData);
        } else if(!flag) {
            alert_pop(1, '입력이 부족한 칸이 있습니다.\n한 행의 칸을 모두 채우거나 모든 칸을 비워주세요.');
            break;
        }
    }

    if(isNameDuplicate)
        alert_pop(1, '중복되는 직원이 있습니다. 수정해주세요.');
    else if(flag && resources.length === 0)
        alert_pop(1, '모든 칸이 비어있습니다. 내용을 입력해주세요.');
    else if(flag)
        sendPostRequest(resources, seq);
}

//seq = 1: 일반 등록 / seq = 2: 일반 수정
//seq = 3: 관리 등록 / seq = 4: 관리 수정
function sendPostRequest(resources, seq) {
    let url = seq % 2 === 1 ? "/project/insResourceReq" : "/project/udtResource";
    let projectNo = seq % 2 === 1 ? document.getElementById("project").value : projNo;
    let data = {
        projectNo: projectNo,
        resources: resources };

    $.ajax({
        type: "post",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json",
        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        success: function (response) {
            alert_pop2(1, response, function () {
                let redirectUrl = seq < 3 ? "/project/selResource" : "/admin/project/selResource";
                window.location.href = redirectUrl;
            });
            console.log("post 성공");
        },
        error: function (error) {
            alert_pop(1, error);
            console.error("post 실패");
        }
    });
}

// modal pop
function modal_pop(idx) {
    focusRowIdx = idx;
    modal_on_off(1, 1);
}

// modal
function modal_on_off(seq, flag) {
    for(let i = 0; i < 3; i++) {
        if(i > 0) resetGroup(i + 1);
        resetItemClass(i + 1);
    }
    if (flag === 1) {
        $("#modal_" + seq).css("display", "block");
    } else if (flag === 2) {
        $("#modal_" + seq).css("display", "none");
    }
}

////////////////////////////////// 셀렉트 박스 관련 //////////////////////////////////

function handleListClick(item, seq) {
    if(seq == 1) {
        resetGroup(2);
        resetItemClass(2);
    }
    resetGroup(3);
    resetItemClass(3);
    let idx = item.getAttribute("value");
    changeNextGroup(idx, seq + 1);
    changeNextGroup(idx, 3);
    changeItemClass(item, seq);
}

//이전 기록 모두 삭제
function resetGroup(seq) {
    let ul = document.getElementById("listbox-" + seq);
    let groupCount = ul.getElementsByTagName("div").length;
    console.log('groupCount: ', groupCount);

    for(let i = 0; i < groupCount; i++) {
        let div = ul.getElementsByTagName("div")[i];
        div.style.display = 'none';
        console.log('div none: ', div);
    }
}
//listbox 선택 시 다음 group에 표시
function changeNextGroup(idx, seq) {
    let item = document.getElementById('group' + seq + '-' + idx);
    item.style.display='';
}

//listbox 효과
function changeItemClass(item, seq) {
    resetItemClass(seq);

    // 선택된 li 요소에 클래스를 추가
    item.classList.toggle('list-selected');
}

function resetItemClass(seq) {
    let listboxItems = document.querySelectorAll('#listbox-' + seq + ' li');

    // 모든 li 요소의 클래스를 'list'로 설정
    for (let i = 0; i < listboxItems.length; i++) {
        listboxItems[i].classList.remove('list-selected');
        listboxItems[i].classList.add('list');
    }
}