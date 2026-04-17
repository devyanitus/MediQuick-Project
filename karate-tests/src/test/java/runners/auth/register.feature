Feature: Auth register

  Background:
    * def uniqueEmail = 'karate.register+' + java.util.UUID.randomUUID() + '@example.com'

  Scenario: Register a new user successfully
    Given url baseUrl
    And path 'auth', 'register'
    And request
    """
    {
      "name": "Register User",
      "email": "#(uniqueEmail)",
      "password": "Password123"
    }
    """
    When method post
    * print 'register success status:', responseStatus
    * print 'register success response:', response
    Then assert responseStatus == 201 || responseStatus == 200

  Scenario: Register duplicate user should fail
    Given url baseUrl
    And path 'auth', 'register'
    And request
    """
    {
      "name": "Register User",
      "email": "#(uniqueEmail)",
      "password": "Password123"
    }
    """
    When method post
    * print 'first duplicate setup status:', responseStatus
    * print 'first duplicate setup response:', response
    Then assert responseStatus == 201 || responseStatus == 200

    Given url baseUrl
    And path 'auth', 'register'
    And request
    """
    {
      "name": "Register User",
      "email": "#(uniqueEmail)",
      "password": "Password123"
    }
    """
    When method post
    * print 'duplicate status:', responseStatus
    * print 'duplicate response:', response
    Then status 400
    And match response.error == 'User already registered'