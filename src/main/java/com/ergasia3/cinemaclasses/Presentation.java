package com.ergasia3.cinemaclasses;

import java.util.Date;

public class Presentation {
	private int ID;
	private Film Film;
	private Cinema Cinema;
	private Date StartDate;
	private int total_seats;
	private boolean IsAvailable;

	//constructor
	public Presentation(int presentationID, Film presentationFilm, Cinema presentationCinema,
				   Date presentationStartDate, int total_seats, boolean presentationIsAvailable) {
		this.ID = presentationID;
		this.Film = presentationFilm;
		this.Cinema = presentationCinema;
		this.StartDate = presentationStartDate;
		this.total_seats = total_seats;
		this.IsAvailable = presentationIsAvailable;
	}

	//getters/setters
	public int getID() { return ID; }

	public void setID(int ID) { this.ID = ID; }

	public Film getFilm() { return Film; }

	public void setFilm(Film film) { this.Film = film; }

	public Cinema getCinema() { return Cinema; }

	public void setCinema(Cinema cinema) { this.Cinema = cinema; }

	public Date getStartDate() { return StartDate; }

	public void setStartDate(Date startDate) { this.StartDate = startDate; }

	public int getTotal_seats() { return total_seats; }

	public boolean isAvailable() { return IsAvailable; }

	public void setAvailable(boolean available) { this.IsAvailable = available; }

	@Override
	public String toString()
	{
		return String.format("Presentation{ ID: %s, Film Title: %s, Cinema ID: %s, Start Date: %s, Total Seats: %s }",
				ID, Film.getTitle(), Cinema.getID(), StartDate, total_seats);
	}
}