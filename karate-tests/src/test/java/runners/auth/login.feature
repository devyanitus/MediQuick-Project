Feature: Auth login

  Background:
    Given url baseUrl
    And path 'auth', 'register'
    And request
    """
    {
      "name": "Karate User",
      "email": "karate.login@example.com",
      "password": "Password123"
    }
    """
    When method post
    * print 'register status:', responseStatus
    * print 'register response:', response
    Then assert responseStatus == 201 || responseStatus == 200 || responseStatus == 400 || responseStatus == 409

  Scenario: Login successfully
    Given url baseUrl
    And path 'auth', 'login'
    And request
    """
    {
      "email": "karate.login@example.com",
      "password": "Password123"
    }
    """
    When method post
    Then status 200
    And match response.email == 'karate.login@example.com'
    And match response.name == 'Karate User'
    And match response.token == '#string'

  Scenario: Login with wrong password
    Given url baseUrl
    And path 'auth', 'login'
    And request
    """
    {
      "email": "karate.login@example.com",
      "password": "WrongPassword"
    }
    """
    When method post
    Then status 400
    And match response.error == 'Invalid password'

  Scenario: Login with unknown user
    Given url baseUrl
    And path 'auth', 'login'
    And request
    """
    {
      "email": "unknown@example.com",
      "password": "Password123"
    }
    """
    When method post
    Then status 400
    And match response.error == 'User not found'