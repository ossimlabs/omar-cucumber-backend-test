package omar.cucumber.ogc.wfs

class WFSGetCapabilities {
    def wfsGetCapabilitiesResult
    WFSGetCapabilities() {}
    WFSGetCapabilities(wfsServer) {

        def wfsUrl = "${wfsServer}/getCapabilities?service=WFS&version=1.1.0&request=GetCapabilities"
        wfsGetCapabilitiesResult = new XmlSlurper().parse(wfsUrl)
    }

    String getResult() {
        wfsGetCapabilitiesResult
    }
}
