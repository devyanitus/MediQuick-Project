Feature: Auth API Tests

Background:
* url baseUrl

Scenario: Register user
Given path '/auth/register'
And request { name: 'Test User', email: 'test@test.com', password: '123456' }
When method POST
Then status 200

Scenario: Login user
Given path '/auth/login'
And request { email: 'test@test.com', password: '123456' }
When method POST
Then status 200
And match response.token != null