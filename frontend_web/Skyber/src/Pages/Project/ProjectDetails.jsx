import React from 'react';
import sampleImage from '../../assets/proj/sampleImage.png'; 
import { Link } from 'react-router-dom';
import back from '../../assets/icons/back.png'; 
import bottomBack from '../../assets/icons/bottomBack.png';
import Navbar from '../../components/Navbar';
import Vince from '../../assets/proj/vince.png';
import { Tooltip } from '@mantine/core';

const mockAvatar = "https://i.pravatar.cc/100"; // Mock avatar URL

const AnnouncementPage = () => {
  return (
    <>
    <div
        className="w-full h-auto"
        // style={{
        // background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        // }}
    >
        <Navbar />
    </div>

      {/* MAIN ANNOUNCEMENT */}
    <div className="content bg-white min-h-screen text-gray-900 w-[90%] mx-auto rounded-xl shadow-lg">
        {/* Left arrow */}
        <Link
            to="/projects"
            className="absolute top-6 left-6 z-20 p-2 bg-white rounded-full shadow hover:bg-gray-100 transition"
            >
            <Tooltip label="Back to Projects" position="bottom" withArrow>
                <img src={back} className="w-10 h-10" alt="Back" />
            </Tooltip>
        </Link>

      <main className="max-w-5xl mx-auto px-4 py-10 pt-30 ">

        {/* Fcking Image */}
        <div className="relative h-[400px]">
            {/* Blur  */}
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

            {/*  main image */}
            <div className="relative z-10 flex justify-center items-center h-full">
                <img
                src={sampleImage}
                alt="Main Announcement"
                className="rounded-lg  shadow-lg max-h-[450px] object-cover"
                />
                
            </div>
            
            </div>


        {/* Text Content */}
        <div className="text-center mt-6 pt-20">
          <h2 className="text-3xl font-bold">Project Title</h2>
          <p className="italic text-sm text-gray-700 mt-1"><span className='font-bold text-red-500'>STATUS</span> 
          <span className='font-bold'>  |  </span> Start Date: (03/05/2025)
          <span className='font-bold'>  |  </span>
           End Date:  (05/05/2025) </p>
        </div>

        <hr className="my-4 border-gray-300" />

        <p className="text-justify text-gray-700 max-w-3xl mx-auto leading-relaxed">
          Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. 
          Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. 
          Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. 
          Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.
        </p>
      </main>

        {/* Project Analysis */}
        <section className="bg-white px-4 pb-12 max-w-5xl mx-auto">
            <div className="grid grid-cols-2 md:grid-cols-4 gap-1 mt-12 mb-4 items-center">
                <h3 className="text-2xl font-bold "
                style={{ color: 'oklch(37.9% 0.146 265.522)' }}
                >BUDGET: </h3>
                <h3 className="text-2xl font-bold underline decoration-green-500 decoration-4"> Php 20,000.00</h3>
                <h3 className="text-2xl font-bold "
                style={{ color: 'oklch(37.9% 0.146 265.522)' }}>Sustainability Goal: </h3>
                <h3 className="text-2xl font-bold ">Statements </h3>
            </div>
            <div className="bg-gradient-to-b from-blue-600 to-black text-white p-6 rounded-2xl flex flex-col md:flex-row gap-6 max-w-5xl mx-auto shadow-lg">
                {/* Left Column - Project Manager */}
                <div className="flex-shrink-0 text-center md:text-left">
                    <img
                    src={Vince}
                    alt="Project Manager"
                    className="w-32 h-32 rounded-xl object-cover mx-auto md:mx-0"
                    />
                    <h2 className="text-xl font-bold mt-4">Vince Kimlo Gwapo</h2>
                    <hr className="w-3/4 mx-auto md:mx-0 border-t-2 border-white my-2" />
                    <p className="uppercase text-sm font-semibold">Project Manager</p>
                </div>

                {/* Vertical Divider Nako */}
                <div className="w-px bg-white self-stretch" />

                {/* Right Column - Team Members and Stakeholders */}
                <div className="flex-1">
                    <div className="flex flex-wrap items-start justify-start gap-4">
                    {/* Team Members */}
                    <div className="flex flex-col flex-wrap gap-2">
                    <h3 className="font-semibold">Team Members:</h3>
                    <div className="flex flex-wrap gap-6">
                        {Array.from({ length: 6 }).map((_, index) => (
                        <div key={index} className="flex flex-col items-center">
                            <img
                            src={mockAvatar}
                            alt={`Team Member ${index + 1}`}
                            className="w-25 h-20 rounded-xl object-cover"
                            />
                            <div className="text-xs bg-white text-black mt-1 px-2 py-0.5 rounded-full font-semibold">
                            Team Member
                            </div>
                        </div>
                        ))}
                    </div>
                    </div>

                    {/* Vertical Divider */}
                    <div className="border-l border-white h-full mx-4" />

                    {/* Stakeholders */}
                    <div>
                        <h3 className="font-semibold mb-2">Stakeholders:</h3>
                        <ul className="list-disc list-inside space-y-1">
                        <li>stakeholder one</li>
                        <li>stakeholder two</li>
                        <li>stakeholder three</li>
                        </ul>
                    </div>
                    </div>
                </div>
                </div>

        </section>


        {/* OTHER Projects */}
        <section className="bg-white px-4 pb-12 max-w-5xl mx-auto">
            <div className="flex justify-between items-center mt-12 mb-4">
            <h3 className="text-xl font-bold">Other Projects</h3>
            <Tooltip label="Back to Projects" position="bottom" withArrow>
            <Link to='/projects' className="bg-yellow-400 p-2 rounded-full flex justify-center items-center shadow-md w-30 hover:bg-yellow-300 transition">
            <img src={bottomBack} alt="Other Announcements" className="w-6 h-6 object-cover" />
            </Link>
            </Tooltip>
            </div> 

            {/* Card Preview */}
            <div className="grid grid-cols-1 md:grid-cols-2 gap-4 place-items-center">

            <div className="bg-white border border-gray-200 shadow rounded-xl p-4 flex gap-4 max-w-xl items-center justify-center w-full">
                <img src={sampleImage} alt="Other Announcement" className="w-24 h-24 object-cover rounded-md" />
                <div>
                <p className="italic text-sm text-gray-700 mt-1"><span className='font-bold text-red-500'>STATUS</span> 
                    <span className='font-bold'>  |  </span> Start Date: (03/05/2025)
                    <span className='font-bold'>  |  </span>
                    End Date:  (05/05/2025)
                </p>
                <h4 className="font-semibold text-md mb-1">Project 1</h4>
                <p className="text-gray-600 text-sm">
                    Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.
                </p>
                </div>
                
            </div>

            <div className="bg-white border border-gray-200 shadow rounded-xl p-4 flex gap-4 max-w-xl items-center justify-center w-full">
                    <img src={sampleImage} alt="Other Announcement" className="w-24 h-24 object-cover rounded-md" />
                    <div>
                    <p className="italic text-sm text-gray-700 mt-1"><span className='font-bold text-red-500'>STATUS</span> 
                        <span className='font-bold'>  |  </span> Start Date: (03/05/2025)
                        <span className='font-bold'>  |  </span>
                        End Date:  (05/05/2025)
                    </p>
                    <h4 className="font-semibold text-md mb-1">Project 2</h4>
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
