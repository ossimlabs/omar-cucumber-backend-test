@image_space
Feature: ImageSpaceService


  Scenario: [IMG-01] call ImageSpace to view a full screen jpeg image of an entire commercial EO image
#    Given a GeoEye MSI NITF21 image has been staged
    When a call is made to ImageSpace for a png of the entire bounding box of a GeoEye MSI NITF21 image
    Then ImageSpace returns a png that matches the validation of a GeoEye MSI NITF21 image

# Removed CONUS imagery, replace with new image test
#  Scenario: [IMG-02] call ImageSpace to view a single tile of overview jpeg image of an entire Rapideye MSI GeoTIFF image
#    Given a Rapideye MSI GeoTIFF image has been staged
#    When a call is made to ImageSpace for a jpeg single tile overview of a Rapideye MSI GeoTIFF image
#    Then ImageSpace returns a jpeg that matches the validation of a Rapideye MSI GeoTIFF image

  Scenario Outline: [IMG-3] call ImageSpace to get a thumbnail overview of an image
#    Given that example images and thumbnails are staged
    When a call is made to ImageSpace with a time limit of <timeLimitInMillis> to get a <thumbnailType> thumbnail of a <platform> <sensor> <format> image
    Then ImageSpace returns a <thumbnailType> that matches the validation of a <platform> <sensor> <format>_thumbnail image
    Examples:
    | thumbnailType | platform  | sensor | format  | timeLimitInMillis |
    | png           | QuickBird | MSI    | GeoTIFF | 1000              |

  Scenario: [IMG-04] call ImageSpace to view an overview tile of an commercial msi image in red green blue band order
#    Given a WorldView2 MSI GeoTIFF image has been staged
    When a call is made to ImageSpace for an overview tile in red green blue order of a WorldView2 MSI GeoTIFF image
    Then ImageSpace returns a png that matches the validation of a WorldView2 MSI GeoTIFF_rgb image

  Scenario: [IMG-05] call ImageSpace to view an overview tile of an commercial msi image in green blue red band order
#    Given a WorldView2 MSI GeoTIFF image has been staged
    When a call is made to ImageSpace for an overview tile in green blue red order of a WorldView2 MSI GeoTIFF image
    Then ImageSpace returns a png that matches the validation of a WorldView2 MSI GeoTIFF_gbr image

  Scenario: [IMG-06] call ImageSpace to view an overview tile of an commercial msi image with only the green band
#    Given a WorldView2 MSI GeoTIFF image has been staged
    When a call is made to ImageSpace for an overview tile green band of a WorldView2 MSI GeoTIFF image
    Then ImageSpace returns a png that matches the validation of a WorldView2 MSI GeoTIFF_g image
