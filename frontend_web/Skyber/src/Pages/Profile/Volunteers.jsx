import React, { useState } from 'react';
import { Badge, Tabs, Title } from '@mantine/core';

// Mock data for volunteer activities the user has signed up for
const userVolunteerActivities = [
  {
    id: 1,
    title: "Tree Planting Day",
    description: "Help us plant 500 trees in our barangay! 🌱",
    category: "Environment",
    location: "Barangay Park",
    eventdate: "2025-05-10",
    status: "upcoming",
    image: "https://source.unsplash.com/160x160/?tree,planting"
  },
  {
    id: 2,
    title: "Feeding Program",
    description: "Assist in preparing and distributing meals for kids.",
    category: "Community Service",
    location: "Barangay Hall", 
    eventdate: "2024-02-15",
    status: "completed",
    image: "https://source.unsplash.com/160x160/?food,charity"
  },
  {
    id: 3,
    title: "Clean-up Drive",
    description: "Help clean our local beaches and waterways!",
    category: "Environment",
    location: "Coastal Area",
    eventdate: "2024-01-20",
    status: "completed",
    image: "https://source.unsplash.com/160x160/?beach,cleanup"
  }
];

const Volunteers = () => {
  const [activeTab, setActiveTab] = useState("all");
  
  // Filter activities based on active tab
  const filteredActivities = activeTab === 'all' 
    ? userVolunteerActivities 
    : userVolunteerActivities.filter(activity => activity.status === activeTab);
  
  // Calculate total points
//   const totalPoints = userVolunteerActivities.reduce((total, activity) => {
//     return total + activity.points;
//   }, 0);

  return (
    <div className="p-1">
      <div className="flex flex-col md:flex-row justify-between items-start md:items-center mb-6">
        <Title order={2} className="text-2xl font-bold mb-6 ">My Volunteer Activities</Title>
        
      </div>

      <Tabs value={activeTab} onChange={setActiveTab} className="mb-6">
        <Tabs.List>
          <Tabs.Tab value="all" className="font-medium">All Activities</Tabs.Tab>
          <Tabs.Tab value="upcoming" className="font-medium">Upcoming</Tabs.Tab>
          <Tabs.Tab value="completed" className="font-medium">Completed</Tabs.Tab>
        </Tabs.List>
      </Tabs>

      {filteredActivities.length === 0 ? (
        <div className="text-center p-10 bg-gray-50 rounded-lg">
          <p className="text-gray-500">No volunteer activities found (｡•́︿•̀｡)</p>
        </div>
      ) : (
        <div className="space-y-6">
          {filteredActivities.map((activity) => (
            <div 
              key={activity.id} 
              className="bg-white rounded-lg shadow p-6 flex flex-col md:flex-row gap-4"
            >
              <div className="flex-shrink-0">
                <img
                  src={activity.image}
                  alt={activity.title}
                  className="w-32 h-32 object-cover rounded-lg border-2 border-blue-200"
                />
              </div>
              
              <div className="flex-1">
                <div className="flex flex-col md:flex-row md:items-center md:justify-between">
                  <h2 className="text-xl font-bold text-pink-500">{activity.title}</h2>
                  <Badge 
                    color={activity.status === 'completed' ? 'green' : 'blue'}
                    variant="light" 
                    size="lg"
                  >
                    {activity.status === 'completed' ? 'Completed' : 'Upcoming'}
                  </Badge>
                </div>
                
                <p className="text-gray-700 mb-2">{activity.description}</p>
                
                <div className="flex flex-wrap gap-2 text-sm mb-2">
                  <span className="bg-blue-100 text-blue-600 px-2 py-1 rounded">{activity.category}</span>
                  <span className="bg-yellow-100 text-yellow-600 px-2 py-1 rounded">{activity.location}</span>
                  <span className="bg-green-100 text-green-600 px-2 py-1 rounded">
                    {new Date(activity.eventdate).toLocaleDateString()}
                  </span>
                </div>
                
              </div>
            </div>
          ))}
        </div>
      )}
      
      <div className="mt-8 text-center text-xs text-gray-400">
        Keep volunteering to earn more points and make our community better! (づ｡◕‿‿◕｡)づ
      </div>
    </div>
  );
};

export default Volunteers;