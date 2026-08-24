package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ServiceType
import com.example.ui.theme.*

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun BookAppointmentScreen(
    onBookConfirmed: (name: String, phone: String, email: String, service: ServiceType, date: String, slot: String, preferredCounter: Int?, isPriority: Boolean) -> Unit,
    modifier: Modifier = Modifier
) {
    val scrollState = rememberScrollState()

    var name by remember { mutableStateOf("Sneha Rao") }
    var phone by remember { mutableStateOf("+1 (555) 901-2345") }
    var email by remember { mutableStateOf("sneha.rao@example.com") }
    var selectedService by remember { mutableStateOf(ServiceType.GENERAL) }
    var selectedDate by remember { mutableStateOf("Today") }
    var selectedSlot by remember { mutableStateOf("11:30 AM") }
    var selectedCounter by remember { mutableStateOf<Int?>(null) }
    var isPriority by remember { mutableStateOf(false) }

    val dateOptions = listOf("Today", "Tomorrow", "Aug 26")
    val slotOptions = listOf("10:00 AM", "10:30 AM", "11:00 AM", "11:30 AM", "12:00 PM", "2:00 PM", "2:30 PM", "3:00 PM", "3:30 PM")

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(PolishCanvas)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .padding(bottom = 36.dp)
    ) {
        // Title Header
        Text(
            text = "Book an Appointment",
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Black,
                color = PolishTextPrimary,
                letterSpacing = (-0.3).sp
            )
        )
        Text(
            text = "Generate a virtual ticket and join the intelligent queue instantly.",
            style = MaterialTheme.typography.bodySmall.copy(color = PolishTextSecondary)
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Form Card (Professional Polish White Card)
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = Color.White,
            shadowElevation = 2.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, PolishCardBorder),
            modifier = Modifier.fillMaxWidth()
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Customer Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Customer Full Name") },
                    leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = PrimaryIndigo) },
                    singleLine = true,
                    shape = RoundedCornerShape(14.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = PrimaryIndigo,
                        unfocusedBorderColor = Slate200,
                        focusedLabelColor = PrimaryIndigo
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("book_name_input")
                )

                // Phone & Email
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    OutlinedTextField(
                        value = phone,
                        onValueChange = { phone = it },
                        label = { Text("Phone") },
                        leadingIcon = { Icon(Icons.Default.Phone, contentDescription = null, tint = PrimaryIndigo) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedBorderColor = Slate200,
                            focusedLabelColor = PrimaryIndigo
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("book_phone_input")
                    )

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email (Optional)") },
                        leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = PrimaryIndigo) },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        singleLine = true,
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = PrimaryIndigo,
                            unfocusedBorderColor = Slate200,
                            focusedLabelColor = PrimaryIndigo
                        ),
                        modifier = Modifier
                            .weight(1f)
                            .testTag("book_email_input")
                    )
                }

                // Service Type Selector
                Column {
                    Text(
                        text = "Select Service Type",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PolishTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                        ServiceType.entries.forEach { service ->
                            val isSelected = selectedService == service
                            Surface(
                                shape = RoundedCornerShape(16.dp),
                                color = if (isSelected) PrimaryIndigo50 else PolishCanvas,
                                border = androidx.compose.foundation.BorderStroke(
                                    if (isSelected) 1.5.dp else 1.dp,
                                    if (isSelected) PrimaryIndigo else PolishCardBorder
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clickable { selectedService = service }
                                    .testTag("service_option_${service.name}")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(14.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Box(
                                            modifier = Modifier
                                                .size(36.dp)
                                                .clip(RoundedCornerShape(10.dp))
                                                .background(if (isSelected) PrimaryIndigo else Slate300),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = service.prefix,
                                                color = Color.White,
                                                fontWeight = FontWeight.Black,
                                                fontSize = 15.sp
                                            )
                                        }
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Column {
                                            Text(
                                                text = service.title,
                                                fontWeight = FontWeight.Bold,
                                                fontSize = 14.sp,
                                                color = if (isSelected) PrimaryIndigo900 else PolishTextPrimary
                                            )
                                            Text(
                                                text = service.description,
                                                fontSize = 11.sp,
                                                color = PolishTextSecondary
                                            )
                                        }
                                    }

                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) PrimaryIndigo else Slate200
                                    ) {
                                        Text(
                                            text = "~${service.avgDurationMinutes} min",
                                            color = if (isSelected) Color.White else PolishTextSecondary,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Bold,
                                            modifier = Modifier.padding(horizontal = 7.dp, vertical = 3.dp)
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                // Date Selection Chips
                Column {
                    Text(
                        text = "Appointment Date",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PolishTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        dateOptions.forEach { date ->
                            val isSelected = selectedDate == date
                            Surface(
                                shape = RoundedCornerShape(12.dp),
                                color = if (isSelected) PrimaryIndigo else PolishCanvas,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) PrimaryIndigo else PolishCardBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedDate = date }
                                    .testTag("date_option_$date")
                            ) {
                                Row(
                                    modifier = Modifier.padding(vertical = 10.dp),
                                    horizontalArrangement = Arrangement.Center,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        Icons.Default.CalendarMonth,
                                        contentDescription = null,
                                        tint = if (isSelected) Color.White else PolishTextSecondary,
                                        modifier = Modifier.size(15.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = date,
                                        color = if (isSelected) Color.White else PolishTextPrimary,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 12.sp
                                    )
                                }
                            }
                        }
                    }
                }

                // Time Slots Chips
                Column {
                    Text(
                        text = "Select Time Slot",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PolishTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        slotOptions.forEach { slot ->
                            val isSelected = selectedSlot == slot
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) PrimaryIndigo else PolishCanvas,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) PrimaryIndigo else PolishCardBorder
                                ),
                                modifier = Modifier
                                    .clickable { selectedSlot = slot }
                                    .testTag("slot_option_$slot")
                            ) {
                                Text(
                                    text = slot,
                                    color = if (isSelected) Color.White else PolishTextPrimary,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                )
                            }
                        }
                    }
                }

                // Preferred Counter (Optional)
                Column {
                    Text(
                        text = "Preferred Counter (Optional)",
                        style = MaterialTheme.typography.titleSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = PolishTextPrimary
                        )
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        val counterOptions = listOf<Pair<String, Int?>>(
                            "Auto (Fastest)" to null,
                            "Counter 1" to 1,
                            "Counter 2" to 2,
                            "Counter 3" to 3,
                            "Counter 4" to 4
                        )
                        counterOptions.forEach { (label, id) ->
                            val isSelected = selectedCounter == id
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isSelected) PrimaryIndigo50 else PolishCanvas,
                                border = androidx.compose.foundation.BorderStroke(
                                    1.dp,
                                    if (isSelected) PrimaryIndigo else PolishCardBorder
                                ),
                                modifier = Modifier
                                    .weight(1f)
                                    .clickable { selectedCounter = id }
                            ) {
                                Text(
                                    text = label,
                                    fontSize = 10.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) PrimaryIndigo else PolishTextSecondary,
                                    textAlign = androidx.compose.ui.text.style.TextAlign.Center,
                                    modifier = Modifier.padding(vertical = 8.dp)
                                )
                            }
                        }
                    }
                }

                // Priority VIP Switch
                Surface(
                    shape = RoundedCornerShape(16.dp),
                    color = if (isPriority) Color(0xFFF5F3FF) else PolishCanvas,
                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,
                        if (isPriority) StatusPriority else PolishCardBorder
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = null,
                                tint = if (isPriority) StatusPriority else PolishTextSecondary,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Priority VIP Ticket",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = if (isPriority) StatusPriority else PolishTextPrimary
                                )
                                Text(
                                    text = "Senior citizen, disability or urgent clearance",
                                    fontSize = 10.sp,
                                    color = PolishTextSecondary
                                )
                            }
                        }

                        Switch(
                            checked = isPriority,
                            onCheckedChange = { isPriority = it },
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = StatusPriority
                            ),
                            modifier = Modifier.testTag("priority_vip_switch")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Submit Button
                Button(
                    onClick = {
                        onBookConfirmed(
                            name.ifBlank { "Guest Customer" },
                            phone.ifBlank { "+1 (555) 000-0000" },
                            email,
                            selectedService,
                            selectedDate,
                            selectedSlot,
                            selectedCounter,
                            isPriority
                        )
                    },
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = PrimaryIndigo),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .testTag("confirm_booking_button")
                ) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Generate Token & Join Queue", fontSize = 14.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
