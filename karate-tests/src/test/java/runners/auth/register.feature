Feature: Auth register

Scenario: Register user
  Given url baseUrl
  * def randomEmail = 'demo_' + uuid() + '@example.com'
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
  Then assert responseStatus >= 200 && responseStatus < 500