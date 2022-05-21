package com.hrms.serviceImpl.multimedia;

import com.hrms.service.multimedia.StorageService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

@Service
public class StorageServiceImpl implements StorageService {

    Boolean sysDebug = true;
    @Value("${upload.file.directory}")
    private String path;

    @Value("${upload.file.directory}")
    private String uploadDir;
    String uploadPath;

    @Override
    public void uploadFile(MultipartFile file, String fileNewName) {
        this.uploadPath = this.uploadDir + "profile-pic/";
        if(this.sysDebug) System.out.println(this.uploadPath);
        if(this.sysDebug) System.out.println(this.uploadPath + fileNewName);

        // file validation
        if (file.isEmpty()) {
            System.out.println("File is empty");
            //            throw new StorageException("Failed to store empty file");
        }
        // check directory is exist?
        // if not then create
        if (!Files.isDirectory(Paths.get(this.uploadPath))) {
            System.out.println("Directory is not exist!");
            File file9 = new File(this.uploadPath);
            if (file9.mkdir()) {
                System.out.println("Successfully create directory: " + this.uploadPath);
            } else {
                System.out.println("Fail to create directory: " + this.uploadPath);
            }
        }

        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            Files.copy(is, Paths.get(this.uploadPath + fileNewName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            String msg = String.format("Failed to store file %f", file.getName());
            System.out.println(msg);
        }
    }

    @Override
    public void uploadTrainingFile(MultipartFile file, String fileNewName) {
        this.uploadPath = this.uploadDir + "DocTraining-pic/";
        if(this.sysDebug) System.out.println(this.uploadPath);
        if(this.sysDebug) System.out.println(this.uploadPath + fileNewName);

        // file validation
        if (file.isEmpty()) {
            System.out.println("File is empty");
            //            throw new StorageException("Failed to store empty file");
        }
        // check directory is exist?
        // if not then create
        if (!Files.isDirectory(Paths.get(this.uploadPath))) {
            System.out.println("Directory is not exist!");
            File file9 = new File(this.uploadPath);
            if (file9.mkdir()) {
                System.out.println("Successfully create directory: " + this.uploadPath);
            } else {
                System.out.println("Fail to create directory: " + this.uploadPath);
            }
        }

        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            Files.copy(is, Paths.get(this.uploadPath + fileNewName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            String msg = String.format("Failed to store file %f", file.getName());
            System.out.println(msg);
        }
    }

    @Override
    public void uploadDocument(MultipartFile file, String fileNewName) {
        this.uploadPath = this.uploadDir + "docx/";
        if(this.sysDebug) System.out.println(this.uploadPath);
        if(this.sysDebug) System.out.println(this.uploadPath +"/"+ fileNewName);

        // file validation
        if (file.isEmpty()) {
            System.out.println("File is empty");
            //            throw new StorageException("Failed to store empty file");
        }
        // check directory is exist?
        // if not then create
        if (!Files.isDirectory(Paths.get(this.uploadPath))) {
            System.out.println("Directory is not exist!");
            File file9 = new File(this.uploadPath);
            if (file9.mkdir()) {
                System.out.println("Successfully create directory: " + this.uploadPath);
            } else {
                System.out.println("Fail to create directory: " + this.uploadPath);
            }
        }

        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            Files.copy(is, Paths.get(this.uploadPath + fileNewName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            String msg = String.format("Failed to store file %f", file.getName());
            System.out.println(msg);
        }
    }

    @Override
    public void uploadMultipleFile(MultipartFile file, String fileNewName) {
        this.uploadPath = this.uploadDir + "multiple-pic/";
        if(this.sysDebug) System.out.println(this.uploadPath);
        if(this.sysDebug) System.out.println(this.uploadPath + fileNewName);

        // file validation
        if (file.isEmpty()) {
            System.out.println("File is empty");
            //            throw new StorageException("Failed to store empty file");
        }
        // check directory is exist?
        // if not then create
        if (!Files.isDirectory(Paths.get(this.uploadPath))) {
            System.out.println("Directory is not exist!");
            File file9 = new File(this.uploadPath);
            if (file9.mkdir()) {
                System.out.println("Successfully create directory: " + this.uploadPath);
            } else {
                System.out.println("Fail to create directory: " + this.uploadPath);
            }
        }

        try {
            String fileName = file.getOriginalFilename();
            InputStream is = file.getInputStream();
            Files.copy(is, Paths.get(this.uploadPath + fileNewName), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            String msg = String.format("Failed to store file %f", file.getName());
            System.out.println(msg);
        }
    }



}
