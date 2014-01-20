package fr.krachimmo.controller;

import java.util.ArrayList;
import java.util.List;

import org.mockito.invocation.InvocationOnMock;
import org.mockito.stubbing.Answer;

import com.google.appengine.api.datastore.Entity;

/**
 *
 * @author Sébastien Chatel
 * @since 20 January 2014
 */
public final class HomeControllerTestFixtures {
	private HomeControllerTestFixtures() { }

	public static Answer<List<Entity>> sampleAnnoncesAnswer() {
		return new Answer<List<Entity>>() {
			@Override
			public List<Entity> answer(InvocationOnMock invocation) throws Throwable {
				return sampleAnnonces();
			}
		};
	}

	public static List<Entity> sampleAnnonces() {
		List<Entity> entities = new ArrayList<Entity>();
		entities.add(sampleAnnonce(50L, 3500L));
		entities.add(sampleAnnonce(35L, 5100L));
		return entities;
	}

	public static Entity sampleAnnonce(Long surface, Long price) {
		Entity entity = new Entity("Annonce");
		entity.setUnindexedProperty("surface", surface);
		entity.setUnindexedProperty("price", price);
		return entity;
	}
}
