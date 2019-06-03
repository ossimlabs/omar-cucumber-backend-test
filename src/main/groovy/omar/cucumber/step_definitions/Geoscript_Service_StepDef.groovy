package omar.cucumber.step_definitions

import groovy.json.JsonSlurper
import org.apache.groovy.json.internal.LazyMap
import omar.cucumber.config.CucumberConfig
import omar.cucumber.ogc.geoscript.GeoscriptGetCapabilitiesData

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

config = CucumberConfig.config
def geoscriptService = config.geoscriptService
def defaultMax = config.geoscriptDefaultMax

def gsGetCapabilitiesReturn
def gsGetSchemaInfoByTypeNameReturn
def gsQueryLayerReturn
def httpStatus

String urlParamsToString(HashMap urlParams)
{
    urlParams.collect() { k, v -> "${k}=${v}" }.join("&")
}

// Scenario [GSS-01]
When(~/^a call is made to the Geoscript service GetCapabilitiesData$/) { ->
    gsGetCapabilitiesReturn = new GeoscriptGetCapabilitiesData(geoscriptService).GSGetCapabilitiesDataResult
}

// Scenario [GSS-02]
When(~/^a call is made to the Geoscript service GetSchemaInfoByTypeName with the TypeName (.*)$/) { String typeName ->
    HashMap params = [typeName: typeName]

    URL geoscriptUrl = new URL("${geoscriptService}/getSchemaInfoByTypeName?${urlParamsToString(params)}")
    gsGetSchemaInfoByTypeNameReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-03]
When(~/^a call is made to the Geoscript service GetSchemaInfoByTypeName with an invalid TypeName$/) { ->
    httpStatus = null

    HttpURLConnection geoscriptConnection = new URL("${geoscriptService}/getSchemaInfoByTypeName?typeName=ashdfjkasdhfaeuifhsdkdv").openConnection()
    httpStatus = geoscriptConnection.getResponseCode()

    assert httpStatus != null
}

// Scenario [GSS-04]
When(~/^a call is made to the Geoscript service QueryLayer with a resultType (.*) and featureFormat (.*)$/) {
    String resultType, String featureFormat ->
        HashMap params = [
                typeName     : "omar:raster_entry",
                resultType   : resultType,
                featureFormat: featureFormat,
                max          : defaultMax
        ]

        URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
        gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-05]
When(~/^a call is made to the Geoscript service QueryLayer requesting a JSON sorted by (.*)$/) { String field ->
    HashMap params = [
            typeName     : "omar:raster_entry",
            resultType   : "results",
            featureFormat: "JSON",
            sort         : field,
            max          : defaultMax
    ]

    URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
    gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-06]
When(~/^a call is made to the Geoscript service QueryLayer with an invalid sort parameter$/) { ->
    httpStatus = null

    HttpURLConnection geoscriptConnection = new URL("${geoscriptService}/queryLayer?typeName=omar:raster_entry&resultType=results&featureFormat=JSON&sort=asdfsdfasdf").openConnection()
    httpStatus = geoscriptConnection.getResponseCode()

    assert httpStatus != null
}

// Scenario [GSS-07]
When(~/^a call is made to the Geoscript service QueryLayer requesting a JSON with the field (.*)$/) { String field ->
    HashMap params = [
            typeName     : "omar:raster_entry",
            resultType   : "results",
            featureFormat: "JSON",
            fields       : field,
            max          : defaultMax
    ]

    URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
    gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-08]
When(~/^a call is made to the Geoscript service QueryLayer requesting a JSON with the fields (.*) (.*) (.*) (.*)$/) {
    String field1, String field2, String field3, String field4 ->
        String field = "${field1},${field2}"
        if (field3 != "") field = "${field},${field3}"
        if (field4 != "") field = "${field},${field4}"
        HashMap params = [
                typeName     : "omar:raster_entry",
                resultType   : "results",
                featureFormat: "JSON",
                fields       : field,
                max          : defaultMax
        ]

        URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
        gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-09]
When(~/^a call is made to the Geoscript service QueryLayer requesting a JSON with a max of (.*)$/) { String max ->
    HashMap params = [
            typeName     : "omar:raster_entry",
            resultType   : "results",
            featureFormat: "JSON",
            max          : max
    ]

    URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
    gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-10]
When(~/^a call is made to the Geoscript service QueryLayer requesting a JSON with the filter (.*)$/) { String filter ->
    HashMap params = [
            typeName     : "omar:raster_entry",
            resultType   : "results",
            featureFormat: "JSON",
            filter       : filter,
            max          : defaultMax
    ]

    URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
    gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-11]
When(~/^a call is made to the Geoscript service QueryLayer with an invalid filter$/) { ->
    httpStatus = null

    HttpURLConnection geoscriptConnection = new URL("${geoscriptService}/queryLayer?typeName=omar:raster_entry&resultType=results&featureFormat=JSON&filter=asdfsdfasdf").openConnection()
    httpStatus = geoscriptConnection.getResponseCode()

    assert httpStatus != null
}

