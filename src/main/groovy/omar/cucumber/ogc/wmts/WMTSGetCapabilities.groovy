package omar.cucumber.ogc.wmts

class WMTSGetCapabilities
{
    def wmtsGetCapabilitiesResult

    WMTSGetCapabilities(){}

    WMTSGetCapabilities(wmtsServer, version)
    {
        def wmtsUrl = "${wmtsServer}/getCapabilities?service=WMTS&version=${version}&request=GetCapabilities"
        wmtsGetCapabilitiesResult = new XmlSlurper().parse(wmtsUrl)
    } // end of WMTGetCapabilities

    String getResult()
    {
        wmtsGetCapabilitiesResult
    } // end of getResult
}