targetDeployment = System.getenv("TARGET_DEPLOYMENT")
if (!targetDeployment) {
    targetDeployment = "dev"
}

domainName = System.getenv("DOMAIN_NAME")
if (!domainName) {
    domainName = "ossim.io"
}

s3Bucket = System.getenv("TEST_IMAGE_S3_BUCKET")
if (!s3Bucket) {
    s3Bucket = "o2-test-data/Standard_test_imagery_set"
}

s3BucketUrl = System.getenv("TEST_IMAGE_S3_BUCKET_URL")
if (!s3BucketUrl) {
    s3BucketUrl = "https://s3.amazonaws.com"
}

rbtcloudRootDir = System.getenv("RBT_CLOUD_ROOT_DIR")
if (!rbtcloudRootDir) {
    rbtcloudRootDir = "https://omar-${targetDeployment}.${domainName}"
}

s3BasemapUrlList = "NOT_ASSIGNED"
//rbtcloudRootDir = "https://omar-${targetDeployment}.${domainName}"
switch(targetDeployment) {
    case "stage":
        s3BasemapUrlList = "Basemaptest-stage.txt"
        break
    case "prod":
        s3BasemapUrlList = "Basemaptest-prod.txt"
        break
    case "blue":
        s3BasemapUrlList = "Basemaptest-prod.txt"
        break
    case "green":
        s3BasemapUrlList = "Basemaptest-prod.txt"
        break
    case "rel":
        s3BasemapUrlList = "Basemaptest-rel.txt"
        break
    case "dev":
        s3BasemapUrlList = "Basemaptest-dev.txt"
        break
    default:
        println("\nBad TARGET_DEPLOYMENT provided: <${targetDeployment}>. Defaulting to dev.")
        s3BasemapUrlList = "Basemaptest-dev.txt"
        rbtcloudRootDir = "https://omar-dev.${domainName}"
        break
}
s3WcsVerificationFiles = "WCS_verification_images"
s3BasemapVerificationFiles = "Basemap_verification_images"

println("\nOMAR URL being tested: ${rbtcloudRootDir}\n")

downloadService = "${rbtcloudRootDir}/omar-download"
stagingService = "${rbtcloudRootDir}/omar-stager/dataManager"
wfsServerProperty = "${rbtcloudRootDir}/omar-wfs/wfs"
wmsServerProperty = "${rbtcloudRootDir}/omar-wms/wms"
wcsServerProperty = "${rbtcloudRootDir}/omar-wcs/wcs"
wmtsServerProperty = "${rbtcloudRootDir}/omar-wmts/wmts"
geoscriptService = "${rbtcloudRootDir}/omar-geoscript/geoscriptApi"
imageSpaceServerProperty = "${rbtcloudRootDir}/omar-oms/imageSpace"
superOverlayProperty = "${rbtcloudRootDir}/omar-superoverlay/superOverlay"
ngtService = "${rbtcloudRootDir}/ngt-service/ngt"
jpipService = "${rbtcloudRootDir}/omar-jpip/jpip"


mensaUrl = "${rbtcloudRootDir}/omar-mensa"
wfsUrl = "${rbtcloudRootDir}/omar-wfs"
wmsUrl = "${rbtcloudRootDir}/omar-wms"
wmtsUrl = "${rbtcloudRootDir}/omar-wmts"
omarOldmarProxy = "${rbtcloudRootDir}/omar"
//wfsPostString = """<?xml version="1.0" encoding="UTF-8" standalone="yes"?><wfs:GetFeature xmlns:wfs="http://www.opengis.net/ogc" version="1.0.0" resultType="results" maxFeatures="20"><wfs:Query srsName="EPSG:4326" typeName="omar:raster_entry"><Filter xmlns="http://www.opengis.net/ogc" xmlns:gml="http://www.opengis.net/gml"><And><And><PropertyIsGreaterThanOrEqualTo><PropertyName>acquisition_date</PropertyName><Literal>2000-12-13T00:00:00Z</Literal></PropertyIsGreaterThanOrEqualTo><PropertyIsLessThan><PropertyName>acquisition_date</PropertyName><Literal>2017-12-14T00:00:00Z</Literal></PropertyIsLessThan></And><BBox areanamehint="temp area 9"><PropertyName>ground_geom</PropertyName><gml:Envelope srsName="CRS84"><gml:lowerCorner>138.9577467 -23.5650826</gml:lowerCorner><gml:upperCorner>138.9745629 -23.5502934</gml:upperCorner></gml:Envelope></BBox></And></Filter></wfs:Query></wfs:GetFeature>"""
geoscriptDefaultMax = 15

