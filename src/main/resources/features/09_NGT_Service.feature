@ngt_service
Feature: NGT Service

	Scenario Outline: [NGT-01] Calls are made to known NGT-services with proper parameters
		Given that the NGT service is running
		When a call is made to the <service> with the <parameters> to the NGT service
		Then <service> call should appear in the NGT-service log table with <parameters> to the NGT service
		Examples:
			| service	| parameters  |
			| stereo  | /data/s3/msp/Stereo/test/A.NTF,/data/s3/msp/Stereo/test/B.NTF,/data/s3/msp/Stereo/test/output |