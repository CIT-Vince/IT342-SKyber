import React, { useState } from 'react';
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
  ActionIcon
} from '@mantine/core';
import { IconShare, IconArrowLeft } from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import { useDisclosure } from '@mantine/hooks';

// Enhanced candidates data with more entries to show the grid layout
const candidates = [
  {
    id: 1,
    firstname: "Juan",
    lastname: "Dela Cruz",
    age: 21,
    address: "Purok 3, Barangay Mabini",
    partylist: "Kabataan Para sa Kaayohan",
    position: "SK Chairman",
    platform: "Maghatag og libreng school supplies, mag-organize og sports events, ug magtabang sa mga kabatan-onan nga nanginahanglan og scholarship. Vote for me kay gusto nako nga ang batan-on mahimong empowered ug active sa barangay! (๑˃ᴗ˂)ﻭ",
    achievements: ["Best Youth Leader 2024", "Community Service Award", "Sports Festival Organizer"]
  },
  {
    id: 2,
    firstname: "Maria",
    lastname: "Santos",
    age: 22,
    address: "Zone 5, Barangay Malipayon",
    partylist: "Batan-on United",
    position: "SK Chairman",
    platform: "Magsugod og livelihood programs para sa kabatan-onan, maghimo og leadership trainings, ug magpadayon sa transparency sa SK projects.",
    achievements: ["Student Council President", "Youth Volunteer of the Year"]
  },
  {
    id: 3,
    firstname: "Pedro",
    lastname: "Reyes",
    age: 20,
    address: "Street 4, Barangay San Jose",
    partylist: "Kabataan Para sa Kaayohan",
    position: "SK Kagawad",
    platform: "Magsulong ng edukasyon at scholarship programs para sa mga kabataan. Maghahanda rin ng sports programs at cultural activities para sa community building.",
    achievements: ["Academic Excellence Award", "Basketball Team Captain"]
  },
  {
    id: 4,
    firstname: "Ana",
    lastname: "Lim",
    age: 21,
    address: "Block 5 Lot 2, Barangay Sta. Rosa",
    partylist: "Batan-on United",
    position: "SK Kagawad",
    platform: "Magpatayo ng youth center at magsagawa ng regular na skills training. Magkakaroon din ng clean-up drives at tree planting activities para sa kalikasan.",
    achievements: ["Environmental Youth Leader", "Digital Skills Trainer"]
  },
  {
    id: 5,
    firstname: "Carlo",
    lastname: "Mendoza",
    age: 23,
    address: "Phase 2, Barangay New Town",
    partylist: "Progressive Youth",
    position: "SK Kagawad",
    platform: "Magtatag ng anti-drug campaigns at awareness programs. Maglulunsad din ng sports leagues at cultural shows para sa mga kabataan.",
    achievements: ["Anti-Drug Campaign Leader", "Community Theater Director"]
  },
  {
    id: 6,
    firstname: "Sophia",
    lastname: "Garcia",
    age: 20,
    address: "Lot 7, Barangay San Miguel",
    partylist: "Progressive Youth",
    position: "SK Treasurer",
    platform: "Mangangasiwa ng transparent na pamamahala ng pondo. Magsasagawa ng financial literacy workshops para sa mga kabataan.",
    achievements: ["Math Olympiad Winner", "Budgeting Workshop Facilitator"]
  },
  {
    id: 7,
    firstname: "Miguel",
    lastname: "Tan",
    age: 22,
    address: "Unit 3, Barangay Rizal",
    partylist: "Kabataan Para sa Kaayohan",
    position: "SK Secretary",
    platform: "Magtutok sa digital literacy at e-governance. Magsasaayos ng maayos na record-keeping at communication systems para sa SK.",
    achievements: ["IT Club President", "Digital Transformation Advocate"]
  },
  {
    id: 8,
    firstname: "Luisa",
    lastname: "Reyes",
    age: 21,
    address: "Area 2, Barangay Del Pilar",
    partylist: "Batan-on United",
    position: "SK Kagawad",
    platform: "Maglulunsad ng mental health awareness campaigns at support programs. Magkakaroon din ng creative arts workshops para sa kabataan.",
    achievements: ["Peer Counselor", "Arts Festival Organizer"]
  },
  {
    id: 9,
    firstname: "Roberto",
    lastname: "Cruz",
    age: 23,
    address: "Lane 5, Barangay Bagong Silang",
    partylist: "Progressive Youth",
    position: "SK Kagawad",
    platform: "Magpapatupad ng youth entrepreneurship programs at job fairs. Magsasagawa din ng skills training para sa employment readiness.",
    achievements: ["Young Entrepreneur Award", "Job Fair Coordinator"]
  }
];

const CandidatesList = () => {
  const [selectedCandidate, setSelectedCandidate] = useState(null);
  const [opened, { open, close }] = useDisclosure(false);

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
            Candidates
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Meet your aspiring leaders! Discover their dreams, platforms, and choose who will make our community shine brighter~ (｡•̀ᴗ-)✧
          </Text>
        </header>
      </div>

      {/* Main content section with grid layout */}
      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-7xl mx-auto">
          <Grid gutter="lg">
            {candidates.map((candidate) => (
              <Grid.Col key={candidate.id} span={{ base: 12, sm: 6, lg: 4 }}>
                <Card 
                  shadow="md" 
                  radius="lg" 
                  className="flex flex-col transition-transform duration-300 hover:scale-[1.02]"
                >
                  <div className="flex p-3">
                    {/* Candidate Image */}
                    <Avatar
                      src={`https://api.dicebear.com/7.x/personas/svg?seed=${candidate.firstname}${candidate.lastname}`}
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
          <div className="mt-8 text-center text-xs text-gray-400">Stay kawaii and informed! (づ｡◕‿‿◕｡)づ</div>
        </div>
      </div>
      
      {/* Enhanced Modal for Candidate Details */}
      <Modal
        opened={opened}
        onClose={close}
        fullScreen
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
              <Paper radius="lg" shadow="md" className="p-6 flex flex-col md:flex-row items-center mb-6">
                <Avatar
                  src={`https://api.dicebear.com/7.x/personas/svg?seed=${selectedCandidate.firstname}${selectedCandidate.lastname}`}
                  alt={`${selectedCandidate.firstname} ${selectedCandidate.lastname}`}
                  size={150}
                  radius={150}
                  className="border-4 border-blue-200 mb-4 md:mb-0"
                />
                
                <div className="md:ml-8 text-center md:text-left">
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
                  <div className="grid grid-cols-2 gap-y-2 gap-x-4 mt-3">
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
                      <Text weight={500}>{selectedCandidate.address}</Text>
                    </div>
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
              <Paper radius="lg" shadow="md" className="p-6">
                <Title order={3} className="text-blue-700 mb-4">Achievements & Experience</Title>
                <Divider className="mb-4" />
                <div className="space-y-2">
                  {selectedCandidate.achievements?.map((achievement, idx) => (
                    <div key={idx} className="flex items-center">
                      <div className="w-2 h-2 bg-blue-500 rounded-full mr-3"></div>
                      <Text>{achievement}</Text>
                    </div>
                  ))}
                </div>
              </Paper>
              
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