import React from 'react';
import communityImage from '../assets/communityLogo.png';
import WaveDivider from '../components/WaveDivider';
import fb from '../assets/icons/fb.png';
import tiktok from '../assets/icons/tiktok.png';
import x from '../assets/icons/x.png';
import Navbar from '../components/Navbar';
import { Link } from 'react-router-dom';
import { FaApple, FaGooglePlay } from 'react-icons/fa'; 
import Atropos from 'atropos/react';
import 'atropos/css';
import { useAuth } from '../contexts/AuthContext';

const Home = () => { 
  const { currentUser } = useAuth(); 
  return (
    <>
      <Navbar/>
      
      {/* Hero Section */}
      <section className="content w-full min-h-screen flex items-center justify-center px-2 py-6">
        <div className="max-w-7xl w-full flex flex-col md:flex-row items-center justify-between gap-10">
          {/* Left content */}
          <div className="flex-1 text-center md:text-left">
            <h1 className="text-4xl md:text-5xl font-bold leading-tight text-white mb-6">
              Empowering <span className="text-red-600">Communities</span><br />
              <span className="text-blue-600">Securing</span> the <span className="text-yellow-500">Future.</span>
            </h1>
            <p className="text-gray-600 max-w-md text-white mb-6">
              Transform your community with SKyber — an AI-powered security platform bringing safety and efficiency to neighborhoods and campuses. From visitor management to threat detection, SKyber delivers comprehensive protection with intuitive simplicity.
            </p>
            <div className="flex items-center gap-4 mb-6 justify-center md:justify-start">
              {/* Social icons */}
            </div>
            <Link 
              to={currentUser ? "/announcements" : "/register"}
              className="bg-gradient-to-r from-blue-500 to-cyan-400 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition w-full md:w-auto"
            >
              {currentUser ? "View Announcements" : "Get Started"}
            </Link>
          </div>
          
          {/* Right image with Atropos effect */}
          <div className="flex-1 flex justify-center md:justify-end">
            <Atropos 
              className="atropos-banner" 
              highlight={false} 
              shadow={false}
              shadowScale={1.05}
              rotateXMax={15}
              rotateYMax={15}
              stretchZ={10}
            >
              <div data-atropos-offset="-5" className="absolute inset-0 rounded-lg" />
              
              <img
                src={communityImage}
                alt="Community"
                className="w-full max-w-3xl h-auto object-contain z-10!"
                data-atropos-offset="0"
              />
            </Atropos>
          </div>
        </div>
        <div className="absolute w-full h-full overflow-hidden" style={{ bottom: 0, zIndex: -1 }}>
        <WaveDivider/>
      </div>
      </section>
      {/* NEW App Download Section */}
      <section className="w-full py-20 bg-gradient-to-br from-blue-900 to-indigo-900 relative overflow-hidden">
        {/* Background Decorations */}
        <div className="absolute top-0 left-0 w-64 h-64 bg-blue-500 rounded-full opacity-10 -translate-x-1/2 -translate-y-1/2"></div>
        <div className="absolute bottom-0 right-0 w-80 h-80 bg-cyan-400 rounded-full opacity-10 translate-x-1/3 translate-y-1/3"></div>
        
        <div className="max-w-7xl mx-auto px-4 relative z-10">
          <div className="text-center mb-12">
            <h2 className="text-3xl md:text-4xl font-bold text-white mb-4">
              Take SKyber <span className="text-cyan-400">Everywhere</span> You Go
            </h2>
            <p className="text-blue-100 max-w-2xl mx-auto">
              Stay connected with your community, access resources, and participate in events right from your mobile device. Download the SKyber app now!
            </p>
          </div>
          
          <div className="flex flex-col-reverse md:flex-row items-center justify-between gap-10">
            {/* Left: App Store Buttons */}
            <div className="md:w-1/2 text-center md:text-left">
              <h3 className="text-2xl font-semibold text-white mb-6">Download Our App</h3>
              <p className="text-blue-100 mb-8">
                Get instant updates, join community projects, report concerns, and more with our intuitive mobile app. Available for iOS and Android devices.
              </p>
              
              <div className="flex flex-col sm:flex-row gap-4 justify-center md:justify-start">
                <a 
                  href="#" 
                  className="flex items-center justify-center gap-2 bg-black text-white px-6 py-3 rounded-xl hover:bg-gray-900 transition"
                >
                  <FaApple size={24} />
                  <div className="text-left">
                    <div className="text-xs">Download on the</div>
                    <div className="font-semibold">App Store</div>
                  </div>
                </a>
                
                <a 
                  href="#" 
                  className="flex items-center justify-center gap-2 bg-black text-white px-6 py-3 rounded-xl hover:bg-gray-900 transition"
                >
                  <FaGooglePlay size={20} />
                  <div className="text-left">
                    <div className="text-xs">GET IT ON</div>
                    <div className="font-semibold">Google Play</div>
                  </div>
                </a>
              </div>
              
              <div className="mt-8 flex items-center justify-center md:justify-start gap-6">
                <div className="flex items-center">
                  <div className="flex">
                    {[1, 2, 3, 4, 5].map(star => (
                      <svg key={star} className="w-5 h-5 text-yellow-400" fill="currentColor" viewBox="0 0 20 20">
                        <path d="M9.049 2.927c.3-.921 1.603-.921 1.902 0l1.07 3.292a1 1 0 00.95.69h3.462c.969 0 1.371 1.24.588 1.81l-2.8 2.034a1 1 0 00-.364 1.118l1.07 3.292c.3.921-.755 1.688-1.54 1.118l-2.8-2.034a1 1 0 00-1.175 0l-2.8 2.034c-.784.57-1.838-.197-1.539-1.118l1.07-3.292a1 1 0 00-.364-1.118L2.98 8.72c-.783-.57-.38-1.81.588-1.81h3.461a1 1 0 00.951-.69l1.07-3.292z" />
                      </svg>
                    ))}
                  </div>
                  <span className="ml-2 text-white text-sm">4.9/5</span>
                </div>
                <div className="text-white text-sm">10K+ Downloads</div>
              </div>
            </div>
            
            {/* Right: Phone Mockup */}
            <div className="md:w-1/2 flex justify-center">
              <Atropos 
                className="atropos-app" 
                highlight={false}
                shadow={true}
                shadowScale={1.05}
                rotateXMax={10}
                rotateYMax={10}
              >
                <div className="relative">
                  {/* Phone frame */}
                  <div 
                    data-atropos-offset="3"
                    className="w-64 h-[500px] bg-black rounded-[36px] p-3 relative overflow-hidden border-8 border-gray-900"
                  >
                    {/* Screen content */}
                    <div 
                      data-atropos-offset="5" 
                      className="w-full h-full bg-gradient-to-br from-blue-500 to-cyan-400 rounded-[24px] overflow-hidden"
                    >
                      {/* App UI elements - simplified for example */}
                      <div className="p-4">
                        <div className="flex justify-between items-center mb-6">
                          <div className="text-white font-bold">SKyber</div>
                          <div className="w-8 h-8 rounded-full bg-white/20"></div>
                        </div>
                        
                        <div className="bg-white/20 h-32 w-full rounded-lg mb-4"></div>
                        
                        <div className="space-y-3">
                          <div className="bg-white/10 h-12 w-full rounded-lg"></div>
                          <div className="bg-white/10 h-12 w-full rounded-lg"></div>
                          <div className="bg-white/10 h-12 w-full rounded-lg"></div>
                        </div>
                      </div>
                    </div>
                    
                    {/* Home button/notch */}
                    <div 
                      data-atropos-offset="8" 
                      className="absolute bottom-1 left-1/2 -translate-x-1/2 w-1/3 h-1 bg-gray-700 rounded-full"
                    ></div>
                  </div>
                  
                  {/* Decorative elements */}
                  <div 
                    data-atropos-offset="-3"
                    className="absolute -top-6 -right-6 w-24 h-24 bg-cyan-500 rounded-full opacity-30 blur-xl"
                  ></div>
                  <div 
                    data-atropos-offset="-5"
                    className="absolute -bottom-8 -left-8 w-28 h-28 bg-blue-500 rounded-full opacity-30 blur-xl"
                  ></div>
                </div>
              </Atropos>
            </div>
          </div>
          
          {/* App Features */}
          <div className="grid grid-cols-1 sm:grid-cols-2 md:grid-cols-3 gap-8 mt-20">
            {[
              { title: "Community Updates", desc: "Get real-time notifications about events and announcements" },
              { title: "Project Tracking", desc: "Follow ongoing community projects and volunteer opportunities" },
              { title: "Resource Directory", desc: "Access important resources and contact information anywhere" }
            ].map((feature, index) => (
              <div key={index} className="bg-white/5 backdrop-blur p-6 rounded-xl">
                <div className="w-12 h-12 flex items-center justify-center bg-blue-500/20 rounded-lg mb-4">
                  <span className="text-blue-300 font-bold text-xl">{index + 1}</span>
                </div>
                <h3 className="text-xl font-semibold text-white mb-2">{feature.title}</h3>
                <p className="text-blue-100">{feature.desc}</p>
              </div>
            ))}
          </div>
        </div>
      </section>
    </>
  );
};

export default Home;