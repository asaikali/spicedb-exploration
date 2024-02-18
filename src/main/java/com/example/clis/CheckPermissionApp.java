package com.example.clis;

import com.authzed.api.v1.PermissionService;
import com.authzed.api.v1.PermissionsServiceGrpc;
import com.authzed.grpcutil.BearerToken;
import com.authzed.api.v1.Core.*;
import io.grpc.*;
 
public class CheckPermissionApp {
  public static void main(String[] args) {
    ManagedChannel channel = ManagedChannelBuilder
            .forTarget("localhost:50051")
            .usePlaintext()  
            .build();
  
    BearerToken bearerToken = new BearerToken("foobar");
 
    PermissionsServiceGrpc.PermissionsServiceBlockingStub permissionsService = PermissionsServiceGrpc.newBlockingStub(channel)
            .withCallCredentials(bearerToken);
 
    ZedToken zedToken = ZedToken.newBuilder()
            .setToken("zed_token_value")
            .build();
 
    PermissionService.CheckPermissionRequest request = PermissionService.CheckPermissionRequest.newBuilder()
//            .setConsistency(
//                    PermissionService.Consistency.newBuilder()
//                            .setAtLeastAsFresh(zedToken)
//                            .build())
            .setResource(
                    ObjectReference.newBuilder()
                            .setObjectType("post")
                            .setObjectId("1")
                            .build())
            .setSubject(
                    SubjectReference.newBuilder()
                            .setObject(
                                    ObjectReference.newBuilder()
                                            .setObjectType("user")
                                            .setObjectId("emilia")
                                            .build())
                            .build())
            .setPermission("read")
            .build();
 
    PermissionService.CheckPermissionResponse response;
    try {
      response = permissionsService.checkPermission(request);
      System.out.println(response.getPermissionship());
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}