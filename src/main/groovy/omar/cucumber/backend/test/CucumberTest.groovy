package omar.cucumber.backend.test

import omar.cucumber.backend.test.TomcatStart

class CucumberTest {
    public void startTest() {
        String main_path = 'omar/cucumber/step_definitions'
        String resource_path = 'src/main/resources'

        if (System.getProperty("mainPath"))
        {
            main_path = System.getProperty("mainPath")
        }

        if (System.getProperty("resourcePath"))
        {
            resource_path = System.getProperty("resourcePath")
        }

        String[] arguments = [
                "--tags",
                "@image_space, @mensa_service, @wfs_image_search, @wms_ortho, @web_coverage_service, @map_tile_service, @omar_geoscript, @o2_basemap, @download_service, @superoverlay_service",
                "--tags",
                "~@C2S",
                '--plugin', "json:src/main/groovy/omar/webapp/reports/json/backend.json",
                '--plugin', "html:src/main/groovy/omar/webapp/reports/html",
                '--plugin', "pretty",
                '--glue', main_path,
                resource_path]

        SecurityManagerCheck secManager = new SecurityManagerCheck()
        System.setSecurityManager(secManager)
        
        try {
            cucumber.api.cli.Main.main(arguments)
        } catch (SecurityException e) {
            //put what you want to happen when exit is called
        }
    }
}