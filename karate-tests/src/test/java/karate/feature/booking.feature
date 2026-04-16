Feature: Booking API Tests

  Background:
    * url baseUrl

  # Step 1: Login and get token
  Scenario: Full booking flow

    Given path '/auth/login'
    And request { email: 'test@test.com', password: '123456' }
    When method POST
    Then status 200
    * def token = response.token

    # Step 2: Get doctors
    Given path '/consultants/doctors/category/GP'
    And header Authorization = 'Bearer ' + token
    When method GET
    Then status 200
    * def doctorId = response[0].id

    # Step 3: Get availability
    Given path '/consultants/doctors/' + doctorId + '/availability'
    And header Authorization = 'Bearer ' + token
    When method GET
    Then status 200
    * def slotId = response[0].id
    * def bookingDate = response[0].availableDate
    * def timeSlot = response[0].timeSlot

    # Step 4: Book slot (mark as booked)
    Given path '/consultants/availability/' + slotId + '/book'
    And header Authorization = 'Bearer ' + token
    When method PUT
    Then status 200

    #  Step 5: Create booking record
    Given path '/consultants/bookings'
    And header Authorization = 'Bearer ' + token
    And request
      """
      {
    userEmail: 'test@test.com',
    doctorId: '#(doctorId)',
    bookingDate: '#(bookingDate)',
    timeSlot: '#(timeSlot)'
    }
    """
    When method POST
    Then status 200
    And match response.bookingReference != null