package fr.krachimmo.controller;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.google.appengine.api.search.Document;
import com.google.appengine.api.search.Field;
import com.google.appengine.api.search.Index;
import com.google.appengine.api.search.IndexSpec;
import com.google.appengine.api.search.Query;
import com.google.appengine.api.search.Results;
import com.google.appengine.api.search.ScoredDocument;
import com.google.appengine.api.search.SearchService;

/**
 *
 * @author Sébastien Chatel
 * @since 23 January 2014
 */
@Controller
public class SearchController {

	private static final Log log = LogFactory.getLog(SearchController.class);

	@Autowired
	SearchService searchService;

	@RequestMapping(value = "/api/indexes/foo", method = RequestMethod.POST)
	@ResponseStatus(HttpStatus.OK)
	public void index(@RequestBody String value) {
		log.info("indexing '" + value + "'");
		Index index = this.searchService.getIndex(IndexSpec.newBuilder().setName("Foo").build());
		index.put(Document.newBuilder()
				.setId("documentId")
				.addField(Field.newBuilder().setName("subject").setText("my first email"))
				.addField(Field.newBuilder().setName("body").setText(value))
				.build());
	}

	@RequestMapping(value = "/api/indexes/foo/search", method = RequestMethod.GET)
	@ResponseBody
	public String search(@RequestParam("q") String query) {
		Index index = this.searchService.getIndex(IndexSpec.newBuilder().setName("Foo").build());
		Results<ScoredDocument> results = index.search(Query.newBuilder().build(query));
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
