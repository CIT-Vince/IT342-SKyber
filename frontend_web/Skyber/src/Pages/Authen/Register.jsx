import React from 'react';
import skyber from '../../assets/skyber.svg';
import { Link, useNavigate } from 'react-router-dom'; // Add useNavigate
import logo from '../../assets/communityLogo.png';
import { FcGoogle } from "react-icons/fc";
import { FaFacebook } from "react-icons/fa";
import { GoogleAuthProvider, signInWithPopup } from 'firebase/auth'; // Add these imports
import { auth } from '../../firebase/firebase';
import { getDatabase, ref, get, set } from 'firebase/database'; // Add database imports
import { showNotification } from '@mantine/notifications';

const Register = () => {
  const navigate = useNavigate(); // Add this hook
  
  // Add this function to handle Google sign-in
  const handleGoogleSignIn = async () => {
    try {
      const provider = new GoogleAuthProvider();
      const result = await signInWithPopup(auth, provider);
      
      // Get the user from the result
      const user = result.user;
      
      // Check if this user already exists in your Realtime Database
      const database = getDatabase();
      const userRef = ref(database, `users/${user.uid}`);
      const snapshot = await get(userRef);
      
      if (!snapshot.exists()) {
        // This is a new user, save their info to Firebase Realtime Database
        await set(ref(database, 'users/' + user.uid), {
          firstName: user.displayName?.split(' ')[0] || '',
          lastName: user.displayName?.split(' ')[1] || '',
          email: user.email,
          photoURL: user.photoURL,
          birthdate: '',
          gender: '',
          phoneNumber: user.phoneNumber || '',
          address: '',
          role: 'USER',
          lastUpdated: new Date().toISOString()
        });
        
        // Also save to Spring Boot backend if needed
        const idToken = await user.getIdToken();
        
        try {
          const response = await fetch('http://localhost:8080/api/users/register', {
            method: 'POST',
            headers: {
              'Content-Type': 'application/json',
              'Authorization': `Bearer ${idToken}`
            },
            body: JSON.stringify({
              uid: user.uid,
              firstName: user.displayName?.split(' ')[0] || '',
              lastName: user.displayName?.split(' ')[1] || '',
              email: user.email,
              role: 'USER'
            })
          });
          
          if (!response.ok) {
            console.warn('Backend registration had issues, but continuing with Firebase auth');
          }
        } catch (backendError) {
          console.error('Backend registration error:', backendError);
          // Continue anyway since Firebase Auth succeeded
        }
      }
      
      showNotification({
        title: 'Success!',
        message: 'You have successfully signed in with Google',
        color: 'green',
        position: 'top-left'
      });
      
      // Redirect to home page or dashboard
      navigate('/');
      
    } catch (error) {
      console.error('Google sign-in error:', error);
      
      showNotification({
        title: 'Sign-in Failed',
        message: error.message || 'Could not sign in with Google',
        color: 'red',
        position: 'top-left'
      });
    }
  };
  
  // Add Facebook handler placeholder (just shows notification for now)
  const handleFacebookSignIn = () => {
    showNotification({
      title: 'Coming Soon',
      message: 'Facebook login will be available soon!',
      color: 'blue',
      position: 'top-left'
    });
  };

  return (
    <div className="content flex min-h-screen">
      {/* Left side - same as before */}
      <div className="hidden lg:flex w-1/2 items-center justify-center relative overflow-hidden">
        <img
          src={logo}
          alt="Logo"
          className="max-w-[80%] object-contain"
        />
      </div>

      {/* Right side - Form */}
      <div className="w-full lg:w-1/2 flex items-center justify-center px-6 py-10 bg-white relative overflow-hidden">
          {/* Sparkles background - same as before */}
          <div className="absolute inset-0 pointer-events-none z-0">
            <div className="absolute top-10 left-10 text-pink-300 text-3xl animate-pulse">🤖</div>
            <div className="absolute bottom-10 right-16 text-blue-300 text-2xl animate-bounce">✨</div>
            <div className="absolute top-1/2 left-1/2 text-purple-300 text-4xl animate-spin">★</div>
          </div>
          <div className="max-w-md w-full space-y-6 bg-white/80 rounded-3xl shadow-2xl p-8 z-10 ">
            {/* Logo - same as before */}
            <div className="flex items-center gap-2 mb-2">
              <img src={skyber} alt="Skyber" className="w-10 h-10 drop-shadow" />
              <span className="text-xl font-bold text-[#0033CC] tracking-wide">SKYBER</span>
            </div>

            {/* Heading - updated link to use React Router */}
            <div>
              <h2 className="text-3xl font-bold text-gray-900 flex items-center gap-2">
                Create Account <span className="animate-bounce">💠</span>
              </h2>
              <p className="text-sm text-gray-600">
                Already have an account?{' '}
                <Link to="/login" className="text-red-500 font-medium hover:underline transition">Log In</Link>
              </p>
            </div>

            {/* Email button - same as before */}
            <Link to="/registerFull">
              <button className="w-full py-3 bg-gradient-to-r from-cyan-500 to-blue-500 text-white font-semibold rounded-full shadow-lg hover:scale-105 hover:opacity-90 transition-all duration-200">
                Continue with Email
              </button>
            </Link>
            
            {/* Divider - same as before */}
            <div className="flex items-center gap-4 text-gray-400">
              <hr className="flex-grow border-gray-300" />
              <span className="text-sm italic text-cyan-700">or continue with~</span>
              <hr className="flex-grow border-gray-300" />
            </div>

            {/* OAuth Buttons - now with click handlers */}
            <div className="space-y-3">
              <button 
                className="w-full cursor-pointer flex items-center py-2 px-4 border border-gray-200 rounded-full bg-white hover:bg-pink-50 transition relative shadow hover:scale-105 duration-150"
                onClick={handleGoogleSignIn} // Add click handler here
              >
                <span className="absolute left-4 flex items-center">
                  <FcGoogle size={22} />
                </span>
                <span className="w-full text-center text-gray-700 font-medium">Continue with Google</span>
              </button>

              <button 
                className="w-full cursor-pointer flex items-center py-2 px-4 border border-gray-200 rounded-full bg-white hover:bg-blue-50 transition relative shadow hover:scale-105 duration-150"
                onClick={handleFacebookSignIn} // Add placeholder handler
              >
                <span className="absolute left-4 flex items-center">
                  <FaFacebook className="text-blue-600" size={22} />
                </span>
                <span className="w-full text-center text-gray-700 font-medium">Continue with Facebook</span>
              </button>
            </div>
          </div>
        </div>
    </div>
  );
};

export default Register;