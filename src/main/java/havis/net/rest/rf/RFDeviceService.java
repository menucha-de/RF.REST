package havis.net.rest.rf;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.xml.bind.DatatypeConverter;

import com.fasterxml.jackson.databind.ObjectMapper;

import havis.device.rf.RFDevice;
import havis.device.rf.capabilities.Capabilities;
import havis.device.rf.capabilities.CapabilityType;
import havis.device.rf.capabilities.DeviceCapabilities;
import havis.device.rf.capabilities.RegulatoryCapabilities;
import havis.device.rf.configuration.AntennaConfiguration;
import havis.device.rf.configuration.AntennaProperties;
import havis.device.rf.configuration.Configuration;
import havis.device.rf.configuration.ConfigurationType;
import havis.device.rf.configuration.InventorySettings;
import havis.device.rf.configuration.KeepAliveConfiguration;
import havis.device.rf.configuration.RssiFilter;
import havis.device.rf.configuration.SelectionMask;
import havis.device.rf.configuration.SingulationControl;
import havis.device.rf.configuration.TagSmoothingSettings;
import havis.device.rf.exception.CommunicationException;
import havis.device.rf.exception.ConnectionException;
import havis.device.rf.exception.ImplementationException;
import havis.device.rf.exception.ParameterException;
import havis.device.rf.tag.Filter;
import havis.device.rf.tag.TagData;
import havis.device.rf.tag.operation.TagOperation;
import havis.net.rest.shared.Resource;
import havis.util.core.common.license.Util;
import havis.util.core.license.License;
import havis.util.core.license.LicenseException;

@Path("rf")
public class RFDeviceService extends Resource {

	private final static String API = "openapi.yaml";

	private RFConsumerREST rc;

	public RFDeviceService(RFDevice device) {
		rc = new RFConsumerREST(device);
	}

	@PermitAll
	@GET
	@Path("")
	@Produces({ MediaType.APPLICATION_OCTET_STREAM })
	public Response getAPI() throws IOException {
		InputStream stream = getClass().getClassLoader().getResourceAsStream(API);
		return Response.status(Response.Status.OK).entity(stream).header("Content-Disposition", "attachment; filename=" + API)
				.header("Content-Length", stream.available()).build();
	}

	/**
	 * Capability categories which can be requested
	 * 
	 * @return List of String; Relative paths to request certain capability
	 *         categories
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@PermitAll
	@GET
	@Path("capabilities")
	@Produces({ "application/json", "application/xml" })
	public AllCapabilities getCapabilities() throws ConnectionException, ImplementationException {
		AllCapabilities result = new AllCapabilities();
		result.setDevice(getDeviceCapabilities());
		result.setRegulatory(getRegulatoryCapabilities());
		result.setAntennaProperties(getAntennaProperties());
		return result;
	}

	/**
	 * Gets the capabilities of the device.
	 * 
	 * @return {@link DeviceCapabilities}
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@PermitAll
	@GET
	@Path("capabilities/device")
	@Produces({ "application/json", "application/xml" })
	public DeviceCapabilities getDeviceCapabilities() throws ConnectionException, ImplementationException {
		List<Capabilities> device = rc.getOpenController().getCapabilities(CapabilityType.DEVICE_CAPABILITIES);
		if (device.size() > 0) {
			return (DeviceCapabilities) device.get(0);
		}
		return null;
	}

	/**
	 * Get the regulatory capabilities
	 * 
	 * @return {@link RegulatoryCapabilities}
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@PermitAll
	@GET
	@Path("capabilities/regulatory")
	@Produces({ "application/json", "application/xml" })
	public RegulatoryCapabilities getRegulatoryCapabilities() throws ConnectionException, ImplementationException {
		List<Capabilities> regulatory = rc.getOpenController().getCapabilities(CapabilityType.REGULATORY_CAPABILITIES);
		if (regulatory.size() > 0) {
			return (RegulatoryCapabilities) regulatory.get(0);
		}
		return null;
	}

	/**
	 * Get antenna properties. If the id is 0 the properties of all antennas
	 * will be returned. If no id is specified the default value is 0.
	 * 
	 * @return List of AntennaProperties
	 * @throws ConnectionException
	 * @throws Exception
	 */

