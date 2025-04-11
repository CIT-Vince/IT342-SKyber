import React, { useEffect } from 'react';

const ParticlesComponent = () => {
  useEffect(() => {
    if (window.particlesJS) {
      // Load the particles.json configuration
      window.particlesJS.load('particles-js', '/particles.json', function () {
        console.log('particles.js config loaded');
      });
    } else {
      console.error('particlesJS is not available on the window object');
    }
  }, []);

  return (
    <div
      id="particles-js"
      style={{
        position: 'absolute',
        top: 0,
        left: 0,
        width: '100%',
        height: '100%',
        zIndex: 0,
      }}
    />
  );
};

export default ParticlesComponent;