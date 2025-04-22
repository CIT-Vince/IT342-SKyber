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
import Profile from './Pages/Profile/Profile';
import VolunteerHub from './Pages/Project/VolunteerHub';
import Candidates from './Pages/Project/Candidates';
import Scholar from './Pages/Project/Scholarship';
import Job from './Pages/Project/Job';
import Sk from './Pages/Project/Sk.Profile';
import LoadingScreen from './components/Loading';
import { LoadingProvider, useLoading  } from './components/LoadingProvider';

function App() {
  const location = useLocation();
  const { loading, loadingMessage, logoSrc } = useLoading();

  return (
      <div className="min-h-screen relative">
      {/* Show loading screen when loading is true */}
      {loading && (
        <LoadingScreen 
          logoSrc={logoSrc} 
          message={loadingMessage}
        />
      )}

        {/* Persistent components */}
        <ParticlesComponent />
        {/* <Navbar />
        <WaveDivider /> */}

        {/* Conditionally render WaveDivider */}
        {location.pathname === '/' && <WaveDivider />}
        {location.pathname === '/profile' && <WaveDivider />}
        

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

          {/* Projects  */}
          <Route path="/projects" element={<Projects />} />
          <Route path="/projectDetail" element={<ProjectDetails />} />

          {/* Others */}
          <Route path="/volunteerHub" element={<VolunteerHub />} />
          <Route path="/candidates" element={<Candidates />} />
          <Route path="/scholarship" element={<Scholar />} />
          <Route path="/job" element={<Job />} />
          <Route path="/sk" element={<Sk />} />



          <Route path="/profile" element={<Profile />} />
        </Routes>
      </div>
  );
}
const AppWrapper = () => (
  <Router>
    <LoadingProvider>
    <App />
    </LoadingProvider>
  </Router>
);

export default AppWrapper;