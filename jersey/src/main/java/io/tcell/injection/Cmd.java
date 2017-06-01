/*
 * Copyright (c) 2017 tCell.io
 */

package io.tcell.injection;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author paul on 12/13/16.
 */
@Path("injection/cmd")
public class Cmd {


    @GET
    @Path("shc")
    @Produces(MediaType.TEXT_PLAIN)
    public Response shc(@QueryParam("cmd") String cmd,
                        @QueryParam("showCmd") @DefaultValue("true") boolean showCmd,
                        @QueryParam("showOut") @DefaultValue("true") boolean showOut,
                        @QueryParam("showErr") @DefaultValue("true") boolean showErr) throws IOException {
        String[] execArgs = new String[]{"/bin/sh", "-c", cmd};
        final Process proc = Runtime.getRuntime().exec(execArgs);
        StreamingOutput stream = getResponseStream(showCmd, showOut, showErr, Arrays.toString(execArgs), proc.getInputStream(), proc.getErrorStream());
        return Response.ok(stream).build();
    }

    @GET
    @Path("exec")
    @Produces(MediaType.TEXT_PLAIN)
    public Response exec(@QueryParam("cmd") String cmd,
                         @QueryParam("showCmd") @DefaultValue("true") boolean showCmd,
                         @QueryParam("showOut") @DefaultValue("true") boolean showOut,
                         @QueryParam("showErr") @DefaultValue("true") boolean showErr) throws IOException {
        final Process proc = Runtime.getRuntime().exec(cmd);
        StreamingOutput stream = getResponseStream(showCmd, showOut, showErr, cmd, proc.getInputStream(), proc.getErrorStream());
        return Response.ok(stream).build();
    }


    @GET
    @Path("execTokenized")
    @Produces(MediaType.TEXT_PLAIN)
    public Response execTokenized(@QueryParam("cmd") String cmd,
                                  @QueryParam("showCmd") @DefaultValue("true") boolean showCmd,
                                  @QueryParam("showOut") @DefaultValue("true") boolean showOut,
                                  @QueryParam("showErr") @DefaultValue("true") boolean showErr) throws IOException {
        List<String> list = new ArrayList<String>();
        Matcher m = Pattern.compile("([^\"]\\S*|\".+?\")\\s*").matcher(cmd);
        while (m.find())
            list.add(m.group(1).replace("\"", ""));
        String[] execArg = list.toArray(new String[list.size()]);
        final Process proc = Runtime.getRuntime().exec(execArg);
        StreamingOutput stream = getResponseStream(showCmd, showOut, showErr, Arrays.toString(execArg), proc.getInputStream(), proc.getErrorStream());

        return Response.ok(stream).build();
    }

    @POST
    @Path("execTokenized")
    @Produces(MediaType.TEXT_PLAIN)
    public Response execTokenizedPost(@FormParam("cmd") String cmd,
                                      @FormParam("showCmd") @DefaultValue("true") boolean showCmd,
                                      @FormParam("showOut") @DefaultValue("true") boolean showOut,
                                      @FormParam("showErr") @DefaultValue("true") boolean showErr) throws IOException {
        return execTokenized(cmd, showCmd, showOut, showErr);
    }

    static StreamingOutput getResponseStream(final boolean showCmd, final boolean showOut, final boolean showErr, final String execArg, final InputStream stdout, final InputStream stderr) {
        return new StreamingOutput() {
            @Override
            public void write(OutputStream os) throws IOException {
                if (showCmd) {
                    os.write(("Command: [" + execArg + "]\n").getBytes());
                }
                if (showOut) {
                    if (showCmd || showErr)
                        os.write("********** STDOUT **********\n".getBytes());
                    copy(stdout, os);
                }
                if (showErr) {
                    os.write("********** STDERR **********\n".getBytes());
                    copy(stderr, os);
                }
            }
        };
    }

    public static long copy(InputStream source, OutputStream sink)
            throws IOException {
        long nread = 0L;
        byte[] buf = new byte[8192];
        int n;
        while ((n = source.read(buf)) > 0) {
            sink.write(buf, 0, n);
            nread += n;
        }
        return nread;
    }
}
