import { showNotification as mantineShowNotification } from '@mantine/notifications';

// Global flag to enable/disable all notifications
const NOTIFICATIONS_ENABLED = false;

export const showNotification = (props) => {
  // Only show notifications if enabled
  if (NOTIFICATIONS_ENABLED) {
    mantineShowNotification(props);
  }
  
  // Optional: still log to console when disabled
  if (!NOTIFICATIONS_ENABLED) {
    console.log('Notification (disabled):', props.title, props.message);
  }
};