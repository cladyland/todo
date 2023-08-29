function cleanTaskStorage() {
    localStorage.clear();
}

function countCharacters(max_size, element) {
    let count = document.getElementById('count')
    let diff = max_size - element.length
    count.style.color = (diff < 30) ? 'red' : 'black'
    count.innerHTML = 'characters left: ' + diff;
}
