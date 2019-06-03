@mensa_service
Feature: Mensuration Service

  Scenario: [MS-01] Return a measurement for the distance between two points
    #Given the image tempIMG has been staged
    When the mensuration service is called with two points on the image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507
    Then the distance between those two points is returned

  Scenario: [MS-02] Return a measurement for the path length between multiple points
    #Given  the image tempIMG has been staged
    When the mensuration service is called with four points on the image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507
    Then the path distance between those four points is returned

  Scenario: [MS-03] Return return measurement for the area of a polygon
    #Given the image tempIMG has been staged
    When the mensuration service is called with a polygon of nine points on the image 05FEB09OV05010005V090205P0001912264B220000100282M_001508507
    Then the area of that polygon is returned
