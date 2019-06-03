package omar.cucumber.ogc.geoscript

import groovy.json.JsonSlurper

class GeoscriptGetCapabilitiesData
{
    def GSGetCapabilitiesDataResult

    GeoscriptGetCapabilitiesData() {}

    GeoscriptGetCapabilitiesData(geoscriptServer)
    {
        URL geoscriptUrl = new URL("${geoscriptServer}/getCapabilitiesData")
        GSGetCapabilitiesDataResult = new JsonSlurper().parse(geoscriptUrl)
    } // end of GeoscriptGetCapabilitiesData

    String getResult()
    {
        GSGetCapabilitiesDataResult
    } // end of getResult
}