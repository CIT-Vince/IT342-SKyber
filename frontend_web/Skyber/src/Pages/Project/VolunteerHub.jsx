import React, { useState } from 'react';
import Navbar from '../../components/Navbar';
import { Button, Text } from '@mantine/core';
import { modals } from '@mantine/modals';

const mockOpportunities = [
  {
    id: 1,
    title: "Tree Planting Day",
    description: "Help us plant 500 trees in our barangay! 🌱",
    category: "Environment",
    location: "Barangay Park",
    eventdate: "2025-05-10",
    contactperson: "Ate Maria",
    contactemail: "maria@skyber.com",
    status: "active",
    requirements: "Bring gloves and water bottle."
  },
  {
    id: 2,
    title: "Feeding Program",
    description: "Assist in preparing and distributing meals for kids.",
    category: "Community Service",
    location: "Barangay Hall",
    eventdate: "2025-05-20",
    contactperson: "Kuya Juan",
    contactemail: "juan@skyber.com",
    status: "ended",
    requirements: "Food handler certificate preferred."
  }
];
const statuses = ['All', ...Array.from(new Set(mockOpportunities.map(op => op.status.charAt(0).toUpperCase() + op.status.slice(1))))];

const VolunteerHub = () => {
  const [opportunities] = useState(mockOpportunities);
  const [selectedStatus, setSelectedStatus] = useState('All');

  // Filtered opportunities based on selected status
    const filteredOpportunities =
        selectedStatus === 'All'
        ? opportunities
        : opportunities.filter(op => op.status.toLowerCase() === selectedStatus.toLowerCase());

    const handleSignUp = (op) => {
        modals.openConfirmModal({
          title: `Sign up for "${op.title}"?`,
          children: (
            <Text size="sm">
              Are you sure you want to sign up for <b>{op.title}</b>?<br />
              <span className="text-xs text-gray-500">Contact: {op.contactperson} ({op.contactemail})</span>
            </Text>
          ),
          labels: { confirm: 'Yes, Sign Me Up!', cancel: 'Cancel' },
          onCancel: () => {},
          onConfirm: () => {
            // You can add your sign-up logic here!
            modals.closeAll();
            modals.open({
              title: 'Yatta! (≧◡≦)',
              children: <Text size="sm">You have successfully signed up for <b>{op.title}</b>! See you there~</Text>,
            });
          },
        });
      };

  return (
    <>
    <div
        className="w-full h-auto"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
      <Navbar />
      <header className="text-left py-10 pl-10">
          <h1 className="text-5xl font-bold text-white">Volunteer Hub</h1>
          <p className="text-white">
          Find opportunities to help your community and earn rewards! (๑˃ᴗ˂)ﻭ
          </p>
        </header>
      </div>
      {/* Main content area */}
      <div className="content min-h-screen pt-10 pb-10 px-4 bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-5xl mx-auto">
          {/* Filter buttons for status */}
          <div className="flex justify-center gap-4 flex-wrap mb-10">
            {statuses.map((label) => (
              <button
                key={label}
                onClick={() => setSelectedStatus(label)}
                className={`px-6 py-2 rounded-full shadow hover:scale-105 transition ${
                  selectedStatus === label
                    ? 'bg-pink-500 text-white'
                    : 'bg-blue-500 text-white'
                }`}
              >
                {label}
              </button>
            ))}
          </div>

          {/* Cards */}
          <div className="space-y-6">
        {filteredOpportunities.map((op) => (
          <div
            key={op.id}
            className="bg-white rounded-lg shadow p-6 flex flex-col md:flex-row md:items-center md:justify-between gap-4"
          >
            {/* Left: Image + Text */}
            <div className="flex flex-row items-start gap-4 flex-1">
              <div className="flex-shrink-0">
                <img
                  src="https://source.unsplash.com/160x160/?volunteer,community"
                  alt="Volunteer Event"
                  className="w-32 h-32 object-cover rounded-lg border-2 border-blue-200"
                />
              </div>
              <div>
                <h2 className="text-xl font-bold text-pink-500">{op.title}</h2>
                <p className="text-gray-700 mb-2">{op.description}</p>
                <div className="flex flex-wrap gap-2 text-sm mb-2">
                  <span className="bg-blue-100 text-blue-600 px-2 py-1 rounded">{op.category}</span>
                  <span className="bg-yellow-100 text-yellow-600 px-2 py-1 rounded">{op.location}</span>
                  <span className="bg-green-100 text-green-600 px-2 py-1 rounded">
                    {new Date(op.eventdate).toLocaleDateString()}
                  </span>
                </div>
                <div className="text-xs text-gray-500 mb-1">
                  Contact: 👤{op.contactperson}  • 📧({op.contactemail})
                </div>
                <div className="text-xs text-gray-500 mb-1">
                  Requirements: {op.requirements}
                </div>
              </div>
            </div>
            {/* Right: Status & Button */}
            <div className="flex flex-col items-end gap-2 mt-4 md:mt-0">
              <span
                className={`px-3 py-1 rounded-full text-xs font-bold ${
                  op.status === "active"
                    ? "bg-green-200 text-green-700"
                    : "bg-gray-200 text-gray-500"
                }`}
              >
                {op.status === "active" ? "Active" : "Ended"}
              </span>
              {op.status === "active" && (
                <Button radius="xl"
                className="bg-gradient-to-r from-pink-400 to-blue-500 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition"
                style={{ background: 'linear-gradient(to right, #f472b6, #3b82f6)' }}
                onClick={() => handleSignUp(op)}
              >
                Sign Up!
              </Button>
              )}
            </div>
          </div>
        ))}
      </div>
      
        </div>
      </div>
      
    </>
  );
};

export default VolunteerHub;