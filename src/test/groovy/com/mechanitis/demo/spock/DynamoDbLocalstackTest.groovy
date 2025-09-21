package com.mechanitis.demo.spock

import spock.lang.Shared
import com.amazonaws.auth.AWSStaticCredentialsProvider
import com.amazonaws.auth.BasicAWSCredentials
import com.amazonaws.client.builder.AwsClientBuilder
import com.amazonaws.services.dynamodbv2.model.AttributeValue
import com.amazonaws.services.dynamodbv2.model.PutItemRequest
import com.amazonaws.services.dynamodbv2.document.DynamoDB
import com.amazonaws.services.dynamodbv2.model.AttributeDefinition
import com.amazonaws.services.dynamodbv2.model.KeySchemaElement
import com.amazonaws.services.dynamodbv2.model.ProvisionedThroughput
import static com.amazonaws.services.dynamodbv2.model.KeyType.HASH
import org.testcontainers.containers.localstack.LocalStackContainer
import static org.testcontainers.containers.localstack.LocalStackContainer.Service.DYNAMODB
import com.amazonaws.services.dynamodbv2.AmazonDynamoDB;
import com.amazonaws.services.dynamodbv2.model.CreateTableRequest
import com.amazonaws.services.dynamodbv2.AmazonDynamoDBClientBuilder

class DynamoDbLocalstackTest extends LocalstackSpecification {

    @Shared
    LocalStackContainer localstack = new LocalStackContainer(localstackImage).withServices(DYNAMODB)
    @Shared
    AmazonDynamoDB dynamoDbClient
    @Shared
    DynamoDB dynamoDB

    @Shared
    def tableName = "Users"
    @Shared
    def id = 1

    def setupSpec() {

        def endpoint = new AwsClientBuilder.EndpointConfiguration(
                localstack.getEndpointOverride(DYNAMODB).toString(),
                localstack.getRegion()
        )

        dynamoDbClient = AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(endpoint)
            .withCredentials(new AWSStaticCredentialsProvider(
                    new BasicAWSCredentials(localstack.getAccessKey(), localstack.getSecretKey())
            ))
            .build()

        dynamoDB = new DynamoDB(dynamoDbClient)

        // Create Table Users
        def attributeDefinitions = new ArrayList<AttributeDefinition>()
        attributeDefinitions.add(new AttributeDefinition()
            .withAttributeName("Id")
            .withAttributeType("N"))

        def keySchema = new ArrayList<KeySchemaElement>()
        keySchema.add(new KeySchemaElement()
            .withAttributeName("Id")
            .withKeyType(HASH))

        def request = new CreateTableRequest()
            .withTableName(tableName)
            .withKeySchema(keySchema)
            .withAttributeDefinitions(attributeDefinitions)
            .withProvisionedThroughput(new ProvisionedThroughput()
                .withReadCapacityUnits(5L)
                .withWriteCapacityUnits(6L))

        def table = dynamoDB.createTable(request)
        table.waitForActive()

        // Insert Item into Table Users
        def attributeValues = new HashMap<String, AttributeValue>()
        attributeValues.put("Id", new AttributeValue().withN(id.toString()))
        attributeValues.put("Email", new AttributeValue().withS("foo@bar.com"))
        attributeValues.put("FullName", new AttributeValue().withS("Foo Bar"))

        PutItemRequest putItemRequest = new PutItemRequest()
            .withTableName(tableName)
            .withItem(attributeValues)
        dynamoDbClient.putItem(putItemRequest)
    }

    def "should access table Users"() {

        expect:
            dynamoDB.getTable(tableName).describe().getTableName() == "Users"
    }

    def "should read inserted User from DynamoDB"() {

        when:
            def user = dynamoDB.getTable(tableName).getItem("Id", id)
        then:
            user.get("Id") == 1
            user.get("Email") == "foo@bar.com"
            user.get("FullName") == "Foo Bar"
    }
}
