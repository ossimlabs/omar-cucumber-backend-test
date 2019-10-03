@web_coverage_service
Feature: WebCoverageService

  Scenario Outline: [WCS-01] A user calls WCS GetCapabilities
    When a user calls WCS GetCapabilities version <version> for <index> <platform> <sensor> <format> image
    Then the WCS service responds with a correct GetCapabilities statement
  Examples:
    | version | index | platform | sensor |  format  |
    | 1.0.0   | a     | GeoEye   |  MSI   | NITF21   |

  Scenario Outline: [WCS-02] A user calls WCS DescribeCoverage for a given image
    When a user calls WCS DescribeCoverage version <version> for <index> <platform> <sensor> <format> image
    Then the WCS service responds with a correct DescribeCoverage data for that image
  Examples:
    | version | index | platform | sensor |  format  |
    | 1.0.0   | a     | GeoEye   |  MSI   | NITF21   |

  Scenario Outline: [WCS-03] A user call is made to WCS for the entire bounding box of an image
    When a call is made to WCS version <version> for the entire bounding box of <index> <platform> <sensor> <format> image
    Then WCS returns a <imageType> that matches the validation image
  Examples:
  | version | index | platform | sensor |  format  | imageType |
  | 1.0.0   | a     | GeoEye   |  MSI   | NITF21   |    tiff   |

  Scenario: [WCS-04] A user call is made to WCS for a non-existent image
    When a call is made to WCS 1.0.0 for a non-existent image
    Then WCS returns the proper error

  Scenario Outline: [WCS-05] A user call is made to WCS for a bounding box larger than an image
    When a call is made to WCS <version> for a bounding box <bbox> larger than <index> <platform> <sensor> <format> image
    Then WCS returns a <imageType> that matches the validation image
  Examples:
    | version |       bbox      | index | platform | sensor |  format  | imageType |
    | 1.0.0   | 145,-45,149,-41 | a     | GeoEye   |  MSI   | NITF21   |    tiff   |

  Scenario Outline: [WCS-06] A user call is made to WCS for a subset of an image
    When a call is made to WCS <version> using a subset bounding box <bbox> of <index> <platform> <sensor> <format> image
    Then WCS returns a <imageType> that matches the validation image
  Examples:
  | version |             bbox             | index | platform | sensor |  format  | imageType |
  | 1.0.0   | 147.225,-42.8,147.275,-42.83 | a     | GeoEye   |  MSI   | NITF21   |    tiff   |