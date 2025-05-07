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
  IconTrash,
  IconArrowLeft 
} from '@tabler/icons-react';
import { notifications } from '@mantine/notifications';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';
import { Select, Textarea } from '@mantine/core';
import { useAuth } from '../../contexts/AuthContext';
import { apiFetch } from '../utils/api';


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
  const sanitizeLink = (link) => {
    if (!link) return '';
    return link.startsWith('http://') || link.startsWith('https://')
      ? link
      : `https://${link}`;
  };

    // Fetch scholarships from API
    useEffect(() => {
      
      fetchScholarships();
    }, []);

    const fetchScholarships = async () => {
      try {
        setLoading(true);
        
        // Use apiFetch instead of direct fetch! Much cleaner!
        const response = await apiFetch('api/scholarships/getAllScholarships');
        
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
            saved: false,
            scholarImage: item.scholarImage || ""
          }));
          
          setScholarships(transformedData);
          
          // notifications.show({
          //   title: 'Scholarships Loaded (ﾉ´ヮ`)ﾉ*:･ﾟ✧',
          //   message: `Successfully loaded ${transformedData.length} adorable scholarships!`,
          //   color: 'green',
          // });
        } else {
          console.warn("No scholarships found");
          setScholarships([]);
        }
      } catch (error) {
        console.error("Failed to fetch scholarships:", error);
        
        // Fall back to sample data in case of error
        setScholarships([
          // Your existing sample data
        ]);
        
        notifications.show({
          title: 'Error Loading Scholarships (╥﹏╥)',
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
  
    if (!scholarshipForm.title || !scholarshipForm.description || !scholarshipForm.contactEmail) {
      notifications.show({
        title: 'Validation Error (⊙△⊙✿)',
        message: 'Title, description and contact email are required',
        color: 'red'
      });
      return;
    }
  
    try {
      setLoading(true);
  
      // Always use FormData and the with-image endpoint
      const formData = new FormData();
      formData.append('title', scholarshipForm.title || '');
      formData.append('description', scholarshipForm.description || '');
      formData.append('link', sanitizeLink(scholarshipForm.link));
      formData.append('contactEmail', scholarshipForm.contactEmail || '');
      formData.append('type', scholarshipForm.type || '');

      // Always append image, even if empty
      if (scholarshipForm.imageFile) {
        formData.append('image', scholarshipForm.imageFile);
      } else {
        // Some backends require the param to exist, so send an empty Blob
        formData.append('image', new Blob([]), '');
      }
  
      const response = await apiFetch('api/scholarships/createScholarship/with-image', {
        method: 'POST',
        body: formData
      });
  
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Failed to create scholarship: ${errorText}`);
      }
  
      notifications.show({
        title: 'Success (ﾉ´ヮ`)ﾉ*:･ﾟ✧',
        message: 'Scholarship created successfully!',
        color: 'green'
      });
  
      closeCreateModal();
      fetchScholarships();
  
    } catch (error) {
      console.error('Error creating scholarship:', error);
      notifications.show({
        title: 'Error (｡•́︿•̀｡)',
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
        title: 'Validation Error (⊙△⊙✿)',
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
        scholarImage: selectedScholarship.scholarImage
      };
      
      // Use apiFetch instead of direct fetch
      const response = await apiFetch(`api/scholarships/updateScholarship/${selectedScholarship.id}`, {
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
        
        const imageResponse = await apiFetch(`api/scholarships/updateScholarship/${selectedScholarship.id}/image`, {
          method: 'PUT',
          body: imageFormData
        });
        
        if (!imageResponse.ok) {
          const errorText = await imageResponse.text();
          throw new Error(`Failed to update image: ${errorText}`);
        }
      }
      
      notifications.show({
        title: 'Success (っ◔◡◔)っ ♥',
        message: 'Scholarship updated successfully!',
        color: 'green'
      });
      
      closeEditModal();
      fetchScholarships();
      
    } catch (error) {
      console.error('Error updating scholarship:', error);
      notifications.show({
        title: 'Error (｡•́︿•̀｡)',
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
      
      // Use apiFetch instead of direct fetch
      const response = await apiFetch(`api/scholarships/deleteScholarship/${selectedScholarship.id}`, {
        method: 'DELETE'
      });
      
      if (!response.ok) {
        const errorText = await response.text();
        throw new Error(`Failed to delete scholarship: ${errorText}`);
      }
      
      notifications.show({
        title: 'Success (づ￣ ³￣)づ',
        message: 'Scholarship deleted successfully!',
        color: 'green'
      });
      
      closeDeleteModal();
      fetchScholarships();
      
    } catch (error) {
      console.error('Error deleting scholarship:', error);
      notifications.show({
        title: 'Error (｡ŏ﹏ŏ)',
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
            Scholarships & Opportunities<span className="animate-bounce inline-block ml-2">🎓</span>
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Find financial support for your studies and make your dreams come true with these amazing scholarship opportunities! 
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
              
            <Grid.Col span={12} className="mt-4">
            {isAdmin && (
                      <div className="mb-4 flex justify-end">
              <Button
                leftSection={<IconPlus size={16} />}
                variant="gradient"
                gradient={{ from: 'blue', to: 'cyan' }}
                onClick={openCreateModal}
                radius="md"
              >
                Add New Scholarship
              </Button>
              </div>
          )}
            </Grid.Col>
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
                          
                        </Group>
                      </div>
                    </Card.Section>

                    <div className="p-4 flex-grow">
                      <Title order={3} className="mb-2">
                        {scholarship.title}
                      </Title>
                      <Text color="dimmed" size="sm" className="mb-3" lineClamp={2} style={{ whiteSpace: 'pre-line' }}>
                        {scholarship.description}
                      </Text>


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
                        </Group>
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
      withCloseButton={false}
      styles={{
        body: { padding: 0 },
        content: { background: 'linear-gradient(to bottom, #f0f4ff, #fff1f9)' }
      }}
    >
      {selectedScholarship && (
        <div className="min-h-[50vh] flex flex-col">
          {/* Header with close button */}
          <div className="sticky top-0 z-50 bg-gradient-to-r from-blue-600 to-indigo-800 text-white p-4 flex items-center">
            <Button 
              variant="subtle" 
              color="white" 
              leftSection={<IconArrowLeft size={18} />}
              onClick={() => setModalOpened(false)}
            >
              Back
            </Button>
            <Title order={3} className="mx-auto pr-10">Scholarship Details</Title>
          </div>

          {/* Image section with background effect */}
          <div className="relative h-[250px]">
            <div
              className="absolute inset-0 z-0 overflow-hidden"
              style={{
                backgroundImage: selectedScholarship.scholarImage 
                  ? `url(data:image/jpeg;base64,${selectedScholarship.scholarImage})` 
                  : 'linear-gradient(135deg, #0134AA 0%, #7F52FF 100%)',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                filter: 'blur(20px) brightness(0.6)',
                transform: 'scale(1.1)',
              }}
            ></div>
            <div className="relative z-10 flex justify-center items-center h-full">
              {selectedScholarship.scholarImage ? (
                <img
                  src={`data:image/jpeg;base64,${selectedScholarship.scholarImage}`}
                  alt={selectedScholarship.title}
                  className="max-h-[200px] rounded-lg shadow-lg object-contain"
                />
              ) : (
                <div className="bg-white/10 backdrop-blur-md p-8 rounded-lg shadow-lg flex items-center">
                  <IconCoin size={40} className="text-yellow-300 mr-4" />
                  <Text size="xl" fw={700} className="text-white">Financial Opportunity</Text>
                </div>
              )}
            </div>
          </div>

          {/* Content card */}
          <Paper radius="lg" className="mx-4 -mt-10 relative z-20 p-6 shadow-lg">
            <div className="flex flex-wrap justify-between items-start mb-3">
              <Badge 
                variant="gradient" 
                gradient={{ from: selectedScholarship.category === 'public' ? 'blue' : 'pink', to: 'cyan' }}
                size="lg"
                radius="md"
                className="mb-2"
              >
                {selectedScholarship.category.charAt(0).toUpperCase() + selectedScholarship.category.slice(1)}
              </Badge>
              
            </div>
            
            <Title order={2} className="mb-4 text-blue-700">
              {selectedScholarship.title}
            </Title>
            
            <Divider className="my-4" label="Details" labelPosition="center" />
            
            <div className="space-y-4 mb-6">
              <div className="flex items-start gap-3">
                <IconCalendarEvent size={20} className="text-blue-500 mt-1" />
                <div>
                  <Text fw={600} size="sm" className="text-gray-700">Application Deadline</Text>
                  <Text>{selectedScholarship.deadline || "Contact for deadline"}</Text>
                </div>
              </div>
              
              <div className="flex items-start gap-3">
                <IconCoin size={20} className="text-blue-500 mt-1" />
                <div>
                  <Text fw={600} size="sm" className="text-gray-700">Amount</Text>
                  <Text>{selectedScholarship.amount || "Varies based on qualification"}</Text>
                </div>
              </div>
            </div>
            
            <Text className="mb-6 leading-relaxed" style={{ whiteSpace: 'pre-line' }}>
              {selectedScholarship.description}
            </Text>
            
            <Divider className="my-4" label="Contact & Apply" labelPosition="center" />
            
            <Group className="mt-4">
              <Button 
                leftSection={<IconMail size={16} />}
                variant="outline" 
                component="a" 
                href={`mailto:${selectedScholarship.contactEmail}`}
              >
                Contact
              </Button>
              
              <Button
                leftSection={<IconExternalLink size={16} />}
                component="a"
                href={selectedScholarship.link}
                target="_blank"
                variant="gradient"
                gradient={{ from: 'blue', to: 'cyan' }}
              >
                Apply Now
              </Button>
              
            </Group>
          </Paper>

          <div className="flex justify-center w-full">
            <Button 
              variant="subtle"
              color="gray"
              className="mt-6 mb-4" 
              onClick={() => setModalOpened(false)}
            >
              Close
            </Button>
          </div>
        </div>
      )}
      </Modal>
            
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
          label="Scholarship Image (Optional)"
          description="Leave empty to keep existing image"
          placeholder="Upload a new image"
          accept="image/*"
          onChange={(file) => setScholarshipForm({...scholarshipForm, imageFile: file})}
          className="mb-4"
        />
        
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