package io.intracom.util;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategy;

public class MatchCustomer {
	private static final String INPUT = "E:\\gistfile1.txt";
	private static final String CHARSET = "UTF-8";
	private static final double DUBLIN_LAT = 53.3381985;
	private static final double DUBLIN_LNG = -6.2592576;

	private final static Logger LOGGER = Logger.getLogger(MatchCustomer.class.getName());

	public static void main(String[] args) {
		String path = INPUT;
		if (args.length > 0)
			path = args[0];

		MatchCustomer mc = new MatchCustomer();
		try {
			mc.match(path);
		} catch (IOException e) {
			LOGGER.severe("Wrong input file path!");
		}
	}

	public void match(String inputFile) throws IOException {
		List<Customer> customerList = new ArrayList<Customer>();
		//mapper for reading json
		ObjectMapper mapper = new ObjectMapper();
		mapper.setPropertyNamingStrategy(PropertyNamingStrategy.SNAKE_CASE);

		if (inputFile == null)
			throw new IOException();
		// read file
		Path path = FileSystems.getDefault().getPath(inputFile);
		Charset charset = Charset.forName(CHARSET);

		try (BufferedReader reader = Files.newBufferedReader(path, charset)) {
			String line = null;
			while ((line = reader.readLine()) != null) {
				try {
					// parse and add only valid Customers to list
					customerList.add(parseLine(line, mapper));
				} catch (IllegalArgumentException e) {
					LOGGER.warning("wrong input line: " + line);
				}
			}

			List<Customer> output = filterByDistance(customerList);
			//sort by id ascending
			Collections.sort(output);
			//print eligible customers
			for (Customer c : output) {
				System.out.println(c.getUserId() + "|" + c.getName());

			}
		}
	}

	public List<Customer> filterByDistance(List<Customer> input) {
		List<Customer> eligible = new ArrayList<Customer>();
		for (Customer c : input) {
			double dist = CalculateDistance.calculateByLatLong(DUBLIN_LAT, DUBLIN_LNG, c.getLatitude(),
					c.getLongitude());

			if (dist <= 100)
				eligible.add(c);
		}
		return eligible;
	}

	public Customer parseLine(String line, ObjectMapper mapper) throws IllegalArgumentException, IOException {
		try {
			Customer c = mapper.readValue(line, Customer.class);
			//validate Customer
			if (validateCustomer(c))
				return c;
			else {
				throw new IllegalArgumentException();
			}
			//in case of wrong format in line
		} catch (JsonMappingException e) {
			throw new IllegalArgumentException();
		}

	}

	public boolean validateCustomer(Customer c) {

		if (c.getUserId() > 0 && c.getName() != null && c.getLatitude() != 0.0 && c.getLongitude() != 0.0)
			return true;
		else 
			return false;
		

	}

}
