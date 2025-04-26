import React from 'react';
import sampleImage from '../../assets/announce/sample1.png'; // Replace with your actual image path
import { Link } from 'react-router-dom';
//import smallAnnouncement from '../assets/announce/sample2.png'; // Replace accordingly
import back from '../../assets/icons/back.png'; 
import bottomBack from '../../assets/icons/bottomBack.png';// Adjust the import path as necessary
import Navbar from '../../components/Navbar';

const AnnouncementPage = () => {
  return (
    <>
    <div
    className="w-full h-auto"
    style={{
      background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
    }}
  >
    <Navbar />
    </div>

      {/* MAIN ANNOUNCEMENT */}
    <div className="content bg-white min-h-screen text-gray-900">
      <main className="max-w-5xl mx-auto px-4 py-10 ">
        {/* Main Image */}
        <div className="relative h-[400px]">
            {/* Blurred background */}
            <div
                className="absolute inset-0 z-0 overflow-hidden rounded-lg"
                style={{
                backgroundImage: `url(${sampleImage})`,
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                filter: 'blur(20px) brightness(0.6)',
                transform: 'scale(1.1)',
                }}
            ></div>

            {/* Foreground main image */}
            <div className="relative z-10 flex justify-center items-center h-full">
                <img
                src={sampleImage}
                alt="Main Announcement"
                className="rounded-lg  shadow-lg "
                />
                {/* Left arrow */}
                <button className="absolute left-4 top-1/2 transform -translate-y-1/2 bg-white rounded-full p-2 shadow z-20">
                <img src={back} className="w-5 h-5 text-black" />
                </button>
            </div>
            </div>


        {/* Text Content */}
        <div className="text-center mt-6 pt-20">
          <h2 className="text-3xl font-bold">Announcement Title</h2>
          <p className="italic text-sm text-gray-700 mt-1">BARANGAY ; CATEGORY ; 04/09/2025</p>
        </div>

        <hr className="my-4 border-gray-300" />

        <p className="text-justify text-gray-700 max-w-3xl mx-auto leading-relaxed">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
          Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
          Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
          Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
        </p>
      </main>

      {/* OTHER ANNOUNCEMENTS */}
      <section className="bg-white px-4 pb-12 max-w-5xl mx-auto">
        <div className="flex justify-between items-center mt-12 mb-4">
          <h3 className="text-xl font-bold">Other Announcements</h3>
          <Link to='/' className="bg-yellow-400 p-2 rounded-full flex justify-center items-center shadow-md w-30 hover:bg-yellow-300 transition">
           <img src={bottomBack} alt="Other Announcements" className="w-6 h-6 object-cover" />
          </Link>
        </div>

        {/* Card Preview */}
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4 place-items-center">
          <div className="bg-white border border-gray-200 shadow rounded-xl p-4 flex gap-4 max-w-xl items-center justify-center w-full">
            <img src={sampleImage} alt="Other Announcement" className="w-24 h-24 object-cover rounded-md" />
            <div>
              <p className="text-red-500 text-xs font-semibold">STATUS |  04/09/2025</p>
              <h4 className="font-semibold text-md mb-1">Sangguniang kabataan announcement 1</h4>
              <p className="text-gray-600 text-sm">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.
              </p>
            </div>
          </div>
          <div className="bg-white border border-gray-200 shadow rounded-xl p-4 flex gap-4 max-w-xl items-center justify-center w-full">
            <img src={sampleImage} alt="Other Announcement" className="w-24 h-24 object-cover rounded-md" />
            <div>
              <p className="text-red-500 text-xs font-semibold">CATEGORY | 04/09/2025</p>
              <h4 className="font-semibold text-md mb-1">Sangguniang kabataan announcement 1</h4>
              <p className="text-gray-600 text-sm">
                Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.
              </p>
            </div>
          </div>
        </div>
      </section>
    </div>
    </>
  );
};

export default AnnouncementPage;
