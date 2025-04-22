import React, { useState } from 'react';
import skyberLogo from '../assets/skyber.svg';
import { Link } from 'react-router-dom';
import { Menu } from '@mantine/core';

const Navbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);
  const [isLoggedIn, setIsLoggedIn] = useState(false);

  // Kawaii mock user data!
  const mockUser = {
    name: "Jane-chan",
    avatar: "https://i.pravatar.cc/100"
  };

  return (
    <nav className="navbar w-full flex flex-col md:flex-row items-center justify-between px-8 py-4 bg-transparent gap-4 relative">
      <div className="flex items-center space-x-2 text-xl font-bold text-white">
        <img src={skyberLogo} alt="Skyber Logo" className="h-8 w-auto" />
        <span>SKYBER</span>
      </div>

      {/* Hamburger Icon */}
      <button
        className="md:hidden absolute right-8 top-6 text-white"
        onClick={() => setMenuOpen(!menuOpen)}
        aria-label="Toggle menu"
      >
        {menuOpen ? (
          <span className="text-3xl">&times;</span>
        ) : (
          <span className="text-3xl">&#9776;</span>
        )}
      </button>

      {/* Nav Links */}
      <ul
        className={`${
          menuOpen ? 'flex' : 'hidden'
        } flex-col md:flex md:flex-row gap-2 md:gap-8 text-white font-medium w-full md:w-auto items-center bg-[#0134AA] md:bg-transparent absolute md:static top-16 left-0 md:top-auto md:left-auto z-40 md:z-auto`}
      >
        <li><Link to="/" className="hover:text-blue-600" onClick={() => setMenuOpen(false)}>Home</Link></li>
        <li><Link to="/announcements" className="hover:text-blue-600" onClick={() => setMenuOpen(false)}>Announcements</Link></li>
        <li><Link to="/projects" className="hover:text-blue-600" onClick={() => setMenuOpen(false)}>Projects</Link></li>
        <li>
          <Menu  shadow="md" width={180} withinPortal withArrow> 
            <Menu.Target>
              <button
                className="hover:text-blue-600 bg-transparent text-white px-2 py-1 cursor-pointer rounded"
                style={{ position: 'relative', zIndex: 50 }}
              >
                Others
              </button>
            </Menu.Target>
            <Menu.Dropdown style={{ zIndex: 9999 }}>
              <Menu.Item>
                <Link to="/candidates" className="block w-full h-full">Candidates</Link>
              </Menu.Item>
              <Menu.Item>
                <Link to="/volunteerHub" className="block w-full h-full">Volunteer Hub</Link>
              </Menu.Item>
              <Menu.Item>
                <Link to="/scholarship" className="block w-full h-full">Scholarships</Link>
              </Menu.Item>
              <Menu.Item>
                <Link to="/job" className="block w-full h-full">Job Listings</Link>
              </Menu.Item>
              <Menu.Item>
                <Link to="/sk" className="block w-full h-full">SK Profile</Link>
              </Menu.Item>
            </Menu.Dropdown>
          </Menu>
        </li>
      </ul>

      {/* Kawaii CTA/User Button */}
      <div className="w-full md:w-auto flex justify-center mt-2 md:mt-0">
        {!isLoggedIn ? (
          <Link
            to="/register"
            className="bg-gradient-to-r from-yellow-500 to-yellow-400 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition w-full md:w-auto text-center"
          >
            Sign Up
          </Link>
        ) : (
          <Menu shadow="md" width={180} withinPortal withArrow>
            <Menu.Target withArrow>
              <button className="flex items-center gap-2 bg-white text-black px-4 py-2 rounded-full shadow hover:scale-105 transition">
                <img src={mockUser.avatar} alt="avatar" className="w-8 h-8 rounded-full" />
                <span className="hidden md:inline">{mockUser.name}</span>
              </button>
            </Menu.Target>
            <Menu.Dropdown>
              <Menu.Item>
                <Link to="/profile" className="block w-full h-full">Profile</Link>
              </Menu.Item>
              <Menu.Item onClick={() => setIsLoggedIn(false)}>
                Log out
              </Menu.Item>
            </Menu.Dropdown>
          </Menu>
        )}
      </div>

      {/* Kawaii toggle for demo~! */}
      <button
        onClick={() => setIsLoggedIn((prev) => !prev)}
        className="absolute top-2 right-2 bg-pink-400 text-white px-2 py-1 rounded text-xs shadow"
      >
        {isLoggedIn ? "Log out (uwu)" : "Log in (owo)"}
      </button>
    </nav>
  );
};

export default Navbar;