package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.dtos.response.ApiResponse;
import com.example.airlinereservation.utils.Constants;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
public class CloudServiceTest {
	@Autowired
	private CloudService cloudService;
	@Test void uploadImageTest(){
		Path path = Paths.get(Constants.TEST_IMAGE_LOCATION);
		try(InputStream inputStream = Files.newInputStream(path)){
			MultipartFile multipartFile = new MockMultipartFile("test image", inputStream);
			ApiResponse<?> response =  cloudService.uploadFile(multipartFile);
			assertNotNull(response);
			assertThat(response.getResponseData()).isNotNull();
		}
		catch (IOException exception){
			throw new RuntimeException(exception);
		}
	}
	
	
}

































































































































































































