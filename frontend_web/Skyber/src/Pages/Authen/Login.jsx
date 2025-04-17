import React, { useState } from 'react';
import skyber from '../../assets/skyber.svg'; 
import { Link } from 'react-router-dom'; 
import logo from '../../assets/communityLogo.png'; 
import { AiOutlineEye, AiOutlineEyeInvisible } from 'react-icons/ai';

const Login = () => {
  const [showPassword, setShowPassword] = useState(false);
  const [rememberMe, setRememberMe] = useState(false);
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');

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
            <img src={skyber} alt="Skyber" className="w-10 h-10 drop-shadow" />
            <span className="text-xl font-bold text-[#0033CC] tracking-wide">SKYBER</span>
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
          <form className="space-y-5">
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
            <Link to="/">
            <button 
              type="submit" 
              className="w-full py-3 bg-gradient-to-r from-pink-400 to-blue-500 text-white font-semibold rounded-full shadow-lg hover:scale-105 hover:opacity-90 transition-all duration-200 cursor-pointer"
            >
              Sign in
            </button>
            </Link>
          </form>
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