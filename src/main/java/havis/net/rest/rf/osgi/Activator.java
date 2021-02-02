package havis.net.rest.rf.osgi;

import java.util.logging.Logger;

import javax.ws.rs.core.Application;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.util.tracker.ServiceTracker;

import havis.device.rf.RFDevice;
import havis.net.rest.rf.RESTApplication;

public class Activator implements BundleActivator {

	Logger log = Logger.getLogger(Activator.class.getName());

	private ServiceRegistration<Application> registration;
	private ServiceTracker<RFDevice, RFDevice> tracker;

	@Override
	public void start(BundleContext context) throws Exception {

		tracker = new ServiceTracker<RFDevice, RFDevice>(context,
				RFDevice.class, null) {
			@Override
			public RFDevice addingService(ServiceReference<RFDevice> reference) {
				RFDevice service = super.addingService(reference);
				registration = context.registerService(Application.class, new RESTApplication(service), null);
				return service;
			}

			@Override
			public void removedService(ServiceReference<RFDevice> reference,
					RFDevice service) {
				registration.unregister();
				super.removedService(reference, service);
			}
		};
		tracker.open();

	}

	@Override
	public void stop(BundleContext context) throws Exception {
		tracker.close();
	}
}