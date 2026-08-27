package h1.dao;

import h1.config.JpaConfig;
import h1.entity.AppUser;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityTransaction;
import jakarta.persistence.NoResultException;
import jakarta.persistence.TypedQuery;

public class UserDao implements IUserDao {

	@Override
	public AppUser findByUserName(String username) {
		EntityManager enma = JpaConfig.getEntityManager();
		String jpql = "SELECT u FROM AppUser u WHERE u.userName = :username";
		try {
			TypedQuery<AppUser> query = enma.createQuery(jpql, AppUser.class);
			query.setParameter("username", username);
			return query.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@Override
	public void insert(AppUser user) {
		EntityManager enma = JpaConfig.getEntityManager();
		EntityTransaction trans = enma.getTransaction();
		try {
			trans.begin();
			enma.persist(user);
			trans.commit();
		} catch (Exception e) {
			e.printStackTrace();
			trans.rollback();
			throw e;
		} finally {
			enma.close();
		}
	}
}
