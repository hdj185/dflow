/**
 * file.js
 * **/

/** 파일 첨부 요소 **/
let fileBox = document.getElementById("file_box");
let uploadBox = fileBox.querySelector(".upload_box");

/** 파일 정보 list **/
let fileList = new Array();
let fileIndex = 0;

/** 삭제 파일 정보 list **/
let deleteFileList = new Array();

/** 파일첨부 버튼 클릭 **/
document.getElementById('fileBtn').addEventListener('click', function () {
    document.getElementById('files').click();
});

/** input 태그를 참조 **/
const fileInput = document.getElementById('files');

/** input 태그에서 'change' 이벤트가 발생했을 때 실행될 함수를 정의 **/
fileInput.addEventListener('change', function (event) {

    removePtag();

    appendFile(event.target.files);
    // event.target.files에는 input에서 선택된 파일들이 FileList의 형태로 저장
    for (let i = 0; i < event.target.files.length; i++) {
        fileList.push(event.target.files[i]);
    }
});

/* 박스 안에 Drag 들어왔을 때 */
uploadBox.addEventListener('dragenter', function(e) {
    console.log('dragenter');
});

/* 박스 안에 Drag를 하고 있을 때 */
uploadBox.addEventListener('dragover', function(e) {
    e.preventDefault();

    let vaild = e.dataTransfer.types.indexOf('Files') >= 0;

    if(!vaild){
        this.style.backgroundColor = 'red';
    }
    else{
        this.style.backgroundColor = '#f1f1f1';
    }
});

/* 박스 밖으로 Drag가 나갈 때 */
uploadBox.addEventListener('dragleave', function(e) {
    console.log('dragleave');

    this.style.backgroundColor = 'white';
});


/** 박스 안에서 Drag를 Drop했을 때 **/
uploadBox.addEventListener('drop', function(e) {
    e.preventDefault();

    console.log('drop');
    this.style.backgroundColor = 'white';
    removePtag();

    console.dir(e.dataTransfer);

    let files = e.dataTransfer.files;

    for (let i = 0; i < files.length; i++) {
        let file = files[i];
    }
    appendFile(files);

    for (let i = 0; i < files.length; i++) {
        fileList.push(files[i]);
    }

});


/** 첨부 등록 파일 삭제 버튼 **/
function fileDelete(event, fileIndex) {

    fileList[fileIndex] = "x";

    // 자기 자신을 없애기
    let fileLi = event.target.closest('li');
    fileLi.remove();

}
/** 기존 첨부 파일 삭제 버튼 **/
function removeFile(event, fileNo){
    console.log(fileNo);

    // 자기 자신을 없애기
    let fileLi = event.target.closest('li');
    fileLi.remove();

    deleteFileList.push(fileNo);
    console.log(deleteFileList);

}



/** ptag(파일을 마우스로 끌어 오세요) 삭제 **/
function removePtag() {

    const attachBox = document.querySelector('.attach_box');
    if(attachBox != null){
        attachBox.style.display = 'none'
    }

}



/** upload_box에 첨부 파일에 추가 **/
function appendFile(files){

    for (let i = 0; i < files.length; i++) {

        let ul_box = fileBox.querySelector(".ul_box");

        let file = files[i];
        /*formData.append(file.name, file);*/

        let  fileLi = document.createElement('li');
        fileLi.classList.add('fileLi');

        let fileNameSpan = document.createElement('span');
        fileNameSpan.classList.add('file_tit');
        fileNameSpan.textContent = file.name ;

        let fileBtn = document.createElement('button');
        fileBtn.classList.add('btn_del');
        fileBtn.setAttribute('type','button');
        fileBtn.setAttribute('title','삭제하기');
        fileBtn.setAttribute('onclick','fileDelete(event,'+fileIndex+')');
        fileBtn.style.marginRight = '5px';
        fileBtn.style.float = 'left';

        let btnSpan = document.createElement('span');
        btnSpan.classList.add('material-symbols-outlined');
        btnSpan.textContent = 'close';

        let fileSizeSpan = document.createElement('span');
        fileSizeSpan.classList.add('file_byte');
        fileSizeSpan.textContent = file.size+' byte';

        fileBtn.appendChild(btnSpan);
        fileLi.appendChild(fileBtn);
        fileLi.appendChild(fileNameSpan);
        fileLi.appendChild(fileSizeSpan);
        ul_box.appendChild(fileLi);

        fileIndex++;
    }
}





