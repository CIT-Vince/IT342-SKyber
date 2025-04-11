// components/Navbar.jsx

import React from 'react';
import skyberLogo from '../assets/skyber.svg'; // Import your logo here
import { Link } from 'react-router-dom'; // Import Link for routing
const Navbar = () => {
  return (
    <nav className="navbar w-full flex items-center justify-between px-8 py-4">
      <div className="flex items-center space-x-2 text-xl font-bold text-white">
        <img src={skyberLogo} alt="Skyber Logo" className="h-8 w-auto" /> {/* Logo */}
        <span>SKYBER</span>
     </div>

      {/* Nav Links */}
      <ul className="hidden md:flex space-x-8 text-white font-medium">
        <li><Link to="/" href="#home" className="hover:text-blue-600">Home</Link></li>
        <li><Link to="/announcements" className="hover:text-blue-600">Announcements</Link></li>
        <li><a href="#pricing" className="hover:text-blue-600">Events</a></li>
        <li><a href="#about" className="hover:text-blue-600">Reports</a></li>
      </ul>

      {/* CTA Button */}
      <div>
        <Link
          to="/register"
          className="bg-gradient-to-r from-yellow-500 to-yellow-400 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition"
        >
          Sign Up
        </Link>
      </div>
    </nav>
  );
};

export default Navbar;
