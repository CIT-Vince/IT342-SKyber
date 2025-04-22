import React, { useState } from 'react';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';
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
  ActionIcon,
  Divider,
  Progress,
  Modal,
  Button,
  Avatar
} from '@mantine/core';
import { 
  IconSearch, 
  IconCalendar, 
  IconLocation, 
  IconMail,
  IconUser,
  IconArrowRight,
  IconArrowLeft,
  IconList,
  IconHeart
} from '@tabler/icons-react';

// Enhanced opportunities data
const mockOpportunities = [
  {
    id: 1,
    title: "Tree Planting Day",
    description: "Help us plant 500 trees in our barangay to combat climate change and create a greener environment for future generations! Join our community effort to make our surroundings more beautiful and environmentally sustainable. 🌱",
    category: "Environment",
    location: "Barangay Park",
    eventdate: "2025-05-10",
    contactperson: "Ate Maria",
    contactemail: "maria@skyber.com",
    status: "active",
    requirements: "Bring gloves and water bottle. Comfortable clothing and shoes recommended.",
    volunteers: 12,
    maxVolunteers: 30,
    image: "https://images.unsplash.com/photo-1552084117-56a987666449?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400"
  },
  {
    id: 2,
    title: "Feeding Program",
    description: "Assist in preparing and distributing nutritious meals for children in our community. This program aims to combat malnutrition and ensure that every child has access to healthy food.",
    category: "Community Service",
    location: "Barangay Hall",
    eventdate: "2025-05-20",
    contactperson: "Kuya Juan",
    contactemail: "juan@skyber.com",
    status: "ended",
    requirements: "Food handler certificate preferred. Must be comfortable working with children.",
    volunteers: 15,
    maxVolunteers: 15,
    image: "https://images.unsplash.com/photo-1488521787991-ed7bbaae773c?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400"
  },
  {
    id: 3,
    title: "Coastal Clean-up Drive",
    description: "Join us in cleaning our beautiful beaches and protecting marine life from harmful plastic waste and pollution. Let's work together to preserve our natural resources!",
    category: "Environment",
    location: "Barangay Beach Front",
    eventdate: "2025-06-05",
    contactperson: "Kuya Pedro",
    contactemail: "pedro@skyber.com",
    status: "active",
    requirements: "Bring reusable gloves, water bottle, and sun protection.",
    volunteers: 8,
    maxVolunteers: 25,
    image: "https://images.unsplash.com/photo-1567095761054-7a02e69e5c43?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400"
  },
  {
    id: 4,
    title: "Elderly Care Visit",
    description: "Spend time with elderly residents at the local care home. Activities include reading, playing board games, and simply offering companionship to brighten their day.",
    category: "Healthcare",
    location: "Golden Years Care Home",
    eventdate: "2025-06-12",
    contactperson: "Ate Rosa",
    contactemail: "rosa@skyber.com",
    status: "active",
    requirements: "Patient and compassionate individuals. Basic knowledge of Filipino dialects helpful.",
    volunteers: 5,
    maxVolunteers: 10,
    image: "https://images.unsplash.com/photo-1516307365426-bea591f05011?ixlib=rb-4.0.3&q=80&fm=jpg&crop=entropy&cs=tinysrgb&w=400"
  }
];

