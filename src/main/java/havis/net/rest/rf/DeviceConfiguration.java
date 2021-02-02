package havis.net.rest.rf;

import java.util.List;

import havis.device.rf.configuration.AntennaConfiguration;
import havis.device.rf.configuration.InventorySettings;
import havis.device.rf.configuration.KeepAliveConfiguration;

public class DeviceConfiguration {
	private String region;
	private KeepAliveConfiguration keepAlive;
	private List<AntennaConfiguration> antennas;
	private InventorySettings inventory;

	public String getRegion() {
		return region;
	}
	public void setRegion(String region) {
		this.region = region;
	}
	public KeepAliveConfiguration getKeepAlive() {
		return keepAlive;
	}
	public void setKeepAlive(KeepAliveConfiguration keepAliveConfiguration) {
		this.keepAlive = keepAliveConfiguration;
	}
	public List<AntennaConfiguration> getAntennas() {
		return antennas;
	}
	public void setAntennas(List<AntennaConfiguration> antennaConfigurations) {
		this.antennas = antennaConfigurations;
	}
	public InventorySettings getInventory() {
		return inventory;
	}
	public void setInventory(InventorySettings inventorySettings) {
		this.inventory = inventorySettings;
	}
}
