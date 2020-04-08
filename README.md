## Using AWS S3 Bucket with Java
This repository is an example of how to use AWS S3 Bucket for uploading, 
retrieving, downloading and deleting files.

### Requirements
* Java
* Maven

### Configuration
To use this code sample you need to change the following configuration properties in **__application.yml__** file according to your S3 bucket credentials:

* endpointUrl
* accessKey
* secretKey
* bucketName

### How to Run
* From IDEA: run the **__MainApplication.class__**
* From CLI: run command `mvn spring-boot:run` 