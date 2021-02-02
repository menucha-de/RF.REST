package havis.net.rest.rf.provider;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

import havis.device.rf.exception.CommunicationException;

@Provider
public class CommunicationExceptionMapper implements ExceptionMapper<CommunicationException> {

	@Override
	public Response toResponse(CommunicationException ex) {
		return Response.status(Response.Status.BAD_GATEWAY).entity(ex.getMessage()).type(MediaType.TEXT_PLAIN).build();
	}

}
