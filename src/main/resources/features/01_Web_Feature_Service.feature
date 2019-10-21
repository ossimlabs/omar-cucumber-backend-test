@wfs_image_search
Feature: SearchWFSService

  Scenario: [WFS-01] system calls WFS GetCapabilities
    When WFS GetCapabilities call is made
    Then the system returns a GetCapabilites statement that matches the expected WFSGetCapabilites output

  Scenario: [WFS-02] WFS call to search for an image by ImageID
    When a WFS call is made to search for a WorldView2 PAN NITF20 image
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image

  Scenario: [WFS-03] WFS call to search for an image by specifying a bounding box
    When a WFS call is made to search for an image specifying a bounding box 45.0, 22.5, 67.5, 45.0
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image

  Scenario: [WFS-04] WFS call to search for an image by specifying a bounding polygon
    When a WFS call is made to search for an image specifying a bounding polygon 51.05072021484375, 35.57304382324219, 51.05072021484375, 35.80101013183594, 51.89666748046875, 35.80101013183594, 51.89666748046875, 35.57304382324219, 51.05072021484375, 35.57304382324219
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image

  Scenario: [WFS-05] WFS call to search for an image by specifying a single point
    When a WFS call is made to search for an image specifying a single point 51.458587646484375, 35.687713623046875
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image

  Scenario: [WFS-06] WFS call to search for an image by specifying a point and radius
    When a WFS call is made to search for an image specifying a point and radius 51.458587646484375, 35.687713623046875, 20
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image

  Scenario: [WFS-07] WFS call to search for an image by specifying a Country Code
    When a WFS call is made to search for an image specifying a Country Code of AS
    Then the WFS call returns a feature for a GeoEye MSI NITF21 image
    And the WFS call returns a feature for a GeoEye PAN NITF21 image

  Scenario: [WFS-08] WFS call to search for an image by ImageID and verify receve_date is populated
    When a WFS call is made to search for a WorldView2 PAN NITF20 image
    Then the WFS call will have the receive_date populated for WorldView2 PAN NITF20


  @C2S
  Scenario: [WFS-08] WFS call to search for an image by specifying a point BE Number
    When a WFS call is made to search for an image specifying a BE Number of BE12345
    Then the WFS call returns a feature for a TerraSAR-X SAR NITF20 image

  @C2S
  Scenario: [WFS-09] WFS call to search for an image by specifying a point Target ID
    When a WFS call is made to search for an image specifying a Target ID of BE12345
    Then the WFS call returns a feature for another TerraSAR-X SAR NITF20 image

  Scenario: [WFS-10] WFS call to search for an image by specifying a Sensor Type
    When a WFS call is made to search for an image specifying a Sensor Type of AA
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image
    And the WFS call returns a feature for a GeoEye PAN NITF21 image

  Scenario: [WFS-11] WFS call to search for an image by specifying a Date Range
    When a WFS call is made to search for an image specifying a Date Range of 01-27-2007 to 01-29-2010
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image

  Scenario: [WFS-12] WFS call to search for an image by specifying a complex query
    When a WFS call is made to search for an image specifying a Date Range from 01-27-2007 to 01-29-2010 and Country Code of IR
    Then the WFS call returns a feature for a WorldView2 PAN NITF20 image

#  Scenario: WFS call to verify metadata is available in GEOJSON format
#    Given that the WFS service is available
#    When a WFS call is made to with an output format of GEOJSON
#    Then the WFS call returns metadata in a GEOJSON format

  Scenario: WFS call to verify metadata is available in GML3 format
    Given that the WFS service is available
    When a WFS call is made to with an output format of GML3
    Then the WFS call returns metadata in a GML3 format

  Scenario: WFS call to verify metadata is available in JSON format
    Given that the WFS service is available
    When a WFS call is made to with an output format of JSON
    Then the WFS call returns metadata in a JSON format

#  KML does not seem to be a currently implemented outputFormat in the WFS service, removing test until further clarification
#  Scenario: WFS call to verify metadata is available in KML format
#    Given that the WFS service is available
#    When a WFS call is made to with an output format of KML
#    Then the WFS call returns metadata in a KML format

  Scenario: WFS call to verify omar-oldmar Post with filter
    Given that the WFS service is available
    When a WFS post to omar-oldmar with a filter in xml format is made
    Then the WFS call returns metadata in a GML2 format