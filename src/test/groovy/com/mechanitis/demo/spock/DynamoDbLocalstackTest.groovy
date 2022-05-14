package com.mechanitis.demo.spock

import spock.lang.Shared
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
    def tableName = "table_for_tests"

    def setupSpec() {

        dynamoDbClient = AmazonDynamoDBClientBuilder
            .standard()
            .withEndpointConfiguration(localstack.getEndpointConfiguration(DYNAMODB))
            .withCredentials(localstack.getDefaultCredentialsProvider())
            .build()

        dynamoDB = new DynamoDB(dynamoDbClient)

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
    }

    def "should access table_for_tests"() {

        expect:
            dynamoDB.getTable(tableName).describe().getTableName() == "table_for_tests"
    }
}