	@PermitAll
	@GET
	@Path("capabilities/antennaProperties")
	@Produces({ "application/json", "application/xml" })
	public List<AntennaProperties> getAntennaProperties() throws ImplementationException, ConnectionException {
		ArrayList<AntennaProperties> result = new ArrayList<>();
		List<Configuration> antennaProperties = rc.getOpenController().getConfiguration(ConfigurationType.ANTENNA_PROPERTIES, (short) 0, (short) 0, (short) 0);
		for (Configuration configuration : antennaProperties) {
			if (configuration instanceof AntennaProperties) {
				result.add((AntennaProperties) configuration);
			}
		}
		return result;
	}

	/**
	 * Get antenna properties. If the id is 0 the properties of all antennas
	 * will be returned. If no id is specified the default value is 0.
	 * 
	 * @param id
	 *            antenna id to get properties for
	 * @return List of AntennaProperties
	 * @throws ConnectionException
	 * @throws Exception
	 */
	@PermitAll
	@GET
	@Path("capabilities/antennaProperties/{id}")
	@Produces({ "application/json", "application/xml" })
	public AntennaProperties getAntennaProperties(@PathParam("id") short id) throws ImplementationException, ConnectionException {
		List<Configuration> properties = rc.getOpenController().getConfiguration(ConfigurationType.ANTENNA_PROPERTIES, id, (short) 0, (short) 0);
		if (!properties.isEmpty()) {
			return (AntennaProperties) properties.get(0);
		}
		throw new ImplementationException("Empty result!");
	}

	/**
	 * Get the whole configuration at once
	 * 
	 * @return List of String; Relative paths to certain configuration
	 *         categories.
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@RolesAllowed("admin")
	@GET
	@Path("configuration")
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	public Response getConfiguration() throws ConnectionException, ImplementationException {
		DeviceConfiguration dc = new DeviceConfiguration();
		dc.setRegion(rc.getOpenController().getRegion());
		dc.setKeepAlive(getKeepAliveConfiguration());
		dc.setAntennas(getAntennaConfiguration());
		dc.setInventory(getInventorySettings());
		try {
			ByteArrayOutputStream stream = new ByteArrayOutputStream();
			new ObjectMapper().writeValue(stream, dc);
			byte[] data = stream.toByteArray();
			return Response.ok(data).header("Content-Disposition", "attachment; filename=\"rfid_hardware.config\"")
					.header("Content-Type", "text/plain; charset=utf-8").header("Content-Length", data.length).build();
		} catch (IOException e) {
			throw new ImplementationException(e);
		}
	}

	/**
	 * Set the whole configuration at once
	 * 
	 * @param value
	 *            the Configuration
	 * @throws ParameterException
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@RolesAllowed("admin")
	@POST
	@Path("configuration")
	@Consumes(MediaType.APPLICATION_OCTET_STREAM)
	public void setConfiguration(InputStream stream) throws ParameterException, ImplementationException, ConnectionException {
		try {
			DeviceConfiguration value = new ObjectMapper().readValue(stream, DeviceConfiguration.class);
			RFDevice d = rc.getOpenController();
			String region = value.getRegion();
			if (d.getRegion().equals("Unspecified") && region != null && d.getSupportedRegions().contains(region)) {
				d.setRegion(region);
			}

			KeepAliveConfiguration keepAlive = value.getKeepAlive();
			if (keepAlive != null) {
				setKeepAliveConfiguration(keepAlive);
			}

			List<AntennaConfiguration> antennas = value.getAntennas();
			if (antennas != null) {
				setAntennaConfigurations(antennas);
			}

			InventorySettings inventory = value.getInventory();
			if (inventory != null) {
				setInventorySettings(inventory);
			}
		} catch (IOException e) {
			throw new ImplementationException(e);
		}
	}

	private void setAntennaConfigurations(List<AntennaConfiguration> configurations) throws ImplementationException, ConnectionException, ParameterException {
		rc.getOpenController().setConfiguration(new ArrayList<Configuration>(configurations));
	}

	/**
	 * Reset the reader configuration.
	 * 
	 * @throws ImplementationException
	 * @throws ConnectionException
	 * @throws ParameterException
	 */
	@RolesAllowed("admin")
	@PUT
	@Path("reset")
	@Consumes({ MediaType.TEXT_PLAIN })
	public void resetConfiguration(@QueryParam("product") String product, String data) throws ImplementationException, ConnectionException, ParameterException {
		try {
			License lic = Util.get(product, data);
			if (lic != null) {
				rc.getOpenController().resetConfiguration();
			}
		} catch (LicenseException e) {
			throw new ParameterException(e);
		}
	}

