package com.mechanitis.demo.spock

import com.amazonaws.services.s3.AmazonS3
import com.amazonaws.services.s3.AmazonS3ClientBuilder
import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.core.sync.RequestBody
import software.amazon.awssdk.regions.Region
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.S3
import org.testcontainers.containers.localstack.LocalStackContainer
import spock.lang.Shared
import software.amazon.awssdk.services.s3.S3Client

class S3LocalstackTest extends LocalstackSpecification {

    @Shared
    LocalStackContainer localstackS3 = new LocalStackContainer(localstackImage).withServices(S3)

    @Shared
    def bucketName = "foo"
    @Shared
    def key = "bar"
    @Shared
    def content = "baz"

    def "should work with AWS SDK v1"() {

        given:
            AmazonS3 s3 = AmazonS3ClientBuilder
                .standard()
                .withEndpointConfiguration(localstackS3.getEndpointConfiguration(S3))
                .withCredentials(localstackS3.getDefaultCredentialsProvider())
                .build()
            s3.createBucket(bucketName)
            s3.putObject(bucketName, key, content)
        when:
            def objectContent = s3.getObject(bucketName, key).getObjectContent()
            def reader = new BufferedReader(new InputStreamReader(objectContent))
            def result = reader.readLine()
        then:
            result == "baz"
        cleanup:
            reader.close()
            objectContent.close()
            s3.deleteObject(bucketName, key)
            s3.deleteBucket(bucketName)
    }

    def "should work with AWS SDK v2"() {

        given:
            S3Client s3 = S3Client
                .builder()
                .endpointOverride(localstackS3.getEndpointOverride(S3))
                .credentialsProvider(StaticCredentialsProvider.create(AwsBasicCredentials.create(
                    localstackS3.getAccessKey(), localstackS3.getSecretKey()
                )))
                .region(Region.of(localstackS3.getRegion()))
                .build()
            s3.createBucket(b -> b.bucket(bucketName))
            s3.putObject(b -> b.bucket(bucketName).key(key), RequestBody.fromBytes(content.getBytes()))
        when:
            def objectContent =
                s3.getObject(b -> b.bucket(bucketName).key(key))
            def reader = new BufferedReader(new InputStreamReader(objectContent))
            def result = reader.readLine()
        then:
            result == "baz"
        cleanup:
            reader.close()
            objectContent.close()
            s3.deleteObject(b -> b.bucket(bucketName).key(key))
            s3.deleteBucket(b -> b.bucket(bucketName))
    }
}
