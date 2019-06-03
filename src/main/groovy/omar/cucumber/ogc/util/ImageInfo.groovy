package omar.cucumber.ogc.util

import omar.cucumber.config.CucumberConfig

class TestImageInfo
{
   def config = CucumberConfig.config

   HashMap getImageInfo(String id, String app_data) {
      HashMap fileInfo = new HashMap()
      config.image_files.each{ imagesList ->
         if(app_data == imagesList.getKey())
         {
            imagesList.getValue().images.each{ imageData ->
                  imageData.each { imageInformation ->
                     if (id == imageInformation.getValue().image_id)
                     {
                        fileInfo.put("image_id",imageInformation.getValue().image_id.toString())
                        //fileInfo.put("verification_image",imageInformation.getValue().verification_image.toString())
                     }
                  }
            }
         }        
      }
      return fileInfo
   }

}