package com.example.bluetoothtest.Models.Usb;

/**
 * This class represents a usb device.
 * it contains usb devices important informations.
 * All usb devices infomations can be read with android UsbManager class or
 * on usb organisation oline database : @see<a href="https://www.the-sz.com/products/usbid/"> https://www.the-sz.com/products/usbid/</a>
 *
 * @author Gedeon AGOTSI
 * @version
 */
public  class UsbInfo {

    //Attributs
    private int productId;
    private int vendorId;
    private String deviceName;
    private String serialNumlber;
    private String softwareVersion;

    public UsbInfo() {
    }

    /**
     * constructor
     * @param productId a usb device product ID
     * @param vendorId  a usb device vendor ID
     */
    public UsbInfo(int productId, int vendorId) {
        this.productId = productId;
        this.vendorId = vendorId;
    }

    //getters and setters

    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    public int getVendorId() {
        return vendorId;
    }

    public void setVendorId(int vendorId) {
        this.vendorId = vendorId;
    }

    public String getDeviceName() {
        return deviceName;
    }

    public void setDeviceName(String deviceName) {
        this.deviceName = deviceName;
    }

    public String getSerialNumlber() {
        return serialNumlber;
    }

    public void setSerialNumlber(String serialNumlber) {
        this.serialNumlber = serialNumlber;
    }

    public String getSoftwareVersion() {
        return softwareVersion;
    }

    public void setSoftwareVersion(String softwareVersion) {
        this.softwareVersion = softwareVersion;
    }



    @Override
    public String toString() {
        return "UsbSensor{" +
                "productId=" + productId +
                ", vendorId=" + vendorId +
                ", deviceName='" + deviceName + '\'' +
                ", serialNumlber='" + serialNumlber + '\'' +
                ", softwareVersion='" + softwareVersion + '\'' +
                '}';
    }
}
