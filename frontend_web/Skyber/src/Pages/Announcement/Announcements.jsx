import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import sample1 from '../../assets/announce/sample1.png';
import sample2 from '../../assets/announce/sample2.png';
import sample3 from '../../assets/announce/sample3.png';
import { useDisclosure } from '@mantine/hooks';
import { Modal, Button } from '@mantine/core';
import bottomBack from '../../assets/icons/bottomBack.png';

const announcements = [
  {
    id: 1,
    title: 'Sangguniang kabataan announcement 1',
    category: 'CATEGORY',
    date: '04/09/2025',
    image: sample1,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
  },
  {
    id: 2,
    title: 'Sangguniang kabataan announcement 2',
    category: 'CATEGORY',
    date: '04/10/2025',
    image: sample2,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
  },
  {
    id: 3,
    title: 'Sangguniang kabataan announcement 3',
    category: 'CATEGORY',
    date: '04/11/2025',
    image: sample3,
    description: 'Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.',
  },
];

function trimToWords(text, wordLimit = 200) {
  if (!text) return '';
  const words = text.split(' ');
  if (words.length <= wordLimit) return text;
  return words.slice(0, wordLimit).join(' ') + '...';
}

const Announcements = () => {
  const [selectedAnnouncement, setSelectedAnnouncement] = useState(null);
  const [userRegistered, setUserRegistered] = useState(false);
  const navigate = useNavigate();
  const [opened, { open, close }] = useDisclosure(false);

  const handleCardClick = (announcement) => {
    console.log('Card clicked:', announcement); 
    if (userRegistered) {
      navigate(`/announcements/${announcement.id}`, { state: { announcement } });
    } else {
      setSelectedAnnouncement(announcement);
      open();
    }
  };
  const handleCloseModal = () => {
    close();
    setSelectedAnnouncement(null);
  };
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
          <h1 className="text-5xl font-bold text-white">Announcements</h1>
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
          {['All', 'Utilities', 'Community', 'Register'].map((label) => (
            <button
              key={label}
              className="bg-blue-500 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition"
            >
              {label}
            </button>
          ))}
        </div>

        {/* Cards */}
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
                <p className="text-gray-600 text-sm">{trimToWords(item.description, 30)}</p>
              </div>
            </div>
          ))}
        </div>

        {/* Mantine Modal for Announcement Details */}
        
      </section>
      <Modal
          opened={opened && !!selectedAnnouncement}
          onClose={handleCloseModal}
          fullScreen
          centered
          withCloseButton={false}
          styles={{
            body: { padding: 0, background: 'white' }
          }}
        >
          {selectedAnnouncement && (
            <div className="min-h-screen flex flex-col">
              {/* Main Image with Blurred Background */}
              <div className="relative h-[400px]">
                <div
                  className="absolute inset-0 z-0 overflow-hidden rounded-lg"
                  style={{
                    backgroundImage: `url(${selectedAnnouncement.image})`,
                    backgroundSize: 'cover',
                    backgroundPosition: 'center',
                    filter: 'blur(20px) brightness(0.6)',
                    transform: 'scale(1.1)',
                  }}
                ></div>
                <div className="relative z-10 flex justify-center items-center h-full">
                  <img
                    src={selectedAnnouncement.image}
                    alt={selectedAnnouncement.title}
                    className="rounded-lg shadow-lg max-h-[350px] object-cover"
                  />
                  <button
                    className="absolute left-4 top-4 bg-white rounded-full p-2 shadow z-20"
                    onClick={handleCloseModal}
                  >
                    <span className="text-black font-bold text-lg">&larr;</span>
                  </button>
                </div>
              </div>
              {/* Text Content */}
              <div className="text-center mt-6 px-4">
                <h2 className="text-3xl font-bold">{selectedAnnouncement.title}</h2>
                <p className="italic text-sm text-gray-700 mt-1">
                  {selectedAnnouncement.category} ; {selectedAnnouncement.date}
                </p>
                <hr className="my-4 border-gray-300" />
                <p className="text-xl text-justify text-gray-700 max-w-3xl mx-auto leading-relaxed">
                  {selectedAnnouncement.description}
                </p>
                
              </div>
              {/* OTHER ANNOUNCEMENTS */}
              <section className="bg-white px-4 pb-12 max-w-5xl mx-auto w-full">
                <div className="flex justify-between items-center mt-12 mb-4">
                  <h3 className="text-xl font-bold">Other Announcements</h3>
                  <Button
                    variant="light"
                    color="yellow"
                    className="rounded-full flex justify-center items-center shadow-md w-30 hover:bg-yellow-300 transition"
                    onClick={handleCloseModal}
                  >
                    <img src={bottomBack} alt="Other Announcements" className="w-6 h-6 object-cover" />
                  </Button>
                </div>

                {/* Card Preview */}
                <div className="grid grid-cols-1 md:grid-cols-2 gap-4 place-items-center">
                  {announcements
                    .filter(a => a.id !== selectedAnnouncement.id)
                    .map((item) => (
                      <div
                        key={item.id}
                        className="bg-white border border-gray-200 shadow rounded-xl p-4 flex gap-4 max-w-xl items-center justify-center w-full"
                        onClick={() => {
                          setSelectedAnnouncement(item);
                        }}
                        style={{ cursor: 'pointer' }}
                      >
                        <img src={item.image} alt="Other Announcement" className="w-24 h-24 object-cover rounded-md" />
                        <div>
                          <p className="text-red-500 text-xs font-semibold">{item.category} | {item.date}</p>
                          <h4 className="font-semibold text-md mb-1">{item.title}</h4>
                          <p className="text-gray-600 text-sm">
                            {trimToWords(item.description, 20)}
                          </p>
                        </div>
                      </div>
                    ))}
                </div>
              </section>
              <Button fullWidth color="blue" className="mt-8" onClick={handleCloseModal}>
                  Close
                </Button>
            </div>
          )}
        </Modal>
    </>
  );
};

export default Announcements;