package havis.net.rest.rf.async;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.OPTIONS;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;

import org.fusesource.restygwt.client.MethodCallback;
import org.fusesource.restygwt.client.TextCallback;

import havis.device.rf.capabilities.DeviceCapabilities;
import havis.device.rf.capabilities.RegulatoryCapabilities;
import havis.device.rf.configuration.AntennaConfiguration;
import havis.device.rf.configuration.AntennaProperties;
import havis.device.rf.configuration.InventorySettings;
import havis.device.rf.configuration.KeepAliveConfiguration;
import havis.device.rf.configuration.RssiFilter;
import havis.device.rf.configuration.SelectionMask;
import havis.device.rf.configuration.SingulationControl;
import havis.device.rf.configuration.TagSmoothingSettings;
import havis.device.rf.tag.TagData;
import havis.device.rf.tag.operation.TagOperation;
import havis.net.rest.rf.DeviceConfiguration;
import havis.net.rest.shared.data.ServiceAsync;

@Path("../rest/rf")
public interface RFDeviceServiceAsync extends ServiceAsync {

	@OPTIONS
	@Path("installFirmware")
	void optionsInstallFirmware(MethodCallback<Void> callback);

	@GET
	@Path("installFirmware")
	void installFirmware(MethodCallback<Void> callback);

	// Capabilities
	@GET
	@Path("capabilities")
	void getCapabilities(MethodCallback<List<String>> callback);

	@GET
	@Path("capabilities/device")
	void getDeviceCapabilities(MethodCallback<DeviceCapabilities> callback);

	@GET
	@Path("capabilities/regulatory")
	void getRegulatoryCapabilities(MethodCallback<RegulatoryCapabilities> callback);

	@GET
	@Path("capabilities/antennaProperties")
	void getAntennaProperties(MethodCallback<List<AntennaProperties>> callback);

	@GET
	@Path("capabilities/antennaProperties/{id}")
	void getAntennaProperties(@PathParam("id") short id, MethodCallback<AntennaProperties> callback);

	// Configuration
	@OPTIONS
	@Path("configuration")
	void optionsConfiguration(MethodCallback<Void> callback);

	@GET
	@Path("configuration")
	void getConfiguration(MethodCallback<DeviceConfiguration> callback);

	@PUT
	@Path("configuration")
	void setConfiguration(DeviceConfiguration deviceConfiguration, MethodCallback<Void> callback);

	@OPTIONS
	@Path("configuration/keepalive")
	void optionsKeepAliveConfiguration(MethodCallback<Void> callback);

	@GET
	@Path("configuration/keepalive")
	void getKeepAliveConfiguration(MethodCallback<KeepAliveConfiguration> callback);

	@PUT
	@Path("configuration/keepalive")
	void setKeepAliveConfiguration(KeepAliveConfiguration keepAlive, MethodCallback<Void> callback);

	@OPTIONS
	@Path("configuration/antennas")
	void optionsAntennaConfigurations(MethodCallback<Void> callback);

	@GET
	@Path("configuration/antennas")
	void getAntennaConfigurations(MethodCallback<List<AntennaConfiguration>> callback);

	@OPTIONS
	@Path("configuration/antennas/{id}")
	void optionsAntennaConfiguration(MethodCallback<Void> callback);

	@GET
	@Path("configuration/antennas/{id}")
	void getAntennaConfiguration(@PathParam("id") short id, MethodCallback<AntennaConfiguration> callback);

	@PUT
	@Path("configuration/antennas/{id}")
	void setAntennaConfiguration(@PathParam("id") short id, AntennaConfiguration antennaConfiguration,
			MethodCallback<Void> callback);

	// InventorySettings
	@GET
	@Path("configuration/inventory")
	void getInventorySettings(MethodCallback<InventorySettings> callback);

	@OPTIONS
	@Path("configuration/inventory")
	void optionsInventorySettings(MethodCallback<Void> callback);

	@GET
	@Path("configuration/inventory/selectionMasks")
	void getSelectionMasks(MethodCallback<List<SelectionMask>> callback);

	@PUT
	@Path("configuration/inventory/selectionMasks")
	void setSelectionMasks(List<SelectionMask> value, MethodCallback<Void> callback);

	@OPTIONS
	@Path("configuration/inventory/selectionMasks")
	void optionsSelectionMasks(MethodCallback<Void> callback);

	@GET
	@Path("configuration/inventory/rssiFilter")
	void getRssiFilter(MethodCallback<RssiFilter> callback);

	@PUT
	@Path("configuration/inventory/rssiFilter")
	void setRssiFilter(RssiFilter value, MethodCallback<Void> callback);

	@OPTIONS
	@Path("configuration/inventory/rssiFilter")
	void optionsRssiFilter(MethodCallback<Void> callback);

	@GET
	@Path("configuration/inventory/singulationControl")
	void getSingulationControl(MethodCallback<SingulationControl> callback);

	@PUT
	@Path("configuration/inventory/singulationControl")
	void setSingulationControl(SingulationControl value, MethodCallback<Void> callback);

	@OPTIONS
	@Path("configuration/inventory/singulationControl")
	void optionsSingulationControl(MethodCallback<Void> callback);

	@GET
	@Path("configuration/inventory/tagSmoothing")
	void getTagSmoothingSettings(MethodCallback<TagSmoothingSettings> callback);

	@PUT
	@Path("configuration/inventory/tagSmoothing")
	void setTagSmoothingSettings(TagSmoothingSettings value, MethodCallback<Void> callback);

	@OPTIONS
	@Path("configuration/inventory/tagSmoothing")
	void optionsTagSmoothingSettings(MethodCallback<Void> callback);

	// Region
	@OPTIONS
	@Path("region")
	void optionsRegion(MethodCallback<Void> callback);

	@GET
	@Path("region")
	void getRegion(TextCallback callback);

	@PUT
	@Path("region")
	void setRegion(String id, MethodCallback<Void> callback);

	@GET
	@Path("region/supported")
	void getSupportedRegions(MethodCallback<List<String>> callback);

	// Tags
	@GET
	@Path("tags")
	void getTags(MethodCallback<List<TagData>> callback);

	@PUT
	@Path("tags/{id}")
	void getTags(@PathParam("id") String id, List<TagOperation> tagOperations, MethodCallback<List<TagData>> callback);

}
