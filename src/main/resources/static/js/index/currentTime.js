let today = new Date();

////////////////////// 달력 기능 //////////////////////
//월 이름 얻기
const months = ["January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"];
function getMonthName(monthIndex) {
    return months[monthIndex];
}

//현재 시간 기준 달력 표시
function showCalendar(nowDate) {
    const year = nowDate.getFullYear();
    const month = nowDate.getMonth();
    const now = new Date();
    const todayDate = (year === now.getFullYear() && month === now.getMonth()) ? now.getDate() : 0;
    const firstDayOfMonth = new Date(year, month, 1);
    const lastDateOfMonth = new Date(year, month + 1, 0).getDate();
    const startDayOfWeek = firstDayOfMonth.getDay();

    // 년도와 월 표시 업데이트
    const yearMonthElement = document.getElementById("year_month");
    yearMonthElement.textContent = `${getMonthName(month)} ${year}`;

    // 달력 표의 데이터 업데이트
    const calendarTableBody = document.querySelector('.calendar tbody');
    calendarTableBody.innerHTML = '';

    let date = 1;
    for (let i = 0; i < 6; i++) {
        const row = document.createElement('tr');
        for (let j = 0; j < 7; j++) {
            if (i === 0 && j < startDayOfWeek) {
                // 첫 주에서 첫째 주 시작 요일 이전은 빈 칸으로 처리
                const emptyCell = document.createElement('td');
                row.appendChild(emptyCell);
            } else if (date > lastDateOfMonth) {
                // 마지막 날짜 이후는 빈 칸으로 처리
                const emptyCell = document.createElement('td');
                row.appendChild(emptyCell);
            } else {
                const cell = document.createElement('td');
                cell.textContent = date;
                if(date === todayDate) {
                    cell.classList.add('today');
                }
                row.appendChild(cell);
                date++;
            }
        }
        calendarTableBody.appendChild(row);
    }
}

// 초기에 달력 표시
showCalendar(new Date());

//현재 달력 날짜 구하기
function getDate() {
    const yearMonthElement = document.getElementById('year_month');
    const yearMonthText = yearMonthElement.textContent.trim(); // Assuming the format is "Month Year" e.g., "June 2023"
    const [monthText, yearText] = yearMonthText.split(' ');
    const monthIndex = months.indexOf(monthText);
    const year = parseInt(yearText, 10);

    const dateObject = new Date(year, monthIndex, 1);

    return dateObject;
}

//이전 달 캘린더
function preCaleandar() {
    const nowCalendar = getDate();
    let newCalendar;

    if(nowCalendar.getMonth() === 1)
        newCalendar = new Date(nowCalendar.getFullYear() - 1, 12, 1);
    else
        newCalendar = new Date(nowCalendar.getFullYear(), nowCalendar.getMonth() - 1, 1);

    showCalendar(newCalendar);
}

//다음 달 캘린더
function nextCaleandar() {
    const nowCalendar = getDate();
    let newCalendar;

    if(nowCalendar.getMonth() === 12)
        newCalendar = new Date(nowCalendar.getFullYear() + 1, 1, 1);
    else
        newCalendar = new Date(nowCalendar.getFullYear(), nowCalendar.getMonth() + 1, 1);

    showCalendar(newCalendar);
}

////////////////////// 현재 시간 표시 //////////////////////
let clockTarget = document.getElementById("db_time");

function clock() {
    today = new Date();
    let year = today.getFullYear();
    let month = today.getMonth() + 1;
    let clockDate = today.getDate();

    let hours = today.getHours();
    let minutes = today.getMinutes();
    let seconds = today.getSeconds();

    clockTarget.innerHTML = `<p>현재시간</p>` +
        `<p>${hours < 10 ? `0${hours}` : hours}:${minutes < 10 ? `0${minutes }` : minutes}:${seconds < 10 ? `0${seconds}` : seconds}</p>` +
        `<p>${year}년 ${month < 10 ? `0` + month : month}월 ${clockDate < 10 ? `0` + clockDate : clockDate}일</p>`;
}

