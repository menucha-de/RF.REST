package havis.net.rest.rf.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import havis.device.rf.exception.ParameterException;

@Provider
public class ParameterExceptionMapper implements ExceptionMapper<ParameterException> {

	@Override
	public Response toResponse(ParameterException ex) {
		return Response.status(Response.Status.BAD_REQUEST).entity(ex.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}

}
