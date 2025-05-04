import React, { useState, useEffect } from 'react';
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
  Avatar,
  HoverCard,
  Select ,
  Textarea ,
  FileInput 
} from '@mantine/core';
import { 
  IconSearch, 
  IconCalendar, 
  IconShare, 
  IconArrowRight, 
  IconClock, 
  IconArrowLeft,
  IconUsers,
  IconPlus ,
  IconEdit ,
  IconTrash ,
  IconEye 
} from '@tabler/icons-react';
import sample1 from '../../assets/proj/sample1.png';
import sample2 from '../../assets/proj/sample2.png';
import sample3 from '../../assets/proj/sample3.png';
import { showNotification } from '@mantine/notifications';
import { useAuth } from '../../contexts/AuthContext';

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
  const [loading, setLoading] = useState(true); 
  const [error, setError] = useState(null); 
  const { currentUser } = useAuth();
const [isAdmin, setIsAdmin] = useState(false);
const [createModalOpen, { open: openCreateModal, close: closeCreateModal }] = useDisclosure(false);
const [editModalOpen, { open: openEditModal, close: closeEditModal }] = useDisclosure(false);
const [deleteModalOpen, { open: openDeleteModal, close: closeDeleteModal }] = useDisclosure(false);
const [projectForm, setProjectForm] = useState({
  projectName: '',
  description: '',
  budget: '',
  startDate: '',
  endDate: '',
  status: 'Planning',
  projectManager: '',
  teamMembers: '',
  stakeholders: '',
  sustainabilityGoals: '',
  imageFile: null
});
const API_BASE_URL = import.meta.env.MODE === 'production' 
  ? 'https://skyber.onrender.com' 
  : '';


  // Filters
  const filteredProjects = projectData.filter(project => {
    const matchesSearch = project.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         project.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesStatus = activeStatus === 'All' || project.status === activeStatus;
    
    return matchesSearch && matchesStatus;
  });

  const handleCardClick = (project) => {
    setSelectedProject(project);
    open();
  };

  const getStatusColor = (status) => {
    switch(status) {
      case 'Complete': return 'green';
      case 'Pending': return 'blue';
      case 'Closed': return 'red';
      default: return 'gray';
    }
  };

    const formatBudget = (budgetValue) => {
      // Handle null, undefined or empty values
      if (!budgetValue) return "₱0";
      
      // If already has peso sign, just return it
      if (typeof budgetValue === 'string' && budgetValue.includes('₱')) {
        return budgetValue;
      }
      
      // Remove any non-numeric characters
      const numericValue = budgetValue.toString().replace(/[^\d.-]/g, '');
      
      try {
        const formattedValue = Number(numericValue).toLocaleString('en-PH');
        return `₱${formattedValue}`;
      } catch (error) {
        return `₱${budgetValue}`;
      }
    };
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

  useEffect(() => {
    const fetchProjects = async () => {
      try {
        setLoading(true);
        
        const API_URL = `${API_BASE_URL}/api/projects/all`;
        console.log("Fetching projects from:", API_URL);
        
        const response = await fetch(API_URL);
        
        // Better error handling
        if (!response.ok) {
          console.error(`Server responded with ${response.status}: ${response.statusText}`);
          throw new Error(`Server responded with ${response.status}`);
        }
        
        const data = await response.json();
        console.log("Retrieved projects data:", data);
        
        if (data && data.length > 0) {
          const transformedData = data.map(item => ({
            id: item.id || Math.random().toString(),
            title: item.projectName || "Untitled Project",
            status: item.status || "Pending",
            startDate: item.startDate || "N/A",
            endDate: item.endDate || "N/A",
            image: item.projectImage ? `data:image/jpeg;base64,${item.projectImage}` : sample1,
            description: item.description || "No description provided.",
            progress: calculateProgress(item.status),
            volunteers: 15, 
            budget: formatBudget(item.budget), 
            manager: {
              name: item.projectManager || "Not Assigned",
              image: "https://i.pravatar.cc/150?img=32"
            },
            teamMembers: parseTeamMembers(item.teamMembers),
            stakeholders: parseStakeholders(item.stakeholders),
            sustainabilityGoal: item.sustainabilityGoals || "None specified"
          }));
          
          setProjectData(transformedData);
          setError(null);
          
        } else {
          console.warn("Server returned empty projects data");
          throw new Error("No projects found in server response");
        }
      } catch (error) {
        console.error("Error fetching projects:", error);
        setError(error.message);
        
        setProjectData(projects);
        
        showNotification({
          title: 'Using Sample Data',
          message: 'Could not load projects from server. Showing sample data.',
          color: 'yellow'
        });
      } finally {
        setLoading(false);
      }
    };
    
    const calculateProgress = (status) => {
      switch(status) {
        case 'Complete': return 100;
        case 'Closed': return 0;
        default: return Math.floor(Math.random() * 60) + 20; // 20-80% for ongoing projects
      }
    };
    
    const parseTeamMembers = (teamMembersString) => {
      if (!teamMembersString) return [];
      
      const names = teamMembersString.split(',').map(name => name.trim());
      
      return names.map((name, index) => ({
        name: name,
        image: `https://i.pravatar.cc/150?img=${30 + index}`
      }));
    };
    
    const parseStakeholders = (stakeholdersString) => {
      if (!stakeholdersString) return [];
      return stakeholdersString.split(',').map(s => s.trim());
    };
    
    fetchProjects();
  }, []);

