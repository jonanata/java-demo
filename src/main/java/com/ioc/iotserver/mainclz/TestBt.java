package com.ioc.iotserver.mainclz;

import java.util.concurrent.ExecutionException;

import javax.bluetooth.*;

import org.sputnikdev.bluetooth.URL;
import org.sputnikdev.bluetooth.manager.BluetoothManager;
import org.sputnikdev.bluetooth.manager.CharacteristicGovernor;
import org.sputnikdev.bluetooth.manager.impl.BluetoothManagerBuilder;

public class TestBt {

	public static void main(String[] args) {
		// TODO Auto-generated method stub 
		
		String urlWinBB = "/60:A4:23:60:7F:D1/CE:90:3F:E4:6F:1B/00001809-0000-1000-8000-00805f9b34fb/00008801-0000-1000-8000-00805f9b34fb"; 
		String urlMi = "/60:A4:23:60:7F:D1/A4:C1:38:40:F5:BD/EBE0CCB0-7A0A-4B0C-8A1A-6FF2997DA3A6/EBE0CCC1-7A0A-4B0C-8A1A-6FF2997DA3A6"; 
		
		 try {
			
			URL url = new URL(urlWinBB); 
			
			new BluetoothManagerBuilder()
			.withTinyBTransport(false)
			.withBlueGigaTransport("^*.$")
			.withDiscovering(true)
			//.withRediscover(true)
			.build()
			.getCharacteristicGovernor(url)
			//.getCharacteristicGovernor(url.copyWith("00001809-0000-1000-8000-00805f9b34fb", "00008801-0000-1000-8000-00805f9b34fb"))
			.whenReady(CharacteristicGovernor::read)
			.thenAccept(data -> {
			    System.out.println("Temperature: " + data[0]);
			}).get();
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ExecutionException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}  
		
		/* try { 
			// get the Bluetooth Manager
	        BluetoothManager manager = new BluetoothManagerBuilder()
	                .withTinyBTransport(false)
	                .withBlueGigaTransport("^*.$")
	                .withDiscovering(true)
	                .build();
	        
	     // define a URL pointing to the target characteristic
	        URL url = new URL(urlWinBB);
	        // subscribe to the characteristic notification
	        manager.getCharacteristicGovernor(url, true).addValueListener(value -> {
	            System.out.println("Temperature : " + value[0]);
	        });
	        
	        // do your other stuff
	        Thread.sleep(10000); 
		} catch(Throwable e) { 
			
			e.printStackTrace();
		} */ 

		/* 
		 * // define a URL pointing to the target characteristic
	        URL url = new URL(urlStr); 
	        // subscribe to the characteristic notification
	        manager.getCharacteristicGovernor(url.copyWith("00001809-0000-1000-8000-00805f9b34fb", "00008801-0000-1000-8000-00805f9b34fb"), true).addValueListener(value -> {
	            System.out.println("Temperature: " + value[0]);
	        });
		 * 
		 */
		
	}

}
