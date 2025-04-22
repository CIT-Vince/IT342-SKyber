import React from 'react';

const WaveDivider = () => {
  return (
    <div className="absolute bottom-0 left-0 w-full overflow-hidden leading-[0]">
      <svg
        className="relative block w-[160%] h-[500px] rotate-y-180"
        viewBox="0 0 1200 120"
        preserveAspectRatio="none"
        xmlns="http://www.w3.org/2000/svg"
      >
        <path
          d="M985.66,92.83C906.67,72,823.78,31,743.84,14.19
             c-82.26-17.34-168.06-16.33-250.45.39
             -57.84,11.73-114,31.07-172,41.86
             A600.21,600.21,0,0,1,0,27.35V120H1200
             V95.8C1132.19,118.92,1055.71,111.31,
             985.66,92.83Z"
          className="fill-white"
        ></path>
      </svg>
    </div>
  );
};

export default WaveDivider;
