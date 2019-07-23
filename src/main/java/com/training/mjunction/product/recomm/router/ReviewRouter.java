package com.training.mjunction.product.recomm.router;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.web.reactive.function.server.RequestPredicates.DELETE;
import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RequestPredicates.PUT;
import static org.springframework.web.reactive.function.server.RequestPredicates.accept;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import com.training.mjunction.product.recomm.handler.ReviewHandler;

@Configuration
public class ReviewRouter {

	@Bean
	public RouterFunction<ServerResponse> reviewRouterFunction(final ReviewHandler reviewHandler) {

		return route(GET("/api/v1/recommends").and(accept(APPLICATION_JSON)), reviewHandler::getProducts)

				.andRoute(GET("/api/v1/recommends/{productId}").and(accept(APPLICATION_JSON)),
						reviewHandler::findProduct)

				.andRoute(POST("/api/v1/recommends").and(accept(APPLICATION_JSON)), reviewHandler::postProduct)

				.andRoute(PUT("/api/v1/recommends/{id}").and(accept(APPLICATION_JSON)), reviewHandler::putProduct)

				.andRoute(DELETE("/api/v1/recommends/{id}").and(accept(APPLICATION_JSON)),
						reviewHandler::deleteProduct);

	}

}
