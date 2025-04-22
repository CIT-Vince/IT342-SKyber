import React, { useState,useEffect } from 'react';
import { 
  Title,
  Text,
  Paper,
  Grid,
  Badge,
  Group,
  Avatar,
  Button,
  ActionIcon,
  Tabs,
  Divider
} from '@mantine/core';
import { 
  IconBrandFacebook, 
  IconMail, 
  IconPhone, 
  IconMapPin,
  IconFlipVertical,
  IconCalendar
} from '@tabler/icons-react';
import Navbar from '../../components/Navbar';
import { useLoading } from '../../components/LoadingProvider';


// Updated sample SK officials data with term years
const skOfficials = [
  {
    id: 1,
    firstName: "Juan",
    lastName: "Dela Cruz",
    position: "SK Chairman",
    platform: "Prioritizing educational programs and creating opportunities for the youth to discover and develop their talents and skills.",
    role: "ADMIN",
    email: "juan.delacruz@skyber.ph",
    phoneNumber: "09123456789",
    address: "Purok 3, Barangay Mabini",
    age: 23,
    gender: "Male",
    term: "2022-2025",
    achievements: ["Led COVID-19 Relief Operations", "Implemented Free WiFi in Barangay Centers", "Organized Youth Leadership Summit 2024"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=JuanDelaCruz&backgroundColor=b6e3f4"
  },
  {
    id: 2,
    firstName: "Maria",
    lastName: "Santos",
    position: "SK Councilor",
    platform: "Focusing on health and wellness programs for the youth, including mental health awareness and sports activities.",
    role: "USER",
    email: "maria.santos@skyber.ph",
    phoneNumber: "09123456788",
    address: "Zone 5, Barangay Malipayon",
    age: 21,
    gender: "Female",
    term: "2022-2025",
    achievements: ["Organized Mental Health Awareness Campaign", "Coordinated Youth Sports League", "Led Environmental Clean-up Drives"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=MariaSantos&backgroundColor=ffdfbf"
  },
  {
    id: 3,
    firstName: "Pedro",
    lastName: "Reyes",
    position: "SK Councilor",
    platform: "Developing arts and cultural programs to preserve local heritage while providing creative outlets for young people.",
    role: "USER",
    email: "pedro.reyes@skyber.ph",
    phoneNumber: "09123456787",
    address: "Street 4, Barangay San Jose",
    age: 22,
    gender: "Male",
    term: "2022-2025",
    achievements: ["Established Youth Cultural Center", "Created Arts Scholarship Program", "Revived Traditional Dance Competition"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=PedroReyes&backgroundColor=c0aede"
  },
  {
    id: 4,
    firstName: "Sofia",
    lastName: "Bautista",
    position: "SK Councilor",
    platform: "Implementing technology and innovation programs to prepare the youth for the digital economy and future careers.",
    role: "USER",
    email: "sofia.bautista@skyber.ph",
    phoneNumber: "09123456786",
    address: "Block 5 Lot 2, Barangay Sta. Rosa",
    age: 20,
    gender: "Female",
    term: "2022-2025",
    achievements: ["Founded Youth Tech Hub", "Conducted Digital Literacy Workshops", "Partnered with Tech Companies for Internships"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=SofiaBautista&backgroundColor=d1d4f9"
  },
  {
    id: 5,
    firstName: "Miguel",
    lastName: "Cruz",
    position: "SK Councilor",
    platform: "Creating environmental sustainability initiatives led by and for young people to address climate change locally.",
    role: "USER",
    email: "miguel.cruz@skyber.ph",
    phoneNumber: "09123456785",
    address: "Phase 2, Barangay New Town",
    age: 21,
    gender: "Male",
    term: "2019-2022",
    achievements: ["Initiated Tree Planting Project", "Established Waste Segregation System", "Created Youth Environmental Patrol"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=MiguelCruz&backgroundColor=c8e6c9"
  },
  {
    id: 6,
    firstName: "Angela",
    lastName: "Mendoza",
    position: "SK Chairman",
    platform: "Developing entrepreneurship programs to help youth create their own opportunities and contribute to local economy.",
    role: "USER",
    email: "angela.mendoza@skyber.ph",
    phoneNumber: "09123456784",
    address: "Lot 7, Barangay San Miguel",
    age: 23,
    gender: "Female",
    term: "2019-2022",
    achievements: ["Launched Youth Entrepreneurship Fair", "Secured Funding for Startup Grants", "Mentored 30+ Young Business Owners"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=AngelaMendoza&backgroundColor=ffcdd2"
  },
  {
    id: 7,
    firstName: "Gabriel",
    lastName: "Tan",
    position: "SK Councilor",
    platform: "Focusing on sports development and creating more opportunities for physical activities and healthy competition.",
    role: "USER",
    email: "gabriel.tan@skyber.ph",
    phoneNumber: "09123456783",
    address: "Unit 3, Barangay Rizal",
    age: 22,
    gender: "Male",
    term: "2019-2022",
    achievements: ["Renovated Barangay Basketball Court", "Established Inter-Barangay Sports League", "Secured Sports Equipment Donations"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=GabrielTan&backgroundColor=ffe0b2"
  },
  {
    id: 8,
    firstName: "Isabella",
    lastName: "Reyes",
    position: "SK Councilor",
    platform: "Creating community service opportunities for youth to engage in meaningful work and develop leadership skills.",
    role: "USER",
    email: "isabella.reyes@skyber.ph",
    phoneNumber: "09123456782",
    address: "Area 2, Barangay Del Pilar",
    age: 21,
    gender: "Female",
    term: "2016-2019",
    achievements: ["Established Youth Volunteer Corps", "Coordinated Disaster Response Training", "Led Senior Citizen Support Program"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=IsabellaReyes&backgroundColor=dcedc8"
  },
  {
    id: 9,
    firstName: "Rafael",
    lastName: "Lim",
    position: "SK Chairman",
    platform: "Building bridges between youth and senior citizens through intergenerational programs and activities.",
    role: "USER",
    email: "rafael.lim@skyber.ph",
    phoneNumber: "09123456781",
    address: "Block 12, Barangay Emerald",
    age: 24,
    gender: "Male",
    term: "2016-2019",
    achievements: ["Created Youth-Senior Partnership Program", "Established Barangay Digital Archive", "Led Community Historical Project"],
    profileImage: "https://api.dicebear.com/7.x/personas/svg?seed=RafaelLim&backgroundColor=f8bbd0"
  }
];

// Get unique term years from the data
const getUniqueTerms = () => {
  const terms = skOfficials.map(official => official.term);
  return ['All', ...new Set(terms)];
};

const SkProfile = () => {
    const { showLoading, hideLoading } = useLoading();
    useEffect(() => {
        // Show loading with custom message
        showLoading("Loading your content... (✿◠‿◠)", "/your-logo.png");
        
        // Fetch your data or perform operations
        setTimeout(() => {
          hideLoading(); // Hide when done
        }, 1500);
      }, []);
  // State to track which cards are flipped
  const [flippedCards, setFlippedCards] = useState({});
  // State to track selected term year
  const [selectedTerm, setSelectedTerm] = useState('All');

  // Function to handle card flip
  const handleCardClick = (id, event) => {
    setFlippedCards(prev => ({
      ...prev,
      [id]: !prev[id]
    }));
  };

  // Function to get position color
  const getPositionColor = (position) => {
    switch(position) {
      case 'SK Chairman': return { from: 'gold', to: 'orange' };
      case 'SK Councilor': return { from: 'blue', to: 'cyan' };
      default: return { from: 'gray', to: 'blue' };
    }
  };

  // Function to get role color
  const getRoleBadge = (role) => {
    switch(role) {
      case 'ADMIN': return { color: 'red', label: 'Admin' };
      case 'USER': return { color: 'blue', label: 'User' };
      default: return { color: 'gray', label: role };
    }
  };

  // Filter officials based on selected term
  const filteredOfficials = selectedTerm === 'All' 
    ? skOfficials 
    : skOfficials.filter(official => official.term === selectedTerm);

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
            Sangguniang Kabataan Officials
          </Title>
          <Text color="white" className="mt-2 max-w-2xl">
            Meet our elected SK officials who are working hard to serve the youth in our community! (｡•̀ᴗ-)✧
          </Text>
        </header>
      </div>

      {/* Main content section with grid layout */}
      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-7xl mx-auto">
          {/* Term filter section */}
          <Paper shadow="md" radius="lg" className="p-6 mb-8">
            <Group position="apart" className="mb-2">
              <Title order={4} className="flex items-center">
                <IconCalendar size={22} className="mr-2 text-blue-600" />
                SK Term Years
              </Title>
            </Group>
            <Divider className="mb-4" />
            <Tabs value={selectedTerm} onChange={setSelectedTerm} radius="xl" variant="pills" color="blue">
              <Tabs.List>
                {getUniqueTerms().map(term => (
                  <Tabs.Tab 
                    key={term} 
                    value={term}
                    className="transition-all hover:bg-blue-50"
                  >
                    {term === 'All' ? 'All Terms' : term}
                  </Tabs.Tab>
                ))}
              </Tabs.List>
            </Tabs>
          </Paper>

          {/* Display message when no officials match the filter */}
          {filteredOfficials.length === 0 ? (
            <Paper shadow="md" radius="lg" className="p-10 text-center">
              <Title order={3} className="mb-2">No SK officials found for this term (´。＿。｀)</Title>
              <Text>Try selecting a different term year!</Text>
            </Paper>
          ) : (
            <>
              {/* Term header when a specific term is selected */}
              {selectedTerm !== 'All' && (
                <div className="mb-6 text-center">
                  <Badge 
                    size="xl" 
                    radius="md"
                    variant="gradient"
                    gradient={{ from: 'indigo', to: 'cyan' }}
                    className="px-8 py-2"
                  >
                    SK Officials for Term: {selectedTerm}
                  </Badge>
                </div>
              )}

              {/* Officials grid */}
              <Grid gutter="lg">
                {filteredOfficials.map((official) => (
                  <Grid.Col key={official.id} span={{ base: 12, sm: 6, md: 4, lg: 3 }}>
                    {/* Card with flip effect */}
                    <div 
                      className={`flip-card ${flippedCards[official.id] ? 'flipped' : ''}`}
                      style={{ height: '420px' }}
                      onClick={(e) => handleCardClick(official.id, e)}
                    >
                      <div className="flip-card-inner">
                        {/* Front of Card */}
                        <Paper 
                          radius="md" 
                          shadow="md"
                          className="flip-card-front flex flex-col items-center justify-start p-6"
                        >
                          <div className="flex justify-center items-center w-full">
                            <Avatar
                              src={official.profileImage}
                              alt={`${official.firstName} ${official.lastName} avatar`}
                              size={160}
                              radius={80}
                              className="border-4 border-blue-300 mb-4 mx-auto"
                            />
                          </div>
                          <Title order={3} className="text-blue-700 mb-2">
                            {official.firstName} {official.lastName}
                          </Title>
                          
                          <Badge 
                            variant="gradient" 
                            gradient={getPositionColor(official.position)}
                            size="lg"
                            className="mb-3"
                          >
                            {official.position}
                          </Badge>
                          
                          <Group spacing="xs" className="mb-2">
                            {/* <Badge 
                              color={getRoleBadge(official.role).color}
                              size="sm"
                            >
                              {getRoleBadge(official.role).label}
                            </Badge>
                            
                            <Badge 
                              color="grape" 
                              variant="light"
                              size="sm"
                            >
                              {official.term}
                            </Badge> */}
                          </Group>
                          
                          {/* <Text size="sm" color="dimmed" className="mt-2 mb-2" lineClamp={3}>
                            {official.platform}
                          </Text> */}

                          <div className="mt-auto">
                            <ActionIcon 
                              variant="light" 
                              color="blue" 
                              aria-label="Flip card"
                              radius="xl"
                              size="lg"
                              className="mt-4 animate-pulse"
                            >
                              <IconFlipVertical size={20} />
                            </ActionIcon>
                            <Text size="xs" color="dimmed" className="mt-1">Tap for more info</Text>
                          </div>
                        </Paper>
                        
                        {/* Back of Card */}
                        <Paper 
                          radius="md" 
                          shadow="md"
                          className="flip-card-back p-6 overflow-auto"
                        >
                          <Title order={4} className="text-blue-700 mb-3">
                            {official.firstName} {official.lastName}
                          </Title>
                          
                          <div className="space-y-2 mb-4">
                            <Group>
                              <IconMail size={16} className="text-blue-500" />
                              <Text size="sm">{official.email}</Text>
                            </Group>
                            <Group>
                              <IconPhone size={16} className="text-blue-500" />
                              <Text size="sm">{official.phoneNumber}</Text>
                            </Group>
                            <Group>
                              <IconMapPin size={16} className="text-blue-500" />
                              <Text size="sm">{official.address}</Text>
                            </Group>
                          </div>
                          
                          <Title order={5} className="mt-4 mb-2">Platform</Title>
                          <Text size="sm" className="mb-4">
                            {official.platform}
                          </Text>
                          
                          <Title order={5} className="mb-2">Achievements</Title>
                          <div className="mb-4">
                            {official.achievements.map((achievement, idx) => (
                              <div key={idx} className="flex items-start mb-1">
                                <div className="min-w-[6px] h-[6px] bg-blue-500 rounded-full mt-[0.5rem] mr-2"></div>
                                <Text size="sm">{achievement}</Text>
                              </div>
                            ))}
                          </div>
                          
                          <Button
                            variant="gradient"
                            gradient={{ from: 'blue', to: 'cyan' }}
                            size="sm"
                            radius="xl"
                            leftSection={<IconBrandFacebook size={16} />}
                            className="mt-2"
                          >
                            Connect
                          </Button>
                          
                          <Text size="xs" color="dimmed" className="mt-4 text-center">
                            Tap to flip back
                          </Text>
                        </Paper>
                      </div>
                    </div>
                  </Grid.Col>
                ))}
              </Grid>
            </>
          )}
          
          <div className="mt-8 text-center text-xs text-gray-400">
            Working together for a better community for our youth! (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
          </div>
        </div>
      </div>

      {/* CSS for flip card animation */}
      <style jsx>{`
        .flip-card {
          background-color: transparent;
          perspective: 1000px;
          cursor: pointer;
        }

        .flip-card-inner {
          position: relative;
          width: 100%;
          height: 100%;
          text-align: center;
          transition: transform 0.8s;
          transform-style: preserve-3d;
        }

        .flip-card.flipped .flip-card-inner {
          transform: rotateY(180deg);
        }

        .flip-card-front, .flip-card-back {
          position: absolute;
          width: 100%;
          height: 100%;
          -webkit-backface-visibility: hidden; /* Safari */
          backface-visibility: hidden;
        }

        .flip-card-back {
          transform: rotateY(180deg);
        }
      `}</style>
    </>
  );
};

export default SkProfile;