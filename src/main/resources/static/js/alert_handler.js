function alert_pop(flag, msg) {
    console.log('alert_pop');
    if (flag === 1) {
        document.getElementById('alert_span').textContent = msg;
        document.getElementById("alert_wrap").style.display = "block";
    } else {
        document.getElementById("alert_wrap").style.display = "none";
    }
}

function alert_pop2(flag, msg, callback) {
    console.log('alert_pop2');
    let btn = document.getElementById('alert_btn');
    if (flag === 1) {
        document.getElementById('alert_span').textContent = msg;
        document.getElementById("alert_wrap").style.display = "block";
        if (typeof callback === 'function') {
            btn.addEventListener('click', function() {
                alert_pop(2, '');
                callback();
            });
        }
    } else {
        document.getElementById("alert_wrap").style.display = "none";
        if (typeof callback === 'function') {
            callback();
        }

        btn.addEventListener('click', function() {
            alert_pop(2, '');
        });
    }
}


function showConfirmPop(msg) {
    document.getElementById('confirm_span').textContent = msg;
    document.getElementById("confirm_wrap").style.display = "block";
    return new Promise(function(resolve, reject) {
        document.getElementById("confirm_btn").addEventListener("click", function() {
            document.getElementById("confirm_wrap").style.display = "none";
            resolve(true); // Promise 성공 상태로 true 반환
        });

        document.getElementById("confirm_cancel_btn").addEventListener("click", function() {
            document.getElementById("confirm_wrap").style.display = "none";
            resolve(false); // Promise 성공 상태로 false 반환
        });
    });

}

function confirm_pop(msg, callback) {
    showConfirmPop(msg).then(function (result) {
        console.log('result=', result);
        if(result === true) {
            callback();
        }
    });
}




function alert_popOrg(flag, msg, callback) {
    let btn = document.getElementById('alert_btn');
    let orgModal = document.getElementById('modal_1');
    let addDepart = document.getElementById('modal_3');
    if (flag === 1) {
        document.getElementById('alert_span').textContent = msg;
        $("#alert_wrap").css("display", "block");
        orgModal.classList.add('modal-disabled');
        addDepart.classList.add('modal-disabled');

        if (typeof callback === 'function') {
            btn.addEventListener('click', function () {
                alert_pop(2, '', callback);
            });
        }
    } else {
        $("#alert_wrap").css("display", "none");
        orgModal.classList.remove('modal-disabled');
        addDepart.classList.remove('modal-disabled');

        if (typeof callback === 'function') {
            callback();
        }

        btn.addEventListener('click', function () {
            alert_pop(2, '');
        });
    }
}


function alert_confirmOrg(flag, msg, callback) {
    console.log('alert_pop2');
    let btn = document.getElementById('alert_btn');
    if (flag === 1) {
        document.getElementById('alert_span').textContent = msg;
        document.getElementById("alert_wrap").style.display = "block";
        if (typeof callback === 'function') {
            btn.addEventListener('click', function() {
                alert_pop(2, '');
                callback();
            });
        }
    } else {
        document.getElementById("alert_wrap").style.display = "none";
        if (typeof callback === 'function') {
            callback();
        }

        btn.addEventListener('click', function() {
            alert_pop(2, '');
        });
    }
}