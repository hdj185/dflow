
//저장 버튼 클릭
function adminAnuSetBtn() {
    confirm_pop('연차 생성 기준 정보를 저장하시겠습니까?', function () {
        let url = "/admin/rollbook/udtAnnualSetting";
        let data = {
            anuSetResetDate: document.getElementById('annual_reset_date').value,
            anuSetIncrementYear: document.getElementById('annual_plus_year').value,
            anuSetIncrementCnt: document.getElementById('annual_plus').value,
            anuSetDefaultCnt: document.getElementById('annual_default').value,
            anuSetMaxCnt: document.getElementById('annual_max').value
        };
        sendAdminAnuSetAjax(url, data);
    });
}

//ajax 요청
function sendAdminAnuSetAjax(url, data) {
    $.ajax({
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        url: url,
        type: "PUT",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function(response) {
            alert_pop2(1, response.msg, function () {
                location.reload();
            });
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
            alert_pop(1, "저장에 실패하였습니다.");
        }
    });
}
