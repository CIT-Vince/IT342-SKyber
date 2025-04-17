import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import sample1 from '../../assets/proj/sample1.png';
import sample2 from '../../assets/proj/sample2.png';
import sample3 from '../../assets/proj/sample3.png';
import { Link } from 'react-router-dom';

const announcements = [
  {
    id: 1,
    title: 'Project Name 1',
    category: 'STATUS',
    startDate: '04/09/2025',
    endDate: '05/09/2025',
    image: sample1,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.',
  },
  {
    id: 2,
    title: 'Project Name 2',
    category: 'STATUS',
    startDate: '04/10/2025',
    endDate: '05/10/2025',
    image: sample2,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.',
  },
  {
    id: 3,
    title: 'Project Name 3',
    category: 'STATUS',
    startDate: '04/11/2025',
    endDate: '05/11/2025',
    image: sample3,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.',
  },
];

const Projects = () => {

  return (
    <>
      {/* Header */}
      <div
        className="w-full h-auto"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
        <Navbar />
        <header className="text-left py-10 pl-10">
          <h1 className="text-5xl font-bold text-white">Projects </h1>
          <p className="text-white">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.
          </p>
        </header>
      </div>

      {/* Main content section */}
      <div className="w-full h-2 bg-white"></div>
      <section className="w-full min-h-screen bg-white pt-8 shadow-lg relative">
        {/* Filter buttons */}
        <div className="flex justify-center gap-4 flex-wrap mb-10">
          {['All', 'Complete', 'Pending', 'Closed'].map((label) => (
            <button
              key={label}
              className="bg-blue-500 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition"
            >
              {label}
            </button>
          ))}
        </div>

        {/* Cards */}
        <div className="content grid md:grid-cols-3 gap-6 max-w-7xl mx-auto">
          {announcements.map((item) => (
            <Link
              to="/projectDetail"
              key={item.id}
              className="rounded-xl overflow-hidden bg-white shadow-md border border-gray-100 cursor-pointer hover:shadow-lg transition"
              onClick={() => handleCardClick(item)}
            >
              <img src={item.image} alt={item.title} className="w-full h-48 object-cover" />
              <div className="p-4">
                <p className="text-red-500 font-semibold text-sm">
                  {item.category} <span className="text-gray-400">| Start Date: {item.startDate} | End Date:  {item.endDate} </span>
                </p>
                <h3 className="font-semibold text-md mt-2 mb-1">{item.title}</h3>
                <p className="text-gray-600 text-sm">{item.description}</p>
              </div>
            </Link>
          ))}
        </div>

        
      </section>
    </>
  );
};

export default Projects;