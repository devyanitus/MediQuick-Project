Feature: Appointment API Tests

  Background:
    * url baseUrl

  Scenario: Get user bookings

    Given path '/auth/login'
    And request { email: 'test@test.com', password: '123456' }
    When method POST
    Then status 200
    * def token = response.token

    Given path '/consultants/bookings'
    And param email = 'test@test.com'
    And header Authorization = 'Bearer ' + token
    When method GET
    Then status 200