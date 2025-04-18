import React, { useState } from 'react';
import { useNavigate } from 'react-router-dom';
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
  Tooltip,
  Avatar
} from '@mantine/core';
import { 
  IconSearch, 
  IconCalendar, 
  IconShare, 
  IconArrowRight, 
  IconClock, 
  IconArrowLeft,
  IconUsers
} from '@tabler/icons-react';
import sample1 from '../../assets/proj/sample1.png';
import sample2 from '../../assets/proj/sample2.png';
import sample3 from '../../assets/proj/sample3.png';

// Renamed from announcements to projects for clarity
const projects = [
  {
    id: 1,
    title: 'Community Garden Renovation',
    status: 'Complete', // Changed from category to status
    startDate: '04/09/2025',
    endDate: '05/09/2025',
    image: sample1,
    description: 'Revitalizing the local community garden with new plants, accessible pathways, and sustainable irrigation systems.',
    progress: 100, // Added progress percentage
    volunteers: 24, // Added volunteer count
    budget: '₱120,000',
    manager: {
      name: "Vince Kimlo",
      image: "https://i.pravatar.cc/150?img=32"
    },
    teamMembers: [
      { name: "Team Member 1", image: "https://i.pravatar.cc/150?img=33" },
      { name: "Team Member 2", image: "https://i.pravatar.cc/150?img=34" },
      { name: "Team Member 3", image: "https://i.pravatar.cc/150?img=35" },
    ],
    stakeholders: ["Local Community Garden Committee", "Environmental Protection Group", "Municipal Parks Department"],
    sustainabilityGoal: "Green Spaces & Biodiversity"
  },
  {
    id: 2,
    title: 'Youth Sports Center Construction',
    status: 'Pending',
    startDate: '04/10/2025',
    endDate: '05/10/2025',
    image: sample2,
    description: 'Building a multi-purpose sports facility for youth activities including basketball, volleyball, and other recreational programs.',
    progress: 60,
    volunteers: 18,
    budget: '₱450,000',
    manager: {
      name: "Maria Santos",
      image: "https://i.pravatar.cc/150?img=44"
    },
    teamMembers: [
      { name: "Team Member 1", image: "https://i.pravatar.cc/150?img=45" },
      { name: "Team Member 2", image: "https://i.pravatar.cc/150?img=46" },
    ],
    stakeholders: ["Youth Sports Association", "Local Schools", "Municipal Recreation Department"],
    sustainabilityGoal: "Health & Well-being"
  },
  {
    id: 3,
    title: 'Local Library Technology Upgrade',
    status: 'Closed',
    startDate: '04/11/2025',
    endDate: '05/11/2025',
    image: sample3,
    description: 'Updating computers, adding free WiFi, and creating a digital learning center in our community library.',
    progress: 0,
    volunteers: 12,
    budget: '₱250,000',
    manager: {
      name: "Juan Cruz",
      image: "https://i.pravatar.cc/150?img=60"
    },
    teamMembers: [
      { name: "Team Member 1", image: "https://i.pravatar.cc/150?img=61" },
      { name: "Team Member 2", image: "https://i.pravatar.cc/150?img=62" },
      { name: "Team Member 3", image: "https://i.pravatar.cc/150?img=63" },
      { name: "Team Member 4", image: "https://i.pravatar.cc/150?img=64" },
    ],
    stakeholders: ["Public Library Board", "Digital Literacy Foundation", "Community Education Center"],
    sustainabilityGoal: "Quality Education"
  },
];

