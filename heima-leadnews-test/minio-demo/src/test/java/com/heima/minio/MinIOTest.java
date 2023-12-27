package com.heima.minio;

import com.heima.file.service.FileStorageService;
import io.minio.MinioClient;
import io.minio.PutObjectArgs;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.FileInputStream;
import java.io.FileNotFoundException;

@Slf4j
@SpringBootTest
public class MinIOTest {

    @Autowired
    FileStorageService fileStorageService;

    @Test
    public void putObject() {
        MinioClient minioClient = MinioClient
                .builder()
                .credentials("minioadmin", "minioadmin")
                .endpoint("http://192.168.1.100:9000")
                .build();

        try {

            FileInputStream fileInputStream = new FileInputStream("E:\\Code\\heima-leadnews\\heima-leadnews\\heima-leadnews-test\\freemarker-demo\\src\\test\\java\\com\\heima\\freemarker\\list.html");

            PutObjectArgs putObjectArgs = PutObjectArgs
                    .builder()
                    .object("list.html")
                    .contentType("text/html")
                    .bucket("leadnews")
                    .stream(fileInputStream, fileInputStream.available(), -1)
                    .build();

            minioClient.putObject(putObjectArgs);
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        log.info("http://192.168.1.100:9000/leadnews/list.html");
    }

    @Test
    public void testUploadImgFile() throws FileNotFoundException {
        FileInputStream fileInputStream = new FileInputStream("E:\\Code\\heima-leadnews\\heima-leadnews\\heima-leadnews-test\\freemarker-demo\\src\\test\\java\\com\\heima\\freemarker\\list.html");
        String filePath = fileStorageService.uploadHtmlFile("", "list.html", fileInputStream);
        log.info(filePath);
    }
}
