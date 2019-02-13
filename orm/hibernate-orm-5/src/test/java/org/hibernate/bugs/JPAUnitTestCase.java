package org.hibernate.bugs;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase {

	private EntityManagerFactory entityManagerFactory;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
	}

	@After
	public void destroy() {
		entityManagerFactory.close();
	}


	@Test
	public void test() {
		persist();
		fetch();
	}

	public void persist() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		ChildClass childClass = new ChildClass("foo");

		ContainerClass cc = new ContainerClass();
		cc.setParentClass(childClass);

		entityManager.getTransaction().begin();
		entityManager.persist(cc);
		entityManager.getTransaction().commit();

		entityManager.close();
	}

	public void fetch() {
		EntityManager entityManager = entityManagerFactory.createEntityManager();

		ContainerClass cc = entityManager.createQuery("FROM ContainerClass", ContainerClass.class).getResultList().get(0);

		ParentClass parentClass = cc.getParentClass();

		System.out.println("parentClass reports own class name as: " + parentClass.getClassName());

		if (parentClass instanceof ChildClass) {
			System.out.println("parentClass is instanceof ChildClass");
			assertEquals("ChildClass", parentClass.getClassName());
		} else {
			assertNotEquals("ChildClass", parentClass.getClassName());
			System.out.println("parentClass is NOT instanceof ChildClass");
		}

		entityManager.close();
	}
}
