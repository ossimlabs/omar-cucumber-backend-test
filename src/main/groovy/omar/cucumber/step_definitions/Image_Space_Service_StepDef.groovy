package omar.cucumber.step_definitions

import cucumber.api.Scenario
import omar.cucumber.config.CucumberConfig
import omar.cucumber.ogc.imagespace.ImageSpaceCall
import omar.cucumber.ogc.util.FileCompare
import omar.cucumber.ogc.util.TestImageInfo

/**
 * Created by kfeldbush on 8/10/16.
 */

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

config = CucumberConfig.config
def s3Bucket = config.s3Bucket
def s3BucketUrl = config.s3BucketUrl
def wfsServer = config.wfsServerProperty
def imageSpaceServer = config.imageSpaceServerProperty
def imageSpaceReturnImage

String imageData = "image_space_data"

Scenario scenario

private static boolean completesWithin(int timeLimitInMillis, Closure closure) {
    def startTime = System.currentTimeMillis()
    closure()
    def durationInMillis = System.currentTimeMillis() - startTime
    return durationInMillis < timeLimitInMillis
}

Before(){ theScenario ->
    scenario = theScenario
}

When(~/^a call is made to ImageSpace for a (.*) of the entire bounding box of the image (.*)$/) {
    String imageType, String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id
    
    def imageSpaceCall = new ImageSpaceCall()
    imageSpaceReturnImage = imageSpaceCall.getImage(imageSpaceServer, wfsServer, imageId, "256", imageType, "3", "0", "3")
}

Then(~/^ImageSpace returns a (.*) that matches the validation image (.*)$/) {
    String imageType, String image ->

    verificationImageUrl = new URL("${s3BucketUrl}/${s3Bucket}/ImageSpace_verification_images/${image}.${imageType}")
    assert FileCompare.checkImages(verificationImageUrl, imageSpaceReturnImage, imageType)
}

When(~/^a call is made to ImageSpace for a (.*) single tile overview of the image (.*)$/) {
    String imageType, String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def imageSpaceCall = new ImageSpaceCall()
    imageSpaceReturnImage = imageSpaceCall.getImage(imageSpaceServer, wfsServer, imageId, "256", imageType, "0", "0", "0")
}

When(~/^a call is made to ImageSpace with a time limit of (\d+) to get a (.*) thumbnail of the image (.*)$/) {
    String timeLimitInMillis, String imageType, String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id
    
    def imageSpaceCall = new ImageSpaceCall()

    assert completesWithin(timeLimitInMillis.toInteger()) {
        imageSpaceReturnImage = imageSpaceCall.getThumbnail(imageSpaceServer, wfsServer, imageId, "256", imageType)
    }
}

When(~/^a call is made to ImageSpace for an overview tile in red green blue order of the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def imageSpaceCall = new ImageSpaceCall()
    imageSpaceReturnImage = imageSpaceCall.getImage(imageSpaceServer, wfsServer, imageId, "256", "png", "0", "0", "0", "3,2,1", "3")
}
When(~/^a call is made to ImageSpace for an overview tile in green blue red order of the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def imageSpaceCall = new ImageSpaceCall()
    imageSpaceReturnImage = imageSpaceCall.getImage(imageSpaceServer, wfsServer, imageId, "256", "png", "0", "0", "0", "2,1,3", "3")
}
When(~/^a call is made to ImageSpace for an overview tile green band of the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def imageSpaceCall = new ImageSpaceCall()
    imageSpaceReturnImage = imageSpaceCall.getImage(imageSpaceServer, wfsServer, imageId, "256", "png", "0", "0", "0", "2", "1")
}