// Add these handler functions
const handleEditClick = (project) => {
  setSelectedProject(project);
  
  // Format dates for the form
  const startDate = project.startDate && project.startDate !== 'N/A' 
    ? project.startDate 
    : '';
  
  const endDate = project.endDate && project.endDate !== 'N/A'
    ? project.endDate
    : '';
  
  // Remove peso sign and commas from budget
  const rawBudget = project.budget 
    ? project.budget.replace('₱', '').replace(/,/g, '') 
    : '';
  
  setProjectForm({
    projectName: project.title,
    description: project.description,
    budget: rawBudget,
    startDate: startDate,
    endDate: endDate,
    status: project.status,
    projectManager: project.manager?.name || '',
    teamMembers: project.teamMembers.map(member => member.name).join(', '),
    stakeholders: project.stakeholders.join(', '),
    sustainabilityGoals: project.sustainabilityGoal,
    imageFile: null
  });
  
  openEditModal();
};

const handleDeleteClick = (project) => {
  setSelectedProject(project);
  openDeleteModal();
};

const resetForm = () => {
  setProjectForm({
    projectName: '',
    description: '',
    budget: '',
    startDate: '',
    endDate: '',
    status: 'Planning',
    projectManager: '',
    teamMembers: '',
    stakeholders: '',
    sustainabilityGoals: '',
    imageFile: null
  });
};

const handleCreateClick = () => {
  resetForm();
  openCreateModal();
};

