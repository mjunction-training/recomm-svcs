package com.training.mjunction.product.recomm.config;

import java.util.HashMap;
import java.util.Map;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.ClassMapper;
import org.springframework.amqp.support.converter.DefaultClassMapper;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.training.mjunction.product.recomm.data.nodes.Product;

@Configuration
@EnableRabbit
public class AmqpConfig {

	@Bean
	public RabbitTemplate rabbitTemplate(final MessageConverter messageConverter,
			final ConnectionFactory connectionFactory) {
		final RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
		rabbitTemplate.setMessageConverter(messageConverter);
		return rabbitTemplate;
	}

	@Bean
	public MessageConverter messageConverter(final ClassMapper classMapper) {
		final Jackson2JsonMessageConverter jsonConverter = new Jackson2JsonMessageConverter();
		jsonConverter.setClassMapper(classMapper);
		return jsonConverter;
	}

	@Bean
	public DefaultClassMapper classMapper() {
		final DefaultClassMapper classMapper = new DefaultClassMapper();
		final Map<String, Class<?>> idClassMapping = new HashMap<>();
		idClassMapping.put("recomm", Product.class);
		classMapper.setIdClassMapping(idClassMapping);
		return classMapper;
	}

}
