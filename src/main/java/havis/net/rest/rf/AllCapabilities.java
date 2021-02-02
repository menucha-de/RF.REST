package havis.net.rest.rf;

import java.util.List;

import havis.device.rf.capabilities.DeviceCapabilities;
import havis.device.rf.capabilities.RegulatoryCapabilities;
import havis.device.rf.configuration.AntennaProperties;

public class AllCapabilities {
	private DeviceCapabilities device;
	private RegulatoryCapabilities regulatory;
	private List<AntennaProperties> antennaProperties;

	public List<AntennaProperties> getAntennaProperties() {
		return antennaProperties;
	}
	public void setAntennaProperties(List<AntennaProperties> antennaProperties) {
		this.antennaProperties = antennaProperties;
	}
	public DeviceCapabilities getDevice() {
		return device;
	}
	public void setDevice(DeviceCapabilities device) {
		this.device = device;
	}
	public RegulatoryCapabilities getRegulatory() {
		return regulatory;
	}
	public void setRegulatory(RegulatoryCapabilities regulatory) {
		this.regulatory = regulatory;
	}
}
