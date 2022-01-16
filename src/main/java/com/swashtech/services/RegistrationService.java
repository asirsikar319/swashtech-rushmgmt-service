package com.swashtech.services;

import org.json.JSONObject;

public interface RegistrationService {

	public JSONObject registerOrganization(JSONObject jInput);
	
	public JSONObject registerOperatingUnits(JSONObject jInput);

}
