import React, { useState } from 'react';
import { Modal, Button } from '@mantine/core';
import Navbar from '../../components/Navbar';

const candidates = [
  {
    firstname: "Juan",
    lastname: "Dela Cruz",
    age: 21,
    address: "Purok 3, Barangay Mabini",
    partylist: "Kabataan Para sa Kaayohan",
    platform: "Maghatag og libreng school supplies, mag-organize og sports events, ug magtabang sa mga kabatan-onan nga nanginahanglan og scholarship. Vote for me kay gusto nako nga ang batan-on mahimong empowered ug active sa barangay! (๑˃ᴗ˂)ﻭ"
  },
  {
    firstname: "Maria",
    lastname: "Santos",
    age: 22,
    address: "Zone 5, Barangay Malipayon",
    partylist: "Batan-on United",
    platform: "Magsugod og livelihood programs para sa kabatan-onan, maghimo og leadership trainings, ug magpadayon sa transparency sa SK projects."
  }
];

const CandidatesList = () => {
  const [opened, setOpened] = useState(false);
  const [selected, setSelected] = useState(null);

  const handleView = (candidate) => {
    setSelected(candidate);
    setOpened(true);
  };
  

  return (
    <>
    {/* Background gradient and header */}
    <div
        className="w-full h-auto"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
      <Navbar />
      <header className="text-left py-10 pl-10">
          <h1 className="text-5xl font-bold text-white">Candidates</h1>
          <p className="text-white">
          Meet your aspiring leaders! Discover their dreams, platforms, and choose who will make our community shine brighter~ (｡•̀ᴗ-)✧
          </p>
        </header>
      </div>

      {/* Main content area */}
    <div className=" content min-h-screen pt-10 pb-10 px-4 bg-gradient-to-br from-blue-50 to-pink-50">
      <div className="space-y-4 max-w-5xl mx-auto">
      {candidates.map((c, idx) => (
        <div key={idx} className="bg-white rounded-xl shadow p-5 flex flex-col md:flex-row md:items-center md:justify-between gap-4">
            
            {/* Candidate Image */}
            <div className="flex-shrink-0 flex items-center justify-center mb-2 md:mb-0">
            <img
                src={`https://api.dicebear.com/7.x/personas/svg?seed=${c.firstname}${c.lastname}`}
                alt={`${c.firstname} ${c.lastname} avatar`}
                className="w-16 h-16 rounded-full border-2 border-blue-300 object-cover"
            />
            </div>

            {/* Candidate Info */}
            <div className="flex-1">
            <div className="text-lg font-bold text-pink-500">{c.firstname} {c.lastname}</div>
            <div className="text-gray-600 text-sm">Age: {c.age}</div>
            <div className="text-blue-600 text-sm font-semibold">Partylist: {c.partylist}</div>
            </div>

            {/* View Platform Button */}
            <Button radius="xl"
            className="bg-gradient-to-r from-pink-400 to-blue-500 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition"
            style={{ background: 'linear-gradient(to right, #f472b6, #3b82f6)' }}
            onClick={() => handleView(c)}
            >
            View Platform
            </Button>
        </div>
        ))}
      </div>

      <Modal
  opened={opened}
  onClose={() => setOpened(false)}
  title=""
  centered
  withCloseButton={false}
>
  {selected && (
    <div className="max-w-xl mx-auto bg-white rounded-2xl shadow-lg p-8 flex flex-col items-center">
      <img
        src={`https://api.dicebear.com/7.x/personas/svg?seed=${selected.firstname}${selected.lastname}`}
        alt={`${selected.firstname} ${selected.lastname} avatar`}
        className="w-32 h-32 rounded-full border-4 border-blue-200 object-cover mb-4"
      />
      <h2 className="text-2xl font-bold text-pink-500 mb-1">
        {selected.firstname} {selected.lastname}
      </h2>
      <div className="text-gray-600 mb-2">
        Age: <span className="font-semibold">{selected.age}</span>
      </div>
      <div className="text-gray-600 mb-2">
        Address: <span className="font-semibold">{selected.address}</span>
      </div>
      <div className="text-blue-600 font-bold mb-2">
        Partylist: {selected.partylist}
      </div>
      <div className="bg-blue-50 rounded-lg p-4 text-gray-700 w-full mb-2">
        <span className="font-bold text-blue-500">Platform:</span><br />
        {selected.platform}
      </div>
      <button className="mt-4 bg-gradient-to-r from-pink-400 to-blue-500 text-white px-8 py-2 rounded-full shadow hover:scale-105 transition">
        Support {selected.firstname}!
      </button>
      <div className="mt-2 text-xs text-gray-400">Padayon, kabatan-onan! (｡•̀ᴗ-)✧</div>
    </div>
  )}
</Modal>
      <div className="mt-8 text-center text-xs text-gray-400">Stay kawaii and informed! (づ｡◕‿‿◕｡)づ</div>
    </div>
    </>
  );
};

export default CandidatesList;