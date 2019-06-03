@omar_geoscript
Feature: OmarGeoscript

  Scenario: [GSS-01] System calls Geoscript GetCapabilitiesData
    When a call is made to the Geoscript service GetCapabilitiesData
    Then the Geoscript service returns with the correct GetCapabilitiesData statement

  Scenario Outline: [GSS-02] A call is made to GetSchemaInfoByTypeName with a valid TypeName
    When a call is made to the Geoscript service GetSchemaInfoByTypeName with the TypeName <typename>
    Then the Geoscript service returns with the correct JSON
    Examples:
      | typename          |
      | omar:raster_entry |

  Scenario: [GSS-03] A call is made to GetSchemaInfoByTypeName with an incorrect TypeName
    When a call is made to the Geoscript service GetSchemaInfoByTypeName with an invalid TypeName
    Then the service returns the proper error

  Scenario Outline: [GSS-04] A call is made to QueryLayer with valid input
    When a call is made to the Geoscript service QueryLayer with a resultType <resulttype> and featureFormat <featureformat>
    Then the service returns a <featureformat> containing the <resulttype>
    Examples:
      | resulttype | featureformat |
      | results | JSON |
      | results | KML |
      | results | CSV |
      | results | GML2 |
      | results | GML3 |
      | results | GML32 |
      | hits    | JSON |
      | hits    | KML |
      | hits    | CSV |
      | hits    | GML2 |
      | hits    | GML3 |
      | hits    | GML32 |

  Scenario Outline: [GSS-05] A call is made to QueryLayer with a valid sort parameter
    When a call is made to the Geoscript service QueryLayer requesting a JSON sorted by <field>
    Then the service returns a JSON sorted by <field>
    Examples:
      | field             |
      | acquisition_date   |
      | azimuth_angle   |
      | bit_depth   |
      | class_name   |
      | data_type   |
      | description   |
      | entry_id   |
      | filename   |
      | grazing_angle   |
      | gsd_unit   |
      | gsdx   |
      | gsdy   |
      | height   |
      | id   |
      | image_category   |
      | image_representation   |
      | index_id   |
      | ingest_date   |
      | isorce   |
      | mission_id   |

  Scenario: [GSS-06] A call is made to QueryLayer with an invalid sort parameter
    When a call is made to the Geoscript service QueryLayer with an invalid sort parameter
    Then the service returns the proper error

  Scenario Outline: [GSS-07] A call is made to QueryLayer with a single fields parameter
    When a call is made to the Geoscript service QueryLayer requesting a JSON with the field <field>
    Then the service returns a JSON with only the field <field>
    Examples:
      | field             |
      | acquisition_date   |
      | azimuth_angle   |
      | bit_depth   |
      | class_name   |
      | data_type   |
      | description   |
      | entry_id   |
      | filename   |
      | grazing_angle   |
      | gsd_unit   |
      | gsdx   |
      | gsdy   |
      | height   |
      | id   |
      | image_category   |
      | image_representation   |
      | index_id   |
      | ingest_date   |
      | isorce   |
      | mission_id   |

  Scenario Outline: [GSS-08] A call is made to QueryLayer with multiple fields parameters
    When a call is made to the Geoscript service QueryLayer requesting a JSON with the fields <field1> <field2> <field3> <field4>
    Then the service returns a JSON with only the fields <field1> <field2> <field3> <field4>
    Examples:
      | field1               | field2 | field3 | field4 |
      | acquisition_date     |  id   |  |  |
      | acquisition_date    |  id   | height |  |
      | acquisition_date     |  id   | height | isorce |

  Scenario Outline: [GSS-09] A call is made to QueryLayer with a maxium fields parameter
    When a call is made to the Geoscript service QueryLayer requesting a JSON with a max of <max>
    Then the service returns no more than <max> features
    Examples:
      | max |
      | 1   |
      | 5   |
      | 10  |
      | 100 |

  Scenario Outline: [GSS-10] A call is made to QueryLayer with a valid filter parameter
    When a call is made to the Geoscript service QueryLayer requesting a JSON with the filter <filter>
    Then the service returns the proper JSON
    Examples:
      | filter  |
      | in(2056)  |
      | in(1056)  |
      | BBOX(ground_geom,0,0,10,10)  |
      | BBOX(ground_geom,12.44628736879914,41.90886905890596,12.624439958856218,42.043527959643804)  |

  Scenario: [GSS-11] A call is made to QueryLayer with a invalid filter parameter
    When a call is made to the Geoscript service QueryLayer with an invalid filter
    Then the service returns the proper error

  Scenario: [GSS-12] A call is made to QueryLayer with an invalid parameter name
    When a call is made to the Geoscript service QueryLayer with an invalid parameter name
    Then the service returns the proper error

  Scenario: [GSS-13] A call is made to QueryLayer with an invalid resultType
    When a call is made to the Geoscript service QueryLayer with an invalid resultType
    Then the service returns the proper error

  Scenario: [GSS-14] A call is made to QueryLayer with an invalid featureFormat
    When a call is made to the Geoscript service QueryLayer with an invalid featureFormat
    Then the service returns the proper error
