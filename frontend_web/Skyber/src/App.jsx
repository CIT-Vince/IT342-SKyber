import React, { lazy, Suspense } from 'react';
import { BrowserRouter as Router, Routes, Route, useLocation } from 'react-router-dom';
import './App.css';
import Navbar from './components/Navbar';
import ParticlesComponent from './components/ParticlesComponent';
import WaveDivider from './components/WaveDivider'
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
import ProtectedRoute from './components/ProtectedRoute';



const Home = lazy(() => import('./Pages/Home'));
const Announcements = lazy(() => import('./Pages/Announcement/Announcements'));
const AdminDashboard = lazy(() => import('./Pages/Announcement/AdminDashboard'));


function App() {
  const location = useLocation();
  const { loading, loadingMessage, logoSrc } = useLoading();

  return (
      <div className="min-h-screen relative">
      {/* Show loading screen when loading is true */}
      {/* {loading && (
        <LoadingScreen 
          logoSrc={logoSrc} 
          message={loadingMessage}
        />
      )} */}

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
          
          {/* Pubic Routes Nako */}
            {/* Announcements*/}
            <Route path="/announcements" element={<Announcements />} />
            <Route path="/announcementspage" element={<AnnouncementPractice />} />
            <Route path="/announcements/:id" element={<Announcements />} />
            
            {/* Account */}
            <Route path="/register" element={<Register />} />
            <Route path="/registerFull" element={<RegisterFull />} />
            <Route path="/login" element={<Login />} />

          {/* Private Routes Nako */}

            {/* Projects  */}
            <Route path="/projects" element={<ProtectedRoute><Projects /></ProtectedRoute>} />
            <Route path="/projectDetail" element={<ProtectedRoute><ProjectDetails /></ProtectedRoute>} />

            {/* Others */}
            <Route path="/volunteerHub" element={<ProtectedRoute><VolunteerHub /></ProtectedRoute>} />
            <Route path="/candidates" element={<ProtectedRoute><Candidates /></ProtectedRoute>} />
            <Route path="/scholarship" element={<ProtectedRoute><Scholar /></ProtectedRoute>} />
            <Route path="/job" element={<ProtectedRoute><Job /></ProtectedRoute>} />
            <Route path="/sk" element={<ProtectedRoute><Sk /></ProtectedRoute>} />

            <Route path="/profile" element={<ProtectedRoute><Profile /></ProtectedRoute>} /> 

            {/* Admin */}
            <Route path="/admin" element={<ProtectedRoute><AdminDashboard /></ProtectedRoute>} />

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