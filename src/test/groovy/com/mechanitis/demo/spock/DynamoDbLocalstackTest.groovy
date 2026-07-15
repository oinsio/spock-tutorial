package com.mechanitis.demo.spock

import spock.lang.Shared
import org.testcontainers.containers.localstack.LocalStackContainer
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials
import software.amazon.awssdk.auth.credentials.StaticCredentialsProvider
import software.amazon.awssdk.regions.Region
import software.amazon.awssdk.services.dynamodb.DynamoDbClient
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition
import software.amazon.awssdk.services.dynamodb.model.AttributeValue
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement
import software.amazon.awssdk.services.dynamodb.model.KeyType
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType

class DynamoDbLocalstackTest extends LocalstackSpecification {

    @Shared
    LocalStackContainer localstack = new LocalStackContainer(localstackImage).withServices(DYNAMODB)
    @Shared
    DynamoDbClient dynamoDbClient

    @Shared
    def tableName = "Users"
    @Shared
    def id = 1

    def setupSpec() {

        dynamoDbClient = DynamoDbClient.builder()
            .endpointOverride(localstack.getEndpointOverride(DYNAMODB))
            .credentialsProvider(StaticCredentialsProvider.create(
                AwsBasicCredentials.create(localstack.getAccessKey(), localstack.getSecretKey())
            ))
            .region(Region.of(localstack.getRegion()))
            .build()

        // Create Table Users
        dynamoDbClient.createTable(CreateTableRequest.builder()
            .tableName(tableName)
            .attributeDefinitions(AttributeDefinition.builder()
                .attributeName("Id")
                .attributeType(ScalarAttributeType.N)
                .build())
            .keySchema(KeySchemaElement.builder()
                .attributeName("Id")
                .keyType(KeyType.HASH)
                .build())
            .provisionedThroughput(ProvisionedThroughput.builder()
                .readCapacityUnits(5L)
                .writeCapacityUnits(6L)
                .build())
            .build())

        dynamoDbClient.waiter().waitUntilTableExists { it.tableName(tableName) }

        // Insert Item into Table Users
        dynamoDbClient.putItem(PutItemRequest.builder()
            .tableName(tableName)
            .item([
                "Id"      : AttributeValue.builder().n(id.toString()).build(),
                "Email"   : AttributeValue.builder().s("foo@bar.com").build(),
                "FullName": AttributeValue.builder().s("Foo Bar").build()
            ])
            .build())
    }

    def "should access table Users"() {

        expect:
            dynamoDbClient.describeTable { it.tableName(tableName) }.table().tableName() == "Users"
    }

    def "should read inserted User from DynamoDB"() {

        when:
            def user = dynamoDbClient.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(["Id": AttributeValue.builder().n(id.toString()).build()])
                .build()).item()
        then:
            user.get("Id").n() == "1"
            user.get("Email").s() == "foo@bar.com"
            user.get("FullName").s() == "Foo Bar"
    }
}
