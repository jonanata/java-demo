package com.ioc.iotserver.mainclz;

import org.sputnikdev.bluetooth.URL;
import org.sputnikdev.bluetooth.gattparser.BluetoothGattParser;
import org.sputnikdev.bluetooth.gattparser.BluetoothGattParserFactory;
import org.sputnikdev.bluetooth.gattparser.GattResponse;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.BluetoothSmartDeviceListener;
import org.sputnikdev.bluetooth.manager.CharacteristicGovernor;
import org.sputnikdev.bluetooth.manager.GattService;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MiTemperatureAndHumiditySensor {

	private static final Pattern TEMP_ND_HUMIDITY_PATTERN =
            Pattern.compile("^T=(?<temperature>([0-9]*[.])?[0-9]+) H=(?<humidity>([0-9]*[.])?[0-9]+)$");

    private static final String DATA_SERVICE = "EBE0CCB0-7A0A-4B0C-8A1A-6FF2997DA3A6";
    private static final String DATA_CHAR = "EBE0CCC1-7A0A-4B0C-8A1A-6FF2997DA3A6";
    private static final String BATTERY_SERVICE = "0000180f-0000-1000-8000-00805f9b34fb";
    private static final String BATTERY_LEVEL_CHAR = "00002a19-0000-1000-8000-00805f9b34fb";

    private final BluetoothManager bluetoothManager = new BluetoothManagerBuilder()
            .withTinyBTransport(true)
            .withBlueGigaTransport("^*.$")
            .withIgnoreTransportInitErrors(true)
            //.withDiscovering(true)
            .withRediscover(true)
            .build();
    private final BluetoothGattParser gattParser = BluetoothGattParserFactory.getDefault();
    private CharacteristicGovernor data;
    private CharacteristicGovernor battery;

    /**
     * An entry point for the application.
     * A single argument (device address) is required in the following format:
     * <br>/adapter address/device address
     * <br>E.g.: /88:6B:0F:01:90:CA/4C:65:A8:D0:7A:EE
     * @param args a single argument in the following format: /XX:XX:XX:XX:XX:XX/YY:YY:YY:YY:YY:YY
     */
    public static void main(String[] args) throws InterruptedException {
        new MiTemperatureAndHumiditySensor(new URL("/60:A4:23:60:7F:D1/A4:C1:38:40:F5:BD")).run();
    }

    private MiTemperatureAndHumiditySensor(URL url) {
        bluetoothManager.addDeviceDiscoveryListener(discoveredDevice -> {
            System.out.println(String.format("Discovered: %s [%s]", discoveredDevice.getName(),
                    discoveredDevice.getURL().getDeviceAddress()));
        });
        data = bluetoothManager.getCharacteristicGovernor(url.copyWith(DATA_SERVICE, DATA_CHAR));
        battery = bluetoothManager.getCharacteristicGovernor(url.copyWith(BATTERY_SERVICE, BATTERY_LEVEL_CHAR));
        data.addValueListener(value -> {
        	
        	System.out.println("Data : " + value);
        	
            GattResponse response = gattParser.parse(DATA_CHAR, value);
            Matcher matcher = TEMP_ND_HUMIDITY_PATTERN.matcher(response.get("Temperature and humidity").getString());
            if (matcher.matches()) {
                System.out.println(String.format("Temperature: %s °C", matcher.group("temperature")));
                System.out.println(String.format("Humidity: %s %%", matcher.group("humidity")));
            }
        });
        bluetoothManager.getDeviceGovernor(url).addBluetoothSmartDeviceListener(new BluetoothSmartDeviceListener() {
            @Override
            public void servicesResolved(List<GattService> gattServices) {
                System.out.println(String.format("Battery level: %d %%",
                        gattParser.parse(BATTERY_LEVEL_CHAR, battery.read()).get("Level").getInteger()));
            }

            @Override
            public void serviceDataChanged(Map<URL, byte[]> serviceData) {
                serviceData.forEach((url, data) -> {
                    if (gattParser.isValidForRead(url.getServiceUUID())) {
                        GattResponse response = gattParser.parse(url.getServiceUUID(), data);
                        response.getFieldHolders().stream()
                                .filter(holder -> !holder.getField().isUnknown())
                                .forEach(holder -> {
                                    System.out.println(holder.getField().getName() + ": " + holder);
                                });
                    }
                });
            }

            @Override
            public void manufacturerDataChanged(Map<Short, byte[]> manufacturerData) {
                manufacturerData.forEach((key, value) -> System.out.println(String.format("Manufacturer data (%s): %s",
                        key, Utils.formatHex(value))));
            }
        });
        //bluetoothManager.getDeviceGovernor(url).setConnectionControl(false);
    }

    private void run() throws InterruptedException {
        Thread.currentThread().join();
    }
	
}
