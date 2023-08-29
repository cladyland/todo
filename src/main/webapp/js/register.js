import {getById} from './common.js';

window.onload = function () {
    let username_message_id = 'wrong_username'
    let status = document.getElementById('status').value

    if (status === '400') {
        let errors_json = getById('errors').value
        let errors_map = new Map(Object.entries(JSON.parse(errors_json)))
        let username_error = errors_map.get('username')
        let first_name_error = errors_map.get('firstName')
        let last_name_error = errors_map.get('lastName')
        let password_error = errors_map.get('password')

        let message_id;

        if (isDefined(username_error)) {
            setMessageParamToWrongField(username_message_id, 'Username ' + username_error)
        }
        if (isDefined(first_name_error)) {
            message_id = 'wrong_first_name'
            setMessageParamToWrongField(message_id, 'First name ' + first_name_error)
        }
        if (isDefined(last_name_error)) {
            message_id = 'wrong_last_name'
            setMessageParamToWrongField(message_id, 'Last name ' + last_name_error)
        }
        if (isDefined(password_error)) {
            message_id = 'wrong_password'
            setMessageParamToWrongField(message_id, 'Password ' + password_error)
        }
    } else if (status === '409') {
        let error_message = getById('error').value
        setMessageParamToWrongField(username_message_id, error_message)
    }
}

function isDefined(param) {
    return param !== undefined
}

function setMessageParamToWrongField(message_id, error_message) {
    let wrong = getById(message_id)
    wrong.textContent = error_message
}
