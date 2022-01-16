package com.swashtech.services;

import org.bson.Document;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.stereotype.Service;

@Service
public class RegistrationServiceImpl implements RegistrationService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public JSONObject registerOrganization(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {
			Document document = Document.parse(jInput.toString());
			document = mongoTemplate.save(document, "organizations");
			logger.debug(document.toJson());
			jsonObject.put("status", "Success");
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

	@Override
	public JSONObject registerOperatingUnits(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {
			Document document = Document.parse(jInput.toString());
			document = mongoTemplate.save(document, "organizations");
			logger.debug(document.toJson());
			jsonObject.put("status", "Success");
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

}
