package h1.service;

import java.util.List;

import h1.dao.IProductDao;
import h1.dao.ProductDao;
import h1.entity.Product;

public class ProductServiceImpl implements IProductService {
	public IProductDao productDao = new ProductDao();

	@Override
	public List<Product> findAll() {
		return productDao.findAll();
	}

	@Override
	public List<Product> findLatest(int limit) {
		return productDao.findLatest(limit);
	}

	@Override
	public Product findById(int id) {
		return productDao.findById(id);
	}

	@Override
	public List<Product> searchByName(String keyword) {
		return productDao.searchByName(keyword);
	}

	@Override
	public void insert(Product product) {
		productDao.insert(product);
	}

	@Override
	public void update(Product product) {
		Product existing = this.findById(product.getProductId());
		if (existing != null) {
			productDao.update(product);
		}
	}

	@Override
	public void delete(int id) {
		try {
			productDao.delete(id);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public int count() {
		return productDao.count();
	}

	@Override
	public List<Product> findAll(int page, int pagesize) {
		return productDao.findAll(page, pagesize);
	}
}