	/**
	 * Gets the keep alive settings of the reader
	 * 
	 * @return {@link KeepAliveConfiguration}
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@PermitAll
	@GET
	@Path("configuration/keepalive")
	@Produces({ "application/json", "application/xml" })
	public KeepAliveConfiguration getKeepAliveConfiguration() throws ConnectionException, ImplementationException {
		List<Configuration> configuration = rc.getOpenController().getConfiguration(ConfigurationType.KEEP_ALIVE_CONFIGURATION, (short) 0, (short) 0,
				(short) 0);
		if (configuration.size() > 0) {
			return (KeepAliveConfiguration) configuration.get(0);
		}
		return null;
	}

	/**
	 * Set the keep alive settings of the reader to the values in the parameter
	 * 
	 * @param keepAlive
	 *            Keep Alive settings to configure on the reader
	 * @throws ImplementationException
	 * @throws ConnectionException
	 * @throws ParameterException
	 */
	@RolesAllowed("admin")
	@PUT
	@Path("configuration/keepalive")
	@Consumes({ "application/json", "application/xml" })
	public void setKeepAliveConfiguration(KeepAliveConfiguration keepAlive) throws ImplementationException, ConnectionException, ParameterException {
		List<Configuration> configuration = new ArrayList<Configuration>();
		configuration.add(keepAlive);
		rc.getOpenController().setConfiguration(configuration);
	}

	/**
	 * Get antenna configuration. If the id is 0 the configurations of all
	 * antennas will be returned. If no id is specified the default value is 0.
	 * 
	 * @return List of AntennaConfiguration
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@PermitAll
	@GET
	@Path("configuration/antennas")
	@Produces({ "application/json", "application/xml" })
	public List<AntennaConfiguration> getAntennaConfiguration() throws ConnectionException, ImplementationException {
		ArrayList<AntennaConfiguration> result = new ArrayList<>();
		List<Configuration> antennaConfigurations = rc.getOpenController().getConfiguration(ConfigurationType.ANTENNA_CONFIGURATION, (short) 0, (short) 0,
				(short) 0);
		for (Configuration configuration : antennaConfigurations) {
			if (configuration instanceof AntennaConfiguration) {
				result.add((AntennaConfiguration) configuration);
			}
		}
		return result;
	}

	/**
	 * Get antenna configuration. If the id is 0 the configurations of all
	 * antennas will be returned. If no id is specified the default value is 0.
	 * 
	 * @param id
	 *            antenna id to get properties for
	 * @return List of AntennaConfiguration
	 * @throws ImplementationException
	 * @throws ConnectionException
	 * @throws ParameterException 
	 */
	@PermitAll
	@GET
	@Path("configuration/antennas/{id}")
	@Produces({ "application/json", "application/xml" })
	public AntennaConfiguration getAntennaConfiguration(@PathParam("id") short id) throws ConnectionException, ImplementationException, ParameterException {
		if (id != 0) {
			List<Configuration> configurations = rc.getOpenController().getConfiguration(ConfigurationType.ANTENNA_CONFIGURATION, id, (short) 0, (short) 0);
			if (!configurations.isEmpty()) {
				return (AntennaConfiguration) configurations.get(0);
			}
		}
		throw new ParameterException("No antenna configuration with given id found!");
	}

