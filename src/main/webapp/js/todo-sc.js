function checkLoginRespStatus(status) {
    if (status === 400) {
        setErrorMessageParam("Please, enter your login and password")
    } else if (status === 401) {
        setErrorMessageParam("Wrong login or password")
    }
}

function setErrorMessageParam(content) {
    let error_message = getById("error")
    error_message.textContent = content
}

function checkRegisterRespStatus(status) {
    const username_message_id = "wrong_username"

    if (status === 400) {
        const errors_json = getById("errors").value
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
        let error_message = getById("error").value
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

function saveTaskData() {
    let title = document.querySelector("#title")
    let description = document.querySelector("#description")
    let priority = getById("priority")
    let status = getById("status")
    let tags = getById("task_tags")

    setStorageItem(title, "keyup", "title_note")
    setStorageItem(description, "keyup", "description_note")
    setStorageItem(priority, "change", "priority_selected")
    setStorageItem(status, "change", "status_selected")

    tags.onchange = function () {
        let selectedIds = new Array(tags.options.length)
        for (let i = 0; i < selectedIds.length; i++) {
            let option = tags.options[i]
            let selected = option.selected
            selectedIds[i] = selected ? option.value : ""
        }
        localStorage.tags_selected = JSON.stringify(selectedIds)
    }

    let priority_selected = localStorage.priority_selected
    let status_selected = localStorage.status_selected
    let tags_selected = localStorage.tags_selected

    title.value = localStorage.getItem("title_note")
    description.value = localStorage.getItem("description_note")

    if (priority_selected) setSelectedOption(priority_selected)
    if (status_selected) setSelectedOption(status_selected)
    if (tags_selected) {
        JSON.parse(tags_selected).forEach(element => {
            if (element !== "") setSelectedOption(element)
        })
    }
}

function setStorageItem(element, type, key) {
    element.addEventListener(type, evt => {
        localStorage.setItem(key, evt.target.value)
    })
}

function setSelectedOption(element_id) {
    document.getElementById(element_id).selected = true
}

function cleanTaskStorage() {
    localStorage.clear();
}

function setSelectedTaskAttributes(priority, status, tagsIds) {
    setSelectedOption(priority)
    setSelectedOption(status)

    let tags = tagsIds.split(' ')
    tags.forEach(tag => setSelectedOption(tag))
}

function countCharacters(max_size, element) {
    let count = getById('count')
    let diff = max_size - element.length
    count.style.color = (diff < 30) ? 'red' : 'black'
    count.innerHTML = "characters left: " + diff;
}

function checkCommentRespStatus(status) {
    if (status === 400) {
        let wrong_comment = getById("wrong_comment")
        wrong_comment.textContent = "Please, write something"
        wrong_comment.style.color = "red"
    }
}

function getById(id) {
    return document.getElementById(id)
}
