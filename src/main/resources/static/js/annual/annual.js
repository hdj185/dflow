

function handleEditInputChange() {
    let totalValue = $("#anu_total_input").val();
    let usedValue = $("#anu_used_input").val();

    if(!isNumericValue(totalValue) || !isNumericValue(usedValue)) {
        alert_pop(1, "숫자를 입력해주세요.");
        $("#anu_total_input").val(previousTotalValue);
        $("#anu_used_input").val(previousUsedValue);
    } else {
        previousTotalValue = totalValue;
        previousUsedValue = usedValue;
        $("#anu_total_input").val(totalValue);
        $("#anu_used_input").val(usedValue);
        $("#anu_remained_span").text(Number(totalValue) - Number(usedValue));
    }
}

//숫자인지 여부
function isNumericValue(value) {
    return !isNaN(parseFloat(value)) && isFinite(value);
}

//수정 버튼 클릭 이벤트
function clickEditSaveBtn() {
    let total = Number($("#anu_total_input").val());
    let used = Number($("#anu_used_input").val());
    let remained = Number($("#anu_remained_span").text());
    let msg = "총 연차: " + total + ", 사용 연차: " + used + ", 남은 연차: " + remained + "로 수정하시겠습니까?";
    confirm_pop(msg, function () {
        let data = {
            totalAnnual: total,
            remainedAnnual: remained
        };
        sendAjax(data);
    });
}

//ajax 요청
function sendAjax(data) {
    $.ajax({
        beforeSend: function (xhr) {
            /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
            let token = $("meta[name='_csrf']").attr("content");
            let header = $("meta[name='_csrf_header']").attr("content");
            xhr.setRequestHeader(header, token);
        },
        url: "/rollbook/udtAnnual",
        type: "PUT",
        data: JSON.stringify(data),
        contentType: "application/json",
        success: function(response) {
            alert_pop2(1, response, function () {
                location.reload();
            });
        },
        error: function(xhr, status, error) {
            console.error("Error:", error);
            alert_pop(1, error);
        }
    });
}