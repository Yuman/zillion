package com.yu.entity;

import java.util.ArrayList;
/**
 * 
 {"messageHistoryId":"3Xx1Y0f","clientId":3,"clientName":"**ZillionSource**",
 "deviceId":335,"deviceName":"ZS100-010200036","dateAcquired":"2015-05-26T03:58:56Z",
 "dateReceived":"2015-05-26T03:59:23.887Z","u_deviceType":"1","u_tag":"010200036",
 "u_wakeInterval":"300","u_battery":"56","u_batteryVolts":"3.806","u_temperature":24.96,
 "u_temperatureProbe":"24.6","u_humidity":"37.9","u_light":"195.84","u_pressure":"100820",
 "u_acceleration":"1.280","u_accelerationX":"0.068","u_accelerationY":"-0.168",
 "u_accelerationZ":"1.268","u_signalStrength":"31","u_locationType":5,"u_communicationType":1,
 "u_latitude":"31.195587","u_longitude":"121.427277","u_address":"??????????26?"}
 *
 */
		

public class Report {
	public String messageHistoryId;	
	public int clientId;
	public String clientName;
	public int deviceId;
	public String deviceName;
	public String dateAcquired;
	public String dateReceived;
	public String u_deviceType;
	public String u_tag;
	public int u_wakeInterval;
	public int u_battery;
	public float u_batteryVolts;
	public float u_temperature;
	public float u_temperatureProbe;
	public float u_humidity;
	public float u_light;
	public float u_pressure;
	public float u_acceleration;
	public float u_accelerationX;
	public float u_accelerationY;
	public float u_accelerationZ;
	public int u_signalStrength;
	public int u_locationType;
	public int u_communicationType;
	public float u_latitude;
	public float u_longitude;
	public String u_address;
	public ArrayList<Alarm> alarms;
	@Override
	public String toString() {
		return "Report [clientId=" + clientId + ", clientName=" + clientName + ", deviceId=" + deviceId
				+ ", deviceName=" + deviceName + ", dateAcquired=" + dateAcquired + ", dateReceived=" + dateReceived
				+ ", u_deviceType=" + u_deviceType + ", u_tag=" + u_tag + ", u_wakeInterval=" + u_wakeInterval
				+ ", u_signalStrength=" + u_signalStrength + ", u_locationType=" + u_locationType
				+ ", u_communicationType=" + u_communicationType + ", u_latitude=" + u_latitude + ", u_longitude="
				+ u_longitude + ", u_address=" + u_address + "]";
	}
}
