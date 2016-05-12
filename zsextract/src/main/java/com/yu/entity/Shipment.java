package com.yu.entity;

import java.util.ArrayList;

public class Shipment {
	public String id;
	public int shipmentIdType = 1;
	public int clientId;
	public String deviceId;
	public String deviceTag;
	public int shipmentStatusId;
	public String reference;
	public String startDate;
	public String endDate;
	public String createdOn;
	public String shipperName;
	public String subscriber;
	public String notes;
	public int currentStop;
	public int stopsStatus;
	public String deviceName;
	public boolean allowSkipStop;
	public int closeType;
	public ArrayList<Stop> stops;
	
	@Override
	public String toString() {
		return "Shipment [id=" + id + ", clientId=" + clientId + ", deviceId=" + deviceId + ", deviceTag=" + deviceTag
				+ ", shipmentStatusId=" + shipmentStatusId + ", reference=" + reference + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", createdOn=" + createdOn + ", shipperName=" + shipperName + ", subscriber="
				+ subscriber + ", notes=" + notes + ", currentStop=" + currentStop + ", stopsStatus=" + stopsStatus
				+ ", deviceName=" + deviceName + ", allowSkipStop=" + allowSkipStop + ", closeType=" + closeType + "]";
	}


	
}

/*
 "id":"10592","clientId":"3","deviceId":"1462","deviceTag":"010200563","shipmentStatusId":3,
"reference":"daily-report","startDate":"2016-03-31T16:00:00.000Z",
"endDate":"2016-04-30T16:00:00.000Z","createdOn":"2016-04-20T06:24:39.583Z",
"shipperName":"","subscriber":"","notes":"","currentStop":0,"stopsStatus":0,
"deviceName":"ZS100-010200563","allowSkipStop":true,"allowReenterStop":false,"closeType":0,"stops"
*/