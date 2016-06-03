package io.intracom.util;

public class Customer implements Comparable<Customer> {

	private int userId;
	private String name;
	private double latitude;
	private double longitude;

	@Override
	public int compareTo(Customer other) {
		int returnVal = 0;
		if (this.getUserId() < other.getUserId()) {
			returnVal = -1;
		} else if (this.getUserId() > other.getUserId()) {
			returnVal = 1;
		} else if (this.getUserId() == other.getUserId()) {
			returnVal = 0;
		}
		return returnVal;
	}

	@Override
	public boolean equals(Object other) {

		if (this == other)
			return true;

		if (!(other instanceof Customer))
			return false;

		Customer c = (Customer) other;
		// only basic comparison
		if (this.getUserId() == c.getUserId())
			return true;

		return false;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getLatitude() {
		return latitude;
	}

	public void setLatitude(double latitude) {
		this.latitude = latitude;
	}

	public double getLongitude() {
		return longitude;
	}

	public void setLongitude(double longitude) {
		this.longitude = longitude;
	}
}
