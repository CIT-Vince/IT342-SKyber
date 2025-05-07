import React, { useState, useEffect } from 'react';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';
import { useAuth } from '../../contexts/AuthContext'; 
import { apiFetch } from '../utils/api';
import { 
  Paper, 
  Title, 
  Text, 
  Badge, 
  Card, 
  Grid,
  Group,
  Tabs,
  TextInput,
  Divider,
  Progress,
  Modal,
  Button,
  Avatar,
  LoadingOverlay,
  Select,
  Textarea,
  FileInput,
  ActionIcon
} from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { 
  IconSearch, 
  IconCalendar, 
  IconLocation, 
  IconMail,
  IconUser,
  IconArrowRight,
  IconArrowLeft,
  IconList,
  IconHeart,
  IconPlus,
  IconEdit,
  IconTrash
} from '@tabler/icons-react';

const VolunteerHub = () => {
  const [opportunities, setOpportunities] = useState([]);
  const [loading, setLoading] = useState(true);
  const [activeStatus, setActiveStatus] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedOpportunity, setSelectedOpportunity] = useState(null);
  const [opened, { open, close }] = useDisclosure(false);
  {/* Admin NI ANIMAL */}
  const { currentUser } = useAuth();
  const [isAdmin, setIsAdmin] = useState(false);
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [volunteerForm, setVolunteerForm] = useState({
    title: '',
    description: '',
    category: 'Community Service',
    location: '',
    eventDate: '',
    contactPerson: '',
    contactEmail: '',
    status: 'active',
    requirements: '',
    registerLink: '',
    imageFile: null
  });

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
          console.error('Error checking user role (≧﹏ ≦)', error);
        }
      };
      
      checkUserRole();
    }
  }, [currentUser]);
  
  const sanitizeLink = (link) => {
    if (!link) return '';
    return link.startsWith('http://') || link.startsWith('https://')
      ? link
      : `https://${link}`;
  };

  const fetchOpportunities = async () => {
    try {
      setLoading(true);
      
      // Using apiFetch instead of fetch
      const response = await apiFetch('/api/volunteers/getAllVolunteers');
      
      if (!response.ok) {
        throw new Error(`Server responded with ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Retrieved volunteer opportunities:", data);
      
      // Transform data from backend format to frontend format
      if (data && data.length > 0) {
        const transformedData = data.map(item => ({
          id: item.id || Math.random().toString(),
          title: item.title || "Untitled Opportunity",
          description: item.description || "No description provided",
          category: item.category || "Other",
          location: item.location || "Location not specified",
          eventdate: item.eventDate || new Date().toISOString(),
          contactperson: item.contactPerson || "Volunteer Coordinator",
          contactemail: item.contactEmail || "contact@skyber.org",
          status: item.status ? item.status.toLowerCase() : "active",
          requirements: item.requirements || "No specific requirements.",
          registerLink: item.registerLink || "",
          volunteers: Math.floor(Math.random() * 10) + 1, // Random number for demo
          maxVolunteers: Math.floor(Math.random() * 20) + 10, // Random number for demo
          image: item.volunteerImage ? `data:image/jpeg;base64,${item.volunteerImage}` : 
            `https://images.unsplash.com/photo-${Math.floor(Math.random() * 1000000)}?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400`
        }));
        
        setOpportunities(transformedData);
        
        notifications.show({
          title: 'Volunteer Opportunities Loaded',
          message: `Successfully loaded ${transformedData.length} super kawaii opportunities ✧*。٩(ˊᗜˋ*)و✧*。`,
          color: 'green',
        });
      } else {
        console.warn("No volunteer opportunities found (◞‸◟；)");
      }
    } catch (error) {
      console.error("Failed to fetch volunteer opportunities:", error);
      
      notifications.show({
        title: 'Error Loading Opportunities (｡•́︿•̀｡)',
        message: 'Please try again later or contact support',
        color: 'red',
      });
    } finally {
      setLoading(false);
    }
  };
  // Fetch volunteer opportunities from backend
  useEffect(() => {
    fetchOpportunities();
  }, []);

  // Filter opportunities based on search and status
  const filteredOpportunities = opportunities.filter(op => {
    const matchesSearch = op.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         op.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         op.category.toLowerCase().includes(searchQuery.toLowerCase());
    
    const matchesStatus = activeStatus === 'all' || op.status === activeStatus;
    
    return matchesSearch && matchesStatus;
  });

  // Handle volunteer sign-up
  const handleSignUp = (opportunity) => {
    // This would normally call an API to register the user
    const updatedOpportunities = opportunities.map(op => 
      op.id === opportunity.id 
        ? { ...op, volunteers: op.volunteers + 1 } 
        : op
    );
    setOpportunities(updatedOpportunities);
    
    // Update selected opportunity if modal is open
    if (selectedOpportunity && selectedOpportunity.id === opportunity.id) {
      setSelectedOpportunity({ ...selectedOpportunity, volunteers: selectedOpportunity.volunteers + 1 });
    }
    
    notifications.show({
      title: 'Thank you for volunteering!',
      message: `You have signed up for: ${opportunity.title}`,
      color: 'green',
    });
  };

  // Calculate progress percentage
  const getProgressPercentage = (current, max) => {
    return (current / max) * 100;
  };

  // Handle card click to open modal
  const handleCardClick = (opportunity) => {
    setSelectedOpportunity(opportunity);
    open();
  };

  // Get category color
  const getCategoryColor = (category) => {
    if (!category) return { from: 'blue', to: 'cyan' };
    
    switch(category.toLowerCase()) {
      case 'environment': return { from: 'green', to: 'cyan' };
      case 'community service': return { from: 'blue', to: 'violet' };
      case 'healthcare': 
      case 'health': return { from: 'pink', to: 'red' };
      case 'education': return { from: 'yellow', to: 'orange' };
      default: return { from: 'blue', to: 'cyan' };
    }
  };

  // CRUD Operations for Admin
  const handleCreateClick = () => {
    setVolunteerForm({
      title: '',
      description: '',
      category: 'Community Service',
      location: '',
      eventDate: '',
      contactPerson: '',
      contactEmail: '',
      status: 'active',
      requirements: '',
      registerLink: '',
      imageFile: null
    });
    setCreateModalOpen(true);
  };

  const handleEditClick = (opportunity) => {
    setSelectedOpportunity(opportunity);
    setVolunteerForm({
      title: opportunity.title,
      description: opportunity.description,
      category: opportunity.category,
      location: opportunity.location,
      eventDate: opportunity.eventdate ? new Date(opportunity.eventdate).toISOString().split('T')[0] : '',
      contactPerson: opportunity.contactperson,
      contactEmail: opportunity.contactemail,
      status: opportunity.status,
      requirements: opportunity.requirements,
      registerLink: opportunity.registerLink || '',
      imageFile: null // Can't prefill image
    });
    setEditModalOpen(true);
  };

  const handleDeleteClick = (opportunity) => {
    setSelectedOpportunity(opportunity);
    setDeleteModalOpen(true);
  };

  const handleCreateSubmit = async (e) => {
    e.preventDefault();
  
    if (!volunteerForm.title || !volunteerForm.description) {
      notifications.show({
        title: 'Validation Error ｡ﾟ･ (>﹏<) ･ﾟ｡',
        message: 'Please fill in all required fields, pretty please!',
        color: 'red'
      });
      return;
    }
  
    try {
      setLoading(true);
  
      if (volunteerForm.imageFile) {
        const formData = new FormData();
        formData.append('title', volunteerForm.title);
        formData.append('description', volunteerForm.description);
        formData.append('category', volunteerForm.category);
        formData.append('location', volunteerForm.location);
        formData.append('eventDate', volunteerForm.eventDate);
        formData.append('contactPerson', volunteerForm.contactPerson);
        formData.append('contactEmail', volunteerForm.contactEmail);
        formData.append('status', volunteerForm.status);
        formData.append('requirements', volunteerForm.requirements);
        formData.append('registerLink', sanitizeLink(volunteerForm.registerLink));
        formData.append('image', volunteerForm.imageFile);
  
        const response = await apiFetch('/api/volunteers/createVolunteer/with-image', {
          method: 'POST',
          body: formData
        });
  
        if (!response.ok) throw new Error(await response.text());
      } else {
        const volunteerData = {
          title: volunteerForm.title,
          description: volunteerForm.description,
          category: volunteerForm.category,
          location: volunteerForm.location,
          eventDate: volunteerForm.eventDate,
          contactPerson: volunteerForm.contactPerson,
          contactEmail: volunteerForm.contactEmail,
          status: volunteerForm.status,
          requirements: volunteerForm.requirements,
          registerLink: sanitizeLink(volunteerForm.registerLink)
        };
  
        const response = await apiFetch('/api/volunteers/createVolunteer', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(volunteerData)
        });
  
        if (!response.ok) throw new Error(await response.text());
      }
  
      notifications.show({ 
        title: 'Success ヾ(＠⌒ー⌒＠)ノ',
        message: 'Volunteer opportunity created! Yay!',
        color: 'green' 
      });
      setCreateModalOpen(false);
      fetchOpportunities();
    } catch (error) {
      notifications.show({ 
        title: 'Error (っ °Д °;)っ',
        message: error.message,
        color: 'red' 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    if (!selectedOpportunity) return;
  
    try {
      setLoading(true);
  
      if (volunteerForm.imageFile) {
        const formData = new FormData();
        formData.append('title', volunteerForm.title);
        formData.append('description', volunteerForm.description);
        formData.append('category', volunteerForm.category);
        formData.append('location', volunteerForm.location);
        formData.append('eventDate', volunteerForm.eventDate);
        formData.append('contactPerson', volunteerForm.contactPerson);
        formData.append('contactEmail', volunteerForm.contactEmail);
        formData.append('status', volunteerForm.status);
        formData.append('requirements', volunteerForm.requirements);
        formData.append('registerLink', sanitizeLink(volunteerForm.registerLink));
        formData.append('image', volunteerForm.imageFile);
  
        const response = await apiFetch(`/api/volunteers/updateVolunteer/with-image/${selectedOpportunity.id}`, {
          method: 'PUT',
          body: formData
        });
  
        if (!response.ok) throw new Error(await response.text());
      } else {
        const volunteerData = {
          title: volunteerForm.title,
          description: volunteerForm.description,
          category: volunteerForm.category,
          location: volunteerForm.location,
          eventDate: volunteerForm.eventDate,
          contactPerson: volunteerForm.contactPerson,
          contactEmail: volunteerForm.contactEmail,
          status: volunteerForm.status,
          requirements: volunteerForm.requirements,
          registerLink: sanitizeLink(volunteerForm.registerLink)
        };
  
        const response = await apiFetch(`/api/volunteers/updateVolunteer/${selectedOpportunity.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(volunteerData)
        });
  
        if (!response.ok) throw new Error(await response.text());
      }
  
      notifications.show({ 
        title: 'Success (❁´◡`❁)',
        message: 'Volunteer opportunity updated! Super kawaii~',
        color: 'green' 
      });
      setEditModalOpen(false);
      fetchOpportunities();
    } catch (error) {
      notifications.show({ 
        title: 'Error ｡ﾟ(ﾟ´ω`ﾟ)ﾟ｡',
        message: error.message,
        color: 'red' 
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteConfirm = async () => {
    if (!selectedOpportunity) return;
    try {
      setLoading(true);
      const response = await apiFetch(`/api/volunteers/deleteVolunteer/${selectedOpportunity.id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(await response.text());
      notifications.show({ 
        title: 'Deleted (´｡• ᵕ •｡`)',
        message: 'Volunteer opportunity deleted!',
        color: 'green' 
      });
      setDeleteModalOpen(false);
      fetchOpportunities();
    } catch (error) {
      notifications.show({ 
        title: 'Error (╥﹏╥)',
        message: error.message,
        color: 'red' 
      });
    } finally {
      setLoading(false);
    }
  };

  // // Filter opportunities based on search and status
  // const filteredOpportunities = opportunities.filter(op => {
  //   const matchesSearch = op.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
  //                        op.description.toLowerCase().includes(searchQuery.toLowerCase()) ||
  //                        op.category.toLowerCase().includes(searchQuery.toLowerCase());
    
  //   const matchesStatus = activeStatus === 'all' || op.status === activeStatus;
    
  //   return matchesSearch && matchesStatus;
  // });

  // // Handle volunteer sign-up
  // const handleSignUp = (opportunity) => {
  //   // This would normally call an API to register the user
  //   const updatedOpportunities = opportunities.map(op => 
  //     op.id === opportunity.id 
  //       ? { ...op, volunteers: op.volunteers + 1 } 
  //       : op
  //   );
  //   setOpportunities(updatedOpportunities);
    
  //   // Update selected opportunity if modal is open
  //   if (selectedOpportunity && selectedOpportunity.id === opportunity.id) {
  //     setSelectedOpportunity({ ...selectedOpportunity, volunteers: selectedOpportunity.volunteers + 1 });
  //   }
    
  //   notifications.show({
  //     title: 'Thank you for volunteering! (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧',
  //     message: `You have signed up for: ${opportunity.title}`,
  //     color: 'green',
  //   });
  // };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-pink-50">
          <div className="text-center">
            <LoadingOverlay visible={true} overlayBlur={2} />
            <Title order={3}>Loading Volunteer Opportunities</Title>
            <Text color="dimmed">Please wait while we fetch the opportunities...</Text>
          </div>
        </div>
      </>
    );
  }

  return (
    <>
      {/* Header with gradient background */}
      <div
        className="w-full h-auto pt-30!"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
        <Navbar />
        <header className="text-left py-10 pl-10">
          <Title className="text-5xl font-bold text-white">
            Volunteer Hub<span className="animate-bounce inline-block ml-2">🤝</span>
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Find opportunities to help your community and earn rewards! 
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
                  placeholder="Search opportunities..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  radius="xl"
                  size="md"
                  className="mb-4 md:mb-0"
                />
              </Grid.Col>
              <Grid.Col span={{ base: 12, md: 4 }}>
                <Tabs value={activeStatus} onChange={setActiveStatus} radius="xl" variant="pills">
                  <Tabs.List grow>
                    <Tabs.Tab value="all">All</Tabs.Tab>
                    <Tabs.Tab value="active">Active</Tabs.Tab>
                    <Tabs.Tab value="ended">Upcoming</Tabs.Tab>
                    <Tabs.Tab value="completed">Completed</Tabs.Tab>
                  </Tabs.List>
                </Tabs>
              </Grid.Col>
              {isAdmin && (
                <Grid.Col span={12} className="mt-4">
                  <div className="flex justify-end">
                    <Button
                      leftSection={<IconPlus size={16} />}
                      variant="gradient"
                      gradient={{ from: 'cyan', to: 'blue' }}
                      onClick={handleCreateClick}
                      radius="md"
                    >
                      Add New Opportunity 
                    </Button>
                  </div>
                </Grid.Col>
              )}
            </Grid>
          </Paper>

          {/* Opportunity Cards */}
          {filteredOpportunities.length > 0 ? (
            <Grid gutter="lg">
              {filteredOpportunities.map((opportunity) => (
                <Grid.Col key={opportunity.id} span={{ base: 12, md: 6 }}>
                  <Card 
                    shadow="md" 
                    radius="md" 
                    className="h-full flex flex-col transition-transform duration-300 hover:scale-[1.02]"
                    onClick={() => handleCardClick(opportunity)}
                  >
                    <Card.Section>
                      <div className="relative">
                        <img 
                          src={opportunity.image} 
                          alt={opportunity.title} 
                          className="w-full h-48 object-cover" 
                          onError={(e) => {
                            e.target.src = "https://picsum.photos/400/200";
                          }}
                        />
                        <Badge 
                          variant="gradient" 
                          gradient={getCategoryColor(opportunity.category)}
                          className="absolute top-3 left-3"
                          size="lg"
                          radius="md"
                        >
                          {opportunity.category}
                        </Badge>
                        <Badge 
                          color={opportunity.status === 'active' ? 'green' : 'gray'}
                          className="absolute top-3 right-3"
                          radius="md"
                        >
                          {opportunity.status === 'active' ? 'Active' : 'Upcoming' }
                        </Badge>
                      </div>
                      
                    </Card.Section>

                    <div className="p-4 flex-grow">
                      <Group className="justify-between mb-2">
                        <Text size="xs" className="flex items-center text-gray-600">
                          <IconCalendar size={14} className="mr-1" /> 
                          {new Date(opportunity.eventdate).toLocaleDateString()}
                        </Text>
                        <Text size="xs" className="flex items-center text-gray-600">
                          <IconLocation size={14} className="mr-1" /> 
                          {opportunity.location}
                        </Text>
                      </Group>
                      
                      <Title order={3} className="mb-2">
                        {opportunity.title}
                      </Title>
                      
                      <Text color="dimmed" size="sm" className="mb-3" lineClamp={2}>
                        {opportunity.description}
                      </Text>
                      
                      <div className="mt-3">
                        <Group position="apart" mb="xs">
                          <Text size="sm" weight={500}>Volunteer Slots</Text>
                          <Text size="xs" color="dimmed">
                            {opportunity.volunteers}/{opportunity.maxVolunteers}
                          </Text>
                        </Group>
                        <Progress 
                          value={getProgressPercentage(opportunity.volunteers, opportunity.maxVolunteers)} 
                          color={opportunity.status === 'active' ? 'blue' : 'gray'} 
                          size="sm" 
                          radius="xl"
                        />
                      </div>
                    </div>

                    <Card.Section className="p-3 bg-gradient-to-r from-blue-50 to-pink-50">
                      <Group position="apart">
                        <Group spacing="xs">
                          <Text size="xs" color="dimmed" className="flex items-center">
                            <IconUser size={14} className="mr-1" /> 
                            {opportunity.contactperson}
                          </Text>
                        </Group>
                        <Group spacing="xs">
                          <Text size="xs" color="blue">View Details</Text>
                          <IconArrowRight size={14} className="text-blue-500" />
                        </Group>
                        
                        <Group spacing="xs ">
                          {isAdmin && (
                              <Group spacing={4}>
                                <ActionIcon 
                                  variant="light" 
                                  color="yellow"
                                  onClick={(e) => {
                                    e.stopPropagation();
                                    handleEditClick(opportunity);
                                  }}
                                >
                                  <IconEdit size={16} />
                                </ActionIcon>
                                <ActionIcon 
                                  variant="light" 
                                  color="red"
                                  onClick={(e) => {
                                    e.stopPropagation();
                                    handleDeleteClick(opportunity);
                                  }}
                                >
                                  <IconTrash size={16} />
                                </ActionIcon>
                              </Group>
                          )}
                        </Group>
                      </Group>
                      
                    </Card.Section>
                  </Card>
                </Grid.Col>
              ))}
            </Grid>
          ) : (
            <Paper shadow="md" radius="lg" className="p-10 text-center">
              <Title order={3} className="mb-2">No volunteer opportunities found (´。＿。｀)</Title>
              <Text>Try changing your search or filters!</Text>
            </Paper>
          )}
        </div>
      </div>

      {/* Opportunity Details Modal */}
      <Modal
        opened={opened && !!selectedOpportunity}
        onClose={close}
        size="lg"
        centered
        withCloseButton={true}
        styles={{
          body: { padding: 0 },
          content: { background: 'linear-gradient(to bottom, #f0f4ff, #fff1f9)' }
        }}
      >
        {selectedOpportunity && (
          <div className="flex flex-col p-0">
            {/* Main Image */}
            <div className="relative h-[200px]">
              <div
                className="absolute inset-0 z-0 overflow-hidden"
                style={{
                  backgroundImage: `url(${selectedOpportunity.image})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  filter: 'blur(5px) brightness(0.7)',
                }}
              ></div>
              <div className="relative z-10 flex justify-center items-center h-full">
                <img
                  src={selectedOpportunity.image}
                  alt={selectedOpportunity.title}
                  className="max-h-[180px] object-cover shadow-lg"
                  onError={(e) => {
                    e.target.src = "https://via.placeholder.com/400x200?text=Volunteer+Opportunity";
                  }}
                />
              </div>
            </div>
            
            {/* Opportunity Info Content */}
            <div className="p-4">
              <div className="flex justify-between items-center mb-3">
                <Badge 
                  variant="gradient" 
                  gradient={getCategoryColor(selectedOpportunity.category)}
                  size="md"
                  radius="md"
                >
                  {selectedOpportunity.category}
                </Badge>
                <Badge 
                  color={selectedOpportunity.status === 'active' ? 'green' : 'gray'}
                  size="md"
                  radius="md"
                >
                  {selectedOpportunity.status === 'active' ? 'Active' : 'Upcoming'}
                </Badge>
              </div>
              
              <Title order={3} className="mb-2">{selectedOpportunity.title}</Title>
              
              <div className="grid grid-cols-1 gap-2 mb-3">
                <div className="flex items-center">
                  <IconCalendar size={16} className="text-blue-500 mr-2" />
                  <Text size="sm">{new Date(selectedOpportunity.eventdate).toLocaleDateString()}</Text>
                </div>
                <div className="flex items-center">
                  <IconLocation size={16} className="text-blue-500 mr-2" />
                  <Text size="sm">{selectedOpportunity.location}</Text>
                </div>
              </div>
              
              <Divider className="my-3" />
              
              <Text className="text-sm mb-3">
                {selectedOpportunity.description}
              </Text>
              
              <div className="mb-3">
                <Group position="apart" mb="xs">
                  <Text size="sm" weight={500}>Volunteer Slots</Text>
                  <Text size="xs" color="dimmed">{selectedOpportunity.volunteers}/{selectedOpportunity.maxVolunteers}</Text>
                </Group>
                <Progress 
                  value={getProgressPercentage(selectedOpportunity.volunteers, selectedOpportunity.maxVolunteers)} 
                  color={selectedOpportunity.status === 'active' ? 'blue' : 'gray'} 
                  size="md" 
                  radius="xl"
                />
              </div>

              {selectedOpportunity.requirements && (
                <Paper withBorder radius="md" className="p-3 bg-blue-50 mb-3">
                  <Title order={5} className="mb-1 flex items-center">
                    <IconList size={16} className="mr-2 text-blue-500" />
                    Requirements
                  </Title>
                  <Text size="sm">{selectedOpportunity.requirements}</Text>
                </Paper>
              )}
              
              {selectedOpportunity.status === 'active' && selectedOpportunity.volunteers < selectedOpportunity.maxVolunteers && (
                <Button
                  fullWidth
                  size="md"
                  radius="xl"
                  variant="gradient"
                  gradient={{ from: 'pink', to: 'violet' }}
                  leftSection={<IconHeart size={18} />}
                  onClick={() => handleSignUp(selectedOpportunity)}
                  className="mb-3"
                >
                  Sign Me Up!
                </Button>
              )}
              
              {selectedOpportunity.status === 'upcoming' && (
                <Paper withBorder radius="md" className="p-3 bg-gray-50 mb-3 text-center">
                  <Text size="sm" color="dimmed">This opportunity is coming soon.</Text>
                </Paper>
              )}
              
              {selectedOpportunity.volunteers >= selectedOpportunity.maxVolunteers && (
                <Paper withBorder radius="md" className="p-3 bg-yellow-50 mb-3 text-center">
                  <Text size="sm" color="orange">All volunteer slots have been filled.</Text>
                </Paper>
              )}
              
              <Divider className="my-3" />
              
              <div className="flex items-center gap-3">
                <Avatar 
                  radius="xl"
                  size="md"
                  src={`https://api.dicebear.com/7.x/personas/svg?seed=${selectedOpportunity.contactperson}`}
                />
                <div>
                  <Text size="sm" weight={500}>{selectedOpportunity.contactperson}</Text>
                  <Button
                    variant="subtle"
                    compact
                    size="xs"
                    leftSection={<IconMail size={14} />}
                    component="a"
                    href={`mailto:${selectedOpportunity.contactemail}`}
                  >
                    {selectedOpportunity.contactemail}
                  </Button>
                </div>
              </div>

              <Button 
                fullWidth 
                variant="light" 
                className="mt-3" 
                radius="xl"
                onClick={close}
              >
                Close
              </Button>
            </div>
          </div>
        )}
      </Modal>
      {/* Create Opportunity Modal */}
      <Modal
        opened={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        title="Add New Volunteer Opportunity (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧"
        size="lg"
      >
        <form onSubmit={handleCreateSubmit}>
          <TextInput
            label="Title"
            required
            placeholder="What's this kawaii opportunity called?"
            value={volunteerForm.title}
            onChange={(e) => setVolunteerForm({...volunteerForm, title: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Category"
            data={[
              { value: 'Community Service', label: 'Community Service' },
              { value: 'Education', label: 'Education' },
              { value: 'Environment', label: 'Environment' },
              { value: 'Health', label: 'Health' },
              { value: 'Other', label: 'Other' }
            ]}
            value={volunteerForm.category}
            onChange={(value) => setVolunteerForm({...volunteerForm, category: value})}
            className="mb-3"
            required
          />
          
          <TextInput
            label="Location"
            placeholder="Where will this happen?"
            value={volunteerForm.location}
            onChange={(e) => setVolunteerForm({...volunteerForm, location: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Event Date"
            type="date"
            placeholder="When will this happen?"
            value={volunteerForm.eventDate}
            onChange={(e) => setVolunteerForm({...volunteerForm, eventDate: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Status"
            data={[
              { value: 'active', label: 'Active' },
              { value: 'upcoming', label: 'Upcoming' },
              { value: 'completed', label: 'Completed' }
            ]}
            value={volunteerForm.status}
            onChange={(value) => setVolunteerForm({...volunteerForm, status: value})}
            className="mb-3"
            required
          />
          
          <Textarea
            label="Description"
            placeholder="Tell us about this super kawaii opportunity!"
            required
            minRows={3}
            value={volunteerForm.description}
            onChange={(e) => setVolunteerForm({...volunteerForm, description: e.target.value})}
            className="mb-3"
          />
          
          <Textarea
            label="Requirements"
            placeholder="Any special requirements? (◕‿◕✿)"
            minRows={2}
            value={volunteerForm.requirements}
            onChange={(e) => setVolunteerForm({...volunteerForm, requirements: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Contact Person"
            placeholder="Who's the friendly contact person?"
            value={volunteerForm.contactPerson}
            onChange={(e) => setVolunteerForm({...volunteerForm, contactPerson: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Contact Email"
            placeholder="Email for inquiries"
            value={volunteerForm.contactEmail}
            onChange={(e) => setVolunteerForm({...volunteerForm, contactEmail: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Registration Link"
            placeholder="https://example.com/register"
            value={volunteerForm.registerLink}
            onChange={(e) => setVolunteerForm({...volunteerForm, registerLink: e.target.value})}
            className="mb-3"
          />
          
          <FileInput
            label="Event Image (Optional)"
            placeholder="Upload a kawaii image!"
            accept="image/*"
            onChange={(file) => setVolunteerForm({...volunteerForm, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={() => setCreateModalOpen(false)}>Cancel</Button>
            <Button 
              type="submit" 
              variant="gradient" 
              gradient={{ from: 'pink', to: 'violet' }}
            >
              Create Opportunity ✨
            </Button>
          </Group>
        </form>
      </Modal>

      {/* Edit Opportunity Modal */}
      <Modal
        opened={editModalOpen}
        onClose={() => setEditModalOpen(false)}
        title="Edit Volunteer Opportunity (◠‿◠✿)"
        size="lg"
      >
        <form onSubmit={handleEditSubmit}>
          <TextInput
            label="Title"
            required
            placeholder="What's this kawaii opportunity called?"
            value={volunteerForm.title}
            onChange={(e) => setVolunteerForm({...volunteerForm, title: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Category"
            data={[
              { value: 'Community Service', label: 'Community Service' },
              { value: 'Education', label: 'Education' },
              { value: 'Environment', label: 'Environment' },
              { value: 'Health', label: 'Health' },
              { value: 'Other', label: 'Other' }
            ]}
            value={volunteerForm.category}
            onChange={(value) => setVolunteerForm({...volunteerForm, category: value})}
            className="mb-3"
            required
          />
          
          <TextInput
            label="Location"
            placeholder="Where will this happen?"
            value={volunteerForm.location}
            onChange={(e) => setVolunteerForm({...volunteerForm, location: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Event Date"
            type="date"
            placeholder="When will this happen?"
            value={volunteerForm.eventDate}
            onChange={(e) => setVolunteerForm({...volunteerForm, eventDate: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Status"
            data={[
              { value: 'active', label: 'Active' },
              { value: 'upcoming', label: 'Upcoming' },
              { value: 'completed', label: 'Completed' }
            ]}
            value={volunteerForm.status}
            onChange={(value) => setVolunteerForm({...volunteerForm, status: value})}
            className="mb-3"
            required
          />
          
          <Textarea
            label="Description"
            placeholder="Tell us about this super kawaii opportunity!"
            required
            minRows={3}
            value={volunteerForm.description}
            onChange={(e) => setVolunteerForm({...volunteerForm, description: e.target.value})}
            className="mb-3"
          />
          
          <Textarea
            label="Requirements"
            placeholder="Any special requirements? (◕‿◕✿)"
            minRows={2}
            value={volunteerForm.requirements}
            onChange={(e) => setVolunteerForm({...volunteerForm, requirements: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Contact Person"
            placeholder="Who's the friendly contact person?"
            value={volunteerForm.contactPerson}
            onChange={(e) => setVolunteerForm({...volunteerForm, contactPerson: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Contact Email"
            placeholder="Email for inquiries"
            value={volunteerForm.contactEmail}
            onChange={(e) => setVolunteerForm({...volunteerForm, contactEmail: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Registration Link"
            placeholder="https://example.com/register"
            value={volunteerForm.registerLink}
            onChange={(e) => setVolunteerForm({...volunteerForm, registerLink: e.target.value})}
            className="mb-3"
          />
          
          <FileInput
            label="Event Image (Optional)"
            description="Leave empty to keep existing image ✧*。ヾ(｡>﹏<｡)ﾉﾞ✧*。"
            placeholder="Upload a new kawaii image!"
            accept="image/*"
            onChange={(file) => setVolunteerForm({...volunteerForm, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={() => setEditModalOpen(false)}>Cancel</Button>
            <Button 
              type="submit" 
              color="yellow"
            >
              Update Opportunity (❁´◡`❁)
            </Button>
          </Group>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        opened={deleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        title="Delete Volunteer Opportunity (⊙﹏⊙)"
        size="sm"
      >
        {selectedOpportunity && (
          <>
            <Text>Are you sure you want to delete "{selectedOpportunity.title}"?</Text>
            <Text size="sm" color="dimmed" className="mt-2">
              This action cannot be undone. (｡•́︿•̀｡)
            </Text>
            
            <Group position="right" className="mt-4">
              <Button variant="outline" onClick={() => setDeleteModalOpen(false)}>Cancel</Button>
              <Button color="red" onClick={handleDeleteConfirm}>Delete</Button>
            </Group>
          </>
        )}
      </Modal>
    </>
  );
};

export default VolunteerHub;