import { useState, useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import {
  Container, Title, Text, Button, Group, Table, Badge, Modal, TextInput,
  Textarea, Select, FileInput, LoadingOverlay, ActionIcon, Paper, Tabs,
  Notification, Divider
} from '@mantine/core';
import { useDisclosure } from '@mantine/hooks';
import { showNotification } from '@mantine/notifications';
import { 
  IconPlus, IconEdit, IconTrash, IconSearch, 
  IconUpload, IconX, IconCheck, IconEye 
} from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import { useAuth } from '../../contexts/AuthContext';

const AdminDashboard = () => {
  const { currentUser } = useAuth();
  const [userData, setUserData] = useState(null);
  const [announcements, setAnnouncements] = useState([]);
  const [selectedAnnouncement, setSelectedAnnouncement] = useState(null);
  const [loading, setLoading] = useState(true);
  const [createOpened, { open: openCreateModal, close: closeCreateModal }] = useDisclosure(false);
  const [editOpened, { open: openEditModal, close: closeEditModal }] = useDisclosure(false);
  const [deleteOpened, { open: openDeleteModal, close: closeDeleteModal }] = useDisclosure(false);
  const [activeTab, setActiveTab] = useState('announcements');
  const navigate = useNavigate();
  
  // Form states
  const [formData, setFormData] = useState({
    title: '',
    content: '',
    category: 'News',
    barangay: 'All',
    image: null
  });

  useEffect(() => {
    // Verify user is admin, otherwise redirect
    const checkAdmin = async () => {
      try {
        if (!currentUser) {
          navigate('/login');
          return;
        }
        
        const { getDatabase, ref, get } = await import('firebase/database');
        const db = getDatabase();
        const userRef = ref(db, `users/${currentUser.uid}`);
        const snapshot = await get(userRef);
        
        if (snapshot.exists()) {
          const userData = snapshot.val();
          setUserData(userData);
          
          if (userData.role !== 'ADMIN') {
            showNotification({
              title: 'Access Denied',
              message: 'You do not have permission to view this page',
              color: 'red'
            });
            navigate('/');
          }
        } else {
          navigate('/login');
        }
      } catch (error) {
        console.error('Error checking admin status:', error);
        navigate('/');
      }
    };
    
    checkAdmin();
    fetchAnnouncements();
  }, [currentUser, navigate]);

  const fetchAnnouncements = async () => {
    try {
      setLoading(true);
      const response = await fetch('http://skyber.onrender.com/api/announcements/getAllAnnouncements');
      if (!response.ok) throw new Error('Failed to fetch announcements');
      
      const data = await response.json();
      setAnnouncements(data);
    } catch (error) {
      console.error('Error fetching announcements:', error);
      showNotification({
        title: 'Error',
        message: 'Failed to load announcements',
        color: 'red'
      });
    } finally {
      setLoading(false);
    }
  };

  const handleCreateAnnouncement = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      
      const formDataObj = new FormData();
      formDataObj.append('title', formData.title);
      formDataObj.append('content', formData.content);
      formDataObj.append('category', formData.category);
      formDataObj.append('barangay', formData.barangay);
      
      if (formData.image) {
        formDataObj.append('image', formData.image);
      }
      
      const response = await fetch('http://skyber.onrender.com/api/announcements/createWithImage', {
        method: 'POST',
        body: formDataObj,
      });
      
      if (!response.ok) throw new Error('Failed to create announcement');
      
      showNotification({
        title: 'Success',
        message: 'Announcement created successfully',
        color: 'green',
        icon: <IconCheck />
      });
      
      closeCreateModal();
      resetForm();
      fetchAnnouncements();
    } catch (error) {
      console.error('Error creating announcement:', error);
      showNotification({
        title: 'Error',
        message: 'Failed to create announcement',
        color: 'red',
        icon: <IconX />
      });
    } finally {
      setLoading(false);
    }
  };

  const handleEditAnnouncement = async (e) => {
    e.preventDefault();
    try {
      setLoading(true);
      
      const updateData = {
        title: formData.title,
        content: formData.content,
        category: formData.category,
        barangay: formData.barangay
      };
      
      const response = await fetch(`http://skyber.onrender.com/api/announcements/updateAnnouncement/${selectedAnnouncement.id}`, {
        method: 'PUT',
        headers: {
          'Content-Type': 'application/json'
        },
        body: JSON.stringify(updateData)
      });
      
      if (!response.ok) throw new Error('Failed to update announcement');
      
      showNotification({
        title: 'Success',
        message: 'Announcement updated successfully',
        color: 'green',
        icon: <IconCheck />
      });
      
      closeEditModal();
      resetForm();
      fetchAnnouncements();
    } catch (error) {
      console.error('Error updating announcement:', error);
      showNotification({
        title: 'Error',
        message: 'Failed to update announcement',
        color: 'red',
        icon: <IconX />
      });
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteAnnouncement = async () => {
    try {
      setLoading(true);
      
      const response = await fetch(`http://skyber.onrender.com/api/announcements/deleteAnnouncement/${selectedAnnouncement.id}`, {
        method: 'DELETE'
      });
      
      if (!response.ok) throw new Error('Failed to delete announcement');
      
      showNotification({
        title: 'Success',
        message: 'Announcement deleted successfully',
        color: 'green',
        icon: <IconCheck />
      });
      
      closeDeleteModal();
      fetchAnnouncements();
    } catch (error) {
      console.error('Error deleting announcement:', error);
      showNotification({
        title: 'Error',
        message: 'Failed to delete announcement',
        color: 'red',
        icon: <IconX />
      });
    } finally {
      setLoading(false);
    }
  };

  const openEdit = (announcement) => {
    setSelectedAnnouncement(announcement);
    setFormData({
      title: announcement.title || '',
      content: announcement.content || '',
      category: announcement.category || 'News',
      barangay: announcement.barangay || 'All',
      image: null
    });
    openEditModal();
  };

  const openDelete = (announcement) => {
    setSelectedAnnouncement(announcement);
    openDeleteModal();
  };

  const resetForm = () => {
    setFormData({
      title: '',
      content: '',
      category: 'News',
      barangay: 'All',
      image: null
    });
  };

  const formatDate = (dateString) => {
    if (!dateString) return 'N/A';
    try {
      return new Date(dateString).toLocaleDateString('en-US', {
        year: 'numeric',
        month: 'short',
        day: 'numeric'
      });
    } catch (e) {
      return dateString;
    }
  };

  return (
    <>
      <div className="w-full h-auto" style={{ background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)' }}>
        <Navbar />
        <header className="text-left py-10 pl-10 pt-30">
          <Title className="text-5xl font-bold text-white">Admin Dashboard</Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Manage announcements and other content (✿◠‿◠)
          </Text>
        </header>
      </div>

      <Container size="xl" className="py-8">
        <Paper shadow="md" radius="lg" className="p-6 mb-8">
          <Tabs value={activeTab} onChange={setActiveTab} radius="md">
            <Tabs.List>
              <Tabs.Tab value="announcements">Announcements</Tabs.Tab>
              <Tabs.Tab value="users">Users</Tabs.Tab>
              <Tabs.Tab value="projects">Projects</Tabs.Tab>
            </Tabs.List>
          </Tabs>
        </Paper>

        <LoadingOverlay visible={loading} overlayBlur={2} />

        {activeTab === 'announcements' && (
          <>
            <Group position="apart" className="mb-4">
              <Title order={2}>Manage Announcements</Title>
              <Button 
                leftIcon={<IconPlus size={16} />} 
                color="blue" 
                onClick={() => {
                  resetForm();
                  openCreateModal();
                }}
              >
                Add New Announcement
              </Button>
            </Group>

            <Paper shadow="md" radius="md" p="md" className="mb-8">
              <Table striped highlightOnHover>
                <thead>
                  <tr>
                    <th>Title</th>
                    <th>Category</th>
                    <th>Date Posted</th>
                    <th>Actions</th>
                  </tr>
                </thead>
                <tbody>
                  {announcements.length > 0 ? (
                    announcements.map((item) => (
                      <tr key={item.id}>
                        <td>{item.title}</td>
                        <td>
                          <Badge color={
                            item.category === 'News' ? 'blue' :
                            item.category === 'Event' ? 'green' :
                            item.category === 'Notice' ? 'yellow' :
                            'red'
                          }>
                            {item.category}
                          </Badge>
                        </td>
                        <td>{formatDate(item.postedAt)}</td>
                        <td>
                          <Group spacing="xs">
                            <ActionIcon color="blue" onClick={() => navigate(`/announcements/${item.id}`)}>
                              <IconEye size={18} />
                            </ActionIcon>
                            <ActionIcon color="yellow" onClick={() => openEdit(item)}>
                              <IconEdit size={18} />
                            </ActionIcon>
                            <ActionIcon color="red" onClick={() => openDelete(item)}>
                              <IconTrash size={18} />
                            </ActionIcon>
                          </Group>
                        </td>
                      </tr>
                    ))
                  ) : (
                    <tr>
                      <td colSpan={4} style={{ textAlign: 'center' }}>
                        No announcements found
                      </td>
                    </tr>
                  )}
                </tbody>
              </Table>
            </Paper>
          </>
        )}

        {/* Create Announcement Modal */}
        <Modal
          opened={createOpened}
          onClose={closeCreateModal}
          title="Create New Announcement"
          size="lg"
        >
          <form onSubmit={handleCreateAnnouncement}>
            <TextInput
              label="Title"
              placeholder="Enter announcement title"
              required
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
              placeholder="Enter announcement content"
              required
              minRows={4}
              value={formData.content}
              onChange={(e) => setFormData({...formData, content: e.target.value})}
              className="mb-3"
            />
            
            <FileInput
              label="Upload Image"
              placeholder="Select image file"
              accept="image/*"
              onChange={(file) => setFormData({...formData, image: file})}
              icon={<IconUpload size={14} />}
              className="mb-4"
            />
            
            <Group position="right" mt="md">
              <Button variant="outline" onClick={closeCreateModal}>Cancel</Button>
              <Button type="submit" color="blue">Create Announcement</Button>
            </Group>
          </form>
        </Modal>

        {/* Edit Announcement Modal */}
        <Modal
          opened={editOpened}
          onClose={closeEditModal}
          title="Edit Announcement"
          size="lg"
        >
          <form onSubmit={handleEditAnnouncement}>
            <TextInput
              label="Title"
              placeholder="Enter announcement title"
              required
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
              placeholder="Enter announcement content"
              required
              minRows={4}
              value={formData.content}
              onChange={(e) => setFormData({...formData, content: e.target.value})}
              className="mb-3"
            />
            
            <Text size="sm" className="mb-2">Current image will be kept unless you upload a new one</Text>
            
            <Group position="right" mt="md">
              <Button variant="outline" onClick={closeEditModal}>Cancel</Button>
              <Button type="submit" color="yellow">Update Announcement</Button>
            </Group>
          </form>
        </Modal>

        {/* Delete Confirmation Modal */}
        <Modal
          opened={deleteOpened}
          onClose={closeDeleteModal}
          title="Confirm Deletion"
          size="sm"
        >
          <Text>Are you sure you want to delete "{selectedAnnouncement?.title}"?</Text>
          <Text size="sm" color="dimmed" className="mt-2">This action cannot be undone.</Text>
          
          <Group position="right" mt="xl">
            <Button variant="outline" onClick={closeDeleteModal}>Cancel</Button>
            <Button color="red" onClick={handleDeleteAnnouncement}>Delete</Button>
          </Group>
        </Modal>

        {activeTab === 'users' && (
          <Paper shadow="md" radius="md" p="xl" className="text-center">
            <Title order={3}>User Management</Title>
            <Text className="mt-2">User management features coming soon!</Text>
          </Paper>
        )}

        {activeTab === 'projects' && (
          <Paper shadow="md" radius="md" p="xl" className="text-center">
            <Title order={3}>Project Management</Title>
            <Text className="mt-2">Project management features coming soon!</Text>
          </Paper>
        )}
      </Container>
    </>
  );
};

export default AdminDashboard;