// minutes to wait
waitForStage = 5

// The field names ued in the avro message.
// I set it here because highside and lowside are different
image_id_field_name = "imageId"
observation_date_time_field_name = "observationDateTime"
url_field_name = "uRL"


// Image to be ingested and the information associated with them
image_files = [
        download_data:[
                images:[
                        image_01:[
                                image_id:"04DEC11050020-M2AS_R1C1-000000185964_01_P001",
                                file_name:"04DEC11050020-M2AS_R1C1-000000185964_01_P001.TIF",
                                url:"o2-test-data/Standard_test_imagery_set/QuickBird/TIFF/Multi/04DEC11050020-M2AS_R1C1-000000185964_01_P001.TIF",
                                image_type:"",
                                bbox:""
                        ],
                        image_02:[
                                image_id:"14SEP12113301-P1BS-053951940020_01_P001",
                                file_name:"14SEP12113301-P1BS-053951940020_01_P001.TIF",
                                url:"o2-test-data/Standard_test_imagery_set/WorldView/WV2/GeoTIFF/Pan/14SEP12113301-P1BS-053951940020_01_P001.TIF",
                                image_type:"",
                                bbox:""
                        ],
                        image_03:[
                                image_id:"14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG",
                                file_name:"14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG.ntf",
                                url:"o2-test-data/Standard_test_imagery_set/TerraSAR-X/NITF2_0/14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG.ntf",
                                image_type:"",
                                bbox:""
                        ]
                ]
        ],
        superoverlay_data:[
                images:[
                        image_01:[
                                image_id:"4DEC11050020-M2AS_R1C1-000000185964_01_P001",
                                file_name:"4DEC11050020-M2AS_R1C1-000000185964_01_P001.tiff",
                                url:"o2-test-data/Standard_test_imagery_set/QuickBird/TIFF/Multi/04DEC11050020-M2AS_R1C1-000000185964_01_P001.TIF",
                                image_type:"",
                                bbox:""
                        ]
                ]
        ],
        jpip_data:[
                images:[
                        image_01:[
                                image_id:"po_106005_pan_0000000.ntf",
                                file_name:"po_106005_pan_0000000.ntf",
                                url:"/data/s3/2009/02/05/00/ntf/po_106005_pan_0000000.ntf",
                                image_type:"",
                                bbox:""
                        ]
                ]
        ],
        ngt_data:[
                images:[
                        image_01:[
                                image_id:"A",
                                file_name:"A.NTF",
                                url:"/data/s3/msp/Stereo/test/A.NTF",
                                image_type:"",
                                bbox:""
                        ],
                        image_02:[
                                image_id:"B",
                                file_name:"B.NTF",
                                url:"/data/s3/msp/Stereo/test/B.NTF",
                                image_type:"",
                                bbox:""
                        ],
                        image_03:[
                                image_id:"output",
                                file_name:"output",
                                url:"/data/s3/msp/Stereo/test/output",
                                image_type:"",
                                bbox:""
                        ]
                ]
        ],
        wfs_data:[
                images:[
                        image_01:[
                                image_id:"11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003",
                                file_name:"11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003.ntf",
                                url:"o2-test-data/Standard_test_imagery_set/WorldView/WV2/NITF/11MAR08WV010500008MAR11071429-P1BS-005707719010_04_P003.ntf",
                                bbox:"45.0, 22.5, 67.5, 45.0",
                                polygon:"51.05072021484375, 35.57304382324219, 51.05072021484375, 35.80101013183594, 51.89666748046875, 35.80101013183594, 51.89666748046875, 35.57304382324219, 51.05072021484375, 35.57304382324219",
                                point:"51.458587646484375, 35.687713623046875",
                                radius:"20",
                                be_number:"",
                                target_id:"",
                                country_code:"IR",
                                start_date:"01-27-2007",
                                end_date:"01-29-2010",
                                sensor_type:"AA"
                        ],
                        image_02:[
                                image_id:"05FEB09OV05010005V090205P0001912264B220000100282M_001508507",
                                file_name:"05FEB09OV05010005V090205P0001912264B220000100282M_001508507.ntf",
                                url:"o2-test-data/Standard_test_imagery_set/GeoEye/NITF2_1/Pan/05FEB09OV05010005V090205P0001912264B220000100282M_001508507.ntf",
                                bbox:"",
                                polygon:"",
                                point:"",
                                radius:"",
                                be_number:"",
                                target_id:"",
                                country_code:"AS",
                                start_date:"",
                                end_date:"",
                                sensor_type:"AA"
                        ],
                        image_03:[
                                image_id:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507",
                                file_name:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507.ntf",
                                url:"o2-test-data/Standard_test_imagery_set/GeoEye/NITF2_1/multi/05FEB09OV05010005V090205M0001912264B220000100072M_001508507.ntf",
                                bbox:"",
                                polygon:"",
                                point:"",
                                radius:"",
                                be_number:"",
                                target_id:"",
                                country_code:"AS",
                                start_date:"",
                                end_date:"",
                                sensor_type:""
                        ],
                        image_04:[
                                image_id:"14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG",
                                file_name:"14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG.ntf",
                                url:"o2-test-data/Standard_test_imagery_set/TerraSAR-X/NITF2_0/14SEP15TS0107001_100021_SL0023L_25N121E_001X___SVV_0101_OBS_IMAG.ntf",
                                bbox:"",
                                polygon:"",
                                point:"",
                                radius:"",
                                be_number:"BE12345",
                                target_id:"BE12345",
                                country_code:"",
                                start_date:"",
                                end_date:"",
                                sensor_type:""
                        ]
                ]
        ],
        geoscript_data:[
                images:[
                        image_01:[
                                results:"JSON,KML,CSV,GML2,GML3,GML32",
                                hits:"JSON,KML,CSV,GML2,GML3,GML32",
                                sort:"acquisition_date,azimuth_angle,bit_depth,class_name,data_type,description,entry_id,filename,grazing_angle,gsd_unit,gsdx,gsdy,height,id,image_category,image_representation,index_id,ingest_date,isorce,mission_id",
                                multi_sort:"acquisition_date,id,height,isorce",
                                filter:"in(2056);in(1056);BBOX(ground_geom,0,0,10,10);BBOX(ground_geom,12.44628736879914,41.90886905890596,12.624439958856218,42.043527959643804)"
                        ]
                ]
        ],
        wmts_data:[
                images:[
                        image_01:[
                                image_id:"po_106005_pan_0000000",
                                file_name:"po_106005_pan_0000000.ntf",
                                url:"",
                                image_type:"jpeg",
                                bbox:""
                        ]
                ]
        ],
        wcs_data:[
                images:[
                        image_01:[
                                image_id:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507",
                                file_name:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507.tiff",
                                url:"o2-test-data/Standard_test_imagery_set/WCS_verification_images/05FEB09OV05010005V090205M0001912264B220000100072M_001508507.tiff",
                                image_type:"tiff",
                                bbox:"145,-45,149,-41;147.225,-42.8,147.275,-42.83"
                        ]
                ]
        ],
        wms_data:[
                images:[
                        image_01:[
                                image_id:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507",
                                verification_image:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507_dev",
                                image_type:"jpeg,png"
                        ],
                        image_02:[
                                image_id:"05FEB09OV05010005V090205P0001912264B220000100282M_001508507",
                                verification_image:"05FEB09OV05010005V090205P0001912264B220000100282M_001508507_chip_dev",
                                image_type:"png"
                        ],
                        image_03:[
                                image_id:"16MAY02111606-P1BS-055998375010_01_P013",
                                verification_image:"",
                                image_type:""
                        ],
                        image_04:[
                                image_id:"16MAY02111607-P1BS-055998375010_01_P014",
                                verification_image:"",
                                image_type:""
                        ]
                ]
        ],
        mensa_data:[
                images:[
                        image_01:[
                                image_id:"05FEB09OV05010005V090205P0001912264B220000100282M_001508507",
                                file_name:"",
                                url:"",
                                image_type:"",
                                bbox:""
                        ]
                ]
        ],
        image_space_data:[
                images:[
                        image_01:[
                                image_id:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507",
                                verification_image:"05FEB09OV05010005V090205M0001912264B220000100072M_001508507",
                                image_type:"png"
                        ],
                        image_02:[
                                image_id:"04DEC11050020-M2AS_R1C1-000000185964_01_P001",
                                verification_image:"04DEC11050020-M2AS_R1C1-000000185964_01_P001_thumbnail",
                                image_type:"png"
                        ],
                        image_03:[
                                image_id:"14SEP12113301-M1BS-053951940020_01_P001",
                                verification_image:"14SEP12113301-M1BS-053951940020_01_P001_rgb",
                                image_type:"png"
                        ],
                        image_04:[
                                image_id:"14SEP12113301-M1BS-053951940020_01_P001",
                                verification_image:"14SEP12113301-M1BS-053951940020_01_P001_gbr",
                                image_type:"png"
                        ],
                        image_05:[
                                image_id:"14SEP12113301-M1BS-053951940020_01_P001",
                                verification_image:"14SEP12113301-M1BS-053951940020_01_P001_g",
                                image_type:"png"
                        ]
                ]
        ]
]