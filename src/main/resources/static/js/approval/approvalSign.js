///////////////////////////사진 첨부 관련///////////////////////////
//이미지 첨부

function attachImg() {
    const imgInput = document.getElementById("img_input");
    const saveSignBtn = document.getElementById('saveSign_submit');
    const editSignBtn = document.getElementById('editSignBtn');
    const removeSignBtn = document.getElementById('removeSignBtn');

    imgInput.click(); // 숨겨진 파일 입력 요소 클릭

    imgInput.addEventListener("change", function () {
        const imgPreview = document.getElementById("img_preview");
        const editPreview = document.getElementById("edit_preview");
        const selectedFile = imgInput.files[0];
        // console.log("이미지 첨부 시 파일 개수:", imgInput.files.length);
        if (selectedFile) {
            const objectUrl = URL.createObjectURL(selectedFile);
            editPreview.src = objectUrl;
            editPreview.style.display = "";
            editPreview.style.width = "100%";
            editPreview.style.height = "auto";

            imgPreview.src = objectUrl;
            imgPreview.style.display = "";
            saveSignBtn.style.display = "";
            editSignBtn.style.display = "";
            removeSignBtn.style.display = "";
            document.getElementById('modifySignBtnGroup').style.display = "";
        }
    });
}

// 이미지 삭제 버튼
function removeImg() {
    const imgInput = document.getElementById("img_input");
    confirm_pop("등록된 서명을 삭제하시겠습니까?", function () {
        imgInput.value = "";
        // console.log("이미지 첨부 시 파일 개수:", imgInput.files.length);

        deleteSign();

        // // 서버로 삭제 요청 보내기
        // fetch("/aprv/delSignMain", {
        //     method: "DELETE",
        //     headers: {
        //         "Content-Type": "multipart/form-data"
        //     },
        //     body: new FormData(),
        // })
        //     .then(response => response.json())
        //     .then(data => {
        //         console.log("서명 삭제 결과:", data);
        //         // 서명이 성공적으로 삭제되었을 때 클라이언트에서 수행할 작업 추가
        //     })
        //     .catch(error => {
        //         console.error("서명 삭제 에러:", error);
        //         // 오류 처리 작업 추가
        //     });
        // }
    });
}

// 이미지 삭제 ajax
function deleteSign() {

    const imgElement = document.getElementById("img_preview");
    const editSignBtn = document.getElementById('editSignBtn');
    const removeSignBtn = document.getElementById('removeSignBtn');

    let token = $("meta[name='_csrf']").attr("content");
    let header = $("meta[name='_csrf_header']").attr("content");

    fetch("/aprv/delSignMain", {
        method: "DELETE",
        headers: {
            [header]: token
        }
    }).then(response => {
        if (response.ok) {
            alert_pop2(1, "서명 삭제 완료하였습니다.", function () {
                imgElement.src = "";
                imgElement.style.display = "none";
                window.location.href = "/aprv/aprvSign";
            });
        } else {
            alert_pop(1, "서명 삭제에 실패했습니다.");
        }
    }).catch(error => {
        console.error("Error:", error);
    });
}

// 서명 등록 버튼
function saveSignBtn() {

    confirm_pop("서명을 등록하시겠습니까?", function () {
        saveSign();
    });
}

// 서명 등록 ajax
function saveSign() {
    let form = document.getElementById("saveSignForm");
    let data = new FormData(form);
    let url = "/aprv/insSignMain";
    const editSignBtn = document.getElementById('editSignBtn');
    const removeSignBtn = document.getElementById('removeSignBtn');


    // console.log()

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
            alert_pop2(1, xhr.responseText, function (){
                location.reload();
            });
        } else {
            // Handle error response
            alert_pop(1, xhr.responseText || "서명 등록에 실패했습니다.");
            console.error(xhr.responseText || "서명 등록 실패");
        }
    };

    xhr.onerror = function () {
        // Handle error
        alert_pop(1, "에러가 발생했습니다.");
    };

    // Send the request
    xhr.send(data);
}