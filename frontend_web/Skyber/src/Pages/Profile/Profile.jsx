import React, { useState } from 'react';
import { Link } from 'react-router-dom';
import { 
  TextInput, 
  Textarea, 
  Select, 
  Paper, 
  Title, 
  Button, 
  Grid,
  Avatar,
  Input,
  Badge
} from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { IMaskInput } from 'react-imask';
import { IconStar } from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import Volunteers from './Volunteers';
import { notifications } from '@mantine/notifications'; 
import { IconCheck } from '@tabler/icons-react';

const Profile = () => {
  // Kawaii user data (⁄ ⁄•⁄ω⁄•⁄ ⁄)
  const [userData, setUserData] = useState({
    firstName: "John",
    lastName: "Doe",
    email: "john.doe@gmail.com",
    age: "28",
    gender: "Male",
    phone: "+63 912 345 6789",
    address: "Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    birthdate: new Date("1995-06-15")
  });

  const [activeTab, setActiveTab] = useState("profile");

  // Super kawaii form handling (づ｡◕‿‿◕｡)づ
  const handleChange = (value, name) => {
    setUserData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  const handleSubmit = (e) => {
    e.preventDefault();
    // This would connect to your backend API ^_^
    notifications.show({
      title: 'Profile Updated',
      message: 'Your profile was updated successfully! ★~(◠ω◕✿)',
      icon: <IconCheck size="1.1rem" />,
      color: 'pink',
      autoClose: 4000,
    });
  };

  return (
    <>
      <Navbar />
      <div className="min-h-screen pt-20 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row gap-6">
          
          {/* Kawaii sidebar (≧◡≦) */}
          <Paper shadow="md" radius="lg" className="w-full md:w-64 p-5 h-fit">
            <div className="flex flex-col items-center">
              <Avatar 
                src="https://i.pravatar.cc/300"
                size={120}
                radius={120}
                className="border-4 border-blue-500 mb-4"
              />
              <Title order={3} className="font-bold">{userData.firstName} {userData.lastName}</Title>
              <p className="text-gray-500 mb-2">{userData.email}</p>
              
              <Badge variant="gradient" gradient={{ from: 'pink', to: 'blue' }} className="mb-6">
                <IconStar size={14} className="mr-1" style={{display: 'inline'}} /> Member
              </Badge>
              
              {/* Kawaii navigation (つ≧▽≦)つ */}
              <div className="w-full space-y-2">
                <button 
                  className={`w-full flex items-center justify-between p-3 rounded-md ${activeTab === "profile" ? "bg-blue-50 text-blue-500" : "hover:bg-gray-100"}`}
                  onClick={() => setActiveTab("profile")}
                >
                  <div className="flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M16 7a4 4 0 11-8 0 4 4 0 018 0zM12 14a7 7 0 00-7 7h14a7 7 0 00-7-7z" />
                    </svg>
                    Profile
                  </div>
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </button>
                
                <button 
                  className={`w-full flex items-center justify-between p-3 rounded-md ${activeTab === "volunteers" ? "bg-blue-50 text-blue-500" : "hover:bg-gray-100"}`}
                  onClick={() => setActiveTab("volunteers")}
                >
                  <div className="flex items-center">
                    <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5 mr-2" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                      <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M17 20h5v-2a3 3 0 00-5.356-1.857M17 20H7m10 0v-2c0-.656-.126-1.283-.356-1.857M7 20H2v-2a3 3 0 015.356-1.857M7 20v-2c0-.656.126-1.283.356-1.857m0 0a5.002 5.002 0 019.288 0M15 7a3 3 0 11-6 0 3 3 0 016 0zm6 3a2 2 0 11-4 0 2 2 0 014 0zM7 10a2 2 0 11-4 0 2 2 0 014 0z" />
                    </svg>
                    Volunteers
                  </div>
                  <svg xmlns="http://www.w3.org/2000/svg" className="h-5 w-5" fill="none" viewBox="0 0 24 24" stroke="currentColor">
                    <path strokeLinecap="round" strokeLinejoin="round" strokeWidth={2} d="M9 5l7 7-7 7" />
                  </svg>
                </button>
              </div>
            </div>
          </Paper>
          
          {/* Kawaii form section (＾▽＾) */}
          <Paper shadow="md" radius="lg" className="flex-1 p-6">
          {activeTab === "profile" ? (
            <>
            <Title order={2} className="mb-6">Personal Information</Title>
            
            <form onSubmit={handleSubmit} className="space-y-6">
              <Grid gutter="md">
                {/* First name and Last name */}
                <Grid.Col span={{ base: 12, md: 6 }}>
                  <TextInput
                    label="First Name"
                    placeholder="Your first name"
                    value={userData.firstName}
                    onChange={(e) => handleChange(e.target.value, 'firstName')}
                    required
                    radius="md"
                  />
                </Grid.Col>
                
                <Grid.Col span={{ base: 12, md: 6 }}>
                  <TextInput
                    label="Last Name"
                    placeholder="Your last name"
                    value={userData.lastName}
                    onChange={(e) => handleChange(e.target.value, 'lastName')}
                    required
                    radius="md"
                  />
                </Grid.Col>
                
                {/* Age and Gender */}
                <Grid.Col span={{ base: 12, md: 6 }}>
                  <TextInput
                    label="Age"
                    placeholder="Your age"
                    value={userData.age}
                    onChange={(e) => handleChange(e.target.value, 'age')}
                    radius="md"
                  />
                </Grid.Col>
                
                <Grid.Col span={{ base: 12, md: 6 }}>
                  <Select
                    label="Gender"
                    placeholder="Select gender"
                    value={userData.gender}
                    onChange={(value) => handleChange(value, 'gender')}
                    data={[
                      { value: 'Male', label: 'Male' },
                      { value: 'Female', label: 'Female' },
                      { value: 'Non-binary', label: 'Non-binary' },
                      { value: 'Prefer not to say', label: 'Prefer not to say' }
                    ]}
                    radius="md"
                    withAsterisk
                  />
                </Grid.Col>
                
                {/* Phone and Email */}
                <Grid.Col span={{ base: 12, md: 6 }}>
                  <Input.Wrapper label="Phone Number" required>
                    <Input
                      component={IMaskInput}
                      mask="+63 (000) 000-0000"
                      placeholder="Your phone number"
                      value={userData.phone}
                      onChange={(e) => handleChange(e.target.value, 'phone')}
                      radius="md"
                    />
                  </Input.Wrapper>
                </Grid.Col>
                
                <Grid.Col span={{ base: 12, md: 6 }}>
                  <TextInput
                    label="E-mail"
                    type="email"
                    placeholder="your.email@example.com"
                    value={userData.email}
                    onChange={(e) => handleChange(e.target.value, 'email')}
                    required
                    radius="md"
                  />
                </Grid.Col>
              </Grid>
              
              {/* Address */}
              <Textarea
                label="Address"
                placeholder="Your full address"
                value={userData.address}
                onChange={(e) => handleChange(e.target.value, 'address')}
                radius="md"
                minRows={3}
              />
              
              {/* Birthdate */}
              <DateInput
                label="Birthdate"
                placeholder="Select your birthdate"
                value={userData.birthdate}
                onChange={(value) => handleChange(value, 'birthdate')}
                radius="md"
                valueFormat="YYYY-MM-DD"
              />
              
              {/* Save button */}
              <div className="flex justify-end">
                <Button
                  type="submit"
                  radius="xl"
                  variant="gradient"
                  gradient={{ from: 'pink', to: 'blue' }}
                  className="px-8 py-2 transition hover:scale-105"
                >
                  Save Changes (✿◠‿◠)
                </Button>
              </div>
            </form>
            </>
            ) : (
              // Show Volunteers component when activeTab is "volunteers"
              <Volunteers />
            )}
          </Paper>
        </div>
      </div>
    </>
  );
};

export default Profile;