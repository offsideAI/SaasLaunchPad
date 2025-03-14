
/*
 * popup.js - Handles the popup UI interaction
 */
document.addEventListener('DOMContentLoaded', function() {
    // Add click event listeners to buttons
    document.getElementById('scrapeHTML').addEventListener('click', function() {
      scrapeContent('html');
    });

    document.getElementById('scrapeCSS').addEventListener('click', function() {
      scrapeContent('css');
    });

    document.getElementById('scrapeBoth').addEventListener('click', function() {
      scrapeContent('both');
    });

    // Function to scrape content based on type
    function scrapeContent(type) {
      // Update status
      document.getElementById('status').textContent = 'Scraping...';

      // Get the active tab
      chrome.tabs.query({active: true, currentWindow: true}, function(tabs) {
        const activeTab = tabs[0];

        // Execute content script to scrape the page
        chrome.scripting.executeScript({
          target: {tabId: activeTab.id},
          func: scrapeFromPage,
          args: [type]
        }, (results) => {
          if (results && results[0].result) {
            const data = results[0].result;

            if (type === 'html' || type === 'both') {
              downloadFile(data.html, 'scraped_page.html', 'text/html');
            }

            if (type === 'css' || type === 'both') {
              downloadFile(data.css, 'scraped_styles.css', 'text/css');
            }

            document.getElementById('status').textContent = 'Scraping complete!';
          } else {
            document.getElementById('status').textContent = 'Error scraping content.';
          }
        });
      });
    }

    // Function to download scraped content as a file
    function downloadFile(content, filename, contentType) {
      const blob = new Blob([content], {type: contentType});
      const url = URL.createObjectURL(blob);

      chrome.downloads.download({
        url: url,
        filename: filename,
        saveAs: true
      });
    }
  });

  // Function that will be injected into the page to scrape content
  function scrapeFromPage(type) {
    let result = {};

    // Scrape HTML
    if (type === 'html' || type === 'both') {
      result.html = document.documentElement.outerHTML;
    }

    // Scrape CSS
    if (type === 'css' || type === 'both') {
      let cssText = '';

      // Get CSS from stylesheets
      for (let i = 0; i < document.styleSheets.length; i++) {
        try {
          const styleSheet = document.styleSheets[i];

          // Handle both internal and external stylesheets
          if (styleSheet.href) {
            cssText += `/* External stylesheet: ${styleSheet.href} */\n\n`;
          } else {
            cssText += `/* Internal stylesheet */\n\n`;
          }

          // Get CSS rules
          const cssRules = styleSheet.cssRules || styleSheet.rules;
          for (let j = 0; j < cssRules.length; j++) {
            cssText += cssRules[j].cssText + '\n';
          }

          cssText += '\n\n';
        } catch (e) {
          // Some stylesheets might not be accessible due to CORS
          cssText += `/* Could not access stylesheet: ${e.message} */\n\n`;
        }
      }

      // Get inline styles
      const elementsWithStyle = document.querySelectorAll('[style]');
      if (elementsWithStyle.length > 0) {
        cssText += '/* Inline styles */\n\n';

        for (let i = 0; i < elementsWithStyle.length; i++) {
          const element = elementsWithStyle[i];
          const selector = element.tagName.toLowerCase() +
                           (element.id ? '#' + element.id : '') +
                           (element.className ? '.' + element.className.replace(/\s+/g, '.') : '');

          cssText += `${selector} {${element.style.cssText}}\n`;
        }
      }

      result.css = cssText;
    }

    return result;
  }
