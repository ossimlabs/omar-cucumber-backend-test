@mensa_service
Feature: Mensuration Service

  Scenario: [MS-01] Return a measurement for the distance between two points
    Given a GeoEye PAN NITF21 image has been staged
    When the mensuration service is called with two points on a GeoEye PAN NITF21 image
    Then the distance between those two points is returned

  Scenario: [MS-02] Return a measurement for the path length between multiple points
    Given a GeoEye PAN NITF21 image has been staged
    When the mensuration service is called with four points on a GeoEye PAN NITF21 image
    Then the path distance between those four points is returned

  Scenario: [MS-03] Return return measurement for the area of a polygon
    Given a GeoEye PAN NITF21 image has been staged
    When the mensuration service is called with a polygon of nine points on a GeoEye PAN NITF21 image
    Then the area of that polygon is returned
