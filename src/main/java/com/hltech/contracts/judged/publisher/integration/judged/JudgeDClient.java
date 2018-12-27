package com.hltech.contracts.judged.publisher.integration.judged;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

public interface JudgeDClient {

    @POST
    @Path("/new/contracts/{serviceName}/{serviceVersion}")
    @Consumes("application/json")
    void publish(@PathParam("serviceName") String serviceName, @PathParam("serviceVersion") String serviceVersion, ServiceContractsForm serviceContracts);

}
