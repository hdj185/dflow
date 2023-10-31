let docNo = null;

function pageClick(e){
    console.log(e)
    let target = e.target;
    let pageInput = document.getElementById("page");

    const num = target.getAttribute("data-num");
    const form = document.getElementById('docAprvForm');

    if (!pageInput) {
        pageInput = document.createElement("input");
        pageInput.type = "hidden";
        pageInput.id = "page";
        pageInput.name = "page";
        form.appendChild(pageInput);
    }

    pageInput.value = num;
    form.submit();
}

function docClick(element){



    let docAprvNo = element.dataset.docAprvNo;
    let url ='/aprv/aprvReg/temp/'+docAprvNo

    location.href = url;
}


//form submit하기
function submitForm(selectElement) {
    console.log(selectElement)


    var form = document.getElementById('form');
    console.log(form);

    if (form) {
        form.submit();
    } else {
        console.error('Cannot find the form to submit');
    }
}



/**
 *  23-9-1
 임시 문서 삭제
 **/
function tempDeleteBtn(element) {

    let docArpvNo = element.dataset.no;
    let url = "/aprv/aprvTempMain/" + docArpvNo;

    console.log(url)
    confirm_pop("임시 저장된 문서를 삭제하시겠습니까?", function () {

        $.ajax({
            url: url,
            type: "delete",

            beforeSend: function (xhr) {
                /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
                let token = $("meta[name='_csrf']").attr("content");
                let header = $("meta[name='_csrf_header']").attr("content");
                xhr.setRequestHeader(header, token);
            },

            success: function () {
                console.log("성공");
                alert_pop2(1, "삭제되었습니다.", function () {
                    location.reload();
                });
            },

            error: function (err) {
                alert_pop(1, "실패");
            }
        });
    });
}