	/**
	 * Set the antenna configurations to the values from the configuration
	 * objects in the list. The configuration objects itself contain an antenna
	 * id.
	 * 
	 * @param configuration
	 *            configuration
	 * @throws ImplementationException
	 * @throws ConnectionException
	 * @throws ParameterException
	 */
	@RolesAllowed("admin")
	@PUT
	@Path("configuration/antennas/{id}")
	@Consumes({ "application/json", "application/xml" })
	public void setAntennaConfiguration(@PathParam("id") short id, AntennaConfiguration configuration)
			throws ImplementationException, ConnectionException, ParameterException {
		if (id == configuration.getId()) {
			rc.getOpenController().setConfiguration(Arrays.<Configuration> asList(configuration));
		} else {
			throw new ParameterException("Id from path does not match id in given configuration");
		}
	}

	@PermitAll
	@GET
	@Path("configuration/inventory")
	@Produces(MediaType.APPLICATION_JSON)
	public InventorySettings getInventorySettings() throws ConnectionException, ImplementationException {
		List<Configuration> configuration = rc.getOpenController().getConfiguration(ConfigurationType.INVENTORY_SETTINGS, (short) 0, (short) 0, (short) 0);
		if (configuration.size() > 0) {
			return (InventorySettings) configuration.get(0);
		}
		return null;
	}

	private void setInventorySettings(InventorySettings inventorySettings) throws ImplementationException, ConnectionException, ParameterException {
		List<Configuration> configuration = new ArrayList<Configuration>();
		configuration.add(inventorySettings);
		rc.getOpenController().setConfiguration(configuration);
	}

