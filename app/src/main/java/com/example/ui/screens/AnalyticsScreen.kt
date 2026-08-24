package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Assessment
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.DesktopWindows
import androidx.compose.material.icons.filled.PieChart
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Counter
import com.example.data.model.ServiceType
import com.example.data.repository.QueueState
import com.example.ui.theme.*

@Composable
fun AnalyticsScreen(
    queueState: QueueState,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    val totalServed = queueState.totalServedCount
    val totalCancelled = queueState.totalCancelledCount
    val totalProcessed = (totalServed + totalCancelled).coerceAtLeast(1)
    val completionRate = ((totalServed.toFloat() / totalProcessed) * 100).toInt()

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishCanvas)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 36.dp)
    ) {
        // Header
        Text(
            text = "Queue Analytics & Insights",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = PolishTextPrimary,
                letterSpacing = (-0.3).sp
            )
        )
        Text(
            text = "Operational metrics, throughput and counter utilization telemetry.",
            style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Top 3 KPI Banner Cards
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            KpiCard(
                title = "Total Served",
                value = "$totalServed",
                subtitle = "+$completionRate% rate",
                icon = Icons.Default.CheckCircle,
                color = StatusSuccess,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Avg Wait",
                value = "7.4 min",
                subtitle = "-42% with algorithm",
                icon = Icons.Default.Speed,
                color = PrimaryIndigo,
                modifier = Modifier.weight(1f)
            )
            KpiCard(
                title = "Cancelled",
                value = "$totalCancelled",
                subtitle = "Low drop-off",
                icon = Icons.Default.Cancel,
                color = StatusError,
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Counter Utilization Meter Section
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryIndigo50),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.DesktopWindows, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Counter Utilization & Load",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PolishTextPrimary
                            )
                        )
                    }
                    Text(text = "Overall: 86%", fontWeight = FontWeight.Black, color = PrimaryIndigo, fontSize = 12.sp)
                }

                Spacer(modifier = Modifier.height(14.dp))

                val mockUtilizations = listOf(
                    Triple("Counter 1 (General)", 0.88f, 6),
                    Triple("Counter 2 (Payment)", 0.94f, 5),
                    Triple("Counter 3 (Consultation)", 0.76f, 4),
                    Triple("Counter 4 (Multi-Service)", 0.65f, 3)
                )

                mockUtilizations.forEach { (name, util, served) ->
                    Column(modifier = Modifier.padding(vertical = 4.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(text = name, fontSize = 12.sp, fontWeight = FontWeight.Medium, color = PolishTextSecondary)
                            Text(text = "${(util * 100).toInt()}% ($served served)", fontSize = 12.sp, fontWeight = FontWeight.Bold, color = PolishTextPrimary)
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        LinearProgressIndicator(
                            progress = { util },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(8.dp)
                                .clip(RoundedCornerShape(4.dp)),
                            color = if (util > 0.85f) StatusSuccess else PrimaryIndigo,
                            trackColor = PolishCanvas,
                            strokeCap = StrokeCap.Round
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Peak Hours Traffic Chart
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color.White,
            shadowElevation = 1.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .clip(RoundedCornerShape(8.dp))
                                .background(PrimaryIndigo50),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.BarChart, contentDescription = null, tint = PrimaryIndigo, modifier = Modifier.size(18.dp))
                        }
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Peak Queue Period (Today)",
                            style = MaterialTheme.typography.titleMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = PolishTextPrimary
                            )
                        )
                    }
                    Text(text = "Peak: 11 AM - 1 PM", fontWeight = FontWeight.Bold, color = PolishOrangeText, fontSize = 11.sp)
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Visual Bars for Hours
                val hourlyTraffic = listOf(
                    "9 AM" to 0.3f,
                    "10 AM" to 0.6f,
                    "11 AM" to 0.95f,
                    "12 PM" to 0.85f,
                    "1 PM" to 0.7f,
                    "2 PM" to 0.5f,
                    "3 PM" to 0.65f,
                    "4 PM" to 0.4f
                )

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    hourlyTraffic.forEach { (hour, heightFraction) ->
                        val isPeak = heightFraction > 0.8f
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.weight(1f)
                        ) {
                            Box(
                                modifier = Modifier
                                    .width(22.dp)
                                    .height((heightFraction * 90).dp)
                                    .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                    .background(
                                        if (isPeak) PolishOrangeHighlight
                                        else PrimaryIndigo
                                    )
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(text = hour, fontSize = 9.sp, color = PolishTextSecondary, fontWeight = FontWeight.Medium)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun KpiCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = Color.White,
        shadowElevation = 1.dp,
        border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
        modifier = modifier
    ) {
        Column(modifier = Modifier.padding(12.dp)) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(color.copy(alpha = 0.12f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(imageVector = icon, contentDescription = null, tint = color, modifier = Modifier.size(16.dp))
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, fontSize = 10.sp, color = PolishTextSecondary)
            Text(text = value, fontWeight = FontWeight.Black, fontSize = 16.sp, color = PolishTextPrimary)
            Text(text = subtitle, fontSize = 9.sp, color = PolishTextSecondary)
        }
    }
}
