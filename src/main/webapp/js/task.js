import {getById, setSelectedOption} from './common.js';

window.onload = function () {
    let priority, status, tagsIds;

    priority = getById('priority_').value
    status = getById('status_').value
    tagsIds = getById('tags_').value

    setSelectedTaskAttributes(priority, status, tagsIds)
}

function setSelectedTaskAttributes(priority, status, tagsIds) {
    setSelectedOption(priority)
    setSelectedOption(status)

    let tags = tagsIds.split(' ')
    tags.forEach(tag => setSelectedOption(tag))
}
