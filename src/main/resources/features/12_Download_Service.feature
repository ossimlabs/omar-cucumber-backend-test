@download_service
Feature: DownloadService
  As the OC2S system, I need to ingest imagery for discovery, processing and viewing in Test and Ops

Scenario Outline: [DL-01] Calling Download Service for image
  Given that the download service is running
  When we download the image <image-name>
  Then the file <image-name> should exist
  Then the downloaded file <image-name> matches the validation of S3 file <file-path>

  Examples:
    | image-name | file-path |
    | 04DEC11050020-M2AS_R1C1-000000185964_01_P001 | o2-test-data/Standard_test_imagery_set/QuickBird/TIFF/Multi/04DEC11050020-M2AS_R1C1-000000185964_01_P001.TIF |
    | 14SEP12113301-P1BS-053951940020_01_P001 | o2-test-data/Standard_test_imagery_set/WorldView/WV2/GeoTIFF/Pan/14SEP12113301-P1BS-053951940020_01_P001.TIF |
    | 14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG | o2-test-data/Standard_test_imagery_set/TerraSAR-X/NITF2_0/14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG.ntf |

Scenario: [DL-02] Calling Download Service without a json message
  Given that the download service is running
  When the download service is called without a json message
  Then the response should return a status of 400 and a message of "Invalid parameters"

Scenario: [DL-03] Calling Download Service without files groups
  Given that the download service is running
  When the download service is called with no fileGroups specified in the json
  Then the response should return a status of 406 and a message of "No File Group Specified"

Scenario: [DL-04] Calling Download Service with the wrong archive type
  Given that the download service is running
  When the download service is called with the wrong archive type
  Then the response should return a status of 415 and a message of "Archive Option Type Not Recognized"
