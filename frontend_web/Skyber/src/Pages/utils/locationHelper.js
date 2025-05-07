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
    return sessionStorage.getItem('lastLocation') || '/announcements';
  };