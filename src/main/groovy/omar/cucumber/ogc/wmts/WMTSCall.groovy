package omar.cucumber.ogc.wmts

import geoscript.layer.Pyramid
import geoscript.layer.TileLayer
import geoscript.geom.Bounds
import geoscript.geom.Point
import geoscript.proj.Projection
import groovy.json.JsonSlurper
import groovy.time.TimeCategory
import groovy.time.TimeDuration
import java.nio.charset.Charset

class WMTSCall
{
    String wmtsServer
    HashMap wmtsGetTileParams = [
            service      : "WMTS",
            version      : "1.0.0",
            request      : "GetTile",
            layer        : "",
            filter       : "",
            format       : "image/jpeg",
            tileRow      : null,
            tileCol      : null,
            tileMatrix   : null,
            tileMatrixSet: null
    ]

    String getCapabilities(String version = "1.0.0")
    {
        String wmsUrlString = "${wmtsServer}/getCapabilities?service=WMTS&version=${version}&request=GetCapabilities"

        String capabilities = new URL(wmsUrlString).text
    }

    def getLayers()
    {
        URL wmtsLayers = new URL("${wmtsServer}/layers")
        String wmtsLayerResultString = wmtsLayers.text

        def jsonWmtsLayers = new JsonSlurper().parseText(wmtsLayerResultString)

        jsonWmtsLayers?.data
    }

    /**
     *
     * @return HashMap that contains a data and elaps
     */
    HashMap getTile(HashMap layer, Double gsd, Integer nResLevels, Integer width, Integer height, Bounds bounds,
                    String filter = "",
                    String format = "image/jpeg")
    {
        Date startWmtsGetTile = new Date()

        WMTSTileCachePyramid wmtsPyramid = newPyramid(layer)
        def tileIntersections = wmtsPyramid.findIntersections(bounds, gsd, width, height, nResLevels)
        Double midx = (tileIntersections.clippedBounds.minX + tileIntersections.clippedBounds.maxX) * 0.5
        Double midy = (tileIntersections.clippedBounds.minY + tileIntersections.clippedBounds.maxY) * 0.5
        Integer level = (tileIntersections.maxLevel - 1)
        if (level < tileIntersections.minLevel) level = tileIntersections.minLevel
        Point centerPoint = new Point(midx, midy)
        WMTSTileLayer wmtsTileLayer = new WMTSTileLayer(pyramid: wmtsPyramid, bounds: wmtsPyramid.bounds)
        def centerTileInfo = getCenterTileInfo(wmtsTileLayer, level, centerPoint)
        HashMap params = new HashMap(wmtsGetTileParams)

        params.layer = layer.name
        params.filter = filter
        params.format = format
        params.tileRow = centerTileInfo.tile.y
        params.tileCol = centerTileInfo.tile.x
        params.tileMatrix = centerTileInfo.tile.z
        params.tileMatrixSet = layer.tileMatrixName
        params.styles = URLEncoder.encode("{\"nullPixelFlip\": false}", Charset.defaultCharset().displayName())

        String urlParamsString = urlParamsToString(params)
        String url = "${wmtsServer}/getTile?${urlParamsString}"

        HashMap result = [url: new URL(url), duration: 0]
        Date endWmtsGetTile = new Date()

        //calculate how long the process took
        TimeDuration duration = TimeCategory.minus(endWmtsGetTile, startWmtsGetTile)
        result.duration = duration.toMilliseconds() / 1000.0
        result
    }

    HashMap getInvalidTile(HashMap layer, Double gsd, Integer nResLevels, Integer width, Integer height, Bounds bounds,
                           String format = "image/jpeg")
    {
        Date startWmtsGetTile = new Date()

        HashMap params = new HashMap(wmtsGetTileParams)

        params.layer = layer.name
        params.format = format
        params.tileRow = 0
        params.tileCol = 0
        params.tileMatrix = layer.maxLevel
        params.tileMatrixSet = layer.tileMatrixName
        params.styles = URLEncoder.encode("{\"nullPixelFlip\": false}", Charset.defaultCharset().displayName())

        String urlParamsString = urlParamsToString(params)
        String url = "${wmtsServer}/getTile?${urlParamsString}"
        println "wmts getInvalidTile URL: ${url}"

        HashMap result = [url: new URL(url), duration: 0]
        Date endWmtsGetTile = new Date()

        //calculate how long the process took
        TimeDuration duration = TimeCategory.minus(endWmtsGetTile, startWmtsGetTile)
        result.duration = duration.toMilliseconds() / 1000.0
        result
    }

    static HashMap getCenterTileInfo(TileLayer tileLayer, Integer zoom, Point point)
    {
        HashMap result = [tile: null, bounds: null]
        def tiles = tileLayer.tiles(new Bounds(point.x, point.y, point.x, point.y), zoom)

        if (tiles)
        {
            result.tile = tiles[0]
            result.bounds = tileLayer.pyramid.bounds(result.tile)
        }
        result
    }

    static String urlParamsToString(HashMap urlParams)
    {
        urlParams.collect() { k, v -> "${k}=${v}" }.join("&")?.toString()
    }

    static WMTSTileCachePyramid newPyramid(HashMap layer)
    {
        Projection proj = new Projection(layer.epsgCode)
        WMTSTileCacheHints cacheHints = new WMTSTileCacheHints(
                proj: proj,
                origin: Pyramid.Origin.TOP_LEFT,
                layerBounds: new Bounds(layer.minX, layer.minY, layer.maxX, layer.maxY, proj),
                tileWidth: layer.tileWidth,
                tileHeight: layer.tileHeight,
                minLevel: layer.minLevel,
                maxLevel: layer.maxLevel)
        
        WMTSTileCachePyramid wmtsPyramid = new WMTSTileCachePyramid()
        wmtsPyramid.initializeGrids(cacheHints)

        wmtsPyramid
    }
}

