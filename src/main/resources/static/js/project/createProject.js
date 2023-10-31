
//진행률 범위 판정
function progressListener() {
    let element = document.getElementById('projectProgress');
    let num = Number(element.value);
    if(isNaN(num) || num > 100 || num < 0) {
        alert_pop(1, '진행률은 0 이상 100 이하의 숫자만 받습니다. 다시 입력해주세요.');
        element.value = '';
    }
}

//등록 버튼
//seq = 1: 일반 등록 / seq = 2: 일반 수정
//seq = 3: 관리 등록 / seq = 4: 관리 수정
function clickRegProjectBtn(seq) {
    let isAllFilled = true;
    let data = {};
    let arr = [
        { id: 'projectName', name: '프로젝트 명' },
        { id: 'projectDescription', name: '프로젝트 설명' },
        { id: 'projectType', name: '프로젝트 구분' },
        { id: 'projectStartDate', name: '시작일' },
        { id: 'projectEndDate', name: '종료일' },
        { id: 'projectState', name: '프로젝트 상태' },
        { id: 'projectProgress', name: '진행률' },
        { id: 'projectManhour', name: '투입공수' },
        { id: 'projectResources', name: '투입인력' },
        { id: 'projectOverview', name: '프로젝트 개요' },
        { id: 'projectObjective', name: '프로젝트 목적' },
        { id: 'projectFeatures', name: '주요기능' },
        { id: 'projectRemarks', name: '비고' }
    ];

    //비고 제외 모두 차 있는지 확인
    for(let i = 0; i < arr.length; i++) {
        let value = document.getElementById(arr[i].id).value;
        if(i < arr.length - 1 && (value === '' || value === ' ' || !value)) {
            alert_pop(1, '[' + arr[i].name + '] 칸이 비어있습니다. 값을 입력해주세요.');
            isAllFilled = false;
            break;
        } else {
            data[arr[i].id] = value;
        }
    }

    if(isAllFilled) {
        let startDate = new Date(data.projectStartDate);
        let endDate  = new Date(data.projectEndDate);

        if(startDate > endDate)
            alert_pop(1, '시작일이 종료일보다 이후입니다. 날짜를 다시 지정해주세요.');
        else {
            let msg = "프로젝트를 " + (seq % 2 === 1 ? "등록" : "수정") + "하시겠습니까?";
            confirm_pop(msg, function () {
                if(seq === 2 || seq === 4) {
                    data['projectNo'] = projectNo;
                }
                sendProjectData(data, seq);
            });
        }
    }
}

function sendProjectData(data, seq) {
    console.log('sendProjectData');
    let url = seq % 2 === 1 ? "/project/insProjectMain" : "/project/udtProjectMain";

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
                let redirectUrl = seq % 2 === 1 ? "/project/selProjectMain" : "/project/selProjectMainDetail/" + projectNo;
                window.location.href = seq > 2 ? "/admin" + redirectUrl : redirectUrl;
            });
            console.log("post 성공");
        },
        error: function (error) {
            alert_pop(1, error);
            console.error("post 실패");
        }
    });
}