package com.swashtech.controller;

import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.swashtech.services.RegistrationService;
import com.swashtech.utils.JSchemaUtility;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/register")
public class RegistrationController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	JSchemaUtility jSchemaUtility;
	
	@Autowired
	RegistrationService registrationService;

	@ApiOperation(value = "Register Organization", response = Iterable.class)
	@RequestMapping(value = "/registerOrganization", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> registerOrganization(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start registerOrganization...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("validateOrganization.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = registrationService.registerOrganization(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.CREATED);
				} else {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.BAD_REQUEST);
		}
		logger.info("registerOrganization response : " + response);
		logger.info("Time taken for registerOrganization() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}
	
	@ApiOperation(value = "Register Organization", response = Iterable.class)
	@RequestMapping(value = "/registerOperatingUnits", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> registerOperatingUnits(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start registerOperatingUnits...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("validateOperatingUnits.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = registrationService.registerOperatingUnits(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.CREATED);
				} else {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.BAD_REQUEST);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.BAD_REQUEST);
		}
		logger.info("registerOperatingUnits response : " + response);
		logger.info("Time taken for registerOperatingUnits() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}

}
