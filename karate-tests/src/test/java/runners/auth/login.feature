Feature: Auth login

Background:
  * def uniqueEmail = 'karate.login+' + new Date().getTime() + '@example.com'

  Given url baseUrl
  And path 'auth', 'register'
  And request
  """
  {
    "name": "Karate User",
    "email": "#(uniqueEmail)",
    "password": "Password123"
  }
  """
  When method post
  * print 'register status:', responseStatus
  * print 'register response:', response
  Then assert responseStatus < 500

Scenario: Login successfully
  Given url baseUrl
  And path 'auth', 'login'
  And request
  """
  {
    "email": "#(uniqueEmail)",
    "password": "Password123"
  }
  """
  When method post
  * print 'login success status:', responseStatus
  * print 'login success response:', response
  Then assert responseStatus < 500