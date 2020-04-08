package com.example.s3.services;

import com.example.s3.dto.FileDTO;
import com.example.s3.dto.SavedFileDTO;

interface FileManagerService {

	SavedFileDTO uploadFile(FileDTO fileDTO);

	String getFileInBase64(String fileName);

	void deleteFile(String fileName);

	byte[] getFileAsBytes(String fileName);
}
