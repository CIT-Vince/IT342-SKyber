import React from 'react';
import { useLocation } from 'react-router-dom';

const AnnouncementDetails = () => {
  const location = useLocation();
  const { announcement } = location.state;

  return (
    <div className="content grid md:grid-cols-3 gap-6 max-w-6xl mx-auto">
          {announcements.map((item) => (
            <div
              key={item.id}
              className="rounded-xl overflow-hidden bg-white shadow-md border border-gray-100 cursor-pointer hover:shadow-lg transition"
              onClick={() => handleCardClick(item)}
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
  );
};

export default AnnouncementDetails;