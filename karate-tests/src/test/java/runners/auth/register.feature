Feature: Auth register

Scenario: Register user
  Given url baseUrl
  * def randomEmail = 'demo_' + new Date().getTime() + '@example.com'
  And path 'auth', 'register'
  And request
  """
  {
    "name": "Demo User",
    "email": "#(randomEmail)",
    "password": "Password123"
  }
  """
  When method post
  * print 'register status:', responseStatus
  * print 'register response:', response
  Then assert responseStatus >= 200 && responseStatus < 500