import React, { useState } from 'react';
import { useNavigate, useParams, useLocation  } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';
import { 
  Modal, 
  Button, 
  Select,
  Textarea,
  FileInput,
  Paper, 
  Title, 
  Text, 
  Badge, 
  Card, 
  Grid,
  Group,
  Tabs,
  TextInput,
  ActionIcon,
  Divider,
  Avatar
} from '@mantine/core';
import { IconSearch, IconBell, IconCalendar, IconArrowLeft,IconEdit,IconTrash, IconShare,IconPlus , IconHeart } from '@tabler/icons-react';
import sample1 from '../../assets/announce/sample1.png';
import sample2 from '../../assets/announce/sample2.png';
import sample3 from '../../assets/announce/sample3.png';
import { useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { showNotification} from '@mantine/notifications';
import { apiFetch } from '../utils/api';



function trimToWords(text, wordLimit = 200) {
  if (!text) return '';
  const words = text.split(' ');
  if (words.length <= wordLimit) return text;
  return words.slice(0, wordLimit).join(' ') + '...';
}

const Announcements = () => {
  {/* Hooks and State Management */}
  const { currentUser } = useAuth();
  const [loading, setLoading] = useState(true);
  const [selectedAnnouncement, setSelectedAnnouncement] = useState(null);
  const [userRegistered, setUserRegistered] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeCategory, setActiveCategory] = useState('All');
  const [announceData, setAnnounceData] = useState([]);
  const navigate = useNavigate();
  const [opened, { open, close }] = useDisclosure(false);
  const params = useParams();
  const location = useLocation();
  const [isAdmin, setIsAdmin] = useState(false);
  const [editModalOpen, { open: openEditModal, close: closeEditModal }] = useDisclosure(false);
  const [createModalOpen, { open: openCreateModal, close: closeCreateModal }] = useDisclosure(false);
  const [deleteModalOpen, { open: openDeleteModal, close: closeDeleteModal }] = useDisclosure(false);
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    category: 'News',
    barangay: 'All',
    imageFile: null
  });

  // const API_BASE_URL = import.meta.env.VITE_API_BASE_URL;
  
  useEffect(() => {
    // Check if we have an ID in the URL (e.g., /announcements/123)
    if (params.id) {
      const fetchSpecificAnnouncement = async () => {
        try {
          setLoading(true);
          
          const response = await apiFetch(`api/announcements/getAnnouncementByHashId/${params.id}`);
          console.log("Attempting to fetch announcement with ID:", params.id, "type:", typeof params.id);
          if (!response.ok) throw new Error('Announcement not found');
          
          const data = await response.json();
          console.log("Fetched specific announcement:", data);
          
          // Transform the data with all required fields
          const announcementData = {
            id: data.id,
            title: data.title || "Untitled Announcement",
            category: data.category || 'Community',
            date: data.postedAt ? new Date(data.postedAt).toLocaleDateString() : new Date().toLocaleDateString(),
            image: data.imageData ? `data:image/jpeg;base64,${data.imageData}` : sample1,
            description: data.content || "No description provided.",
            likes: 0,
            isLiked: false
          };
          
          // Open the modal with this announcement
          setSelectedAnnouncement(announcementData);
          open();
        } catch (error) {
          console.error("Error fetching announcement by ID:", error);
          showNotification({
            title: 'Announcement Not Found',
            message: 'The announcement you tried to view could not be found',
            color: 'red',
            position: 'top-right'
          });
          // If announcement not found, redirect to main announcements page
          navigate('/announcements');
        } finally {
          setLoading(false);
        }
      };
      
      fetchSpecificAnnouncement();
    }
  }, [params.id]);

  const handleCardClick = (announcement) => {
    console.log('Card clicked:', announcement);
    setSelectedAnnouncement(announcement);
    open();
    
    window.history.pushState(
      {}, 
      announcement.title, 
      `/announcements/${announcement.id}`
    );
  };

  const handleCloseModal = () => {
    close();
    setSelectedAnnouncement(null);
    if (params.id) {
      window.history.pushState({}, '', '/announcements');
    }
  };

  const handleLikeToggle = (id) => {
    if (!currentUser) {
      showNotification({
        title: 'Login Required',
        message: 'Please log in to like announcements',
        color: 'blue',
        position: 'top-left'
      });
      return;
    }
    
    setAnnounceData(announceData.map(item => 
      item.id === id 
        ? { ...item, isLiked: !item.isLiked, likes: item.isLiked ? item.likes - 1 : item.likes + 1 } 
        : item
    ));
    
  };

  useEffect(() => {
    if (currentUser) {
      const fetchUserData = async () => {
        try {
          const { getDatabase, ref, get } = await import('firebase/database');
          const db = getDatabase();
          const userRef = ref(db, `users/${currentUser.uid}`);
          const snapshot = await get(userRef);
          
          if (snapshot.exists()) {
            const userData = snapshot.val();
            // Check if user is admin
            setIsAdmin(userData.role === 'ADMIN');
          }
        } catch (error) {
          console.error('Error fetching user data:', error);
        }
      };
      
      fetchUserData();
    } else {
      setIsAdmin(false);
    }
  }, [currentUser]);

  // Filters
  const filteredAnnouncements = announceData.filter(announcement => {
    const matchesSearch = announcement.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         announcement.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = activeCategory === 'All' || 
                            announcement.category.toLowerCase() === activeCategory.toLowerCase();
    
    return matchesSearch && matchesCategory;
  });

  const handleEditClick = (announcement) => {
    setSelectedAnnouncement(announcement);
    setFormData({
      title: announcement.title,
      content: announcement.description,
      category: announcement.category,
      barangay: announcement.barangay || 'All',
      imageFile: null 
    });
    openEditModal();
  };
  
  const handleDeleteClick = (announcement) => {
    setSelectedAnnouncement(announcement);
    openDeleteModal();
  };
  
  const handleCreateSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      
      // Check if an image is being uploaded
      if (formData.imageFile) {
        const formDataObj = new FormData();
        formDataObj.append('title', formData.title);
        formDataObj.append('content', formData.content);
        formDataObj.append('category', formData.category);
        formDataObj.append('barangay', formData.barangay);
        formDataObj.append('image', formData.imageFile);
        
        const response = await apiFetch('api/announcements/createWithImage', {
          method: 'POST',
          body: formDataObj
        });
        
        if (!response.ok) throw new Error('Failed to create announcement with image');
      } else {
        const response = await apiFetch('api/announcements/createAnnouncements', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json'
          },
          body: JSON.stringify({
            title: formData.title,
            content: formData.content,
            category: formData.category,
            barangay: formData.barangay
          })
        });
        
        if (!response.ok) throw new Error('Failed to create announcement');
      }
      
      showNotification({
        title: 'Success',
        message: 'Announcement created successfully',
        color: 'green'
      });
      
      closeCreateModal();
      fetchAnnouncements();
    } catch (error) {
      console.error('Error creating announcement:', error);
      showNotification({
        title: 'Error',
        message: 'Failed to create announcement',
        color: 'red'
      });
    } finally {
      setLoading(false);
    }
  };
  
  const handleEditSubmit = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      
      const updateData = {
        title: formData.title,
        content: formData.content,
        category: formData.category,
        barangay: formData.barangay
      };
      
      const response = await apiFetch(`api/announcements/updateAnnouncement/${selectedAnnouncement.id}`, {
        method: 'PUT',
        headers: { 'Content-Type': 'application/json' },
        body: JSON.stringify(updateData)
      });
      
      if (!response.ok) throw new Error('Failed to update announcement');
      
      showNotification({
        title: 'Success',
        message: 'Announcement updated successfully',
        color: 'green'
      });
      
      closeEditModal();
      fetchAnnouncements(); 
    } catch (error) {
      console.error('Error updating announcement:', error);
      showNotification({
        title: 'Error',
        message: 'Failed to update announcement', 
        color: 'red'
      });
    } finally {
      setLoading(false);
    }
  };

  // Add this function outside of any hooks, near your other handler functions
