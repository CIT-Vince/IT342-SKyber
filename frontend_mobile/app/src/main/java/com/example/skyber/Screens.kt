package com.example.skyber

sealed class Screens(val screen: String){
    //Navbar screens
    data object Home: Screens("Home")
    data object VolunteerHub: Screens("Volunteer Hub")
    data object Portal: Screens("Portal")
    data object Feedback: Screens("Feedback and Polls")
    data object UserProfile: Screens("User Profile")

    //VolunteerHub Screens
    data object PostVolunteerHub: Screens("Post Volunteer Hub")
    data object DetailsVolunteerHub: Screens("Details Volunteer Hub")

    //Portal nav routes
    data object Reports : Screens("Reports")

    data object Projects : Screens("Projects")//Project and Nested Screens in Projects
    data object PostProject: Screens("Post Projects")
    data object DetailsProject: Screens("Details Projects")

    //data object JobsAndScholarships : Screens("Signup")
    data object Announcement: Screens("Announcement")//Announcement and Nested Screens in Announcement
    data object PostAnnouncement: Screens("Post Announcement")
    data object DetailsAnnouncement: Screens("Details Announcement")

    //screens found in user profile
    data object EditProfile: Screens("Edit Profile")

    //Auth Screens
    data object Login : Screens("Login")
    data object SignUp : Screens("Signup")

}