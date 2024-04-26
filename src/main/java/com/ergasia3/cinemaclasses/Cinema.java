package com.ergasia3.cinemaclasses;

public class Cinema {
	private int ID;
	private boolean Is3D;
	private int NumberOfSeats;

	//constructor
	public Cinema(int Id, boolean Is3D, int NumberOfSeats) {
		this.ID = Id;
		this.Is3D = Is3D;
		this.NumberOfSeats = NumberOfSeats;
	}

	//getters/setters
	public int getID() { return ID; }

	public void setID(int ID) { this.ID = ID; }

	public boolean is3D() { return Is3D; }

	public void setCinema3D(boolean Is3D) { this.Is3D = Is3D; }

	public int getNumberOfSeats() { return NumberOfSeats; }

	public void setNumberOfSeats(int NumberOfSeats) { this.NumberOfSeats = NumberOfSeats; }

	@Override
	public String toString()
	{
		return String.format("Cinema{ ID: %s, Is 3D: %b, Number of seats: %d }",
				ID, Is3D, NumberOfSeats);
	}
}