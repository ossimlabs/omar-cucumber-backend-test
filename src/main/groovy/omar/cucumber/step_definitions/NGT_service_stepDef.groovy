package omar.cucumber.step_definitions

import groovy.json.JsonSlurper
import org.apache.groovy.json.internal.LazyMap
import omar.cucumber.config.CucumberConfig

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

// Configuration params
config = CucumberConfig.config
def ngtService = config.ngtService

// Params passed along steps go here
String ngtServiceResponse

/** GIVENS */

// NGT-01
Given(~/^that the NGT service is running$/) { ->
    def httpStatus = null

    HttpURLConnection ngtConnection = new URL("${ngtService}").openConnection()
    httpStatus = ngtConnection.getResponseCode()
    ngtConnection.disconnect()

    assert httpStatus==200
}



/** WHENS */

// NGT-01
When(~/^a call is made to the (.*) with the (.*) to the NGT service$/) { String serviceName, String parameters ->
    def httpStatus = null

    // Just ping the site to start the request
    HttpURLConnection ngtConnection = new URL("${ngtService}/stereo?app=${serviceName}&args=${parameters}").openConnection()
    httpStatus = ngtConnection.getResponseCode()
    ngtConnection.disconnect()

    assert httpStatus==200
}



/** THENS */

// NGT-01
Then(~/^(.*) call should appear in the NGT-service log table with (.*) to the NGT service$/) { String serviceName, String parameters ->
	URL ngtLog = new URL("${ngtService}/index")

	ngtServiceResponse = ngtLog.getText()

assert ngtServiceResponse.contains(serviceName)
assert ngtServiceResponse.contains(parameters)
}