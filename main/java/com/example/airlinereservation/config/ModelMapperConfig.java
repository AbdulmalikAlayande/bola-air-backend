package com.example.airlinereservation.config;

import com.example.airlinereservation.data.repositories.UserBioDataRepository;
import org.modelmapper.ModelMapper;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableAutoConfiguration
public class ModelMapperConfig {
	
	@Bean
	public ModelMapper modelMapper(){
		return new ModelMapper();
	}
}
