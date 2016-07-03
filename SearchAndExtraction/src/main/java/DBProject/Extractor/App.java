package DBProject.Extractor;

import java.io.File;

/**
 * Hello world!
 *
 */
public class App {
	
	  public static boolean isTest = false;

		static DatabaseConnector dbc = new DatabaseConnector();
		
		public static boolean parseFile(File file, DatabaseConnector dbc, int docId, String docName, String extension) {
					
			if(extension.equals("csv")) {
				System.out.println("css Parser!!!!");
				CommonsCSVParser.parseWithCommonsCSV(file, docId, docName);
			}
			else if(extension.equals("json")) {
				System.out.println("json Parser!!!!");
				JacksonJSONParser.parseWithJackson(file, docId, docName);	
			}
			else if(extension.equals("xml")) {
				System.out.println("xml Parser!!!!");
				JaxpXMLParser.parseWithJaxp(file, docId, docName);
			}
			else {
				System.err.println("This file format is not supported.");
			}

			return true;
		}
		
	public static void run(String msg) {
		System.out.println("\n\nThe extractor is firing with message: " + msg + "\n");
		String[] arguments = msg.split(" ");
		if (arguments.length != 4) {
			System.out.println("\n\nIncorrect Args given to the extractor!: " + msg);
			return;
		}
		
		int docId = Integer.parseInt(arguments[0]);
		String docName = arguments[1];
		String extension = arguments[3].trim();
		String path = arguments[2];
		File input = new File(path);
		dbc.createConnection(isTest);
		parseFile(input, dbc, docId, docName, extension);
		dbc.closeConnection();
		input.delete();
		System.out.println("Extractor has finished.\n");
	}
}