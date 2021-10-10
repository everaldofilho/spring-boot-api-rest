package com.criativoweb.libraryapi;

import org.junit.jupiter.api.Test;
import org.modelmapper.ModelMapper;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Bean;

@SpringBootTest
class LibraryApiApplicationTests {


	@Bean
	public ModelMapper modelMapper() {
		return new ModelMapper();
	}

	@Test
	void contextLoads() {
	}

}
