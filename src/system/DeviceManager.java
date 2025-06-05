package system;

import java.util.ArrayList;
import java.util.List;
import model.Device;

public class DeviceManager {

    private List<Device> devices;

    public DeviceManager() {
        this.devices = new ArrayList<>();
    }

    public void addDevice(Device device) {
        devices.add(device);
    }

    public List<Device> getDevices() {
        return devices;
    }

    public Device getAvailableDevice() throws SystemException {
        for (Device d : devices) {
            if (d.isAvailable()) {
                return d;
            }
        }
        throw new SystemException("No hay dispositivos disponibles.");
    }

    public void releaseDevice(Device device) {
        device.releaseClient();
    }
}
