import React, { useState } from 'react';
import skyber from '../../assets/skyber.svg';
import { useNavigate, Link } from 'react-router-dom';
import logo from '../../assets/communityLogo.png';
import { 
  Input, 
  Radio, 
  CloseButton, 
  PasswordInput, 
  Group, 
  Checkbox, 
  Button, 
  Select ,
  LoadingOverlay
} from '@mantine/core'; // AHHHH KAPOYA
import { IconAt, IconEye, IconEyeOff } from '@tabler/icons-react';
import { showNotification } from '@mantine/notifications';
import { createUserWithEmailAndPassword } from 'firebase/auth';
import { auth } from '../../firebase/firebase';
import { getDatabase, ref, set } from "firebase/database";

const RegisterFull = () => {
  
  const [firstName, setFirstName] = useState('');
  const [lastName, setLastName] = useState('');
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirmPassword, setConfirmPassword] = useState('');
  const [gender, setGender] = useState('');
  const [month, setMonth] = useState('');
  const [day, setDay] = useState('');
  const [year, setYear] = useState('');
  const [rememberMe, setRememberMe] = useState(false);

  
  const [loading, setLoading] = useState(false);
  const [phoneNumber, setPhoneNumber] = useState('');
  const [address, setAddress] = useState('');
  const navigate = useNavigate();
  
  // Form validation function
  const validateForm = () => {
    // Email validation
    const emailRegex = /^[^\s@]+@[^\s@]+\.[^\s@]+$/;
    if (!emailRegex.test(email)) {
      showNotification({
        title: 'Invalid Email',
        message: 'Please enter a valid email address',
        color: 'red'
      });
      return false;
    }
    
    // Password validation
    if (password.length < 6) {
      showNotification({
        title: 'Weak Password',
        message: 'Password must be at least 6 characters long',
        color: 'red'
      });
      return false;
    }
    
    // Password match check
    if (password !== confirmPassword) {
      showNotification({
        title: 'Password Mismatch',
        message: 'Passwords do not match',
        color: 'red'
      });
      return false;
    }
    
    // Name validation
    if (!firstName.trim() || !lastName.trim()) {
      showNotification({
        title: 'Missing Information',
        message: 'Please enter your first and last name',
        color: 'red'
      });
      return false;
    }
    
    return true;
  };
  
  // Form submission handler
  const handleSubmit = async (e) => {
    e.preventDefault();
    
    if (!validateForm()) {
      return;
    }
    
    try {
      setLoading(true);
      
      // Step 1: Create user account in Firebase Auth
      const userCredential = await createUserWithEmailAndPassword(
        auth, 
        email, 
        password
      );
      
      const user = userCredential.user;
      
      // Format birthdate
      let birthdate = null;
      if (year && month && day) {
        birthdate = `${year}-${month.padStart(2, '0')}-${day.padStart(2, '0')}`;
      }
      
      // Calculate age from birthdate
      let age = '';
      if (birthdate) {
        const birthDate = new Date(birthdate);
        const today = new Date();
        age = String(today.getFullYear() - birthDate.getFullYear());
      }
      
      // Step 2: Get Firebase ID token
      const idToken = await user.getIdToken();
      
      // Step 3: Send user details to Spring Boot backend
      const response = await fetch('http://localhost:8080/api/users/register', {
        method: 'POST',
        headers: {
          'Content-Type': 'application/json',
          'Authorization': `Bearer ${idToken}`
        },
        body: JSON.stringify({
          uid: user.uid,
          firstName: firstName,
          lastName: lastName,
          email: email,
          birthdate: birthdate,
          gender: gender || '',
          age: age,
          phoneNumber: phoneNumber || '',
          address: address || '',
          role: 'USER'
        })
      });
      
      if (response.ok) {
        // Also store profile in Firebase Realtime Database
        const database = getDatabase();
        console.log("Database reference created, attempting to write user data...");

        await set(ref(database, 'users/' + user.uid), {
          firstName: firstName,
          lastName: lastName,
          email: email,
          photoURL: null, // User can add this later
          birthdate: birthdate,
          gender: gender || '',
          phoneNumber: phoneNumber || '',
          address: address || '',
          role: 'USER', // Add this!
          lastUpdated: new Date().toISOString()
        });
        
      console.log("Successfully wrote user data to Firebase Realtime Database!");
    
        showNotification({
          title: 'Registration Successful',
          message: 'Your account has been created! You can now log in.',
          color: 'green'
        });
        
        navigate('/login');
      } else {
        // If backend registration fails, delete the Firebase user
        await user.delete();
        throw new Error('Failed to register user data');
      }
      
    } catch (dberror) {
      console.error("Firebase Realtime Database error:", dberror);
      console.error("Registration error:", dberror);
      
      let errorMessage = 'Failed to register. Please try again.';
      
      if (dberror.code === 'auth/email-already-in-use') {
        errorMessage = 'This email is already registered.';
      } else if (dberror.code === 'auth/invalid-email') {
        errorMessage = 'Invalid email address.';
      } else if (dberror.code === 'auth/weak-password') {
        errorMessage = 'Password is too weak.';
      }
      
      showNotification({
        title: 'Registration Failed',
        message: errorMessage,
        color: 'red'
      });
    } finally {
      setLoading(false);
    }
  };

  // Generate years for dropdown
  const years = Array.from({ length: 100 }, (_, i) => {
    const year = new Date().getFullYear() - i;
    return { value: year.toString(), label: year.toString() };
  });

  // Generate days for dropdown
  const days = Array.from({ length: 31 }, (_, i) => {
    const day = i + 1;
    return { value: day.toString(), label: day.toString() };
  });

  // Months for dropdown
  const months = [
    { value: '1', label: 'January' },
    { value: '2', label: 'February' },
    { value: '3', label: 'March' },
    { value: '4', label: 'April' },
    { value: '5', label: 'May' },
    { value: '6', label: 'June' },
    { value: '7', label: 'July' },
    { value: '8', label: 'August' },
    { value: '9', label: 'September' },
    { value: '10', label: 'October' },
    { value: '11', label: 'November' },
    { value: '12', label: 'December' },
  ];

  return (
    <div className="content flex min-h-screen">
      {/* Left side - Image and blue background */}
      <div className="hidden lg:flex w-1/2 items-center justify-center relative overflow-hidden ">
        <img
          src={logo}
          alt="Logo"
          className="max-w-[80%] object-contain"
        />
      </div>

      {/* Right side - Form */}
      <div className="w-full lg:w-1/2 flex items-center justify-center px-6 py-10 bg-white relative overflow-hidden">
        <div className="absolute inset-0 pointer-events-none z-0">
          <div className="absolute top-10 left-10 text-blue-300 text-3xl animate-pulse">💠</div>
          <div className="absolute bottom-10 right-16 text-blue-300 text-2xl animate-bounce">✨</div>
          <div className="absolute top-1/2 left-1/2 text-purple-300 text-4xl animate-spin">★</div>
        </div>
        
        <div className="max-w-md w-full space-y-6 bg-white/80 rounded-3xl shadow-2xl p-8 z-10 ">
          {/* Logo */}
          <div className="flex items-center gap-2 mb-2">
            <img src={skyber} alt="Skyber" className="w-10 h-10 drop-shadow" />
            <span className="text-xl font-bold text-[#0033CC] tracking-wide">SKYBER</span>
          </div>

          {/* Heading */}
          <div>
            <h2 className="text-3xl font-bold text-gray-900 flex items-center gap-2">
              Create Account <span className="animate-bounce">💠</span>
            </h2>
            <p className="text-sm text-gray-600">
              Already have an account?{' '}
              <Link to="/login" className="text-red-500 font-medium hover:underline transition">Sign In</Link>
            </p>
          </div>

          {/* Registration Form */}
          <form className="space-y-4" onSubmit={handleSubmit}>
            {/* Name fields */}
            <div className="flex gap-4">
              <div className="w-1/2">
                <label className="block text-sm font-medium text-gray-700 mb-1">First Name</label>
                <Input 
                  value={firstName}
                  onChange={(event) => setFirstName(event.currentTarget.value)}
                  placeholder="John"
                  styles={(theme) => ({
                    input: { borderRadius: '4px' },
                  })}
                />
              </div>
              <div className="w-1/2">
                <label className="block text-sm font-medium text-gray-700 mb-1">Last Name</label>
                <Input 
                  value={lastName}
                  onChange={(event) => setLastName(event.currentTarget.value)}
                  placeholder="Doe"
                  styles={(theme) => ({
                    input: { borderRadius: '4px' },
                  })}
                />
              </div>
            </div>
            
            {/* Email Field */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">E-mail</label>
              <Input
                placeholder="example@gmail.com"
                leftSection={<IconAt size={16} />}
                value={email}
                onChange={(event) => setEmail(event.currentTarget.value)}
                rightSectionPointerEvents="all"
                styles={(theme) => ({
                  input: { borderRadius: '4px' },
                })}
                rightSection={
                  <CloseButton
                    aria-label="Clear input"
                    onClick={() => setEmail('')}
                    style={{ display: email ? undefined : 'none' }}
                  />
                }
              />
            </div>
            
            {/* Password Field */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Password</label>
              <PasswordInput 
                value={password}
                onChange={(event) => setPassword(event.currentTarget.value)}
                placeholder="@#*%"
                styles={(theme) => ({
                  input: { borderRadius: '4px' },
                })}
                visibilityToggleIcon={({ reveal }) =>
                  reveal ? <IconEyeOff size={16} /> : <IconEye size={16} />
                }
              />
            </div>
            
            {/* Confirm Password Field */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Confirm Password</label>
              <PasswordInput 
                value={confirmPassword}
                onChange={(event) => setConfirmPassword(event.currentTarget.value)}
                placeholder="@#*%"
                styles={(theme) => ({
                  input: { borderRadius: '4px' },
                })}
                visibilityToggleIcon={({ reveal }) =>
                  reveal ? <IconEyeOff size={16} /> : <IconEye size={16} />
                }
              />
            </div>
            
            {/* Gender Selection */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">What's your gender? (Optional)</label>
              <Radio.Group value={gender} onChange={setGender}>
                <Group>
                  <Radio value="female" label="Female" />
                  <Radio value="male" label="Male" />
                  <Radio value="non-binary" label="Non-binary" />
                </Group>
              </Radio.Group>
            </div>
            
            {/* Birth Date */}
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-2">What's your date of birth?</label>
              <div className="flex gap-2">
                <Select
                  placeholder="Month"
                  data={months}
                  value={month}
                  onChange={setMonth}
                  styles={(theme) => ({
                    input: { borderRadius: '4px' },
                  })}
                  className="w-1/3"
                />
                <Select
                  placeholder="Day"
                  data={days}
                  value={day}
                  onChange={setDay}
                  styles={(theme) => ({
                    input: { borderRadius: '4px' },
                  })}
                  className="w-1/3"
                />
                <Select
                  placeholder="Year"
                  data={years}
                  value={year}
                  onChange={setYear}
                  styles={(theme) => ({
                    input: { borderRadius: '4px' },
                  })}
                  className="w-1/3"
                />
              </div>
            </div>

            {/* Phone Number */}            
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Phone Number</label>
              <Input 
                value={phoneNumber}
                onChange={(event) => setPhoneNumber(event.currentTarget.value)}
                placeholder="09123456789"
                styles={(theme) => ({
                  input: { borderRadius: '4px' },
                })}
              />
            </div>
            
            {/* Address*/}    
            <div>
              <label className="block text-sm font-medium text-gray-700 mb-1">Address</label>
              <Input 
                value={address}
                onChange={(event) => setAddress(event.currentTarget.value)}
                placeholder="Your address"
                styles={(theme) => ({
                  input: { borderRadius: '4px' },
                })}
              />
            </div>
            {/* Remember me */}
            <div className="flex items-center">
              <Checkbox 
                checked={rememberMe}
                onChange={(event) => setRememberMe(event.currentTarget.checked)}
                label="Remember me"
                classNames={{
                  label: "text-sm text-gray-700"
                }}
              />
              <div className="ml-auto">
                <Link to="/forgot-password" className="text-red-500 text-sm hover:underline">
                  Forgot Password?
                </Link>
              </div>
            </div>
            
            {/* Sign In Button */}
              <button
                type="submit"
                className="w-full py-3 bg-gradient-to-r from-pink-400 to-blue-500 text-white font-semibold rounded-full shadow-lg hover:scale-105 hover:opacity-90 transition-all duration-200 cursor-pointer"
                disabled={loading}
              >
                {loading ? 'Creating Account...' : 'Create Account'}
              </button>
          </form>
        </div>
      </div>
    </div>
  );
};

export default RegisterFull;