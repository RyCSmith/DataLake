package project.webComponents;

import java.io.BufferedOutputStream;

import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;
import java.io.File;
import java.io.FileNotFoundException;

import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.Session;
import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.jms.core.MessageCreator;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import project.components.S3Interface;
import project.components.TempFileData;
import project.database.Document;
import project.database.DocumentJdbcTemplate;
import project.database.User;
import project.database.UserJdbcTemplate;

@Controller
public class AccountsController {
	
	@Autowired
	@Qualifier("s3Interface")
	private S3Interface s3Manager;

	@Autowired
	@Qualifier("userJdbcBean")
	UserJdbcTemplate userJdbc;
	
	@Autowired
	@Qualifier("docJdbcBean")
	DocumentJdbcTemplate docJdbc;
	
	@Autowired
	@Qualifier("sessionHelperFunctionsBean")
	SessionHelperFunctions shf;
	
	@Autowired
	@Qualifier("jmsTemplateBean")
	JmsTemplate jmsTemplate;
	
	@RequestMapping("/")
	public String home(Model model, HttpSession session) {
		model.addAttribute("isLoggedIn", shf.isLoggedIn(session));
		if (shf.isLoggedIn(session))
			model.addAttribute("documents", docJdbc.getDocsByUser((String) session.getAttribute("username")));
		return "index";
	}
	
	@RequestMapping(value="/authenticate", params={"username", "password"}, method = RequestMethod.POST)
	public String authenticate(@RequestParam(value="username", required=true) String username,
		@RequestParam(value="password", required=true) String password,
		String search, HttpSession session, RedirectAttributes redirectAttributes) {
		
		User user = userJdbc.getUser(username);
		if (user != null && user.getPassword().equals(password)) {
			session.setAttribute("isAuthenticated", true);
			session.setAttribute("username", username);
			redirectAttributes.addFlashAttribute("resultMessage", "Login Successful");
		} else {
			session.setAttribute("isAuthenticated", false);
			redirectAttributes.addFlashAttribute("resultMessage", "We could not authenticate you with the provided credentials.");
		}		
		return "redirect:/";
	}
	
	@RequestMapping(value="/logout", method = RequestMethod.POST)
	public String authenticate(HttpSession session, RedirectAttributes redirectAttributes) {
		session.invalidate();
		redirectAttributes.addFlashAttribute("resultMessage", "You have been logged out.");	
		return "redirect:/";
	}
	
	@RequestMapping(value="/signup", method = RequestMethod.POST)
	public String signup(@RequestParam(value="username", required=true) String username,
			@RequestParam(value="first", required=true) String first,
			@RequestParam(value="last", required=true) String last,
			@RequestParam(value="password", required=true) String password,
			RedirectAttributes redirectAttributes,
			HttpSession session) {
		userJdbc.create(username, first, last, password);
		redirectAttributes.addFlashAttribute("resultMessage", "New account created.<br>You have been signed in.");	
		session.setAttribute("username", username);
		session.setAttribute("isAuthenticated", true);
		return "redirect:/";
	}
	
	@RequestMapping(value = "/uploadFile", method = RequestMethod.POST)
	public String handleFileUpload(@RequestParam("file") MultipartFile[] files, HttpSession session) {
		if (shf.isLoggedIn(session)) {
			List<TempFileData> newDocs = s3Manager.upload(files, (String) session.getAttribute("username"));
			//sending messages to extractor
			for (TempFileData file : newDocs) {
				final String message = file.getDocument().getId() + " " 
						+ file.getDocument().getName() + " " + file.getPath() + " " + file.getType();
				jmsTemplate.send(new MessageCreator() {
					public Message createMessage(Session session) throws JMSException {
					     return session.createTextMessage(message);
					 }
				});
			}
		}
		return "redirect:/";
	}
	
	@RequestMapping(value = "/setDocPermission", method = RequestMethod.POST)
	public ResponseEntity setDocPermission(@RequestParam("docID") Integer docID, 
			@RequestParam("permLevel") Character permLevel, HttpSession session) {
		if (shf.isLoggedIn(session)) {
			Document doc = docJdbc.getDocument(docID);
			if (doc.getUsername().equals(session.getAttribute("username"))) {
				docJdbc.updatePermission(docID, permLevel);
				return new ResponseEntity(HttpStatus.OK);
			}
			return new ResponseEntity(HttpStatus.INTERNAL_SERVER_ERROR);
		}
		return new ResponseEntity(HttpStatus.BAD_REQUEST);
	}
	
	@RequestMapping(value="/downloadFile/{id}")
	public ResponseEntity getDownload(@PathVariable Integer id, HttpSession session) {
		try {
			//check permission
			Document requestedDoc = docJdbc.getDocument(id);
			byte[] bytes = null;
			if (requestedDoc.getPermission() == 'A')
				bytes = s3Manager.getFileStream(requestedDoc.getName());
			else if(shf.isLoggedIn(session)) {
				String authenticatedUser = (String) session.getAttribute("username");
				if (requestedDoc.getUsername().equals(authenticatedUser))
					bytes = s3Manager.getFileStream(requestedDoc.getName());
				else if (requestedDoc.getPermission() == 'E') {
					User u = userJdbc.getUser(authenticatedUser);
					if (u.getPermLevel().equalsIgnoreCase("E"))
						bytes = s3Manager.getFileStream(requestedDoc.getName());
				}
			}
			
			//permission failure or error getting file
			if (bytes == null)
				return new ResponseEntity(HttpStatus.FORBIDDEN);
			
			//return stream response
			String fileName = URLEncoder.encode(requestedDoc.getName(), "UTF-8").replaceAll("\\+", "%20");
			HttpHeaders httpHeaders = new HttpHeaders();
			httpHeaders.setContentType(MediaType.APPLICATION_OCTET_STREAM);
			httpHeaders.setContentLength(bytes.length);
			httpHeaders.setContentDispositionFormData("attachment", fileName);
			return new ResponseEntity<byte[]>(bytes, httpHeaders, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity(HttpStatus.BAD_REQUEST);
		}
	}

}
