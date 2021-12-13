@TheDemoApiForOnboarding
Feature: The Demo Api For Onboarding

	Background:
		Given def PropertyHelper = Java.type('com.hotstar.frameworks.endgame.helper.PropertyHelper')
		And def props = new PropertyHelper().getProperties(karate.properties['propFile'])
		And url props['baseUrl']

	@200
	Scenario: Hello hotstar bootcamp example
		Given path '/healthy'
		When method get
		And print response
		Then status 200
