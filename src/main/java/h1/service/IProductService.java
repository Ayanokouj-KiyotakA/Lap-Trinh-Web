package h1.service;

import java.util.List;

import h1.entity.Product;

public interface IProductService {
	void insert(Product product);

	int count();

	List<Product> findAll(int page, int pagesize);

	List<Product> searchByName(String name);

	List<Product> findAll();

	List<Product> findLatest(int limit);

	List<Product> findAllActive(int page, int pagesize);

	int countActive();

	List<Product> findLatestActive(int limit);

	Product findById(int productId);

	void delete(int productId);

	void update(Product product);
}
