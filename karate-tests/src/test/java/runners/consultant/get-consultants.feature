Feature: Consultant APIs

  Background:
    Given url baseUrl
    And path 'auth', 'register'
    And request
    """
    {
      "name": "Consultant Test User",
      "email": "consultant.test@example.com",
      "password": "Password123"
    }
    """
    When method post
    Then assert responseStatus == 200 || responseStatus == 400

    Given url baseUrl
    And path 'auth', 'login'
    And request
    """
    {
      "email": "consultant.test@example.com",
      "password": "Password123"
    }
    """
    When method post
    Then status 200
    * def token = response.token

  Scenario: Get all consultants
    Given url baseUrl
    And path 'consultants'
    And header Authorization = 'Bearer ' + token
    When method get
    Then status 200
    And match response == '#[]'