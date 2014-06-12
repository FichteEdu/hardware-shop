package model.serialization.db;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;

import org.apache.openjpa.persistence.OpenJPAPersistence;

import fpt.com.Product;


public class OpenJPAStrategy implements fpt.com.SerializableStrategy {

	EntityManagerFactory	emf	= null;
	EntityManager			em	= null;
	EntityTransaction		et	= null;

	@Override
	public Product readObject() throws IOException {
		emf = getWithoutConfig();
		// emf = Persistence.createEntityManagerFactory("openjpa",
		// System.getProperties());
		em = emf.createEntityManager();
		et = em.getTransaction();
		et.begin();
		Object obj = em.createQuery("SELECT p FROM Product p").getResultList();
		et.commit();
		return (Product) obj;
	}

	@Override
	public void writeObject(Product obj) throws IOException {
		emf = getWithoutConfig();
		// emf = Persistence.createEntityManagerFactory("openjpa",
		// System.getProperties());
		em = emf.createEntityManager();
		EntityTransaction et = em.getTransaction();
		et.begin();
		em.persist(obj);
		et.commit();
	}

	@Override
	public void close() throws IOException {
		if (em != null)
			em.close();
		if (emf != null)
			emf.close();
	}

	private static EntityManagerFactory getWithoutConfig() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("openjpa.ConnectionURL", "jdbc:postgresql://java.is.uni-due.de/ws1011");
		map.put("openjpa.ConnectionDriverName", "org.postgresql.Driver");
		map.put("openjpa.ConnectionUserName", "ws1011");
		map.put("openjpa.ConnectionPassword", "ftpw10");
		map.put("openjpa.RuntimeUnenhancedClasses", "supported");
		map.put("openjpa.jdbc.SynchronizeMappings", "false");

		List<Class<?>> types = new ArrayList<Class<?>>();
		types.add(Product.class);
		if (!types.isEmpty()) {
			StringBuffer buf = new StringBuffer();
			for (Class<?> c : types) {
				if (buf.length() > 0) {
					buf.append(";");
				}
				buf.append(c.getName());
			} // <class>Product</class>
			map.put("openjpa.MetaDataFactory", "jpa(Types=" + buf.toString() + ")");
		}
		return OpenJPAPersistence.getEntityManagerFactory(map);
	}

}
