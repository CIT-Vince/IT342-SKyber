import React, { useState, useEffect } from 'react';
import { 
  Paper, 
  Title, 
  Text, 
  Button, 
  Grid, 
  TextInput,
  Tabs,
  Card,
  Badge,
  ActionIcon,
  Anchor,
  Group,
  Divider,
  Modal,
  LoadingOverlay,
  FileInput ,
  
} from '@mantine/core';
import { 
  IconSearch, 
  IconMail, 
  IconExternalLink, 
  IconBookmark, 
  IconHeart, 
  IconShare,
  IconPlus ,
  IconCalendarEvent,
  IconCoin ,
  IconEdit ,
  IconTrash
} from '@tabler/icons-react';
import { notifications } from '@mantine/notifications';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';
import { Select, Textarea } from '@mantine/core';
import { useAuth } from '../../contexts/AuthContext';


const Scholarship = () => {
  const [scholarships, setScholarships] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState('all');
  const [selectedScholarship, setSelectedScholarship] = useState(null);
  const [modalOpened, setModalOpened] = useState(false);
  const { currentUser } = useAuth();
const [isAdmin, setIsAdmin] = useState(false);
const [createModalOpen, { open: openCreateModal, close: closeCreateModal }] = useDisclosure(false);
const [editModalOpen, { open: openEditModal, close: closeEditModal }] = useDisclosure(false);
const [deleteModalOpen, { open: openDeleteModal, close: closeDeleteModal }] = useDisclosure(false);
const [scholarshipForm, setScholarshipForm] = useState({
  title: '',
  description: '',
  link: '',
  contactEmail: '',
  type: 'public',
  deadline: '',
  amount: '',
  imageFile: null
});
  
  // Fetch scholarships from API
  useEffect(() => {
    const fetchScholarships = async () => {
      try {
        setLoading(true);
        
        // The actual API endpoint
        const API_URL = '/api/scholarships/getAllScholarships';
        console.log("Fetching scholarships from:", API_URL);
        
        const response = await fetch(API_URL);
        
        if (!response.ok) {
          console.error(`Server responded with ${response.status}: ${response.statusText}`);
          throw new Error(`Server responded with ${response.status}`);
        }
        
        const data = await response.json();
        console.log("Retrieved scholarships:", data);
        
        // Transform data from backend format to frontend format
        if (data && data.length > 0) {
          const transformedData = data.map(item => ({
            id: item.id || Math.random().toString(),
            title: item.title || "Untitled Scholarship",
            description: item.description || "No description provided",
            link: item.link || "#",
            contactEmail: item.contactEmail || "contact@skyber.org",
            category: item.type?.toLowerCase() === 'private' ? 'private' : 'public',
            deadline: item.deadline || "Open until filled",
            amount: item.amount || "Varies",
            saved: false,
            scholarImage: item.scholarImage || ""
          }));
          
          setScholarships(transformedData);
          
          notifications.show({
            title: 'Scholarships Loaded',
            message: `Successfully loaded ${transformedData.length} scholarships`,
            color: 'green',
          });
        } else {
          console.warn("No scholarships found");
          setScholarships([]);
        }
      } catch (error) {
        console.error("Failed to fetch scholarships:", error);
        
        // Fall back to sample data in case of error
        setScholarships([
          {
            id: 1,
            title: "SKyber Excellence Scholarship",
            description: "Full scholarship for outstanding students pursuing computer science or IT-related courses. Covers tuition, books, and monthly allowance.",
            link: "https://skyber.org/excellence-scholarship",
            contactEmail: "excellence@skyber.org",
            category: "public",
            deadline: "June 30, 2025",
            amount: "$5,000",
            saved: false
          },
          {
            id: 2,
            title: "Women in Tech Scholarship",
            description: "Supporting female students pursuing degrees in STEM fields with focus on addressing gender disparities in technology careers.",
            link: "https://skyber.org/women-in-tech",
            contactEmail: "womenintech@skyber.org",
            category: "public",
            deadline: "July 15, 2025",
            amount: "$3,500",
            saved: true
          },
          {
            id: 3,
            title: "Rural Development Innovation Grant",
            description: "For students from rural communities developing technology solutions for agricultural or community development.",
            link: "https://skyber.org/rural-innovation",
            contactEmail: "ruralinnovation@skyber.org",
            category: "private",
            deadline: "August 5, 2025",
            amount: "$4,200",
            saved: false
          },
          {
            id: 4,
            title: "Digital Arts & Design Scholarship",
            description: "Supporting creative students pursuing education in digital arts, UI/UX design, and interactive media.",
            link: "https://skyber.org/digital-arts",
            contactEmail: "arts@skyber.org",
            category: "public",
            deadline: "May 20, 2025",
            amount: "$2,800",
            saved: false
          }
        ]);
        
        notifications.show({
          title: 'Error Loading Scholarships',
          message: 'Using sample data instead. Please check your connection.',
          color: 'red',
        });
      } finally {
        setLoading(false);
      }
    };
    
    fetchScholarships();
  }, []);
// Extract this function from useEffect so it can be called from handlers
const fetchScholarships = async () => {
  try {
    setLoading(true);
    
    // The actual API endpoint
    const API_URL = '/api/scholarships/getAllScholarships';
    console.log("Fetching scholarships from:", API_URL);
    
    const response = await fetch(API_URL);
    
    if (!response.ok) {
      console.error(`Server responded with ${response.status}: ${response.statusText}`);
      throw new Error(`Server responded with ${response.status}`);
    }
    
    const data = await response.json();
    console.log("Retrieved scholarships:", data);
    
    // Transform data from backend format to frontend format
    if (data && data.length > 0) {
      const transformedData = data.map(item => ({
        id: item.id || Math.random().toString(),
        title: item.title || "Untitled Scholarship",
        description: item.description || "No description provided",
        link: item.link || "#",
        contactEmail: item.contactEmail || "contact@skyber.org",
        category: item.type?.toLowerCase() === 'private' ? 'private' : 'public',
        deadline: item.deadline || "Open until filled",
        amount: item.amount || "Varies",
        saved: false,
        scholarImage: item.scholarImage || ""
      }));
      
      setScholarships(transformedData);
      
      notifications.show({
        title: 'Scholarships Loaded',
        message: `Successfully loaded ${transformedData.length} scholarships`,
        color: 'green',
      });
    } else {
      console.warn("No scholarships found");
      setScholarships([]);
    }
  } catch (error) {
    console.error("Failed to fetch scholarships:", error);
    
    // Fall back to sample data in case of error
    setScholarships([
      // Your sample data here
    ]);
    
    notifications.show({
      title: 'Error Loading Scholarships',
      message: 'Using sample data instead. Please check your connection.',
      color: 'red',
    });
  } finally {
    setLoading(false);
  }
};

// In useEffect, just call the function
useEffect(() => {
  fetchScholarships();
}, []);
  // Filter scholarships based on search and category
  const filteredScholarships = scholarships.filter(scholarship => {
    const matchesSearch = scholarship.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         scholarship.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = activeTab === 'all' || scholarship.category === activeTab;
    
    return matchesSearch && matchesCategory;
  });

  // Toggle save/bookmark scholarship
  const toggleSaveScholarship = (id, event) => {
    event.stopPropagation(); // Prevent card click when clicking the bookmark icon
    
    setScholarships(scholarships.map(scholarship => 
      scholarship.id === id 
        ? { ...scholarship, saved: !scholarship.saved } 
        : scholarship
    ));
    
    const scholarship = scholarships.find(s => s.id === id);
    const action = scholarship.saved ? 'removed from' : 'added to';
    
    notifications.show({
      title: `Scholarship ${action} favorites`,
      message: `"${scholarship.title}" has been ${action} your favorites!`,
      color: scholarship.saved ? 'gray' : 'pink',
    });
  };
  
  // View scholarship details
  const viewScholarshipDetails = (scholarship) => {
    setSelectedScholarship(scholarship);
    setModalOpened(true);
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

  // Add these handler functions to your component
const handleCreateClick = () => {
  setScholarshipForm({
    title: '',
    description: '',
    link: '',
    contactEmail: '',
    type: 'public',
    deadline: '',
    amount: '',
    imageFile: null
  });
  openCreateModal();
};

const handleEditClick = (scholarship) => {
  setSelectedScholarship(scholarship);
  setScholarshipForm({
    title: scholarship.title,
    description: scholarship.description,
    link: scholarship.link,
    contactEmail: scholarship.contactEmail,
    type: scholarship.category === 'public' ? 'public' : 'private',
    deadline: scholarship.deadline || '',
    amount: scholarship.amount || '',
    imageFile: null // Can't prefill image
  });
  openEditModal();
};

const handleDeleteClick = (scholarship) => {
  setSelectedScholarship(scholarship);
  openDeleteModal();
};

const handleCreateSubmit = async (e) => {
  e.preventDefault();
  
  // Validate required fields
  if (!scholarshipForm.title || !scholarshipForm.description || !scholarshipForm.contactEmail) {
    notifications.show({
      title: 'Validation Error',
      message: 'Title, description and contact email are required',
      color: 'red'
    });
    return;
  }
  
  try {
    setLoading(true);
    
    const formData = new FormData();
    formData.append('title', scholarshipForm.title);
    formData.append('description', scholarshipForm.description);
    formData.append('link', scholarshipForm.link || '#');
    formData.append('contactEmail', scholarshipForm.contactEmail);
    formData.append('type', scholarshipForm.type);
    
    if (scholarshipForm.imageFile) {
      formData.append('image', scholarshipForm.imageFile);
    }
    
    const response = await fetch('/api/scholarships/createScholarship/with-image', {
      method: 'POST',
      body: formData
    });
    
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to create scholarship: ${errorText}`);
    }
    
    notifications.show({
      title: 'Success',
      message: 'Scholarship created successfully',
      color: 'green'
    });
    
    closeCreateModal();
    fetchScholarships(); // Make sure to extract this from your useEffect
    
  } catch (error) {
    console.error('Error creating scholarship:', error);
    notifications.show({
      title: 'Error',
      message: error.message || 'Failed to create scholarship',
      color: 'red'
    });
  } finally {
    setLoading(false);
  }
};
const completeSubmit = async (formData) => {
  try {
    const response = await fetch('/api/scholarships/createScholarship/with-image', {
      method: 'POST',
      body: formData
    });
    
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to create scholarship: ${errorText}`);
    }
    
    notifications.show({
      title: 'Success',
      message: 'Scholarship created successfully',
      color: 'green'
    });
    
    closeCreateModal();
    fetchScholarships();
  } catch (error) {
    console.error('Error creating scholarship:', error);
    notifications.show({
      title: 'Error',
      message: error.message || 'Failed to create scholarship',
      color: 'red'
    });
  } finally {
    setLoading(false);
  }
};
const handleEditSubmit = async (e) => {
  e.preventDefault();
  
  if (!selectedScholarship) return;
  
  // Validate required fields
  if (!scholarshipForm.title || !scholarshipForm.description || !scholarshipForm.contactEmail) {
    notifications.show({
      title: 'Validation Error',
      message: 'Title, description and contact email are required',
      color: 'red'
    });
    return;
  }
  
  try {
    setLoading(true);
    
    // First update scholarship details
    const updateData = {
      title: scholarshipForm.title,
      description: scholarshipForm.description,
      link: scholarshipForm.link || '#',
      contactEmail: scholarshipForm.contactEmail,
      type: scholarshipForm.type,
      // Keep existing image if no new image is provided
      scholarImage: selectedScholarship.scholarImage
    };
    
    const response = await fetch(`/api/scholarships/updateScholarship/${selectedScholarship.id}`, {
      method: 'PUT',
      headers: {
        'Content-Type': 'application/json'
      },
      body: JSON.stringify(updateData)
    });
    
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to update scholarship: ${errorText}`);
    }
    
    // If there's a new image, update it separately
    if (scholarshipForm.imageFile) {
      const imageFormData = new FormData();
      imageFormData.append('image', scholarshipForm.imageFile);
      
      const imageResponse = await fetch(`/api/scholarships/updateScholarship/${selectedScholarship.id}/image`, {
        method: 'PUT',
        body: imageFormData
      });
      
      if (!imageResponse.ok) {
        const errorText = await imageResponse.text();
        throw new Error(`Failed to update image: ${errorText}`);
      }
    }
    
    notifications.show({
      title: 'Success',
      message: 'Scholarship updated successfully',
      color: 'green'
    });
    
    closeEditModal();
    fetchScholarships(); // Make sure to extract this from your useEffect
    
  } catch (error) {
    console.error('Error updating scholarship:', error);
    notifications.show({
      title: 'Error',
      message: error.message || 'Failed to update scholarship',
      color: 'red'
    });
  } finally {
    setLoading(false);
  }
};

const handleDeleteConfirm = async () => {
  if (!selectedScholarship) return;
  
  try {
    setLoading(true);
    
    const response = await fetch(`/api/scholarships/deleteScholarship/${selectedScholarship.id}`, {
      method: 'DELETE'
    });
    
    if (!response.ok) {
      const errorText = await response.text();
      throw new Error(`Failed to delete scholarship: ${errorText}`);
    }
    
    notifications.show({
      title: 'Success',
      message: 'Scholarship deleted successfully',
      color: 'green'
    });
    
    closeDeleteModal();
    fetchScholarships(); // Make sure to extract this from your useEffect
    
  } catch (error) {
    console.error('Error deleting scholarship:', error);
    notifications.show({
      title: 'Error',
      message: error.message || 'Failed to delete scholarship',
      color: 'red'
    });
  } finally {
    setLoading(false);
  }
};
  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-pink-50">
          <LoadingOverlay visible={true} overlayBlur={2} />
          <div className="text-center">
            <Title order={3}>Loading Scholarships</Title>
            <Text color="dimmed">Please wait while we fetch available opportunities...</Text>
          </div>
        </div>
      </>
    );
  }

  return (
    <>
      <div
        className="w-full h-auto pt-30!"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
        <Navbar />
        <header className="text-left py-10 pl-10">
          <Title className="text-5xl font-bold text-white">
            Scholarships & Opportunities
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Find financial support for your studies and make your dreams come true with these amazing scholarship opportunities! (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
          </Text>
        </header>
      </div>

      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-7xl mx-auto">
          <Paper shadow="md" radius="lg" className="p-6 mb-8">
            <Grid>
              <Grid.Col span={{ base: 12, md: 8 }}>
                <TextInput
                  icon={<IconSearch size={18} />}
                  placeholder="Search scholarships..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  radius="xl"
                  size="md"
                  className="mb-4 md:mb-0"
                />
              </Grid.Col>
              <Grid.Col span={{ base: 12, md: 4 }}>
                <Tabs value={activeTab} onChange={setActiveTab} radius="xl" variant="pills">
                  <Tabs.List grow>
                    <Tabs.Tab value="all">All</Tabs.Tab>
                    <Tabs.Tab value="public">Public</Tabs.Tab>
                    <Tabs.Tab value="private">Private</Tabs.Tab>
                  </Tabs.List>
                </Tabs>
              </Grid.Col>
              
            {isAdmin && (
      <Grid.Col span={12} className="mt-4">
        <Button
          leftSection={<IconPlus size={16} />}
          variant="gradient"
          gradient={{ from: 'blue', to: 'cyan' }}
          onClick={openCreateModal}
          radius="md"
        >
          Add New Scholarship
        </Button>
      </Grid.Col>
    )}
            </Grid>
          </Paper>

          {filteredScholarships.length > 0 ? (
            <Grid gutter="lg">
              {filteredScholarships.map((scholarship) => (
                <Grid.Col key={scholarship.id} span={{ base: 12, sm: 6, lg: 4 }}>
                  <Card 
                    shadow="md" 
                    radius="md" 
                    className="h-full flex flex-col transition-transform duration-300 hover:scale-[1.02]"
                    onClick={() => viewScholarshipDetails(scholarship)}
                  >
                    <Card.Section className="p-4 bg-gradient-to-r from-sky-400 to-slate-50">
                      <div className="flex justify-between items-center">
                        <Badge 
                          variant="gradient" 
                          gradient={{ from: scholarship.category === 'public' ? 'blue' : 'pink', to: 'cyan' }}
                          radius="md"
                        >
                          {scholarship.category.charAt(0).toUpperCase() + scholarship.category.slice(1)}
                        </Badge>
                        <Group spacing={4}>
      {isAdmin && (
        <>
          <ActionIcon 
            variant="light" 
            color="yellow"
            onClick={(e) => {
              e.stopPropagation();
              handleEditClick(scholarship);
            }}
          >
            <IconEdit size={16} />
          </ActionIcon>
          <ActionIcon 
            variant="light" 
            color="red"
            onClick={(e) => {
              e.stopPropagation();
              handleDeleteClick(scholarship);
            }}
          >
            <IconTrash size={16} />
          </ActionIcon>
        </>
      )}
      <ActionIcon 
        variant="subtle" 
        color={scholarship.saved ? "pink" : "gray"}
        onClick={(e) => toggleSaveScholarship(scholarship.id, e)}
      >
        <IconBookmark size={18} />
      </ActionIcon>
    </Group>
                      </div>
                    </Card.Section>

                    <div className="p-4 flex-grow">
                      <Title order={3} className="mb-2">
                        {scholarship.title}
                      </Title>
                      <Text color="dimmed" size="sm" className="mb-3" lineClamp={2}>
                        {scholarship.description}
                      </Text>

                      <Group className="mb-2">
                        <Badge variant="dot" color="green">Amount: {scholarship.amount}</Badge>
                        <Badge variant="dot" color="red">Deadline: {scholarship.deadline}</Badge>
                      </Group>

                      <Divider my="sm" />

                      <Group>
                        <Anchor href={`mailto:${scholarship.contactEmail}`} size="sm" className="flex items-center">
                          <IconMail size={16} className="mr-1" />
                          Contact
                        </Anchor>
                        <Anchor href={scholarship.link} target="_blank" size="sm" className="flex items-center">
                          <IconExternalLink size={16} className="mr-1" />
                          Apply
                        </Anchor>
                      </Group>
                    </div>

                    <Card.Section className="p-3 bg-gradient-to-r from-blue-50 to-pink-50">
                      <Group position="apart">
                        <Group spacing="xs">
                          <ActionIcon variant="subtle" color="pink">
                            <IconHeart size={16} />
                          </ActionIcon>
                          <Text size="xs" color="dimmed">24 Students Applied</Text>
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
              <Title order={3} className="mb-2">No scholarships found (´。＿。｀)</Title>
              <Text>Try changing your search or filters to find more opportunities!</Text>
            </Paper>
          )}
        </div>
      </div>
      
      {/* Scholarship Details Modal */}
      <Modal
        opened={modalOpened}
        onClose={() => setModalOpened(false)}
        size="lg"
        centered
        title={<Text size="lg" weight={600}>Scholarship Details</Text>}
      >
        {selectedScholarship && (
          <div>
            {selectedScholarship.scholarImage && (
              <div className="text-center mb-4">
                <img 
                  src={`data:image/jpeg;base64,${selectedScholarship.scholarImage}`} 
                  alt={selectedScholarship.title}
                  className="max-h-[200px] rounded-lg shadow-md" 
                />
              </div>
            )}
            
            <Title order={2} className="mb-2 text-blue-700">
              {selectedScholarship.title}
            </Title>
            
            <Badge 
              variant="gradient" 
              gradient={{ from: selectedScholarship.category === 'public' ? 'blue' : 'pink', to: 'cyan' }}
              size="lg"
              className="mb-4"
            >
              {selectedScholarship.category.charAt(0).toUpperCase() + selectedScholarship.category.slice(1)}
            </Badge>
            
            <Text className="mb-4">
              {selectedScholarship.description}
            </Text>
            
            <div className="grid grid-cols-2 gap-3 mb-4">
              <div className="flex items-center">
                <IconCalendarEvent size={20} className="text-blue-500 mr-2" />
                <div>
                  <Text size="sm" weight={500}>Application Deadline</Text>
                  <Text size="sm">{selectedScholarship.deadline}</Text>
                </div>
              </div>
              
              <div className="flex items-center">
                <IconCoin size={20} className="text-blue-500 mr-2" />
                <div>
                  <Text size="sm" weight={500}>Award Amount</Text>
                  <Text size="sm">{selectedScholarship.amount}</Text>
                </div>
              </div>
            </div>
            
            <Divider className="my-3" />
            
            <Group position="apart" className="mb-4">
              <Button 
                leftIcon={<IconMail size={16} />}
                variant="outline" 
                component="a" 
                href={`mailto:${selectedScholarship.contactEmail}`}
              >
                Contact
              </Button>
              
              <Button
                leftIcon={<IconExternalLink size={16} />}
                component="a"
                href={selectedScholarship.link}
                target="_blank"
              >
                Apply Now
              </Button>
            </Group>
          </div>
        )}
      </Modal>
      {/* Add these modal components at the bottom of your component */}
{/* Create Scholarship Modal */}
<Modal
  opened={createModalOpen}
  onClose={closeCreateModal}
  title="Add New Scholarship"
  size="lg"
>
  <form onSubmit={handleCreateSubmit}>
    <TextInput
      label="Title"
      required
      placeholder="Scholarship name"
      value={scholarshipForm.title}
      onChange={(e) => setScholarshipForm({...scholarshipForm, title: e.target.value})}
      className="mb-3"
    />
    
    <Select
      label="Type"
      data={[
        { value: 'public', label: 'Public' },
        { value: 'private', label: 'Private' },
      ]}
      value={scholarshipForm.type}
      onChange={(value) => setScholarshipForm({...scholarshipForm, type: value})}
      className="mb-3"
      required
    />
    
    <TextInput
      label="Application Link"
      placeholder="https://example.com/application"
      value={scholarshipForm.link}
      onChange={(e) => setScholarshipForm({...scholarshipForm, link: e.target.value})}
      className="mb-3"
    />
    
    <TextInput
      label="Contact Email"
      required
      placeholder="contact@example.com"
      value={scholarshipForm.contactEmail}
      onChange={(e) => setScholarshipForm({...scholarshipForm, contactEmail: e.target.value})}
      className="mb-3"
    />
    
    <Grid>
      <Grid.Col span={6}>
        <TextInput
          label="Deadline"
          placeholder="e.g., June 30, 2025"
          value={scholarshipForm.deadline}
          onChange={(e) => setScholarshipForm({...scholarshipForm, deadline: e.target.value})}
          className="mb-3"
        />
      </Grid.Col>
      <Grid.Col span={6}>
        <TextInput
          label="Amount"
          placeholder="e.g., $5,000"
          value={scholarshipForm.amount}
          onChange={(e) => setScholarshipForm({...scholarshipForm, amount: e.target.value})}
          className="mb-3"
        />
      </Grid.Col>
    </Grid>
    
    <Textarea
      label="Description"
      placeholder="Describe the scholarship details, eligibility, and requirements"
      required
      minRows={4}
      value={scholarshipForm.description}
      onChange={(e) => setScholarshipForm({...scholarshipForm, description: e.target.value})}
      className="mb-3"
    />
    
    <FileInput
      label="Scholarship Image"
      placeholder="Upload an image"
      accept="image/*"
      onChange={(file) => setScholarshipForm({...scholarshipForm, imageFile: file})}
      className="mb-4"
    />
    
    <Group position="right" mt="md">
      <Button variant="outline" onClick={closeCreateModal}>Cancel</Button>
      <Button type="submit" color="blue">Create Scholarship</Button>
    </Group>
  </form>
</Modal>

{/* Edit Scholarship Modal */}
<Modal
  opened={editModalOpen}
  onClose={closeEditModal}
  title="Edit Scholarship"
  size="lg"
>
  <form onSubmit={handleEditSubmit}>
    {/* Same fields as create modal */}
    <TextInput
      label="Title"
      required
      placeholder="Scholarship name"
      value={scholarshipForm.title}
      onChange={(e) => setScholarshipForm({...scholarshipForm, title: e.target.value})}
      className="mb-3"
    />
    
    <Select
      label="Type"
      data={[
        { value: 'public', label: 'Public' },
        { value: 'private', label: 'Private' },
      ]}
      value={scholarshipForm.type}
      onChange={(value) => setScholarshipForm({...scholarshipForm, type: value})}
      className="mb-3"
      required
    />
    
    {/* Other fields... */}
    
    <Group position="right" mt="md">
      <Button variant="outline" onClick={closeEditModal}>Cancel</Button>
      <Button type="submit" color="yellow">Update Scholarship</Button>
    </Group>
  </form>
</Modal>

{/* Delete Confirmation Modal */}
<Modal
  opened={deleteModalOpen}
  onClose={closeDeleteModal}
  title="Delete Scholarship"
  size="sm"
>
  {selectedScholarship && (
    <>
      <Text>Are you sure you want to delete "{selectedScholarship.title}"?</Text>
      <Text size="sm" color="dimmed" className="mt-2">
        This action cannot be undone.
      </Text>
      
      <Group position="right" className="mt-4">
        <Button variant="outline" onClick={closeDeleteModal}>Cancel</Button>
        <Button color="red" onClick={handleDeleteConfirm}>Delete</Button>
      </Group>
    </>
  )}
</Modal>
    </>
  );
};

export default Scholarship;