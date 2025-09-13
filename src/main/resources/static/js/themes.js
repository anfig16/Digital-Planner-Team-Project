// Store API base URL
const API_BASE_URL = '/themes';

// Cache DOM elements
const themeSelect = document.getElementById('theme-select');
const themeForm = document.getElementById('theme-form');
const applyThemeButton = document.getElementById('apply-theme');

// Initialize theme management
async function initializeThemes() {
  try {
    // Fetch both user-specific and default themes
    const [userThemes, defaultThemes] = await Promise.all([
      fetchThemes('user123'), // Replace with actual user ID
      fetchThemes(null, true)
    ]);

    // Combine and populate themes dropdown
    populateThemeDropdown([...defaultThemes, ...userThemes]);
  } catch (error) {
    console.error('Failed to initialize themes:', error);
  }
}

// Fetch themes from the backend
async function fetchThemes(userId = null, isDefault = false) {
  try {
    const queryParams = new URLSearchParams();
    if (userId) queryParams.append('userId', userId);
    if (isDefault) queryParams.append('isDefault', 'true');

    const response = await fetch(`${API_BASE_URL}/getThemes?${queryParams}`);
    if (!response.ok) throw new Error('Failed to fetch themes');
    
    return await response.json();
  } catch (error) {
    console.error('Error fetching themes:', error);
    return [];
  }
}

// Populate the theme dropdown with fetched themes
function populateThemeDropdown(themes) {
  // Clear existing options except the first one (default)
  while (themeSelect.options.length > 1) {
    themeSelect.remove(1);
  }

  // Add fetched themes to dropdown
  themes.forEach(theme => {
    const option = document.createElement('option');
    option.value = theme.id;
    option.textContent = theme.name;
    themeSelect.appendChild(option);
  });
}

// Apply selected theme
async function applyTheme(themeId) {
  try {
    const response = await fetch(`${API_BASE_URL}/getTheme?id=${themeId}`);
    if (!response.ok) throw new Error('Failed to fetch theme');
    
    const theme = await response.json();
    
    // Apply theme properties to root element
    document.documentElement.style.setProperty('--primary-color', theme.primaryColor);
    document.documentElement.style.setProperty('--secondary-color', theme.secondaryColor);
    document.documentElement.style.setProperty('--text-color', theme.textColor);
    
    if (theme.backgroundImage) {
      document.documentElement.style.setProperty(
        '--background-image', 
        `url(${theme.backgroundImage})`
      );
    }
    
    // Save theme preference
    localStorage.setItem('selectedTheme', themeId);
  } catch (error) {
    console.error('Error applying theme:', error);
  }
}

// Save custom theme
async function saveCustomTheme(themeData) {
  try {
    const response = await fetch(`${API_BASE_URL}/saveTheme`, {
      method: 'POST',
      headers: {
        'Content-Type': 'application/json',
      },
      body: JSON.stringify({
        userId: 'user123', // Replace with actual user ID
        name: themeData.name,
        primaryColor: themeData.primaryColor,
        secondaryColor: themeData.secondaryColor,
        textColor: themeData.textColor,
        backgroundImage: themeData.backgroundImage
      })
    });

    if (!response.ok) throw new Error('Failed to save theme');
    
    // Refresh themes list
    await initializeThemes();
    
    return await response.json();
  } catch (error) {
    console.error('Error saving theme:', error);
    throw error;
  }
}

// Event Listeners
applyThemeButton.addEventListener('click', () => {
  const selectedThemeId = themeSelect.value;
  applyTheme(selectedThemeId);
});

themeForm.addEventListener('submit', async (e) => {
  e.preventDefault();
  
  const formData = {
    name: document.getElementById('theme-name').value,
    primaryColor: document.getElementById('primary-color').value,
    secondaryColor: document.getElementById('secondary-color').value,
    textColor: document.getElementById('text-color').value,
    backgroundImage: document.getElementById('background-image').value
  };

  try {
    await saveCustomTheme(formData);
    alert('Theme saved successfully!');
    themeForm.reset();
  } catch (error) {
    alert('Failed to save theme. Please try again.');
  }
});

// Load previously selected theme on page load
document.addEventListener('DOMContentLoaded', async () => {
  await initializeThemes();
  
  const savedThemeId = localStorage.getItem('selectedTheme');
  if (savedThemeId) {
    themeSelect.value = savedThemeId;
    applyTheme(savedThemeId);
  }
});
