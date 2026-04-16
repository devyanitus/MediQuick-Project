Feature: Consultant API Tests

  Background:
    * url baseUrl

  Scenario: Get doctors by category
    Given path '/consultants/doctors/category/GP'
    When method GET
    Then status 200