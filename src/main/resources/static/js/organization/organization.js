/**
 * organization.js
 *
 * **/

let detailUrl = urlList.detailUrl;
let searchUrl = urlList.searchUrl;
let allUrl = urlList.allUrl;
let deptUrl = urlList.deptUrl;
let upDeptUrl = urlList.upDeptUrl;
let excelUrl = urlList.excelUrl;




/** depth1 클릭시 하위부서 드롭 **/
function depth1Click(element) {
    console.log(element)

    // depth1 요소를 선택합니다.
    let depth1 = element.querySelector('.depth2');
    let depth2 = element.querySelectorAll('.depth3');

    depth2.forEach(function (depth3) {

        if (depth3.style.display === 'block') {
            depth3.style.display = 'none';
        } else {
            depth3.style.display = 'block';
        }
    });
}

/** 검색 **/
function search() { 
    let type = document.getElementById("searchType").value;
    let keyword = document.getElementById("search_keyword").value;

    let data = {type: type, keyword: keyword};

    memberListAjax(data, searchUrl);

}

/** 부서별 직원 리스트 조회 **/
function memberList(departmentNo) {

    let data = {departmentNo: departmentNo}

    memberListAjax(data, deptUrl);

}

/** 전체 직원 리스트 조회 **/
function upMemberList(departmentNo) {

    let data = {departmentNo: departmentNo};

    memberListAjax(data, upDeptUrl);

}

/** 전체 직원 리스트 조회 **/
function memberAllList() {

    let data = {};

    memberListAjax(data, allUrl);

}


/** 직원 리스트 ajax function **/
function memberListAjax(data, url) {

    $.ajax({
        url: url,                                          // 요청이 전송될 URL 주소
        type: "GET",                                    // http 요청 방식 (default: ‘GET’)
        data: data,                                       // 요청 시 포함되어질 데이터
        processData: true,                                 // 데이터를 컨텐트 타입에 맞게 변환 여부
        contentType: "application/json",                   // 요청 컨텐트 타입
        dataType: "json",
        success: function (List) {
            document.querySelector('.profile_container').appendChild(createProfileBox(List.data, url)); // 원하는 셀렉터로 대체하세요.
            console.log("ajax 성공");
            excelData = List.data;
        },
        error: function (error) {
            alert_pop(1, "서버 ERROR 관리자에게 문의 하세요");
        }
    });

}


/** 직원 정보 상세 모달 **/
function memberInfoDetail(memberNo) {


    $.ajax({
        url: detailUrl,                                        // 요청이 전송될 URL 주소
        type: "GET",                                        // http 요청 방식 (default: ‘GET’)
        data: {memberNo: memberNo},                      // 요청 시 포함되어질 데이터
        processData: true,                                 // 데이터를 컨텐트 타입에 맞게 변환 여부
        contentType: "application/json",                   // 요청 컨텐트 타입
        dataType: "json",
        success: function (result) {
            console.log(result)
            setModal(result.data);
        },
        error: function (error) {
            alert_pop(1, "서버 ERROR 관리자에게 문의 하세요");
        }
    });
}

/** 상세 정보 모달에 자동 입력 **/
function setModal(result) {

    $('#staff_name').val(result.staffName);
    $('#department_name').val(result.departmentName);
    $('#member_enable_date').val(result.memberEnableDate);
    $('#member_gender').val(result.memberGender);
    $('#member_name_kr').val(result.memberNameKr);
    $('#member_birthdate').val(result.memberBirthdate);
    $('#member_name_cn').val(result.memberNameCn);
    $('#member_phone').val(result.memberPhone);
    $('#member_name_en').val(result.memberNameEn);
    $('#member_email').val(result.memberEmail);
    $('#member_address').val(result.memberAddress);
    var imgUrl = result.imgNo == null ? img : '/files/images/' + result.imgNo;
    $('#img').attr('src', imgUrl);
    $('#modal_' + 1).css('display', 'block');

}

