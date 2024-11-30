package org.oz.basic_board.utill.file;

import com.amazonaws.AmazonServiceException;
import jakarta.annotation.PostConstruct;
import lombok.Cleanup;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import net.coobird.thumbnailator.Thumbnailator;
import net.coobird.thumbnailator.Thumbnails;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@Log4j2
@RequiredArgsConstructor
public class CustomFileUtil {

    private final S3Uploader s3Uploader;

    @Value("${org.oz.upload.path}")
    private String uploadPath;

    @PostConstruct
    public void init() {
        File tempFolder = new File(uploadPath);

        if(tempFolder.exists() == false) {
            tempFolder.mkdir();
        }

        uploadPath = tempFolder.getAbsolutePath();

        log.info("-------------------------------------");
        log.info(uploadPath);
    }

    public List<String> saveFiles(List<MultipartFile> files)throws RuntimeException{

        if(files == null || files.size() == 0){
            return List.of();
        }

        List<String> uploadNames = new ArrayList<>();

        for (MultipartFile file : files) {

            String savedName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();

            Path savePath = Paths.get(uploadPath, savedName);
            // log.info("실제 경로 : " + savePath);

            try {
                Files.copy(file.getInputStream(), savePath);

                String contentType = file.getContentType();

                log.info("-------------------------");
                log.info("contentType : " + contentType);
                log.info("-------------------------");

                if(file.getContentType().startsWith("image")){

                    String thumbnailFileName = "s_" + savedName;

                    @Cleanup
                    InputStream inputStream = new FileInputStream(new File(uploadPath, savedName));
                    @Cleanup
                    OutputStream outputStream = new FileOutputStream(new File(uploadPath, thumbnailFileName));

                    Thumbnailator.createThumbnail(inputStream, outputStream, 200, 200);

                    Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
                    // 썸네일 S3 업로드
                    s3Uploader.upload(thumbnailPath.toString());
                } // end if

                // S3 업로드
                s3Uploader.upload(savePath.toString());

                uploadNames.add(savedName);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            } // end catch
        } //end for
        return uploadNames;
    }

    public Resource getFile(String fileName) {
        try {
            // S3에서 Resource 로드
            Resource resource = s3Uploader.getFileUrl(fileName);
            if (!resource.exists()) {
                throw new FileNotFoundException("Requested file does not exist: " + fileName);
            }

            return resource;

        } catch (AmazonServiceException e) {
            log.error("S3에서 파일을 가져오는 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("S3에서 파일을 가져오는 중 오류가 발생했습니다.", e);
        } catch (IOException e) {
            log.error("파일 처리 중 오류 발생: {}", e.getMessage());
            throw new RuntimeException("파일 처리 중 오류가 발생했습니다.", e);
        }
    }

    public void deleteFiles(List<String> fileNames) {

        if(fileNames == null || fileNames.size() == 0){
            return;
        }

        fileNames.forEach(fileName -> {

            //썸네일이 있는지 확인하고 삭제
            String thumbnailFileName = "s_" + fileName;
            Path thumbnailPath = Paths.get(uploadPath, thumbnailFileName);
            Path filePath = Paths.get(uploadPath, fileName);

            try {
                Files.deleteIfExists(filePath);
                Files.deleteIfExists(thumbnailPath);
                s3Uploader.removeS3File(fileName);
                s3Uploader.removeS3File(thumbnailFileName);
            } catch (IOException e) {
                throw new RuntimeException(e.getMessage());
            }
        });
    }

}
