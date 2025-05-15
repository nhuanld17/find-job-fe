package com.example.findjob.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.findjob.ui.screen.employee.AIScreen
import com.example.findjob.ui.screen.employee.CompanyInfoScreen
import com.example.findjob.ui.screen.employee.EmployeeHomeScreen
import com.example.findjob.ui.screen.employee.EmployeeProfileScreen
import com.example.findjob.ui.screen.employee.FilterScreen
import com.example.findjob.ui.screen.employee.JobDescriptionScreen
import com.example.findjob.ui.screen.employee.NotificationDetailScreen
import com.example.findjob.ui.screen.employee.NotificationScreen
import com.example.findjob.ui.screen.employee.SavedJobScreen
import com.example.findjob.ui.screen.employee.SearchJobScreen
import com.example.findjob.ui.screen.employee.UploadCVScreen
import com.example.findjob.ui.screen.login.LoginScreen
import com.example.findjob.ui.screen.recruiter.CreateJobScreen
import com.example.findjob.ui.screen.recruiter.CurriculumDetailScreen
import com.example.findjob.ui.screen.recruiter.RecruiterHomeScreen
import com.example.findjob.ui.screen.recruiter.ListCurriculumSceen
import com.example.findjob.ui.screen.recruiter.RecruiterJobListScreen
import com.example.findjob.ui.screen.recruiter.RecruiterProfileScreen
import com.example.findjob.ui.screen.register.RegisterScreen
import com.example.findjob.ui.screen.splash.SplashScreen
import com.example.findjob.utils.InfoManager
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

@Composable
fun AppNavGraph(
    navController: NavHostController,
    infoManager: InfoManager
) {
    NavHost(navController, startDestination = "splash") {
        composable("splash") { 
            SplashScreen(navController, infoManager)
        }
        
        composable("login") { LoginScreen(navController) }
        composable("register") { RegisterScreen(navController) }

        // Employee Screen
        composable("employeeHome") { EmployeeHomeScreen(navController) }
        composable("companyInfo/{email}") { backStackEntry ->
            val email = backStackEntry.arguments?.getString("email")?.toString() ?: ""
            CompanyInfoScreen(navController, email) }
        composable("employeeProfile") { EmployeeProfileScreen(navController, infoManager = infoManager) }
        composable("filter") { FilterScreen(navController) }
        composable("jobDescription/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            JobDescriptionScreen(navController, id)
        }
        composable("notification") { NotificationScreen(navController) }
        composable("notificationDetail") { NotificationDetailScreen(navController)
        }
        composable("savedJobs") { SavedJobScreen(navController) }
        composable("searchJob") { SearchJobScreen(navController) }
        composable("uploadCV/{jobId}&{jobTitle}") { backStackEntry ->
            val jobId = backStackEntry.arguments?.getString("jobId")?.toIntOrNull() ?: 0
            val jobTitle = backStackEntry.arguments?.getString("jobTitle") ?: ""
            UploadCVScreen(navController, jobId = jobId, jobTitle = jobTitle)
        }
        composable("chatAI") { AIScreen(navController) }

        // Recruiter Screen
        composable("recruiterHome") { RecruiterHomeScreen(navController, infoManager = infoManager) }
        composable("createJob") { CreateJobScreen(navController) }
        composable("jobLists") { RecruiterJobListScreen(navController) }
        composable("recruiterProfile") { RecruiterProfileScreen(navController) }
        composable("listCurriculum/{id}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: "0"
            ListCurriculumSceen(navController, id)
        }
        composable("curriculumDetail/{id}/{cvLink}") { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id")?.toIntOrNull() ?: 0
            val cvLink = backStackEntry.arguments?.getString("cvLink") ?: ""
            CurriculumDetailScreen(navController, id, cvLink)
        }
    }
}