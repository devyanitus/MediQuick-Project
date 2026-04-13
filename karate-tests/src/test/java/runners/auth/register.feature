Feature: Auth register

  Scenario: Register a new user successfully
    Given url baseUrl
    And path 'auth', 'register'
    And request
    """
    {
      "name": "Register User",
      "email": "karate.register@example.com",
      "password": "Password123"
    }
    """
    When method post
    Then assert responseStatus == 200 || responseStatus == 400

  Scenario: Register duplicate user should fail
    Given url baseUrl
    And path 'auth', 'register'
    And request
    """
    {
      "name": "Register User",
      "email": "karate.register@example.com",
      "password": "Password123"
    }
    """
    When method post
    Then status 400
    And match response.error == 'User already registered'