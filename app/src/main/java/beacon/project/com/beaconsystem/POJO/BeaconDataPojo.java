package beacon.project.com.beaconsystem.POJO;

/**
 * Created by Dell4050 on 12/23/2017.
 */

public class BeaconDataPojo {

    String macAddress,name;

    public BeaconDataPojo(String macAddress, String name) {
        this.macAddress = macAddress;
        this.name = name;
    }

    public String getMacAddress() {
        return macAddress;
    }

    public void setMacAddress(String macAddress) {
        this.macAddress = macAddress;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
