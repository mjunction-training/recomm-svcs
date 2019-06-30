package com.training.mjunction.product.recomm.handler;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.recomm.data.nodes.Product;
import com.training.mjunction.product.recomm.data.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class ReviewHandler {

	@Autowired
	private ProductRepository productRepository;

	public Mono<ServerResponse> findProduct(final ServerRequest request) {

		log.info("Retrieveing product");

		// parse query parameter product id
		final String productId = request.pathVariable("productId");

		if (isNotBlank(productId)) {

			// fetch product from repository by product id, build response
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
					.body(Flux.fromIterable(productRepository.findByProductId(productId, 500)), Product.class);

		}

		// build not found response
		return ServerResponse.notFound().build();

	}

	public Mono<ServerResponse> getProducts(final ServerRequest request) {

		log.info("Retrieveing products");

		// parse query parameter product name
		final String productName = request.queryParam("productName").orElseGet(() -> null);

		if (isBlank(productName)) {

			// fetch all products from repository
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
					.body(Flux.fromIterable(productRepository.findAll()), Product.class);

		}

		if (isNotBlank(productName)) {

			// fetch product from repository by product name, build response
			return ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
					.body(Flux.fromIterable(productRepository.findByProductName(productName, 500)), Product.class);

		}

		// build not found response
		return ServerResponse.notFound().build();

	}

	public Mono<ServerResponse> postProduct(final ServerRequest request) {

		log.info("Saving product");

		final Mono<Product> monoProduct = request.bodyToMono(Product.class);

		return ServerResponse.status(201)
				.build(monoProduct.flatMap(p -> Mono.fromSupplier(() -> productRepository.save(p))).then());

	}

	public Mono<ServerResponse> putProduct(final ServerRequest request) {

		log.info("Updating product");

		// parse id from path-variable
		final String productId = request.pathVariable("productId");

		// get product data from request object
		final Mono<Product> monoProduct = request.bodyToMono(Product.class);

		monoProduct.doOnNext(b -> b.setProductId(productId)).then();

		// get product from repository
		final Mono<Product> responseMono = monoProduct.doOnNext(productRepository::save);

		// build response
		return responseMono.flatMap(product -> ServerResponse.ok().contentType(MediaType.APPLICATION_JSON)
				.body(BodyInserters.fromObject(product)));

	}

	public Mono<ServerResponse> deleteProduct(final ServerRequest request) {

		log.info("Deleting product");

		// parse id from path-variable
		final String productId = request.pathVariable("productId");

		productRepository.deleteByProductId(productId);

		// get product from repository
		final Mono<String> responseMono = Mono.just("Deleted Succesfully!");

		// build response
		return responseMono.flatMap(strMono -> ServerResponse.accepted().contentType(MediaType.TEXT_PLAIN)
				.body(BodyInserters.fromObject(strMono)));

	}

}
