package fr.krachimmo.web.scrap;


public interface ScrapOperations {

	<T> T getObject(String url, DocumentMapper<T> mapper);
}
