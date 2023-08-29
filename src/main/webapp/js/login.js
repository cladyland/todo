import {getById} from './common.js';

window.onload = function () {
    let status = getById('status').value

    if (status === '400') {
        setErrorMessageParam('Please, enter your login and password')
    } else if (status === '401') {
        setErrorMessageParam('Wrong login or password')
    }
}

function setErrorMessageParam(content) {
    let error_message = getById('error')
    error_message.textContent = content
}
