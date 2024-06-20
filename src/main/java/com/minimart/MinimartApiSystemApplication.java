package com.minimart;

import org.modelmapper.Condition;
import org.modelmapper.Conditions;
import org.modelmapper.ModelMapper;
import org.modelmapper.spi.MappingContext;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class MinimartApiSystemApplication {

	public static void main(String[] args) {
		SpringApplication.run(MinimartApiSystemApplication.class, args);
	}

	@Bean
	public ModelMapper modelMapper() {
		ModelMapper modelMapper = new ModelMapper();

		// Define a condition to skip properties that are not present in the destination type
		Condition<?, ?> skipUnmapped = new Condition<Object, Object>() {
			@Override
			public boolean applies(MappingContext<Object, Object> context) {
				return context.getMapping() != null;
			}
		};

		// Apply the condition to the configuration
		modelMapper.getConfiguration().setPropertyCondition(skipUnmapped);

		return modelMapper;
	}

}