/** profileBox 초기화 **/
function remove() {
    var profileBoxElements = document.querySelector('.profile_box');

    profileBoxElements.remove();

}

/** profileBox 생성 **/
function createProfileBox(memberList, url) {
    remove();
    let profileBoxElement = document.createElement('div');
    profileBoxElement.classList.add('profile_box');

    for (let i = 0; i < memberList.length; i++) {
        let memberInfo = memberList[i];
        let profileCardElement = document.createElement('div');
        profileCardElement.classList.add('profile_card');
        profileCardElement.setAttribute('onclick', "memberInfoDetail(" + memberInfo.memberNo + ")");

        let profilePhotoElement = document.createElement('div');
        profilePhotoElement.classList.add('profile_photo');

        let photoElement = document.createElement('img');
        photoElement.setAttribute('src', memberInfo.imgNo == null ? "/img/IoMdImages.png" : "/files/images/" + memberInfo.imgNo);
        photoElement.setAttribute('alt', '사진');


        profilePhotoElement.appendChild(photoElement);

        let profileInfoElement = document.createElement('div');
        profileInfoElement.classList.add('profile_info');

        let profileNameElement = document.createElement('div');
        profileNameElement.classList.add('profile_name');
        profileNameElement.textContent = memberInfo.memberNameKr + ' ' + memberInfo.staffName;

        profileInfoElement.appendChild(profileNameElement);

        let affiliationElement = document.createElement('p');
        let affiliationSpan1 = document.createElement('span');
        affiliationSpan1.textContent = '소속';
        let affiliationSpan2 = document.createElement('span');
        affiliationSpan2.textContent = '|';
        let affiliationSpan3 = document.createElement('span');
        affiliationSpan3.textContent = memberInfo.departmentName;
        affiliationElement.appendChild(affiliationSpan1);
        affiliationElement.appendChild(affiliationSpan2);
        affiliationElement.appendChild(affiliationSpan3);

        let phoneElement = document.createElement('p');
        let phoneSpan1 = document.createElement('span');
        phoneSpan1.textContent = '연락처';
        let phoneSpan2 = document.createElement('span');
        phoneSpan2.textContent = '|';
        let phoneSpan3 = document.createElement('span');
        phoneSpan3.textContent = memberInfo.memberPhone;
        phoneElement.appendChild(phoneSpan1);
        phoneElement.appendChild(phoneSpan2);
        phoneElement.appendChild(phoneSpan3);

        let emailElement = document.createElement('p');
        let emailSpan1 = document.createElement('span');
        emailSpan1.textContent = '이메일';
        let emailSpan2 = document.createElement('span');
        emailSpan2.textContent = '|';
        let emailSpan3 = document.createElement('span');
        emailSpan3.textContent = memberInfo.memberEmail;
        emailElement.appendChild(emailSpan1);
        emailElement.appendChild(emailSpan2);
        emailElement.appendChild(emailSpan3);

        profileInfoElement.appendChild(affiliationElement);
        profileInfoElement.appendChild(phoneElement);
        profileInfoElement.appendChild(emailElement);

        profileCardElement.appendChild(profilePhotoElement);
        profileCardElement.appendChild(profileInfoElement);

        profileBoxElement.appendChild(profileCardElement);
    }

    return profileBoxElement;
}

function downloadExcel(){
    fetch(excelUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
            // CSRF 토큰 설정
            [$("meta[name='_csrf_header']").attr("content")]: $("meta[name='_csrf']").attr("content")
        },
        body: JSON.stringify({excelData : excelData})
    })
        .then(response => response.blob())
        .then(blob => {
            let url = window.URL.createObjectURL(blob);

            let a = document.createElement('a');

            a.href = url;
            a.download = '직원리스트.xlsx';

            document.body.appendChild(a);  // Append the link to the body

            a.click();  // Simulate click on link to download file

            document.body.removeChild(a);  // Remove link from body

            window.URL.revokeObjectURL(url);  // Clean up by revoking the object URL
        })
        .catch(error => console.error('Error:', error));
}
