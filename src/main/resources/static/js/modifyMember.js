//// 비밀번호 재설정 ///////
// 비밀번호 확인
$('#checkPwd').click(function () {
    const checkPassword = $('#modify_member_pw').val();
    if (!checkPassword || checkPassword.trim() === "") {
        alert("비밀번호를 입력하세요.");
    } else {
        $.ajax({
            type: 'GET',
            url: '/auth/checkPw',
            data: {'checkPassword': checkPassword},
            datatype: "text"
        }).done(function (response) {
            console.log(response);
            if (response) {
                alert(response);
                console.log("비밀번호 일치");
                modal_onoff(9,1);
            } else if (!response) {
                console.log("비밀번호 틀림");
                // 비밀번호가 일치하지 않으면
                alert("비밀번호가 맞지 않습니다.");
                window.location.reload();
            }
        }).fail(function (error) {
            alert(error.responseText);
        })
    }
});

// 비밀번호 수정
function pwUpdate() {

    const password = document.getElementById('change-password').value.trim();
    const confPassword = document.getElementById('conf-password').value.trim();

    if (password !== confPassword) {
        alert("비밀번호가 일치하지 않습니다.");
    } else {
        let data = {
            memberPw : password,
            confirmPw : confPassword
        };
        sendModifyPw(data);
    }

}

function sendModifyPw(data) {
    const confirmCheck = confirm("변경하시겠습니까?");

    if (confirmCheck == true) {
        $.ajax({
            type: 'PUT',
            url: '/auth/changePw',
            contentType: 'application/json; charset=utf-8',
            data: JSON.stringify(data),
            beforeSend: function(xhr) {
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            }
        }).done(function (result) {
            if (result) {
                alert("비밀번호가 변경되었습니다. 다시 로그인해 주세요.");
                window.location.href = "/auth/logout";
            } else {
                alert("비밀번호 변경에 실패하였습니다.");
            }
        }).fail(function (xhr, status, error) {
            var errorMessage = xhr.responseText;
            alert(errorMessage || "에러가 발생했습니다.");
        });
    }
}


//////////////////////////////모달 창 세팅//////////////////////////////
function modifyMemberBtn() {

    // let data = { msg : '회원 정보 요청' };
    let url = "/auth/modify";

    $.ajax({
        type: "get",
        url: url,
        success: function (response) {
            console.log(response.message);
            set_modal(response.member, response.deptList, response.stfList);
            modal_onoff(2, 1);
        },
        error: function (error) {
            alert(error.message);
            console.error("post 실패");
        }
    });
}

// 모달 창에 정보 전달
function set_modal(member, deptList, stfList) {
    resetAllOptions();
    setElement("modify_member_id", member.memberId);
    // setElement("modify_member_pw", member.memberPw);
    setElement("modify_member_name_kr", member.memberNameKr);
    setElement("modify_member_name_cn", member.memberNameCn);
    setElement("modify_member_name_en", member.memberNameEn);
    setElement("modify_member_birthdate", member.memberBirthdate);
    setElement("modify_member_phone", member.memberPhone);
    setElement("modify_member_email", member.memberEmail);
    setElement("modify_member_address", member.memberAddress);
    setElement("modify_member_enable_date", member.memberEnableDate);
    setElement("modify_member_leave_date", member.memberLeaveDate);
    setGender("modify_member_gender", member.memberGender);
    addDeptOption("department", deptList, member.departmentNo);
    addStaffOption("staff", stfList, member.staffNo);
    document.getElementById("modify_photo_preview").src = member.memberImgNo === null ? "" : "/files/images/" + member.memberImgNo;
    //드롭다운 클리어
    crear_select();
}

//옵션 리셋
function resetAllOptions() {
    // removeOption('modify_member_gender');
    removeOption('department');
    removeOption('staff');
    removeAllListItems();
}

//실제 옵션 모두 삭제
function removeOption(id) {
    let selectElement = document.getElementById(id);
    while (selectElement.firstChild) {
        selectElement.removeChild(selectElement.firstChild);
    }
}

