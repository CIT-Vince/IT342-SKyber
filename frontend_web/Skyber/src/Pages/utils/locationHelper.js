// Store last visited non-auth page
export const saveLastLocation = (location) => {
    // Don't store auth-related pages
    const authPages = ['/login', '/register', '/forgot-password'];
    if (!authPages.includes(location.pathname)) {
      sessionStorage.setItem('lastLocation', location.pathname);
    }
  };
  
  // Get the stored location or default to announcements
  export const getRedirectLocation = () => {
    // Check if there's a saved location in sessionStorage
    const savedLocation = sessionStorage.getItem('redirectAfterLogin');
    
    if (savedLocation) {
      // Clear it so it's used only once
      sessionStorage.removeItem('redirectAfterLogin');
      return savedLocation;
    }
    
    // Default to home if no saved location
    return '/';
  };