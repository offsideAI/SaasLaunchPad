"use strict";

console.log("Welcome to SaasLaunchPad")

function setBadgeText(enabled) {
  const text = enabled ? "ON" : "OFF"
  void chrome.action.setBadgeText({text: text})
}

// Handle the ON/OFF switch
const checkbox = document.getElementById("enabled")

chrome.storage.sync.get("enabled", (data) => {
  checkbox.checked = !!data.enabled
  void setBadgeText(data.enabled)
})


checkbox.addEventListener("change", (event) => {
  if (event.target instanceof HTMLInputElement) {
    void chrome.storage.sync.set({"enabled": event.target.checked})
    void setBadgeText(event.target.checked)
  }
})

// Handle the input field
const inputItem = document.getElementById("item")

chrome.storage.sync.get("item", (data) => {
  inputItem.value = data.item
})

inputItem.addEventListener("change", (event) => {
  if (event.target instanceof HTMLInputElement) {
      void chrome.storage.sync.set({"item": event.target.value})
  }
})