//JOHN ORTEGA, EMPIL ID: 24425175
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Arrays; 

class InvalidRatingException extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidRatingException(String message) { super(message); }
}
class InvalidHeadersException extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidHeadersException(String message) { super(message); }
}
class InvalidYearException extends Exception {
	private static final long serialVersionUID = 1L;
	public InvalidYearException(String message) { super(message); }
}
class NoValidRecordsException extends Exception {
	private static final long serialVersionUID = 1L;
	public NoValidRecordsException(String message) { super(message); }
}

class Movie implements Comparable<Movie> {
	private String title;
	private int year;
	private String genre;
	private int rating1;
	private int rating2;
	private int rating3;
	
	public Movie(String title, int year, String genre, int rating1, int rating2, int rating3) {
		this.title = title;
		this.year = year;
		this.genre = genre;
		this.rating1 = rating1;
		this.rating2 = rating2;
		this.rating3 = rating3;
	}
	
	public double getAverageRating() { return (rating1 + rating2 + rating3) / 3.0; }
	public String getTitle() { return title; }
	public int getYear() { return year; }
    public String getGenre() { return genre; }
    
    @Override
    public int compareTo(Movie other) {
        double x = other.getAverageRating() - this.getAverageRating();
        if (x > 0) return 1;
        if (x < 0) return -1;
        return 0;
    }
    
}

public class homework4{
	public static void main(String[] args) {
		String filePath = "movies.csv";
        String line;
        int numMovies = 0; //Will be used to determine total valid records
        
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        	String headers = br.readLine();
        	if(headersValidation(headers) == false) //Program halts if headers aren't correct
        		throw new InvalidHeadersException("Invalid format for headers!");
            while ((line = br.readLine()) != null) {
            	try {
	            	String[] values = line.split(",");
	            	String title = values[0];
	            	int year = Integer.parseInt(values[1].trim());
	            	String genre = values[2];
	            	
	            	//Ratings will be in a array to easily loop and check validity 
	            	int[] ratings = { Integer.parseInt(values[3].trim() ), Integer.parseInt(values[4].trim()), Integer.parseInt(values[5].trim()) };
	                
	            	if (!(year >= 1900 && year <= 2100)) { //Record ignored if year isn't within [1900, 2100]
	                	throw new InvalidYearException("Year (" + year + ") is not withing proper range");
	                }
	                for(int i = 0; i < 3; i++) {
	                	if(!(ratings[i] >= 1 && ratings[i] <= 100)) { //Record ignored if rating isn't within [1, 100]
	                		throw new InvalidRatingException("Rating (" + ratings[i] + ") is not within proper range");
	                	}
	                }
	               
	                numMovies++;
	                
            	}
            	catch(NumberFormatException e) { 
            		if(e.getMessage().equals("For input string: \"\""))
            			System.err.println("Missing a field, record invalid!");
            		else
            			System.err.println("Cannot convert string to numeric type " + e.getMessage());
            	}
            	catch (ArrayIndexOutOfBoundsException e) { //Will execute when a record isn't entered
            		System.err.println("Missing a field, record invalid!");
                }
            	catch(InvalidYearException e) { //Catching and handling the exception thrown when year is invalid
            		System.err.println("Invalid Year: " + e.getMessage());
            	}
            	catch(InvalidRatingException e) { //Catching and handling the exception thrown when a rating is invalid
            		System.err.println("Invalid Record: " + e.getMessage());
            	}
         
            	
            }
            if(numMovies == 0) throw new NoValidRecordsException("No valid records, exiting program");
        } 
        catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        }
        catch (InvalidHeadersException e) {
        	System.err.println(e.getMessage());
        }
        catch(NoValidRecordsException e) {
        	System.err.println(e.getMessage());
        }
        
        //Sizing array to a size of the amount of valid records, and inserting
        Movie[] movies = new Movie[numMovies];
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
        	br.readLine();
        	
        	int index_movies = 0; //Used to properly store movie objects into movies array
            while ((line = br.readLine()) != null) {
            	try {
	            	String[] values = line.split(",");
	            	String title = values[0];
	            	int year = Integer.parseInt(values[1].trim());
	            	String genre = values[2];
	            	int[] ratings = { Integer.parseInt(values[3].trim() ), Integer.parseInt(values[4].trim()), Integer.parseInt(values[5].trim()) };
	                
	            	if (!(year >= 1900 && year <= 2100)) continue;
	                for(int i = 0; i < 3; i++) {
	                	if(!(ratings[i] >= 1 && ratings[i] <= 100)) continue;
	                }
	                //Initializing array at index index_movies, with a new movie object
	                movies[index_movies] = new Movie(title, year, genre, ratings[0], ratings[1], ratings[2]);
	                index_movies++;
	                
            	}
            	catch(NumberFormatException | ArrayIndexOutOfBoundsException e) {}
            }
            printMovieInfo(movies); //Printing out the information about all the valid records
        } 
        catch (IOException e) {}
	}
	
	//Checks to see if the headers match Title, Year, Genre, Rating1, Rating2, and Rating3 (case insensitive)
	private static boolean headersValidation(String headers) {
        String[] requiredHeaders = {"Title", "Year", "Genre", "Rating1", "Rating2", "Rating3"};
        String[] actualHeaders = headers.split(",");
        if (actualHeaders.length != 6) return false; 
        for (int i = 0; i < 6; i++) {
        	String str1 = actualHeaders[i].trim().toUpperCase();
        	String str2 = requiredHeaders[i].trim().toUpperCase();
            if (!(str1.equals(str2))) { return false; }
        }
        return true;
    }
	
	private static void printMovieInfo(Movie[] movies) {
		int maxTitleLength = 0; //Will have the length of the title with most characters + 1
		int maxGenreLength = 0; //Will have the length of the genre with most characters + 1
		
		for (int i = 0; i < movies.length; i++) {
			if(movies[i].getTitle().length() > maxTitleLength) {
				maxTitleLength = movies[i].getTitle().length();
			}
		}
		for (int i = 0; i < movies.length; i++) {
			if(movies[i].getGenre().length() > maxGenreLength) {
				maxGenreLength = movies[i].getGenre().length();
			}
		}
		maxTitleLength++;
		maxGenreLength++;
		
		Arrays.sort(movies);
		
		System.out.println("Title" + " ".repeat(maxTitleLength - 5) + "| Year | Genre" + " ".repeat(maxGenreLength - 5) + "| Avg Rating");
		System.out.println("-".repeat(60));
		for (int i = 0; i < movies.length; i++) {
			//Calculating how many spaces needed before adding the '|' separator 
			int apdTitleSpaces = maxTitleLength - movies[i].getTitle().length();
			int apdGenreSpaces = maxGenreLength - movies[i].getGenre().length();
			System.out.print(movies[i].getTitle() + " ".repeat(apdTitleSpaces) + "| " + movies[i].getYear() + " | " + movies[i].getGenre() + " ".repeat(apdGenreSpaces) + "| ");
			System.out.printf("%.1f\n", movies[i].getAverageRating());
		}
		
	}
}