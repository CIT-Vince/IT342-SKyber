import React from 'react';
import communityImage from '../assets/communityLogo.png'; // Adjust the path as necessary
import WaveDivider from '../components/WaveDivider';
import fb from '../assets/icons/fb.png';
import tiktok from '../assets/icons/tiktok.png';
import x from '../assets/icons/x.png';
import Navbar from '../components/Navbar'; // Import your Navbar component
import {Link} from 'react-router-dom'; // Import Link for navigation

const Home = () => { 
  return (
    <>
    <Navbar/>
    <section className="content w-full min-h-screen flex items-center justify-center px-2 py-6">
  <div className="max-w-7xl w-full flex flex-col md:flex-row items-center justify-between gap-10">
    {/* Left content */}
    <div className="flex-1 text-center md:text-left">
      <h1 className="text-4xl md:text-5xl font-bold leading-tight text-white mb-6">
        Empowering <span className="text-red-600">Communities</span><br />
        <span className="text-blue-600">Securing</span> the <span className="text-yellow-500">Future.</span>
      </h1>
      <p className="text-gray-600 max-w-md text-white mb-6">
        Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis...
      </p>
      <div className="flex items-center gap-4 mb-6 justify-center md:justify-start">
        {/* Social icons */}
      </div>
      <Link to="/register"className="bg-gradient-to-r from-blue-500 to-cyan-400 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition w-full md:w-auto">
        Get Started
      </Link>
    </div>
    {/* Right image */}
    <div className="flex-1 flex justify-center md:justify-end">
      <img
        src={communityImage}
        alt="Community"
        className="w-full max-w-3xl h-auto object-contain"
      />
    </div>
  </div>
</section>
    </>
  );
};

export default Home;