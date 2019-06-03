package omar.cucumber.step_definitions

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import org.apache.commons.io.FileUtils
import omar.cucumber.config.CucumberConfig
import omar.cucumber.ogc.util.FileCompare
import omar.cucumber.ogc.wfs.WFSCall
import omar.cucumber.ogc.util.TestImageInfo

import java.nio.charset.Charset

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)

String defaultCharset = Charset.defaultCharset().displayName()

String imageData = "download_data"

def httpResponse

config = CucumberConfig.config
def downloadService = config.downloadService
def stagingService = config.stagingService
def wfsServer = config.wfsServerProperty
def s3BucketUrl = config.s3BucketUrl

Given(~/^that the download service is running$/) { ->
    def healthText = new URL("${downloadService}/health").text
    def healthJson = new JsonSlurper().parseText(healthText)
    assert healthJson.status == "UP"
}

When(~/^we download the image (.*)$/) { String image ->
        println "we download image: $image"
        TestImageInfo imageInfo = new TestImageInfo()
        String imageId = imageInfo.getImageInfo(image,imageData).image_id
        String imageFileName = validFileName(imageId)

        assert imageFileName != null && imageFileName != "/"
        def feature = fetchWfsFeaturesForImageId(imageFileName)
        assert feature != null
        String rasterFiles = fetchSupportingFilesForFeature(feature)
        def downloadRequestOptions = getPostDataForDownloadRequest(imageFileName, rasterFiles)
        downloadImageFile(imageFileName, downloadRequestOptions)
}

When(~/^the download service is called without a json message$/) { ->
    def command = curlDownloadCommand(null, "")

    def stdOut = new StringBuilder()
    def stdError = new StringBuilder()
    def process = command.execute()
    process.consumeProcessOutput(stdOut, stdError)
    process.waitFor()

    httpResponse = new JsonSlurper().parseText(stdOut.toString())

    assert httpResponse != null
}

When(~/^the download service is called with no fileGroups specified in the json$/) { ->
    def map = [
            type          : "Download",
            zipFileName   : "",
            archiveOptions: [type: "zip"],
            fileGroups    : []
    ]
    def jsonPost = JsonOutput.toJson(map)

    def command = curlDownloadCommand(null, jsonPost)

    def stdOut = new StringBuilder()
    def stdError = new StringBuilder()
    def process = command.execute()
    process.consumeProcessOutput(stdOut, stdError)
    process.waitFor()

    httpResponse = new JsonSlurper().parseText(stdOut.toString())


    assert httpResponse != null
}

When(~/^the download service is called with the wrong archive type$/) { ->
    def map = [
            type          : "Download",
            zipFileName   : "",
            archiveOptions: [type: ""],
            fileGroups    : [
                    [
                            rootDirectory: "",
                            files        : ["", ""]
                    ]
            ]
    ]
    def jsonPost = JsonOutput.toJson(map)

    def command = curlDownloadCommand(null, jsonPost)

    def stdOut = new StringBuilder()
    def stdError = new StringBuilder()
    def process = command.execute()
    process.consumeProcessOutput(stdOut, stdError)
    process.waitFor()

    httpResponse = new JsonSlurper().parseText(stdOut.toString())


    assert httpResponse != null
}

Then(~/^the file (.*) should exist$/) { String image ->
        TestImageInfo imageInfo = new TestImageInfo()
        String imageId = imageInfo.getImageInfo(image,imageData).image_id
        String imageFileName = validFileName(imageId)

        assert imageFileName != null && imageFileName != "/"
        File imageFile = new File(imageFileName)
        assert imageFile.exists()
        println "Image file: $imageFile"
}

Then(~/^the downloaded file (.*) matches the validation of S3 file (.*)/) {
    String image, String validationImagePath ->

        TestImageInfo imageInfo = new TestImageInfo()
        String imageId = imageInfo.getImageInfo(image,imageData).image_id
        String imageFileName = validFileName(imageId)

        URL verificationImageUrl = new URL("${s3BucketUrl}/$validationImagePath")
        compareLocalImageToUrl(new File(imageFileName), verificationImageUrl)
}

Then(~/^the response should return a status of (\d+) and a message of "(.*)"$/) { int statusCode, String message ->
    println "Response should have status $statusCode and message '$message': $httpResponse"
    assert httpResponse.status == statusCode && httpResponse.message == message
}

def fetchWfsFeaturesForImageId(String imageId) {
    String geoscriptFilter = "filename LIKE '%${imageId}%'"
    def wfsQuery = new WFSCall(config.wfsServerProperty, geoscriptFilter, "JSON", 1)
    return wfsQuery.result.features
}

def fetchWfsFeaturesForFileName(String fileName) {
    String geoscriptFilter = "filename = $fileName"
    def wfsQuery = new WFSCall(config.wfsServerProperty, geoscriptFilter, "JSON", 1)
    return wfsQuery.result.features
}

def fetchSupportingFilesForFeature(def feature) {
    def rasterFilesUrl = config.stagingService + "/getRasterFiles?id=${feature["properties"]["id"][0]}"
    def rasterFilesText = new URL(rasterFilesUrl).getText()
    return new JsonSlurper().parseText(rasterFilesText).results
}

String getPostDataForDownloadRequest(String imageFileName, String rasterFiles) {
    return JsonOutput.toJson([
            type          : "Download",
            zipFileName   : imageFileName+".zip",
            archiveOptions: [type: "zip"],
            fileGroups    : [
                    [
                            rootDirectory: "",
                            files        : rasterFiles
                    ]
            ]
    ])
}

List<String> curlDownloadCommand(String fileName = null, String fileInfo = null) {
    List<String> command = ["curl", "-L", "${config.downloadService}/archive/download"]

    // Callers may want to output to stdout.
    if (fileName != null) command.addAll(1, ["-o", "${validFileName(fileName)}"])

    // An empty string for 'fileInfo' is invalid but is used in tests.
    if (fileInfo != null) command.addAll(1,
            ["-d", "fileInfo=${URLEncoder.encode(fileInfo, Charset.defaultCharset().displayName())}"]
    )

    // Necessary to optionally support authentication
    if (config.curlOptions) command.addAll(1, config.curlOptions)
    println "Using curl command: '${command.join(" ")}'"
    return command
}

String validFileName(String imageId) {
    return imageId.replace('/', '_').replace('\\', '_')
}

void downloadImageFile(String imageFileName, String fileInfo) {
    def command = curlDownloadCommand(imageFileName, fileInfo)

    def stdOut = new StringBuilder()
    def stdErr = new StringBuilder()
    def process = command.execute()
    process.consumeProcessOutput(stdOut, stdErr)
    process.waitFor()
}

boolean compareLocalImageToUrl(File localImageFile, URL imageUrl, image_type = null) {
    String suffix = image_type ? ".${image_type}" : ""
    File imageUrlFile = File.createTempFile("tempImage", suffix)

    FileUtils.copyURLToFile(imageUrl, imageUrlFile)

    boolean imagesEqual = FileUtils.contentEquals(localImageFile, imageUrlFile)

    imageUrlFile.deleteOnExit()
    return imagesEqual
}
