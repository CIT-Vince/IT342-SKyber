import React, { useState } from 'react';
import skyberLogo from '../assets/skyber.svg';
import { Link } from 'react-router-dom';
import { Menu } from '@mantine/core';

const Navbar = () => {
  const [menuOpen, setMenuOpen] = useState(false);

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
          <span className="text-3xl">&times;</span> // X icon
        ) : (
          <span className="text-3xl">&#9776;</span> // Hamburger icon
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
        <li><a href="#pricing" className="hover:text-blue-600" onClick={() => setMenuOpen(false)}>Events</a></li>
        <li>
          <Menu shadow="md" width={180} withinPortal>
            <Menu.Target>
              <button
                className="hover:text-blue-600 bg-transparent text-white px-2 py-1 rounded"
                style={{ position: 'relative', zIndex: 50 }}
              >
                Others
              </button>
            </Menu.Target>
            <Menu.Dropdown style={{ zIndex: 9999 }}>
              <Menu.Item>
                <Link to="/projects" className="block w-full h-full">Projects</Link>
              </Menu.Item>
              <Menu.Item>
                <Link to="/other2" className="block w-full h-full">Other Link 2</Link>
              </Menu.Item>
            </Menu.Dropdown>
          </Menu>
        </li>
      </ul>

      {/* CTA Button */}
      <div className="w-full md:w-auto flex justify-center mt-2 md:mt-0">
        <Link
          to="/register"
          className="bg-gradient-to-r from-yellow-500 to-yellow-400 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition w-full md:w-auto text-center"
        >
          Sign Up
        </Link>
      </div>
    </nav>
  );
};

export default Navbar;