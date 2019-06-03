@wms_ortho
Feature: OrthoWMSService

  Scenario: [WMS-01] call Ortho WMS to view a full screen jpeg image of an entire commercial EO image
    Given the needed image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507 has been staged
    When a call is made to Ortho WMS with an image type of jpeg for the entire bounding box of the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then Ortho WMS returns a jpeg that matches the validation image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507_dev

  Scenario: [WMS-02] call Ortho WMS to view a full screen png image of an entire commercial EO image
    Given the needed image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507 has been staged
    When a call is made to Ortho WMS with an image type of png for the entire bounding box of the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then Ortho WMS returns a png that matches the validation image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507_dev

  Scenario: [WMS-04] call ortho WMS for full resolution image chip
    Given the needed image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507 has been staged
    When a call is made to Ortho WMS with an image type of png for a 256 by 256 chip of the image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507 at full resolution
    Then Ortho WMS returns a png that matches the validation image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507_chip_dev

  Scenario: [WMS-06] Call WMS GetCapabilities version 1.1.1
    When a system calls WMS GetCapabilities version 1.1.1
    Then the service returns the expected GetCapabilities response

  Scenario: [WMS-07] Call WMS GetCapabilities version 1.3
    When a system calls WMS GetCapabilities version 1.3
    Then the service returns the expected GetCapabilities response

  Scenario: [WMS-08] WMS call to create single image from multiple image IDs
    Given the needed image 16MAY02111606-P1BS-055998375010_01_P013 has been staged
    And the needed image 16MAY02111607-P1BS-055998375010_01_P014 has been staged
    When a WMS call is made for the image 16MAY02111606-P1BS-055998375010_01_P013 and the image 16MAY02111607-P1BS-055998375010_01_P014
    Then a stitched image will be returned
