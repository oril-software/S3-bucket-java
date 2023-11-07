## Using AWS S3 Bucket with Java
This repository is an example of how to use AWS S3 Bucket for uploading, 
retrieving, downloading and deleting files.

### Prerequisites
* Java 11+
* Maven ^3.6.0
* AWS Account
* [IAM user](https://docs.aws.amazon.com/IAM/latest/UserGuide/id_users_create.html) on AWS account

### Configuration
To use this code sample you need to change the following configuration properties in **__application.yml__** file according to your S3 bucket credentials:

* accessKey
* secretKey
* bucketName

### How to Run
* From IDEA: run the **__MainApplication.class__**
* From CLI: run command `mvn spring-boot:run` 

### Community
* Please send us your suggestions on how we make this code even more useful for the development community or contribute to this repo!
* Check out our [blog](https://oril.co/blog) with more articles!