package com.example.clis;

import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.grpcutil.BearerToken;
import com.authzed.api.v1.Core.*;
import io.grpc.*;
 
public class WriteTupleApp {
  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder
            .forTarget("localhost:50051")
            .usePlaintext()
            .build();
  
    BearerToken bearerToken = new BearerToken("foobar");
    PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService = PermissionsServiceGrpc.newBlockingStub(channel)
            .withCallCredentials(bearerToken);
 
    PermissionService.WriteRelationshipsRequest request = PermissionService.WriteRelationshipsRequest.newBuilder()
            .addUpdates(
                    RelationshipUpdate.newBuilder()
                            .setOperation(RelationshipUpdate.Operation.OPERATION_CREATE)
                            .setRelationship(
                                    Relationship.newBuilder()
                                            .setResource(
                                                    ObjectReference.newBuilder()
                                                            .setObjectType("post")
                                                            .setObjectId("1")
                                                            .build())
                                            .setRelation("writer")
                                            .setSubject(
                                                    SubjectReference.newBuilder()
                                                            .setObject(
                                                                    ObjectReference.newBuilder()
                                                                            .setObjectType("user")
                                                                            .setObjectId("emilia")
                                                                            .build())
                                                            .build())
                                            .build())
                            .build())
            .build();
 
    PermissionService.WriteRelationshipsResponse response;
    try {
      response = permissionsService.writeRelationships(request);
      String zedToken = response.getWrittenAt().getToken();
    } catch (Exception e) {
      // Uh oh!
    }
  }
}