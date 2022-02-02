package com.swashtech.services;

import org.json.JSONObject;

public interface ManagementService {

	public JSONObject bookSlotOnline(JSONObject jInput);

	public JSONObject bookSlotOffline(JSONObject jInput);

	public JSONObject updateSlot(JSONObject jInput);

	public JSONObject cancelSlot(JSONObject jInput);

	public JSONObject releaseSlot(JSONObject jInput);
	
	public JSONObject availableSlots(JSONObject jInput);
	
	public JSONObject availableSlots2(JSONObject jInput);
}
