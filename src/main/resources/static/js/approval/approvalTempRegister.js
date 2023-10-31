/** 삭제 파일 정보 list **/
let deleteFileList = new Array();


/* 문서 임시 저장 */
function docTempReg() {
  let formData = new FormData();
  const currentDate = new Date();
  const year = currentDate.getFullYear().toString().slice(-2); // 년도에서 마지막 두 자리만 가져오기
  const month = (currentDate.getMonth() + 1).toString().padStart(2, "0"); // 월은 0부터 시작하므로 1을 더해줌
  const day = currentDate.getDate().toString().padStart(2, "0");
  let docTitle = docFormCode + "-" + year + month + day;

  resetFileBox(); //첨부파일 리스트 초기화

  formData.append("docTTL", docTitle);
  formData.append("docFormNo", docTypeNo);
  formData.append("docCn", getDocumentContent());
  docSendTempData(formData);
}


function docSendTempData(data) {
  $.ajax({
    beforeSend: function (xhr) {
      /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
      let token = $("meta[name='_csrf']").attr("content");
      let header = $("meta[name='_csrf_header']").attr("content");
      xhr.setRequestHeader(header, token);
    },
    url: "/aprv/insTempDocAprv",
    enctype: "multipart/form-data",
    processData: false,
    contentType: false,
    data: data, // 수정된 부분
    type: "POST",
    success: function (result) {
      alert_pop2(1, result.msg, function () {
        window.location.replace("/aprv/aprvTempMain");
      });
    },
    error: function (xhr, status, error) {
      console.log("=====통신 실패 ====");
      alert_pop(1, error);
    },
  });
}

/* 문서 임시 저장 수정 */
function docTempUdt() {
  let formData = new FormData();
  const currentDate = new Date();
  const year = currentDate.getFullYear().toString().slice(-2); // 년도에서 마지막 두 자리만 가져오기
  const month = (currentDate.getMonth() + 1).toString().padStart(2, "0"); // 월은 0부터 시작하므로 1을 더해줌
  const day = currentDate.getDate().toString().padStart(2, "0");
  let docTitle = formCode + "-" + year + month + day;

  resetFileBox(); //첨부파일 리스트 초기화
  formData.append("docNo", docNo);
  formData.append("docTTL", docTitle);
  formData.append("docFormNo", docFormNo);
  formData.append("docCn", getDocumentContent());
  udtTempData(formData);
}

function udtTempData(data) {
  $.ajax({
    beforeSend: function (xhr) {
      /*데이터를 전송하기 전에 헤더에 csrf값을 설정한다*/
      let token = $("meta[name='_csrf']").attr("content");
      let header = $("meta[name='_csrf_header']").attr("content");
      xhr.setRequestHeader(header, token);
    },
    url: "/aprv/udtTempDocAprv",
    enctype: "multipart/form-data",
    processData: false,
    contentType: false,
    data: data,
    type: "PUT",
    success: function (result) {
      alert_pop2(1, result.msg, function () {
        window.location.replace("/aprv/aprvTempMain");
      });
    },
    error: function (xhr, status, error) {
      console.log("=====통신 실패 ====");
      alert_pop(1, error);
    },
  });
}


function docTempUdtBtn() {
  let approverList = getHidden("approver");
  if (approverList.length < 1) {
    alert_pop(1, "결재자를 추가해주세요.");
  } else {
    confirm_pop("문서를 등록하시겠습니까?", function () {
      console.log("approver", approverList);
      console.log("referrer", getHidden("referrer"));
      let formData = new FormData();
      const currentDate = new Date();
      const year = currentDate.getFullYear().toString().slice(-2); // 년도에서 마지막 두 자리만 가져오기
      const month = (currentDate.getMonth() + 1).toString().padStart(2, "0"); // 월은 0부터 시작하므로 1을 더해줌
      const day = currentDate.getDate().toString().padStart(2, "0");
      let docTitle = formCode + "-" + year + month + day;

      formData.append("docNo", docNo);
      formData.append("docTTL", docTitle);
      formData.append("docFormNo", docFormNo);
      formData.append("docCn", getDocumentContent());
      formData.append("approver", approverList);
      formData.append("referrer", getHidden("referrer"));
      addFiles(formData, fileList);

      docSendData(formData, "/aprv/insDocAprvTemp");
      crear_select();
    });
  }
}

function docUdtBtn() {
  let approverList = getHidden("approver");
  if (approverList.length < 1) {
    alert_pop(1, "결재자를 추가해주세요.");
  } else {
    confirm_pop("문서를 수정하시겠습니까?", function () {
      console.log("approver", approverList);
      console.log("referrer", getHidden("referrer"));
      let formData = new FormData();
      const currentDate = new Date();
      const year = currentDate.getFullYear().toString().slice(-2); // 년도에서 마지막 두 자리만 가져오기
      const month = (currentDate.getMonth() + 1).toString().padStart(2, "0"); // 월은 0부터 시작하므로 1을 더해줌
      const day = currentDate.getDate().toString().padStart(2, "0");
      let docTitle = formCode + "-" + year + month + day;

      formData.append("docNo", docNo);
      formData.append("docTTL", docTitle);
      formData.append("docFormNo", docFormNo);
      formData.append("docCn", getDocumentContent());
      formData.append("approver", approverList);
      formData.append("referrer", getHidden("referrer"));
      formData.append("delFiles", deleteFileList);
      addFiles(formData, fileList);
      docSendData(formData, "/aprv/insDocAprvUdt");
      crear_select();
    });
  }
}

function resetFileBox() {
  let uploadBox = $(".upload_box");
  uploadBox.children().remove();
}

function fileAjax(docNo) {
  console.log("나 작동");
  $.ajax({
    url: "/aprv/selFileList/" + docNo,
    dataType: "html",
    type: "GET",
    success: function (result) {
      console.log("통신 성공");
      let upload_box = $(".upload_box");
      upload_box.children().remove();
      upload_box.append(result);
    },
    error: function (xhr, status, error) {
      console.log("=====통신 실패 ====");
    },
  });
}

function removeFile(event, fileNo) {
  console.log(fileNo);

  // 자기 자신을 없애기
  let fileLi = event.target.closest("li");
  fileLi.remove();

  deleteFileList.push(fileNo);
  console.log(deleteFileList);
}
