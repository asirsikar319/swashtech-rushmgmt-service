package com.swashtech.services;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

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
			jInput.put("tokenNo", jInput.getString("oprId") + "-" + IdGenerator.getMaxNo(mongoTemplate, "TKNGEN"));
			jInput.put("isEnter", false);
			jInput.put("isExit", false);
			Document document = Document.parse(jInput.toString());
			document.put("createdOn", new Date());
			document.put("entryDateTime", new Date(jInput.getLong("entryDateTime")));
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
			jInput.put("tokenNo", jInput.getString("oprId") + "-" + IdGenerator.getMaxNo(mongoTemplate, "TKNGEN"));
			jInput.put("isEnter", true);
			jInput.put("isExit", false);
			Document document = Document.parse(jInput.toString());
			document.put("createdOn", new Date());
			document.put("entryDateTime", new Date());
			Date exitTime = document.getDate("entryDateTime");
			Calendar cal = Calendar.getInstance();
			cal.setTime(exitTime);
			cal.add(Calendar.MINUTE, 60);
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
			query.addCriteria(Criteria.where("tokenNo").is(jInput.get("tokenNo"))
					.andOperator(Criteria.where("isEnter").is(false)));
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());
			cal.add(Calendar.MINUTE, 60);
			Update update = new Update().set("entryDateTime", new Date()).set("exitDateTime", cal.getTime())
					.set("isEnter", true);
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
			query.addCriteria(Criteria.where("tokenNo").is(jInput.get("tokenNo"))
					.andOperator(Criteria.where("mode").is("online")));
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
			int totalSlots = 60;
			int availableSlots = 0;

			Query query = new Query();
			List<Criteria> criteriaLst = new ArrayList<Criteria>();
			criteriaLst.add(Criteria.where("orgId").is(jInput.get("orgId")));
			criteriaLst.add(Criteria.where("oprId").is(jInput.get("oprId")));
			criteriaLst.add(Criteria.where("isExit").is(false));
			criteriaLst.add(Criteria.where("isEnter").is(true));
			criteriaLst.add(Criteria.where("exitDateTime").gt(new Date(jInput.getLong("timestamp"))));
			query.addCriteria(new Criteria().andOperator(criteriaLst));

			List<String> slotLst = mongoTemplate.find(query, String.class, "slots");
			JSONArray jsonArray = new JSONArray(slotLst);
			List<Long> availSlotList = new ArrayList<Long>();
			if (jsonArray != null && jsonArray.length() != 0) {
				for (int i = 0; i < jsonArray.length(); i++) {
					JSONObject jsonObject2 = new JSONObject(jsonArray.getString(i));

					SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
					Date date = inputFormat.parse(jsonObject2.getJSONObject("exitDateTime").getString("$date"));
					availSlotList.add(date.getTime());
				}
			}

			if (slotLst != null && slotLst.size() != 0) {
				availableSlots = totalSlots - slotLst.size();
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

}
