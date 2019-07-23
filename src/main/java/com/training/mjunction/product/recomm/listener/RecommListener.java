package com.training.mjunction.product.recomm.listener;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.stereotype.Component;

import com.training.mjunction.product.recomm.data.nodes.Product;
import com.training.mjunction.product.recomm.data.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;

@Log4j2
@Component
public class RecommListener {

	@Autowired
	private ProductRepository repository;

	@RabbitListener(queues = "${spring.rabbitmq.queue.name}")
	public void save(@Payload final Product recomm, @Header("action") final String action) {

		log.error(() -> "Received event " + recomm + " " + action);

		switch (action) {

		case "create":

			log.error(() -> "Creating product " + recomm);
			repository.save(recomm);

			break;

		case "update":

			if (repository.existsById(recomm.getId())) {
				log.error(() -> "Updating product " + recomm);
				repository.save(recomm);
			}

			break;

		case "delete":

			if (repository.existsById(recomm.getId())) {
				log.error(() -> "Deleting product " + recomm);
				repository.deleteById(recomm.getId());
			}

			break;

		default:

			log.error(() -> "Unknown action :" + action);

		}

	}

}
