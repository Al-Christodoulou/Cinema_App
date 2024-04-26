package com.ergasia3.utils;

import com.ergasia3.cinemaclasses.Cinema;
import com.ergasia3.cinemaclasses.Film;
import com.ergasia3.cinemaclasses.Presentation;

import java.security.InvalidParameterException;
import java.sql.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public final class DbUtil {
    private static Connection connection;
    private static final SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy HH:mm:ss");

    public static Connection getConnection() {
        if (connection == null) {
            try {
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/cinema", "root", "qwerty");
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return connection;
    }

    public static Cinema getCinema(int ID) {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement pstatement = connection.prepareStatement("SELECT * FROM cinemas WHERE ID=?");
                pstatement.setInt(1, ID);

                ResultSet rs = pstatement.executeQuery();
                if (rs.next()) {
                    return new Cinema(
                            rs.getInt("ID"),
                            rs.getInt("is_3d") != 0,
                            rs.getInt("seats")
                    );
                }
                else
                    return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            return null;
    }

    public static ArrayList<Cinema> getCinemaRooms() {
        ArrayList<Cinema> cinemaRooms = new ArrayList<>();
        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement pstatement = connection.prepareStatement("SELECT * FROM cinemas");
                ResultSet rs = pstatement.executeQuery();
                while (rs.next()) {
                    cinemaRooms.add(new Cinema(
                            rs.getInt("ID"),
                            rs.getInt("is_3d") != 0,
                            rs.getInt("seats")
                    ));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return cinemaRooms;
    }

    // if the key is an integer type, search by the ID, otherwise by the title
    public static Film getFilm(Object key) throws ClassCastException {
        if (!(key instanceof Integer || key instanceof String))
            throw new ClassCastException("key is neither an integer nor a string!");

        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement pstatement = key instanceof Integer
                        ? connection.prepareStatement("SELECT * FROM movies WHERE ID=?")
                        : connection.prepareStatement("SELECT * FROM movies WHERE title=?");

                if (key instanceof Integer) {
                    pstatement.setInt(1, (Integer) key);
                }
                else {
                    pstatement.setString(1, (String) key);
                }

                ResultSet rs = pstatement.executeQuery();
                if (rs.next()) {
                    return new Film(
                            rs.getInt("ID"),
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getString("description")
                    );
                }
                else
                    return null;
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        else
            return null;
    }

    public static void addFilm(String title, String category, String description) {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement pstatement = connection.prepareStatement("INSERT INTO movies VALUES(default, ?, ?, ?)");
                pstatement.setString(1, title);
                pstatement.setString(2, category);
                pstatement.setString(3, description);

                pstatement.executeUpdate();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static ArrayList<Film> getAvailableFilms() {
        ArrayList<Film> films = new ArrayList<>();
        Connection connection = getConnection();
        if (connection != null) {
            try {
                PreparedStatement pstatement = connection.prepareStatement("SELECT * FROM movies");
                ResultSet rs = pstatement.executeQuery();
                while (rs.next()) {
                    films.add(new Film(
                            rs.getInt("ID"),
                            rs.getString("title"),
                            rs.getString("category"),
                            rs.getString("description")
                    ));
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return films;
    }

    public static ArrayList<Presentation> getPresentations() {
        ArrayList<Presentation> presentations = new ArrayList<>();
        Connection connection = getConnection();
        if (connection != null) {
            try {
                Statement statement = connection.createStatement();
                ResultSet presentations_rs = statement.executeQuery("SELECT * FROM presentations");

                //final SimpleDateFormat dateFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzzz yyyy");
                while (presentations_rs.next()) {
                    presentations.add(new Presentation(
                            presentations_rs.getInt("ID"),
                            getFilm(presentations_rs.getInt("movies_ID")),
                            getCinema(presentations_rs.getInt("cinemas_ID")),
                            dateFormat.parse(presentations_rs.getString("start_date")),
                            presentations_rs.getInt("total_seats"),
                            presentations_rs.getInt("is_available") != 0
                    ));
                }
            }
            catch (SQLException | ParseException e) {
                throw new RuntimeException(e);
            }
        }
        return presentations;
    }

    public static void addPresentation(String filmTitle, int cinemaID, java.util.Date startDate,
                                  boolean is_available)
            throws InvalidParameterException {
        Connection connection = getConnection();
        if (connection != null) {
            throwIfFindPresentationCollision(startDate, getCinema(cinemaID), null);
            Film selectedFilm = getFilm(filmTitle);
            if (selectedFilm == null)
                throw new InvalidParameterException("Couldn't select film with title " + filmTitle + "!");

            try {
                PreparedStatement pstatement = connection.prepareStatement(
                        "INSERT INTO presentations VALUES (default, ?, ?, ?, ?, ?, ?, 0)"
                );
                pstatement.setInt(1, selectedFilm.getId());
                pstatement.setString(2, filmTitle);
                pstatement.setInt(3, cinemaID);
                //pstatement.setString(4, startDate.toString());
                pstatement.setString(4, dateFormat.format(startDate));
                pstatement.setInt(5, 0); // number of reservations
                pstatement.setInt(6, is_available ? 1 : 0);
                pstatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void editPresentation(Presentation candidatePresentation, String editedFilmTitle, int editedCinemaID,
                                   boolean editedIsAvailable, java.util.Date editedStartDate)
            throws InvalidParameterException {
        Connection connection = getConnection();
        if (connection != null) {
            try {
                final Film film = getFilm(editedFilmTitle);
                final Cinema cinema = getCinema(editedCinemaID);
                if (film == null)
                    throw new InvalidParameterException("There's no film with title " + editedFilmTitle + "!");
                if (cinema == null)
                    throw new InvalidParameterException("There's no cinema room with ID " + editedCinemaID + "!");

                throwIfFindPresentationCollision(editedStartDate, cinema, candidatePresentation.getID());

                // first, delete all the reservations for this presentation
                final PreparedStatement deleteReservations_pstatement = connection.prepareStatement(
                        "DELETE FROM reservations WHERE presentations_movies_ID=? AND presentations_movies_title=? AND " +
                                "presentations_cinemas_ID=?"
                );
                deleteReservations_pstatement.setInt(1, candidatePresentation.getFilm().getId());
                deleteReservations_pstatement.setString(2, candidatePresentation.getFilm().getTitle());
                deleteReservations_pstatement.setInt(3, candidatePresentation.getCinema().getID());

                // and then modify it
                final PreparedStatement updatePresentations_pstatement = connection.prepareStatement(
                        "UPDATE presentations SET movies_ID=?, movies_title=?, cinemas_ID=?, start_date=?," +
                                "number_of_reservations=0, is_available=?, total_seats=0 WHERE ID=?"
                );

                updatePresentations_pstatement.setInt(1, film.getId());
                updatePresentations_pstatement.setString(2, editedFilmTitle);
                updatePresentations_pstatement.setInt(3, editedCinemaID);
                //updatePresentations_pstatement.setString(4, editedStartDate.toString());
                updatePresentations_pstatement.setString(4, dateFormat.format(editedStartDate));
                updatePresentations_pstatement.setInt(5, editedIsAvailable ? 1 : 0);

                updatePresentations_pstatement.setInt(6, candidatePresentation.getID());

                deleteReservations_pstatement.executeUpdate();
                updatePresentations_pstatement.executeUpdate();
            }
            catch (SQLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private static void throwIfFindPresentationCollision(final Date scheduledTime, final Cinema selectedCinema,
                                                    Integer optPresentationIDIgnore)	throws InvalidParameterException {
        Presentation possibleCollidedPresentation = findPresentationCollision(scheduledTime, selectedCinema, optPresentationIDIgnore);
        if (possibleCollidedPresentation != null)
            throw new InvalidParameterException("Error: The specified time & date collides with " +
                    possibleCollidedPresentation + "!");
    }

    // checks if a presentation can be scheduled by comparing the date & time of other presentations. if there's a
    // collision, return the collided presentation
    private static Presentation findPresentationCollision(final Date scheduledTime, final Cinema selectedCinema,
                                                Integer optPresentationIDIgnore) {
        final long oneHundredMinutes = 1000 * 60 * 100;

        ArrayList<Presentation> presentations = DbUtil.getPresentations();
        if (!presentations.isEmpty()) {
            for (Presentation presentation : presentations) {
                // if: 1) this is the same cinema room of this other presentation,
                // 2) the time difference is less than 100 minutes
                // 3) we're not comparing against the same presentation (only check this if optPresentationIDIgnore != null)
                // then this presentation can't be made
                if (selectedCinema.getID() == presentation.getCinema().getID() &&
                        Math.abs(scheduledTime.getTime() - presentation.getStartDate().getTime()) < oneHundredMinutes &&
                        (optPresentationIDIgnore == null || !(presentation.getID() == optPresentationIDIgnore))
                ) {
                    return presentation;
                }
            }
        }
        return null;
    }
}

