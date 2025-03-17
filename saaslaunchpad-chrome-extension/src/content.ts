const blurFilter = "blur(6px)"

let textToBlur = ""

// Search this DOM node for text to blur and blur the parent element if found
function processNode(node: Node) {
    if (node.childNodes.length > 0) {
        Array.from(node.childNodes).forEach(processNode)
    }

    if (node.nodeType === Node.TEXT_NODE
        && node.textContent !== null && node.textContent.trim().length > 0) {
        const parent = node.parentElement
        if (parent == null) {
            return
        }
        if (parent.tagName == 'SCRIPT' || parent.style.filter == blurFilter) {
            // Already blurred
            return
        }

        if (node.textContent.includes(textToBlur)) {
            blurElement(parent)
        }
    }
}

function blurElement(elem: HTMLElement) {
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

function observe() {
    // Only start observing the DOM if the extension is enabled and there is text to blur
    if (enabled && textToBlur.trim().length > 0) {
        observer.observe(document, {
            attributes: false,
            characterData: true,
            childList: true,
            subtree: true,
        })
        // Loop through all elements on the page for initial processing
        processNode(document)
    }
}

chrome.storage.sync.get(keys, (data) => {
    if (data.enabled === false) {
        enabled = false
    }
    if (data.item) {
        textToBlur = data.item
    }
    observe()
})

// Listen for messages from popup
chrome.runtime.onMessage.addListener( (request, sender, sendResponse) => {
    if (request.enabled !== undefined) {
        console.log("Received message from sender %s", sender.id, request)
        enabled = request.enabled
        if (enabled) {
            observe()
        } else {
            observer.disconnect()
        }
        sendResponse({title: document.title, url: window.location.href})
    }

    // Handle blurNow action from popup
    if (request.action === "blurNow") {
        console.log("--Received blurNow request with text:", request.textToBlur)
        
        // Store the original text to blur
        const originalText = textToBlur
        
        // Temporarily set the text to blur to the requested text
        textToBlur = request.textToBlur
        
        // Disconnect observer temporarily to avoid double processing
        observer.disconnect()
        
        // Track how many elements were blurred
        let blurredCount = 0

        processNode(document)
        
        // Restore the original text to blur
        textToBlur = originalText
        
        // Reconnect observer if extension is enabled
        if (enabled && textToBlur.trim().length > 0) {
            observe()
        }
        
        // Send response with the number of elements blurred
        sendResponse({
            success: true,
            count: blurredCount
        })
        
        return true // Keep the message channel open for the async response
    }

    // Handle removeAds action from popup
    if (request.action === "removeAds") {
        console.log("Received removeAds request")

        // Try multiple selectors that might match ads
        const adSelectors = [
            'div[data-testid="StandardAd"]',
            'div[class*="ad-"]',
            'div[class*="advertisement"]',
            'div[id*="ad-"]',
            'div[id*="advertisement"]',
            'iframe[src*="ad"]',
            'div[aria-label*="advertisement"]'
        ]
        
        let removedCount = 0
        
        // Try each selector
        adSelectors.forEach(selector => {
            const adDivs = document.querySelectorAll(selector)
            console.log(`Found ${adDivs.length} elements matching selector: ${selector}`)
            
            // Remove each ad div from the DOM
            adDivs.forEach(adDiv => {
                // Remove the element from its parent
                if (adDiv.parentNode) {
                    adDiv.parentNode.removeChild(adDiv)
                    removedCount++
                }
            })
        })

        console.log(`Removed ${removedCount} ad divs from the page`)
    
        // Set up a MutationObserver to catch dynamically added ads
        const adObserver = new MutationObserver((mutations) => {
            mutations.forEach((mutation) => {
                if (mutation.addedNodes.length > 0) {
                    mutation.addedNodes.forEach((node) => {
                        if (node.nodeType === Node.ELEMENT_NODE) {
                            // Check if the added node is an ad
                            const element = node as HTMLElement
                            adSelectors.forEach(selector => {
                                if (element.matches && element.matches(selector)) {
                                    console.log('Removing dynamically added ad:', element)
                                    element.remove()
                                }
                                
                                // Also check children of the added node
                                const childAds = element.querySelectorAll(selector)
                                childAds.forEach(ad => {
                                    console.log('Removing dynamically added ad child:', ad)
                                    ad.remove()
                                })
                            })
                        }
                    })
                }
            })
        })
        
        // Start observing the document for dynamically added ads
        adObserver.observe(document.body, {
            childList: true,
            subtree: true
        })
        
        // Store the observer so we can disconnect it later if needed
        const existingObserver = (window as any)._adObserver
        if (existingObserver) {
            existingObserver.disconnect()
        }
        (window as any)._adObserver = adObserver
        
        // Send response with the number of ads removed
        sendResponse({
            success: true,
            count: removedCount
        })
        
        return true // Keep the message channel open for the async response
    }

    return true // Keep the message channel open for the async response

})
