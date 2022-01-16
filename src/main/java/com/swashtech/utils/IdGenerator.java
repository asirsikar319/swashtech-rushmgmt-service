package com.swashtech.utils;

import org.json.JSONObject;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

public class IdGenerator {

	private static int v_counter;
	static String v_Return = "";

	public synchronized static String getMaxNo(MongoTemplate mongoTemplate, String a_var) {

		String l_prefix = null;
		String l_pad = "0000000000000000000000000";
		char v_Global;
		int l_Length = 0;
		int cnt = 0;

		try {
			Query query = new Query();
			query.addCriteria(Criteria.where("seqtype").is(a_var));
			String str = mongoTemplate.find(query, String.class, "sequencer").get(0);
			JSONObject jSObject = new JSONObject(str);
			l_pad = "00000000000000000000";
			v_Global = 'G';
			l_Length = jSObject.getInt("length");
			l_prefix = jSObject.getString("prefix");
			if (l_prefix != null) {
				cnt = l_prefix.length();
			}
			if (v_Global == 'G') {
				v_Return = "";
				v_counter = jSObject.getInt("counter");
				v_counter = v_counter + 1;
				cnt = cnt + (Integer.toString(v_counter).length());
				try {
					Update update = new Update().set("counter", v_counter);
					mongoTemplate.upsert(query, update, "sequencer");
				} catch (Exception e) {
					e.printStackTrace();
					return "Error";
				}
				String v_Return = v_counter + "";
				if (l_prefix != null) {
					if (!l_prefix.equals("")) {
						if (cnt < l_Length && l_Length > 0) {
							cnt = l_Length - cnt;
							l_pad = l_pad.substring(0, cnt);
						}
						v_Return = l_prefix;
						v_Return = v_Return + l_pad + v_counter;
					}
				}
				System.out.println(a_var + " getMaxNo :" + v_Return);
				return v_Return;
			}
		} catch (Exception e) {
			e.printStackTrace();
			return "Error";
		}
		return v_Return;
	}

}
