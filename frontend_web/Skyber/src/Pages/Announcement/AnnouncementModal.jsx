import React from 'react';

const AnnouncementModal = ({ announcement, onClose }) => {
  return (
    <div className="fixed inset-0 z-50 flex items-center justify-center bg-black bg-opacity-50">
      <div className="bg-white rounded-lg w-[90%] md:w-[600px] p-6 relative">
        <button
          className="absolute top-3 right-4 text-gray-500 hover:text-black text-xl"
          onClick={onClose}
        >
          &times;
        </button>
        <img src={announcement.image} alt={announcement.title} className="w-full h-56 object-cover rounded-md mb-4" />
        <h2 className="text-xl font-bold mb-2">{announcement.title}</h2>
        <p className="text-sm text-gray-500 mb-1">{announcement.category} | {announcement.date}</p>
        <p className="text-gray-700">{announcement.description}</p>
      </div>
    </div>
  );
};

export default AnnouncementModal;
