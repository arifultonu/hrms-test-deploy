package com.hrms.service.multimedia;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface StorageService {
    public void uploadFile(MultipartFile file, String fileNewName);
    public void uploadTrainingFile(MultipartFile file, String fileNewName);

    public void uploadDocument(MultipartFile file, String fileNewName);

    /** Multiple File upload
     * this section includes the multiple file uploading.....
     * */
    public void uploadMultipleFile(MultipartFile file, String fileNewName);

}
