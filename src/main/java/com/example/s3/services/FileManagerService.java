package com.example.s3.services;

import com.amazonaws.util.IOUtils;
import com.example.s3.dto.FileDTO;
import com.example.s3.dto.SavedFileDTO;
import com.example.s3.utils.AmazonClient;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Service
public class FileManagerService {

	private static final String UPLOAD_FOLDER_NAME = "public-files";
	private final AmazonClient amazonClient;

	@Autowired
	FileManagerService(AmazonClient amazonClient) {
		this.amazonClient = amazonClient;
	}

	public SavedFileDTO uploadFile(FileDTO fileDTO) {
		SavedFileDTO savedFile = new SavedFileDTO();
		savedFile.setGeneratedFileName(generateFileName(fileDTO));
		savedFile.setOriginalFileName(fileDTO.getFileName());
		File file = convertBase64ToFile(fileDTO.getBase64(), fileDTO.getFileName());
		this.amazonClient.uploadFileToBucket(savedFile.getGeneratedFileName(), file, UPLOAD_FOLDER_NAME);
		savedFile.setUploadedAt(new Date());
		try {
			FileUtils.forceDelete(file);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
		return savedFile;
	}

	public String getFileInBase64(String fileName) {
		File file = amazonClient.getFileFromBucket(fileName, UPLOAD_FOLDER_NAME);
		try {
			return Base64.getEncoder().encodeToString(FileUtils.readFileToByteArray(file));
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	public void deleteFile(String fileName) {
		amazonClient.deleteFileFromBucket(fileName, UPLOAD_FOLDER_NAME);
	}

	public byte[] getFileAsBytes(String fileName) {
		InputStream inputStream = amazonClient.getFileInputStream(fileName, UPLOAD_FOLDER_NAME);
		try {
			return IOUtils.toByteArray(inputStream);
		} catch (IOException e) {
			e.printStackTrace();
		}
		return new byte[0];
	}

	private File convertBase64ToFile(String base64Content, String filename) {
		byte[] decodedContent = Base64.getDecoder().decode(base64Content.getBytes(StandardCharsets.UTF_8));
		return bytesToFile(decodedContent, filename);
	}

	private File bytesToFile(byte[] content, String fileName) {
		File file = new File(fileName);
		try (FileOutputStream fos = new FileOutputStream(file)) {
			fos.write(content);
		} catch (IOException e) {
			return null;
		}
		return file;
	}

	//Need to replace all special characters with underscores + add timestamp to filename to make it unique
	private String generateFileName(FileDTO fileDTO) {
		String name = fileDTO.getFileName().replaceAll("[^a-zA-Z0-9.-]", "_");
		return (new Date().getTime() + "_" + name);
	}


}
