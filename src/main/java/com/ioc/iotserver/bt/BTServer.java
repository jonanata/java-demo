package com.ioc.iotserver.bt;

import java.util.ArrayList;

import javax.bluetooth.DataElement;
import javax.bluetooth.DeviceClass;
import javax.bluetooth.DiscoveryAgent;
import javax.bluetooth.DiscoveryListener;
import javax.bluetooth.LocalDevice;
import javax.bluetooth.RemoteDevice;
import javax.bluetooth.ServiceRecord;
import javax.bluetooth.UUID;

public class BTServer implements DiscoveryListener{
	
	private static Object lock=new Object();
    public ArrayList<RemoteDevice> devices;
    
    public BTServer() {
        devices = new ArrayList<RemoteDevice>();
    }

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		BTServer listener =  new BTServer(); 
		
		try{ 
			
			MyRemoteDevice d = new MyRemoteDevice("ce903fe46f1b"); 
			
			String name;
	        try {
	            name = d.getFriendlyName(false);
	        } catch (Exception e) {
	            name = d.getBluetoothAddress();
	        }
			
			System.out.println(name);
			
			LocalDevice localDevice = LocalDevice.getLocalDevice();
            DiscoveryAgent agent = localDevice.getDiscoveryAgent();
            agent.startInquiry(DiscoveryAgent.LIAC, listener);
            
            try {
                synchronized(lock){
                    lock.wait();
                }
            }
            catch (InterruptedException e) {
                e.printStackTrace();
                return;
            }
            
            
            System.out.println("Device Inquiry Completed. "); 
            
            UUID[] uuidSet = new UUID[1];
            uuidSet[0]=new UUID(0x1105); //OBEX Object Push service
            
            int[] attrIDs =  new int[] {
                    0x0100 // Service name
            };
            
            for (RemoteDevice device : listener.devices) {
                agent.searchServices(
                        attrIDs,uuidSet,device,listener);
                
                
                try {
                    synchronized(lock){
                        lock.wait();
                    }
                }
                catch (InterruptedException e) {
                    e.printStackTrace();
                    return;
                }
                
                
                System.out.println("Service search finished.");
            }
			
		} catch (Exception e) {
            e.printStackTrace();
        }

	}

	@Override
	public void deviceDiscovered(RemoteDevice btDevice, DeviceClass arg1) {
		// TODO Auto-generated method stub
		
		String name;
        try {
            name = btDevice.getFriendlyName(false);
        } catch (Exception e) {
            name = btDevice.getBluetoothAddress();
        }
        
        devices.add(btDevice);
        System.out.println("device found: " + name);
		
	}

	@Override
	public void inquiryCompleted(int arg0) {
		// TODO Auto-generated method stub
		synchronized(lock){
            lock.notify();
        }
	}

	@Override
	public void serviceSearchCompleted(int arg0, int arg1) {
		// TODO Auto-generated method stub
		synchronized (lock) {
            lock.notify();
        }
	}

	@Override
	public void servicesDiscovered(int transID, ServiceRecord[] servRecord) {
		// TODO Auto-generated method stub
		for (int i = 0; i < servRecord.length; i++) {
            String url = servRecord[i].getConnectionURL(ServiceRecord.NOAUTHENTICATE_NOENCRYPT, false);
            if (url == null) {
                continue;
            }
            DataElement serviceName = servRecord[i].getAttributeValue(0x0100);
            if (serviceName != null) {
                System.out.println("service " + serviceName.getValue() + " found " + url);

            } else {
                System.out.println("service found " + url);
            }
            
          
        }
	}

}
