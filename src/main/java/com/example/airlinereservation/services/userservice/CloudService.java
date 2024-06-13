package com.example.airlinereservation.services.userservice;

import com.example.airlinereservation.dtos.response.ApiResponse;
import org.springframework.web.multipart.MultipartFile;

public interface CloudService {
	ApiResponse<?> uploadFile(MultipartFile multipartFile);
	
}