const fetchAnnouncements = async () => {
  try {
    setLoading(true);
    
    const API_URL = 'https://it342-skyber.onrender.com/api/announcements/getAllAnnouncements';
    console.log("Fetching announcements from:", API_URL);
    
    const response = await fetch(API_URL);
    
    // Better error handling
    if (!response.ok) {
      console.error(`Server responded with ${response.status}: ${response.statusText}`);
      throw new Error(`Server responded with ${response.status}`);
    }
    
    const data = await response.json();
    console.log("Retrieved announcements data:", data);
    
    // Check if data exists and is not empty
    if (data && data.length > 0) {
      const transformedData = data.map(item => ({
        id: item.id,
        title: item.title || "Untitled Announcement",
        category: item.category || 'Community',
        date: item.postedAt ? new Date(item.postedAt).toLocaleDateString() : new Date().toLocaleDateString(),
        image: item.imageData ? `data:image/jpeg;base64,${item.imageData}` : sample1,
        description: item.content || "No description provided.",
        likes: 0,
        isLiked: false
      }));
      
      const API_URL = 'https://it342-skyber.onrender.com/api/announcements/getAllAnnouncements';
      console.log("Fetching announcements from:", API_URL);
      
      const response = await fetch(API_URL);
      
      // Better error handling
      if (!response.ok) {
        console.error(`Server responded with ${response.status}: ${response.statusText}`);
        throw new Error(`Server responded with ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Retrieved announcements data:", data);
      
      // Check if data exists and is not empty
      if (data && data.length > 0) {
        const transformedData = data.map(item => ({
          id: item.id,
          title: item.title || "Untitled Announcement",
          category: item.category || 'Community',
          date: item.postedAt ? new Date(item.postedAt).toLocaleDateString() : new Date().toLocaleDateString(),
          image: item.imageData ? `data:image/jpeg;base64,${item.imageData}` : sample1,
          description: item.content || "No description provided.",
          likes: 0,
          isLiked: false
        }));
        
        setAnnounceData(transformedData);
      } else {
        console.warn("Server returned empty announcement data");
        throw new Error("No announcements found in server response");
      }
    } catch (error) {
      console.error("Error fetching announcements:", error);
      // sagdai lang ni
      const mockData = [
        {
          id: "mock1",
          title: "Community Meeting",
          category: "Community",
          date: new Date().toLocaleDateString(),
          image: sample1,
          description: "Join us for our monthly community meeting to discuss upcoming projects and initiatives. Everyone is welcome to attend and share their ideas.",
          likes: 15,
          isLiked: false
        },
      ];
      
      setAnnounceData(mockData);
      
      showNotification({
        title: 'Using Demo Data',
        message: 'Could not connect to server. Showing demo announcements.',
        color: 'yellow',
        position: 'top-right'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleConfirmDelete = async () => {
    try {
      setLoading(true);
      
      const response = await apiFetch(`/api/announcements/deleteAnnouncement/${selectedAnnouncement.id}`, {
        method: 'DELETE'
      });
      
      if (!response.ok) throw new Error('Failed to delete announcement');
      
      showNotification({
        title: 'Success',
        message: 'Announcement deleted successfully',
        color: 'green'
      });
      
      closeDeleteModal();
      fetchAnnouncements(); // Re-fetch announcements
    } catch (error) {
      console.error('Error deleting announcement:', error);
      showNotification({
        title: 'Error',
        message: 'Failed to delete announcement',
        color: 'red'
      });
    } finally {
      setLoading(false);
    }
  };

// firebase nato
useEffect(() => {
  const fetchAnnouncements = async () => {
    try {
      setLoading(true);
      
      const API_URL = 'https://it342-skyber.onrender.com/api/announcements/getAllAnnouncements';
      console.log("Fetching announcements from:", API_URL);
      
      const response = await fetch(API_URL);
      
      // Better error handling
      if (!response.ok) {
        console.error(`Server responded with ${response.status}: ${response.statusText}`);
        throw new Error(`Server responded with ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Retrieved announcements data:", data);
      
      // Check if data exists and is not empty
      if (data && data.length > 0) {
        const transformedData = data.map(item => ({
          id: item.id,
          title: item.title || "Untitled Announcement",
          category: item.category || 'Community',
          date: item.postedAt ? new Date(item.postedAt).toLocaleDateString() : new Date().toLocaleDateString(),
          image: item.imageData ? `data:image/jpeg;base64,${item.imageData}` : sample1,
          description: item.content || "No description provided.",
          likes: 0,
          isLiked: false
        }));
        
        setAnnounceData(transformedData);
        
        // showNotification({
        //   title: 'Connected to SKyber Server',
        //   message: `Loaded ${transformedData.length} announcements from backend`,
        //   color: 'green',
        //   position: 'top-right'
        // });
      } else {
        // error naa diri
        console.warn("Server returned empty announcement data");
        throw new Error("No announcements found in server response");
      }
    } catch (error) {
      console.error("Error fetching announcements:", error);
      // sagdai lang ni
      const mockData = [
        {
          id: "mock1",
          title: "Community Meeting",
          category: "Community",
          date: new Date().toLocaleDateString(),
          image: sample1,
          description: "Join us for our monthly community meeting to discuss upcoming projects and initiatives. Everyone is welcome to attend and share their ideas.",
          likes: 15,
          isLiked: false
        },
      ];
      
      setAnnounceData(mockData);
      
  showNotification({
        title: 'Using Demo Data',
        message: 'Could not connect to server. Showing demo announcements.',
        color: 'yellow',
        position: 'top-right'
      });
    } finally {
      setLoading(false);
    }
  };
  
  fetchAnnouncements();
}, []);


const copyToClipboard = (text) => {
  navigator.clipboard.writeText(text)
    .then(() => {
      showNotification({
        title: 'Link Copied!',
        message: 'Share link has been copied to clipboard',
        color: 'blue',
      });
    })
    .catch(err => {
      console.error('Failed to copy:', err);
      showNotification({
        title: 'Copy Failed',
        message: 'Please try selecting and copying the URL manually',
        color: 'red',
      });
    });
};

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex flex-col items-center justify-center bg-gradient-to-br from-blue-50 to-pink-50">
          <div className="text-center p-8">
            <div className="inline-block animate-spin rounded-full h-12 w-12 border-4 border-blue-500 border-t-transparent mb-4"></div>
            <h2 className="text-xl font-semibold">Loading Announcements...</h2>
            <p className="text-gray-600">Please wait while we fetch the latest updates</p>
          </div>
        </div>
      </>
    );
  }
  
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
        <header className="text-left py-10 pl-10 pt-30">
          <Title className="text-5xl font-bold text-white">
            Announcements
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Stay updated with the latest community updates and news! (◕‿◕✿)
          </Text>
        </header>
      </div>

      {/* Main content section */}
      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-7xl mx-auto">
          {/* Search and filter section */}
          <Paper shadow="md" radius="lg" className="p-6 mb-8">
            <Grid>
              <Grid.Col span={{ base: 12, md: 8 }}>
                <TextInput
                  icon={<IconSearch size={18} />}
                  placeholder="Search announcements..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  radius="xl"
                  size="md"
                  className="mb-4 md:mb-0"
                />
              </Grid.Col>
              <Grid.Col span={{ base: 12, md: 4 }}>
                <Tabs value={activeCategory} onChange={setActiveCategory} radius="xl" variant="pills">
                  <Tabs.List grow>
                    <Tabs.Tab value="All" leftSection={<IconBell size={16} />}>All</Tabs.Tab>
                    <Tabs.Tab value="News">News</Tabs.Tab>
                    <Tabs.Tab value="Event">Event</Tabs.Tab>
                    <Tabs.Tab value="Notice">Notice</Tabs.Tab>
                    <Tabs.Tab value="Emergency">Emergency</Tabs.Tab>
                    
                  </Tabs.List>
                </Tabs>
              </Grid.Col>
              <Grid.Col span={12} className="mt-4">
                    {isAdmin && (
                      <div className="mb-4 flex justify-end">
                        <Button 
                          leftSection={<IconPlus size={16} />}
                          onClick={openCreateModal}
                          variant="gradient" 
                          gradient={{ from: 'blue', to: 'cyan' }}
                        >
                          Create Announcement
                        </Button>
                      </div>
                    )}
                </Grid.Col>
            </Grid>
          </Paper>

          {/* Announcements Cards */}
          {filteredAnnouncements.length > 0 ? (
            <Grid gutter="lg">
              {filteredAnnouncements.map((item) => (
                <Grid.Col key={item.id} span={{ base: 12, sm: 6, lg: 4 }}>
                  <Card 
                    shadow="md" 
                    radius="md" 
                    className="h-full flex flex-col transition-transform duration-300 hover:scale-[1.02]"
                    onClick={() => handleCardClick(item)}
                  >
                    <Card.Section>
                      <div className="relative">
                        <img 
                          src={item.image} 
                          alt={item.title} 
                          className="w-full h-48 object-cover" 
                        />
                        <Badge 
                          variant="gradient" 
                          gradient={{ from: 
                            item.category === 'Community' ? 'blue' : 
                            item.category === 'Utilities' ? 'violet' : 'pink', 
                            to: 'cyan' 
                          }}
                          className="absolute top-3 left-3"
                          size="lg"
                          radius="md"
                        >
                          {item.category}
                        </Badge>
                      </div>
                    </Card.Section>

                    <div className="p-4 flex-grow">
                      <Group className="justify-between mb-2">
                        <Text size="xs" c="dimmed" className="flex items-center">
                          <IconCalendar size={14} className="mr-1" /> {item.date}
                        </Text>
                      </Group>
                      
                      <Title order={3} className="mb-2">
                        {item.title}
                      </Title>
                      
                      <Text color="dimmed" size="sm" className="mb-3">
                        {trimToWords(item.description, 30)}
                      </Text>
                    </div>

                    <Card.Section className="p-3 bg-gradient-to-r from-blue-50 to-pink-50">
                      <Group position="apart">
                        <Group spacing="xs">
                        </Group>
                        <ActionIcon variant="subtle">
                          <IconShare size={16} />
                        </ActionIcon>
                        {isAdmin && (
                            <>
                              <ActionIcon 
                                color="yellow" 
                                onClick={(e) => {
                                  e.stopPropagation();
                                  handleEditClick(item);
                                }}
                              >
                                <IconEdit size={16} />
                              </ActionIcon>
                              <ActionIcon 
                                color="red" 
                                onClick={(e) => {
                                  e.stopPropagation();
                                  handleDeleteClick(item);
                                }}
                              >
                                <IconTrash size={16} />
                              </ActionIcon>
                            </>
                          )}
                      </Group>
                    </Card.Section>
                  </Card>
                </Grid.Col>
              ))}
            </Grid>
          ) : (
            <Paper shadow="md" radius="lg" className="p-10 text-center">
              <Title order={3} className="mb-2">No announcements found (´。＿。｀)</Title>
              <Text>Try changing your search or filters!</Text>
            </Paper>
          )}
        </div>
      </div>

      {/* Improved Modal for Announcement Details */}
      <Modal
        opened={opened && !!selectedAnnouncement}
        onClose={handleCloseModal}
        fullScreen
        centered
        withCloseButton={false}
        styles={{
          body: { padding: 0 },
          content: { background: 'linear-gradient(to bottom, #f0f4ff, #fff1f9)' }
        }}
      >
        {selectedAnnouncement && (
          <div className="min-h-screen flex flex-col">
            {/* Header with Back Button */}
            <div className="sticky top-0 z-50 bg-gradient-to-r from-blue-600 to-indigo-800 text-white p-4 flex items-center">
              <Button 
                variant="subtle" 
                color="white" 
                leftSection={<IconArrowLeft />}
                onClick={handleCloseModal}
              >
              </Button>
              <Title order={3} className="mx-auto pr-10">Announcement Details</Title>
            </div>

            {/* Main Image with Blurred Background */}
            <div className="relative h-[400px]">
              <div
                className="absolute inset-0 z-0 overflow-hidden"
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
              </div>
            </div>
            
            {/* Text Content */}
            <Paper radius="lg" className="mx-4 -mt-10 relative z-20 p-6 shadow-lg">
              <div className="flex justify-between items-center mb-3">
                <Badge 
                  variant="gradient" 
                  gradient={{ from: 
                    selectedAnnouncement.category === 'Community' ? 'blue' : 
                    selectedAnnouncement.category === 'Utilities' ? 'cyan' : 'pink', 
                    to: 'cyan' 
                  }}
                  size="lg"
                  radius="md"
                >
                  {selectedAnnouncement.category}
                </Badge>
                <Text size="sm" className="flex items-center">
                  <IconCalendar size={16} className="mr-1" /> {selectedAnnouncement.date}
                </Text>
              </div>
              
              <Title order={2} className="mb-4">{selectedAnnouncement.title}</Title>
              <Divider className="my-4" />
              
              <Text className="text-lg leading-relaxed">
                {selectedAnnouncement.description}
              </Text>
              
              {/* Engagement Section */}
              <Group className="mt-8">
                
                {/*Like button 
                <Button 
                  variant="light" 
                  color={selectedAnnouncement.isLiked ? "pink" : "gray"}
                  leftSection={<IconHeart size={18} />}
                  onClick={() => handleLikeToggle(selectedAnnouncement.id)}
                >
                  {selectedAnnouncement.isLiked ? "Liked" : "Like"}
                </Button> */}
                <Button 
                  variant="light" 
                  color="blue"
                  leftSection={<IconShare size={18} />}
                  onClick={(e) => {
                    e.stopPropagation(); // Prevent modal from closing
                    
                    // Create a specific URL for this announcement
                    const shareUrl = `${window.location.origin}/announcements/${String(selectedAnnouncement.id)}`;
                    
                    if (navigator.share) {
                      navigator.share({
                        title: selectedAnnouncement.title,
                        text: `${selectedAnnouncement.title} - ${trimToWords(selectedAnnouncement.description, 15)}`,
                        url: shareUrl,
                      })
                      .then(() => {
                        showNotification({
                          title: 'Shared Successfully',
                          message: 'Announcement shared with your contacts',
                          color: 'green',
                        });
                      })
                      .catch(err => {
                        console.log('Error sharing:', err);
                        // Fall back to clipboard if share was cancelled
                        copyToClipboard(shareUrl);
                      });
                    } else {
                      // Better fallback - copy to clipboard with notification
                      copyToClipboard(shareUrl);
                    }
                  }}
                >
                  Share
                </Button>             
              </Group>
            </Paper>
            
            {/* Other Announcements Section */}
            <Paper radius="lg" className="mx-4 mt-6 p-6 shadow-md">
              <Title order={3} className="mb-4">Other Announcements</Title>
              <Divider className="mb-4" />
              
              <Grid>
                {announceData
                  .filter(a => a.id !== selectedAnnouncement.id)
                  .map((item) => (
                    <Grid.Col key={item.id} span={{ base: 12, md: 6 }}>
                      <Card 
                        className="flex gap-4 hover:bg-gray-50 transition cursor-pointer"
                        onClick={() => setSelectedAnnouncement(item)}
                        padding="sm"
                      >
                        <img 
                          src={item.image} 
                          alt={item.title} 
                          className="w-24 h-24 object-cover rounded-md" 
                        />
                        <div>
                          <Group>
                            <Badge size="sm" variant="light" color="blue">{item.category}</Badge>
                            <Text size="xs" c="dimmed">{item.date}</Text>
                          </Group>
                          <Text weight={500}>{item.title}</Text>
                          <Text size="sm" lineClamp={2} color="dimmed">
                            {trimToWords(item.description, 15)}
                          </Text>
                        </div>
                      </Card>
                    </Grid.Col>
                  ))}
              </Grid>
              
              {/* Bottom Close Button */}
              <div className="flex justify-center w-full">
                <Button 
                  variant="gradient" 
                  gradient={{ from: 'blue', to: 'cyan' }} 
                  className="mt-6" 
                  w="40%" // Smaller percentage for a more balanced look
                  radius="xl"
                  onClick={handleCloseModal}
                >
                  Close
                </Button>
              </div>
            </Paper>
          </div>
        )}
      </Modal>
      {/* Create Modal */}
      <Modal opened={createModalOpen} onClose={closeCreateModal} title="Create Announcement" size="lg">
        <form onSubmit={handleCreateSubmit}>
          <TextInput
            label="Title"
            required
            placeholder="Announcement title"
            value={formData.title}
            onChange={(e) => setFormData({...formData, title: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Category"
            data={[
              { value: 'News', label: 'News' },
              { value: 'Event', label: 'Event' },
              { value: 'Notice', label: 'Notice' },
              { value: 'Emergency', label: 'Emergency' },
            ]}
            value={formData.category}
            onChange={(value) => setFormData({...formData, category: value})}
            className="mb-3"
            required
          />
          
          <Select
            label="Barangay"
            data={[
              { value: 'All', label: 'All Barangays' },
              { value: 'Barangay 1', label: 'Barangay 1' },
              { value: 'Barangay 2', label: 'Barangay 2' },
              { value: 'Barangay 3', label: 'Barangay 3' },
            ]}
            value={formData.barangay}
            onChange={(value) => setFormData({...formData, barangay: value})}
            className="mb-3"
            required
          />
          
          <Textarea
            label="Content"
            placeholder="Announcement content"
            required
            minRows={4}
            value={formData.content}
            onChange={(e) => setFormData({...formData, content: e.target.value})}
            className="mb-3"
          />
          
          <FileInput
            label="Image"
            placeholder="Upload announcement image"
            accept="image/*"
            onChange={(file) => setFormData({...formData, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={closeCreateModal}>Cancel</Button>
            <Button type="submit" color="blue">Create</Button>
          </Group>
        </form>
      </Modal>

      {/* Edit Modal */}
      <Modal opened={editModalOpen} onClose={closeEditModal} title="Edit Announcement" size="lg">
        <form onSubmit={handleEditSubmit}>
          <TextInput
            label="Title"
            required
            placeholder="Announcement title"
            value={formData.title}
            onChange={(e) => setFormData({...formData, title: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Category"
            data={[
              { value: 'News', label: 'News' },
              { value: 'Event', label: 'Event' },
              { value: 'Notice', label: 'Notice' },
              { value: 'Emergency', label: 'Emergency' },
            ]}
            value={formData.category}
            onChange={(value) => setFormData({...formData, category: value})}
            className="mb-3"
            required
          />
          
          <Select
            label="Barangay"
            data={[
              { value: 'All', label: 'All Barangays' },
              { value: 'Barangay 1', label: 'Barangay 1' },
              { value: 'Barangay 2', label: 'Barangay 2' },
              { value: 'Barangay 3', label: 'Barangay 3' },
            ]}
            value={formData.barangay}
            onChange={(value) => setFormData({...formData, barangay: value})}
            className="mb-3"
            required
          />
          
          <Textarea
            label="Content"
            placeholder="Announcement content"
            required
            minRows={4}
            value={formData.content}
            onChange={(e) => setFormData({...formData, content: e.target.value})}
            className="mb-3"
          />
          
          <FileInput
            label="Image (Optional)"
            description="Leave empty to keep existing image"
            placeholder="Upload new announcement image"
            accept="image/*"
            onChange={(file) => setFormData({...formData, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={closeEditModal}>Cancel</Button>
            <Button type="submit" color="yellow">Update</Button>
          </Group>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal opened={deleteModalOpen} onClose={closeDeleteModal} title="Delete Announcement" size="sm">
        <Text>Are you sure you want to delete this announcement?</Text>
        <Text size="sm" color="dimmed" className="mt-2">This action cannot be undone.</Text>
        
        <Group position="right" mt="xl">
          <Button variant="outline" onClick={closeDeleteModal}>Cancel</Button>
          <Button color="red" onClick={handleConfirmDelete}>Delete</Button>
        </Group>
      </Modal>
    </>
  );

};

export default Announcements; 