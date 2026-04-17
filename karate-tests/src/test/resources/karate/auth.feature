Feature: Auth API

  Background:
    * url baseUrl

  Scenario: Register and Login

    * def email = 'auth.test+' + new Date().getTime() + '@example.com'
    * def password = 'Password123'

# Register
    Given path 'auth', 'register'
    And request
"""
{
  "name": "Test User",
  "email": "#(email)",
  "password": "#(password)"
}
"""
    When method post
    Then status 200
    And match response.message == 'User registered successfully'

# Login
    Given path 'auth', 'login'
    And request
"""
{
  "email": "#(email)",
  "password": "#(password)"
}
"""
    When method post
    Then status 200
    And match response.token != null

    * def token = response.token