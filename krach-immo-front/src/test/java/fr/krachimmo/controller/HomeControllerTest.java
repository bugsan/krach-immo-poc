package fr.krachimmo.controller;

import static fr.krachimmo.controller.HomeControllerTestFixtures.sampleAnnoncesAnswer;
import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.apphosting.api.ApiProxy;
import com.google.apphosting.api.ApiProxy.Environment;

public class HomeControllerTest {

	private Environment environment = mock(Environment.class);
	private DatastoreService datastoreService = mock(DatastoreService.class);
	private PreparedQuery preparedQuery = mock(PreparedQuery.class);

	@Test
	public void getAllAnnonces() {
		ApiProxy.setEnvironmentForCurrentThread(this.environment);
		when(this.environment.getAppId()).thenReturn("krach-immo");
		when(this.preparedQuery.asIterable()).then(sampleAnnoncesAnswer());
		when(this.datastoreService.prepare(new Query("Annonce"))).thenReturn(this.preparedQuery);
		HomeController controller = new HomeController();
		controller.datastoreService = this.datastoreService;

		ResponseEntity<String> responseEntity = controller.getAllAnnonces();
		assertEquals(HttpStatus.OK, responseEntity.getStatusCode());
		assertEquals("application/json", responseEntity.getHeaders().get("Content-Type").get(0));
		assertEquals("[[50,3500],[35,5100]]", responseEntity.getBody());
	}
}
