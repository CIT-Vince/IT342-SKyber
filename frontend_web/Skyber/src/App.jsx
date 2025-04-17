import React from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import './App.css';
import Navbar from './components/Navbar';
import ParticlesComponent from './components/ParticlesComponent';
import WaveDivider from './components/WaveDivider'
import Home from './Pages/Home';
import Announcements from './Pages/Announcement/Announcements';
import AnnouncementDetails from './Pages/Announcement/AnnouncmentDetails';
import AnnouncementPractice from './Pages/Announcement/AnnouncementPractice';
import Projects from './Pages/Project/Projects';
import ProjectDetails from './Pages/Project/ProjectDetails';
import Register from './Pages/Authen/Register';
import Login from './Pages/Authen/Login';
import RegisterFull from './Pages/Authen/RegisterFull';

function App() {
  const location = useLocation();
  return (
      <div className="min-h-screen relative">
        {/* Persistent components */}
        <ParticlesComponent />
        {/* <Navbar />
        <WaveDivider /> */}

        {/* Conditionally render WaveDivider */}
        {location.pathname === '/' && <WaveDivider />}
        

        {/* Routes */}
        <Routes>
          <Route path="/" element={<Home />} />
          
          {/* Announcements*/}
          <Route path="/announcements" element={<Announcements />} />
          <Route path="/announcementspage" element={<AnnouncementPractice />} />
          
          {/* Account */}
          <Route path="/register" element={<Register />} />
          <Route path="/registerFull" element={<RegisterFull />} />
          <Route path="/login" element={<Login />} />

          {/* Projects */}
          <Route path="/projects" element={<Projects />} />
          <Route path="/projectDetail" element={<ProjectDetails />} />

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