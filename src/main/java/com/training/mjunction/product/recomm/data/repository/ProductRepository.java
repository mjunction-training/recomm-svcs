
package com.training.mjunction.product.recomm.data.repository;

import java.util.List;

import org.springframework.data.neo4j.annotation.Query;
import org.springframework.data.neo4j.repository.Neo4jRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.training.mjunction.product.recomm.data.nodes.Product;

@Repository
public interface ProductRepository extends Neo4jRepository<Product, Long> {

	@Query("MATCH (p:Product)-[r: RECOMMENDS]->(p1:Product) WHERE p.productId = {productId} RETURN p,r,p1 LIMIT {limit}")
	List<Product> findByProductId(@Param("productId") String productId, @Param("limit") int limit);

	@Query("MATCH (p:Product)-[r: RECOMMENDS]->(p1:Product) WHERE p.name = {productName} RETURN p,r,p1 LIMIT {limit}")
	List<Product> findByProductName(@Param("productName") String productName, @Param("limit") int limit);

	@Query("MATCH (p:Product {productId: {productId}}) DETACH DELETE p")
	void deleteByProductId(@Param("productId") String productId);

}
