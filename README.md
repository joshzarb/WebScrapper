# WebScrapper

**Overview:** 
This project provides a web scraper that can be used to collect information from a website and store it in a MySQL database. The program uses the ChromeDriver and Jsoup libraries to navigate to a website, extract the relevant data, and store it in a MySQL database using JDBC.

**Dependencies:** This project requires the following dependencies: ChromeDriver, Jsoup, MySQL Connector/J, and Selenium WebDriver.

**Installation:** To use this program, you will need to install the ChromeDriver executable and add it to your system's PATH. You will also need to install the required dependencies using a build tool such as Maven or Gradle.

**Usage:** To use the program, run the LaunchBrowser class. The program will establish a connection to the MySQL database and create a new instance of the WebScraper class. The scrape method is then called to extract the relevant data from the website and store it in the MySQL database. The scrape method can be called multiple times with different URLs to extract data from different pages.

**Configuration:** The program currently connects to a local MySQL database with the default username and password. If you need to connect to a different database, you will need to modify the getConnection method in the WebScraper class.

**License:** This code is provided under the MIT License. You are free to use, modify, and distribute the code as long as you include the original license text in your distribution.
