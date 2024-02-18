package com.example.clis;

import com.authzed.api.v1.SchemaServiceGrpc;
import com.authzed.api.v1.SchemaServiceOuterClass.*;
import com.authzed.grpcutil.BearerToken;
import io.grpc.*;
 
public class CreateSchemaApp {
  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder
            .forTarget("localhost:50051")
            .usePlaintext()
            .build();
 
    BearerToken bearerToken = new BearerToken("foobar");
    SchemaServiceGrpc.SchemaServiceBlockingStub schemaService = SchemaServiceGrpc.newBlockingStub(channel)
            .withCallCredentials(bearerToken);
 
    String schema = """
            definition user {}
            
            definition post {
                relation reader: user
                relation writer: user
                relation owner: user
                
                permission read = reader + writer
                permission write = writer
            }
            """;
 
    WriteSchemaRequest request = WriteSchemaRequest
            .newBuilder()
            .setSchema(schema)
            .build();
 
    WriteSchemaResponse response;
    try {
      response = schemaService.writeSchema(request);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}