	@PermitAll
	@GET
	@Path("configuration/inventory/selectionMasks")
	@Produces(MediaType.APPLICATION_JSON)
	public List<SelectionMask> getSelectionMasks() throws ConnectionException, ImplementationException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			return is.getSelectionMasks();
		}
		return null;
	}

	@PermitAll
	@PUT
	@Path("configuration/inventory/selectionMasks")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setSelectionMasks(List<SelectionMask> value) throws ConnectionException, ImplementationException, ParameterException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			is.getSelectionMasks().clear();
			is.getSelectionMasks().addAll(value);
		}
		setInventorySettings(is);
	}

	@PermitAll
	@GET
	@Path("configuration/inventory/rssiFilter")
	@Produces(MediaType.APPLICATION_JSON)
	public RssiFilter getRssiFilter() throws ConnectionException, ImplementationException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			return is.getRssiFilter();
		}
		return null;
	}

	@PermitAll
	@PUT
	@Path("configuration/inventory/rssiFilter")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setRssiFilter(RssiFilter value) throws ConnectionException, ImplementationException, ParameterException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			is.setRssiFilter(value);
		}
		setInventorySettings(is);
	}

	@PermitAll
	@GET
	@Path("configuration/inventory/singulationControl")
	@Produces(MediaType.APPLICATION_JSON)
	public SingulationControl getSingulationControl() throws ConnectionException, ImplementationException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			return is.getSingulationControl();
		}
		return null;
	}

	@PermitAll
	@PUT
	@Path("configuration/inventory/singulationControl")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setSingulationControl(SingulationControl value) throws ConnectionException, ImplementationException, ParameterException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			is.setSingulationControl(value);
		}
		setInventorySettings(is);
	}

	@PermitAll
	@GET
	@Path("configuration/inventory/tagSmoothing")
	@Produces(MediaType.APPLICATION_JSON)
	public TagSmoothingSettings getTagSmoothingSettings() throws ConnectionException, ImplementationException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			return is.getTagSmoothing();
		}
		return null;
	}

	@PermitAll
	@PUT
	@Path("configuration/inventory/tagSmoothing")
	@Consumes(MediaType.APPLICATION_JSON)
	public void setTagSmoothingSettings(TagSmoothingSettings value) throws ConnectionException, ImplementationException, ParameterException {
		InventorySettings is = getInventorySettings();
		if (is != null) {
			is.setTagSmoothing(value);
		}
		setInventorySettings(is);
	}

	/**
	 * Get the current region setting of the reader
	 * 
	 * @return String current region
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@PermitAll
	@GET
	@Path("region")
	@Produces({ "text/plain" })
	public String getRegion() throws ConnectionException, ImplementationException {
		return rc.getOpenController().getRegion();
	}

	/**
	 * Set the region of the reader to the given id
	 * 
	 * @param id
	 *            region id
	 * @throws ImplementationException
	 * @throws ParameterException
	 * @throws ConnectionException
	 */
	@RolesAllowed("admin")
	@PUT
	@Path("region")
	@Consumes({ "text/plain" })
	public void setRegion(String id) throws ParameterException, ImplementationException, ConnectionException {
		rc.getOpenController().setRegion(id);
	}

	/**
	 * List all supported regions of the reader
	 * 
	 * @return List of String regions
	 * @throws ImplementationException
	 * @throws ConnectionException
	 */
	@PermitAll
	@GET
	@Path("region/supported")
	@Produces({ "application/json", "application/xml" })
	public List<String> getSupportedRegions() throws ConnectionException, ImplementationException {
		return rc.getOpenController().getSupportedRegions();
	}

	/**
	 * Get all tags
	 * 
	 * @return List of TagData
	 * @throws ImplementationException
	 * @throws ParameterException
	 * @throws ConnectionException
	 * @throws CommunicationException
	 */
	@PermitAll
	@GET
	@Path("tags")
	@Produces({ "application/json", "application/xml" })
	public List<TagData> getTags(@QueryParam("antenna") short antenna) throws ParameterException, ImplementationException, ConnectionException, CommunicationException {
		return rc.getOpenController().execute(Arrays.asList(antenna), new ArrayList<Filter>(), new ArrayList<TagOperation>());
	}

	/**
	 * Get all tags
	 * 
	 * @return List of TagData
	 * @throws ImplementationException
	 * @throws ParameterException
	 * @throws ConnectionException
	 * @throws CommunicationException
	 */
	@PermitAll
	@PUT
	@Path("tags/{id}")
	@Produces({ "application/json", "application/xml" })
	@Consumes({ "application/json", "application/xml" })
	public List<TagData> getTags(@PathParam("id") String id, @QueryParam("antenna") short antenna, List<TagOperation> tagOperations)
			throws ParameterException, ImplementationException, ConnectionException, CommunicationException {
		List<Filter> filterList = createFilter(id);
		return rc.getOpenController().execute(Arrays.asList(antenna), filterList, tagOperations);
	}

	List<Filter> createFilter(String id) throws ParameterException {
		List<Filter> filterList = new ArrayList<Filter>();

		if ((id != null) && ((id.trim().length() % 4)) == 0) {
			byte[] data = DatatypeConverter.parseHexBinary(id.trim());

			Filter filter = new Filter();
			filter.setData(data);
			filter.setBank((short) 1); // EPC Bank
			filter.setBitOffset((short) 32); // offset = 2 words (CRC+PC)
			filter.setBitLength((short) (data.length * 8)); // convert to bit
			filter.setMask(createFilterMask(data.length));
			filter.setMatch(true);

			filterList.add(filter);
		} else {
			throw new ParameterException("Invalid id length. Multiple of 16 (in bits) expected.");
		}

		return filterList;
	}

	byte[] createFilterMask(int len) {
		byte[] mask = new byte[len];
		for (int i = 0; i < mask.length; i++)
			mask[i] = (byte) 0xff;
		return mask;
	}

	@RolesAllowed("admin")
	@GET
	@Path("installFirmware")
	@Produces({ "application/json", "application/xml" })
	public void installFirmware() throws ImplementationException, ConnectionException, ParameterException {
		rc.getOpenController().installFirmware();
	}

}