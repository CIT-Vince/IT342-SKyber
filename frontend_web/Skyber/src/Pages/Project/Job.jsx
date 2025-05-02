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
  Group,
  Select,
  LoadingOverlay
} from '@mantine/core';
import { IconSearch, IconMapPin, IconExternalLink, IconBuilding, IconClock } from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import { notifications } from '@mantine/notifications';

const JobListings = () => {
  const [jobs, setJobs] = useState([]);
  const [loading, setLoading] = useState(true);
  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState('all');
  const [locationFilter, setLocationFilter] = useState('all');

  // Fetch jobs from API
  useEffect(() => {
    const fetchJobs = async () => {
      try {
        setLoading(true);
        
        // API endpoint
        const API_URL = '/api/jobs/getAllJobs';
        console.log("Fetching job listings from:", API_URL);
        
        const response = await fetch(API_URL);
        
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
            title: 'Jobs Loaded',
            message: `Successfully loaded ${transformedJobs.length} job listings`,
            color: 'green',
          });
        } else {
          console.warn("No job listings found");
          // Keep the sample data if no API data
        }
      } catch (error) {
        console.error('Error fetching jobs:', error);
        
        notifications.show({
          title: 'Error Loading Jobs',
          message: 'Using sample data instead',
          color: 'red',
        });
        
        // Sample data is already loaded in the initial state
      } finally {
        setLoading(false);
      }
    };
    
    fetchJobs();
  }, []);

  // List of unique locations for the filter dropdown
  const locations = ['all', ...new Set(jobs.map(job => job.location))];

  // Filter jobs based on search, employment type, and location (⌒‿⌒)
  const filteredJobs = jobs.filter(job => {
    const matchesSearch = job.jobtitle.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         job.companyName.toLowerCase().includes(searchQuery.toLowerCase()) ||
                         job.description.toLowerCase().includes(searchQuery.toLowerCase());
    
    const matchesEmployment = activeTab === 'all' || job.employmentType === activeTab;
    
    const matchesLocation = locationFilter === 'all' || job.location === locationFilter;
    
    return matchesSearch && matchesEmployment && matchesLocation;
  });

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
              Job Opportunities
            </Title>
            <Text color="white" className="mt-2 max-w-2xl">
              Find your dream job and start building your career with these amazing opportunities! (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
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
              </Grid.Col>
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
    </>
  );
};

export default JobListings;