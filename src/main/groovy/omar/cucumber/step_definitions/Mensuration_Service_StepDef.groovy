package omar.cucumber.step_definitions

import groovy.json.JsonOutput
import groovy.json.JsonSlurper
import omar.cucumber.config.CucumberConfig
import omar.cucumber.ogc.util.TestImageInfo

import omar.cucumber.ogc.wfs.WFSCall

this.metaClass.mixin(cucumber.api.groovy.Hooks)
this.metaClass.mixin(cucumber.api.groovy.EN)


config = CucumberConfig.config
def mensaUrl = config.mensaUrl
def wfsServer = config.wfsServerProperty

String imageData = "mensa_data"

def area
def distance

Then(~/^the area of that polygon is returned$/) { ->
    assert area == 73694.98382206976
}

Then(~/^the distance between those two points is returned$/) { ->
    assert distance == 395.045396278192
}

Then(~/^the path distance between those four points is returned$/) { ->
    assert distance == 739.5000363888523
}

When(~/^the mensuration service is called with two points on the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def wfsQuery = new WFSCall(wfsServer, "filename LIKE '%${imageId}%'", "JSON", 1)

    def postBody = [
            entryId  : 0,
            filename : wfsQuery.filename,
            pointList: "LINESTRING(14003.649810791016 15219.103088378904, 14774.048736572266 15271.243347167967)"
    ]

    def command = ["curl",
                    "-X",
                    "POST",
                    "-d",
                    JsonOutput.toJson(postBody),
                    "-H",
                    "Content-Type: application/json",
                    "${mensaUrl}/mensa/imageDistance"
    ]
    /*
    add an ArrayList called curlOptions to the config file if
    addition info needs to be added to the curl command.
    */
    if (config?.curlOptions)
    {
    command.addAll(1, config.curlOptions)
    }
    def process = command.execute()
    process.waitFor()
    def text = process.getText()
    def json = new JsonSlurper().parseText(text)

    distance = json.data.distance


    assert "${distance}".isNumber()
}

When(~/^the mensuration service is called with four points on the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def wfsQuery = new WFSCall(wfsServer, "filename LIKE '%${imageId}%'", "JSON", 1)

    def postBody = [
            entryId  : 0,
            filename : wfsQuery.filename,
            pointList: "LINESTRING(14497.652160644531 14804.64123535156, 14776.442932128906 15281.35217285156, 14446.575988769531 15468.63146972656, 14006.044006347656 15211.122436523436)"
    ]

    def command = ["curl",
                    "-X",
                    "POST",
                    "-d",
                    JsonOutput.toJson(postBody),
                    "-H",
                    "Content-Type: application/json",
                    "${mensaUrl}/mensa/imageDistance"
    ]
    /*
    add an ArrayList called curlOptions to the config file if
    addition info needs to be added to the curl command.
    */
    if (config?.curlOptions)
    {
    command.addAll(1, config.curlOptions)
    }
    def process = command.execute()
    process.waitFor()
    def text = process.getText()
    def json = new JsonSlurper().parseText(text)

    distance = json.data.distance


    assert "${distance}".isNumber()
}

When(~/^the mensuration service is called with a polygon of nine points on the image (.*)$/) { String image ->

    TestImageInfo imageInfo = new TestImageInfo()
    String imageId = imageInfo.getImageInfo(image,imageData).image_id

    def wfsQuery = new WFSCall(wfsServer, "filename LIKE '%${imageId}%'", "JSON", 1)

    def postBody = [
            entryId  : 0,
            filename : wfsQuery.filename,
            pointList: "POLYGON((14499.780334472656 14804.64123535156, 14772.186584472656 15272.83947753906, 14440.191467285156 15466.503295898436, 14276.322082519531 15387.76086425781, 14225.245910644531 15332.42834472656, 14125.221740722656 15345.19738769531, 14012.428527832031 15211.122436523436, 14214.605041503906 14962.12609863281, 14365.705383300781 14851.46105957031, 14499.780334472656 14804.64123535156))"
    ]

    def command = ["curl",
                    "-X",
                    "POST",
                    "-d",
                    JsonOutput.toJson(postBody),
                    "-H",
                    "Content-Type: application/json",
                    "${mensaUrl}/mensa/imageDistance"
    ]
    /*
    add an ArrayList called curlOptions to the config file if
    addition info needs to be added to the curl command.
    */
    if (config?.curlOptions)
    {
    command.addAll(1, config.curlOptions)
    }
    def process = command.execute()
    process.waitFor()
    def text = process.getText()
    def json = new JsonSlurper().parseText(text)

    area = json.data.area


    assert "${area}".isNumber()
}
