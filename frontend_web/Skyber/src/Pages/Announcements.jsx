import React, { useState } from 'react';
import AnnouncementModal from './AnnouncementModal';
import sample1 from '../assets/announce/sample1.png';
import sample2 from '../assets/announce/sample2.png';
import sample3 from '../assets/announce/sample3.png';

const announcements = [
  {
    id: 1,
    title: 'Sangguniang kabataan announcement 1',
    category: 'CATEGORY',
    date: '04/09/2025',
    image: sample1,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.',
  },
  {
    id: 2,
    title: 'Sangguniang kabataan announcement 1',
    category: 'CATEGORY',
    date: '04/09/2025',
    image: sample2,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.',
  },
  {
    id: 3,
    title: 'Sangguniang kabataan announcement 1',
    category: 'CATEGORY',
    date: '04/09/2025',
    image: sample3,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.',
  },
];

const Announcements = () => {
  const [selectedAnnouncement, setSelectedAnnouncement] = useState(null);

  return (
    <section className=" py-12 px-4">
      {/* Header */}
      <div className="text-center mb-8">
        <h1 className="text-4xl font-bold text-white">Announcements</h1>
        <p className="text-white">Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit venenatis.</p>
      </div>

      {/* Filter buttons */}
      <div className="flex justify-center gap-4 flex-wrap mb-10">
        {['All', 'Utilities', 'Community', 'Register'].map((label) => (
          <button
            key={label}
            className="bg-gradient-to-r from-blue-500 to-blue-700 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition"
          >
            {label}
          </button>
        ))}
      </div>

      {/* Cards */}
      <div className="grid md:grid-cols-3 gap-6 max-w-6xl mx-auto">
        {announcements.map((item) => (
          <div
            key={item.id}
            className="rounded-xl overflow-hidden bg-white shadow-md border border-gray-100 cursor-pointer hover:shadow-lg transition"
            onClick={() => setSelectedAnnouncement(item)}
          >
            <img src={item.image} alt={item.title} className="w-full h-48 object-cover" />
            <div className="p-4">
              <p className="text-red-500 font-semibold text-sm">
                {item.category} <span className="text-gray-400">| {item.date}</span>
              </p>
              <h3 className="font-semibold text-md mt-2 mb-1">{item.title}</h3>
              <p className="text-gray-600 text-sm">{item.description}</p>
            </div>
          </div>
        ))}
      </div>

      {/* Modal */}
      {selectedAnnouncement && (
        <AnnouncementModal
          announcement={selectedAnnouncement}
          onClose={() => setSelectedAnnouncement(null)}
        />
      )}
    </section>
  );
};

export default Announcements;
