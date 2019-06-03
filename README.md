# omar-cucumber-backend-test
Testing the ingest for https://omar-dev.ossim.io

A java application that runs a cucumber test against the O2 environment sqs-stager service.

# Quickstart

## Gradle Commands

To run backend test in Jenkins or locally
 ```
gradle run
 ```

To build the fat jar
 ```
gradle
 ```

To create dockerfile
 ```
gradle createDockerfile
 ```

 To build the docker image
 ```
gradle buildImage
 ```

## Run Commands

To run the docker image
```
docker run -p 8080:8080 omar-cucumber-backend-test:<tag>
```
To run the Fat Jar (jars must be at the same level of src directory)
```
java -jar <path to jar>/omar-cucumber-backend-test-<version>.jar
```
or
```
java -server -Xms256m -Xmx5400m -XX:+CMSClassUnloadingEnabled -XX:+UseGCOverheadLimit -Djava.security.egd=file:/dev/./urandom -DmainPath=src/main -DresourcePath=src/main/resources -jar <path to jar>/omar-cucumber-backend-test-<version>.jar
```

## Openshift Environment Variables
### Overides the values in the cucumber config file
- TARGET_DEPLOYMENT
- DOMAIN_NAME
- TEST_IMAGE_S3_BUCKET
- TEST_IMAGE_S3_BUCKET_URL
- RBT_CLOUD_ROOT_DIR