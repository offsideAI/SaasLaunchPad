/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/*!**********************!*\
  !*** ./src/popup.ts ***!
  \**********************/


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
/******/ })()
;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoicG9wdXAuanMiLCJtYXBwaW5ncyI6Ijs7Ozs7QUFBYTs7QUFFYjs7QUFFQTtBQUNBO0FBQ0EsbUNBQW1DLFdBQVc7QUFDOUM7O0FBRUE7QUFDQTs7QUFFQTtBQUNBO0FBQ0E7QUFDQSxDQUFDOzs7QUFHRDtBQUNBO0FBQ0Esa0NBQWtDLGdDQUFnQztBQUNsRTtBQUNBO0FBQ0EsQ0FBQzs7QUFFRDtBQUNBOztBQUVBO0FBQ0E7QUFDQSxDQUFDOztBQUVEO0FBQ0E7QUFDQSxvQ0FBb0MsMkJBQTJCO0FBQy9EO0FBQ0EsQ0FBQyxDIiwic291cmNlcyI6WyJ3ZWJwYWNrOi8vc2Fhc2xhdW5jaHBhZC8uL3NyYy9wb3B1cC50cyJdLCJzb3VyY2VzQ29udGVudCI6WyJcInVzZSBzdHJpY3RcIjtcblxuY29uc29sZS5sb2coXCJXZWxjb21lIHRvIFNhYXNMYXVuY2hQYWRcIilcblxuZnVuY3Rpb24gc2V0QmFkZ2VUZXh0KGVuYWJsZWQpIHtcbiAgY29uc3QgdGV4dCA9IGVuYWJsZWQgPyBcIk9OXCIgOiBcIk9GRlwiXG4gIHZvaWQgY2hyb21lLmFjdGlvbi5zZXRCYWRnZVRleHQoe3RleHQ6IHRleHR9KVxufVxuXG4vLyBIYW5kbGUgdGhlIE9OL09GRiBzd2l0Y2hcbmNvbnN0IGNoZWNrYm94ID0gZG9jdW1lbnQuZ2V0RWxlbWVudEJ5SWQoXCJlbmFibGVkXCIpXG5cbmNocm9tZS5zdG9yYWdlLnN5bmMuZ2V0KFwiZW5hYmxlZFwiLCAoZGF0YSkgPT4ge1xuICBjaGVja2JveC5jaGVja2VkID0gISFkYXRhLmVuYWJsZWRcbiAgdm9pZCBzZXRCYWRnZVRleHQoZGF0YS5lbmFibGVkKVxufSlcblxuXG5jaGVja2JveC5hZGRFdmVudExpc3RlbmVyKFwiY2hhbmdlXCIsIChldmVudCkgPT4ge1xuICBpZiAoZXZlbnQudGFyZ2V0IGluc3RhbmNlb2YgSFRNTElucHV0RWxlbWVudCkge1xuICAgIHZvaWQgY2hyb21lLnN0b3JhZ2Uuc3luYy5zZXQoe1wiZW5hYmxlZFwiOiBldmVudC50YXJnZXQuY2hlY2tlZH0pXG4gICAgdm9pZCBzZXRCYWRnZVRleHQoZXZlbnQudGFyZ2V0LmNoZWNrZWQpXG4gIH1cbn0pXG5cbi8vIEhhbmRsZSB0aGUgaW5wdXQgZmllbGRcbmNvbnN0IGlucHV0SXRlbSA9IGRvY3VtZW50LmdldEVsZW1lbnRCeUlkKFwiaXRlbVwiKVxuXG5jaHJvbWUuc3RvcmFnZS5zeW5jLmdldChcIml0ZW1cIiwgKGRhdGEpID0+IHtcbiAgaW5wdXRJdGVtLnZhbHVlID0gZGF0YS5pdGVtXG59KVxuXG5pbnB1dEl0ZW0uYWRkRXZlbnRMaXN0ZW5lcihcImNoYW5nZVwiLCAoZXZlbnQpID0+IHtcbiAgaWYgKGV2ZW50LnRhcmdldCBpbnN0YW5jZW9mIEhUTUxJbnB1dEVsZW1lbnQpIHtcbiAgICAgIHZvaWQgY2hyb21lLnN0b3JhZ2Uuc3luYy5zZXQoe1wiaXRlbVwiOiBldmVudC50YXJnZXQudmFsdWV9KVxuICB9XG59KSJdLCJuYW1lcyI6W10sInNvdXJjZVJvb3QiOiIifQ==