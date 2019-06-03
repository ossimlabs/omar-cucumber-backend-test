package omar.cucumber.ogc.geoscript

import geoscript.geom.Point
import geoscript.proj.Projection

class Geoscript
{
    Geoscript() {}

    static String createPolygon(lat, lon, radius)
    {
        def polygon
        Projection epsg4326 = new Projection("EPSG:4326")
        Projection epsg3857 = new Projection("EPSG:3857")

        polygon = epsg3857.transform(epsg4326.transform(new Point(lon, lat), epsg3857).buffer(radius), epsg4326)
        polygon.toString()
    }
}
