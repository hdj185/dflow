/**
 * adminOrg.js 아리가또
 * **/

/*
let adminSelMemberUrl = urlList.adminSelMemberUrl;  // 전체 직원 조회
let adminUdtMemberUrl = urlList.adminUdtMemberUrl; // 조직도 부서별 직원 조회
let upDeptUrl = urlList.upDeptUrl; // 상위 부서 직원 목록 조회
*/



let saveDepartment = null;
let staffMemberNo = null;


function openUpdateDepartmentModal(departmentNo) {
    saveDepartment = departmentNo ;
    let url = "/admin/org/udtDepart/" + departmentNo;
    console.log(url.toString());

// 부서 정보 가져오기 및 모달 폼에 채우기
    $.ajax({
        type: "GET",
        url: url,
        dataType : "json",



        success: function (response) {
            console.log(response)

            document.getElementById("departmentName").value = response.departmentName;


            // 가져온 부서 정보를 모달 폼에 채우는 로직 추가
            let departmentName = document.getElementById("departmentName").value = response.departmentName;
            let departmentParentNo = document.getElementById("departmentParent-id").value = response.departmentParentNo;
            let departmentDepth = document.getElementById("departmentDepth").value = response.departmentDepth;
            console.log(departmentParentNo, departmentName, departmentDepth);

        },
        error: function (error) {
            console.error("부서 정보 가져오기 실패:", error);
        }
    });
}


// 부서 정보 수정 요청 보내기
function submitUpdateDepartment() {
    const departmentNo = saveDepartment;
    const departmentName = document.getElementById("departmentName").value;
    let testParent = document.getElementById("departmentParentNo").value || null; // 이거 이게 셀렉트로 고른 no을 가져옴


    const formData = {
        departmentName: document.getElementById("departmentName").value,
        departmentDepth: document.getElementById("departmentDepth").value,
        departmentParentNo: testParent,
        departmentFlag: document.getElementById("on_OffCheck").value === "false"
    };

    console.log(formData)

    let url = "/admin/org/udtDepartPost/" + departmentNo;
    console.log(url.toString());

    // 수정 요청 보내기
    confirm_pop('부서를 수정 하시겠습니까?', function () {
        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify(formData),
            dataType: "json",
            contentType: "application/json;charset='UTF-8'",

            beforeSend: function (xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },

            success: function (response) {
                // alert_pop(1, "부서가 수정 되었습니다.");
                alert_popOrg(1, response.result); // 서버로부터 받은 메시지 출력

            },
            error: function (request, status, error) {
                console.error("부서 수정 실패:")
                alert_popOrg(1, "code:" + request.status + "\n" + "message:" + request.responseText + "\n" + "error:" + error);


                err.toString()
            }
        });
    });
}

// 23-8-18 임직원 부서 조회 카드 활용
function adminMemberList(departmentNo){
    console.log(departmentNo)

}


//23 - 8 - 13
// 부서 추가 요청
function submitCreateDepartment() {
    // let testParent = document.getElementById("createDepartmentParentNo").value || null; // 이거 이게 셀렉트로 고른 no을 가져옴
    // departmentName =  document.getElementById("createDepartmentName").value;
    let departmentName = $("#createDepartmentName").val();
    let departmentParentNo = $("#createDepartmentParentNo").val();
    let departmentDepth = $("#departmentDepth").val();
    let departmentCreateMemberName = $("#createMemberName").val();
    let memberNo = $("#memberNo").val();


    let url = "/admin/org/selDepart";
    console.log(url.toString());

    // 추가 요청 보내기
    confirm_pop('부서를 추가 하시겠습니까?', function () {
        $.ajax({
            type: "POST",
            url: url,
            data: JSON.stringify({
                departmentName: departmentName,
                departmentParentNo: departmentParentNo,
                departmentDepth: departmentDepth,
                departmentFlag: document.getElementById("create_On_OffCheck").value === "false"
                // memberNameKr : departmentCreateMemberName,
                // memberNo : memberNo
            }),
            dataType: "json",
            contentType: "application/json;charset='UTF-8'",

            beforeSend: function (xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },

            success: function (response) {
                alert_pop2(1, "부서가 성공적으로 추가되었습니다!", function () {
                    location.replace(location.href);
                });


            },
            error: function (request, status, errorThrow) {
                console.error("부서 수정 실패:")
                alert_pop(1, "부서명을 입력 하십시오.")


                err.toString()
            }
        });
    });
}

