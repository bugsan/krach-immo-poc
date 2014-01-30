package fr.krachimmo.util;

import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;

/**
 *
 * @author Sébastien Chatel
 * @since 30 January 2014
 */
public class SearchUtils {

	public static String toJson(Results<ScoredDocument> results) {
		StringBuilder json = new StringBuilder(256);
		json.append("{\"documents\":[");
		int docCount = 0;
		for (ScoredDocument document : results) {
			if (docCount > 0) {
				json.append(',');
			}
			json.append("{\"id\":\"" + document.getId() + "\"");
			int fieldCount = 0;
			for (Field field : document.getFields()) {
				if (fieldCount > 0) {
					json.append(',');
				}
				json.append("\"" + field.getName() + "\":\"" + field.getText() + "\"");
				fieldCount++;
			}
			json.append('}');
			docCount++;
		}
		json.append("]}");
		return json.toString();
	}
}
