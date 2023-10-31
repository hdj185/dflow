const progressBarBoxes = document.querySelectorAll('.m_progressbar .progressbar_box');

progressBarBoxes.forEach((box, index) => {
    const iconBox = box.querySelector('.icon_box');
    const progressBar = box.querySelector('.progressbar span');
    const colors = ['#6672fb', '#f64130', '#ffa100'];

    iconBox.style.background = colors[index % colors.length];
    progressBar.style.backgroundColor = colors[index % colors.length];
});