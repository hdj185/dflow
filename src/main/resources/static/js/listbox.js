

function changeItemClass(item, seq) {
    var listbox1Items = document.querySelectorAll('#listbox-' + seq + ' li');

    // 모든 li 요소의 클래스를 'list'로 설정
    for (var i = 0; i < listbox1Items.length; i++) {
        listbox1Items[i].classList.remove('list-selected');
        listbox1Items[i].classList.add('list');
    }

    // 선택된 li 요소에 클래스를 추가
    item.classList.toggle('list-selected');

}