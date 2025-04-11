import React from 'react';
import communityImage from '../assets/communityLogo.png'; // Adjust the path as necessary
import WaveDivider from '../components/WaveDivider';
import fb from '../assets/icons/fb.png';
import tiktok from '../assets/icons/tiktok.png';
import x from '../assets/icons/x.png';
const Home = () => {
  return (
    <section className="content w-full min-h-screen flex items-center justify-center  px-2 py-2">
      <div className="max-w-7xl w-full flex flex-col md:flex-row items-center justify-between gap-10">
        {/* Left content */}
        <div className="flex-1 text-center md:text-left">
          <h1 className="text-4xl md:text-5xl font-bold leading-tight text-white mb-6">
            Empowering{' '}
            <span className="text-red-600">Communities</span><br />
            <span className="text-blue-600">Securing</span> the{' '}
            <span className="text-yellow-500">Future.</span>
          </h1>

          <p className="text-gray-600 max-w-md text-white mb-6">
            Lorem ipsum dolor sit amet, consectetur adipiscing elit. Maecenas sit
            venenatis aliquet nunc nam scelerisque. Proin congue viverra risus
            placerat augue odio cras neque. Felis netus tincidunt sed hac urna.
          </p>

          {/* Social Icons */}
          <div className="flex items-center gap-4 mb-6 justify-center md:justify-start">
            <a href="#"><img src={fb} alt="Facebook" className="w-6 h-6" /></a>
            <a href="#"><img src={x} alt="X" className="w-6 h-6" /></a>
            <a href="#"><img src={tiktok} alt="TikTok" className="w-6 h-6" /></a>
          </div>

          {/* Register Button */}
          <button className="bg-gradient-to-r from-blue-500 to-cyan-400 text-white px-6 py-2 rounded-full shadow hover:scale-105 transition">
            Register
          </button>
        </div>

        {/* Right image */}
        <div className="flex-1 flex justify-center md:justify-end">
          <img
            src={communityImage}
            alt="Community"
            className="w-[80%] max-w-md object-contain"
          />
        </div>
      </div>
    </section>
  );
};

export default Home;