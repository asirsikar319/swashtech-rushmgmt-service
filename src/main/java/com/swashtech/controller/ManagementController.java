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

import com.swashtech.services.ManagementService;
import com.swashtech.services.RegistrationService;
import com.swashtech.utils.JSchemaUtility;

import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/slots")
public class ManagementController {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	JSchemaUtility jSchemaUtility;

	@Autowired
	ManagementService managementService;

	@ApiOperation(value = "Book Slot Online", response = Iterable.class)
	@RequestMapping(value = "/bookSlotOnline", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> bookSlotOnline(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start bookSlotOnline...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("bookSlotOnline.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = managementService.bookSlotOnline(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.CREATED);
				} else {
					response = new ResponseEntity<String>(json.toString(),
							json.has("httpStatus") ? (HttpStatus) json.get("httpStatus")
									: HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("bookSlotOnline response : " + response);
		logger.info("Time taken for bookSlotOnline() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}

	@ApiOperation(value = "Book Slot Offline", response = Iterable.class)
	@RequestMapping(value = "/bookSlotOffline", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> bookSlotOffline(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start bookSlotOffline...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("bookSlotOffline.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = managementService.bookSlotOffline(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.CREATED);
				} else {
					response = new ResponseEntity<String>(json.toString(),
							json.has("httpStatus") ? (HttpStatus) json.get("httpStatus")
									: HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("bookSlotOffline response : " + response);
		logger.info("Time taken for bookSlotOffline() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}

	@ApiOperation(value = "Update Slot", response = Iterable.class)
	@RequestMapping(value = "/updateSlot", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<String> updateSlot(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start updateSlot...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("updateSlot.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = managementService.updateSlot(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.OK);
				} else {
					response = new ResponseEntity<String>(json.toString(),
							json.has("httpStatus") ? (HttpStatus) json.get("httpStatus")
									: HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("updateSlot response : " + response);
		logger.info("Time taken for updateSlot() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}

	@ApiOperation(value = "Cancel Slot", response = Iterable.class)
	@RequestMapping(value = "/cancelSlot", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<String> cancelSlot(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start cencelSlot...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("cancelSlot.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = managementService.cancelSlot(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.OK);
				} else {
					response = new ResponseEntity<String>(json.toString(),
							json.has("httpStatus") ? (HttpStatus) json.get("httpStatus")
									: HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("cancelSlot response : " + response);
		logger.info("Time taken for cancelSlot() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}

	@ApiOperation(value = "Release Slot", response = Iterable.class)
	@RequestMapping(value = "/releaseSlot", method = RequestMethod.PUT, produces = "application/json")
	public ResponseEntity<String> releaseSlot(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start releaseSlot...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("releaseSlot.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = managementService.releaseSlot(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.OK);
				} else {
					response = new ResponseEntity<String>(json.toString(),
							json.has("httpStatus") ? (HttpStatus) json.get("httpStatus")
									: HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("releaseSlot response : " + response);
		logger.info("Time taken for releaseSlot() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}

	@ApiOperation(value = "Available Slots", response = Iterable.class)
	@RequestMapping(value = "/availableSlots", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> availableSlots(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.debug("start availableSlots...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("availableSlots.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = managementService.availableSlots(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.OK);
				} else {
					response = new ResponseEntity<String>(json.toString(),
							json.has("httpStatus") ? (HttpStatus) json.get("httpStatus")
									: HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("availableSlots response : " + response);
		logger.info("Time taken for availableSlots() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}
	
	@ApiOperation(value = "Available Slots 2", response = Iterable.class)
	@RequestMapping(value = "/availableSlots2", method = RequestMethod.POST, produces = "application/json")
	public ResponseEntity<String> availableSlots2(@RequestBody String input) {
		long start = System.currentTimeMillis();
		logger.info("start availableSlots2...");
		ResponseEntity<String> response = null;
		JSONObject resp = new JSONObject();
		try {
			JSONObject jInput = new JSONObject(input);
			JSONObject schema = jSchemaUtility.readResourceFile("availableSlots2.json");
			JSONObject schemaOutput = jSchemaUtility.validateSchema(schema, jInput);
			if (schemaOutput != null && "Success".equals(schemaOutput.getString("status"))) {
				JSONObject json = managementService.availableSlots2(jInput);
				if (json != null && "Success".equals(json.getString("status"))) {
					response = new ResponseEntity<String>(json.toString(), HttpStatus.OK);
				} else {
					response = new ResponseEntity<String>(json.toString(),
							json.has("httpStatus") ? (HttpStatus) json.get("httpStatus")
									: HttpStatus.INTERNAL_SERVER_ERROR);
				}
			} else {
				response = new ResponseEntity<String>(schemaOutput.toString(), HttpStatus.BAD_REQUEST);
			}
		} catch (Exception e) {
			resp.put("status", "Error");
			resp.put("message", e.getMessage());
			response = new ResponseEntity<String>(resp.toString(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
		logger.info("availableSlots2 response : " + response);
		logger.info("Time taken for availableSlots2() : " + (System.currentTimeMillis() - start) + " ms");
		return response;
	}

}
