@superoverlay_service
Feature: SuperoverlayService
  As the OC2S system, I need to provide kml superoverlays

  Scenario: [DLS-02] Calling WFS SuperOverlay Service for the KML superoverlay of an image
    Given a QuickBird MSI GeoTIFF image has been staged
    When the superoverlay service is called to download a KML super-overlay of a QuickBird MSI GeoTIFF image
    Then the service returns a KML file for a QuickBird MSI GeoTIFF image