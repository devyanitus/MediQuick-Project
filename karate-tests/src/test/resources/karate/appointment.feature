#Feature: Appointment API Tests
#
#  Scenario: Get user bookings
#
#    * def uniqueEmail = 'appointment.test+' + new Date().getTime() + '@example.com'
#    * def password = 'Password123'
#
## Register
#    Given url baseUrl
#    And path 'auth', 'register'
#    And request
#"""
#{
#  "name": "Appointment User",
#  "email": "#(uniqueEmail)",
#  "password": "#(password)"
#}
#"""
#    When method post
#    Then status 200
#
## Login
#    Given url baseUrl
#    And path 'auth', 'login'
#    And request
#"""
#{
#  "email": "#(uniqueEmail)",
#  "password": "#(password)"
#}
#"""
#    When method post
#    Then status 200
#    And match response.token != null
#
#    * def token = response.token
#
## Create booking first (IMPORTANT)
#    Given url baseUrl
#    And path 'consultants', 'bookings'
#    And header Authorization = 'Bearer ' + token
#    And request
#"""
#{
#  "userEmail": "#(uniqueEmail)",
#  "doctorId": 1,
#  "bookingDate": "2026-04-20",
#  "timeSlot": "10:00"
#}
#"""
#    When method post
#    Then status 200
#
## Get bookings (email param REQUIRED)
#    Given url baseUrl
#    And path 'consultants', 'bookings'
#    And param email = uniqueEmail
#    And header Authorization = 'Bearer ' + token
#    When method get
#    Then status 200

Feature: Appointment APIs

  Background:
    * url baseUrl

  Scenario: Get user bookings

    * def email = 'appt.test+' + new Date().getTime() + '@example.com'
    * def password = 'Password123'

# Register
    Given path 'auth', 'register'
    And request { name: 'User', email: '#(email)', password: '#(password)' }
    When method post

# Login
    Given path 'auth', 'login'
    And request { email: '#(email)', password: '#(password)' }
    When method post
    * def token = response.token

    * print 'TOKEN:', token

# Create booking
    Given path 'consultants', 'bookings'
    And header Authorization = 'Bearer ' + token
    And request
"""
{
  "userEmail": "#(email)",
  "doctorId": 1,
  "bookingDate": "2026-04-20",
  "timeSlot": "10:00"
}
"""
    When method post
    Then status 200

# Get bookings (CRITICAL FIX)
    Given path 'consultants', 'bookings'
    And param email = email
    And header Authorization = 'Bearer ' + token
    When method get
    Then status 200