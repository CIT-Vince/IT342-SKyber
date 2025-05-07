import { notifications } from '@mantine/notifications';

// Store the original show method
const originalShow = notifications.show;

// Global flag to enable/disable notifications
const NOTIFICATIONS_ENABLED = false;

// Override the show method
notifications.show = function(props) {
  if (NOTIFICATIONS_ENABLED) {
    // Call the original method if notifications are enabled
    originalShow.call(this, props);
  } else {
    // Optional: log to console when disabled
    console.log('Notification (disabled):', props.title, props.message);
  }
};