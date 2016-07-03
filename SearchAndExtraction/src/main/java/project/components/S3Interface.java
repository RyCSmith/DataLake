package project.components;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.*;

import project.database.Document;
import project.database.DocumentJdbcTemplate;

import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.FileCopyUtils;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class S3Interface {
	
	private String TEMP_FILES_DIR;
	private AmazonS3Client amazonS3Client;
	private String bucket;
	
	@Autowired
	@Qualifier("docJdbcBean")
	DocumentJdbcTemplate docJdbc;

	/**
	 * Uploads an individual file from temp storage to s3.
	 * @param file
	 * @return
	 * @throws IOException
	 */
	private PutObjectResult processSingleFile(TempFileData file) throws IOException {
		//process metadata
		ObjectMetadata metadata = new ObjectMetadata();
		if (file.getContentType() != null)
			metadata.setContentType(file.getContentType());
		metadata.setContentLength(new File(file.getPath()).length());
		//upload to s3
		InputStream inputStream = new FileInputStream(file.getPath());
		PutObjectRequest putObjectRequest = new PutObjectRequest(bucket, file.getName(), inputStream, metadata);
		putObjectRequest.setCannedAcl(CannedAccessControlList.BucketOwnerFullControl);
		PutObjectResult putObjectResult = amazonS3Client.putObject(putObjectRequest);
		IOUtils.closeQuietly(inputStream);
		return putObjectResult;
	}

	/**
	 * Saves Multipart files locally and then submits for upload to s3.
	 * @param files
	 * @return
	 */
	public List<TempFileData> upload(MultipartFile[] files, String username) {
		//save all files to local storage
		ArrayList<TempFileData> tempFiles = new ArrayList<TempFileData>();
		for (MultipartFile file : files) {
			if (!file.isEmpty()) {
				try {
					String name = generateOutputName(file.getOriginalFilename(), file.getContentType());
					String path = TEMP_FILES_DIR + "/" + name;
					BufferedOutputStream stream = new BufferedOutputStream(new FileOutputStream(new File(path)));
	                FileCopyUtils.copy(file.getInputStream(), stream);
					stream.close();
					tempFiles.add(new TempFileData(path, name, file.getContentType()));
				}
				catch (Exception e) {
					System.out.println("An error occurred uploading file: " + file.getOriginalFilename());
				}
			}
		}
		//upload files to s3 one at a time
		List<PutObjectResult> putObjectResults = new ArrayList<PutObjectResult>();
		for (TempFileData file : tempFiles) {
			try {
				putObjectResults.add(processSingleFile(file));
				int id = docJdbc.createAndReturnKey(username, file.getName());
				file.setDocument(docJdbc.getDocument(id));
			}
			catch (Exception e) {
				System.out.println("An error occurred uploading file: " + file.getName());
			}
		}
		return tempFiles;
	}

	public byte[] getFileStream(String docName) throws IOException {
		try {
			GetObjectRequest getObjectRequest = new GetObjectRequest(bucket, docName);
			S3Object s3Object = amazonS3Client.getObject(getObjectRequest);
			S3ObjectInputStream objectInputStream = s3Object.getObjectContent();
			byte[] bytes = IOUtils.toByteArray(objectInputStream);
			return bytes;
		} catch (Exception e) {
			return null;
		}
	}
	
	/**
	 * Ensures extension is present in filename based on content-type.
	 * @param originalName
	 * @param type
	 * @return
	 */
	private String generateOutputName(String originalName, String type) {
		if (type == null)
			return originalName;
		String extension = null;
		if (type.equals("text/xml") || type.equals("application/xml") || type.equals("application/rss+xml"))
			extension = ".xml";
		else if (type.equals("text/pdf") || type.equals("application/pdf"))
			extension = ".pdf";
		else if (type.equals("text/json") || type.equals("application/json"))
			extension = ".json";
		else if (type.equals("text/plain"))
			extension = ".txt";
		else if (type.equals("text/html") || type.equals("application/html"))
			extension = ".html";
		else if (type.equals("text/csv"))
			extension = ".csv";
		if (extension != null && !originalName.endsWith(extension))
			originalName = originalName + extension;
		
		//truncate name if it's too long
		if (originalName.length() > 254) {
			int extensionIndex = originalName.lastIndexOf(".");
			String firstPiece = originalName.substring(0, extensionIndex);
			String lastPiece = originalName.substring(extensionIndex);
			originalName = firstPiece.substring(0, 254 - extensionIndex) + lastPiece;
		}
		return originalName;
	}
	
	public void setTempFilesDir(String temp_files_dir) {
		TEMP_FILES_DIR = temp_files_dir;
	}
	
	public void setAmazonS3Client(AmazonS3Client amazonS3Client) {
		this.amazonS3Client = amazonS3Client;
	}
	
	public void setBucket(String bucket) {
		this.bucket = bucket;
	}
}