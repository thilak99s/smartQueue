package com.example

import android.content.Context
import androidx.test.core.app.ApplicationProvider
import com.example.data.model.ServiceType
import com.example.data.repository.QueueRepository
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ExampleRobolectricTest {

  @Test
  fun `read string from context`() {
    val context = ApplicationProvider.getApplicationContext<Context>()
    val appName = context.getString(R.string.app_name)
    assertEquals("SmartQueue", appName)
  }

  @Test
  fun `repository booking creates new ticket and updates queue`() {
    val repository = QueueRepository()
    val initialTicketsCount = repository.state.value.tickets.size

    val booked = repository.bookAppointment(
      name = "Test Customer",
      phone = "+1 555 123 4567",
      email = "test@example.com",
      serviceType = ServiceType.GENERAL,
      date = "Today",
      timeSlot = "10:00 AM",
      preferredCounterId = null,
      isPriority = false
    )

    assertNotNull(booked)
    assertEquals("Test Customer", booked.customerName)
    assertTrue(repository.state.value.tickets.size > initialTicketsCount)
  }
}

