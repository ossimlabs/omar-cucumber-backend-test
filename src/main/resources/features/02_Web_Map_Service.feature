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

  Scenario: [WMS-09] WMS call to get a Pan-Sharpened Image
    Given the needed image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507 has been staged
    And the needed image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507 has been staged
    When a WMS getPSM call is made for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507 and the image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507
    Then GetPsm WMS returns a png that matches the validation image 05FEB09OV05010005V090205_psm_chip_dev

  Scenario: [WMS-10] WMS call to auto-format a jpeg image
    Given the needed image 16AUG23141705-P1BS-057310297010_01_P002 has been staged
    When a WMS call is made for the image 16AUG23141705-P1BS-057310297010_01_P002 with bounding box of -3.5767,40.4258,-3.4071,40.5200
    Then Ortho WMS returns a jpeg that matches the validation image 16AUG23141705-P1BS-057310297010_01_P002_chip_dev

  Scenario: [WMS-11] WMS call to auto-format png image
    Given the needed image 16AUG23141705-P1BS-057310297010_01_P002 has been staged
    When a WMS call is made for the image 16AUG23141705-P1BS-057310297010_01_P002 with bounding box of -3.6177,40.4172,-3.5438,40.4663
    Then Ortho WMS returns a png that matches the validation image 16AUG23141705-P1BS-057310297010_01_P002_chip_dev
