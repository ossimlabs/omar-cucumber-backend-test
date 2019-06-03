package omar.cucumber.ogc.wmts

import geoscript.layer.Pyramid
import geoscript.layer.Tile
import geoscript.layer.TileLayer
import geoscript.layer.ImageTile

class WMTSTileLayer extends TileLayer<ImageTile>
{
    Pyramid pyramid

    Pyramid getPyramid()
    {
        pyramid
    }

    ImageTile get(long z, long x, long y)
    {
        new ImageTile(z, x, y)
    }

    void put(ImageTile t)
    {
        // intentionally left blank.
    }

    void delete(ImageTile t)
    {
        // intentionally left blank.
    }

    void close() throws IOException
    {
        // intentionally left blank.
    }

    void delete(Tile tile)
    {
        // intentionally left blank.
    }
}
