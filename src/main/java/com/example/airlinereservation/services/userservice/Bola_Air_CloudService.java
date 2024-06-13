package com.example.airlinereservation.services.userservice;

import com.cloudinary.Cloudinary;
import com.cloudinary.Uploader;
import com.cloudinary.utils.ObjectUtils;
import com.example.airlinereservation.dtos.response.ApiResponse;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.Map;

@Service
@AllArgsConstructor
public class Bola_Air_CloudService implements CloudService{
	
//	private CloudConfig config;
	@Override
	public ApiResponse<?> uploadFile(MultipartFile multipartFile) {
		try {
			Cloudinary cloudinary = new Cloudinary();
			Uploader uploader = cloudinary.uploader();
			Map<?, ?> uploadResponse = uploader.upload(multipartFile.getBytes(), ObjectUtils.asMap(
					"public_id", "cloudinary_image/promiscuous/uploaded_images/" + multipartFile.getName(),
//					"api_key", config.getCloudApiKey(),
//					"api_secret", config.getCloudApiSecret(),
//					"cloud_name", config.getCloudApiName(),
					"secure", true
			));
			ApiResponse<String> response = new ApiResponse<>();
			response.setResponseData(uploadResponse.get("url").toString());
			System.out.println(response);
			return response;
		}catch (Throwable exception){
			throw new RuntimeException(exception);
		}
	}
}
