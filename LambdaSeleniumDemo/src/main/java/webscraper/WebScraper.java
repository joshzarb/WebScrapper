package webscraper;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Statement;


import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.jsoup.safety.Safelist;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.support.ui.ExpectedCondition;
import org.openqa.selenium.support.ui.WebDriverWait;

import org.openqa.selenium.JavascriptExecutor;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.function.Function;



public class WebScraper {
	private Connection conn;
	private Statement stmt;
	
	public WebScraper(Connection connection) throws SQLException 
	{
        // Initialize the database connection and statement
        conn = connection;
        stmt=conn.createStatement();
        
     // Delete the products table if it already exists
        stmt.execute("DROP TABLE IF EXISTS products");
        
     // Create the products table if it does not exist
        stmt.execute("CREATE TABLE IF NOT EXISTS products ("
                    + "id INT PRIMARY KEY AUTO_INCREMENT,"
                    + "name VARCHAR(255),"
                    + "manufacturer VARCHAR(255),"
                    + "msrp DECIMAL(10,2) NOT NULL DEFAULT 0.0"
                    + ")");
    }
	
    public void scrape(String url) throws SQLException {
    	
        
        // Set the path to the Chrome driver executable
        System.setProperty("webdriver.chrome.driver", "./drivers/chromedriver.exe");

        // Set the Chrome options to run in headless mode
        ChromeOptions options = new ChromeOptions();
        options.addArguments("--headless");

        // Create a new ChromeDriver with the headless options
        WebDriver driver = new ChromeDriver(options);

        try {
            // Navigate to the website and wait for the JavaScript to finish executing
        	driver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
        	driver.get(url);
            


            // Get the page content and clean it using the Safelist
            Document document = Jsoup.parse(driver.getPageSource());
            Safelist safelist = Safelist.basic();
            safelist.removeTags("style", "script");
            String cleanHtml = Jsoup.clean(document.html(), safelist);

            // Select the elements you want to scrape
            Elements elements = document.select("ul.search-result-list > li");
            //System.out.println(elements);
            //Loop through the elements and print their text content
            for (Element element : elements) 
            {
                //System.out.println(element.text());
            	
            	// Parse the fields from the product information text
                String[] fields = element.text().split("\\s+");
                //System.out.println(Arrays.toString(fields));
                
            	// Extract the relevant information from the fields
                String productName = "";
                String manufacturer="";
                BigDecimal msrp = null;
                for (int i = 0; i < fields.length; i++) 
                {
                	if (fields[i].contains("Mfr#:")) 
        			{
        				manufacturer = fields[i].substring(5);
        				productName=fields[i-1];
        			}
                	else if (fields[i].equals("MSRP:")) 
        			{
            			// Get the next element in the array, which should be the price
            			msrp = new BigDecimal(fields[i+1].substring(1));
            			//System.out.println("MSRP Price: " + msrpPrice);
            			//break; // Exit the loop once you find the MSRP price
        			}
    			}
                //BigDecimal msrp = new BigDecimal(fields[5].substring(1));
                //System.out.println(productName+" "+manufacturer+" "+msrp);
             // Insert the product into the database
                insertProduct(productName, manufacturer, msrp);
            }
        } 
        finally 
        {
            // Close the browser window and terminate the ChromeDriver process
            driver.quit();
        }
    }
    public void insertProduct(String productName, String manufacturer, BigDecimal msrp) throws SQLException {
        // Prepare the SQL statement to insert a new product into the database
        String sql = "INSERT INTO products (name, manufacturer, msrp) VALUES (?, ?, ?)";
        PreparedStatement stmt = conn.prepareStatement(sql);

        // Set the values for the parameters in the SQL statement
        stmt.setString(1, productName);
        stmt.setString(2, manufacturer);
        stmt.setBigDecimal(3, msrp);

        // Execute the SQL statement to insert the product into the database
        int rowsAffected = stmt.executeUpdate();

        // Close the statement object
        stmt.close();

        // Print the number of rows affected (should be 1 if the product was successfully inserted)
        //System.out.println(rowsAffected + " row(s) affected");
    }
    
    public void close() throws SQLException {
        // Close the statement and connection objects
        stmt.close();
        conn.close();
    }
    
}
