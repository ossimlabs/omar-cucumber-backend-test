@map_tile_service
Feature: MapTileService

  Scenario Outline: [WMTS-01] system calls WMTS GetCapabilities
    When a user calls GetCapabilities for version <version>
    Then the WMTS service responds with a correct GetCapabilities statement
    Examples:
      |version|
      |1.0.0  |

  Scenario: [WMTS-02] WMTS call to get the entire bounding box of an image
    When a call is made to WMTS for a jpeg image the for the entire bounding box of a Ikonos PAN NITF image
    Then WMTS returns tiles that matches the validation jpeg image

  Scenario: [WMTS-03] WMTS call to get the outside entire bounding box of an image
    When a call is made to WMTS for a jpeg outside entire bounding box of a Ikonos PAN NITF image
    Then WMTS returns the proper error

  Scenario: [WMTS-04] WMTS call for a non-existent image
    When a call is made to WMTS for a non-existent image
    Then WMTS returns the proper error

  Scenario: [WMTS-05] WMTS call for a subset of an image
    When a call is made to WMTS for jpeg of a subset of a Ikonos PAN NITF image
    Then WMTS returns tiles that matches the validation jpeg image