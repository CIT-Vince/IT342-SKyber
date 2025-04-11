import React, { useEffect, useRef } from 'react';

const ParticlesBackground = () => {
  const particlesRef = useRef(null);
  
  useEffect(() => {
    // Load particles.js
    if (typeof window !== 'undefined' && particlesRef.current) {
      const loadParticles = async () => {
        try {
          // Import the library
          const particlesJS = (await import('particles.js')).default;
          
          // Initialize with configuration
          window.particlesJS('particles-js', {
            particles: {
              number: {
                value: 80,
                density: {
                  enable: true,
                  value_area: 800
                }
              },
              color: {
                value: "#ffffff"
              },
              shape: {
                type: "circle",
                stroke: {
                  width: 0,
                  color: "#000000"
                },
              },
              opacity: {
                value: 0.5,
                random: false,
              },
              size: {
                value: 3,
                random: true,
              },
              line_linked: {
                enable: true,
                distance: 150,
                color: "#ffffff",
                opacity: 0.4,
                width: 1
              },
              move: {
                enable: true,
                speed: 2,
                direction: "none",
                random: false,
                straight: false,
                out_mode: "out",
                bounce: false,
              }
            },
            interactivity: {
              detect_on: "canvas",
              events: {
                onhover: {
                  enable: true,
                  mode: "grab"
                },
                onclick: {
                  enable: true,
                  mode: "push"
                },
                resize: true
              },
              modes: {
                grab: {
                  distance: 140,
                  line_linked: {
                    opacity: 1
                  }
                },
                push: {
                  particles_nb: 4
                }
              }
            },
            retina_detect: true
          });
        } catch (error) {
          console.error("Error initializing particles:", error);
        }
      };
      
      loadParticles();
    }
    
    // Cleanup function
    return () => {
      // If particles.js has a destroy method, call it here
      if (window.pJSDom && window.pJSDom.length) {
        window.pJSDom[0].pJS.fn.vendors.destroypJS();
      }
    };
  }, []);

  return (
    <div 
      id="particles-js"
      ref={particlesRef}
      style={{
        position: "absolute",
        top: 0,
        left: 0,
        width: "100%",
        height: "100%",
        background: "linear-gradient(180deg, #0134AA 0%, #001544 100%)",
        zIndex: 0
      }}
    />
  );
};

export default ParticlesBackground;