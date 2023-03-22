package lambdaDemo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import webscraper.WebScraper;

//https://www.youtube.com/watch?v=VeV_sup5S8E

public class LaunchBrowser {
	public static void main(String[] args) 
	{
		try 
		{
            // Establish a connection to the database
            Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/inventory", "root", "password");

            // Create a new instance of the WebScraper class and call the scrape method
            WebScraper scraper = new WebScraper(connection);
            
            scraper.scrape("https://www.scansource.com/shop#search-product_discounttypes=Clearance%2CRebox%2CB-Stock");
         
            
            String baseURL = "https://www.scansource.com/shop#search-product_discounttypes=Clearance%2CRebox%2CB-Stock&search-product_e=";
            int increment = 20;
            for (int i = 20; i <= 40; i += increment) 
            {
            	System.out.println(i);
            	String url = baseURL + i;
            	scraper.scrape(url);
            	
            	/*
            	 The code adds a delay of 5 seconds between each page request by calling TimeUnit.SECONDS.sleep(5) 
            	 inside the for loop that iterates through the different pages. 
            	 This should help prevent overwhelming the server with too many requests at once.
            	 */
            	try 
            	{
					TimeUnit.SECONDS.sleep(5);
				} catch (InterruptedException e) 
            	{
					e.printStackTrace();
				}
            }
            
            //scraper.scrape("https://www.scansource.com/shop#search-product_discounttypes=Clearance%2CRebox%2CB-Stock&search-product_e=40");
            
            //scraper.scrape("https://www.scansource.com/shop#search-product_discounttypes=Clearance%2CRebox%2CB-Stock");

            // Call the scrape method again with a different URL
            //scraper.scrape("https://www.example.com");

            // Close the database connection
            scraper.close();
        } 
		catch (SQLException e) 
		{
            e.printStackTrace();
        }
    }
}