package havis.net.rest.rf;

import havis.device.rf.RFDevice;
import havis.net.rest.rf.provider.CommunicationExceptionMapper;
import havis.net.rest.rf.provider.ConnectionExceptionMapper;
import havis.net.rest.rf.provider.ImplementationExceptionMapper;
import havis.net.rest.rf.provider.ParameterExceptionMapper;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.core.Application;
import javax.ws.rs.ext.Providers;

public class RESTApplication extends Application {

	private final static String PROVIDERS = Providers.class.getName();
	private final static Map<String, Object> properties = new HashMap<>();

	private final Set<Object> singletons = new HashSet<Object>();

	static {
		properties.put(PROVIDERS, new Class<?>[] { CommunicationExceptionMapper.class, ConnectionExceptionMapper.class, ImplementationExceptionMapper.class,
				ParameterExceptionMapper.class });
	}

	public RESTApplication(RFDevice device) {
		singletons.add(new RFDeviceService(device));
	}

	@Override
	public Set<Object> getSingletons() {
		return singletons;
	}

	@Override
	public Map<String, Object> getProperties() {
		return properties;
	}
}