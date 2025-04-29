import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';
import { 
  Modal, 
  Button, 
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
import { IconSearch, IconBell, IconCalendar, IconArrowLeft, IconShare, IconHeart } from '@tabler/icons-react';
import sample1 from '../../assets/announce/sample1.png';
import sample2 from '../../assets/announce/sample2.png';
import sample3 from '../../assets/announce/sample3.png';
import { useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
import { showNotification } from '@mantine/notifications';



function trimToWords(text, wordLimit = 200) {
  if (!text) return '';
  const words = text.split(' ');
  if (words.length <= wordLimit) return text;
  return words.slice(0, wordLimit).join(' ') + '...';
}

const Announcements = () => {
  const { currentUser } = useAuth();
  const [loading, setLoading] = useState(true);
  const [selectedAnnouncement, setSelectedAnnouncement] = useState(null);
  const [userRegistered, setUserRegistered] = useState(false);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeCategory, setActiveCategory] = useState('All');
  const [announceData, setAnnounceData] = useState([]);
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

  const handleLikeToggle = (id) => {
    // If user is not logged in, show notification
    if (!currentUser) {
      showNotification({
        title: 'Login Required',
        message: 'Please log in to like announcements',
        color: 'blue',
        position: 'top-left'
      });
      return;
    }
    
    // Otherwise update the UI immediately for responsiveness
    setAnnounceData(announceData.map(item => 
      item.id === id 
        ? { ...item, isLiked: !item.isLiked, likes: item.isLiked ? item.likes - 1 : item.likes + 1 } 
        : item
    ));
    
    // In a real app, you would persist this to your backend
    // Example: await fetch(`http://localhost:8080/api/announcements/like/${id}`...
  };

  // Filter announcements based on search and category
  const filteredAnnouncements = announceData.filter(announcement => {
    const matchesSearch = announcement.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         announcement.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = activeCategory === 'All' || announcement.category === activeCategory;
    
    return matchesSearch && matchesCategory;
  });

  // In your useEffect
useEffect(() => {
  const fetchAnnouncements = async () => {
    try {
      setLoading(true);
      
      // API URL constant (move to config file in production)
      const API_URL = 'http://localhost:8080/api/announcements/getAllAnnouncements';
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
        // Transform data with better date handling
        const transformedData = data.map(item => ({
          id: item.id,
          title: item.title || "Untitled Announcement",
          category: item.category || 'Community',
          // Handle different date formats
          date: item.postedAt ? new Date(item.postedAt).toLocaleDateString() : new Date().toLocaleDateString(),
          image: item.imageData ? `data:image/jpeg;base64,${item.imageData}` : sample1,
          description: item.content || "No description provided.",
          likes: 0,
          isLiked: false
        }));
        
        setAnnounceData(transformedData);
        
        // Show success notification
        showNotification({
          title: 'Connected to SKyber Server',
          message: `Loaded ${transformedData.length} announcements from backend`,
          color: 'green',
          position: 'top-right'
        });
      } else {
        // If data is empty, show specific message
        console.warn("Server returned empty announcement data");
        throw new Error("No announcements found in server response");
      }
    } catch (error) {
      console.error("Error fetching announcements:", error);
      
      // Use fallback data
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
        // Add more mock data entries here
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
        <header className="text-left py-10 pl-10">
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
                    <Tabs.Tab value="Utilities">Utilities</Tabs.Tab>
                    <Tabs.Tab value="Community">Community</Tabs.Tab>
                    <Tabs.Tab value="Register">Register</Tabs.Tab>
                  </Tabs.List>
                </Tabs>
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
                          <ActionIcon 
                            variant="subtle" 
                            color={item.isLiked ? "pink" : "gray"}
                            onClick={(e) => {
                              e.stopPropagation();
                              handleLikeToggle(item.id);
                            }}
                          >
                            <IconHeart size={16} fill={item.isLiked ? "#F472B6" : "none"} />
                          </ActionIcon>
                          <Text size="xs" color="dimmed">{item.likes} Likes</Text>
                        </Group>
                        <ActionIcon variant="subtle">
                          <IconShare size={16} />
                        </ActionIcon>
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
                Back
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
                <Button 
                  variant="light" 
                  color={selectedAnnouncement.isLiked ? "pink" : "gray"}
                  leftSection={<IconHeart size={18} />}
                  onClick={() => handleLikeToggle(selectedAnnouncement.id)}
                >
                  {selectedAnnouncement.isLiked ? "Liked" : "Like"}
                </Button>
                <Button 
                  variant="light" 
                  color="blue"
                  leftSection={<IconShare size={18} />}
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
              <Button 
                fullWidth 
                variant="gradient" 
                gradient={{ from: 'blue', to: 'cyan' }} 
                className="mt-6" 
                radius="xl"
                onClick={handleCloseModal}
              >
                Close
              </Button>
            </Paper>
          </div>
        )}
      </Modal>
    </>
  );

};

export default Announcements;