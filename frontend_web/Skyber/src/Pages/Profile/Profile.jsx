import React, { useState, useEffect } from 'react';
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
  Badge,
  Group,
  Loader
} from '@mantine/core';
import { DateInput } from '@mantine/dates';
import { IMaskInput } from 'react-imask';
import { IconStar, IconEdit, IconCheck, IconX } from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import Volunteers from './Volunteers';
import { notifications } from '@mantine/notifications';
import { useAuth } from '../../contexts/AuthContext';
import { getDatabase, ref, get, update } from 'firebase/database';
import defaultAvatar from '../../assets/default-avatar.jpg';

const Profile = () => {
  const { currentUser } = useAuth();
  const [userData, setUserData] = useState(null);
  const [originalData, setOriginalData] = useState(null);
  const [loading, setLoading] = useState(true);
  const [editMode, setEditMode] = useState(false);
  const [activeTab, setActiveTab] = useState("profile");

  // Fetch user data from Firebase
  useEffect(() => {
    const fetchUserData = async () => {
      if (!currentUser) return;
      
      try {
        setLoading(true);
        const database = getDatabase();
        const userRef = ref(database, `users/${currentUser.uid}`);
        const snapshot = await get(userRef);
        
        if (snapshot.exists()) {
          const data = snapshot.val();
          
          const normalizedData = {
            ...data,
            firstName: data.firstname || data.firstName || '',
            lastName: data.lastname || data.lastName || '',
          };
          
          if (normalizedData.birthdate) {
            normalizedData.birthdate = new Date(normalizedData.birthdate);
          }
          
          if (normalizedData.phoneNumber && !normalizedData.phone) {
            normalizedData.phone = normalizedData.phoneNumber;
          }
          
          setUserData(normalizedData);
          setOriginalData(normalizedData);
        } else {
          console.log("No user data found!");
          // Set defaults from Firebase Auth
          setUserData({
            firstName: currentUser.displayName?.split(' ')[0] || '',
            lastName: currentUser.displayName?.split(' ')[1] || '',
            email: currentUser.email,
            photoURL: currentUser.photoURL,
          });
        }
      } catch (error) {
        console.error("Error fetching user data:", error);
        notifications.show({
          title: 'Error',
          message: 'Failed to load profile data',
          color: 'red',
        });
      } finally {
        setLoading(false);
      }
    };
    
    fetchUserData();
  }, [currentUser]);

  // Handle form field changes
  const handleChange = (value, name) => {
    setUserData(prev => ({
      ...prev,
      [name]: value
    }));
  };

  // Handle form submission
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    try {
      const database = getDatabase();
      const userRef = ref(database, `users/${currentUser.uid}`);
      
      // Prepare data for update
      const updateData = {
        ...userData,
        birthdate: userData.birthdate ? userData.birthdate.toISOString().split('T')[0] : null,
        phoneNumber: userData.phone || userData.phoneNumber,
        lastUpdated: new Date().toISOString()
      };
      
      // Update Firebase
      await update(userRef, updateData);
      
      notifications.show({
        title: 'Profile Updated',
        message: 'Your profile was updated successfully! ★~(◠ω◕✿)',
        icon: <IconCheck size="1.1rem" />,
        color: 'green',
        autoClose: 4000,
      });
      
      setEditMode(false);
      setOriginalData(userData);
    } catch (error) {
      console.error("Error updating profile:", error);
      notifications.show({
        title: 'Update Failed',
        message: 'Could not update your profile',
        color: 'red',
      });
    }
  };

  // Cancel editing
  const handleCancel = () => {
    setUserData(originalData);
    setEditMode(false);
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen pt-20 flex justify-center items-center">
          <Loader size="xl" color="blue" />
          <p className="ml-3 text-xl">Loading your profile...</p>
        </div>
      </>
    );
  }

  return (
    <>
      <Navbar />
      <div className="min-h-screen pt-20 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50 pt-30!">
        <div className="max-w-7xl mx-auto flex flex-col md:flex-row gap-6">
          
          {/* Profile sidebar */}
          <Paper shadow="md" radius="lg" className="w-full md:w-64 p-5 h-fit">
            <div className="flex flex-col items-center">
              <Avatar 
                src={userData?.photoURL || defaultAvatar}
                size={120}
                radius={120}
                className="border-4 border-blue-500 mb-4"
              />
              <Title order={3} className="font-bold">{userData?.firstName} {userData?.lastName}</Title>
              <p className="text-gray-500 mb-2">{userData?.email}</p>
              
              <Badge variant="gradient" gradient={{ from: 'pink', to: 'blue' }} className="mb-6">
                <IconStar size={14} className="mr-1" style={{display: 'inline'}} /> {userData?.role || 'Member'}
              </Badge>
              
              {/* Navigation tabs */}
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
                
                {/* <button 
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
                </button> */}
              </div>
            </div>
          </Paper>
          
          {/* Main content */}
          <Paper shadow="md" radius="lg" className="flex-1 p-6">
            <>
              <div className="flex justify-between items-center mb-6">
                <Title order={2}>Personal Information</Title>
                {!editMode && (
                  <Button
                    leftIcon={<IconEdit size={16} />}
                    onClick={() => setEditMode(true)}
                    variant="light"
                    color="blue"
                    radius="md"
                  >
                    Edit Profile
                  </Button>
                )}
              </div>
            
              <form onSubmit={handleSubmit} className="space-y-6">
                <Grid gutter="md">
                  {/* First name and Last name */}
                  <Grid.Col span={{ base: 12, md: 6 }}>
                    <TextInput
                      label="First Name"
                      placeholder="Your first name"
                      value={userData?.firstName || ''}
                      onChange={(e) => handleChange(e.target.value, 'firstName')}
                      required
                      disabled={!editMode}
                      radius="md"
                    />
                  </Grid.Col>
                  
                  <Grid.Col span={{ base: 12, md: 6 }}>
                    <TextInput
                      label="Last Name"
                      placeholder="Your last name"
                      value={userData?.lastName || ''}
                      onChange={(e) => handleChange(e.target.value, 'lastName')}
                      required
                      disabled={!editMode}
                      radius="md"
                    />
                  </Grid.Col>
                  
                  {/* Age and Gender */}
                  <Grid.Col span={{ base: 12, md: 6 }}>
                    <TextInput
                      label="Age"
                      placeholder="Your age"
                      value={userData?.age || ''}
                      onChange={(e) => handleChange(e.target.value, 'age')}
                      disabled={!editMode}
                      radius="md"
                    />
                  </Grid.Col>
                  
                  <Grid.Col span={{ base: 12, md: 6 }}>
                    <Select
                      label="Gender"
                      placeholder="Select gender"
                      value={userData?.gender || ''}
                      onChange={(value) => handleChange(value, 'gender')}
                      data={[
                        { value: 'Male', label: 'Male' },
                        { value: 'Female', label: 'Female' },
                        { value: 'Non-binary', label: 'Non-binary' },
                        { value: 'Prefer not to say', label: 'Prefer not to say' }
                      ]}
                      disabled={!editMode}
                      radius="md"
                    />
                  </Grid.Col>
                  
                  {/* Phone and Email */}
                  <Grid.Col span={{ base: 12, md: 6 }}>
                    <Input.Wrapper label="Phone Number">
                      <Input
                        component={IMaskInput}
                        mask="+63 (000) 000-0000"
                        placeholder="Your phone number"
                        value={userData?.phone || userData?.phoneNumber || ''}
                        onChange={(e) => handleChange(e.target.value, 'phone')}
                        disabled={!editMode}
                        radius="md"
                      />
                    </Input.Wrapper>
                  </Grid.Col>
                  
                  <Grid.Col span={{ base: 12, md: 6 }}>
                    <TextInput
                      label="E-mail"
                      type="email"
                      placeholder="your.email@example.com"
                      value={userData?.email || ''}
                      onChange={(e) => handleChange(e.target.value, 'email')}
                      required
                      disabled={true} // Email cannot be edited through this form
                      radius="md"
                    />
                  </Grid.Col>
                </Grid>
                
                {/* Address */}
                <Textarea
                  label="Address"
                  placeholder="Your full address"
                  value={userData?.address || ''}
                  onChange={(e) => handleChange(e.target.value, 'address')}
                  disabled={!editMode}
                  radius="md"
                  minRows={3}
                />
                
                {/* Birthdate */}
                <DateInput
                  label="Birthdate"
                  placeholder="Select your birthdate"
                  value={userData?.birthdate || null}
                  onChange={(value) => handleChange(value, 'birthdate')}
                  disabled={!editMode}
                  radius="md"
                  valueFormat="YYYY-MM-DD"
                />
                
                {/* Action buttons */}
                {editMode && (
                  <Group position="right" spacing="md">
                    <Button
                      onClick={handleCancel}
                      variant="outline"
                      color="red"
                      radius="xl"
                      leftIcon={<IconX size={16} />}
                    >
                      Cancel
                    </Button>
                    <Button
                      type="submit"
                      radius="xl"
                      variant="gradient"
                      gradient={{ from: 'pink', to: 'blue' }}
                      className="px-8 transition hover:scale-105"
                      leftIcon={<IconCheck size={16} />}
                    >
                      Save Changes
                    </Button>
                  </Group>
                )}
              </form>
            </>
          </Paper>
        </div>
      </div>
    </>
  );
};

export default Profile;