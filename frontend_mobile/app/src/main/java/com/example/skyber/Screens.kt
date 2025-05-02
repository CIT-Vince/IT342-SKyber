package com.example.skyber

sealed class Screens(val screen: String){
    //Navbar screens
    data object Home: Screens("Home")
    data object VolunteerHub: Screens("Volunteer Hub")
    data object Portal: Screens("Portal")
    data object SKcandidates: Screens("SKcandidates")
    data object UserProfile: Screens("User Profile")

    //VolunteerHub Screens
    data object PostVolunteerHub: Screens("Post Volunteer Hub")
    data object DetailsVolunteerHub: Screens("Details Volunteer Hub")

    //Portal nav routes
    data object Reports : Screens("Reports")

    data object Projects : Screens("Projects")//Project and Nested Screens in Projects
    data object PostProject: Screens("Post Projects")
    data object DetailsProject: Screens("Details Projects")

    data object Job: Screens("Jobs" )//Jobs listing and nested screens
    data object PostJob: Screens("Post Job")
    data object DetailsJob: Screens("Details Job")

    data object Scholarship: Screens("Scholarships" )//Scholarships and Nested Screens in Scholarships
    data object PostScholarship: Screens("Post Scholarship")
    data object DetailsScholarship: Screens("Details Scholarship")

    data object PostSKcandidates: Screens("Post SKCandidates")//SK candidates nested screens
    data object DetailsSKcandidates: Screens("Details SKCandidates")

    data object Announcement: Screens("Announcement")//Announcement and Nested Screens in Announcement
    data object PostAnnouncement: Screens("Post Announcement")
    data object DetailsAnnouncement: Screens("Details Announcement")

    //screens found in user profile
    data object EditProfile: Screens("Edit Profile")
    data object VolunteerList: Screens("Volunteer List")
    data object DetailsVolunteerList: Screens("Details Volunteer List")

    //Auth Screens
    data object Login : Screens("Login")
    data object SignUp : Screens("Signup")

    //Test screens
    data object CandidateTestScreen : Screens("Signup")
    data object CandidateList : Screens("candidateList")
}