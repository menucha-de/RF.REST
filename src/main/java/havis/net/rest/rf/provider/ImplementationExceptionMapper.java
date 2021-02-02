package havis.net.rest.rf.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import havis.device.rf.exception.ImplementationException;

@Provider
public class ImplementationExceptionMapper implements ExceptionMapper<ImplementationException> {

	@Override
	public Response toResponse(ImplementationException ex) {
		return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(ex.getMessage()).type(MediaType.TEXT_PLAIN)
				.build();
	}
}
