import React from 'react';
import skyber from '../../assets/skyber.svg'; // Import your logo here
import { Link } from 'react-router-dom'; // Import Link for routing
import logo from '../../assets/communityLogo.png'; // Import your logo here
import { FcGoogle } from "react-icons/fc";
import { FaFacebook } from "react-icons/fa";

const Register = () => {
  return (
    <div className="content flex min-h-screen">
      {/* Left side - Image and blue background */}
      <div className="hidden lg:flex w-1/2  items-center justify-center relative overflow-hidden">
        <img
          src={logo}  // Replace this with your real image path
          alt="Logo"
          className="max-w-[80%] object-contain"
        />
        {/* Add network lines or background via CSS or SVG here if needed */}
      </div>

      {/* Right side - Form */}
      <div className="w-full lg:w-1/2 flex items-center justify-center px-6 py-10 bg-white relative overflow-hidden">
          {/* Sparkles background */}
          <div className="absolute inset-0 pointer-events-none z-0">
            <div className="absolute top-10 left-10 text-pink-300 text-3xl animate-pulse">🤖</div>
            <div className="absolute bottom-10 right-16 text-blue-300 text-2xl animate-bounce">✨</div>
            <div className="absolute top-1/2 left-1/2 text-purple-300 text-4xl animate-spin">★</div>
          </div>
          <div className="max-w-md w-full space-y-6 bg-white/80 rounded-3xl shadow-2xl p-8 z-10 ">
            {/* Logo */}
            <div className="flex items-center gap-2 mb-2">
              <img src={skyber} alt="Skyber" className="w-10 h-10 drop-shadow" />
              <span className="text-xl font-bold text-[#0033CC] tracking-wide">SKYBER</span>
            </div>

            {/* Heading */}
            <div>
              <h2 className="text-3xl font-bold text-gray-900 flex items-center gap-2">
                Create Account <span className="animate-bounce">💠</span>
              </h2>
              <p className="text-sm text-gray-600">
                Already have an account?{' '}
                <a href="#" className="text-red-500 font-medium hover:underline transition">Log In</a>
              </p>
            </div>

            {/* Big Google Button */}
            <Link to="/registerFull">
            <button className="w-full py-3 bg-gradient-to-r from-pink-400 to-blue-500 text-white font-semibold rounded-full shadow-lg hover:scale-105 hover:opacity-90 transition-all duration-200">
              Continue with Email
            </button>
            </Link>
            {/* Divider */}
            <div className="flex items-center gap-4 text-gray-400">
              <hr className="flex-grow border-gray-300" />
              <span className="text-sm italic text-pink-400">or continue with~</span>
              <hr className="flex-grow border-gray-300" />
            </div>

            {/* OAuth Buttons */}
            <div className="space-y-3">
              <button className="w-full flex items-center py-2 px-4 border border-gray-200 rounded-full bg-white hover:bg-pink-50 transition relative shadow hover:scale-105 duration-150">
                <span className="absolute left-4 flex items-center">
                  <FcGoogle size={22} />
                </span>
                <span className="w-full text-center text-gray-700 font-medium">Continue with Google</span>
              </button>

              <button className="w-full flex items-center py-2 px-4 border border-gray-200 rounded-full bg-white hover:bg-blue-50 transition relative shadow hover:scale-105 duration-150">
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
