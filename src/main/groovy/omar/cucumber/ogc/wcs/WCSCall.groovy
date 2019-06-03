package omar.cucumber.ogc.wcs

import groovy.util.slurpersupport.GPathResult
import omar.cucumber.ogc.wms.WMSCall

import java.nio.charset.Charset

/**
 * Created by stephenlallier on 5/18/17.
 */
class WCSCall
{
    XmlSlurper xmlParser

    WCSCall()
    {
        xmlParser = new XmlSlurper()
    }

    GPathResult getCapabilities(wcsServer, version, coverage, filter)
    {
        def coverageEncoded = encode(coverage)
        def filterEncoded = encode(filter)
        def wcsUrl = "${wcsServer}/getCapabilities?service=WCS&version=${version}&request=GetCapabilities&coverage=${coverageEncoded}&filter=${filterEncoded}"
        println "wcsUrl: ${wcsUrl}"
        return xmlParser.parse(wcsUrl)
    }

    GPathResult describeCoverage(wcsServer, version, coverage, filter)
    {
        def coverageEncoded = encode(coverage)
        def filterEncoded = encode(filter)
        def wcsUrl = "${wcsServer}/describeCoverage?service=WCS&version=${version}&request=DescribeCoverage&coverage=${coverageEncoded}&filter=${filterEncoded}"
        println "wcsUrl: ${wcsUrl}"
        return xmlParser.parse(wcsUrl)
    }

    static URL getCoverage(wcsServer, wfsServer, version, coverage, filter)
    {
        def wmsCall = new WMSCall()
        def bbox = wmsCall.getBBox(wfsServer, filter)
        return getCoverageWithBbox(wcsServer, version, coverage, filter, bbox)
    }

    static URL getCoverageWithBbox(wcsServer, version, coverage, filter, bbox)
    {
        def coverageEncoded = encode(coverage)
        def filterEncoded = encode(filter)
        def bboxEncoded = encode(bbox)
        def wcsUrl = "${wcsServer}/getCoverage?service=WCS&version=${version}&request=GetCoverage&coverage=${coverageEncoded}&filter=${filterEncoded}&crs=epsg%3A4326&bbox=${bboxEncoded}&width=1024&height=512&format=GeoTIFF"
        println "wcsUrl: ${wcsUrl}"
        return new URL(wcsUrl)
    }

    static URL getCoverageNonExistentImage(wcsServer, version, coverage, filter)
    {
        def coverageEncoded = encode(coverage)
        def filterEncoded = encode(filter)
        def wcsUrl = "${wcsServer}/getCoverage?service=WCS&version=${version}&request=GetCoverage&coverage=${coverageEncoded}&filter=${filterEncoded}&crs=epsg%3A4326&bbox=1,1,1,1&width=1024&height=512&format=GeoTIFF"
        return new URL(wcsUrl)
    }

    GPathResult getVerificationResultFromStringURL(String s3BucketURL)
    {
        return xmlParser.parse(s3BucketURL)
    }

    static boolean compareResults(GPathResult wcsCallResult, GPathResult wcsVerificationResult)
    {
        return Math.abs(wcsCallResult.text().length() - wcsVerificationResult.text().length()) <= 3
    }

    private static String encode(toEncode)
    {
        return URLEncoder.encode(toEncode, Charset.defaultCharset().displayName())
    }
}