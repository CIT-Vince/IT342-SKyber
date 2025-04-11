import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import './App.css';
import Navbar from './components/NavBar';
import ParticlesComponent from './components/ParticlesComponent';
import WaveDivider from './components/WaveDivider'
import Home from './Pages/Home';
import Announcements from './Pages/Announcements';
import Register from './Pages/Register';

function App() {
  const location = useLocation();
  return (
      <div className="min-h-screen relative">
        {/* Persistent components */}
        <ParticlesComponent />
        {/* <Navbar />
        <WaveDivider /> */}

        {/* Conditionally render WaveDivider */}
      {location.pathname !== '/register' && <Navbar />}
        {location.pathname === '/' && <WaveDivider />}

        {/* Routes */}
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/announcements" element={<Announcements />} />
          <Route path="/register" element={<Register />} />
        </Routes>
      </div>
  );
}
const AppWrapper = () => (
  <Router>
    <App />
  </Router>
);

export default AppWrapper;