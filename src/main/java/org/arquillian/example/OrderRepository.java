package org.arquillian.example;

import java.util.List;

import javax.ejb.Local;

@Local
public interface OrderRepository {
	public void addOrder(List<String> order);
	public List<List<String>> getOrders();
	public int getOrderCount();
}
