package havis.net.rest.rf;

import havis.device.rf.RFConsumer;
import havis.device.rf.RFDevice;
import havis.device.rf.exception.ConnectionException;
import havis.device.rf.exception.ImplementationException;
import havis.device.rf.tag.TagData;
import havis.device.rf.tag.operation.TagOperation;

import java.util.List;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.naming.NamingException;

public class RFConsumerREST implements RFConsumer {

	Logger log = Logger.getLogger(RFConsumerREST.class.getName());
	
	private static final int TIME_OUT = 3000;

	private Lock lock = new ReentrantLock();
	private RFDevice device;
	private boolean connected;

	public RFConsumerREST(RFDevice device) {
		try {
			lock.lock();

			if (this.device == device)
				return;

			if (connected) {
				try {
					this.device.closeConnection();
				} catch (ConnectionException e) {
					e.printStackTrace();
				}
			}
			connected = false;
			this.device = device;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void connectionAttempted() {
		try {
			lock.lock();

			if (connected) {
				connected = false;
				device.closeConnection();
			}
		} catch (ConnectionException e) {
			log.log(Level.SEVERE, "Failed to close connection", e);
		} finally {
			lock.unlock();
		}
	}

	/**
	 * Get the RFDevice
	 * 
	 * @return RFDevice
	 * @throws ImplementationException
	 * @throws ConnectionException
	 * @throws NamingException
	 */
	public RFDevice getOpenController() throws ConnectionException,
			ImplementationException {
		try {
			lock.lock();

			if (device == null) {
				throw new ConnectionException(
						"No RF controller instance available.");
			}

			if (!connected) {
				device.openConnection(this, TIME_OUT);
				connected = true;
			}

			return device;
		} finally {
			lock.unlock();
		}
	}

	@Override
	public List<TagOperation> getOperations(TagData arg0) {
		return null;
	}

	@Override
	public void keepAlive() {
	}
}