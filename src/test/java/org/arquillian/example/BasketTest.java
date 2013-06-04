package org.arquillian.example;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.ejb.EJB;
import javax.inject.Inject;

import org.jboss.arquillian.container.test.api.Deployment;
import org.jboss.arquillian.junit.Arquillian;
import org.jboss.arquillian.junit.InSequence;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.asset.EmptyAsset;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(Arquillian.class)
public class BasketTest {
	@Deployment
	public static JavaArchive createDeployment() {
		return ShrinkWrap.create(JavaArchive.class, "test.jar")
			.addClasses(Basket.class, OrderRepository.class, SingletonOrderRepository.class)
			.addAsManifestResource(EmptyAsset.INSTANCE, "beans.xml");
	}
	
	@Inject
	Basket basket;
	
	@EJB
	OrderRepository orderRepository;
	
	@Test
	@InSequence(1)
	public void test_place_order_should_add_order() throws Exception {
		basket.addItem("sunglasses");
		basket.addItem("suit");
		basket.placeOrder();
		
		assertThat(orderRepository.getOrderCount(), is(1));
		assertThat(basket.getItemCount(), is(0));
		
		basket.addItem("raygun");
		basket.addItem("spaceship");
		basket.placeOrder();
		
		assertThat(orderRepository.getOrderCount(), is(2));
		assertThat(basket.getItemCount(), is(0));
	}
	
	@Test
	@InSequence(2)
	public void order_should_be_persistent() throws Exception {
		assertThat(orderRepository.getOrderCount(), is(2));
	}
}