// 부서 삭제
function deleteDepartment(obj){
    let departmentNo = obj.dataset.id;
    let url ="/admin/org/delDepart/" + departmentNo;
    let departmentName;



    $.ajax({
        url: url,
        type: "DELETE",
        cache: false,

        beforeSend: function (xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success: function (err) {
            alert_confirmOrg(1, "부서가 삭제되었습니다.", function () {
                location.replace(location.href);
            });



        },

        error: function (request, status, errorThrow) {
            alert_pop(1, "부서 삭제 실패. \t 관리자에게 문의 하세요." )



        }
    })
}

/** 23-8-23
 *  딜리트 확인 로직
 * **/
function adminDeleteDepart(obj) {

    let departmentName = obj.parentElement.previousElementSibling.previousElementSibling.previousElementSibling.previousElementSibling.textContent.trim();





    confirm_pop("정말로 삭제하시겠습니까?", function () {

        deleteDepartment(obj)

    });
}




/** depth1 클릭시 하위부서 드롭 **/
function depth1Click(element) {

    console.log(element)



    // depth1 요소를 선택합니다.
    let depth1 = element.querySelector('.depth2');
    let depth2 = element.querySelectorAll('.depth3');

    depth2.forEach(function(depth3) {

        if(depth3.style.display === 'block'){
            depth3.style.display = 'none';
        }else {
            depth3.style.display = 'block';
        }
    });


}




/*  $.ajax({
    type : "GET",
    url : "
    contentType : "application/json",
    data : JSON.stringify({}),
    success : function (response){
      // 성공
      console.log("업데이트 성공?", response);
    },
    // 실패
    error : function (error){
    console.error("실패", error)
    }*/
// })
//부서 옵션 지정



/** 23-8-17  **/
/** 23-8-20  수정
 * 멤버 수정  값 받아오기
 * **/

function adminMemberModifyForm(memberNo) {
    staffMemberNo = memberNo;

    // 부서 고유 번호 - 해당 값이 option으로 존재하는지 확인하고, 있다면 선택합니다.
    let departmentSelect = $("#adminDepartmentSelect");  // 여기에서 변수를 선언하고 초기화합니다.
    let staffSelect = $("#adminStaff");

    let url = "/admin/org/udtMember/" + memberNo;
    console.log(url.toString());


// 임직원 정보 가져오기 및 모달 폼에 채우기
    $.ajax({
        type: "GET",
        url: url,
        success: function (response) {
            const memberId = response.memberId;
            const memberNameKr = response.memberNameKr;
            const departmentNo = response.departmentNo;
            const staffNo = response.staffNo;
            const memberEnableDate = response.memberEnableDate;

            console.log(response)


            // 가져온 부서 정보를 모달 폼에 채우는 로직 추가
            document.getElementById("admin_modify_memberNameKr").value = memberNameKr;      // 멤버 한글 이름
            document.getElementById("adminDepartmentSelect").value = departmentNo;              // 부서 고유 번호
            document.getElementById("adminStaff").value = staffNo;                          // 직원 고유 번호
            document.getElementById("admin_enable_date").value = memberEnableDate;


            let departmentSelect = $("#adminDepartmentSelect");
            if (departmentSelect.find("option[value='" + departmentNo + "']").length > 0) {
                // 부서 고유 번호 - 해당 값이 option으로 존재하고, 있다면 선택합니다.
                departmentSelect.val(departmentNo);

                // 선택된 옵션을 화면에 표시
                $('.selecionado_opcion', '#adminDepartmentParent-id').text(departmentSelect.find('option:selected').text());

            } else {
                console.error("부서번호가 옵션 리스트에 없습니다:", departmentNo);
            }


            let staffSelect = $("#adminStaff");
            if(staffSelect.find("option[value='" + staffNo + "']").length > 0) {
                // 직책 고유 번호 - 해당 값이 option으로 존재하고, 있다면 선택합니다.
                staffSelect.val(staffNo);

                // 선택된 옵션을 화면에 표시
                $('.selecionado_opcion', '#adminStaffId').text(staffSelect.find('option:selected').text());

            } else{
                console.error("직책번호가 옵션 리스트에 없습니다:", staffNo);
            }


            $("#adminDepartmentSelect").change(function() {
                let selectedOptionText = $(this).find('option:selected').text();
                $('.selecionado_opcion', '#adminDepartmentParent-id').text(selectedOptionText);
            });


            $("#adminStaff").change(function() {
                let selectedOptionText = $(this).find('option:selected').text();
                $('.selecionado_opcion', '#adminStaffId').text(selectedOptionText);
            });






            if (response.memberFlag === '퇴사직원') {
                document.getElementById('admin_modify_leaveDate').value = response.memberLeaveDate;
            } else {  // '퇴사직원'이 아니라면 해당 필드들을 초기화합니다.
                document.getElementById('admin_modify_leaveDate').value ='';



            }
        },
        error: function (error) {
            console.error("멤버 정보 가져오기 실패:", error);
        }
    });
}



/** 임직원 수정 값 채우기 및 수정 처리 ---- 23-8-21**/

function adminMemberModify(){
    // Form 데이터를 FormData 객체로 변환

    let memberNo = staffMemberNo;
    let url = "/admin/org/udtMember/" + memberNo;
    const selectedDepartmentName = $('#adminDepartmentSelect option:selected').text();
    const selectedStaffName = $('#adminStaff option:selected').text();


    const formData = {
        departmentNo: document.getElementById("adminDepartmentSelect").value,
        staffNo: document.getElementById("adminStaff").value,
        memberNameKr: document.getElementById("admin_modify_memberNameKr").value,
        departmentName: selectedDepartmentName,
        staffName: selectedStaffName,
        memberLeaveDate: document.getElementById("admin_modify_leaveDate").value,

        memberEnableDate: document.getElementById("admin_enable_date").value
    };
    console.log(formData.memberLeaveDate)

    console.log(formData);

    $.ajax({
        url : url,
        type: "POST",
        data: JSON.stringify(formData),
        dataType : "JSON",
        contentType: "application/json;charset='UTF-8'",


        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success :  function (response){
            console.log(response);
            location.replace(location.href);
        },


        error : function (err){
            err.toString();
        }



    })
}

//부서 옵션 지정
function addDeptOption(id, list, value) {
    let selectElement = document.getElementById(id); // 지정된 ID를 가진 <select> 요소 가져오기

    if (selectElement) {
        for (let i = 0; i < list.length; i++) {
            let option = document.createElement("option"); // 새로운 <option> 요소 생성
            option.value = list[i].departmentNo; // 옵션의 값 설정
            option.text = list[i].departmentName; // 옵션의 텍스트 설정
            option.selected = (list[i].departmentNo === value);
            selectElement.appendChild(option); // <select> 요소에 옵션 추가
        }
    } else {
        console.log("Select element with ID '" + id + "' not found.");
    }
}

//직책 옵션 지정
function addStaffOption(id, list, value) {
    let selectElement = document.getElementById(id); // 지정된 ID를 가진 <select> 요소 가져오기

    if (selectElement) {
        for (let i = 0; i < list.length; i++) {
            let option = document.createElement("option"); // 새로운 <option> 요소 생성
            option.value = list[i].staffNo; // 옵션의 값 설정
            option.text = list[i].staffName; // 옵션의 텍스트 설정
            option.selected = (list[i].staffNo === value);
            selectElement.appendChild(option); // <select> 요소에 옵션 추가
        }
    } else {
        console.log("Select element with ID '" + id + "' not found.");
    }
}










/**
 * 23-9-15 로직 수정
 * **/
// 부서 정보 가져와서 채우기
function openUpdateDepartmentModal(departmentNo) {
    saveDepartment = departmentNo ;
    let url = "/admin/org/udtDepart/" + departmentNo;


// 부서 정보 가져오기 및 모달 폼에 채우기
    $.ajax({
        type: "GET",
        url: url,
        dataType : "json",

        success: function (response) {
            document.getElementById("departmentName").value = response.departmentName;
            // 상위 부서 셀렉트에 기존 선택된 부서 표시

            // 가져온 부서 정보를 모달 폼에 채우는 로직 추가
            let departmentName = document.getElementById("departmentName").value = response.departmentName;
            let departmentParentNo = document.getElementById("departmentParent-id").value = response.departmentParentNo;
            let departmentDepth = document.getElementById("departmentDepth").value = response.departmentDepth;



            let departmentUpDepth = $("#departmentParentNo");

            console.log(departmentUpDepth);
            if (departmentUpDepth.find("option[value='" + departmentParentNo + "']").length > 0) {
                // 부서 고유 번호 - 해당 값이 option으로 존재하고, 있다면 선택합니다.
                departmentUpDepth.val(departmentParentNo);

                // 선택된 옵션을 화면에 표시
                $('.selecionado_opcion', '#departmentParent-id').text(departmentUpDepth.find('option:selected').text());
                console.log(departmentUpDepth.find('option:selected').text());

                $("#departmentParentNo").change(function() {
                    let selectedOptionText = $(this).find('option:selected').text();
                    $('.selecionado_opcion', '#departmentParentNo').text(selectedOptionText);
                });


            } else {
                console.error("상위 부서가 없습니다:", departmentParentNo);
                departmentUpDepth.val(''); // 상위부서 선택을 '선택 안 함'으로 설정
                $('.selecionado_opcion', '#departmentParent-id').text('선택안함'); // 화면에 표시되는 텍스트도 '선택 안 함'으로 변경



            }

        },
        error: function (error) {
            console.error("부서 정보 가져오기 실패:", error);
        }
    });
}


// 부서 정보 수정 요청 보내기
function submitUpdateDepartment() {
    const departmentNo = saveDepartment;
    const departmentName =  document.getElementById("departmentName").value;
    let testParent = document.getElementById("departmentParentNo").value || null; // 이거 이게 셀렉트로 고른 no을 가져옴


    const formData = {
        departmentName: document.getElementById("departmentName").value,
        departmentDepth: document.getElementById("departmentDepth").value,
        departmentParentNo: testParent,
        departmentFlag: document.getElementById("on_OffCheck").value === "false"
    };

    console.log(formData)

    let url = "/admin/org/udtDepartPost/" + departmentNo;
    console.log(url.toString());

    // 수정 요청 보내기
    $.ajax({
        type: "POST",
        url: url,
        data: JSON.stringify(formData),
        dataType : "json",
        contentType: "application/json;charset='UTF-8'",

        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success: function (response) {
            alert_pop2(1, "부서가 성공적으로 수정되었습니다!",function () {
                location.replace(location.href);
                let modalSet = document.getElementById('alert_btn');
                modalSet.classList.add('modal-disabled');

            });

        },
        error: function (request, status, error) {
            console.error("부서 수정 실패:")
            alert_pop(1, "code:"+request.status+"\n"+"message:"+request.responseText+"\n"+"error:"+error);



            err.toString()
        }
    });
}


// 이거 첫 화면에서 계층 클릭 했을 때 나오는 애들을 갈아끼는
function memberList(departmentNo){

    let url = "/admin/org/selMember?departmentNo=" + departmentNo;

    console.log(url);
    console.log(departmentNo);


    $.ajax({
        url : url,
        type : "POST",
        dataType:'html',
        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success: function(data){
            let adminMemberTbody = $('#adminMember-tbody') // 계속 갈아 껴지는 놈
            adminMemberTbody.children().remove();
            adminMemberTbody.append(data);
        }
    })
}

// DEPS 클릭했을 때 전체 조회
function adminMemberAllList(){

    let url = "/admin/org/selMember";
    console.log(url);

    $.ajax({
        url: url,
        type: "POST",
        dataType: 'html',

        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success:function (data){
            let adminMemberTbody = $('#adminMember-tbody') // 계속 갈아 껴지는 놈
            adminMemberTbody.children().remove();
            adminMemberTbody.append(data);
        }
    })
}

/** 23-8-22
 *  1계층 - 부서 조회
 * **/

function adminFirstList(departmentNo){

    let url = "/admin/org/selMember?departmentNo=" + departmentNo;

    console.log(url);

    $.ajax({
        url: url,
        type: "POST",
        detaType: "html",

        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success:function (data){
            let adminMemberTbody = $('#adminMember-tbody') // 계속 갈아 껴지는 놈
            adminMemberTbody.children().remove();
            adminMemberTbody.append(data);
        }
    })
}


/**
 *  우선순위 큐
 *  23-9-15 우선순위 위로 수정
 *
 * **/
function queueDepartmentUp(departmentNo) {
    let url = '/admin/org/upDepartmentOrder/' + departmentNo;

    $.ajax({
        url: url,
        type: "PUT",
        dataType: "json",

        beforeSend: function (xhr) {
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success: function (response) {
            console.log(response.result)

            if (response.result === "SUCCESS") {

                location.replace(location.href);

            } else {
                alert_pop(1, response.result); // 서버로부터 받은 메시지 출력
            }
        },

        error: function (err) {
            alert_pop(1, err.responseText);  // 응답 텍스트만 출력
        }
    })
}




/**
 *  23-9-15 우선순위 아래로 수정
 *
 * **/
function queueDepartmentDown(departmentNo){

    console.log(departmentNo);

    let url = "/admin/org/downDepartmentOrder/" + departmentNo;

    console.log(url);

    // 수정 요청 보내기
    $.ajax({
        type: "PUT",
        url: url,
        dataType : "json",

        beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },

        success: function (response) {
            console.log(response)
            if (response.result === 'SUCCESS') {
                location.replace(location.href);
            } else {
                alert_pop(1, response.result); // 서버로부터 받은 메시지 출력
            }
        },
        error: function (jqXHR, textStatus, errorThrown) {
            let response = JSON.parse(jqXHR.responseText);
            alert_pop(1, response.result); // 서버로부터 받은 메시지 출력

            err.toString()
        }
    });

}


/** 우선순위 초기화 **/

/** admin DEPS 클릭 했을 때 나오는 부분 **/

/** 직원 리스트 ajax function **/

// modal
function modal_onoff(seq, flag) {
    console.log('in');
    if (flag === 1) {
        $("#modal_" + seq).css("display", "block");
    } else if (flag === 2) {
        $("#modal_" + seq).css("display", "none");
    }
}
