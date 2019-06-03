@web_coverage_service
Feature: WebCoverageService

  Scenario Outline: [WCS-01] A user calls WCS GetCapabilities
    When a user calls WCS GetCapabilities version <version> for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then the WCS service responds with a correct GetCapabilities statement for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
  Examples:
    | version |
    | 1.0.0   |

  Scenario Outline: [WCS-02] A user calls WCS DescribeCoverage for a given image
    When a user calls WCS DescribeCoverage version <version> for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then the WCS service responds with a correct DescribeCoverage data for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
  Examples:
    | version |
    | 1.0.0   |

  Scenario Outline: [WCS-03] A user call is made to WCS for the entire bounding box of an image
    When a call is made to WCS version <version> for the entire bounding box of the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then WCS returns a tiff that matches the validation image for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
  Examples:
  | version |
  | 1.0.0   |

  Scenario: [WCS-04] A user call is made to WCS for a non-existent image
    When a call is made to WCS 1.0.0 for a non-existent image
    Then WCS returns the proper error

  Scenario Outline: [WCS-05] A user call is made to WCS for a bounding box larger than an image
    When a call is made to WCS <version> for a bounding box <bbox> larger than the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then WCS returns a tiff that matches the validation image for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
  Examples:
    | version |       bbox      |
    | 1.0.0   | 145,-45,149,-41 |

  Scenario Outline: [WCS-06] A user call is made to WCS for a subset of an image
    When a call is made to WCS <version> using a subset bounding box <bbox> of the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then WCS returns a tiff that matches the validation image for the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
  Examples:
  | version |             bbox             |
  | 1.0.0   | 147.225,-42.8,147.275,-42.83  |