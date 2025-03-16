/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/*!************************!*\
  !*** ./src/content.ts ***!
  \************************/


const blurFilter = "blur(6px)"

let textToBlur = ""

// Search this DOM node for text to blur and blur the parent element if found

function processNode(node) {
    if (node.childNodes.length > 0) {
        Array.from(node.childNodes).forEach(processNode)
    }

    if (node.nodeType === Node.TEXT_NODE
        && node.textContent !== null && node.textContent.trim().length > 0) {
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

function blurElement(elem) {
    elem.style.filter = blurFilter
    console.debug("blurred id:" + elem.id + " class:" + elem.className +
        " tag: " + elem.tagName + " text:" + elem.textContent)
}

// Create a MutationObserver to watchfor changes to the DOM
const observer = new MutationObserver( (mutations) => {
        mutations.forEach( (mutation) => {
           if (mutation.addedNodes.length > 0) {
               mutation.addedNodes.forEach(processNode)
           } else {
               processNode(mutation.target)
           }
        })
})

// Enable the content script by default
let enabled = true
const keys = ["enabled", "item"]

chrome.storage.sync.get(keys, (data) => {
    if (data.enabled === false) {
        enabled = false
    }
    if (data.item) {
        textToBlur = data.item
    }
    // Only start observing the DOM if the extension is enabled and there is text to blur
    if (enabled && textToBlur.trim().length > 0) {
        observer.observe(document, {
            attributes: true,
            characterData: true,
            childList: true,
            subtree: true,
        })
        // Loop through all elements on the page for initial processing
        processNode(document)
    }
})
/******/ })()
;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiY29udGVudC5qcyIsIm1hcHBpbmdzIjoiOzs7OztBQUFZOztBQUVaOztBQUVBOztBQUVBOztBQUVBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBOztBQUVBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0EsYUFBYTtBQUNiO0FBQ0E7QUFDQSxTQUFTO0FBQ1QsQ0FBQzs7QUFFRDtBQUNBO0FBQ0E7O0FBRUE7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBO0FBQ0E7QUFDQTtBQUNBLFNBQVM7QUFDVDtBQUNBO0FBQ0E7QUFDQSxDQUFDLEMiLCJzb3VyY2VzIjpbIndlYnBhY2s6Ly9zYWFzbGF1bmNocGFkLy4vc3JjL2NvbnRlbnQudHMiXSwic291cmNlc0NvbnRlbnQiOlsiXCJ1c2Ugc3RyaWN0XCJcblxuY29uc3QgYmx1ckZpbHRlciA9IFwiYmx1cig2cHgpXCJcblxubGV0IHRleHRUb0JsdXIgPSBcIlwiXG5cbi8vIFNlYXJjaCB0aGlzIERPTSBub2RlIGZvciB0ZXh0IHRvIGJsdXIgYW5kIGJsdXIgdGhlIHBhcmVudCBlbGVtZW50IGlmIGZvdW5kXG5cbmZ1bmN0aW9uIHByb2Nlc3NOb2RlKG5vZGUpIHtcbiAgICBpZiAobm9kZS5jaGlsZE5vZGVzLmxlbmd0aCA+IDApIHtcbiAgICAgICAgQXJyYXkuZnJvbShub2RlLmNoaWxkTm9kZXMpLmZvckVhY2gocHJvY2Vzc05vZGUpXG4gICAgfVxuXG4gICAgaWYgKG5vZGUubm9kZVR5cGUgPT09IE5vZGUuVEVYVF9OT0RFXG4gICAgICAgICYmIG5vZGUudGV4dENvbnRlbnQgIT09IG51bGwgJiYgbm9kZS50ZXh0Q29udGVudC50cmltKCkubGVuZ3RoID4gMCkge1xuICAgICAgICBjb25zdCBwYXJlbnQgPSBub2RlLnBhcmVudEVsZW1lbnRcblxuICAgICAgICBpZiAocGFyZW50ICE9PSBudWxsICYmXG4gICAgICAgICAgICAocGFyZW50LnRhZ05hbWUgPT0gJ1NDUklQVCcgfHwgcGFyZW50LnN0eWxlLmZpbHRlciA9PSBibHVyRmlsdGVyKSkge1xuICAgICAgICAgICAgLy8gQWxyZWFkeSBibHVycmVkXG4gICAgICAgICAgICByZXR1cm5cbiAgICAgICAgfVxuXG4gICAgICAgIGlmIChub2RlLnRleHRDb250ZW50LmluY2x1ZGVzKHRleHRUb0JsdXIpKSB7XG4gICAgICAgICAgICBibHVyRWxlbWVudChwYXJlbnQpXG4gICAgICAgIH1cbiAgICB9XG59XG5cbmZ1bmN0aW9uIGJsdXJFbGVtZW50KGVsZW0pIHtcbiAgICBlbGVtLnN0eWxlLmZpbHRlciA9IGJsdXJGaWx0ZXJcbiAgICBjb25zb2xlLmRlYnVnKFwiYmx1cnJlZCBpZDpcIiArIGVsZW0uaWQgKyBcIiBjbGFzczpcIiArIGVsZW0uY2xhc3NOYW1lICtcbiAgICAgICAgXCIgdGFnOiBcIiArIGVsZW0udGFnTmFtZSArIFwiIHRleHQ6XCIgKyBlbGVtLnRleHRDb250ZW50KVxufVxuXG4vLyBDcmVhdGUgYSBNdXRhdGlvbk9ic2VydmVyIHRvIHdhdGNoZm9yIGNoYW5nZXMgdG8gdGhlIERPTVxuY29uc3Qgb2JzZXJ2ZXIgPSBuZXcgTXV0YXRpb25PYnNlcnZlciggKG11dGF0aW9ucykgPT4ge1xuICAgICAgICBtdXRhdGlvbnMuZm9yRWFjaCggKG11dGF0aW9uKSA9PiB7XG4gICAgICAgICAgIGlmIChtdXRhdGlvbi5hZGRlZE5vZGVzLmxlbmd0aCA+IDApIHtcbiAgICAgICAgICAgICAgIG11dGF0aW9uLmFkZGVkTm9kZXMuZm9yRWFjaChwcm9jZXNzTm9kZSlcbiAgICAgICAgICAgfSBlbHNlIHtcbiAgICAgICAgICAgICAgIHByb2Nlc3NOb2RlKG11dGF0aW9uLnRhcmdldClcbiAgICAgICAgICAgfVxuICAgICAgICB9KVxufSlcblxuLy8gRW5hYmxlIHRoZSBjb250ZW50IHNjcmlwdCBieSBkZWZhdWx0XG5sZXQgZW5hYmxlZCA9IHRydWVcbmNvbnN0IGtleXMgPSBbXCJlbmFibGVkXCIsIFwiaXRlbVwiXVxuXG5jaHJvbWUuc3RvcmFnZS5zeW5jLmdldChrZXlzLCAoZGF0YSkgPT4ge1xuICAgIGlmIChkYXRhLmVuYWJsZWQgPT09IGZhbHNlKSB7XG4gICAgICAgIGVuYWJsZWQgPSBmYWxzZVxuICAgIH1cbiAgICBpZiAoZGF0YS5pdGVtKSB7XG4gICAgICAgIHRleHRUb0JsdXIgPSBkYXRhLml0ZW1cbiAgICB9XG4gICAgLy8gT25seSBzdGFydCBvYnNlcnZpbmcgdGhlIERPTSBpZiB0aGUgZXh0ZW5zaW9uIGlzIGVuYWJsZWQgYW5kIHRoZXJlIGlzIHRleHQgdG8gYmx1clxuICAgIGlmIChlbmFibGVkICYmIHRleHRUb0JsdXIudHJpbSgpLmxlbmd0aCA+IDApIHtcbiAgICAgICAgb2JzZXJ2ZXIub2JzZXJ2ZShkb2N1bWVudCwge1xuICAgICAgICAgICAgYXR0cmlidXRlczogdHJ1ZSxcbiAgICAgICAgICAgIGNoYXJhY3RlckRhdGE6IHRydWUsXG4gICAgICAgICAgICBjaGlsZExpc3Q6IHRydWUsXG4gICAgICAgICAgICBzdWJ0cmVlOiB0cnVlLFxuICAgICAgICB9KVxuICAgICAgICAvLyBMb29wIHRocm91Z2ggYWxsIGVsZW1lbnRzIG9uIHRoZSBwYWdlIGZvciBpbml0aWFsIHByb2Nlc3NpbmdcbiAgICAgICAgcHJvY2Vzc05vZGUoZG9jdW1lbnQpXG4gICAgfVxufSkiXSwibmFtZXMiOltdLCJzb3VyY2VSb290IjoiIn0=