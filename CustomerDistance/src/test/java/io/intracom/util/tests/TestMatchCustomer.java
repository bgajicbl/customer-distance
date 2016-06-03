package io.intracom.util.tests;

import java.io.IOException;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

import io.intracom.util.CalculateDistance;
import io.intracom.util.Customer;
import io.intracom.util.MatchCustomer;

public class TestMatchCustomer {
	MatchCustomer mc;
	ObjectMapper om;

	@Before
	public void setUp() {
		mc = new MatchCustomer();
		om = new ObjectMapper();
		om.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);
	}

	@Test(expected = IOException.class)
	public void testNullFile() throws IOException {
		mc.match(null);
	}

	@Test(expected = IOException.class)
	public void testWrongFile() throws IOException {
		mc.match("wrongFile.txt");
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testEmptyLine() throws IllegalArgumentException, IOException {
		String wrongLine = "";
		mc.parseLine(wrongLine, om);
	}

	@Test(expected = IllegalArgumentException.class)
	public void testEmptyJson() throws IllegalArgumentException, IOException {
		String wrongLine = "{}";
		mc.parseLine(wrongLine, om);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void testWrongJson() throws IllegalArgumentException, IOException {
		String wrongLine = "{\"l\": \"53.1489345\"}";
		mc.parseLine(wrongLine, om);
	}
	
	@Test
	public void testOkJson() throws IllegalArgumentException, IOException {
		String goodLine = "{\"latitude\": \"53.1489345\", \"user_id\": 31, \"name\": \"Alan Behan\", \"longitude\": \"-6.8422408\"}";
		Customer c = new Customer();
		c.setUserId(31);
		c.setName("Alan Behan");
		c.setLatitude(53.1489345);
		c.setLongitude(-6.8422408);
		Assert.assertEquals(c, mc.parseLine(goodLine, om));
	}
	
	@Test
	public void testDistanceCalculation() {
		//as from http://www.onlineconversion.com/map_greatcircle_distance.htm
		Assert.assertEquals(44.13286096134961,CalculateDistance.
				calculateByLatLong(53.3381985, -6.2592576, 53.1489345, -6.8422408),0.05);
	}
	


}
