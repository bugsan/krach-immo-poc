package fr.krachimmo.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;

import fr.krachimmo.service.quotation.QuotationService;

/**
 *
 * @author Sébastien Chatel
 * @since 6 July 2014
 */
@Controller
public class QuotationController {
	@Autowired
	QuotationService quotationService;
	@RequestMapping("/services/quotation")
	@ResponseStatus(HttpStatus.CREATED)
	public void emitQuote() throws Exception {
		quotationService.emitQuote();
	}
}
