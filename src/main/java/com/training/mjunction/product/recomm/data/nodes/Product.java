package com.training.mjunction.product.recomm.data.nodes;

import java.util.List;

import org.neo4j.ogm.annotation.GeneratedValue;
import org.neo4j.ogm.annotation.Id;
import org.neo4j.ogm.annotation.Index;
import org.neo4j.ogm.annotation.NodeEntity;
import org.neo4j.ogm.annotation.Property;
import org.neo4j.ogm.annotation.Relationship;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonPropertyOrder;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data()
@NoArgsConstructor
@NodeEntity
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonPropertyOrder({ "id", "name", "productId", "products" })
public class Product {

	@Id
	@GeneratedValue
	@JsonProperty("id")
	private Long id;

	@Property("name")
	@Index(unique = true)
	@JsonProperty("name")
	private String name;

	@Property("productId")
	@Index(unique = true)
	@JsonProperty("productId")
	private String productId;

	@JsonProperty("products")
	@JsonIgnoreProperties("products")
	@Relationship(type = "RECOMMENDS", direction = Relationship.OUTGOING)
	private List<Product> products;

	@Override
	public boolean equals(final Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}
		if (getClass() != obj.getClass()) {
			return false;
		}
		final Product other = (Product) obj;
		if (id == null) {
			if (other.id != null) {
				return false;
			}
		} else if (!id.equals(other.id)) {
			return false;
		}
		if (name == null) {
			if (other.name != null) {
				return false;
			}
		} else if (!name.equals(other.name)) {
			return false;
		}
		if (productId == null) {
			if (other.productId != null) {
				return false;
			}
		} else if (!productId.equals(other.productId)) {
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (id == null ? 0 : id.hashCode());
		result = prime * result + (name == null ? 0 : name.hashCode());
		result = prime * result + (productId == null ? 0 : productId.hashCode());
		return result;
	}

	@Override
	public String toString() {
		return "Product [id=" + id + ", name=" + name + ", productId=" + productId + "]";
	}

}
