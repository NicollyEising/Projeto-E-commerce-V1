package com.example.demo;

import io.swagger.v3.oas.models.OpenAPI;

//@Configuration
public class SwaggerConfig {

	//@Bean
	public OpenAPI customOpenAPI() {
		return new OpenAPI();
	}

}
