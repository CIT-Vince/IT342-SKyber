import { StrictMode } from 'react'
import { createRoot } from 'react-dom/client'
import { MantineProvider } from '@mantine/core';
import { ModalsProvider } from '@mantine/modals';
import { Notifications } from '@mantine/notifications';
import { AuthProvider } from './contexts/AuthContext'
import './index.css'
import 'atropos/css'
import '@mantine/core/styles.css';
import '@mantine/dates/styles.css';
import '@mantine/notifications/styles.css'; 
import App from './App.jsx'
import './Pages/utils/notificationOverride'; // Import the notification override

createRoot(document.getElementById('root')).render(
  <StrictMode>
    <MantineProvider withGlobalStyles withNormalizeCSS>
    <Notifications position="bottom-left" 
    />
      <ModalsProvider>
        <AuthProvider>
          <App />
        </AuthProvider>
      </ModalsProvider>
    </MantineProvider>
  </StrictMode>,
)

const removePreloader = () => {
  const preloader = document.getElementById('pre-loader')
  if (preloader) {
    preloader.style.opacity = '0'
    preloader.style.transition = 'opacity 0.5s'
    setTimeout(() => {
      preloader?.remove()
    }, 500)
  }
}

removePreloader()