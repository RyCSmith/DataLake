package project.webComponents;

import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import project.components.SearchEngine;
import project.components.SingleWordSingleSearch;
import project.components.TwsSinglePathDisplay;

@Controller
public class SearchController {
	
	@Autowired
	@Qualifier("searchEngineBean")
	SearchEngine searchEngine;
	
	@Autowired
	@Qualifier("sessionHelperFunctionsBean")
	SessionHelperFunctions shf;
	
	@RequestMapping("/search")
	public String home(@RequestParam(value="query", required=true) String query, Model model, HttpSession session) throws InterruptedException {
		model.addAttribute("isLoggedIn", shf.isLoggedIn(session));
		try {
			String[] queryWords = query.split(" ");
			if (queryWords.length == 1) {
				List<SingleWordSingleSearch> results = 
						searchEngine.singleWordSearch(query, shf.getCurrentUser(session));
				model.addAttribute("resultsList", results);
			} else if (queryWords.length > 1) {
				List<TwsSinglePathDisplay> results = 
						searchEngine.twoWordSearch(queryWords[0], queryWords[1], shf.getCurrentUser(session));
				model.addAttribute("resultsList", results);
				return "resultsTwoWord";
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return "resultsSingleWord";
	}


}