const handleCreateSubmit = async (e) => {
  e.preventDefault();
  
  // Validate required fields
  if (!projectForm.projectName || !projectForm.description) {
    showNotification({
      title: 'Validation Error',
      message: 'Project name and description are required',
      color: 'red'
    });
    return;
  }
  
  // Validate budget is a number
  const budgetValue = parseFloat(projectForm.budget);
  if (isNaN(budgetValue)) {
    showNotification({
      title: 'Validation Error',
      message: 'Budget must be a valid number',
      color: 'red'
    });
    return;
  }
  
  // Validate dates
  if (!projectForm.startDate || !projectForm.endDate) {
    showNotification({
      title: 'Validation Error',
      message: 'Start date and end date are required',
      color: 'red'
    });
    return;
  }
  
  try {
    setLoading(true);
    
    // Format dates to ISO format (YYYY-MM-DD)
    const formatDate = (dateStr) => {
      // If already in ISO format, return as is
      if (/^\d{4}-\d{2}-\d{2}$/.test(dateStr)) return dateStr;
      
      // If in MM/DD/YYYY format, convert to ISO
      const parts = dateStr.split('/');
      if (parts.length === 3) {
        return `${parts[2]}-${parts[0].padStart(2, '0')}-${parts[1].padStart(2, '0')}`;
      }
      
      // Default to today if parsing fails
      const today = new Date();
      return today.toISOString().split('T')[0];
    };
    
    const formDataObj = new FormData();
    formDataObj.append('projectName', projectForm.projectName);
    formDataObj.append('description', projectForm.description);
    formDataObj.append('budget', budgetValue); // Send as number
    formDataObj.append('startDate', formatDate(projectForm.startDate));
    formDataObj.append('endDate', formatDate(projectForm.endDate));
    formDataObj.append('status', projectForm.status || 'Planning');
    formDataObj.append('projectManager', projectForm.projectManager || 'Not Assigned');
    
    // Optional fields
    if (projectForm.teamMembers) {
      formDataObj.append('teamMembers', projectForm.teamMembers);
    }
    
    if (projectForm.stakeholders) {
      formDataObj.append('stakeholders', projectForm.stakeholders);
    }
    
    if (projectForm.sustainabilityGoals) {
      formDataObj.append('sustainabilityGoals', projectForm.sustainabilityGoals);
    }
    
    if (projectForm.imageFile) {
      formDataObj.append('image', projectForm.imageFile);
    }
    
    console.log("Sending project data to API:", {
      projectName: projectForm.projectName,
      description: projectForm.description,
      budget: budgetValue,
      startDate: formatDate(projectForm.startDate),
      endDate: formatDate(projectForm.endDate),
      status: projectForm.status || 'Planning',
      projectManager: projectForm.projectManager || 'Not Assigned'
    });
    
    const response = await fetch(`${API_BASE_URL}/api/projects/createWithImage` , {
      method: 'POST',
      body: formDataObj
    });
    
  } catch (error) {
    console.error('Error creating project:', error);
    showNotification({
      title: 'Error',
      message: 'Failed to create project: ' + error.message,
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
    
    // For edit without image, use PUT with JSON
    const updateData = {
      projectName: projectForm.projectName,
      description: projectForm.description,
      budget: projectForm.budget,
      startDate: projectForm.startDate,
      endDate: projectForm.endDate,
      status: projectForm.status,
      projectManager: projectForm.projectManager,
      teamMembers: projectForm.teamMembers,
      stakeholders: projectForm.stakeholders,
      sustainabilityGoals: projectForm.sustainabilityGoals
    };
    
    const response = await fetch(`${API_BASE_URL}/api/projects/${selectedProject.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(updateData)
    });
    
    if (!response.ok) {
      throw new Error(`Server responded with ${response.status}`);
    }
    
    // If there's a new image, upload it separately
    if (projectForm.imageFile) {
      const imageFormData = new FormData();
      imageFormData.append('image', projectForm.imageFile);
      
      const imageResponse = await fetch(`/api/projects/${selectedProject.id}/updateImage`, {
        method: 'POST',
        body: imageFormData
      });
      
      if (!imageResponse.ok) {
        throw new Error(`Image upload failed: ${imageResponse.status}`);
      }
    }
    
    showNotification({
      title: 'Success',
      message: 'Project updated successfully',
      color: 'green'
    });
    
    closeEditModal();
    fetchProjects(); // Make sure to define this outside useEffect
    
  } catch (error) {
    console.error('Error updating project:', error);
    showNotification({
      title: 'Error',
      message: 'Failed to update project: ' + error.message,
      color: 'red'
    });
  } finally {
    setLoading(false);
  }
};

const handleConfirmDelete = async () => {
  try {
    setLoading(true);
    
    const response = await fetch(`${API_BASE_URL}/api/projects/${selectedProject.id}`, {
      method: 'DELETE'
    });
    
    if (!response.ok) {
      throw new Error(`Server responded with ${response.status}`);
    }
    
    showNotification({
      title: 'Success',
      message: 'Project deleted successfully',
      color: 'green'
    });
    
    closeDeleteModal();
    fetchProjects(); // Make sure to define this outside useEffect
    
  } catch (error) {
    console.error('Error deleting project:', error);
    showNotification({
      title: 'Error',
      message: 'Failed to delete project: ' + error.message,
      color: 'red'
    });
  } finally {
    setLoading(false);
  }
};
// Extract this function from useEffect so it can be called from other functions
const fetchProjects = async () => {
  try {
    setLoading(true);
    
    const API_URL = `${API_BASE_URL}/api/projects/all`;
    console.log("Fetching projects from:", API_URL);
    
    const response = await fetch(API_URL);
    
    // Better error handling
    if (!response.ok) {
      console.error(`Server responded with ${response.status}: ${response.statusText}`);
      throw new Error(`Server responded with ${response.status}`);
    }
    
    const data = await response.json();
    console.log("Retrieved projects data:", data);
    
    if (data && data.length > 0) {
      const transformedData = data.map(item => ({
        id: item.id || Math.random().toString(),
        title: item.projectName || "Untitled Project",
        status: item.status || "Pending",
        startDate: item.startDate || "N/A",
        endDate: item.endDate || "N/A",
        image: item.projectImage ? `data:image/jpeg;base64,${item.projectImage}` : sample1,
        description: item.description || "No description provided.",
        progress: calculateProgress(item.status),
        volunteers: 15, 
        budget: formatBudget(item.budget), 
        manager: {
          name: item.projectManager || "Not Assigned",
          image: "https://i.pravatar.cc/150?img=32"
        },
        teamMembers: parseTeamMembers(item.teamMembers),
        stakeholders: parseStakeholders(item.stakeholders),
        sustainabilityGoal: item.sustainabilityGoals || "None specified"
      }));
      
      setProjectData(transformedData);
      setError(null);
      
    } else {
      console.warn("Server returned empty projects data");
      throw new Error("No projects found in server response");
    }
  } catch (error) {
    console.error("Error fetching projects:", error);
    setError(error.message);
    
    setProjectData(projects);
    
    showNotification({
      title: 'Using Sample Data',
      message: 'Could not load projects from server. Showing sample data.',
      color: 'yellow'
    });
  } finally {
    setLoading(false);
  }
};

// These helper functions stay in this scope
const calculateProgress = (status) => {
  switch(status) {
    case 'Complete': return 100;
    case 'Closed': return 0;
    default: return Math.floor(Math.random() * 60) + 20; // 20-80% for ongoing projects
  }
};

const parseTeamMembers = (teamMembersString) => {
  if (!teamMembersString) return [];
  
  const names = teamMembersString.split(',').map(name => name.trim());
  
  return names.map((name, index) => ({
    name: name,
    image: `https://i.pravatar.cc/150?img=${30 + index}`
  }));
};

const parseStakeholders = (stakeholdersString) => {
  if (!stakeholdersString) return [];
  return stakeholdersString.split(',').map(s => s.trim());
};

// Then in your useEffect, just call it
useEffect(() => {
  fetchProjects();
}, []);
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
        <header className="text-left py-10 pl-10 pt-30">
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
                    <Tabs.Tab value="Ongoing">Ongoing</Tabs.Tab>
                    <Tabs.Tab value="Planning">Planning</Tabs.Tab>
                    <Tabs.Tab value="Completed">Completed</Tabs.Tab>
                    <Tabs.Tab value="Delayed">Delayed</Tabs.Tab>
                  </Tabs.List>
                </Tabs>
              </Grid.Col>
            </Grid>
            {/* Add this admin button */}
                {isAdmin && (
                  <Button
                    leftSection={<IconPlus size={16} />}
                    variant="gradient"
                    gradient={{ from: 'blue', to: 'cyan' }}
                    onClick={openCreateModal}
                    className="mt-4"
                    radius="md"
                  >
                    Create New Project
                  </Button>
                )}
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
                            from: project.status === 'Ongoing' ? 'blue' : 
                                  project.status === 'Planning' ? 'blue' :
                                  project.status === 'Completed' ? 'blue':
                                  project.status === 'Delayed' ? 'blue' : 'red', 
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
                      <Group spacing="xs">
      {isAdmin ? (
        <>
          <ActionIcon 
            color="yellow" 
            onClick={(e) => {
              e.stopPropagation();
              handleEditClick(project);
            }}
            variant="light"
          >
            <IconEdit size={16} />
          </ActionIcon>
          <ActionIcon 
            color="red" 
            onClick={(e) => {
              e.stopPropagation();
              handleDeleteClick(project);
            }}
            variant="light"
          >
            <IconTrash size={16} />
          </ActionIcon>
          <ActionIcon 
            color="blue" 
            onClick={() => handleCardClick(project)}
            variant="light"
          >
            <IconEye size={16} />
          </ActionIcon>
        </>
      ) : (
        <>
          <Text size="xs" color="blue">View Details</Text>
          <IconArrowRight size={14} className="text-blue-500" />
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
                  <HoverCard width={280} shadow="md" withArrow openDelay={200} closeDelay={300}>
                    <HoverCard.Target>
                      <Badge size="lg" color="teal" className="mr-2 cursor-help">Sustainability Goal</Badge>
                    </HoverCard.Target>
                    <HoverCard.Dropdown>
                      <Text size="sm" fw={500}>Sustainability Goal</Text>
                      <Text size="xs" c="dimmed">The UN Sustainable Development Goal this project contributes to.</Text>
                    </HoverCard.Dropdown>
                  </HoverCard>
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
      {/* Create Project Modal */}
<Modal
  opened={createModalOpen}
  onClose={closeCreateModal}
  title="Create New Project"
  size="lg"
>
  <form onSubmit={handleCreateSubmit}>
    <TextInput
      label="Project Name"
      placeholder="Enter project name"
      required
      value={projectForm.projectName}
      onChange={(e) => setProjectForm({...projectForm, projectName: e.target.value})}
      className="mb-3"
    />
    
    <Grid>
      <Grid.Col span={6}>
        <TextInput
          label="Project Manager"
          placeholder="Manager name"
          value={projectForm.projectManager}
          onChange={(e) => setProjectForm({...projectForm, projectManager: e.target.value})}
          className="mb-3"
        />
      </Grid.Col>
      <Grid.Col span={6}>
        <Select
          label="Status"
          data={[
            { value: 'Planning', label: 'Planning' },
            { value: 'Ongoing', label: 'Ongoing' },
            { value: 'Completed', label: 'Completed' },
            { value: 'Delayed', label: 'Delayed' },
          ]}
          value={projectForm.status}
          onChange={(value) => setProjectForm({...projectForm, status: value})}
          className="mb-3"
        />
      </Grid.Col>
    </Grid>
    
    <Grid>
      <Grid.Col span={6}>
        <TextInput
          label="Start Date (MM/DD/YYYY)"
          placeholder="MM/DD/YYYY"
          value={projectForm.startDate}
          onChange={(e) => setProjectForm({...projectForm, startDate: e.target.value})}
          className="mb-3"
        />
      </Grid.Col>
      <Grid.Col span={6}>
        <TextInput
          label="End Date (MM/DD/YYYY)"
          placeholder="MM/DD/YYYY"
          value={projectForm.endDate}
          onChange={(e) => setProjectForm({...projectForm, endDate: e.target.value})}
          className="mb-3"
        />
      </Grid.Col>
    </Grid>
    
    <TextInput
      label="Budget"
      placeholder="Budget amount"
      value={projectForm.budget}
      onChange={(e) => setProjectForm({...projectForm, budget: e.target.value})}
      className="mb-3"
    />
    
    <Textarea
      label="Description"
      placeholder="Project description"
      required
      minRows={3}
      value={projectForm.description}
      onChange={(e) => setProjectForm({...projectForm, description: e.target.value})}
      className="mb-3"
    />
    
    <TextInput
      label="Team Members"
      placeholder="Comma-separated list of team members"
      value={projectForm.teamMembers}
      onChange={(e) => setProjectForm({...projectForm, teamMembers: e.target.value})}
      className="mb-3"
    />
    
    <TextInput
      label="Stakeholders"
      placeholder="Comma-separated list of stakeholders"
      value={projectForm.stakeholders}
      onChange={(e) => setProjectForm({...projectForm, stakeholders: e.target.value})}
      className="mb-3"
    />
    
    <TextInput
      label="Sustainability Goals"
      placeholder="Sustainability goals for this project"
      value={projectForm.sustainabilityGoals}
      onChange={(e) => setProjectForm({...projectForm, sustainabilityGoals: e.target.value})}
      className="mb-3"
    />
    
    <FileInput
      label="Project Image"
      placeholder="Upload an image"
      accept="image/*"
      onChange={(file) => setProjectForm({...projectForm, imageFile: file})}
      className="mb-4"
    />
    
    <Group position="right" mt="md">
      <Button variant="outline" onClick={closeCreateModal}>Cancel</Button>
      <Button type="submit" color="blue">Create Project</Button>
    </Group>
  </form>
</Modal>

{/* Edit Project Modal (similar to Create) */}
<Modal
  opened={editModalOpen}
  onClose={closeEditModal}
  title="Edit Project"
  size="lg"
>
  <form onSubmit={handleEditSubmit}>
    {/* Same fields as Create with different submit handler */}
    <Group position="right" mt="md">
      <Button variant="outline" onClick={closeEditModal}>Cancel</Button>
      <Button type="submit" color="yellow">Update Project</Button>
    </Group>
  </form>
</Modal>

{/* Delete Confirmation Modal */}
<Modal
  opened={deleteModalOpen}
  onClose={closeDeleteModal}
  title="Delete Project"
  size="sm"
>
  <Text>Are you sure you want to delete "{selectedProject?.title}"?</Text>
  <Text size="sm" color="dimmed" className="mt-2">This action cannot be undone.</Text>
  
  <Group position="right" mt="xl">
    <Button variant="outline" onClick={closeDeleteModal}>Cancel</Button>
    <Button color="red" onClick={handleConfirmDelete}>Delete</Button>
  </Group>
</Modal>
    </>
  );
};

export default Projects;