/******/ (() => { // webpackBootstrap
/******/ 	"use strict";
/*!***************************!*\
  !*** ./src/background.ts ***!
  \***************************/

function setBadgeText(enabled) {
    const text = enabled ? "ON" : "OFF";
    void chrome.action.setBadgeText({ text: text });
}
function startUp() {
    chrome.storage.sync.get("enabled", (data) => {
        setBadgeText(!!data.enabled);
    });
}
// Ensure the background script always runs
chrome.runtime.onStartup.addListener(startUp);
chrome.runtime.onInstalled.addListener(startUp);

/******/ })()
;
//# sourceMappingURL=data:application/json;charset=utf-8;base64,eyJ2ZXJzaW9uIjozLCJmaWxlIjoiYmFja2dyb3VuZC5qcyIsIm1hcHBpbmdzIjoiOzs7OztBQUFZO0FBRVosU0FBUyxZQUFZLENBQUMsT0FBTztJQUN6QixNQUFNLElBQUksR0FBRyxPQUFPLENBQUMsQ0FBQyxDQUFDLElBQUksQ0FBQyxDQUFDLENBQUMsS0FBSztJQUNuQyxLQUFLLE1BQU0sQ0FBQyxNQUFNLENBQUMsWUFBWSxDQUFDLEVBQUMsSUFBSSxFQUFFLElBQUksRUFBQyxDQUFDO0FBQ2pELENBQUM7QUFFRCxTQUFTLE9BQU87SUFDWixNQUFNLENBQUMsT0FBTyxDQUFDLElBQUksQ0FBQyxHQUFHLENBQUMsU0FBUyxFQUFFLENBQUMsSUFBSSxFQUFFLEVBQUU7UUFDeEMsWUFBWSxDQUFDLENBQUMsQ0FBQyxJQUFJLENBQUMsT0FBTyxDQUFDO0lBQ2hDLENBQUMsQ0FBQztBQUNOLENBQUM7QUFFRCwyQ0FBMkM7QUFDM0MsTUFBTSxDQUFDLE9BQU8sQ0FBQyxTQUFTLENBQUMsV0FBVyxDQUFDLE9BQU8sQ0FBQztBQUM3QyxNQUFNLENBQUMsT0FBTyxDQUFDLFdBQVcsQ0FBQyxXQUFXLENBQUMsT0FBTyxDQUFDIiwic291cmNlcyI6WyJ3ZWJwYWNrOi8vc2Fhc2xhdW5jaHBhZC8uL3NyYy9iYWNrZ3JvdW5kLnRzIl0sInNvdXJjZXNDb250ZW50IjpbIlwidXNlIHN0cmljdFwiXG5cbmZ1bmN0aW9uIHNldEJhZGdlVGV4dChlbmFibGVkKSB7XG4gICAgY29uc3QgdGV4dCA9IGVuYWJsZWQgPyBcIk9OXCIgOiBcIk9GRlwiXG4gICAgdm9pZCBjaHJvbWUuYWN0aW9uLnNldEJhZGdlVGV4dCh7dGV4dDogdGV4dH0pXG59XG5cbmZ1bmN0aW9uIHN0YXJ0VXAoKSB7XG4gICAgY2hyb21lLnN0b3JhZ2Uuc3luYy5nZXQoXCJlbmFibGVkXCIsIChkYXRhKSA9PiB7XG4gICAgICAgIHNldEJhZGdlVGV4dCghIWRhdGEuZW5hYmxlZClcbiAgICB9KVxufVxuXG4vLyBFbnN1cmUgdGhlIGJhY2tncm91bmQgc2NyaXB0IGFsd2F5cyBydW5zXG5jaHJvbWUucnVudGltZS5vblN0YXJ0dXAuYWRkTGlzdGVuZXIoc3RhcnRVcClcbmNocm9tZS5ydW50aW1lLm9uSW5zdGFsbGVkLmFkZExpc3RlbmVyKHN0YXJ0VXApXG4iXSwibmFtZXMiOltdLCJzb3VyY2VSb290IjoiIn0=