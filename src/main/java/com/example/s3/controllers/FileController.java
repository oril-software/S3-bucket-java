package com.example.s3.controllers;

import com.example.s3.dto.FileDTO;
import com.example.s3.dto.SavedFileDTO;
import com.example.s3.services.FileManagerServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class FileController {

	private FileManagerServiceImpl fileManager;

	@Autowired
	FileController(FileManagerServiceImpl fileManager) {
		this.fileManager = fileManager;
	}

	@PostMapping("/files/upload")
	public ResponseEntity<SavedFileDTO> uploadFile(@RequestBody FileDTO fileDTO) {
		return ResponseEntity.ok(fileManager.uploadFile(fileDTO));
	}

	@GetMapping("/files/{fileName}/base64")
	public ResponseEntity<String> getFileInBase64(@PathVariable("fileName") String fileName) {
		return ResponseEntity.ok(fileManager.getFileInBase64(fileName));
	}

	@GetMapping("/files/{fileName}/download")
	public ResponseEntity<Resource> downloadFile(@PathVariable("fileName") String fileName) {
		byte[] content = fileManager.getFileAsBytes(fileName);
		return ResponseEntity.ok()
				.header(HttpHeaders.CONTENT_TYPE, getFileMediaType(fileName))
				.header(HttpHeaders.CONTENT_DISPOSITION, MediaType.APPLICATION_OCTET_STREAM_VALUE)
				.header(HttpHeaders.CONTENT_LENGTH, String.valueOf(content.length))
				.body(new ByteArrayResource(content));
	}

	@DeleteMapping("/files/{fileName:.+}")
	public ResponseEntity<Void> deleteFile(@PathVariable("fileName") String fileName) {
		fileManager.deleteFile(fileName);
		return ResponseEntity.ok().build();
	}

	private String getFileMediaType(String fileName) {
		String mediaType;
		String fileExtension = fileName.substring(fileName.lastIndexOf('.') + 1);
		switch (fileExtension.toLowerCase()) {
			case "pdf":
				mediaType = MediaType.APPLICATION_PDF_VALUE;
				break;
			case "png":
				mediaType = MediaType.IMAGE_PNG_VALUE;
				break;
			case "jpeg":
				mediaType = MediaType.IMAGE_JPEG_VALUE;
				break;
			default:
				mediaType = MediaType.TEXT_PLAIN_VALUE;
		}
		return mediaType;
	}


}
