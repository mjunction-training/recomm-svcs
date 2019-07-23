package com.training.mjunction.product.recomm.handler;

import static org.apache.commons.lang3.StringUtils.isBlank;
import static org.apache.commons.lang3.StringUtils.isNotBlank;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.BodyInserters.fromObject;
import static org.springframework.web.reactive.function.server.ServerResponse.notFound;
import static org.springframework.web.reactive.function.server.ServerResponse.ok;
import static org.springframework.web.reactive.function.server.ServerResponse.status;
import static reactor.core.publisher.Flux.fromIterable;
import static reactor.core.publisher.Mono.fromSupplier;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.recomm.data.nodes.Product;
import com.training.mjunction.product.recomm.data.repository.ProductRepository;

import lombok.extern.log4j.Log4j2;
import reactor.core.publisher.Mono;

@Component
@Log4j2
public class ReviewHandler {

	@Autowired
	private ProductRepository productRepository;

	public Mono<ServerResponse> findProduct(final ServerRequest request) {

		log.info("Get product");

		// parse query parameter product id
		final String productId = request.pathVariable("productId");

		if (isNotBlank(productId)) {

			// fetch product from repository by product id
			final List<Product> products = productRepository.findByProductId(productId, 500);

			if (null == products || products.isEmpty()) {

				// build not found response
				return notFound().build();

			}

			for (final Product product : products) {

				if (product.getProductId().equals(productId)) {

					// build and return response
					return ok().contentType(APPLICATION_JSON).body(fromSupplier(() -> product), Product.class);

				}

			}

		}

		// build not found response
		return notFound().build();

	}

	public Mono<ServerResponse> getProducts(final ServerRequest request) {

		log.info("Get products");

		// parse query parameter product name
		final String productName = request.queryParam("productName").orElseGet(() -> null);

		if (isBlank(productName)) {

			// fetch all products from repository, build response
			return ok().contentType(APPLICATION_JSON).body(fromIterable(productRepository.findAll()), Product.class);

		}

		if (isNotBlank(productName)) {

			// fetch product from repository by product name, build response
			return ok().contentType(APPLICATION_JSON)
					.body(fromIterable(productRepository.findByProductName(productName, 500)), Product.class);

		}

		// build not found response
		return notFound().build();

	}

	public Mono<ServerResponse> postProduct(final ServerRequest request) {

		log.info("Saving product");

		final Mono<Product> monoProduct = request.bodyToMono(Product.class);

		// save product
		final Mono<Product> responseMono = monoProduct.doOnNext(productRepository::save);

		// build response
		return responseMono.flatMap(product -> status(201).contentType(APPLICATION_JSON).body(fromObject(product)));

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
		return responseMono.flatMap(product -> ok().contentType(APPLICATION_JSON).body(fromObject(product)));

	}

	public Mono<ServerResponse> deleteProduct(final ServerRequest request) {

		log.info("Deleting product");

		// parse id from path-variable
		final String productId = request.pathVariable("productId");

		if (isNotBlank(productId)) {

			// fetch product from repository by product id
			final List<Product> products = productRepository.findByProductId(productId, 500);

			if (null == products || products.isEmpty()) {

				// build not found response
				return notFound().build();

			}

			for (final Product product : products) {

				if (product.getProductId().equals(productId)) {

					// Delete the product
					productRepository.deleteByProductId(productId);

					// build and return response
					return status(202).contentType(APPLICATION_JSON).body(fromSupplier(() -> product), Product.class);

				}

			}

		}

		// build not found response
		return notFound().build();

	}

}
