package omar.cucumber.ogc.imagespace

import omar.cucumber.ogc.wfs.WFSCall

class ImageSpaceCall
{
    URL imageSpaceUrl
    String imageSpaceUrlString

    ImageSpaceCall() {}

    URL getImage(imageSpaceServer, wfsServer, imageID, tile_size = 256, return_image_type, x, y, z, bands = "default", numBands = 1, histOp = "auto-minmax")
    {

        def filter = "entry_id='0' and filename LIKE '%${imageID}%'"
        def wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
        String wfsFilename = wfsCall.getFilename()

        HashMap imageSpaceParams = [
                filename    : "${wfsFilename}",
                tileSize    : "${tile_size}",
                outputFormat: "${return_image_type}",
                x           : "${x}",
                y           : "${y}",
                z           : "${z}",
                numOfBands  : "${numBands}",
                bands       : "${bands}",
                histOp      : "${histOp}",
                numResLevels: "${wfsCall.getNumberOfResLevels()}",
                width       : "${wfsCall.getWidth()}",
                height      : "${wfsCall.getHeight()}",
                entry       : "0"
        ]

        String imageSpaceParamsString = urlParamsToString(imageSpaceParams)
        imageSpaceUrlString = "${imageSpaceServer}/getTile?${imageSpaceParamsString}"
        println "getImage URL: ${imageSpaceUrlString}"

        imageSpaceUrl = new URL(imageSpaceUrlString)

        imageSpaceUrl
    }//end getImage

    URL getThumbnail(imageSpaceServer, wfsServer, imageID, size = 256, return_image_type)
    {

        def filter = "entry_id='0' and filename LIKE '%${imageID}%'"
        WFSCall wfsCall = new WFSCall(wfsServer, filter, "JSON", 1)
        String wfsFilename = wfsCall.getFilename()

        HashMap imageSpaceParams = [
                filename    : "${wfsFilename}",
                size        : "${size}",
                outputFormat: "${return_image_type}",
                entry       : "0"
        ]

        String imageSpaceParamsString = urlParamsToString(imageSpaceParams)
        imageSpaceUrlString = "${imageSpaceServer}/getThumbnail?${imageSpaceParamsString}"

        imageSpaceUrl = new URL(imageSpaceUrlString)

        imageSpaceUrl
    }//end getThumbnail

    static String urlParamsToString(HashMap urlParams)
    {
        urlParams.collect() { k, v -> "${k}=${v}" }.join("&")
    }//end urlParamsToString
}
