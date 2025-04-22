import React from 'react';
import Logo from '../assets/skyber.svg'; // Adjust the path as necessary

const LoadingScreen = ({ 
  logoSrc = Logo, 
  message = "Loading... Please Wait (๑˃ᴗ˂)ﻭ",
  bgClass = "bg-gradient-to-b from-blue-50 to-pink-50"
}) => {
  return (
    <div className={`fixed inset-0 z-50 flex flex-col justify-center items-center ${bgClass}`}>
      <div className="flipping-logo-container">
        <div className="flipping-logo">
          <div className="flipping-logo-inner">
            <div className="flipping-logo-front">
              <img 
                src={Logo} 
                alt="SKyber Logo" 
                className="w-24 h-24"
              />
            </div>
            <div className="flipping-logo-back">
              <img 
                src={Logo}
                alt="SKyber Logo" 
                className="w-24 h-24 transform rotate-180"
              />
            </div>
          </div>
        </div>
        <div className="mt-4 text-blue-600 font-medium">
          {message}
        </div>
      </div>

      <style jsx>{`
        .flipping-logo {
          perspective: 1000px;
          width: 100px;
          height: 100px;
        }

        .flipping-logo-inner {
          position: relative;
          width: 100%;
          height: 100%;
          text-align: center;
          transform-style: preserve-3d;
          animation: flipLogo 1.8s infinite;
        }

        .flipping-logo-front, .flipping-logo-back {
          position: absolute;
          width: 100%;
          height: 100%;
          -webkit-backface-visibility: hidden;
          backface-visibility: hidden;
          display: flex;
          justify-content: center;
          align-items: center;
          border-radius: 50%;
          box-shadow: 0 4px 15px rgba(0, 0, 0, 0.1);
        }

        .flipping-logo-back {
          transform: rotateY(180deg);
        }

        @keyframes flipLogo {
          0% { transform: rotateY(0); }
          50% { transform: rotateY(180deg); }
          100% { transform: rotateY(360deg); }
        }
      `}</style>
    </div>
  );
};

export default LoadingScreen;