const VolunteerHub = () => {
  const [opportunities, setOpportunities] = useState(mockOpportunities);
  const [activeStatus, setActiveStatus] = useState('all');
  const [searchQuery, setSearchQuery] = useState('');
  const [selectedOpportunity, setSelectedOpportunity] = useState(null);
  const [opened, { open, close }] = useDisclosure(false);

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
    // Update local state to reflect sign-up
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
    switch(category) {
      case 'Environment': return { from: 'green', to: 'cyan' };
      case 'Community Service': return { from: 'blue', to: 'violet' };
      case 'Healthcare': return { from: 'pink', to: 'red' };
      default: return { from: 'blue', to: 'cyan' };
    }
  };

  return (
    <>
      {/* Header with gradient background */}
      <div
        className="w-full h-auto"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
        <Navbar />
        <header className="text-left py-10 pl-10">
          <Title className="text-5xl font-bold text-white">
            Volunteer Hub
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Find opportunities to help your community and earn rewards! (๑˃ᴗ˂)ﻭ
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
                    <Tabs.Tab value="ended">Ended</Tabs.Tab>
                  </Tabs.List>
                </Tabs>
              </Grid.Col>
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
                          {opportunity.status === 'active' ? 'Active' : 'Ended'}
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
        fullScreen
        centered
        withCloseButton={false}
        styles={{
          body: { padding: 0 },
          content: { background: 'linear-gradient(to bottom, #f0f4ff, #fff1f9)' }
        }}
      >
        {selectedOpportunity && (
          <div className="min-h-screen flex flex-col">
            {/* Header with Back Button */}
            <div className="sticky top-0 z-50 bg-gradient-to-r from-blue-600 to-indigo-800 text-white p-4 flex items-center">
              <Button 
                variant="subtle" 
                color="white" 
                leftSection={<IconArrowLeft />}
                onClick={close}
              >
                Back
              </Button>
              <Title order={3} className="mx-auto pr-10">Volunteer Opportunity</Title>
            </div>

            {/* Main Image with Blurred Background */}
            <div className="relative h-[400px]">
              <div
                className="absolute inset-0 z-0 overflow-hidden"
                style={{
                  backgroundImage: `url(${selectedOpportunity.image})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  filter: 'blur(20px) brightness(0.6)',
                  transform: 'scale(1.1)',
                }}
              ></div>
              <div className="relative z-10 flex justify-center items-center h-full">
                <img
                  src={selectedOpportunity.image}
                  alt={selectedOpportunity.title}
                  className="rounded-lg shadow-lg max-h-[350px] object-cover"
                />
              </div>
            </div>
            
            {/* Opportunity Info Content */}
            <Paper radius="lg" className="mx-4 -mt-10 relative z-20 p-6 shadow-lg">
              <div className="flex justify-between items-center mb-3">
                <Badge 
                  variant="gradient" 
                  gradient={getCategoryColor(selectedOpportunity.category)}
                  size="lg"
                  radius="md"
                >
                  {selectedOpportunity.category}
                </Badge>
                <Badge 
                  color={selectedOpportunity.status === 'active' ? 'green' : 'gray'}
                  size="lg"
                  radius="md"
                >
                  {selectedOpportunity.status === 'active' ? 'Active' : 'Ended'}
                </Badge>
              </div>
              
              <Title order={2} className="mb-4">{selectedOpportunity.title}</Title>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <div className="flex items-center">
                  <IconCalendar size={18} className="text-blue-500 mr-2" />
                  <Text weight={500}>{new Date(selectedOpportunity.eventdate).toLocaleDateString()}</Text>
                </div>
                <div className="flex items-center">
                  <IconLocation size={18} className="text-blue-500 mr-2" />
                  <Text>{selectedOpportunity.location}</Text>
                </div>
              </div>
              
              <Divider className="my-4" />
              
              <Text className="text-lg leading-relaxed mb-6">
                {selectedOpportunity.description}
              </Text>
              
              <div className="mb-6">
                <Group position="apart" mb="xs">
                  <Text size="md" weight={500}>Volunteer Slots</Text>
                  <Text size="sm" color="dimmed">{selectedOpportunity.volunteers}/{selectedOpportunity.maxVolunteers}</Text>
                </Group>
                <Progress 
                  value={getProgressPercentage(selectedOpportunity.volunteers, selectedOpportunity.maxVolunteers)} 
                  color={selectedOpportunity.status === 'active' ? 'blue' : 'gray'} 
                  size="md" 
                  radius="xl"
                />
              </div>

              <Paper withBorder radius="md" className="p-4 bg-blue-50 mb-6">
                <Title order={4} className="mb-2 flex items-center">
                  <IconList size={18} className="mr-2 text-blue-500" />
                  Requirements
                </Title>
                <Text>{selectedOpportunity.requirements}</Text>
              </Paper>
              
              {selectedOpportunity.status === 'active' && selectedOpportunity.volunteers < selectedOpportunity.maxVolunteers && (
                <Button
                  fullWidth
                  size="lg"
                  radius="xl"
                  variant="gradient"
                  gradient={{ from: 'pink', to: 'violet' }}
                  leftSection={<IconHeart size={20} />}
                  onClick={() => handleSignUp(selectedOpportunity)}
                  className="mb-4"
                >
                  Sign Me Up!
                </Button>
              )}
              
              {selectedOpportunity.status === 'ended' && (
                <Paper withBorder radius="md" className="p-4 bg-gray-50 mb-6 text-center">
                  <Text color="dimmed">This volunteer opportunity has ended.</Text>
                </Paper>
              )}
              
              {selectedOpportunity.volunteers >= selectedOpportunity.maxVolunteers && (
                <Paper withBorder radius="md" className="p-4 bg-yellow-50 mb-6 text-center">
                  <Text color="orange">All volunteer slots have been filled.</Text>
                </Paper>
              )}
            </Paper>
            
            {/* Contact Information */}
            <Paper radius="lg" className="mx-4 mt-6 p-6 shadow-md">
              <Title order={3} className="mb-4">Contact Information</Title>
              <Divider className="mb-4" />
              
              <div className="flex flex-col md:flex-row gap-4">
                <div className="flex items-center">
                  <Avatar 
                    radius="xl"
                    size="lg"
                    src={`https://api.dicebear.com/7.x/personas/svg?seed=${selectedOpportunity.contactperson}`}
                    className="mr-3"
                  />
                  <div>
                    <Text weight={500}>{selectedOpportunity.contactperson}</Text>
                    <Text size="sm" color="dimmed">Organizer</Text>
                  </div>
                </div>
                
                <div className="flex-grow md:ml-6 md:border-l md:pl-6">
                  <Group>
                    <Button
                      variant="light"
                      radius="xl"
                      leftSection={<IconMail size={16} />}
                      component="a"
                      href={`mailto:${selectedOpportunity.contactemail}`}
                    >
                      {selectedOpportunity.contactemail}
                    </Button>
                  </Group>
                </div>
              </div>
              
              {/* Bottom Close Button */}
              <Button 
                fullWidth 
                variant="default" 
                className="mt-6" 
                radius="xl"
                onClick={close}
              >
                Close Details
              </Button>
            </Paper>
          </div>
        )}
      </Modal>
    </>
  );
};

export default VolunteerHub;