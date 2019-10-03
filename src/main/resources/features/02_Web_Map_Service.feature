@wms_ortho
Feature: OrthoWMSService

  Scenario: [WMS-01] call Ortho WMS to view a full screen jpeg image of an entire commercial EO image
    Given a GeoEye MSI NITF21 image has been staged
    When a call is made to Ortho WMS with an image type of jpeg for the entire bounding box of a GeoEye MSI NITF21 image
    Then Ortho WMS returns a jpeg that matches the validation of a GeoEye MSI NITF21_dev image

  Scenario: [WMS-02] call Ortho WMS to view a full screen jpeg image of an entire commercial EO image
    Given a GeoEye MSI NITF21 image has been staged
    When a call is made to Ortho WMS with an image type of png for the entire bounding box of a GeoEye MSI NITF21 image
    Then Ortho WMS returns a png that matches the validation of a GeoEye MSI NITF21_dev image

#  Disable use of CONUS imagery. Test should be replaced.
#  Scenario: [WMS-03] call Ortho WMS to view a full screen jpeg image of an entire commercial Rapideye MSI GeoTIFF image
#    Given a Rapideye MSI GeoTIFF image has been staged
#    When a call is made to Ortho WMS with an image type of jpeg for the entire bounding box of a Rapideye MSI GeoTIFF image
#    Then Ortho WMS returns a jpeg that matches the validation of a Rapideye MSI GeoTIFF image

  Scenario: [WMS-04] call ortho WMS for full resolution image chip
    Given a GeoEye PAN NITF21 image has been staged
    When a call is made to Ortho WMS with an image type of png for a 256 by 256 chip of a GeoEye PAN NITF21 image at full resolution
    Then Ortho WMS returns a png that matches the validation of a GeoEye PAN NITF21_chip_dev image

#  Disable use of CONUS imagery. Test should be replaced.
#  Scenario: [WMS-05] call Ortho WMS to view a full screen image of an entire commercial Rapideye MSI GeoTIFF image
#    Given a Rapideye MSI GeoTIFF image has been staged
#    When a call is made to Ortho WMS with a 512 by 512 jpeg image for the entire bounding box of a Rapideye MSI GeoTIFF image
#    Then Ortho WMS returns a jpeg in less than 1.5 seconds

  Scenario: [WMS-06] Call WMS GetCapabilities version 1.1.1
    When a system calls WMS GetCapabilities version 1.1.1
    Then the service returns the expected GetCapabilities response

  Scenario: [WMS-07] Call WMS GetCapabilities version 1.3
    When a system calls WMS GetCapabilities version 1.3
    Then the service returns the expected GetCapabilities response

  Scenario: [WMS-08] WMS call to create single image from multiple image IDs
    Given a WorldView2 PAN GeoTIFF image has been staged
    And another WorldView2 PAN GeoTIFF image has been staged
    When a WMS call is made for a WorldView2 PAN GeoTIFF image and another WorldView2 PAN GeoTIFF image
    Then a stitched image will be returned
