import {getById, setSelectedOption} from './common.js';

window.onload = function () {
    let title = document.querySelector('#title')
    let description = document.querySelector('#description')
    let priority = getById('priority')
    let status = getById('status')
    let tags = getById('task_tags')

    setStorageItem(title, 'keyup', 'title_note')
    setStorageItem(description, 'keyup', 'description_note')
    setStorageItem(priority, 'change', 'priority_selected')
    setStorageItem(status, 'change', 'status_selected')

    tags.onchange = function () {
        let selectedIds = new Array(tags.options.length)
        for (let i = 0; i < selectedIds.length; i++) {
            let option = tags.options[i]
            let selected = option.selected
            selectedIds[i] = selected ? option.value : ''
        }
        localStorage.tags_selected = JSON.stringify(selectedIds)
    }

    let priority_selected = localStorage.priority_selected
    let status_selected = localStorage.status_selected
    let tags_selected = localStorage.tags_selected

    title.value = localStorage.getItem('title_note')
    description.value = localStorage.getItem('description_note')

    if (priority_selected) setSelectedOption(priority_selected)
    if (status_selected) setSelectedOption(status_selected)
    if (tags_selected) {
        JSON.parse(tags_selected).forEach(element => {
            if (element !== '') setSelectedOption(element)
        })
    }
}

function setStorageItem(element, type, key) {
    element.addEventListener(type, evt => {
        localStorage.setItem(key, evt.target.value)
    })
}