setInterval(clock, 1000);


////////////////////// 출퇴근 기능 //////////////////////
//기준 시간 Date 타입으로 변환
function getStandardDate(strTime){
    let [standardHours, standardMinutes, standardSeconds] = strTime.split(':').map(Number);
    let standardTime = new Date(today.getFullYear(), today.getMonth(), today.getDate(), standardHours, standardMinutes, standardSeconds);
    return standardTime;
}
//출근 기능
function isOpenTime() {
    let standardOpenDate = getStandardDate(standardOpenTime);   //Date타입 출근 기준 시간
    let todayDate = today;                                    //현재 출근했다고 찍은 시간
    const standardTimeInMillis = standardOpenDate.getTime();    //출근 기준 시간 밀리초 변환
    const todayInMillis = todayDate.getTime();                      //현재 시간 밀리초 변환
    const checkInDate = todayDate.toLocaleString("en-GB"); //현재 시간 String 형태 (일/월/년, 시:분:초)

    // 비교
    if (todayInMillis > standardTimeInMillis) {
        console.log("지각입니다.");
        reason_modal_on("지각", checkInDate);
    } else {
        sendRollbook("정상 출근", checkInDate, null);
    }
}

//퇴근 기능
function isCloseTime() {
    let standardOpenDate = getStandardDate(standardOpenTime);   //Date타입 출근 기준 시간
    let standardCloseDate = getStandardDate(standardCloseTime);  //Date타입 퇴근 기준 시간
    let todayDate = today;                                      //현재 출근했다고 찍은 시간
    const standardOpenTimeInMillis = standardOpenDate.getTime();    //출근 기준 시간 밀리초 변환
    const standardTimeInMillis = standardCloseDate.getTime();    //퇴근 기준 시간 밀리초 변환
    const todayOutMillis = todayDate.getTime();                      //현재 시간 밀리초 변환
    const checkOutDate = todayDate.toLocaleString("en-GB"); //현재 시간 String 형태 (일/월/년, 시:분:초)

    // 비교
    if (todayOutMillis >= standardOpenTimeInMillis && todayOutMillis < standardTimeInMillis) {
        console.log("조퇴입니다.");
        reason_modal_on("조퇴", checkOutDate);
    } else {
        sendRollbook("정상 퇴근", checkOutDate, null);
    }
}

//모달 팝업
function reason_modal_on(state, date) {
    document.getElementById("modal-title").textContent = state + " 사유";
    document.getElementById("rollbook_time").value = date;
    document.getElementById("rollbook_state").value = state;
    modal_onoff(1, 1);
}

//사유 모달 - 사유 등록 버튼 클릭
function submitRollbookBtn() {
    console.log('모달 등록 버튼 클릭');
    let state = document.getElementById("rollbook_state").value;
    let time = document.getElementById("rollbook_time").value;
    let reason = state + " 사유: " + document.getElementById("rollbook_reason").value;
    sendRollbook(state, time, reason);

    //모달 닫고 리셋
    modal_onoff(1, 2);
    document.getElementById("rollbook_reason").value = "";
}

//출퇴근 정보 전달
function sendRollbook(rollbookState, rollbookTime, reason) {
    let postData = {
        rollbookState: rollbookState,
        rollbookTime: rollbookTime,
        rollbookContents: reason
    };
    let url = "/main/insRollbook";
    sendPostRequest(url, postData);
}

//ajax로 정보 전달
function sendPostRequest(url, data) {
    console.log('sendPostRequest');

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
                window.location.href = "/main/main/";
            });
            console.log("post 성공");
        },
        error: function (error) {
            alert_pop(1, error);
            console.error("post 실패");
        }
    });
}

////////////////////// modal 기능 //////////////////////
function modal_onoff(seq, flag) {
    if (flag === 1) {
        $("#modal_" + seq).css("display", "block");
    } else if (flag === 2) {
        $("#modal_" + seq).css("display", "none");
    }
}