import React, { useState, useEffect } from 'react';
import { 
  Modal, 
  Button, 
  Text,
  Title,
  Paper,
  Grid,
  Card,
  Badge,
  Group,
  Avatar,
  Divider,
  Loader,
  TextInput,
  Select
} from '@mantine/core';
import { notifications } from '@mantine/notifications';
import { IconSearch, IconShare, IconArrowLeft } from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';

const CandidatesList = () => {
  const [candidates, setCandidates] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [selectedCandidate, setSelectedCandidate] = useState(null);
  const [searchQuery, setSearchQuery] = useState('');
  const [activePartylist, setActivePartylist] = useState('All');
  const [opened, { open, close }] = useDisclosure(false);

  // Fetch candidates from backend
  useEffect(() => {
    const fetchCandidates = async () => {
      try {
        setLoading(true);
        
        const API_URL = '/api/candidates/getAllCandidates';
        console.log("Fetching candidates from:", API_URL);
        
        const response = await fetch(API_URL);
        
        // Better error handling
        if (!response.ok) {
          console.error(`Server responded with ${response.status}: ${response.statusText}`);
          throw new Error(`Server responded with ${response.status}`);
        }
        
        const data = await response.json();
        console.log("Retrieved candidates data:", data);
        
        if (data && data.length > 0) {
          const transformedData = data.map(item => ({
            id: item.id,
            firstname: item.firstName || '', 
            lastname: item.lastName || '',   
            age: item.age || '',
            address: item.address || '',
            partylist: item.partylist || 'Independent',
            position: item.position || 'Candidate',
            platform: item.platform || 'No platform provided.',
            candidateImage: item.candidateImage || '',
            achievements: item.achievements ? 
              item.achievements.split(',').map(a => a.trim()) : 
              ['Community Service'] 
          }));
          
          setCandidates(transformedData);
          setError(null);
          
          notifications.show({
            title: 'Candidates Loaded',
            message: `Successfully loaded ${transformedData.length} candidates`,
            color: 'green',
          });
        } else {
          console.warn("Server returned empty candidates data");
          throw new Error("No candidates found in server response");
        }
      } catch (error) {
        console.error("Error fetching candidates:", error);
        setError(error.message);
        
        notifications.show({
          title: 'Error Loading Candidates',
          message: 'Please try again later or contact support',
          color: 'red',
        });
      } finally {
        setLoading(false);
      }
    };
    
    fetchCandidates();
  }, []);

  // Filter
  const filteredCandidates = candidates.filter(candidate => {
    const matchesSearch = 
      candidate.firstname.toLowerCase().includes(searchQuery.toLowerCase()) || 
      candidate.lastname.toLowerCase().includes(searchQuery.toLowerCase()) ||
      candidate.platform.toLowerCase().includes(searchQuery.toLowerCase());
    
    const matchesPartylist = activePartylist === 'All' || candidate.partylist === activePartylist;
    
    return matchesSearch && matchesPartylist;
  });
  
  const partylistOptions = ['All', ...new Set(candidates.map(c => c.partylist))];

  const handleView = (candidate) => {
    setSelectedCandidate(candidate);
    open();
  };

  // Function to get color based on partylist
  const getPartyColor = (partylist) => {
    switch(partylist) {
      case 'Kabataan Para sa Kaayohan': return { from: 'blue', to: 'cyan' };
      case 'Batan-on United': return { from: 'pink', to: 'violet' };
      case 'Progressive Youth': return { from: 'teal', to: 'blue' };
      default: return { from: 'gray', to: 'blue' };
    }
  };

  if (loading) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-pink-50">
          <div className="text-center">
            <Loader size="xl" variant="dots" className="mx-auto mb-4" />
            <Title order={3}>Loading Candidates</Title>
            <Text color="dimmed">Please wait while we fetch the candidates...</Text>
          </div>
        </div>
      </>
    );
  }

  if (error) {
    return (
      <>
        <Navbar />
        <div className="min-h-screen flex items-center justify-center bg-gradient-to-br from-blue-50 to-pink-50 ">
          <Paper shadow="md" p="xl" className="max-w-md text-center">
            <Title order={2} className="mb-4 text-red-500">Error Loading Candidates</Title>
            <Text className="mb-6">{error}</Text>
            <Button onClick={() => window.location.reload()}>
              Try Again
            </Button>
          </Paper>
        </div>
      </>
    );
  }

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
        <header className="text-left py-10 pl-10 pt-30!">
          <Title className="text-5xl font-bold text-white">
            Candidates
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Meet your aspiring leaders! Discover their dreams, platforms, and choose who will make our community shine brighter~ (｡•̀ᴗ-)✧
          </Text>
        </header>
      </div>

      {/* Search and filters */}
      <div className="bg-white shadow-md py-4">
        <div className="max-w-7xl mx-auto px-4">
          <div className="flex flex-col md:flex-row gap-4 items-center justify-between">
            <TextInput
              icon={<IconSearch size={18} />}
              placeholder="Search candidates..."
              value={searchQuery}
              onChange={(e) => setSearchQuery(e.target.value)}
              className="w-full md:w-1/2"
            />
            <Select
              label="Filter by partylist"
              value={activePartylist}
              onChange={setActivePartylist}
              data={partylistOptions}
              className="w-full md:w-1/4"
            />
          </div>
        </div>
      </div>

      {/* Main content section with grid layout */}
      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-7xl mx-auto">
          {filteredCandidates.length > 0 ? (
            <Grid gutter="lg">
              {filteredCandidates.map((candidate) => (
                <Grid.Col key={candidate.id} span={{ base: 12, sm: 6, lg: 4 }}>
                  <Card 
                    shadow="md" 
                    radius="lg" 
                    className="flex flex-col transition-transform duration-300 hover:scale-[1.02]"
                  >
                    <div className="flex p-3">
                      {/* Candidate Image */}
                      <Avatar
                        src={candidate.candidateImage ? 
                          `data:image/jpeg;base64,${candidate.candidateImage}` : 
                          `https://api.dicebear.com/7.x/personas/svg?seed=${candidate.firstname}${candidate.lastname}`}
                        alt={`${candidate.firstname} ${candidate.lastname} avatar`}
                        size={80}
                        radius={80}
                        className="border-2 border-blue-300"
                      />
                      
                      {/* Candidate Info */}
                      <div className="ml-4 flex-1">
                        <Title order={3} className="text-blue-700">
                          {candidate.firstname} {candidate.lastname}
                        </Title>
                        
                        <Group spacing={8} className="mb-1">
                          <Text size="sm" color="dimmed">Age: {candidate.age}</Text>
                          <Text color="dimmed" size="sm">•</Text>
                          <Text size="sm" color="dimmed">{candidate.position}</Text>
                        </Group>
                        
                        <Badge 
                          variant="gradient" 
                          gradient={getPartyColor(candidate.partylist)}
                          className="mb-2"
                        >
                          {candidate.partylist}
                        </Badge>
                        
                        <Button
                          variant="light" 
                          color="blue"
                          size="xs"
                          radius="xl"
                          onClick={() => handleView(candidate)}
                        >
                          View Platform
                        </Button>
                      </div>
                    </div>
                  </Card>
                </Grid.Col>
              ))}
            </Grid>
          ) : (
            <Paper shadow="md" radius="lg" className="p-10 text-center mt-10">
              <Title order={3} className="mb-2">No candidates found (´。＿。｀)</Title>
              <Text>Try changing your search or filters!</Text>
            </Paper>
          )}
          
          <div className="mt-8 text-center text-xs text-gray-400">
            Stay kawaii and informed! (づ｡◕‿‿◕｡)づ
          </div>
        </div>
      </div>
      
      {/* Enhanced Modal for Candidate Details */}
      <Modal
        opened={opened}
        onClose={close}
        size="xl"
        centered
        withCloseButton={false}
        styles={{
          body: { padding: 0 },
          content: { background: 'linear-gradient(to bottom, #f0f4ff, #fff1f9)' }
        }}
      >
        {selectedCandidate && (
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
              <Title order={3} className="mx-auto pr-10">Candidate Profile</Title>
            </div>

            <div className="p-6 max-w-5xl mx-auto w-full">
              {/* Candidate Header Card */}
              <Paper radius="lg" shadow="md" className="p-6 flex flex-col items-center mb-6">
                {/* Centered Avatar */}
                <Avatar
                  src={selectedCandidate.candidateImage ? 
                    `data:image/jpeg;base64,${selectedCandidate.candidateImage}` : 
                    `https://api.dicebear.com/7.x/personas/svg?seed=${selectedCandidate.firstname}${selectedCandidate.lastname}`}
                  alt={`${selectedCandidate.firstname} ${selectedCandidate.lastname}`}
                  size={150}
                  radius={150}
                  className="border-4 border-blue-200 mb-4 mx-auto"
                />
                
                {/* Centered Name and Partylist */}
                <div className="text-center w-full mb-4">
                  <Title order={2} className="text-blue-700">
                    {selectedCandidate.firstname} {selectedCandidate.lastname}
                  </Title>
                  <Badge 
                    variant="gradient" 
                    gradient={getPartyColor(selectedCandidate.partylist)}
                    size="lg"
                    className="my-2"
                  >
                    {selectedCandidate.partylist}
                  </Badge>
                </div>
                
                {/* Details Grid - Keeping this section more structured */}
                <div className="grid grid-cols-2 gap-y-2 gap-x-4 w-full max-w-md mx-auto">
                  <div>
                    <Text size="sm" color="dimmed">Position:</Text>
                    <Text weight={500}>{selectedCandidate.position}</Text>
                  </div>
                  <div>
                    <Text size="sm" color="dimmed">Age:</Text>
                    <Text weight={500}>{selectedCandidate.age}</Text>
                  </div>
                  <div className="col-span-2">
                    <Text size="sm" color="dimmed">Address:</Text>
                    <Text weight={500}>{selectedCandidate.address || 'Not provided'}</Text>
                  </div>
                </div>
              </Paper>
              
              {/* Platform Section */}
              <Paper radius="lg" shadow="md" className="p-6 mb-6">
                <Title order={3} className="text-blue-700 mb-4">Platform</Title>
                <Divider className="mb-4" />
                <Text className="text-lg leading-relaxed whitespace-pre-line">
                  {selectedCandidate.platform}
                </Text>
                
                <Button 
                  variant="gradient"
                  gradient={{ from: 'pink', to: 'blue' }}
                  className="mt-6 mx-auto block"
                  radius="xl"
                >
                  Support {selectedCandidate.firstname}!
                </Button>
              </Paper>
              
              {/* Achievements Section */}
              {selectedCandidate.achievements && selectedCandidate.achievements.length > 0 && (
                <Paper radius="lg" shadow="md" className="p-6">
                  <Title order={3} className="text-blue-700 mb-4">Achievements & Experience</Title>
                  <Divider className="mb-4" />
                  <div className="space-y-2">
                    {selectedCandidate.achievements.map((achievement, idx) => (
                      <div key={idx} className="flex items-center">
                        <div className="w-2 h-2 bg-blue-500 rounded-full mr-3"></div>
                        <Text>{achievement}</Text>
                      </div>
                    ))}
                  </div>
                </Paper>
              )}
              
              {/* Social Share and Close Section */}
              <Group position="apart" className="mt-6">
                <Button 
                  leftSection={<IconShare size={18} />}
                  variant="light"
                  radius="xl"
                  color="blue"
                >
                  Share Profile
                </Button>
                
                <Button 
                  variant="gradient"
                  gradient={{ from: 'blue', to: 'cyan' }}
                  onClick={close}
                  radius="xl"
                >
                  Back to Candidates
                </Button>
              </Group>
              
              <Text align="center" size="xs" color="dimmed" className="mt-8">
                Padayon, kabatan-onan! (｡•̀ᴗ-)✧
              </Text>
            </div>
          </div>
        )}
      </Modal>
    </>
  );
};

export default CandidatesList;