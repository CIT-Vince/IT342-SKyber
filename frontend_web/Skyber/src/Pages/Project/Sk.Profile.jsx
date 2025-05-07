import React, { useState, useEffect } from 'react';
import { 
  Title,
  Text,
  Paper,
  Grid,
  Badge,
  Group,
  Avatar,
  Button,
  ActionIcon,
  Tabs,
  Divider,
  LoadingOverlay
} from '@mantine/core';
import { 
  IconBrandFacebook, 
  IconMail, 
  IconPhone, 
  IconMapPin,
  IconFlipVertical,
  IconCalendar
} from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import { notifications } from '@mantine/notifications';
import { useAuth } from '../../contexts/AuthContext';
import { apiFetch } from '../utils/api';
import { Modal, FileInput, Textarea,TextInput,Select  } from '@mantine/core';
import { IconPlus, IconEdit, IconTrash } from '@tabler/icons-react';


const SkProfile = () => {
  const [loading, setLoading] = useState(true);
  const [officials, setOfficials] = useState([]);
  const [flippedCards, setFlippedCards] = useState({});
  const [selectedTerm, setSelectedTerm] = useState('All');
  {/* Admin ni */}
  const { currentUser } = useAuth();
  const [isAdmin, setIsAdmin] = useState(false);
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [selectedOfficial, setSelectedOfficial] = useState(null);
  const [profileForm, setProfileForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    position: '',
    term: '',
    platform: '',
    birthdate: '',
    gender: '',
    age: 0,
    phoneNumber: '',
    address: '',
    role: 'ADMIN',
    imageFile: null
  });

  // Fetch SK officials from API
  useEffect(() => {
    
    fetchOfficials();
  }, []);

  // Function to handle card flip
  const handleCardClick = (id, event) => {
    setFlippedCards(prev => ({
      ...prev,
      [id]: !prev[id]
    }));
  };

  // Function to get position color
  const getPositionColor = (position) => {
    if (!position) return { from: 'gray', to: 'blue' };
    
    if (position.toLowerCase().includes('chairman')) 
      return { from: 'gold', to: 'orange' };
    else if (position.toLowerCase().includes('councilor')) 
      return { from: 'blue', to: 'cyan' };
    else 
      return { from: 'gray', to: 'blue' };
  };

  // Get unique term years from the data
  const getUniqueTerms = () => {
    const terms = officials.map(official => official.term);
    return ['All', ...new Set(terms)];
  };

  // Admin check
  useEffect(() => {
    if (currentUser) {
      const checkUserRole = async () => {
        try {
          const { getDatabase, ref, get } = await import('firebase/database');
          const db = getDatabase();
          const userRef = ref(db, `users/${currentUser.uid}`);
          const snapshot = await get(userRef);
          
          if (snapshot.exists()) {
            const userData = snapshot.val();
            setIsAdmin(userData.role === 'ADMIN');
          }
        } catch (error) {
          console.error('Error checking user role:', error);
        }
      };
      
      checkUserRole();
    }
  }, [currentUser]);

  // Replace fetch with apiFetch
  const fetchOfficials = async () => {
    try {
      setLoading(true);
      
      const response = await apiFetch('/api/profiles/getAllProfiles');
      
      if (!response.ok) {
        throw new Error(`Server responded with ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Retrieved SK officials:", data);
      
      // Transform data to match the expected format
      if (data && data.length > 0) {
        const transformedData = data.map((profile, index) => ({
          id: profile.uid || index + 1,
          firstName: profile.firstName || "Unknown",
          lastName: profile.lastName || "Official",
          position: profile.position || "SK Member",
          platform: profile.platform || "Working for the youth of our community.",
          role: profile.role || "USER",
          email: profile.email || "contact@skyber.org",
          phoneNumber: profile.phoneNumber || "N/A",
          address: profile.address || "Local Barangay",
          age: profile.age || 0,
          gender: profile.gender || "Not specified",
          term: profile.term || "Current Term",
          achievements: ["Serving the community", "Youth development programs"],
          profileImage: profile.skImage ? 
            `data:image/jpeg;base64,${profile.skImage}` : 
            `https://api.dicebear.com/7.x/personas/svg?seed=${profile.firstName}${profile.lastName}&backgroundColor=b6e3f4`
        }));
        
        setOfficials(transformedData);
        notifications.show({
          title: 'SK Officials Loaded',
          message: `Successfully loaded ${transformedData.length} SK officials`,
          color: 'green',
        });
      } else {
        console.warn("No SK officials found, using sample data");
        setOfficials(sampleSkOfficials);
      }
    } catch (error) {
      console.error('Error fetching SK officials:', error);
      
      notifications.show({
        title: 'Error Loading Officials',
        message: 'Using sample data instead',
        color: 'red',
      });
      
      setOfficials(sampleSkOfficials);
    } finally {
      setLoading(false);
    }
  };
  
  // Call fetch on component mount
  useEffect(() => {
    fetchOfficials();
  }, []);

  // Create handlers 
  const handleCreateClick = () => {
    setProfileForm({
      firstName: '',
      lastName: '',
      email: '',
      position: '',
      term: '',
      platform: '',
      birthdate: '',
      gender: '',
      age: 0,
      phoneNumber: '',
      address: '',
      role: 'ADMIN',
      imageFile: null
    });
    setCreateModalOpen(true);
  };

  const handleEditClick = (official) => {
    setSelectedOfficial(official);
    setProfileForm({
      firstName: official.firstName,
      lastName: official.lastName,
      email: official.email,
      position: official.position,
      term: official.term,
      platform: official.platform,
      birthdate: official.birthdate || '',
      gender: official.gender,
      age: official.age || 0,
      phoneNumber: official.phoneNumber,
      address: official.address,
      role: official.role || 'ADMIN',
      imageFile: null // Can't prefill image
    });
    setEditModalOpen(true);
  };

  const handleDeleteClick = (official) => {
    setSelectedOfficial(official);
    setDeleteModalOpen(true);
  };

  const handleCreateSubmit = async (e) => {
    e.preventDefault();
  
    if (!profileForm.firstName || !profileForm.lastName || !profileForm.email) {
      notifications.show({
        title: 'Validation Error',
        message: 'Please fill in all required fields.',
        color: 'red'
      });
      return;
    }
  
    try {
      setLoading(true);
  
      if (profileForm.imageFile) {
        const formData = new FormData();
        formData.append('firstName', profileForm.firstName);
        formData.append('lastName', profileForm.lastName);
        formData.append('email', profileForm.email);
        formData.append('position', profileForm.position || '');
        formData.append('term', profileForm.term || '');
        formData.append('platform', profileForm.platform || '');
        formData.append('birthdate', profileForm.birthdate || '');
        formData.append('gender', profileForm.gender || '');
        formData.append('age', profileForm.age || 0);
        formData.append('phoneNumber', profileForm.phoneNumber || '');
        formData.append('address', profileForm.address || '');
        formData.append('role', profileForm.role || 'ADMIN');
        formData.append('image', profileForm.imageFile);
  
        const response = await apiFetch('/api/profiles/createProfile/with-image', {
          method: 'POST',
          body: formData
        });
  
        if (!response.ok) throw new Error(await response.text());
      } else {
        const profileData = {
          firstName: profileForm.firstName,
          lastName: profileForm.lastName,
          email: profileForm.email,
          position: profileForm.position || '',
          term: profileForm.term || '',
          platform: profileForm.platform || '',
          birthdate: profileForm.birthdate || '',
          gender: profileForm.gender || '',
          age: profileForm.age || 0,
          phoneNumber: profileForm.phoneNumber || '',
          address: profileForm.address || '',
          role: profileForm.role || 'ADMIN'
        };
  
        const response = await apiFetch('/api/profiles/createProfile', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(profileData)
        });
  
        if (!response.ok) throw new Error(await response.text());
      }
  
      notifications.show({ title: 'Success', message: 'SK Profile created!', color: 'green' });
      setCreateModalOpen(false);
      fetchOfficials();
    } catch (error) {
      notifications.show({ title: 'Error', message: error.message, color: 'red' });
    } finally {
      setLoading(false);
    }
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    if (!selectedOfficial) return;
  
    try {
      setLoading(true);
      
      const uid = selectedOfficial.id;
      
      if (profileForm.imageFile) {
        // First upload the new image
        const imageFormData = new FormData();
        imageFormData.append('image', profileForm.imageFile);
        
        await apiFetch(`/api/profiles/updateProfile/${uid}/image`, {
          method: 'PUT',
          body: imageFormData
        });
      }
      
      // Then update the profile data
      const profileData = {
        firstName: profileForm.firstName,
        lastName: profileForm.lastName,
        email: profileForm.email,
        position: profileForm.position || '',
        term: profileForm.term || '',
        platform: profileForm.platform || '',
        birthdate: profileForm.birthdate || '',
        gender: profileForm.gender || '',
        age: profileForm.age || 0,
        phoneNumber: profileForm.phoneNumber || '',
        address: profileForm.address || '',
        role: profileForm.role || 'ADMIN'
      };
      
      const response = await apiFetch(`/api/profiles/updateProfile/${uid}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(profileData)
      });
      
      if (!response.ok) throw new Error(await response.text());
      
      notifications.show({ title: 'Success', message: 'SK Profile updated!', color: 'green' });
      setEditModalOpen(false);
      fetchOfficials();
    } catch (error) {
      notifications.show({ title: 'Error', message: error.message, color: 'red' });
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteConfirm = async () => {
    if (!selectedOfficial) return;
    try {
      setLoading(true);
      const response = await apiFetch(`/api/profiles/deleteProfile/${selectedOfficial.id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(await response.text());
      notifications.show({ title: 'Deleted', message: 'SK Profile deleted!', color: 'green' });
      setDeleteModalOpen(false);
      fetchOfficials();
    } catch (error) {
      notifications.show({ title: 'Error', message: error.message, color: 'red' });
    } finally {
      setLoading(false);
    }
  };

  // Filter officials based on selected term
  const filteredOfficials = selectedTerm === 'All' 
    ? officials 
    : officials.filter(official => official.term === selectedTerm);

  return (
    <>
      {/* Header with gradient background */}
      <div
        className="w-full h-auto pt-30!w"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
        <Navbar />
        <header className="text-left py-10 pl-10 pt-30">
          <Title className="text-5xl font-bold text-white">
            Sangguniang Kabataan Officials<span className="animate-bounce inline-block ml-2">👨🏻‍💼</span>
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Meet our elected SK officials who are working hard to serve the youth in our community! 
          </Text>
        </header>
      </div>

      {/* Main content section with grid layout */}
      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        {loading ? (
          <div className="min-h-[60vh] flex items-center justify-center">
            <Paper shadow="md" radius="lg" className="p-8 relative w-full max-w-md">
              <LoadingOverlay visible={true} overlayBlur={2} overlayOpacity={0.3} loaderProps={{ size: 'lg', color: 'blue', variant: 'bars' }} />
              <div className="text-center py-8">
                <Title order={3} className="mb-4">Meeting Our Community Leaders</Title>
                <Text color="dimmed" className="mb-2">Please wait while we load the SK officials...</Text>
                <Text size="xs" color="blue" className="animate-pulse">This should only take a moment</Text>
              </div>
            </Paper>
          </div>
        ) : (
          <div className="max-w-7xl mx-auto">
            {/* Term filter section */}
            <Paper shadow="md" radius="lg" className="p-6 mb-8">
              <Group position="apart" className="mb-2">
                <Title order={4} className="flex items-center">
                  <IconCalendar size={22} className="mr-2 text-blue-600" />
                  SK Term Years
                </Title>
                
                {isAdmin && (
                  <Button
                    leftSection={<IconPlus size={16} />}
                    variant="gradient"
                    gradient={{ from: 'blue', to: 'cyan' }}
                    onClick={handleCreateClick}
                    radius="md"
                  >
                    Add New Official
                  </Button>
                )}
              </Group>
              <Divider className="mb-4" />
              <Tabs value={selectedTerm} onChange={setSelectedTerm} radius="xl" variant="pills" color="blue">
                <Tabs.List>
                  {getUniqueTerms().map(term => (
                    <Tabs.Tab 
                      key={term} 
                      value={term}
                      className="transition-all hover:bg-blue-50"
                    >
                      {term === 'All' ? 'All Terms' : term}
                    </Tabs.Tab>
                  ))}
                </Tabs.List>
              </Tabs>
            </Paper>

            {/* Display message when no officials match the filter */}
            {filteredOfficials.length === 0 ? (
              <Paper shadow="md" radius="lg" className="p-10 text-center">
                <Title order={3} className="mb-2">No SK officials found for this term (´。＿。｀)</Title>
                <Text>Try selecting a different term year!</Text>
              </Paper>
            ) : (
              <>
                {/* Term header when a specific term is selected */}
                {selectedTerm !== 'All' && (
                  <div className="mb-6 text-center">
                    <Badge 
                      size="xl" 
                      radius="md"
                      variant="gradient"
                      gradient={{ from: 'indigo', to: 'cyan' }}
                      className="px-8 py-2"
                    >
                      SK Officials for Term: {selectedTerm}
                    </Badge>
                  </div>
                )}

                {/* Officials grid */}
                <Grid gutter="lg">
                  {filteredOfficials.map((official) => (
                    <Grid.Col key={official.id} span={{ base: 12, sm: 6, md: 4, lg: 3 }}>
                      {/* Card with flip effect */}
                      <div 
                        className={`flip-card ${flippedCards[official.id] ? 'flipped' : ''}`}
                        style={{ height: '420px' }}
                        onClick={(e) => handleCardClick(official.id, e)}
                      >
                        <div className="flip-card-inner">
                          {/* Front of Card */}
                          <Paper 
                            radius="md" 
                            shadow="md"
                            className="flip-card-front flex flex-col items-center justify-start p-6"
                          >
                            <div className="flex justify-center items-center w-full">
                              <Avatar
                                src={official.profileImage}
                                alt={`${official.firstName} ${official.lastName} avatar`}
                                size={160}
                                radius={80}
                                className="border-4 border-blue-300 mb-4 mx-auto"
                              />
                            </div>
                            <Title order={3} className="text-blue-700 mb-2">
                              {official.firstName} {official.lastName}
                            </Title>
                            
                            <Badge 
                              variant="gradient" 
                              gradient={getPositionColor(official.position)}
                              size="lg"
                              className="mb-3"
                            >
                              {official.position}
                            </Badge>
                            
                            <div className="mt-auto">
                              <ActionIcon 
                                variant="light" 
                                color="blue" 
                                aria-label="Flip card"
                                radius="xl"
                                size="lg"
                                className="mt-4 animate-pulse"
                              >
                                <IconFlipVertical size={20} />
                              </ActionIcon>
                              <Text size="xs" color="dimmed" className="mt-1">Tap for more info</Text>
                            </div>
                            {isAdmin && (
                              <Group spacing={4} className="absolute top-2 right-2" onClick={(e) => e.stopPropagation()}>
                                <ActionIcon 
                                  variant="light" 
                                  color="yellow"
                                  onClick={() => handleEditClick(official)}
                                >
                                  <IconEdit size={16} />
                                </ActionIcon>
                                <ActionIcon 
                                  variant="light" 
                                  color="red"
                                  onClick={() => handleDeleteClick(official)}
                                >
                                  <IconTrash size={16} />
                                </ActionIcon>
                              </Group>
                            )}
                          </Paper>
                          
                          {/* Back of Card */}
                          <Paper 
                            radius="md" 
                            shadow="md"
                            className="flip-card-back p-6 overflow-auto"
                          >
                            <Title order={4} className="text-blue-700 mb-3">
                              {official.firstName} {official.lastName}
                            </Title>
                            
                            <div className="space-y-2 mb-4">
                              <Group>
                                <IconMail size={16} className="text-blue-500" />
                                <Text size="sm">{official.email}</Text>
                              </Group>
                              <Group>
                                <IconPhone size={16} className="text-blue-500" />
                                <Text size="sm">{official.phoneNumber}</Text>
                              </Group>
                              <Group>
                                <IconMapPin size={16} className="text-blue-500" />
                                <Text size="sm">{official.address}</Text>
                              </Group>
                            </div>
                            
                            <Title order={5} className="mt-4 mb-2">Platform</Title>
                            <Text size="sm" className="mb-4">
                              {official.platform}
                            </Text>
                            
                            <Title order={5} className="mb-2">Achievements</Title>
                            <div className="mb-4">
                              {official.achievements.map((achievement, idx) => (
                                <div key={idx} className="flex items-start mb-1">
                                  <div className="min-w-[6px] h-[6px] bg-blue-500 rounded-full mt-[0.5rem] mr-2"></div>
                                  <Text size="sm">{achievement}</Text>
                                </div>
                              ))}
                            </div>
                            
                            <Button
                              variant="gradient"
                              gradient={{ from: 'blue', to: 'cyan' }}
                              size="sm"
                              radius="xl"
                              leftSection={<IconBrandFacebook size={16} />}
                              className="mt-2"
                            >
                              Connect
                            </Button>
                            
                            <Text size="xs" color="dimmed" className="mt-4 text-center">
                              Tap to flip back
                            </Text>
                          </Paper>
                        </div>
                      </div>
                    </Grid.Col>
                  ))}
                </Grid>
              </>
            )}
            
            <div className="mt-8 text-center text-xs text-gray-400">
              Working together for a better community for our youth!
            </div>
          </div>
        )}
      </div>
      {/* Create SK Profile Modal */}
      <Modal
        opened={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        title="Add New SK Official"
        size="lg"
      >
        <form onSubmit={handleCreateSubmit}>
          <Grid>
            <Grid.Col span={6}>
              <TextInput
                label="First Name"
                required
                placeholder="First name"
                value={profileForm.firstName}
                onChange={(e) => setProfileForm({...profileForm, firstName: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
            <Grid.Col span={6}>
              <TextInput
                label="Last Name"
                required
                placeholder="Last name"
                value={profileForm.lastName}
                onChange={(e) => setProfileForm({...profileForm, lastName: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
          </Grid>
          
          <TextInput
            label="Email"
            required
            placeholder="Email address"
            value={profileForm.email}
            onChange={(e) => setProfileForm({...profileForm, email: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Position"
            placeholder="SK position"
            value={profileForm.position}
            onChange={(e) => setProfileForm({...profileForm, position: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Term"
            placeholder="Select or enter term years"
            data={getUniqueTerms().filter(term => term !== 'All')}
            value={profileForm.term}
            onChange={(value) => setProfileForm({...profileForm, term: value})}
            searchable
            creatable
            getCreateLabel={(query) => `+ Create ${query}`}
            className="mb-3"
          />
          
          <Textarea
            label="Platform"
            placeholder="Platform and advocacy"
            minRows={3}
            value={profileForm.platform}
            onChange={(e) => setProfileForm({...profileForm, platform: e.target.value})}
            className="mb-3"
          />
          
          <Grid>
            <Grid.Col span={6}>
              <TextInput
                label="Birthdate"
                type="date"
                value={profileForm.birthdate}
                onChange={(e) => setProfileForm({...profileForm, birthdate: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
            <Grid.Col span={6}>
              <Select
                label="Gender"
                data={[
                  { value: 'Male', label: 'Male' },
                  { value: 'Female', label: 'Female' },
                  { value: 'Other', label: 'Other' }
                ]}
                value={profileForm.gender}
                onChange={(value) => setProfileForm({...profileForm, gender: value})}
                className="mb-3"
              />
            </Grid.Col>
          </Grid>
          
          <Grid>
            <Grid.Col span={6}>
              <TextInput
                label="Age"
                type="number"
                value={profileForm.age}
                onChange={(e) => setProfileForm({...profileForm, age: parseInt(e.target.value) || 0})}
                className="mb-3"
              />
            </Grid.Col>
            <Grid.Col span={6}>
              <TextInput
                label="Phone Number"
                placeholder="Phone number"
                value={profileForm.phoneNumber}
                onChange={(e) => setProfileForm({...profileForm, phoneNumber: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
          </Grid>
          
          <TextInput
            label="Address"
            placeholder="Address"
            value={profileForm.address}
            onChange={(e) => setProfileForm({...profileForm, address: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Role"
            data={[
              { value: 'ADMIN', label: 'Admin' },
              { value: 'USER', label: 'User' },
            ]}
            value={profileForm.role}
            onChange={(value) => setProfileForm({...profileForm, role: value})}
            className="mb-3"
          />
          
          <FileInput
            label="Profile Image"
            placeholder="Upload an image"
            accept="image/*"
            onChange={(file) => setProfileForm({...profileForm, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={() => setCreateModalOpen(false)}>Cancel</Button>
            <Button type="submit" color="blue">Create Profile</Button>
          </Group>
        </form>
      </Modal>

      {/* Edit SK Profile Modal */}
      <Modal
        opened={editModalOpen}
        onClose={() => setEditModalOpen(false)}
        title="Edit SK Official"
        size="lg"
      >
        <form onSubmit={handleEditSubmit}>
          <Grid>
            <Grid.Col span={6}>
              <TextInput
                label="First Name"
                required
                placeholder="First name"
                value={profileForm.firstName}
                onChange={(e) => setProfileForm({...profileForm, firstName: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
            <Grid.Col span={6}>
              <TextInput
                label="Last Name"
                required
                placeholder="Last name"
                value={profileForm.lastName}
                onChange={(e) => setProfileForm({...profileForm, lastName: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
          </Grid>
          
          <TextInput
            label="Email"
            required
            placeholder="Email address"
            value={profileForm.email}
            onChange={(e) => setProfileForm({...profileForm, email: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Position"
            placeholder="SK position"
            value={profileForm.position}
            onChange={(e) => setProfileForm({...profileForm, position: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Term"
            placeholder="Select or enter term years"
            data={getUniqueTerms().filter(term => term !== 'All')}
            value={profileForm.term}
            onChange={(value) => setProfileForm({...profileForm, term: value})}
            searchable
            creatable
            getCreateLabel={(query) => `+ Create ${query}`}
            className="mb-3"
          />
          
          <Textarea
            label="Platform"
            placeholder="Platform and advocacy"
            minRows={3}
            value={profileForm.platform}
            onChange={(e) => setProfileForm({...profileForm, platform: e.target.value})}
            className="mb-3"
          />
          
          <Grid>
            <Grid.Col span={6}>
              <TextInput
                label="Birthdate"
                type="date"
                value={profileForm.birthdate}
                onChange={(e) => setProfileForm({...profileForm, birthdate: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
            <Grid.Col span={6}>
              <Select
                label="Gender"
                data={[
                  { value: 'Male', label: 'Male' },
                  { value: 'Female', label: 'Female' },
                  { value: 'Other', label: 'Other' }
                ]}
                value={profileForm.gender}
                onChange={(value) => setProfileForm({...profileForm, gender: value})}
                className="mb-3"
              />
            </Grid.Col>
          </Grid>
          
          <Grid>
            <Grid.Col span={6}>
              <TextInput
                label="Age"
                type="number"
                value={profileForm.age}
                onChange={(e) => setProfileForm({...profileForm, age: parseInt(e.target.value) || 0})}
                className="mb-3"
              />
            </Grid.Col>
            <Grid.Col span={6}>
              <TextInput
                label="Phone Number"
                placeholder="Phone number"
                value={profileForm.phoneNumber}
                onChange={(e) => setProfileForm({...profileForm, phoneNumber: e.target.value})}
                className="mb-3"
              />
            </Grid.Col>
          </Grid>
          
          <TextInput
            label="Address"
            placeholder="Address"
            value={profileForm.address}
            onChange={(e) => setProfileForm({...profileForm, address: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Role"
            data={[
              { value: 'ADMIN', label: 'Admin' },
              { value: 'USER', label: 'User' },
            ]}
            value={profileForm.role}
            onChange={(value) => setProfileForm({...profileForm, role: value})}
            className="mb-3"
          />
          
          <FileInput
            label="Profile Image (Optional)"
            description="Leave empty to keep existing image"
            placeholder="Upload a new image"
            accept="image/*"
            onChange={(file) => setProfileForm({...profileForm, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={() => setEditModalOpen(false)}>Cancel</Button>
            <Button type="submit" color="yellow">Update Profile</Button>
          </Group>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        opened={deleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        title="Delete SK Official"
        size="sm"
      >
        {selectedOfficial && (
          <>
            <Text>Are you sure you want to delete "{selectedOfficial.firstName} {selectedOfficial.lastName}"?</Text>
            <Text size="sm" color="dimmed" className="mt-2">
              This action cannot be undone.
            </Text>
            
            <Group position="right" className="mt-4">
              <Button variant="outline" onClick={() => setDeleteModalOpen(false)}>Cancel</Button>
              <Button color="red" onClick={handleDeleteConfirm}>Delete</Button>
            </Group>
          </>
        )}
      </Modal>
      {/* CSS for flip card animation */}
      <style jsx>{`
        .flip-card {
          background-color: transparent;
          perspective: 1000px;
          cursor: pointer;
        }

        .flip-card-inner {
          position: relative;
          width: 100%;
          height: 100%;
          text-align: center;
          transition: transform 0.8s;
          transform-style: preserve-3d;
        }

        .flip-card.flipped .flip-card-inner {
          transform: rotateY(180deg);
        }

        .flip-card-front, .flip-card-back {
          position: absolute;
          width: 100%;
          height: 100%;
          -webkit-backface-visibility: hidden;
          backface-visibility: hidden;
        }

        .flip-card-back {
          transform: rotateY(180deg);
        }
      `}</style>
    </>
  );
};

// Sample data to use as fallback
const sampleSkOfficials = [
  {
    id: 1,
    firstName: "Juan",
    lastName: "Dela Cruz",
    position: "SK Chairman",
    platform: "Prioritizing educational programs and creating opportunities for the youth to discover and develop their talents and skills.",
    role: "ADMIN",
    email: "juan.delacruz@skyber.ph",
    phoneNumber: "09123456789",
    address: "Purok 3, Barangay Mabini",
    age: 23,
    gender: "Male",
    term: "2022-2025",
    achievements: ["Led COVID-19 Relief Operations", "Implemented Free WiFi in Barangay Centers", "Organized Youth Leadership Summit 2024"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=JuanDelaCruz&backgroundColor=b6e3f4"
  },
  // ... other sample officials (keeping them for fallback)
];

export default SkProfile;