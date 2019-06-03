@image_space
Feature: ImageSpaceService


  Scenario: [IMG-01] call ImageSpace to view a full screen jpeg image of an entire commercial EO image
    When a call is made to ImageSpace for a png of the entire bounding box of the image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507
    Then ImageSpace returns a png that matches the validation image 05FEB09OV05010005V090205M0001912264B220000100072M_001508507

  Scenario Outline: [IMG-3] call ImageSpace to get a thumbnail overview of an image
    When a call is made to ImageSpace with a time limit of <timeLimitInMillis> to get a png thumbnail of the image 04DEC11050020-M2AS_R1C1-000000185964_01_P001
    Then ImageSpace returns a png that matches the validation image 04DEC11050020-M2AS_R1C1-000000185964_01_P001_thumbnail
    Examples:
    | timeLimitInMillis |
    | 1000              |

  Scenario: [IMG-04] call ImageSpace to view an overview tile of an commercial msi image in red green blue band order
    When a call is made to ImageSpace for an overview tile in red green blue order of the image 14SEP12113301-M1BS-053951940020_01_P001
    Then ImageSpace returns a png that matches the validation image 14SEP12113301-M1BS-053951940020_01_P001_rgb

  Scenario: [IMG-05] call ImageSpace to view an overview tile of an commercial msi image in green blue red band order
    When a call is made to ImageSpace for an overview tile in green blue red order of the image 14SEP12113301-M1BS-053951940020_01_P001
    Then ImageSpace returns a png that matches the validation image 14SEP12113301-M1BS-053951940020_01_P001_gbr

  Scenario: [IMG-06] call ImageSpace to view an overview tile of an commercial msi image with only the green band
    When a call is made to ImageSpace for an overview tile green band of the image 14SEP12113301-M1BS-053951940020_01_P001
    Then ImageSpace returns a png that matches the validation image 14SEP12113301-M1BS-053951940020_01_P001_g
