package omar.cucumber.ogc.wfs

import geoscript.geom.Bounds
import geoscript.geom.Geometry
import groovy.json.JsonSlurper

import javax.net.ssl.*
import java.nio.charset.Charset
import java.security.SecureRandom
import java.security.cert.X509Certificate
import org.apache.http.HttpEntity
import org.apache.http.HttpResponse
import org.apache.http.client.HttpClient
import org.apache.http.client.methods.HttpPost
import org.apache.http.entity.StringEntity
import org.apache.http.impl.client.DefaultHttpClient



class WFSCall
{
    def result
    def status
    String text

    WFSCall(){}
/*
    static void initSsl()
    {
        println "************************************"
        TrustManager[] trustAllCerts = [
            new X509TrustManager() {
                X509Certificate[] getAcceptedIssuers()
                {
                    null
                }

                void checkClientTrusted(X509Certificate[] certs, String authType) {}

                void checkServerTrusted(X509Certificate[] certs, String authType) {}
            }
        ]

        SSLContext sc = SSLContext.getInstance("SSL")
        sc.init(null, trustAllCerts, new SecureRandom())
        HttpsURLConnection.setDefaultSSLSocketFactory(sc.getSocketFactory())

        HostnameVerifier allHostsValid = new HostnameVerifier() {
            public boolean verify(String hostname, SSLSession session)
            {
                return true
            }
        }
    }
*/
    WFSCall(wfsServer, filter, outputFormat, maxFeatures)
    {
        HashMap wfsParams = [
                service     : "WFS",
                version     : "1.1.0",
                request     : "GetFeature",
                typeName    : "omar:raster_entry",
                resultType  : "results",
                outputFormat: outputFormat,
                filter      : URLEncoder.encode(filter, Charset.defaultCharset().displayName()),
                maxFeatures : maxFeatures,
                startIndex  : 0
        ]

        String wfsParamsString = urlParamsToString(wfsParams)
        String wfsUrlString = "${wfsServer}/getFeature?${wfsParamsString}"

        println "WFSCall URL: ${wfsUrlString}"
        URL wfsText = new URL(wfsUrlString)

        text = wfsText.text
        result = outputFormat == "JSON" ? new JsonSlurper().parseText(text) : text
    }
    void getFeaturePost(String wfsServer, String postString)
    {
        HttpClient httpClient = new DefaultHttpClient()
        HttpResponse response
        result = ""
        try {
            HttpPost httpPost = new HttpPost(wfsServer)
            httpPost.setHeader("Content-Type", "text/xml")

            HttpEntity reqEntity = new StringEntity(postString, "UTF-8")
            reqEntity.setContentType("text/xml")
            reqEntity.setChunked(true)

            httpPost.setEntity(reqEntity)

            response = httpClient.execute(httpPost)
            HttpEntity resEntity = response.getEntity()

            status = response.getStatusLine()
            if (resEntity != null) {
                text = resEntity.content.text
            }
            // EntityUtils.consume(resEntity);
        }
        finally {
            // When HttpClient instance is no longer needed,
            // shut down the connection manager to ensure
            // immediate deallocation of all system resources
            httpClient.getConnectionManager().shutdown()
        }
    }
    int getNumFeatures()
    {
        result?.features?.size()
    }

    int getLayer(Integer featureIdx = 0)
    {
        result?.features[featureIdx].properties.id
    }
    def getFeature(Integer featureIdx = 0)
    {
        result?.features[featureIdx]
    }
    String getName()
    {
        result.features
    }

    Integer getNumberOfResLevels(Integer featureIdx = 0)
    {
        result?.features[featureIdx].properties.number_of_res_levels
    }

    Integer getWidth(Integer featureIdx = 0)
    {
        result?.features[featureIdx].properties.width
    }

    Integer getHeight(Integer featureIdx = 0)
    {
        result?.features[featureIdx].properties.height
    }

    String getFilename(Integer featureIdx = 0)
    {
        result?.features[featureIdx].properties.filename
    }

    Double getGsd(Integer featureIdx = 0)
    {
        return result.features[featureIdx].properties.gsdy
    }

    Geometry getGeometry(Integer featureIdx = 0)
    {
        def feature = result.features[featureIdx]
        String wktGeometryString
        String polyType = feature.geometry.type.toUpperCase()
        switch (polyType)
        {
            case ~/.*MULTIPOLYGON/:
                def tempValue = feature.geometry.coordinates[0][0].collect() {
                    "${it[0]} ${it[1]}"
                }.join(",").toString()
                wktGeometryString = "MultiPolygon(((${tempValue})))"
                break
            case ~/.*POLYGON/:
                def tempValue = feature.geometry.coordinates[0][0].collect() {
                    "${it[0]} ${it[1]}"
                }.join(",").toString()
                wktGeometryString = "Polygon((${tempValue}))"
                break
            default:
                wktGeometryString = ""
                break
        }
        Geometry.fromWKT(wktGeometryString)
    }

    Bounds getBounds(Integer featureIdx = 0)
    {
        getGeometry(featureIdx).bounds
    }


    static String urlParamsToString(HashMap urlParams)
    {
        urlParams.collect() { k, v -> "${k}=${v}" }.join("&")
    }

    String getResultText()
    {
        return text
    }
}
