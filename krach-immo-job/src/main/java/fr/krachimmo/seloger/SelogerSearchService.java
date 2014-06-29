package fr.krachimmo.seloger;

import java.util.concurrent.Future;


public interface SelogerSearchService {

	Future<AnnonceSearchResults> search(String uri);
}
