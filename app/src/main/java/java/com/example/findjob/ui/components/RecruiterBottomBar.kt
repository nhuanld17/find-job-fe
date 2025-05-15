package com.example.findjob.ui.components

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import com.example.findjob.R

sealed class RecruiterBottomNavItem(
    val route: String,
    val title: String,
    val icon: Int,
    val iconActiveResId: Int
) {
    object Home : RecruiterBottomNavItem(
        route = "recruiterHome",
        title = "Home",
        icon = R.drawable.ic_home,
        iconActiveResId = R.drawable.ic_home_active
    )
    object Jobs : RecruiterBottomNavItem(
        route = "jobLists",
        title = "Post Job",
        icon = R.drawable.ic_connection,
        iconActiveResId = R.drawable.ic_connection_active
    )
    object CreateJob : RecruiterBottomNavItem(
        route = "createJob",
        title = "Company",
        icon = R.drawable.ic_add,
        iconActiveResId = R.drawable.ic_add_active
    )
    object Profile : RecruiterBottomNavItem(
        route = "recruiterProfile",
        title = "Profile",
        icon = R.drawable.ic_profile,
        iconActiveResId = R.drawable.ic_profile_active
    )
}

@Composable
fun RecruiterBottomBar(
    navController: NavController,
    modifier: Modifier = Modifier
) {
    val items = listOf(
        RecruiterBottomNavItem.Home,
        RecruiterBottomNavItem.Jobs,
        RecruiterBottomNavItem.CreateJob,
        RecruiterBottomNavItem.Profile
    )

    NavigationBar(
        modifier = modifier
            .clip(RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp))
            .border(1.dp, Color(0xFFE5E6EB), RoundedCornerShape(topStart = 20.dp, topEnd = 20.dp)),
        containerColor = Color.White
    ) {
        val navBackStackEntry by navController.currentBackStackEntryAsState()
        val currentRoute = navBackStackEntry?.destination?.route

        items.forEach { item ->
            NavigationBarItem(
                icon = {
                    if (currentRoute == item.route) {
                        Icon(
                            painter = painterResource(id = item.iconActiveResId),
                            contentDescription = item.title,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(35.dp)
                        )
                    } else {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.title,
                            tint = Color.Unspecified,
                            modifier = Modifier.size(26.dp)
                        )
                    }
                },
                selected = currentRoute == item.route,
                onClick = {
                    navController.navigate(item.route) {
                        popUpTo(navController.graph.startDestinationId)
                        launchSingleTop = true
                    }
                },
                colors = NavigationBarItemDefaults.colors(
                    indicatorColor = Color.Transparent
                )
            )
        }
    }
} 