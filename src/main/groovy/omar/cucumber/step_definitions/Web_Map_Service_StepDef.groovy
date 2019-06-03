package omar.cucumber.step_definitions

import geoscript.geom.MultiPolygon
import groovy.util.slurpersupport.GPathResult
import omar.cucumber.config.CucumberConfig
import omar.cucumber.ogc.util.FileCompare
import omar.cucumber.ogc.wfs.WFSCall
import omar.cucumber.ogc.wms.WMSCall
import omar.cucumber.ogc.wms.WMSGetCapabilities
import groovy.json.JsonSlurper
import javax.imageio.ImageIO
import omar.cucumber.ogc.util.TestImageInfo

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

config = CucumberConfig.config
def wmsServer = config.wmsServerProperty
def wfsServer = config.wfsServerProperty
def s3Bucket = config.s3Bucket
def s3BucketUrl = config.s3BucketUrl
def wmsReturnImage
def wmsGetCapabilitiesReturn
def serverVersion
def stitchImage1
def stitchImage2
Double elapsedTimeInSeconds

String imageData = "wms_data"

Given(~/^the needed image (.*) has been staged$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def filter = "entry_id='0' and filename LIKE '%${imageId}%'"

    def wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)

    def numFeatures = wfsCall.numFeatures
    assert numFeatures > 0
}

When(~/^a system calls WMS GetCapabilities version (.*)$/) { String version ->
    serverVersion = version
    def wmsGetCapabilities = new WMSGetCapabilities(wmsServer, version)
    wmsGetCapabilitiesReturn = wmsGetCapabilities.wmsGetCapabilitiesResult
}

When(~/^a call is made to Ortho WMS with an image type of (.*) for the entire bounding box of the image (.*)$/) {
    String imageType, String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def filter = "entry_id='0' and filename LIKE '%${imageId}%'"

    def wmsCall = new WMSCall()
    def bbox = wmsCall.getBBox(wfsServer, filter)

    wmsReturnImage = wmsCall.getImage(wmsServer, imageType, bbox, filter)
}

When(~/^a call is made to Ortho WMS with a 512 by 512 (.*) image for the entire bounding box of the image (.*)$/) {
    String imageType, String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def filter = "entry_id='0' and filename LIKE '%${imageId}%'"

    def wmsCall = new WMSCall()
    def bbox = wmsCall.getBBox(wfsServer, filter)

    wmsReturnImage = wmsCall.getImage(wmsServer, imageType, bbox, filter)
    elapsedTimeInSeconds = wmsCall.elapsedTime
}

When(~/^a call is made to Ortho WMS with an image type of (.*) for a 256 by 256 chip of the image (.*) at full resolution$/) {
    String imageType, String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def filter = "entry_id='0' and filename LIKE '%${imageId}%'"
    def wmsCall = new WMSCall()
    def bbox = wmsCall.getBBox(wfsServer, filter)

    wmsReturnImage = wmsCall.getImage(wmsServer, 256, 256, imageType, bbox, filter)
}

When(~/^a WMS call is made for the image (.*) and the image (.*)$/) {
    String image1, String image2 ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId1 = imageInfo.getImageInfo(image1,imageData).image_id
    String imageId2 = imageInfo.getImageInfo(image2,imageData).image_id

    def images = []
    [imageId1, imageId2].each {
        def wfsQuery = new WFSCall(wfsServer, "entry_id='0' and filename LIKE '%${it.trim()}%'", "JSON", 1)
        def json = new JsonSlurper().parseText(wfsQuery.text)
        images << json.features[0]
    }

    def bounds = images.collect { new MultiPolygon(it.geometry.coordinates).getBounds() }
    def bbox = [
            bounds.collect({ it.getMinX() }).min(),
            bounds.collect({ it.getMinY() }).min(),
            bounds.collect({ it.getMaxX() }).max(),
            bounds.collect({ it.getMaxY() }).max()
    ]

    def image1Url = new WMSCall().getImage(wmsServer, 512, 512, "png", bbox.join(","), "in(${images[0].properties.id})")
    stitchImage1 = ImageIO.read(image1Url)

    def image2Url = new WMSCall().getImage(wmsServer, 512, 512, "png", bbox.join(","), "in(${images[1].properties.id})")
    stitchImage2 = ImageIO.read(image2Url)

    def filter = "in(${images.collect({ it.properties.id }).join(",")})"
    def stitchedImageUrl = new WMSCall().getImage(wmsServer, 512, 512, "png", bbox.join(","), filter)
    stitchedImage = ImageIO.read(stitchedImageUrl)
}

Then(~/^a stitched image will be returned$/) { ->
    [stitchImage1, stitchImage2].each {
        def image = it

        def transparencyCount = 0
        (0..511).each {
            def x = it
            (0..511).each {
                def y = it
                def pixel = image.getRGB(x, y)
                def alpha = (pixel >> 24) & 0xff
                if (alpha == 0)
                {
                    transparencyCount++
                }
            }
        }

        // 40% of each stitch image should be transparent
        assert transparencyCount / (512 * 512) > 0.4
    }

    def transparencyCount = 0
    (0..511).each {
        def x = it
        (0..511).each {
            def y = it
            def pixel = stitchedImage.getRGB(x, y)
            def alpha = (pixel >> 24) & 0xff
            if (alpha == 0)
            {
                transparencyCount++
            }
        }
    }

    // the stitched image should be mostly filled in
    assert transparencyCount / (512 * 512) > 0 && transparencyCount / (512 * 512) < 0.05
}


Then(~/^the service returns the expected GetCapabilities response$/) { ->
    assert wmsGetCapabilitiesReturn instanceof GPathResult
}

Then(~/^Ortho WMS returns a (.*) that matches the validation image (.*)$/) {
    String imageType, String image ->

    verificationImageUrl = new URL("${s3BucketUrl}/${s3Bucket}/WMS_verification_images/${image}.${imageType}")
    assert FileCompare.checkImages(verificationImageUrl, wmsReturnImage, imageType)
}

Then(~/Ortho WMS returns a jpeg in less than (.*) seconds$/) { Double seconds ->
    assert elapsedTimeInSeconds <= seconds
}
