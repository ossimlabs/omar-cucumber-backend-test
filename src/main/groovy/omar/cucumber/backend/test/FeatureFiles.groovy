package omar.cucumber.backend.test

import omar.cucumber.config.CucumberConfig
import groovy.text.*

class FeatureFiles {
    static void create() {
        def config = CucumberConfig.config
        config.featureTemplateFiles.templates.each{ files ->
            ArrayList imageDataFromConfigList = new ArrayList()
            File templateFile = new File("${files.getValue().template_file}")
            File featureFile =  new File("${files.getValue().feature_file}")
            
            config.image_files.each{ imagesList -> 
                if(files.getValue().data == imagesList.getKey().toString())
                {
                    imagesList.getValue().images.each{ imageData ->
                        imageData.each { imageInfo ->
                            String data = imagesList.getKey().toString()
                            HashMap imageDataFromConfigMap = new HashMap()
                            switch(data){
                                case "wfs_data":
                                    imageDataFromConfigMap.put("imageId",imageInfo.getValue().image_id)
                                    imageDataFromConfigMap.put("fileName",imageInfo.getValue().file_name)
                                    imageDataFromConfigMap.put("url",imageInfo.getValue().url)
                                    imageDataFromConfigMap.put("bbox",imageInfo.getValue().bbox)
                                    imageDataFromConfigMap.put("polygon",imageInfo.getValue().polygon)
                                    imageDataFromConfigMap.put("point",imageInfo.getValue().point)
                                    imageDataFromConfigMap.put("radius",imageInfo.getValue().radius)
                                    imageDataFromConfigMap.put("beNumber",imageInfo.getValue().be_number)
                                    imageDataFromConfigMap.put("targetId",imageInfo.getValue().target_id)
                                    imageDataFromConfigMap.put("countryCode",imageInfo.getValue().country_code)
                                    imageDataFromConfigMap.put("startDate",imageInfo.getValue().start_date)
                                    imageDataFromConfigMap.put("endDate",imageInfo.getValue().end_date)
                                    imageDataFromConfigMap.put("sensorType",imageInfo.getValue().sensor_type)
                                    imageDataFromConfigList.add(imageDataFromConfigMap)
                                    break
                                case "geoscript_data":
                                    imageDataFromConfigMap.put("results",imageInfo.getValue().results)
                                    imageDataFromConfigMap.put("hits",imageInfo.getValue().hits)
                                    imageDataFromConfigMap.put("sorted",imageInfo.getValue().sort)
                                    imageDataFromConfigMap.put("multiSorted",imageInfo.getValue().multi_sort)
                                    imageDataFromConfigMap.put("filter",imageInfo.getValue().filter)
                                    imageDataFromConfigList.add(imageDataFromConfigMap)
                                    break
                                case "wms_data":
                                    imageDataFromConfigMap.put("imageId",imageInfo.getValue().image_id)
                                    imageDataFromConfigMap.put("image2Id",imageInfo.getValue().image2_id)
                                    imageDataFromConfigMap.put("verifiImage",imageInfo.getValue().verification_image)
                                    imageDataFromConfigMap.put("imageType",imageInfo.getValue().image_type)
                                    imageDataFromConfigMap.put("bbox",imageInfo.getValue().bbox)
                                    imageDataFromConfigList.add(imageDataFromConfigMap)
                                    break
                                case "image_space_data":
                                    imageDataFromConfigMap.put("imageId",imageInfo.getValue().image_id)
                                    imageDataFromConfigMap.put("verifiImage",imageInfo.getValue().verification_image)
                                    imageDataFromConfigMap.put("imageType",imageInfo.getValue().image_type)
                                    imageDataFromConfigList.add(imageDataFromConfigMap)
                                    break
                                default:
                                    imageDataFromConfigMap.put("imageId",imageInfo.getValue().image_id)
                                    imageDataFromConfigMap.put("fileName",imageInfo.getValue().file_name)
                                    imageDataFromConfigMap.put("url",imageInfo.getValue().url)
                                    imageDataFromConfigMap.put("imageType",imageInfo.getValue().image_type)
                                    imageDataFromConfigMap.put("bbox",imageInfo.getValue().bbox)
                                    imageDataFromConfigList.add(imageDataFromConfigMap)
                                    break
                            }
                        }
                    }
                
                }             
            }

            HashMap imageList = ["files":imageDataFromConfigList]
            SimpleTemplateEngine engine = new SimpleTemplateEngine()
            Template template = engine.createTemplate(templateFile)
            Writable writable = template.make(imageList)
            featureFile.write(writable.toString())
        }
    }
}