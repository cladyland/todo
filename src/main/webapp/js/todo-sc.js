function checkLoginRespStatus(status) {
    if (status === 400) {
        setErrorMessageParam("Please, enter your login and password")
    } else if (status === 401) {
        setErrorMessageParam("Wrong login or password")
    }
}

function setErrorMessageParam(content) {
    let error_message = document.getElementById("error")
    error_message.textContent = content
}

function checkRegisterRespStatus(status) {
    const username_message_id = "wrong_username"

    if (status === 400) {
        const errors_json = document.getElementById("errors").value
        const errors_map = new Map(Object.entries(JSON.parse(errors_json)))
        const username_error = errors_map.get("username")
        const first_name_error = errors_map.get("firstName")
        const last_name_error = errors_map.get("lastName")
        const password_error = errors_map.get("password")

        let message_id;

        if (isDefined(username_error)) {
            setMessageParamToWrongField(username_message_id, "Username " + username_error)
        }
        if (isDefined(first_name_error)) {
            message_id = "wrong_first_name"
            setMessageParamToWrongField(message_id, "First name " + first_name_error)
        }
        if (isDefined(last_name_error)) {
            message_id = "wrong_last_name"
            setMessageParamToWrongField(message_id, "Last name " + last_name_error)
        }
        if (isDefined(password_error)) {
            message_id = "wrong_password"
            setMessageParamToWrongField(message_id, "Password " + password_error)
        }
    } else if (status === 409) {
        let error_message = document.getElementById("error").value
        setMessageParamToWrongField(username_message_id, error_message)
    }
}

function isDefined(param) {
    return param !== undefined
}

function setMessageParamToWrongField(message_id, error_message) {
    let wrong = document.getElementById(message_id)
    wrong.textContent = error_message
}

function saveTaskData() {
    let title = document.querySelector("#title")
    let description = document.querySelector("#description")

    title.value = localStorage.getItem("title_note")
    description.value = localStorage.getItem("description_note")

    title.addEventListener("keyup", evt => {
        localStorage.setItem("title_note", evt.target.value)
    })

    description.addEventListener("keyup", evt => {
        localStorage.setItem("description_note", evt.target.value)
    })
}

function cleanTaskStorage() {
    localStorage.removeItem("title_note")
    localStorage.removeItem("description_note")
}

function setSelectedTaskAttributes(priority, status, tagsIds) {
    document.getElementById(priority).selected = true
    document.getElementById(status).selected = true

    let tags = tagsIds.split(' ')
    tags.forEach(tag => document.getElementById(tag).selected = true)
}

function countCharacters(max_size, element) {
    let count = document.getElementById('count')
    let diff = max_size - element.length
    count.style.color = (diff < 0) ? 'red' : 'black'
    count.innerHTML = "characters left: " + diff;
}

function checkCommentRespStatus(status) {
    if (status === 400) {
        let wrong_comment = document.getElementById("wrong_comment")
        wrong_comment.textContent = "Please, write something"
        wrong_comment.style.color = "red"
    }
}
