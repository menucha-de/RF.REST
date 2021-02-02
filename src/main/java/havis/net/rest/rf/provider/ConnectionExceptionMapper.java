package havis.net.rest.rf.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import havis.device.rf.exception.ConnectionException;

@Provider
public class ConnectionExceptionMapper implements ExceptionMapper<ConnectionException> {

	@Override
	public Response toResponse(ConnectionException ex) {
		return Response.status(Response.Status.SERVICE_UNAVAILABLE).entity(ex.getMessage()).type(MediaType.TEXT_PLAIN)
				.build();
	}
}
