Feature: Booking Flow

    Background:
        * url baseUrl

    Scenario: Complete booking journey

        * def email = 'booking.test+' + new Date().getTime() + '@example.com'
        * def password = 'Password123'

# Register + Login
        Given path 'auth', 'register'
        And request { name: 'User', email: '#(email)', password: '#(password)' }
        When method post

        Given path 'auth', 'login'
        And request { email: '#(email)', password: '#(password)' }
        When method post
        * def token = response.token

# Get doctors
        Given path 'consultants', 'doctors', 'category', 'GP'
        And header Authorization = 'Bearer ' + token
        When method get
        Then status 200

        * if (response.length == 0) karate.abort()
        * def doctorId = response[0].id

# Get availability
        Given path 'consultants', 'doctors', doctorId, 'availability'
        And header Authorization = 'Bearer ' + token
        When method get
        Then status 200

        * if (response.length == 0) karate.abort()
        * def slotId = response[0].id
        * def bookingDate = response[0].availableDate
        * def timeSlot = response[0].timeSlot

# Book slot
        Given path 'consultants', 'availability', slotId, 'book'
        And header Authorization = 'Bearer ' + token
        When method put
        Then status 200

# Create booking
        Given path 'consultants', 'bookings'
        And header Authorization = 'Bearer ' + token
        And request
"""
{
  "userEmail": "#(email)",
  "doctorId": #(doctorId),
  "bookingDate": "#(bookingDate)",
  "timeSlot": "#(timeSlot)"
}
"""
        When method post
        Then status 200
        And match response.id != null