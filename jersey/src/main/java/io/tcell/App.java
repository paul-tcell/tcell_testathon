/*
 * Copyright (c) 2017 tCell.io
 */

package io.tcell;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

/**
 * @author paul on 3/13/17.
 */
@Path("/")
public class App {

    @GET
    @Produces(MediaType.TEXT_PLAIN)
    public Response iAmTheRootURL(@QueryParam("parameter") String parameter) {
        return Response.ok("").build();
    }

    @GET
    @Path("status/{status}")
    public Response status(@PathParam("status") String status) {
        return Response.status(new Integer(status)).build();
    }

    @GET
    @Path("internal-server-error")
    public Response internalServerError() {
        throw new RuntimeException("This should cause an internal server error.");
    }

    @GET
    @Path("response-size/{size}")
    @Produces(MediaType.TEXT_PLAIN)
    public Response responseSize(@PathParam("size") String size) {
        String s = IntStream.range(0, Integer.parseInt(size)).boxed()
                .map(i -> "a").collect(Collectors.joining());
        return Response.ok(s).build();
    }

    @POST
    @Path("users")
    @Produces(MediaType.TEXT_PLAIN)
    public Response users() {
        return Response.ok("").build();
    }

    @GET
    @Path("redirect-me")
    public Response redirect(@QueryParam("desired-url") String desiredUrl) {
        return Response.status(302).header("Location", desiredUrl).build();
    }
    
    @POST
    @Path("/gobble-json")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response gobbleJSON(String jsonRequest){
        return Response.ok(jsonRequest).build();
    }
    

}
