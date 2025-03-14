"use strict"

const blurFilter = "blur(6px)"

let textToBlur = ""

// Search this DOM node for text to blur and blur the parent element if found

function processNode(node) {
    if (node.childNodes.length > 0) {
        Array.from(node.childNodes).forEach(processNode)
    }

    if (node.nodeType === Node.TEXT_NODE
        && node.textContent !== null && node.textContent.trim().length > 0 {
        const parent = node.parentElement

        if (parent !== null &&
            (parent.tagName == 'SCRIPT' || parent.style.filter == blurFilter)) {
            // Already blurred
            return
        }

        if (node.textContent.includes(textToBlur)) {
            blurElement(parent)
        }
    }
}

function blurElement(element) {
    elem.style.filter = blurFilter
    console.debug("blurred id:" + elem.id + " class:" + elem.className +
        " tag: " + elem.tagName + " text:" + elem.textContent)
}

