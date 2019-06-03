package omar.cucumber.step_definitions

import omar.cucumber.config.CucumberConfig
import omar.cucumber.ogc.wfs.WFSCall
import omar.cucumber.ogc.util.TestImageInfo

import java.nio.charset.Charset

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

String defaultCharset = Charset.defaultCharset().displayName()

String imageData = "superoverlay_data"

config = CucumberConfig.config
def wfsServer = config.wfsServerProperty
def superOverlayService = config.superOverlayProperty

Given(~/^the image (.*) has been staged$/) {
    String image ->

        TestImageInfo imageInfo = new TestImageInfo()
        String imageId = imageInfo.getImageInfo(image,imageData).image_id

        String filter = "entry_id='0' and filename LIKE '%${imageId}%'"

        WFSCall wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)

        def numFeatures = wfsCall.numFeatures
        assert numFeatures > 0
}

When(~/^the superoverlay service is called to download a KML super-overlay of the image (.*)$/) {
    String image ->

        // Fetch databaseId for the image
        TestImageInfo imageInfo = new TestImageInfo()
        String imageId = imageInfo.getImageInfo(image,imageData).image_id
        def filter = "filename LIKE '%${imageId}%'"
        def wfsCall = new WFSCall(wfsServer, filter, "JSON", 2)
        int databaseId = wfsCall.result.features[0].properties.id

        // Fetch KML
        URL superOverlayUrl = new URL("${superOverlayService}/createKml/$databaseId")
        httpResponse = superOverlayUrl.text

        // Make sure the call was made without error
        assert httpResponse.contains("xml") && httpResponse.contains("kml")
}

Then(~/^the service returns a KML file for the image (.*)$/) {
    String image ->
        def xml = new XmlSlurper().parseText(httpResponse)

        // xml node <document><name>...</name></document> should contain the image Id.
        TestImageInfo imageInfo = new TestImageInfo()
        String imageId = imageInfo.getImageInfo(image,imageData).image_id
        assert xml.Document.name.toString().contains(imageId.toString())
}