import React, { useState, useEffect,useMemo  } from 'react';
import skyberLogo from '../assets/skyber.svg';
import { Link, useNavigate } from 'react-router-dom';
import { Menu } from '@mantine/core';
import { auth } from '../firebase/firebase';
import { signOut } from 'firebase/auth';
import { useAuth } from '../contexts/AuthContext';
import { showNotification } from '@mantine/notifications';
import defaultAvatar from '../assets/default-avatar.jpg'; // Add a default avatar image
import { useLocation } from 'react-router-dom';

const Navbar = ({ lightBackground = false }) => {
  const [menuOpen, setMenuOpen] = useState(false);
  const { currentUser } = useAuth(); 
  const [userData, setUserData] = useState(null);
  const navigate = useNavigate();
  const [scrolled, setScrolled] = useState(false);
  const location = useLocation();
  const { logout } = useAuth();

  useEffect(() => {
    const handleScroll = () => {
      const isScrolled = window.scrollY > 10;
      if (isScrolled !== scrolled) {
        setScrolled(isScrolled);
      }
    };
    
    window.addEventListener('scroll', handleScroll);
    return () => window.removeEventListener('scroll', handleScroll);
  }, [scrolled]);

  useEffect(() => {
    if (currentUser) {
      // Option 1: Get data from Firebase Realtime Database
      const fetchUserData = async () => {
        try {
          const { getDatabase, ref, get } = await import('firebase/database');
          const db = getDatabase();
          const userRef = ref(db, `users/${currentUser.uid}`);
          const snapshot = await get(userRef);
          
          if (snapshot.exists()) {
            setUserData(snapshot.val());
          } else {
            // Fallback to basic Firebase Auth user data
            setUserData({
              firstName: currentUser.displayName?.split(' ')[0] || 'User',
              lastName: currentUser.displayName?.split(' ')[1] || '',
              email: currentUser.email,
              photoURL: currentUser.photoURL
            });
          }
        } catch (error) {
          console.error("Error fetching user data:", error);
        }
      };
      
      fetchUserData();
    } else {
      setUserData(null);
    }
  }, [currentUser]);

  // Use the more thorough logout from context
const handleLogout = async () => {
  try {
    await logout();  // Use this instead of signOut(auth)
    showNotification({
      title: 'Logged Out',
      message: 'You have been successfully logged out',
      color: 'blue',
    });
    navigate('/');
  } catch (error) {
    showNotification({
      title: 'Error',
      message: 'Failed to log out',
      color: 'red',
    });
  }
};
  const isLightBackgroundPage = useMemo(() => {
    const lightBackgroundPaths = [
      '/profile',
      '/sk', 
      '/job',
      '/scholarship',
      '/volunteerHub'
      // Add other paths with light backgrounds
    ];
    
    return lightBackgroundPaths.some(path => 
      location.pathname.startsWith(path)
    );
  }, [location.pathname]);
  
  const getNavBackground = () => {
    if (scrolled) {
      return 'bg-[#0134AA] shadow-lg';
    } else if (lightBackground || isLightBackgroundPage) {
      return 'bg-[#0134AA] bg-opacity-90'; // Blue with opacity when over light backgrounds
    } else {
      return 'bg-transparent'; // Transparent on dark backgrounds
    }
  };
  return (
    <nav
        className={`navbar w-full flex flex-col md:flex-row items-center justify-between px-8 py-4 fixed top-0 left-0 z-50! transition-all duration-300 ${getNavBackground()}`}
        style={{ position: 'fixed', top: 0 }}
      >
      <div className="flex items-center space-x-2 text-xl font-bold text-white">
        <Link to="/" className="flex items-center gap-2" onClick={() => setMenuOpen(false)}>
        <img src={skyberLogo} alt="Skyber Logo" className="h-8 w-auto" />
        <span>SKYBER</span>
        </Link>
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
  } flex-col md:flex md:flex-row gap-2 md:gap-8 text-white font-medium w-full md:w-auto items-center bg-[#0134AA] md:bg-transparent absolute md:static top-16 left-0 md:top-auto md:left-auto z-40 md:z-auto pb-4 pt-2 px-4`}
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

      {/* User Button */}
      <div className={`w-full md:w-auto flex justify-center ${menuOpen ? 'mt-[200px]' : 'mt-2'} md:mt-0`}>

        {!currentUser ? (
          <Link
            to="/login"
            className="bg-gradient-to-r from-yellow-500 to-yellow-400 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition w-full md:w-auto text-center"
          >
            Sign In
          </Link>
        ) : (
          <Menu shadow="md" width={180} withinPortal withArrow>
            <Menu.Target withArrow>
              <button className="flex items-center gap-2 bg-white text-black px-4 py-2 rounded-full shadow hover:scale-105 transition">
                <img 
                  src={userData?.photoURL || defaultAvatar} 
                  alt="avatar" 
                  className="w-8 h-8 rounded-full object-cover"
                  onError={(e) => { e.target.src = defaultAvatar }} 
                />
                <span className="inline truncate max-w-[100px]">
                  {userData ? `${userData.firstName || ''}` : 'Loading...'}
                </span>
              </button>
            </Menu.Target>
            <Menu.Dropdown>
              <Menu.Item>
                <Link to="/profile" className="block w-full h-full">Profile</Link>
              </Menu.Item>
              <Menu.Item onClick={handleLogout}>
                Log out
              </Menu.Item>
            </Menu.Dropdown>
          </Menu>
        )}
      </div>

      {/* Remove the demo toggle button */}
    </nav>
  );
};

export default Navbar;