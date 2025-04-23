import { initializeApp } from "firebase/app";
import { getAuth } from "firebase/auth";
import { getFirestore } from "firebase/firestore";

// Firebase configuration
const firebaseConfig = {
  apiKey: "AIzaSyAuF4fCivSrXo8wHnmzXzYbaxGdZBdlDAo",
  authDomain: "skyber-57fa5.firebaseapp.com",
  projectId: "skyber-57fa5",
  storageBucket: "skyber-57fa5.appspot.com",
  messagingSenderId: "627178043013",
  appId: "1:627178043013:android:eb879a84075c6dbccf9bec",
  databaseURL: "https://skyber-57fa5-default-rtdb.asia-southeast1.firebasedatabase.app"
};

// Initialize Firebase
const app = initializeApp(firebaseConfig);
export const auth = getAuth(app);
export const db = getFirestore(app);

export default app;