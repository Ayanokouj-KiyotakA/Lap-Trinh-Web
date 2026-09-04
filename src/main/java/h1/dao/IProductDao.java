package h1.dao;

import java.util.List;

import h1.entity.Product;

public interface IProductDao {
	void insert(Product product);

	int count();

	List<Product> findAll(int page, int pagesize);

	List<Product> searchByName(String name);

	List<Product> findAll();

	List<Product> findLatest(int limit);

	// Cac ham "Active" chi lay san pham co status = 1, dung cho trang cong khai
	// (trang chu, /product), khac voi trang quan tri (admin) hien thi tat ca
	List<Product> findAllActive(int page, int pagesize);

	int countActive();

	List<Product> findLatestActive(int limit);

	Product findById(int productId);

	void delete(int productId) throws Exception;

	void update(Product product);
}
