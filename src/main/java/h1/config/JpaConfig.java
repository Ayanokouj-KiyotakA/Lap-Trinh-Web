package h1.config;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;

public class JpaConfig {
	// Tao 1 lan duy nhat (singleton) va dung chung cho ca ung dung.
	// Truoc day moi lan goi getEntityManager() lai tao 1 EntityManagerFactory
	// moi -> rat nang (mo lai connection pool moi moi lan) va khong bao gio
	// duoc dong lai -> ro ri tai nguyen sau mot thoi gian chay.
	private static final EntityManagerFactory FACTORY = Persistence
			.createEntityManagerFactory("jpa-hibernate-sqlserver");

	private JpaConfig() {
	}

	public static EntityManager getEntityManager() {
		return FACTORY.createEntityManager();
	}
}
