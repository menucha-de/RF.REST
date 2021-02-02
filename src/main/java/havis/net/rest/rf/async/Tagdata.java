
package havis.net.rest.rf.async;

import java.io.Serializable;

/** 
 * Schema fragment(s) for this class:
 * <pre>
 * &lt;xs:complexType xmlns:ns="urn:havis.device.rf:xsd:1" xmlns:xs="http://www.w3.org/2001/XMLSchema" name="tagData">
 *   &lt;xs:sequence>
 *     &lt;xs:element type="xs:long" name="tagDataId"/>
 *     &lt;xs:element type="xs:hexBinary" name="epc"/>
 *     &lt;xs:element type="xs:short" name="pc"/>
 *     &lt;xs:element type="xs:short" name="crc"/>
 *     &lt;xs:element type="xs:int" name="xpc"/>
 *     &lt;xs:element type="xs:short" name="antennaID"/>
 *     &lt;xs:element type="xs:int" name="rssi"/>
 *     &lt;xs:element type="xs:short" name="channel"/>
 *     &lt;xs:element type="ns:operationResult" name="results" maxOccurs="unbounded"/>
 *   &lt;/xs:sequence>
 * &lt;/xs:complexType>
 * </pre>
 */
@SuppressWarnings("serial")
public class Tagdata implements Serializable
{
    private long tagDataId;
    private String epc;
    private short pc;
    private short crc;
    private int xpc;
    private short antennaID;
    private int rssi;
    private short channel;
    /** 
     * Get the 'tagDataId' element value. 
    					An identifier for the tag data which is unique for one inventory.
    				
     * 
     * @return value
     */
    public long getTagDataId() {
        return tagDataId;
    }

    /** 
     * Set the 'tagDataId' element value. 
    					An identifier for the tag data which is unique for one inventory.
    				
     * 
     * @param tagDataId
     */
    public void setTagDataId(long tagDataId) {
        this.tagDataId = tagDataId;
    }

    /** 
     * Get the 'epc' element value. 
    					The transponder's EPC as array of bytes.
    				
     * 
     * @return value
     */
    public String getEpc() {
        return epc;
    }

    /** 
     * Set the 'epc' element value. 
    					The transponder's EPC as array of bytes.
    				
     * 
     * @param epc
     */
    public void setEpc(String epc) {
        this.epc = epc;
    }


    /** 
     * Get the 'pc' element value. 
    					The transponder's protocol control (PC) as numeric value.
    				
     * 
     * @return value
     */
    public short getPc() {
        return pc;
    }

    /** 
     * Set the 'pc' element value. 
    					The transponder's protocol control (PC) as numeric value.
    				
     * 
     * @param pc
     */
    public void setPc(short pc) {
        this.pc = pc;
    }

    /** 
     * Get the 'crc' element value. 
    					The transponder's CRC as numeric value.
    				
     * 
     * @return value
     */
    public short getCrc() {
        return crc;
    }

    /** 
     * Set the 'crc' element value. 
    					The transponder's CRC as numeric value.
    				
     * 
     * @param crc
     */
    public void setCrc(short crc) {
        this.crc = crc;
    }

    /** 
     * Get the 'xpc' element value. 
    					The transponder's extended protocol control (XPC) as numeric value.
    				
     * 
     * @return value
     */
    public int getXpc() {
        return xpc;
    }

    /** 
     * Set the 'xpc' element value. 
    					The transponder's extended protocol control (XPC) as numeric value.
    				
     * 
     * @param xpc
     */
    public void setXpc(int xpc) {
        this.xpc = xpc;
    }

    /** 
     * Get the 'antennaID' element value. 
    					The ID of the antenna that detected the transponder.
    				
     * 
     * @return value
     */
    public short getAntennaID() {
        return antennaID;
    }

    /** 
     * Set the 'antennaID' element value. 
    					The ID of the antenna that detected the transponder.
    				
     * 
     * @param antennaID
     */
    public void setAntennaID(short antennaID) {
        this.antennaID = antennaID;
    }

    /** 
     * Get the 'rssi' element value. 
    					The transponder's maximum receive signal strength ranging from -128 to 127.
    				
     * 
     * @return value
     */
    public int getRssi() {
        return rssi;
    }

    /** 
     * Set the 'rssi' element value. 
    					The transponder's maximum receive signal strength ranging from -128 to 127.
    				
     * 
     * @param rssi
     */
    public void setRssi(int rssi) {
        this.rssi = rssi;
    }

    /** 
     * Get the 'channel' element value. 
    					The index of the fixed frequency table entry representing frequency in use.  
    				
     * 
     * @return value
     */
    public short getChannel() {
        return channel;
    }

    /** 
     * Set the 'channel' element value. 
    					The index of the fixed frequency table entry representing frequency in use.  
    				
     * 
     * @param channel
     */
    public void setChannel(short channel) {
        this.channel = channel;
    }
}
