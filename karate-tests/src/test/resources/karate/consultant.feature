Feature: Consultant APIs

  Background:
    * url baseUrl

  Scenario: Get consultants

    * def email = 'consultant.test+' + new Date().getTime() + '@example.com'
    * def password = 'Password123'

# Register + Login
    Given path 'auth', 'register'
    And request { name: 'User', email: '#(email)', password: '#(password)' }
    When method post

    Given path 'auth', 'login'
    And request { email: '#(email)', password: '#(password)' }
    When method post
    * def token = response.token

# Get consultants
    Given path 'consultants'
    And header Authorization = 'Bearer ' + token
    When method get
    Then status 200
    And match response != null