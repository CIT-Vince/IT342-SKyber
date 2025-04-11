import React from 'react';
import skyber from '../assets/skyber.svg'; // Import your logo here
import { Link } from 'react-router-dom'; // Import Link for routing
import logo from '../assets/communityLogo.png'; // Import your logo here
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
      <div className="w-full lg:w-1/2 flex items-center justify-center px-6 py-10 bg-[#f9f9f9]">
        <div className="max-w-md w-full space-y-6">
          {/* Logo */}
          <div className="flex items-center gap-2">
            <img src={skyber} alt="Skyber" className="w-10 h-10" />
            <span className="text-xl font-bold text-[#0033CC]">SKYBER</span>
          </div>

          {/* Heading */}
          <div>
            <h2 className="text-3xl font-bold text-gray-900">Create Account</h2>
            <p className="text-sm text-gray-600">
              Already have an account?{' '}
              <a href="#" className="text-red-600 font-medium hover:underline">
                Log In
              </a>
            </p>
          </div>

          {/* Big Google Button */}
          <button className="w-full py-3 bg-gradient-to-r from-blue-500 to-blue-800 text-white font-semibold rounded-xl hover:opacity-90 transition">
            Continue with Google
          </button>

          {/* Divider */}
          <div className="flex items-center gap-4 text-gray-400">
            <hr className="flex-grow border-gray-300" />
            <span className="text-sm">or</span>
            <hr className="flex-grow border-gray-300" />
          </div>

          {/* OAuth Buttons */}
          <div className="space-y-3">
            <button className="w-full flex items-center justify-center gap-3 py-2 px-4 border border-gray-300 rounded-xl bg-white hover:bg-gray-100 transition">
              <img src="/google-icon.svg" alt="Google" className="w-5 h-5" />
              <span className="text-gray-700 font-medium">Continue with Google</span>
            </button>

            <button className="w-full flex items-center justify-center gap-3 py-2 px-4 border border-gray-300 rounded-xl bg-white hover:bg-gray-100 transition">
              <img src="/facebook-icon.svg" alt="Facebook" className="w-5 h-5" />
              <span className="text-gray-700 font-medium">Continue with Facebook</span>
            </button>
          </div>
        </div>
      </div>
    </div>
  );
};

export default Register;
