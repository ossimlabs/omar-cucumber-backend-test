@download_service
Feature: DownloadService
  As the OC2S system, I need to ingest imagery for discovery, processing and viewing in Test and Ops

#  Scenario: [DLS-01] Download image file and supporting zip file
#    Given a GeoEye MSI NITF21 image has been staged
#    When the download service is called to download a GeoEye MSI NITF21 image as a zip file
#    Then a GeoEye MSI NITF21 image is downloaded along with supporting zip file

  Scenario Outline: Calling Download Service for image
    Given that the download service is running
    When we download <index> <platform> <sensor> <format> image
    Then a file of <index> <platform> <sensor> <format> image should exist
    Then a downloaded file of <index> <platform> <sensor> <format> matches the validation of S3 file <s3Path>
    Examples:
      | index | platform   | sensor | format  | s3Path |
      | a     | quickbird  | msi    | geotiff | o2-test-data/Standard_test_imagery_set/QuickBird/TIFF/Multi/04DEC11050020-M2AS_R1C1-000000185964_01_P001.TIF |
      | a     | worldview2 | pan    | nitf20  | o2-test-data/Standard_test_imagery_set/WorldView/WV2/GeoTIFF/Pan/14SEP12113301-P1BS-053951940020_01_P001.TIF |
      | a     | terrasar-x | sar    | nitf20  | o2-test-data/Standard_test_imagery_set/TerraSAR-X/NITF2_0/14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG.ntf |
#      | a     | local      | hsi    | envi    | o2-test-data/hsi/2012-06-11/AM/ALPHA/2012-06-11_18-20-11/HSI/Scan_00007/2012-06-11_18-20-11.HSI.Scan_00007.scene.corrected.hsi |

  Scenario: Calling Download Service without a json message
    Given that the download service is running
    When the download service is called without a json message
    Then the response should return a status of 400 and a message of "Invalid parameters"

  Scenario: Calling Download Service without files groups
    Given that the download service is running
    When the download service is called with no fileGroups specified in the json
    Then the response should return a status of 406 and a message of "No File Group Specified"

  Scenario: Calling Download Service with the wrong archive type
    Given that the download service is running
    When the download service is called with the wrong archive type
    Then the response should return a status of 415 and a message of "Archive Option Type Not Recognized"
