export const apiFetch = (endpoint, options = {}) => {
    const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'https://it342-skyber.onrender.com';
    
    // Remove leading slash if present in endpoint
    const normalizedEndpoint = endpoint.startsWith('/') ? endpoint.substring(1) : endpoint;
    
    return fetch(`${API_BASE_URL}/${normalizedEndpoint}`, options);
  };