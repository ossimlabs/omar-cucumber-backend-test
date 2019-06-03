package omar.cucumber.step_definitions

import org.apache.commons.io.FileUtils
import omar.cucumber.config.CucumberConfig
import omar.cucumber.ogc.util.FileCompare

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

//Config
config = CucumberConfig.config
def s3Bucket = config.s3Bucket
def s3BucketUrl = config.s3BucketUrl
def s3BasemapVerificationFiles = config.s3BasemapVerificationFiles
def s3BasemapUrlList = config.s3BasemapUrlList

// The Urls for the request of all images in the test set
def wmsProxyUrls
File RequestURLFile

// The three test images
File test1
File test2
File test3

File verificationImage

/**
 * WHENs
 */

// Scenario [O2BM-01]
When(~/^a call is made to a basemap (.*)$/) { String imageNum ->

    wmsProxyUrls = new URL("${s3BucketUrl}/${s3Bucket}/${s3BasemapVerificationFiles}/${s3BasemapUrlList}")
    RequestURLFile = File.createTempFile("Request", ".txt")
    FileUtils.copyURLToFile(wmsProxyUrls, RequestURLFile)
    RequestURLFile.deleteOnExit()
    def verificationImageUrl = new URL("${s3BucketUrl}/${s3Bucket}/${s3BasemapVerificationFiles}/image_${imageNum}.jpeg")
    verificationImage = File.createTempFile("tempImage", ".jpeg")
    FileUtils.copyURLToFile(verificationImageUrl, verificationImage)
    verificationImage.deleteOnExit()

    def testImageUrl = new URL(RequestURLFile.readLines().get(Integer.parseInt(imageNum) - 1))
    test1 = File.createTempFile("tempTestImage1", ".jpeg")
    FileUtils.copyURLToFile(testImageUrl, test1)
    test1.deleteOnExit()

}

// Scenario [O2BM-02]
When(~/^multiple calls are made to a basemap (.*)$/) { String imageNum ->

    def verificationImageUrl = new URL("${s3BucketUrl}/${s3Bucket}/${s3BasemapVerificationFiles}/image_${imageNum}.jpeg")
    verificationImage = File.createTempFile("tempImage", ".jpeg")
    FileUtils.copyURLToFile(verificationImageUrl, verificationImage)
    verificationImage.deleteOnExit()

    def testImageUrl = new URL(RequestURLFile.readLines().get(Integer.parseInt(imageNum) - 1))
    test1 = File.createTempFile("tempTestImage1", ".jpeg")
    FileUtils.copyURLToFile(testImageUrl, test1)
    test1.deleteOnExit()

    testImageUrl = new URL(RequestURLFile.readLines().get(Integer.parseInt(imageNum) - 1))
    test2 = File.createTempFile("tempTestImage2", ".jpeg")
    FileUtils.copyURLToFile(testImageUrl, test2)
    test2.deleteOnExit()

    testImageUrl = new URL(RequestURLFile.readLines().get(Integer.parseInt(imageNum) - 1))
    test3 = File.createTempFile("tempTestImage3", ".jpeg")
    FileUtils.copyURLToFile(testImageUrl, test3)
    test3.deleteOnExit()

}

/**
 * THENs
 */

// Scenario [O2BM-01]
Then(~/^a (.*) is returned$/) { String imageNum ->
    assert FileCompare.checkImages(verificationImage, test1)
}

// Scenario [O2BM-02]
Then(~/^an (.*) is returned all times$/) { String imageNum ->
    assert FileCompare.checkImages(verificationImage, test1)
    assert FileCompare.checkImages(verificationImage, test2)
    assert FileCompare.checkImages(verificationImage, test3)
}
