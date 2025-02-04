package com.example.demo.serviceImpl;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.multipart.MultipartFile;

import com.example.demo.service.FileService;

@Service
public class FileServiceImp implements FileService {
	
	@Value("${file.upload-dir}")
	private String path;

//	@Override
//	public String uploadImage(String path, MultipartFile file) throws IOException {
//
//		 String name = file.getOriginalFilename();		 
//		 String randomID = UUID.randomUUID().toString();
//		 String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
//		 String filePath = path+ File.separator+fileName1;
//		 File f = new File(path);
//		 if(!f.exists()) {
//			 f.mkdir();
//		 }	
//		 Files.copy(file.getInputStream(),Paths.get(filePath));
//		return randomID;
//	}
	@Override
	public String uploadImage(String path, MultipartFile file) throws IOException {
	    String name = file.getOriginalFilename();
	    if (name == null || name.isEmpty()) {
	        throw new IOException("Invalid file name");
	    }

	    String randomID = UUID.randomUUID().toString();
	    String fileName1 = randomID.concat(name.substring(name.lastIndexOf(".")));
	    String filePath = path + File.separator + fileName1;
	    File f = new File(path);
	    if (!f.exists()) {
	        if (!f.mkdirs()) {
	            throw new IOException("Failed to create directory: " + path);
	        }
	    }
	    Files.copy(file.getInputStream(), Paths.get(filePath));
	    return fileName1;
	}

}
