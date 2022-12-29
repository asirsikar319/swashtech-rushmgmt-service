package com.swashtech.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import org.bson.Document;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import com.mongodb.client.result.UpdateResult;
import com.swashtech.utils.IdGenerator;

@Service
public class ManagementServiceImpl implements ManagementService {

	private final Logger logger = LoggerFactory.getLogger(this.getClass());

	@Autowired
	MongoTemplate mongoTemplate;

	@Override
	public JSONObject bookSlotOnline(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {

			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("orgId").is(jInput.get("orgId")));
			criteriaLst.add(Criteria.where("oprId").is(jInput.get("oprId")));
			criteriaLst.add(Criteria.where("exitDateTime").gt(new Date(jInput.getLong("entryDateTime"))));

			query.addCriteria(new Criteria().andOperator(criteriaLst));
			System.err.println("query : " + query);
			List<String> slotLst = mongoTemplate.find(query, String.class, "slots");

			System.err.println(slotLst.size());

			if (slotLst != null && slotLst.size() == 5) {
				jsonObject.put("status", "Error");
				jsonObject.put("message",
						"Slots not available at this time!. Please try after some time or check available slots");
				return jsonObject;
			}

			jInput.put("tokenNo", jInput.getString("oprId") + "-" + IdGenerator.getMaxNo(mongoTemplate, "TKNGEN"));
			jInput.put("isEnter", false);
			jInput.put("isExit", false);
			Document document = Document.parse(jInput.toString());
			document.put("createdOn", new Date());
			document.put("entryDateTime", new Date(jInput.getLong("entryDateTime")));

			Date exitTime = new Date(jInput.getLong("entryDateTime"));
			Calendar cal = Calendar.getInstance();
			cal.setTime(exitTime);
			cal.add(Calendar.MINUTE, 5);
			document.put("exitDateTime", cal.getTime());
			document = mongoTemplate.save(document, "slots");
			logger.debug(document.toJson());
			jsonObject.put("status", "Success");
			jsonObject.put("tokenNo", jInput.getString("tokenNo"));
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

	@Override
	public JSONObject bookSlotOffline(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {

			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("orgId").is(jInput.get("orgId")));
			criteriaLst.add(Criteria.where("oprId").is(jInput.get("oprId")));
			criteriaLst.add(Criteria.where("exitDateTime").gt(new Date()));
			query.addCriteria(new Criteria().andOperator(criteriaLst));
			List<String> slotLst = mongoTemplate.find(query, String.class, "slots");

			if (slotLst != null && slotLst.size() == 5) {
				jsonObject.put("status", "Error");
				jsonObject.put("message",
						"Slots not available at this time!. Please try after some time or check available slots");
				return jsonObject;
			}

			jInput.put("tokenNo", jInput.getString("oprId") + "-" + IdGenerator.getMaxNo(mongoTemplate, "TKNGEN"));
			jInput.put("isEnter", true);
			jInput.put("isExit", false);
			Document document = Document.parse(jInput.toString());
			document.put("createdOn", new Date());
			document.put("entryDateTime", new Date());
			Date exitTime = document.getDate("entryDateTime");
			Calendar cal = Calendar.getInstance();
			cal.setTime(exitTime);
			cal.add(Calendar.MINUTE, 5);
			document.put("exitDateTime", cal.getTime());
			document = mongoTemplate.save(document, "slots");
			logger.debug(document.toJson());
			jsonObject.put("status", "Success");
			jsonObject.put("tokenNo", jInput.getString("tokenNo"));
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

	@Override
	public JSONObject updateSlot(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {
			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("tokenNo").is(jInput.get("tokenNo")));
			criteriaLst.add(Criteria.where("isEnter").is(false));
			criteriaLst.add(Criteria.where("mode").is("online"));
//			criteriaLst.add(Criteria.where("exitDateTime").exists(false));
			query.addCriteria(new Criteria().andOperator(criteriaLst));
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, 5);
			Update update = new Update().set("isEnter", true);
			UpdateResult updateResult = mongoTemplate.updateFirst(query, update, "slots");
			jsonObject.put("status", "Success");
			jsonObject.put("tokenNo", jInput.getString("tokenNo"));
			if (updateResult.getModifiedCount() == 0) {
				jsonObject.put("httpStatus", HttpStatus.NOT_FOUND);
				throw new Exception("Not Found");
			}
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

	@Override
	public JSONObject cancelSlot(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {
			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("tokenNo").is(jInput.get("tokenNo")));
			criteriaLst.add(Criteria.where("isEnter").is(false));
			criteriaLst.add(Criteria.where("mode").is("online"));
//			criteriaLst.add(Criteria.where("exitDateTime").exists(false));
			query.addCriteria(new Criteria().andOperator(criteriaLst));
			Update update = new Update().set("exitDateTime", new Date());
			UpdateResult updateResult = mongoTemplate.updateFirst(query, update, "slots");
			jsonObject.put("status", "Success");
			jsonObject.put("tokenNo", jInput.getString("tokenNo"));
			if (updateResult.getModifiedCount() == 0) {
				jsonObject.put("httpStatus", HttpStatus.NOT_FOUND);
				throw new Exception("Not Found");
			}
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

	@Override
	public JSONObject releaseSlot(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {
			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("tokenNo").is(jInput.get("tokenNo")));
			criteriaLst.add(Criteria.where("isEnter").is(true));
			criteriaLst.add(Criteria.where("isExit").is(false));
			query.addCriteria(new Criteria().andOperator(criteriaLst));
			Update update = new Update().set("exitDateTime", new Date()).set("isExit", true);
			UpdateResult updateResult = mongoTemplate.updateFirst(query, update, "slots");
			jsonObject.put("status", "Success");
			jsonObject.put("tokenNo", jInput.getString("tokenNo"));
			if (updateResult.getModifiedCount() == 0) {
				jsonObject.put("httpStatus", HttpStatus.NOT_FOUND);
				throw new Exception("Not Found");
			}
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

	@Override
	public JSONObject availableSlots(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {
			// Hardcode Data
			int totalSlots = 5;
			int availableSlots = 0;

			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("orgId").is(jInput.get("orgId")));
			criteriaLst.add(Criteria.where("oprId").is(jInput.get("oprId")));

//			criteriaLst.add(Criteria.where("isExit").is(false));
//			criteriaLst.add(Criteria.where("isEnter").is(true));
			Date date2 = new Date(jInput.getLong("timestamp"));
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(date2);
			cal2.add(Calendar.MINUTE, -10);
			criteriaLst.add(Criteria.where("exitDateTime").gt(cal2.getTime()));
			query.addCriteria(new Criteria().andOperator(criteriaLst));

			List<String> slotLst = mongoTemplate.find(query, String.class, "slots");
			JSONArray jsonArray = new JSONArray(slotLst);
			List<Long> availSlotList = new ArrayList<Long>();
			if (jsonArray != null && jsonArray.length() != 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = new JSONObject(jsonArray.getString(i));
					try {
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						Date date = inputFormat.parse(jsonObject2.getJSONObject("exitDateTime").getString("$date"));
						Calendar cal = Calendar.getInstance();
						cal.setTime(date);
						cal.add(Calendar.MINUTE, 10);
						availSlotList.add(cal.getTimeInMillis());

					} catch (Exception e) {
						logger.error("Exception Occured for slot {} in Parsing Date : {}", jsonObject2.opt("tokenNo"),
								e.getMessage());
					}
				}
			}

			if (slotLst != null && slotLst.size() != 0) {
				availableSlots = totalSlots - slotLst.size();
			} else {
				availableSlots = totalSlots;
			}
			jsonObject.put("status", "Success");
			jsonObject.put("totalSlots", totalSlots);
			jsonObject.put("availableSlots", availableSlots);
			jsonObject.put("availableInDuration", availSlotList);
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

	@Override
	public JSONObject availableSlots2(JSONObject jInput) {
		JSONObject jsonObject = new JSONObject();
		try {
			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("orgId").is(jInput.get("orgId")));
			criteriaLst.add(Criteria.where("oprId").is(jInput.get("oprId")));
			if (jInput.has("mode")) {
				criteriaLst.add(Criteria.where("mode").is(jInput.get("mode")));
			}
			criteriaLst.add(Criteria.where("isExit").is(jInput.has("isExit") ? jInput.get("isExit") : false));
			criteriaLst.add(Criteria.where("isEnter").is(jInput.has("isEnter") ? jInput.get("isEnter") : false));
			criteriaLst.add(Criteria.where("exitDateTime").gte(new Date(jInput.getLong("frDate"))));
			criteriaLst.add(Criteria.where("exitDateTime").lte(new Date(jInput.getLong("toDate"))));
			query.addCriteria(new Criteria().andOperator(criteriaLst));

			logger.debug("query : " + query.toString());
			List<String> slotLst = mongoTemplate.find(query, String.class, "slots");
			JSONArray jsonArray = new JSONArray();
			if (jsonArray != null && slotLst.size() != 0) {
				for (int i = 0; i < slotLst.size(); i++) {
					JSONObject jsonObject2 = new JSONObject(slotLst.get(i));
					try {
						SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
						inputFormat.setTimeZone(TimeZone.getTimeZone("GMT"));
						jsonObject2.remove("_id");
						jsonObject2.put("exitDateTime", inputFormat
								.parse(jsonObject2.getJSONObject("exitDateTime").getString("$date")).getTime());
						jsonObject2.put("entryDateTime", inputFormat
								.parse(jsonObject2.getJSONObject("entryDateTime").getString("$date")).getTime());
						jsonObject2.put("createdOn",
								inputFormat.parse(jsonObject2.getJSONObject("createdOn").getString("$date")).getTime());
						jsonArray.put(jsonObject2);
					} catch (Exception e) {
						logger.error("Exception Occured for slot {} in Parsing Date : {}", jsonObject2.opt("tokenNo"),
								e.getMessage());
					}
				}
			}

			jsonObject.put("status", "Success");
			jsonObject.put("availableSlots", jsonArray);
		} catch (Exception e) {
			jsonObject.put("status", "Error");
			jsonObject.put("message", e.getMessage());
		}
		return jsonObject;
	}

}
