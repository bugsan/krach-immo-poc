package fr.krachimmo.controller;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.krachimmo.quotes.InsertQuoteService;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Controller
public class InsertQuoteController {

	@Autowired
	InsertQuoteService insertQuoteService;

	@RequestMapping("/insert-quote")
	@ResponseStatus(HttpStatus.CREATED)
	public void insertQuote(@RequestParam String filename, @RequestParam String date) throws IOException {
		this.insertQuoteService.insertFromFile(filename, date);
	}
}
