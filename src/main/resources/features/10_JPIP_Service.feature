@jpip_service
Feature: JPIP Service
  As the OC2S system, I need to provide kml superoverlays
  Scenario Outline: [JP-01] Calls are made to query the JPIP server for a stream link with proper parameters
      Given the JPIP service is running
      When a call is made to JPIP to create a stream of an image at <image-path> entry <entry> and project code <proj-code> with a <timeout> millisecond timeout
      Then the JPIP service returns a status of FINISHED without timing out
  Examples:
    | image-path| entry | proj-code | timeout |
    | /data/s3/2009/02/05/00/ntf/po_106005_pan_0000000.ntf    | 0     | 4326     | 15000    |

