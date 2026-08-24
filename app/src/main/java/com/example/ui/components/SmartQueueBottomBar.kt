package com.example.ui.components

import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.DirectionsWalk
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.*
import com.example.ui.viewmodel.AppDestination
import com.example.ui.viewmodel.UserRole

data class NavItem(
    val destination: AppDestination,
    val label: String,
    val icon: ImageVector,
    val testTag: String
)

@Composable
fun SmartQueueBottomBar(
    currentDestination: AppDestination,
    currentRole: UserRole,
    onNavigate: (AppDestination) -> Unit,
    modifier: Modifier = Modifier
) {
    val items = if (currentRole == UserRole.ADMIN) {
        listOf(
            NavItem(AppDestination.ADMIN_DASHBOARD, "Console", Icons.Default.SupervisorAccount, "nav_admin_console"),
            NavItem(AppDestination.VIRTUAL_QUEUE, "Live Queue", Icons.Default.DirectionsWalk, "nav_live_queue"),
            NavItem(AppDestination.ANALYTICS, "Analytics", Icons.Default.Assessment, "nav_analytics"),
            NavItem(AppDestination.LANDING, "Home", Icons.Default.Home, "nav_home")
        )
    } else {
        listOf(
            NavItem(AppDestination.LANDING, "Home", Icons.Default.Home, "nav_home"),
            NavItem(AppDestination.USER_DASHBOARD, "Dashboard", Icons.Default.Dashboard, "nav_dashboard"),
            NavItem(AppDestination.VIRTUAL_QUEUE, "Line Radar", Icons.Default.DirectionsWalk, "nav_virtual_queue"),
            NavItem(AppDestination.BOOK_APPOINTMENT, "Book", Icons.Default.CalendarMonth, "nav_book"),
            NavItem(AppDestination.ANALYTICS, "Stats", Icons.Default.Assessment, "nav_stats")
        )
    }

    Surface(
        color = Color.White,
        tonalElevation = 8.dp,
        shadowElevation = 8.dp,
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
        modifier = modifier
    ) {
        NavigationBar(
            containerColor = Color.Transparent,
            tonalElevation = 0.dp
        ) {
            items.forEach { item ->
                val selected = currentDestination == item.destination
                NavigationBarItem(
                    selected = selected,
                    onClick = { onNavigate(item.destination) },
                    icon = {
                        Icon(
                            imageVector = item.icon,
                            contentDescription = item.label
                        )
                    },
                    label = {
                        Text(
                            text = item.label,
                            fontSize = 11.sp,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.Normal
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedIconColor = PrimaryIndigo,
                        selectedTextColor = PrimaryIndigo,
                        unselectedIconColor = PolishTextSecondary,
                        unselectedTextColor = PolishTextSecondary,
                        indicatorColor = PrimaryIndigo100
                    ),
                    modifier = Modifier.testTag(item.testTag)
                )
            }
        }
    }
}
