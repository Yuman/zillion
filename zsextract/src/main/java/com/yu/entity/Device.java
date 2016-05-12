package com.yu.entity;

public class Device {
	public String id;
	public int deviceTypeId = 1;
	public String tag;
	public String name;
	public String description;
	public String deviceStatusId;
	public String snPrefix;
	@Override
	public String toString() {
		return "Device[id=" + id + ", deviceTypeId=" + deviceTypeId + ", tag=" + tag +"]";
	}	
	
}
