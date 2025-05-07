import React, { createContext, useContext, useState, useEffect } from 'react';
import { auth } from '../firebase/firebase';
import { 
  onAuthStateChanged, 
  updateProfile as firebaseUpdateProfile,
  signOut,
  setPersistence,
  browserSessionPersistence ,
  browserLocalPersistence
} from 'firebase/auth';

const AuthContext = createContext();

export function useAuth() {
  return useContext(AuthContext);
}

export function AuthProvider({ children }) {
  const [currentUser, setCurrentUser] = useState(null);
  const [loading, setLoading] = useState(true);

  // Set session persistence when component mounts
  useEffect(() => {
    // This ensures login state is not persisted across browser sessions
    setPersistence(auth, browserLocalPersistence)
      .catch(error => console.error("Error setting auth persistence:", error));
  }, []);

  useEffect(() => {
    const unsubscribe = onAuthStateChanged(auth, (user) => {
      setCurrentUser(user);
      setLoading(false);
    });

    return unsubscribe;
  }, []);
  
  const updateProfile = async (displayName, photoURL) => {
    if (!currentUser) return;
    
    try {
      await firebaseUpdateProfile(currentUser, { 
        displayName,
        photoURL
      });
      
      // Force refresh
      setCurrentUser({...currentUser});
    } catch (error) {
      console.error("Error updating profile:", error);
      throw error;
    }
  };
  
  // Add logout function
  const logout = async () => {
    try {
      await signOut(auth);
      // Clear any local tokens that might be cached
      localStorage.removeItem('firebase:authUser:' + auth.config.apiKey + ':[DEFAULT]');
      sessionStorage.removeItem('firebase:authUser:' + auth.config.apiKey + ':[DEFAULT]');
    } catch (error) {
      console.error("Error signing out:", error);
      throw error;
    }
  };

  const value = {
    currentUser,
    loading,
    updateProfile,
    logout
  };

  return (
    <AuthContext.Provider value={value}>
      {!loading && children}
    </AuthContext.Provider>
  );
}