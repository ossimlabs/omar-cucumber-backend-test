
@wfs_image_search
Feature: SearchWFSService

  Scenario: [WFS-01] system calls WFS GetCapabilities
    When WFS GetCapabilities call is made
    Then the system returns a GetCapabilites statement that matches the expected WFSGetCapabilites output

  Scenario: [WFS-02] WFS call to search for an image by ImageID
    When a WFS call is made to search for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  Scenario: [WFS-03] WFS call to search for an image by specifying a bounding box
    When a WFS call is made to search for an image specifying a bounding box 45.0, 22.5, 67.5, 45.0
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  Scenario: [WFS-04] WFS call to search for an image by specifying a bounding polygon
    When a WFS call is made to search for an image specifying a bounding polygon 51.05072021484375, 35.57304382324219, 51.05072021484375, 35.80101013183594, 51.89666748046875, 35.80101013183594, 51.89666748046875, 35.57304382324219, 51.05072021484375, 35.57304382324219
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  Scenario: [WFS-05] WFS call to search for an image by specifying a single point
    When a WFS call is made to search for an image specifying a single point 51.458587646484375, 35.687713623046875
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  Scenario: [WFS-06] WFS call to search for an image by specifying a point and radius
    When a WFS call is made to search for an image specifying a point and radius 51.458587646484375, 35.687713623046875, 20
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  Scenario: [WFS-07] WFS call to search for an image by specifying a Country Code
    When a WFS call is made to search for an image specifying a Country Code of AS
    Then the WFS call returns a feature for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    And the WFS call returns a feature for the image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507

  Scenario: [WFS-08] WFS call to search for an image by ImageID and verify receve_date is populated
    When a WFS call is made to search for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003
    Then the WFS call will have the receive_date populated for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  @C2S
  Scenario: [WFS-09] WFS call to search for an image by specifying a point BE Number
    When a WFS call is made to search for an image specifying a BE Number of BE12345
    Then the WFS call returns a feature for the image 14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG

  @C2S
  Scenario: [WFS-10] WFS call to search for an image by specifying a point Target ID
    When a WFS call is made to search for an image specifying a Target ID of BE12345
    Then the WFS call returns a feature for the image 14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG

  Scenario: [WFS-11] WFS call to search for an image by specifying a Sensor Type
    When a WFS call is made to search for an image specifying a Sensor Type of AA
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003
    And the WFS call returns a feature for the image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507

  Scenario: [WFS-12] WFS call to search for an image by specifying a Date Range
    When a WFS call is made to search for an image specifying a Date Range of 01-27-2007 to 01-29-2010
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  Scenario: [WFS-13] WFS call to search for an image by specifying a complex query
    When a WFS call is made to search for an image specifying a Date Range from 01-27-2007 to 01-29-2010 and Country Code of IR
    Then the WFS call returns a feature for the image 11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003

  Scenario: [WFS-14] WFS call to verify metadata is available in GML3 format
    Given that the WFS service is available
    When a WFS call is made to with an output format of GML3
    Then the WFS call returns metadata in a GML3 format

  Scenario: [WFS-15] WFS call to verify metadata is available in JSON format
    Given that the WFS service is available
    When a WFS call is made to with an output format of JSON
    Then the WFS call returns metadata in a JSON format

  Scenario: [WFS-16] WFS call to verify omar-oldmar Post with filter
    Given that the WFS service is available
    When a WFS post to omar-oldmar with a filter in xml format is made
    Then the WFS call returns metadata in a GML2 format
