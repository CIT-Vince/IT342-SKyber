import React, { useState, useEffect } from 'react';
import { useAuth } from '../../contexts/AuthContext';
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
  Group,
  Select,
  LoadingOverlay
} from '@mantine/core';
import { IconSearch, IconMapPin, IconExternalLink, IconBuilding, IconClock } from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import { notifications } from '@mantine/notifications';
import { apiFetch } from '../utils/api';
import { Modal, FileInput, Textarea, ActionIcon } from '@mantine/core';
import { IconPlus, IconEdit, IconTrash } from '@tabler/icons-react';


const JobListings = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState('all');
  const [locationFilter, setLocationFilter] = useState('all');
  const locations = ['all', ...new Set(jobs.map(job => job.location))];
  {/* Admin check */}
  const { currentUser } = useAuth();
  const [isAdmin, setIsAdmin] = useState(false);
  const [createModalOpen, setCreateModalOpen] = useState(false);
  const [editModalOpen, setEditModalOpen] = useState(false);
  const [deleteModalOpen, setDeleteModalOpen] = useState(false);
  const [selectedJob, setSelectedJob] = useState(null);
  const [jobForm, setJobForm] = useState({
    jobTitle: '',
    companyName: '',
    address: '',
    employementType: 'full-time',
    description: '',
    applicationlink: '',
    imageFile: null
  });
  const sanitizeLink = (link) => {
    if (!link) return '';
    return link.startsWith('http://') || link.startsWith('https://')
      ? link
      : `https://${link}`;
  };
  
  const fetchJobs = async () => {
    try {
      setLoading(true);
      
      // API endpoint
      const API_URL = '/api/jobs/getAllJobs';
      console.log("Fetching job listings from:", API_URL);
      
      // Replace fetch with apiFetch
      const response = await apiFetch(API_URL);
      
      if (!response.ok) {
        throw new Error(`Server responded with ${response.status}`);
      }
      
      const data = await response.json();
      console.log("Retrieved job listings:", data);
      
      // Transform data to match our component's expected structure
      if (data && data.length > 0) {
        const transformedJobs = data.map(job => ({
          id: job.id || Math.random().toString(),
          jobtitle: job.jobTitle || "Untitled Position",
          companyName: job.companyName || "Company Name",
          location: job.address || "Location not specified",
          description: job.description || "No description provided",
          address: job.address || "Address not specified",
          applicationLink: job.applicationlink || "#",
          employmentType: (job.employementType || "full-time").toLowerCase(),
          salary: job.salary || "Competitive salary",
          postedDate: "Recently posted",
          saved: false
        }));
        
        setJobs(transformedJobs);
        
        notifications.show({
          title: 'Jobs Loaded (｡•̀ᴗ-)✧',
          message: `Successfully loaded ${transformedJobs.length} job listings`,
          color: 'green',
        });
      } else {
        console.warn("No job listings found (´。＿。｀)");
      }
    } catch (error) {
      console.error('Error fetching jobs:', error);
      
      notifications.show({
        title: 'Error Loading Jobs (ノಠ益ಠ)ノ彡┻━┻',
        message: 'Using sample data instead',
        color: 'red',
      });
    } finally {
      setLoading(false);
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

  // Filter 
  const filteredJobs = jobs.filter(job => {
    const matchesSearch = job.jobtitle.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         job.companyName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         job.description.toLowerCase().includes(searchQuery.toLowerCase());
    
    const matchesEmployment = activeTab === 'all' || job.employmentType === activeTab;
    
    const matchesLocation = locationFilter === 'all' || job.location === locationFilter;
    
    return matchesSearch && matchesEmployment && matchesLocation;
  });

  // Fetch jobs from API
  useEffect(() => {
    
    fetchJobs();
  }, []);


  // Modal handlers
  const handleCreateClick = () => {
    setJobForm({
      jobTitle: '',
      companyName: '',
      address: '',
      employementType: 'full-time',
      description: '',
      applicationlink: '',
      imageFile: null
    });
    setCreateModalOpen(true);
  };

  const handleEditClick = (job) => {
    setSelectedJob(job);
    setJobForm({
      jobTitle: job.jobtitle,
      companyName: job.companyName,
      address: job.address,
      employementType: job.employmentType,
      description: job.description,
      applicationlink: job.applicationLink,
      imageFile: null // Can't prefill image
    });
    setEditModalOpen(true);
  };

  const handleDeleteClick = (job) => {
    setSelectedJob(job);
    setDeleteModalOpen(true);
  };

  const handleCreateSubmit = async (e) => {
    e.preventDefault();
  
    if (!jobForm.jobTitle || !jobForm.companyName || !jobForm.address || !jobForm.description) {
      notifications.show({
        title: 'Validation Error',
        message: 'Please fill in all required fields.',
        color: 'red'
      });
      return;
    }
  
    try {
      setLoading(true);
  
      if (jobForm.imageFile) {
        const formData = new FormData();
        formData.append('jobTitle', jobForm.jobTitle);
        formData.append('companyName', jobForm.companyName);
        formData.append('address', jobForm.address);
        formData.append('employementType', jobForm.employementType);
        formData.append('description', jobForm.description);
        formData.append('applicationlink', sanitizeLink(jobForm.applicationlink));
        formData.append('image', jobForm.imageFile);
  
        const response = await apiFetch('/api/jobs/createJob/with-image', {
          method: 'POST',
          body: formData
        });
  
        if (!response.ok) throw new Error(await response.text());
      } else {
        const jobData = {
          jobTitle: jobForm.jobTitle,
          companyName: jobForm.companyName,
          address: jobForm.address,
          employementType: jobForm.employementType,
          description: jobForm.description,
          applicationlink: sanitizeLink(jobForm.applicationlink)
        };
  
        const response = await apiFetch('/api/jobs/createJob', {
          method: 'POST',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(jobData)
        });
  
        if (!response.ok) throw new Error(await response.text());
      }
  
      notifications.show({ title: 'Success', message: 'Job created!', color: 'green' });
      setCreateModalOpen(false);
      fetchJobs();
    } catch (error) {
      notifications.show({ title: 'Error', message: error.message, color: 'red' });
    } finally {
      setLoading(false);
    }
  };

  const handleEditSubmit = async (e) => {
    e.preventDefault();
    if (!selectedJob) return;
  
    try {
      setLoading(true);
  
      if (jobForm.imageFile) {
        const formData = new FormData();
        formData.append('jobTitle', jobForm.jobTitle);
        formData.append('companyName', jobForm.companyName);
        formData.append('address', jobForm.address);
        formData.append('employementType', jobForm.employementType);
        formData.append('description', jobForm.description);
        formData.append('applicationlink', sanitizeLink(jobForm.applicationlink));
        formData.append('image', jobForm.imageFile);
  
        const response = await apiFetch(`/api/jobs/updateJob/with-image/${selectedJob.id}`, {
          method: 'PUT',
          body: formData
        });
  
        if (!response.ok) throw new Error(await response.text());
      } else {
        const jobData = {
          jobTitle: jobForm.jobTitle,
          companyName: jobForm.companyName,
          address: jobForm.address,
          employementType: jobForm.employementType,
          description: jobForm.description,
          applicationlink: sanitizeLink(jobForm.applicationlink)
        };
  
        const response = await apiFetch(`/api/jobs/updateJob/${selectedJob.id}`, {
          method: 'PUT',
          headers: { 'Content-Type': 'application/json' },
          body: JSON.stringify(jobData)
        });
  
        if (!response.ok) throw new Error(await response.text());
      }
  
      notifications.show({ title: 'Success', message: 'Job updated!', color: 'green' });
      setEditModalOpen(false);
      fetchJobs();
    } catch (error) {
      notifications.show({ title: 'Error', message: error.message, color: 'red' });
    } finally {
      setLoading(false);
    }
  };

  const handleDeleteConfirm = async () => {
    if (!selectedJob) return;
    try {
      setLoading(true);
      const response = await apiFetch(`/api/jobs/deleteJob/${selectedJob.id}`, { method: 'DELETE' });
      if (!response.ok) throw new Error(await response.text());
      notifications.show({ title: 'Deleted', message: 'Job deleted!', color: 'green' });
      setDeleteModalOpen(false);
      fetchJobs();
    } catch (error) {
      notifications.show({ title: 'Error', message: error.message, color: 'red' });
    } finally {
      setLoading(false);
    }
  };

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
              Job Opportunities<span className="animate-bounce inline-block ml-2">💼</span>
            </Title>
            <Text color="white" className="mt-2 max-w-2xl">
              Find your dream job and start building your career with these amazing opportunities!
            </Text>
        </header>
      </div>

      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        {loading ? (
          <div className="flex items-center justify-center h-64">
            <LoadingOverlay visible={true} overlayBlur={2} />
            <div className="text-center">
              <Title order={3}>Loading Job Opportunities</Title>
              <Text color="dimmed">Please wait while we fetch available positions...</Text>
            </div>
          </div>
        ) : (
        <div className="max-w-7xl mx-auto">
          
          {/* Search and filter section (⌒‿⌒) */}
          <Paper shadow="md" radius="lg" className="p-6 mb-8">
            <Grid>
              <Grid.Col span={{ base: 12, md: 6 }}>
                <TextInput
                  icon={<IconSearch size={18} />}
                  placeholder="Search jobs by title, company or keywords..."
                  value={searchQuery}
                  onChange={(e) => setSearchQuery(e.target.value)}
                  radius="xl"
                  size="md"
                  className="mb-4 md:mb-0"
                />
              </Grid.Col>
              <Grid.Col span={{ base: 12, md: 3 }}>
                <Select
                  data={locations.map(loc => ({ value: loc, label: loc === 'all' ? 'All Locations' : loc }))}
                  value={locationFilter}
                  onChange={setLocationFilter}
                  placeholder="Filter by location"
                  icon={<IconMapPin size={18} />}
                  radius="xl"
                  size="md"
                  className="mb-4 md:mb-0"
                />
              </Grid.Col>
              <Grid.Col span={{ base: 12, md: 3 }}>
                <Tabs value={activeTab} onChange={setActiveTab} radius="xl" variant="pills">
                  <Tabs.List grow>
                    <Tabs.Tab value="all">All</Tabs.Tab>
                    <Tabs.Tab value="full-time">Full Time</Tabs.Tab>
                    <Tabs.Tab value="part-time">Part Time</Tabs.Tab>
                  </Tabs.List>
                </Tabs>
                <Grid.Col span={12} className="mt-4">
                {isAdmin && (
                  <div className="mb-4 flex justify-end">
                    <Button
                      leftSection={<IconPlus size={16} />}
                      variant="gradient"
                      gradient={{ from: 'blue', to: 'cyan' }}
                      onClick={handleCreateClick}
                      radius="md"
                    >
                      Add New Job
                    </Button>
                  </div>
                )}
              </Grid.Col>
              </Grid.Col>
              {/* <Grid.Col span={12} className="mt-4">
              <div className="mb-4 flex justify-end">
                {isAdmin && (
                  <Group spacing={4}>
                    <Button onClick={() => { setSelectedJob(job); setEditModalOpen(true); }}>Edit</Button>
                    <Button color="red" onClick={() => { setSelectedJob(job); setDeleteModalOpen(true); }}>Delete</Button>
                  </Group>
                )}
              </div>
                
              </Grid.Col> */}
            </Grid>
          </Paper>

          {/* Job listing cards (◕‿◕✿) */}
          {filteredJobs.length > 0 ? (
            <Grid gutter="lg">
              {filteredJobs.map((job) => (
                <Grid.Col key={job.id} span={12}>
                  <Card shadow="md" radius="md" className="transition-transform duration-300 hover:scale-[1.01]">
                    <div className="flex flex-col md:flex-row gap-4">
                      <div className="flex-grow p-4">
                        <div className="flex flex-wrap justify-between mb-2">
                          <Title order={3} className="text-black-700">
                            {job.jobtitle}
                          </Title>
                          <Badge 
                            variant="gradient" 
                            gradient={{ from: job.employmentType === 'full-time' ? 'blue' : 'pink', to: 'cyan' }}
                            radius="md"
                            size="lg"
                          >
                            {job.employmentType === 'full-time' ? 'Full Time' : 'Part Time'}
                          </Badge>
                          {isAdmin && (
                              <Group spacing={4} className="ml-2">
                                <ActionIcon 
                                  variant="light" 
                                  color="yellow"
                                  onClick={(e) => {
                                    e.stopPropagation();
                                    handleEditClick(job);
                                  }}
                                >
                                  <IconEdit size={16} />
                                </ActionIcon>
                                <ActionIcon 
                                  variant="light" 
                                  color="red"
                                  onClick={(e) => {
                                    e.stopPropagation();
                                    handleDeleteClick(job);
                                  }}
                                >
                                  <IconTrash size={16} />
                                </ActionIcon>
                              </Group>
                            )}
                        </div>
                        
                        <Group spacing="xs" className="mb-3">
                          <IconBuilding size={16} className="text-gray-500" />
                          <Text weight={500}>{job.companyName}</Text>
                          <Text color="dimmed" size="sm">•</Text>
                          <Group spacing={4}>
                            <IconMapPin size={16} className="text-gray-500" />
                            <Text size="sm">{job.location}</Text>
                          </Group>
                          <Text color="dimmed" size="sm">•</Text>
                          <Group spacing={4}>
                            <IconClock size={16} className="text-gray-500" />
                            <Text size="sm">{job.postedDate}</Text>
                          </Group>
                        </Group>
                        
                        <Text size="sm" className="mb-3">
                          {job.description}
                        </Text>
                        
                        <Group spacing="xs" className="mb-3">
                          <Badge color="green" variant="light">{job.salary}</Badge>
                          <Badge color="blue" variant="light">{job.address}</Badge>
                        </Group>
                      </div>
                      
                      <div className="flex flex-row md:flex-col justify-between md:border-l md:border-gray-200 p-4 pt-10">
                        <Button
                          component="a"
                          href={job.applicationLink}
                          target="_blank"
                          rel="noopener noreferrer" 
                          variant="gradient"
                          gradient={{ from: 'blue', to: 'cyan' }}
                          radius="xl"
                          className="mt-3"
                          leftIcon={<IconExternalLink size={16} />}
                        >
                          Apply Now
                        </Button>
                      </div>
                    </div>
                  </Card>
                </Grid.Col>
              ))}
            </Grid>
          ) : (
            <Paper shadow="md" radius="lg" className="p-10 text-center">
              <Title order={3} className="mb-2">No jobs found (´。＿。｀)</Title>
              <Text>Try changing your search or filters to find more opportunities!</Text>
            </Paper>
          )}
        </div>
        )}
      </div>
      {/* Create Job Modal */}
      <Modal
        opened={createModalOpen}
        onClose={() => setCreateModalOpen(false)}
        title="Add New Job"
        size="lg"
      >
        <form onSubmit={handleCreateSubmit}>
          <TextInput
            label="Job Title"
            required
            placeholder="Position title"
            value={jobForm.jobTitle}
            onChange={(e) => setJobForm({...jobForm, jobTitle: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Company Name"
            required
            placeholder="Company name"
            value={jobForm.companyName}
            onChange={(e) => setJobForm({...jobForm, companyName: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Address/Location"
            required
            placeholder="Job location"
            value={jobForm.address}
            onChange={(e) => setJobForm({...jobForm, address: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Employment Type"
            data={[
              { value: 'full-time', label: 'Full Time' },
              { value: 'part-time', label: 'Part Time' },
            ]}
            value={jobForm.employementType}
            onChange={(value) => setJobForm({...jobForm, employementType: value})}
            className="mb-3"
            required
          />
          
          <TextInput
            label="Application Link"
            placeholder="https://example.com/apply"
            value={jobForm.applicationlink}
            onChange={(e) => setJobForm({...jobForm, applicationlink: e.target.value})}
            className="mb-3"
          />
          
          <Textarea
            label="Job Description"
            placeholder="Describe the job responsibilities, requirements, and benefits"
            required
            minRows={4}
            value={jobForm.description}
            onChange={(e) => setJobForm({...jobForm, description: e.target.value})}
            className="mb-3"
          />
          
          <FileInput
            label="Company Logo (Optional)"
            placeholder="Upload an image"
            accept="image/*"
            onChange={(file) => setJobForm({...jobForm, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={() => setCreateModalOpen(false)}>Cancel</Button>
            <Button type="submit" color="blue">Create Job</Button>
          </Group>
        </form>
      </Modal>

      {/* Edit Job Modal */}
      <Modal
        opened={editModalOpen}
        onClose={() => setEditModalOpen(false)}
        title="Edit Job"
        size="lg"
      >
        <form onSubmit={handleEditSubmit}>
          <TextInput
            label="Job Title"
            required
            placeholder="Position title"
            value={jobForm.jobTitle}
            onChange={(e) => setJobForm({...jobForm, jobTitle: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Company Name"
            required
            placeholder="Company name"
            value={jobForm.companyName}
            onChange={(e) => setJobForm({...jobForm, companyName: e.target.value})}
            className="mb-3"
          />
          
          <TextInput
            label="Address/Location"
            required
            placeholder="Job location"
            value={jobForm.address}
            onChange={(e) => setJobForm({...jobForm, address: e.target.value})}
            className="mb-3"
          />
          
          <Select
            label="Employment Type"
            data={[
              { value: 'full-time', label: 'Full Time' },
              { value: 'part-time', label: 'Part Time' },
            ]}
            value={jobForm.employementType}
            onChange={(value) => setJobForm({...jobForm, employementType: value})}
            className="mb-3"
            required
          />
          
          <TextInput
            label="Application Link"
            placeholder="https://example.com/apply"
            value={jobForm.applicationlink}
            onChange={(e) => setJobForm({...jobForm, applicationlink: e.target.value})}
            className="mb-3"
          />
          
          <Textarea
            label="Job Description"
            placeholder="Describe the job responsibilities, requirements, and benefits"
            required
            minRows={4}
            value={jobForm.description}
            onChange={(e) => setJobForm({...jobForm, description: e.target.value})}
            className="mb-3"
          />
          
          <FileInput
            label="Company Logo (Optional)"
            description="Leave empty to keep existing image"
            placeholder="Upload a new image"
            accept="image/*"
            onChange={(file) => setJobForm({...jobForm, imageFile: file})}
            className="mb-4"
          />
          
          <Group position="right" mt="md">
            <Button variant="outline" onClick={() => setEditModalOpen(false)}>Cancel</Button>
            <Button type="submit" color="yellow">Update Job</Button>
          </Group>
        </form>
      </Modal>

      {/* Delete Confirmation Modal */}
      <Modal
        opened={deleteModalOpen}
        onClose={() => setDeleteModalOpen(false)}
        title="Delete Job"
        size="sm"
      >
        {selectedJob && (
          <>
            <Text>Are you sure you want to delete "{selectedJob.jobtitle}"?</Text>
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
    </>
  );
};

export default JobListings;