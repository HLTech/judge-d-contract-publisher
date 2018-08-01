package com.hltech.contracts.judged.publisher.integration.judged;

import javax.ws.rs.*;

public interface JudgeDClient {

    @POST
    @Path("/contracts/{serviceName}/{serviceVersion}")
    @Consumes("application/json")
    void publish(@PathParam("serviceName") String serviceName, @PathParam("serviceVersion") String serviceVersion, ServiceContractsForm serviceContracts);

}
