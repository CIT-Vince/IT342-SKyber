package com.example.skyber

sealed class Screens(val screen: String){
    data object Home: Screens("home")
    data object VolunteerHub: Screens("Volunteer Hub")
    data object Analytics: Screens("Analytics")
    data object Announcement: Screens("Announcement")
    data object UserProfile: Screens("User Profile")
    data object Login : Screens("Login")
    data object SignUp : Screens("Signup")
}