package model.serialization.db;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
// import javax.persistence.Persistence;
import javax.persistence.Query;

import org.apache.openjpa.persistence.OpenJPAPersistence;

import fpt.com.Product;


public class OpenJPAConnector implements AutoCloseable {

	EntityManagerFactory	emf;
	EntityManager			em;
	EntityTransaction		et;

	public OpenJPAConnector() {
		emf = getWithoutConfig();
		// emf = Persistence.createEntityManagerFactory("openjpa",
		// System.getProperties());
		em = emf.createEntityManager();
		et = em.getTransaction();
	}

	public void insert(Product p) {
		et.begin();
		em.persist(p);
		et.commit();
	}

	public void writeSome(Product[] products) {
		for (Product p : products) {
			insert(p);
		}
	}

	public Product read(long id) {
		et.begin();
		Query query = em.createNamedQuery("Entity.findById");
		query.setParameter("id", id);
		List<?> results = query.getResultList();
		et.commit();
		if (!results.isEmpty()) {
			return (Product) results.get(0);
		}
		return null;
	}

	public Product[] readSome() {
		ArrayList<Product> prodList = new ArrayList<>();
		et.begin();
		for (Object o : em.createQuery("SELECT p FROM Product p").getResultList()) {
			prodList.add((Product) o);
		}
		et.commit();
		return (Product[]) prodList.toArray();
	}

	public Product[] readSomeLast(int n) {
		ArrayList<Product> prodList = new ArrayList<>();
		et.begin();
		Query q = em.createQuery("SELECT p FROM Product p ORDER BY p.id DESC");
		q.setFirstResult(0);
		q.setMaxResults(n);
		for (Object o : q.getResultList()) {
			prodList.add((Product) o);
		}
		et.commit();
		Collections.reverse(prodList);
		return prodList.toArray(new Product[0]);
	}

	@Override
	public void close() {
		em.close();
		emf.close();
	}

	private static EntityManagerFactory getWithoutConfig() {
		Map<String, String> map = new HashMap<String, String>();

		map.put("openjpa.ConnectionURL", "jdbc:postgresql://java.is.uni-due.de/ws1011");
		map.put("openjpa.ConnectionDriverName", "org.postgresql.Driver");
		map.put("openjpa.ConnectionUserName", "ws1011");
		map.put("openjpa.ConnectionPassword", "ftpw10");
		map.put("openjpa.jdbc.SynchronizeMappings", "false");
		map.put("openjpa.RuntimeUnenhancedClasses", "supported");

		List<Class<?>> types = new ArrayList<Class<?>>();
		types.add(model.Product.class);
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
