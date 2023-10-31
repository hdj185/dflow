let rollNo = 0;

//근태 기준 시간 수정 버튼
function clickRollbookSettingbtn() {
    confirm_pop("근태 기준 시간을 수정하시겠습니까?", function () {
        let url = "/admin/rollbook/udtAdminRollbookSetting";
        let data = {
            openTime: $("#setting_open_time").val(),
            closeTime: $("#setting_close_time").val(),
        };

        sendRollbookAjax(url, data);
    });
}

//근태 수정 모달 세팅
function setRollbookEditModal(rollbookNo) {
    rollNo = rollbookNo;
    getRollbookAjax(rollbookNo);
}

//ajax로 정보 요청
function getRollbookAjax(rollbookNo) {

    let url = "/admin/rollbook/selAdminEditRollbook/" + rollbookNo;
    $.ajax({
        type: "get",
        url: url,
        success: function (response) {
            console.log(response.msg);
            setRollbookEditModalInput(response.data.result);
        }, error: function (error) {
            alert_pop(1, "정보 호출에 실패하였습니다.");
        }
    });
}

//모달 input 세팅
function setRollbookEditModalInput(data) {
    removeAllListItems();
    selectOption("open", data.openState);
    selectOption("close", data.closeState);
    $("#open_time").val(data.openTime);
    $("#close_time").val(data.closeTime);
    $("#rollbook_content").text(data.content);
    crear_select();
}

//확인 버튼
function sumbitUpdateRollbook() {
    confirm_pop("근태 정보를 수정하시겠습니까?", function () {
        let url = "/admin/rollbook/udtAdminEditRollbook";
        let data = {
            rollbookNo: rollNo,
            openTime: $("#open_time").val(),
            openState: $("#open_state").val(),
            closeTime: $("#close_time").val(),
            closeState: $("#close_state").val(),
            contents: $("#rollbook_content").val()
        };

        sendRollbookAjax(url, data);
    });
}

//ajax로 정보 요청
function sendRollbookAjax(url, data) {
    $.ajax({
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        type: "PUT",
        url: url,
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function(response) {
            console.log('처리 성공!');
            alert_pop2(1, response.msg, function () {
                location.reload();
            });
        }, error: function(xhr, status, error) {
            console.error("Error:", error);
            alert_pop(1, "수정에 실패하였습니다.");
        }
    });
}

///////////////////////////////////////select 관련///////////////////////////////////////
//select 선택
function selectOption(id, value) {
    let selectElement = document.getElementById(id + '_state');
    if(selectElement) {
        for(let i = 0; i < selectElement.options.length; i++) {
            if (selectElement.options[i].value === value) {
                selectElement.options[i].selected = true;
                break;
            }
        }
    }
}
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



//근태 요청 삭제
function adminDeleteRollbook(){

    let checkboxes = document.getElementsByClassName("modify-chk");
    let rollbookNo = [];



    for(let i=0; i<checkboxes.length; i++){
        if(checkboxes[i].checked){
            rollbookNo.push(checkboxes[i].value);
            console.log(rollbookNo[i])
        }
    }


    if(rollbookNo.length === 0 ){
        alert_pop(1, "선택된 값이 없습니다")
        return;

    }

    console.log(rollbookNo)


    let url = "/admin/rollbook/selRollbook?" + rollbookNo ;


    console.log(url);

    confirm_pop('해당 문서를 삭제하시겠습니까?', function () {
        $.ajax ({
            url	: url,
            type: "POST",
            data: {
                rollbookNo : rollbookNo
            },

            beforeSend: function (xhr) {
                /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },

            success: function(result) {
                alert_pop2(1, "문서가 삭제되었습니다.", function (){
                    location.reload();
                });
            },
            error: function(error){
                alert_pop(1, "서버 ERROR 관리자에게 문의 하세요");
            }
        });
    });
}
