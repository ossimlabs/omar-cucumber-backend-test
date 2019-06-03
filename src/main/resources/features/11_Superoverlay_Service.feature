@superoverlay_service
Feature: SuperoverlayService
  As the OC2S system, I need to provide kml superoverlays

Scenario Outline: [SOL-01] Calling WFS SuperOverlay Service for the KML superoverlay of an image
    Given the image <image-name> has been staged
    When the superoverlay service is called to download a KML super-overlay of the image <image-name>
    Then the service returns a KML file for the image <image-name>

  Examples:
    | image-name |
    | 4DEC11050020-M2AS_R1C1-000000185964_01_P001 |