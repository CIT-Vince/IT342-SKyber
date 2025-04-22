import React, { useState } from 'react';
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
  Divider
} from '@mantine/core';
import { IconSearch, IconMail, IconExternalLink, IconBookmark, IconHeart, IconShare } from '@tabler/icons-react';
import Navbar from '../../components/Navbar';

const Scholarship = () => {
  // Sample scholarship data (≧◡≦)
  const [scholarships, setScholarships] = useState([
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

  const [searchQuery, setSearchQuery] = useState('');
  const [activeTab, setActiveTab] = useState('all');

  // Filter scholarships based on search and category (*≧ω≦)
  const filteredScholarships = scholarships.filter(scholarship => {
    const matchesSearch = scholarship.title.toLowerCase().includes(searchQuery.toLowerCase()) || 
                         scholarship.description.toLowerCase().includes(searchQuery.toLowerCase());
    const matchesCategory = activeTab === 'all' || scholarship.category === activeTab;
    
    return matchesSearch && matchesCategory;
  });

  // Toggle save/bookmark scholarship (♡˙︶˙♡)
  const toggleSaveScholarship = (id) => {
    setScholarships(scholarships.map(scholarship => 
      scholarship.id === id 
        ? { ...scholarship, saved: !scholarship.saved } 
        : scholarship
    ));
  };

  return (
    <>
      <div
        className="w-full h-auto"
        style={{
          background: 'linear-gradient(180deg, #0134AA 0%, #001544 100%)',
        }}
      >
      <Navbar />
      <header className="text-left py-10 pl-10">
            <Title className="text-5xl! font-bold text-white! bg-clip-text text-transparent">
              Scholarships & Opportunities
            </Title>
            <Text className="text-white! mt-2 max-w-2xl mx-auto">
              Find financial support for your studies and make your dreams come true with these amazing scholarship opportunities! (ﾉ◕ヮ◕)ﾉ*:･ﾟ✧
            </Text>
        </header>
        
      </div>

      <div className="min-h-screen pt-5 pb-10 px-4 relative bg-gradient-to-br from-blue-50 to-pink-50">
        <div className="max-w-7xl mx-auto">
          

          {/* Search and filter section (⌒‿⌒) */}
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
            </Grid>
          </Paper>

          {/* Scholarship cards (◕‿◕✿) */}
          {filteredScholarships.length > 0 ? (
            <Grid gutter="lg">
              {filteredScholarships.map((scholarship) => (
                <Grid.Col key={scholarship.id} span={{ base: 12, sm: 6, lg: 4 }}>
                  <Card shadow="md" radius="md" className="h-full flex flex-col transition-transform duration-300 hover:scale-[1.02]">
                    <Card.Section className="p-4 bg-gradient-to-r from-sky-400 to-slate-50">
                      <div className="flex justify-between items-center">
                        <Badge 
                          variant="gradient" 
                          gradient={{ from: scholarship.category === 'public' ? 'blue' : 'pink', to: 'cyan' }}
                          radius="md"
                        >
                          {scholarship.category.charAt(0).toUpperCase() + scholarship.category.slice(1)}
                        </Badge>
                        <ActionIcon 
                          variant="subtle" 
                          color={scholarship.saved ? "pink" : "gray"}
                          onClick={() => toggleSaveScholarship(scholarship.id)}
                        >
                        </ActionIcon>
                      </div>
                    </Card.Section>

                    <div className="p-4 flex-grow">
                      <Title order={3} className="mb-2">
                        {scholarship.title}
                      </Title>
                      <Text color="dimmed" size="sm" className="mb-3">
                        {scholarship.description}
                      </Text>

                        {/* Mayta iadd ni Vince-kun(✿◠‿◠) */}
                      {/* <Group className="mb-2">
                        <Badge variant="dot" color="green">Amount: {scholarship.amount}</Badge>
                        <Badge variant="dot" color="red">Deadline: {scholarship.deadline}</Badge>
                      </Group> */}
                      {/* atay kaluod na nako oi AHAHAAAAHHAH nabuang nakoooo ga kawaii2 nakos ako AI */}

                      {/* RUBY CHANNNNNN HAIIIIIIIIIIIIIIIIIII */}

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
    </>
  );
};

export default Scholarship;