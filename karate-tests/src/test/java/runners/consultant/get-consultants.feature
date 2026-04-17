Feature: Consultant APIs

Background:
  * def uniqueEmail = 'consultant.test+' + new Date().getTime() + '@example.com'

  Given url baseUrl
  And path 'auth', 'register'
  And request
  """
  {
    "name": "Consultant Test User",
    "email": "#(uniqueEmail)",
    "password": "Password123"
  }
  """
  When method post
  * print 'consultant register status:', responseStatus
  * print 'consultant register response:', response
  Then assert responseStatus < 500

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
  * print 'consultant login status:', responseStatus
  * print 'consultant login response:', response
  Then assert responseStatus < 500
  * def token = response.token

Scenario: Get all consultants
  Given url baseUrl
  And path 'consultants'
  And header Authorization = 'Bearer ' + token
  When method get
  * print 'get consultants status:', responseStatus
  * print 'get consultants response:', response
  Then assert responseStatus < 500