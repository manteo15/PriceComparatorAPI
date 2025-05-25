PROJECT STRUCTURE

The project is split in 5 modules:
-Model: it contains the entities
-Repository: it contains the repositories for entities created
-Service: it contains the store service
-API: it contains the main and the controllers
-Utils: it contains the CSV's, models used to receive and return from endpoints and exceptions

The application was built using Maven, Spring Boot and H2 database.

The application is built and run by running the main method from the API module.

API endpoints:

- http://localhost:8080/api/stores/uploadAll - this endpoint uploads the CSVs, it parses through it and then saves the data in db
- http://localhost:8080/api/stores/bestDiscounts - for this endpoint, a model object with a String parameter currentDate is needed; this returns the best 3 current discounts from every store based on the currentDate parameter
- http://localhost:8080/api/stores/newDiscounts - for this endpoint, a model object with a String parameter currentDate is needed; this returns the discounts that are newly added( fromDate == currentDate)
- http://localhost:8080/api/stores/shoppingBasket - for this endpoint, a model object with a String parameter currentDate and a list of String called productList; the productList is a list of product ids;
                                                   this returns a map with the key being the name of the store and the value a list of products;
-http://localhost:8080/api/stores/priceHistory - for this endpoint, a model object with the following parameters: String currentDate, String brand, String category, int storeId
                                                brand and category parameters can be empty, meaning that there will be no filtration based on them; the storeId can have a existing value or it can be 0, meaning it will return from all the stores
                                                it will return a list of priceTrendModel(float price, String currency, String storeName, int discountPercentage, Date fromDate, Date toDate) based on the currentDate
-http://localhost:8080/api/stores/priceAlert - for this endpoint, a model object with the following parameters: String currentDate, String productId, float targetPrice
                                                it will return a list of priceTrendModel(float price, String currency, String storeName, int discountPercentage, Date fromDate, Date toDate) based on the currentDate

  