const Projects = () => {
  const [searchQuery, setSearchQuery] = useState('');
  const [activeStatus, setActiveStatus] = useState('All');
  const [projectData, setProjectData] = useState(projects);
  const [selectedProject, setSelectedProject] = useState(null);
  const [opened, { open, close }] = useDisclosure(false);

  // Filter projects based on search and status
  const filteredProjects = projectData.filter(project => {
    const matchesSearch = project.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         project.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = activeStatus === 'All' || project.status === activeStatus;
    
    return matchesSearch && matchesStatus;
  });

  // Handle card click to open modal
  const handleCardClick = (project) => {
    setSelectedProject(project);
    open();
  };

  // Get status color for badges and progress bars
  const getStatusColor = (status) => {
    switch(status) {
      case 'Complete': return 'green';
      case 'Pending': return 'blue';
      case 'Closed': return 'red';
      default: return 'gray';
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
            Projects
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Explore our ongoing and completed community projects! (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
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
                  placeholder="Search projects..."
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
                    <Tabs.Tab value="All">All</Tabs.Tab>
                    <Tabs.Tab value="Complete">Complete</Tabs.Tab>
                    <Tabs.Tab value="Pending">Pending</Tabs.Tab>
                    <Tabs.Tab value="Closed">Closed</Tabs.Tab>
                  </Tabs.List>
                </Tabs>
              </Grid.Col>
            </Grid>
          </Paper>

          {/* Projects Cards */}
          {filteredProjects.length > 0 ? (
            <Grid gutter="lg">
              {filteredProjects.map((project) => (
                <Grid.Col key={project.id} span={{ base: 12, sm: 6, lg: 4 }}>
                  <Card 
                    shadow="md" 
                    radius="md" 
                    className="h-full flex flex-col transition-transform duration-300 hover:scale-[1.02]"
                    onClick={() => handleCardClick(project)}
                  >
                    <Card.Section>
                      <div className="relative">
                        <img 
                          src={project.image} 
                          alt={project.title} 
                          className="w-full h-48 object-cover" 
                        />
                        <Badge 
                          variant="gradient" 
                          gradient={{ 
                            from: project.status === 'Complete' ? 'green' : 
                                  project.status === 'Pending' ? 'blue' : 'red', 
                            to: 'cyan' 
                          }}
                          className="absolute top-3 left-3"
                          size="lg"
                          radius="md"
                        >
                          {project.status}
                        </Badge>
                      </div>
                    </Card.Section>

                    <div className="p-4 flex-grow">
                      <Title order={3} className="mb-2">
                        {project.title}
                      </Title>
                      
                      <Text color="dimmed" size="sm" className="mb-3">
                        {project.description}
                      </Text>
                      
                      <Group className="mb-2 flex-wrap">
                        <Text size="xs" className="flex items-center text-gray-600">
                          <IconCalendar size={14} className="mr-1 text-blue-600" /> 
                          {project.startDate} - {project.endDate}
                        </Text>
                        <Badge color="blue" variant="light">Budget: {project.budget}</Badge>
                      </Group>
                      
                      <div className="mt-3">
                        <Group position="apart" mb="xs">
                          <Text size="sm" weight={500}>Project Progress</Text>
                          <Text size="xs" color="dimmed">{project.progress}%</Text>
                        </Group>
                        <Progress 
                          value={project.progress} 
                          color={getStatusColor(project.status)} 
                          size="sm" 
                          radius="xl"
                        />
                      </div>
                    </div>

                    <Card.Section className="p-3 bg-gradient-to-r from-blue-50 to-pink-50">
                      <Group position="apart">
                        <Group spacing="xs">
                          <Text size="xs" color="dimmed">{project.volunteers} Volunteers</Text>
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
              <Title order={3} className="mb-2">No projects found (´。＿。｀)</Title>
              <Text>Try changing your search or filters!</Text>
            </Paper>
          )}
        </div>
      </div>

      {/* Project Details Modal */}
      <Modal
        opened={opened && !!selectedProject}
        onClose={close}
        fullScreen
        centered
        withCloseButton={false}
        styles={{
          body: { padding: 0 },
          content: { background: 'linear-gradient(to bottom, #f0f4ff, #fff1f9)' }
        }}
      >
        {selectedProject && (
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
              <Title order={3} className="mx-auto pr-10">Project Details</Title>
            </div>

            {/* Main Image with Blurred Background */}
            <div className="relative h-[400px]">
              <div
                className="absolute inset-0 z-0 overflow-hidden"
                style={{
                  backgroundImage: `url(${selectedProject.image})`,
                  backgroundSize: 'cover',
                  backgroundPosition: 'center',
                  filter: 'blur(20px) brightness(0.6)',
                  transform: 'scale(1.1)',
                }}
              ></div>
              <div className="relative z-10 flex justify-center items-center h-full">
                <img
                  src={selectedProject.image}
                  alt={selectedProject.title}
                  className="rounded-lg shadow-lg max-h-[350px] object-cover"
                />
              </div>
            </div>
            
            {/* Project Info Content */}
            <Paper radius="lg" className="mx-4 -mt-10 relative z-20 p-6 shadow-lg">
              <div className="flex justify-between items-center mb-3">
                <Badge 
                  variant="gradient" 
                  gradient={{ from: getStatusColor(selectedProject.status), to: 'cyan' }}
                  size="lg"
                  radius="md"
                >
                  {selectedProject.status}
                </Badge>
                <Group>
                  <Text size="sm" className="flex items-center">
                    <IconCalendar size={16} className="mr-1" /> {selectedProject.startDate} - {selectedProject.endDate}
                  </Text>
                </Group>
              </div>
              
              <Title order={2} className="mb-4">{selectedProject.title}</Title>
              
              <div className="grid grid-cols-1 md:grid-cols-2 gap-4 mb-4">
                <div className="flex items-center">
                  <Badge size="lg" color="blue" className="mr-2">Budget:</Badge>
                  <Text weight={500}>{selectedProject.budget}</Text>
                </div>
                <div className="flex items-center">
                  <Badge size="lg" color="teal" className="mr-2">Sustainability Goal:</Badge>
                  <Text>{selectedProject.sustainabilityGoal}</Text>
                </div>
              </div>
              
              <Divider className="my-4" />
              
              <Text className="text-lg leading-relaxed mb-6">
                {selectedProject.description}
              </Text>
              
              <div className="mb-4">
                <Group position="apart" mb="xs">
                  <Text size="md" weight={500}>Project Progress</Text>
                  <Text size="sm" color="dimmed">{selectedProject.progress}%</Text>
                </Group>
                <Progress 
                  value={selectedProject.progress} 
                  color={getStatusColor(selectedProject.status)} 
                  size="md" 
                  radius="xl"
                />
              </div>
            </Paper>
            
            {/* Team Information */}
            <Paper radius="lg" className="mx-4 mt-6 p-6 shadow-lg bg-gradient-to-r from-blue-600 to-indigo-800 text-white">
              <Title order={3} className="mb-4">Project Team</Title>
              
              <div className="flex flex-col md:flex-row gap-6">
                {/* Project Manager */}
                <div className="flex-shrink-0 text-center">
                  <Avatar 
                    src={selectedProject.manager.image} 
                    size={100} 
                    radius="xl"
                    className="mx-auto border-2 border-white"
                  />
                  <Text className="mt-2 font-bold">{selectedProject.manager.name}</Text>
                  <Badge color="pink" className="mt-1">Project Manager</Badge>
                </div>
                
                {/* Vertical Divider */}
                <div className="hidden md:block w-px bg-white self-stretch opacity-50" />
                
                {/* Right Column - Team Members and Stakeholders */}
                <div className="flex-1">
                  <div className="grid grid-cols-1 md:grid-cols-2 gap-6">
                    {/* Team Members */}
                    <div>
                      <Group className="mb-2 items-center">
                        <IconUsers size={18} />
                        <Text weight={600}>Team Members</Text>
                      </Group>
                      <div className="flex flex-wrap gap-4">
                        {selectedProject.teamMembers.map((member, index) => (
                          <div key={index} className="flex flex-col items-center">
                            <Avatar 
                              src={member.image} 
                              size={60} 
                              radius="xl"
                              className="border-2 border-white"
                            />
                            <Text size="xs" className="mt-1">{member.name}</Text>
                          </div>
                        ))}
                      </div>
                    </div>
                    
                    {/* Stakeholders */}
                    <div>
                      <Text weight={600} className="mb-2">Stakeholders</Text>
                      <ul className="list-disc list-inside space-y-1 pl-2">
                        {selectedProject.stakeholders.map((stakeholder, index) => (
                          <li key={index}>{stakeholder}</li>
                        ))}
                      </ul>
                    </div>
                  </div>
                </div>
              </div>
            </Paper>
            
            {/* Other Projects */}
            <Paper radius="lg" className="mx-4 mt-6 p-6 shadow-md">
              <Title order={3} className="mb-4">Other Projects</Title>
              <Divider className="mb-4" />
              
              <Grid>
                {projectData
                  .filter(p => p.id !== selectedProject.id)
                  .slice(0, 2)
                  .map((project) => (
                    <Grid.Col key={project.id} span={{ base: 12, md: 6 }}>
                      <Card 
                        className="flex gap-4 hover:bg-gray-50 transition cursor-pointer"
                        onClick={() => setSelectedProject(project)}
                        padding="sm"
                      >
                        <img 
                          src={project.image} 
                          alt={project.title} 
                          className="w-24 h-24 object-cover rounded-md" 
                        />
                        <div>
                          <Group>
                            <Badge size="sm" color={getStatusColor(project.status)}>{project.status}</Badge>
                            <Text size="xs" c="dimmed">{project.startDate} - {project.endDate}</Text>
                          </Group>
                          <Text weight={500}>{project.title}</Text>
                          <Text size="sm" lineClamp={2} color="dimmed">
                            {project.description.substring(0, 80)}...
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
                onClick={close}
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

export default Projects;