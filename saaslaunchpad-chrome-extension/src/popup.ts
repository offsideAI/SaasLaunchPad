import { Message, setBadgeText, StoredConfig } from "./common"

console.log("Welcome to Web Pro Tools")

// Handle the ON/OFF switch
const checkbox = document.getElementById("enabled") as HTMLInputElement

chrome.storage.sync.get("enabled", (data) => {
  const config: StoredConfig = data as StoredConfig
  checkbox.checked = !!config.enabled
  void setBadgeText(!!config.enabled)
})

// Utility function to show status messages
function showStatus(message: string, isError: boolean = false) {
  const statusElement = document.getElementById("status") as HTMLDivElement
  statusElement.textContent = message
  statusElement.className = isError ? "status error" : "status"
  setTimeout(() => {
    statusElement.textContent = ""
    statusElement.className = "status"
  }, 3000)
}

checkbox.addEventListener("change", (event) => {
  if (event.target instanceof HTMLInputElement) {
    void chrome.storage.sync.set({ enabled: event.target.checked })
    void setBadgeText(event.target.checked)
    // Send message to content script
    // Send message to content script in all tabs
    const message: Message = { enabled: event.target.checked }
      chrome.tabs.query({})
      .then((tabs) => {
        for (const tab of tabs) {
          // Note: sensitive tab properties such as tab.title or tab.url can only be accessed for
          // URLs in the host_permissions section of manifest.json
          chrome.tabs
            .sendMessage(tab.id!, { enabled: message })
            .then((response) => {
              console.info(
                "Popup received response from tab with title '%s' and url %s",
                response.title,
                response.url,
              )
            })
            .catch((error) => {
              console.warn("Popup could not send message to tab %d", tab.id, error)
            })
        }
      })
      .catch( (error) => {

      })

  }
})

// Handle the Retrieve button
const retrieveButton = document.getElementById(
  "doRetrieveData",
) as HTMLButtonElement
retrieveButton.addEventListener("click", async (event) => {
  // Check if the extension is enabled
  if (!checkbox.checked) {
    // Show error message if extension is disabled
    showStatus("Please enable the extension first...", true)
    return
  }
  // Send message to content script
  // Send message to content script in all tabs
  const tabs = await chrome.tabs.query({})
  for (const tab of tabs) {
    // Note: sensitive tab properties such as tab.title or tab.url can only be accessed for
    // URLs in the host_permissions section of manifest.json
    chrome.tabs
      .sendMessage(tab.id!, { enabled: true })
      .then((response) => {
        console.info(
          "Retrieve button received response from tab with title '%s' and url %s",
          response.title,
          response.url,
        )
        const inputRetrievedTitle = document.getElementById(
          "inputRetrievedTitle",
        ) as HTMLInputElement
        if (inputRetrievedTitle) {
          inputRetrievedTitle.value = response.title
        }
        const inputRetrievedUrl = document.getElementById(
          "inputRetrievedUrl",
        ) as HTMLInputElement
        if (inputRetrievedUrl) {
          inputRetrievedUrl.value = response.url
        }
      })
      .catch((error) => {
        console.warn("Popup could not send message to tab %d", tab.id, error)
      })
  }
})

// Handle the input field
const inputItem = document.getElementById("item") as HTMLInputElement

chrome.storage.sync.get("item", (data) => {
  inputItem.value = data.item
})

inputItem.addEventListener("change", (event) => {
  if (event.target instanceof HTMLInputElement) {
    void chrome.storage.sync.set({ item: event.target.value })
  }
})

// Handle the Blur Now button
const doBlurNowButton = document.getElementById(
  "doBlurNow",
) as HTMLButtonElement
doBlurNowButton.addEventListener("click", async (event) => {
  // Check if the extension is enabled
  if (!checkbox.checked) {
    // Show error message if extension is disabled
    showStatus("Please enable the extension first.", true)
    return
  }
  // Get the current text to blur from the input field
  const textToBlur = inputItem.value.trim()
  const tabs = await chrome.tabs.query({ active: true, currentWindow: true })
  if (tabs[0]?.id) {
    chrome.tabs
      .sendMessage(tabs[0].id, { action: "blurNow", textToBlur })
      .then((response) => {
        console.info("Blur Now button was pressed")
      })
      .catch((error) => {
        console.warn("Blur Now button error %d", error)
      })
  }
})

// Handle the Runtime Message button
const doRuntimeMessageButton = document.getElementById(
  "doRuntimeMessageNow",
) as HTMLButtonElement
doRuntimeMessageButton.addEventListener("click", async (event) => {
  // Check if the extension is enabled
  // Check if the extension is enabled
  if (!checkbox.checked) {
    // Show error message if extension is disabled
    showStatus("Please enable the extension first...", true)
    return
  }

  chrome.runtime
    .sendMessage({ enabled: true, action: "runtimeMessageNow" })
    .then((response) => {
      console.info("Popup received runtimeMessageNow response", response)
    })
    .catch((error) => {
      console.warn("Popup could not send runtimeMessageNow")
    })
})

// Handle the Remove Ads button
const doRemoveAdsButton = document.getElementById(
  "doRemoveAdsNow",
) as HTMLButtonElement
doRemoveAdsButton.addEventListener("click", async (event) => {
  // Check if the extension is enabled
  if (!checkbox.checked) {
    // Show error message if extension is disabled
    showStatus("Please enable the extension first...", true)
    return
  }
  // Get the active tab
  const tabs = await chrome.tabs.query({ active: true, currentWindow: true })
  if (tabs[0]?.id) {
    // Send message to remove ads in the active tab
    chrome.tabs
      .sendMessage(tabs[0].id, { action: "removeAds" })
      .then((response) => {
        console.info("Ad removal response:", response)
        if (response && response.success) {
          showStatus(`Removed ${response.count} advertisements from the page`)
        } else {
          showStatus("No advertisements found to remove")
        }
      })
      .catch((error) => {
        console.warn("Could not remove ads:", error)
        showStatus("Failed to remove advertisements", true)
      })
  }
})
