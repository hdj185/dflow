
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
    document.getElementById("projForm").submit();
}

//프로젝트 상세페이지 리다이렉트
function redirectToProjectDetail (projectNo) {
    console.log('redirectToProjectDetail 발생: ', projectNo);
    const url = '/project/selProjectMainDetail/' + projectNo;
    location.href = url;
}

// 검색 유효성 검사
function validateSearchOp() {

    let typeCode = document.getElementById('projectType').value;
    let projState = document.getElementById('projectState').value;
    let keywordType = document.getElementById('keywordType').value;
    let searchTxt = document.getElementById('search_txt').value;
    let startDate = document.getElementById('startDate').value;
    let endDate = document.getElementById('endDate').value;

    if ((startDate != "" && endDate == "") || (startDate == "" && endDate != "")) {
        alert_pop(1, "정확한 날짜 범위를 입력하세요.");
        return false;  // Prevent form submission
    }

    if (new Date(startDate) > new Date(endDate)) {
        alert_pop(1, "시작날짜가 끝날짜보다 늦을 수 없습니다.");
        return false;  // Prevent form submission
    }

    if (typeCode == "" && projState == "" && keywordType == "" && searchTxt.trim() == "" && (startDate == "" || endDate == "")) {
        alert_pop(1, "검색 조건을 선택하세요.");
        return false;
    }

    if (keywordType != "" && searchTxt.trim() == "") {
        alert_pop(1, "검색어를 입력하세요.");
        return false;
    }

    return true;
}