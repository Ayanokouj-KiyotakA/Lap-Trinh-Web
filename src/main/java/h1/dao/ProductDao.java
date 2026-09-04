package h1.dao;

import java.util.List;

import h1.config.JpaConfig;
import h1.entity.Product;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.Query;
import jakarta.persistence.TypedQuery;

public class ProductDao implements IProductDao {

	@Override
	public void insert(Product product) {
		EntityManager enma = JpaConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.persist(product); // insert vào bảng
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}

	@Override
	public void update(Product product) {
		EntityManager enma = JpaConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.merge(product); // update vào bảng
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}

	@Override
	public void delete(int productId) throws Exception {
		EntityManager enma = JpaConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			Product product = enma.find(Product.class, productId);
			if (product != null) {
				enma.remove(product);
			} else {
				throw new Exception("Không tìm thấy");
			}
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}

	@Override
	public Product findById(int productId) {
		EntityManager enma = JpaConfig.getEntityManager();
		return enma.find(Product.class, productId);
	}

	@Override
	public List<Product> findAll() {
		EntityManager enma = JpaConfig.getEntityManager();
		TypedQuery<Product> query = enma.createNamedQuery("Product.findAll", Product.class);
		return query.getResultList();
	}

	@Override
	public List<Product> findLatest(int limit) {
		EntityManager enma = JpaConfig.getEntityManager();
		TypedQuery<Product> query = enma.createNamedQuery("Product.findLatest", Product.class);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	@Override
	public List<Product> findAllActive(int page, int pagesize) {
		EntityManager enma = JpaConfig.getEntityManager();
		String jpql = "SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.createdDate DESC";
		TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
		query.setFirstResult(page * pagesize);
		query.setMaxResults(pagesize);
		return query.getResultList();
	}

	@Override
	public int countActive() {
		EntityManager enma = JpaConfig.getEntityManager();
		String jpql = "SELECT count(p) FROM Product p WHERE p.status = 1";
		Query query = enma.createQuery(jpql);
		return ((Long) query.getSingleResult()).intValue();
	}

	@Override
	public List<Product> findLatestActive(int limit) {
		EntityManager enma = JpaConfig.getEntityManager();
		String jpql = "SELECT p FROM Product p WHERE p.status = 1 ORDER BY p.createdDate DESC";
		TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
		query.setMaxResults(limit);
		return query.getResultList();
	}

	@Override
	public List<Product> searchByName(String name) {
		EntityManager enma = JpaConfig.getEntityManager();
		String jpql = "SELECT p FROM Product p WHERE p.productname LIKE :name";
		TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
		query.setParameter("name", "%" + name + "%");
		return query.getResultList();
	}

	@Override
	public List<Product> findAll(int page, int pagesize) {
		EntityManager enma = JpaConfig.getEntityManager();
		String jpql = "SELECT p FROM Product p ORDER BY p.createdDate DESC";
		TypedQuery<Product> query = enma.createQuery(jpql, Product.class);
		query.setFirstResult(page * pagesize);
		query.setMaxResults(pagesize);
		return query.getResultList();
	}

	@Override
	public int count() {
		EntityManager enma = JpaConfig.getEntityManager();
		String jpql = "SELECT count(p) FROM Product p";
		Query query = enma.createQuery(jpql);
		return ((Long) query.getSingleResult()).intValue();
	}
}
