package com.example.skyber

sealed class Screens(val screen: String){
    //Navbar screens
    data object Home: Screens("Home")
    data object VolunteerHub: Screens("Volunteer Hub")
    data object Portal: Screens("Portal")
    data object Feedback: Screens("Feedback and Polls")
    data object UserProfile: Screens("User Profile")

    //Portal nav routes
    data object Reports : Screens("Reports")
    //data object JobsAndScholarships : Screens("Signup")
    data object Announcement: Screens("Announcement")
    data object PostAnnouncement: Screens("Post Announcement")

    //screens found in user profile
    data object EditProfile: Screens("Edit Profile")

    //Auth Screens
    data object Login : Screens("Login")
    data object SignUp : Screens("Signup")

}