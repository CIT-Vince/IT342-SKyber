import React, { useState } from 'react';
import skyber from '../../assets/skyber.svg'; 
import { Link, useNavigate } from 'react-router-dom'; 
import logo from '../../assets/communityLogo.png'; 
import { AiOutlineEye, AiOutlineEyeInvisible } from 'react-icons/ai';
import { FcGoogle } from "react-icons/fc"; // Add Google icon
import { signInWithEmailAndPassword, GoogleAuthProvider, signInWithPopup } from 'firebase/auth'; // Add Google auth imports
import { auth } from '../../firebase/firebase';
import { getDatabase, ref, get, set } from 'firebase/database'; // Add database imports
import { showNotification } from '@mantine/notifications';

const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [loading, setLoading] = useState(false);
  const navigate = useNavigate();

  // Email/password login - existing code
  const handleLogin = async (e) => {
    e.preventDefault();
    
    if (!email || !password) {
      showNotification({
        title: 'Error',
        message: 'Please fill in all fields',
        color: 'red',
        position: 'top-left'
      });
      return;
    }

    try {
      setLoading(true);
      const userCredential = await signInWithEmailAndPassword(auth, email, password);
      const user = userCredential.user;

      // Save user token if "remember me" is checked
      if (rememberMe) {
        localStorage.setItem('userToken', await user.getIdToken());
      } else {
        sessionStorage.setItem('userToken', await user.getIdToken());
      }

      showNotification({
        title: 'Success',
        message: 'Login successful! Redirecting...',
        color: 'green',
        position: 'top-left'
      });

      // Redirect to home page after successful login
      navigate('/');
    } catch (error) {
      console.error('Login error:', error.message);
      
      let errorMessage = 'Login failed';
      if (error.code === 'auth/user-not-found' || error.code === 'auth/wrong-password') {
        errorMessage = 'Invalid email or password';
      } else if (error.code === 'auth/too-many-requests') {
        errorMessage = 'Too many failed login attempts. Please try again later';
      }
      
      showNotification({
        title: 'Error',
        message: errorMessage,
        color: 'red',
        position: 'top-left'
      });
    } finally {
      setLoading(false);
    }
  };

  // New Google sign-in handler
  const handleGoogleSignIn = async () => {
    try {
      setLoading(true);
      const provider = new GoogleAuthProvider();
      const result = await signInWithPopup(auth, provider);
      
      // Get the user from the result
      const user = result.user;
      
      // Check if this user already exists in your Realtime Database
      const database = getDatabase();
      const userRef = ref(database, `users/${user.uid}`);
      const snapshot = await get(userRef);
      
      if (!snapshot.exists()) {
        // This is a new Google user, automatically register them
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
        
        // Save to Spring Boot backend
        try {
          const idToken = await user.getIdToken();
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
        } catch (backendError) {
          console.error('Backend registration error:', backendError);
          // Continue anyway since Firebase Auth succeeded
        }
        
        showNotification({
          title: 'Account Created!',
          message: 'Welcome to Skyber! Your account has been created automatically.',
          color: 'green',
          position: 'top-left'
        });
      } else {
        // Existing user, show login success
        showNotification({
          title: 'Welcome Back!',
          message: 'You have successfully signed in with Google',
          color: 'green',
          position: 'top-left'
        });
      }
      
      // Handle remember me for Google sign-in
      if (rememberMe) {
        localStorage.setItem('userToken', await user.getIdToken());
      } else {
        sessionStorage.setItem('userToken', await user.getIdToken());
      }
      
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
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="content flex min-h-screen">
      {/* Left side - Image and blue background */}
      
      <div className="w-full lg:w-1/2 flex items-center justify-center px-6 py-10 bg-white relative overflow-hidden">
        <div className="absolute inset-0 pointer-events-none z-0">
          <div className="absolute top-10 left-10 text-blue-300 text-3xl animate-pulse">💠</div>
          <div className="absolute bottom-10 right-16 text-blue-300 text-2xl animate-bounce">✨</div>
        </div>
        <div className="max-w-md w-full space-y-6 bg-white/80 rounded-3xl shadow-2xl p-8 z-10 ">
          {/* Logo */}
          <div className="flex items-center gap-2 mb-2">
            <Link to="/" className="flex items-center gap-2">
            <img src={skyber} alt="Skyber" className="w-10 h-10 drop-shadow" />
            <span className="text-xl font-bold text-[#0033CC] tracking-wide">SKYBER</span>
            </Link>
          </div>

          {/* Heading */}
          <div>
            <h2 className="flex text-3xl font-bold text-gray-900">
              Sign In<span className="animate-bounce">💠</span>
            </h2>
            <p className="text-sm text-gray-600 mt-1">
              Don't have an account?{' '}
              <Link to="/register" className="text-red-500 font-medium hover:underline transition">
                Create now
              </Link>
            </p>
          </div>

          {/* Login Form */}
          <form className="space-y-5" onSubmit={handleLogin}>
            {/* Email Field */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                E-mail
              </label>
              <input 
                type="email" 
                value={email}
                onChange={(e) => setEmail(e.target.value)}
                className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                placeholder="example@gmail.com"
              />
            </div>
            
            {/* Password Field */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">
                Password
              </label>
              <div className="relative">
                <input 
                  type={showPassword ? "text" : "password"} 
                  value={password}
                  onChange={(e) => setPassword(e.target.value)}
                  className="w-full px-3 py-2 border border-gray-300 rounded-md focus:outline-none focus:ring-2 focus:ring-blue-500"
                  placeholder="@#*%"
                />
                <button 
                  type="button"
                  onClick={() => setShowPassword(!showPassword)}
                  className="absolute right-2 top-1/2 transform -translate-y-1/2 text-gray-500"
                >
                  {showPassword ? <AiOutlineEyeInvisible size={20} /> : <AiOutlineEye size={20} />}
                </button>
              </div>
            </div>

            {/* Remember Me & Forgot Password */}
            <div className="flex items-center justify-between">
              <div className="flex items-center">
                <input 
                  type="checkbox" 
                  id="remember_me"
                  checked={rememberMe}
                  onChange={() => setRememberMe(!rememberMe)}
                  className="h-4 w-4 text-blue-600 focus:ring-blue-500 border-gray-300 rounded"
                />
                <label htmlFor="remember_me" className="ml-2 block text-sm text-gray-700">
                  Remember me
                </label>
              </div>
              <Link to="/forgot-password" className="text-sm text-red-500 hover:underline">
                Forgot Password?
              </Link>
            </div>

            {/* Sign In Button */}
            <button 
              type="submit" 
              className="w-full py-3 bg-gradient-to-r from-cyan-300 to-blue-500 text-white font-semibold rounded-full shadow-lg hover:scale-105 hover:opacity-90 transition-all duration-200 cursor-pointer"
              disabled={loading}
            >
              {loading ? 'Signing in...' : 'Sign in'}
            </button>
          </form>
          
          {/* Divider */}
          <div className="flex items-center gap-4 text-gray-400">
            <hr className="flex-grow border-gray-300" />
            <span className="text-sm italic text-cyan-700">or sign in with</span>
            <hr className="flex-grow border-gray-300" />
          </div>
          
          {/* Google Sign In Button */}
          <button 
            type="button"
            onClick={handleGoogleSignIn}
            disabled={loading}
            className="w-full  cursor-pointer flex items-center py-2 px-4 border border-gray-200 rounded-full bg-white hover:bg-pink-50 transition relative shadow hover:scale-105 duration-150"
          >
            <span className="absolute left-4 flex items-center">
              <FcGoogle size={22} />
            </span>
            <span className="w-full text-center text-gray-700 font-medium">Continue with Google</span>
          </button>
        </div>
      </div>

      {/* Right side - Form */}
      <div className="hidden lg:flex w-1/2 items-center justify-center relative overflow-hidden ">
        {/* Network background pattern */}
        <div className="absolute inset-0 opacity-30">
          <svg className="w-full h-full" xmlns="http://www.w3.org/2000/svg">
            {/* You could add an actual SVG pattern here */}
          </svg>
        </div>
        <img
          src={logo}
          alt="Logo"
          className="max-w-[70%] object-contain relative z-10"
        />
      </div>
    </div>
  );
};

export default Login;