//디자인으로 적용된 select option 모두 삭제
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

//성별 지정
function setGender(id, value) {
    var selectElement = document.getElementById(id);

    if (selectElement) {
        for (var i = 0; i < selectElement.options.length; i++) {
            if (selectElement.options[i].value === value) {
                selectElement.options[i].selected = true;
                break;
            }
        }
    }
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

//id 값에 value 지정
function setElement(id, value) {
    document.getElementById(id).value = value;
}

///////////////////////////사진 첨부 관련///////////////////////////
function modify_attachPhoto() {
    const photoInput = document.getElementById("modify_photo_input");
    photoInput.click(); // 숨겨진 파일 입력 요소 클릭

    photoInput.addEventListener("change", function () {
        const photoPreview = document.getElementById("modify_photo_preview");
        const selectedFile = photoInput.files[0];

        if (selectedFile) {
            const objectUrl = URL.createObjectURL(selectedFile);
            photoPreview.src = objectUrl;
        }
    });
}
///////////////////////////submit 관련///////////////////////////
function modifySubmitBtn() {
    let form = document.getElementById("modifyMemberForm");
    let data = new FormData(form);
    let url = "/auth/modify";

    var token = $("meta[name='_csrf']").attr("content");
    var header = $("meta[name='_csrf_header']").attr("content");

    // Set up request
    var xhr = new XMLHttpRequest();
    xhr.open("POST", form.action, true);
    xhr.setRequestHeader(header, token);

    // Handle response
    xhr.onload = function () {
        if (xhr.status === 200) {
            // Handle successful response
            alert(xhr.responseText);
            modal_onoff(2, 2);
            location.reload();
        } else {
            // Handle error response
            alert(xhr.responseText || "회원 정보 수정에 실패했습니다.");
            console.error(xhr.responseText || "회원 정보 수정 실패");
        }
    };

    xhr.onerror = function () {
        // Handle error
        alert("에러가 발생했습니다.");
    };

    // Send the request
    xhr.send(data);
}
// function modifySubmitBtn() {
//     let data = {
//         memberImg: document.getElementById('modify_photo_input').value,
//         memberId: document.getElementById('modify_member_id').value.trim(),
//         // memberPw: document.getElementById('modify_member_pw').value.trim(),
//         memberNameKr: document.getElementById('modify_member_name_kr').value.trim(),
//         memberBirthdate: document.getElementById('modify_member_birthdate').value.trim(),
//         memberNameCn: document.getElementById('modify_member_name_cn').value.trim(),
//         memberPhone: document.getElementById('modify_member_phone').value.trim(),
//         memberNameEn: document.getElementById('modify_member_name_en').value.trim(),
//         memberEmail: document.getElementById('modify_member_email').value.trim(),
//         memberAddress: document.getElementById('modify_member_address').value.trim(),
//         departmentNo: document.getElementById('department').value.trim(),
//         staffNo: document.getElementById('staff').value.trim(),
//         memberEnableDate: document.getElementById('modify_member_enable_date').value.trim(),
//         memberLeaveDate: document.getElementById('modify_member_leave_date').value.trim(),
//         memberGender: document.getElementById('modify_member_gender').value.trim()
//     };
//     let url = "/auth/modify";
//
//     $.ajax({
//         type: "post",
//         url: url,
//         data: JSON.stringify(data),
//         contentType: "application/json",
//         beforeSend : function(xhr) {   /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
//             let token = $("meta[name='_csrf']").attr("content");
//             let header = $("meta[name='_csrf_header']").attr("content");
//             xhr.setRequestHeader(header, token);
//         },
//         success: function (response) {
//             alert(response);
//             modal_onoff(2, 2);
//             location.reload();
//         },
//         error: function (error) {
//             alert(error);
//             console.error("post 실패");
//         }
//     });
// }


///////////////////////////기본 js function///////////////////////////
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