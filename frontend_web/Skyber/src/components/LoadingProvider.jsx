import React, { createContext, useState, useContext } from 'react';

const LoadingContext = createContext();

export const LoadingProvider = ({ children }) => {
  const [loading, setLoading] = useState(false);
  const [loadingMessage, setLoadingMessage] = useState("Loading... Please Wait (๑˃ᴗ˂)ﻭ");
  const [logoSrc, setLogoSrc] = useState("/logo.png");

  // Show loading with optional custom message and logo
  const showLoading = (message, logo) => {
    if (message) setLoadingMessage(message);
    if (logo) setLogoSrc(logo);
    setLoading(true);
  };

  // Hide loading
  const hideLoading = () => {
    setLoading(false);
  };

  return (
    <LoadingContext.Provider value={{ 
      loading, 
      loadingMessage, 
      logoSrc,
      showLoading, 
      hideLoading 
    }}>
      {children}
    </LoadingContext.Provider>
  );
};

// Custom hook to use the loading context
export const useLoading = () => {
  const context = useContext(LoadingContext);
  if (!context) {
    throw new Error('useLoading must be used within a LoadingProvider');
  }
  return context;
};