// Scenario [GSS-12]
When(~/^a call is made to the Geoscript service QueryLayer with an invalid parameter name$/) { ->
    HashMap params = [
            typeName         : "omar:raster_entry",
            resultType       : "results",
            featureFormat    : "JSON",
            justForYouScottie: "Try_to_deal_with_this_one_Scottie",
            max              : defaultMax
    ]

    URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
    gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-13]
When(~/^a call is made to the Geoscript service QueryLayer with an invalid resultType$/) { ->
    HashMap params = [
            typeName     : "omar:raster_entry",
            resultType   : "ThisIsFoobar",
            featureFormat: "JSON"
    ]

    URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
    gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-14]
When(~/^a call is made to the Geoscript service QueryLayer with an invalid featureFormat$/) { ->
    HashMap params = [
            typeName     : "omar:raster_entry",
            resultType   : "results",
            featureFormat: "500ErrorsForEveryone"
    ]

    URL geoscriptUrl = new URL("${geoscriptService}/queryLayer?${urlParamsToString(params)}")
    println "Query URL: ${geoscriptUrl}"
    gsQueryLayerReturn = new JsonSlurper().parse(geoscriptUrl)
}

// Scenario [GSS-01]
Then(~/^the Geoscript service returns with the correct GetCapabilitiesData statement$/) { ->
    assert gsGetCapabilitiesReturn instanceof LazyMap
}

// Scenario [GSS-02]
Then(~/^the Geoscript service returns with the correct JSON$/) { ->
    assert gsGetSchemaInfoByTypeNameReturn instanceof LazyMap
}

// Scenario [GSS-03], [GSS-06], & [GSS-11]
Then(~/^the service returns the proper error$/) { ->
    // should expect 400, not 500, but for now we will accept any type of error
    assert (httpStatus >= 400)
    assert (httpStatus <= 600)
}

// Scenario [GSS-04]
Then(~/^the service returns a (.*) containing the (.*)$/) {
    String featureFormat, String resultType ->
        assert gsQueryLayerReturn instanceof LazyMap
}

// Scenario [GSS-05]
Then(~/^the service returns a JSON sorted by (.*)$/) { String field ->

    boolean fieldIsString = false
    String previous, current
    int prev, curr

    if (gsQueryLayerReturn.get("features").get(0).get("properties").get(field) instanceof String)
    {
        previous = gsQueryLayerReturn.get("features").get(0).get("properties").get(field)
        fieldIsString = true
    }
    else
    {
        if (gsQueryLayerReturn.get("features").get(0).get("properties").get(field) != null)
        {
            prev = gsQueryLayerReturn.get("features").get(0).get("properties").get(field)
        }
        else
        {
            prev = 2147483647
        }
        fieldIsString = false
    }

    for (int i = 1; i < gsQueryLayerReturn.get("features").size(); ++i)
    {
        if (fieldIsString)
        {
            current = gsQueryLayerReturn.get("features").get(i).get("properties").get(field)
            if (current != null && previous != null) {
                // Special case for filenames since geoscript sorts differently then simple comparing ASCII strings.
                // Lower case to match geoscript's alphabetical sorting.
                // Replace double slash with slash to match geoscript's sorting with filenames containing double slash.
                current = current.toLowerCase().replace("//", "/")
                previous = previous.toLowerCase().replace("//", "/")
                assert current >= previous
            }
            previous = current
        }
        else
        {
            if (gsQueryLayerReturn.get("features").get(i).get("properties").get(field) != null)
            {
                curr = gsQueryLayerReturn.get("features").get(i).get("properties").get(field)
            }
            else
            {
                curr = 2147483647
            }
            assert prev <= curr
            prev = curr
        }
    }

    assert gsQueryLayerReturn instanceof LazyMap
}

// Scenario [GSS-07]
Then(~/^the service returns a JSON with only the field (.*)$/) { String field ->

    for (int i = 0; i < gsQueryLayerReturn.get("features").size(); ++i)
    {
        Set<String> keys = gsQueryLayerReturn.get("features").get(i).get("properties").keySet()
        assert (keys.size() == 1 && keys.contains(field)) || keys.size() == 0
    }

    assert gsQueryLayerReturn instanceof LazyMap
}

// Scenario [GSS-08]
Then(~/^the service returns a JSON with only the fields (.*) (.*) (.*) (.*)$/) {
    String field1, String field2, String field3, String field4 ->
        assert gsQueryLayerReturn instanceof LazyMap

        for (int i = 0; i < gsQueryLayerReturn.get("features").size(); ++i)
        {
            int numOfMatchedKeys = 0
            Set<String> keys = gsQueryLayerReturn.get("features").get(i).get("properties").keySet()

            if (keys.contains(field1)) ++numOfMatchedKeys
            if (keys.contains(field2)) ++numOfMatchedKeys
            if (keys.contains(field3)) ++numOfMatchedKeys
            if (keys.contains(field4)) ++numOfMatchedKeys

            assert keys.size() == numOfMatchedKeys
        }
}

// Scenario [GSS-09]
Then(~/^the service returns no more than (.*) features$/) { String max ->
    assert gsQueryLayerReturn.get("numberOfFeatures") <= Integer.valueOf(max)
    assert gsQueryLayerReturn instanceof LazyMap
}

// Scenario [GSS-10]
Then(~/^the service returns the proper JSON$/) { ->
    assert gsQueryLayerReturn instanceof LazyMap
}
