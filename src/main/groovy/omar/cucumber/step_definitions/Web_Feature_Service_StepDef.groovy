package omar.cucumber.step_definitions

import groovy.json.JsonSlurper
import groovy.util.slurpersupport.GPathResult
import omar.cucumber.config.CucumberConfig

import omar.cucumber.ogc.wfs.WFSCall
import omar.cucumber.ogc.wfs.WFSGetCapabilities
import omar.cucumber.ogc.geoscript.Geoscript
import omar.cucumber.ogc.util.TestImageInfo

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

config = CucumberConfig.config
def wfsServer = config.wfsServerProperty
def wfsUrl = config.wfsUrl
def wfsGetCapabilitiesReturn
def wfsCall
def omarOldmarProxy = config.omarOldmarProxy

String imageData = "wfs_data"

Given(~/^image (.*) has been staged in the system$/) {
    String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def filter = "entry_id='0' and filename LIKE '%${imageId}%'"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
    numFeatures = wfsCall.getNumFeatures()
    assert numFeatures > 0
}

Given(~/^that the WFS service is available$/) { ->
    def url = new URL("${wfsUrl}/health")
    def text = url.getText()
    def json = new JsonSlurper().parseText(text)


    assert json.status == "UP"
}

Then(~/^system returns only image (.*)$/) { String imageID ->
    numFeatures = wfsCall.getNumFeatures()
    if (numFeatures > 0)
    {
        layer = wfsCall.getLayer()
    }
    else
    {
        assert numFeatures > 0
    }
    assert layer == 1
}

Then(~/^the WFS call returns metadata in a (.*) format$/) { String format ->
    def text = wfsCall.getResultText()
    boolean succeeded = false

    switch (format)
    {
        case "GEOJSON":
        case "JSON":
            try
            {
                def json = new JsonSlurper().parseText(text)
                succeeded = true
            }
            catch (e)
            {
                println e
            }
            break
        case "GML3":
        case "GML2":
        case "KML":
            try
            {
                def xml = new XmlSlurper().parseText(text)
                succeeded = true
            }
            catch (e)
            {
                println e
            }
            break
    }

    assert (succeeded)
}

Then(~/^the system returns a GetCapabilites statement that matches the expected WFSGetCapabilites output$/) { ->
    assert wfsGetCapabilitiesReturn instanceof GPathResult
}

Then(~/^the WFS call returns a feature for the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    for (int i = 0; i < wfsCall.getName().size(); i++)
    {
        if (wfsCall.getName().properties.title == imageId)
        {
            assert wfsCall.getName() == imageId
        }
    }
}

Then(~/^the WFS call will have the receive_date populated for the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    String filter = "title = '${imageId}'"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
    def props = wfsCall.getFeature()?.properties
    assert props?.receive_date != null
}


When(~/^a WFS call is made to search for an image specifying a BE Number of (.*)$/) { String beNumber ->
    def filter = "be_number LIKE '%${beNumber}%'"
}

When(~/^a WFS call is made to search for an image specifying a bounding box (.*), (.*), (.*), (.*)$/) { double minLon, double minLat, double maxLon, double maxLat ->
    def filter = "BBOX(ground_geom, ${minLon},${minLat},${maxLon},${maxLat})"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a bounding polygon (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*), (.*)$/) { double lat1, double lon1, double lat2, double lon2, double lat3, double lon3, double lat4, double lon4, double lat5, double lon5 ->
    def filter = "INTERSECTS(ground_geom, POLYGON((${lat1} ${lon1},${lat2} ${lon2},${lat3} ${lon3},${lat4} ${lon4},${lat5} ${lon5})))"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a Country Code of (.*)$/) { String countryCode ->
    def filter = "country_code LIKE '%${countryCode}%'"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a Date Range of (.*) to (.*)$/) { String startDate, String endDate ->
    def filter = "acquisition_date >= '${startDate}' AND acquisition_date <= '${endDate}'"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a Date Range from (.*) to (.*) and Country Code of (.*)$/) { String startDate, String endDate, String countryCode ->
    def filter = "acquisition_date >= '${startDate}' AND acquisition_date <= '${endDate}' AND country_code LIKE '${countryCode}'"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a point and radius (.*), (.*), (\d+)$/) { double lat, double lon, int radius ->
    def buildPolygon = new Geoscript()
    def polygon = buildPolygon.createPolygon(lon, lat, radius)
    def filter = "INTERSECTS(ground_geom, ${polygon})"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a single point (.*), (.*)$/) { double lat, double lon ->
    def filter = "INTERSECTS(ground_geom, POINT(${lat} ${lon}))"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a Sensor Type of (.*)$/) { String sensorType ->
    def filter = "sensor_id LIKE '%${sensorType}%'"
    wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to search for an image specifying a Target ID of (.*)$/) { String targetID ->
    def filter = "target_id LIKE '%${targetID}%'"
}

When(~/^a WFS call is made to search for the image (.*)$/) {
    String image ->

        TestImageInfo imageInfo = new TestImageInfo()
        String imageId = imageInfo.getImageInfo(image,imageData).image_id

        def filter = "entry_id='0' and title LIKE '${imageId}'"
        wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
}

When(~/^a WFS call is made to with an output format of (.*)$/) { String format ->
    def filter = ""
    wfsCall = new WFSCall(wfsServer, filter, format, 1)
}

When(~/^WFS GetCapabilities call is made$/) { ->
    def wfsGetCapabilities = new WFSGetCapabilities(wfsServer)
    wfsGetCapabilitiesReturn = wfsGetCapabilities.wfsGetCapabilitiesResult
}

When(~/^a WFS post to omar-oldmar with a filter in xml format is made$/){->
    wfsCall = new WFSCall();
    wfsCall.getFeaturePost("${omarOldmarProxy}/wfs".toString(), config.wfsPostString)
}
