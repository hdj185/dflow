
let docTypeNo = 0;  //양식 번호. 0이면 신규 생성
//양식 필드 id, 명칭, 필수여부
let typeInfo = [
    { id: 'folderNo', name: '양식폴더', required: true },
    { id: 'docFormCode', name: '양식코드', required: true },
    { id: 'docFormName', name: '양식', required: true },
    { id: 'docFormUseFlag', name: '표시여부', required: false }
];

//양식저장 버튼 클릭
function saveDocType(contents) {
    let data = validateDocType();
    if(data) {  //data에 값이 있고 문서 양식 수락하면 true, 그외에는 false
        confirm_pop("문서 양식을 저장하시겠습니까?", function () {
            data['contents'] = contents;
            console.log(data);
            formAjax(data, "/admin/aprv/insDocTypeMain");
        });
    }
}

function udtDocType(contents){
    let data = validateDocType();
    data.attachFileNo = attachFileNo;
    data.docFormNo = docFormNo;
    console.log(data);
    if(data) {  //data에 값이 있고 문서 양식 수락하면 true, 그외에는 false
        confirm_pop("문서 양식을 저장하시겠습니까?", function () {
            data['contents'] = contents;
            console.log(data);
            formAjax(data, "/admin/aprv/udtDocTypeMain");
        });
    }
}

//유효성 검사
function validateDocType() {
    let data = {};
    for(let i = 0; i < typeInfo.length; i++) {
        let value = document.getElementById(typeInfo[i].id).value;

        //필수요소인데 값이 없을 경우, 알림창
        if(typeInfo[i].required && (value === '' || value === ' ' || !value)) {
            alert_pop(1, '[' + typeInfo[i].name + '] 칸이 비었습니다. 값을 입력해주세요.');
            return false;
        } else { //값이 있거나 필수요소가 아닐 경우, data에 넣기
            data[typeInfo[i].id] = value;
        }
    }
    return data;
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
        // enctype : 'multipart/form-data',
        // processData: false,
        // contentType: false,
        // data: data,
        contentType: 'application/json', // 데이터 형식 설정
        data: JSON.stringify(data), // JSON 형식으로 변환
        type: "POST",
        success: function (result) {
            console.log("통신 성공");
            alert_pop2(1, "저장하였습니다.", function () {
                location.replace("/admin/aprv/aprvType");
            });
        },
        error: function (xhr, status, error) {
            console.log("=====통신 실패 ====");
            alert_pop(1, error);
        },
    });
}