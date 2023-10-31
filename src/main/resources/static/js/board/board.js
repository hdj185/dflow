/**
 * board.js
 * **/

function pageClick(e){
    console.log(e)
    let target = e.target;
    let pageInput = document.getElementById("page");

    const num = target.getAttribute("data-num");
    const formSearch = document.getElementById('fileForm');

    if (!pageInput) {
        pageInput = document.createElement("input");
        pageInput.type = "hidden";
        pageInput.id = "page";
        pageInput.name = "page";
        formSearch.appendChild(pageInput);
    }

    pageInput.value = num;
    formSearch.submit();
}

// 게시글 상세조회 페이지 이동
//seq: 1=일반 공지사항, 2=일반 개선요청사항
//seq: 3=관리 공지사항, 4=관리 개선요청사항
function showBoardDetail (boardNo, seq) {

    let url = seq % 2 === 1 ? '/board/selNoticeBoardDetail/' + boardNo : '/board/selBoardDetail/' + boardNo;
    if(seq > 2) url = '/admin' + url;
    location.href = url;

}