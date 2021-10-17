# GroceryMarket
Percent Java Coding Test

#Build
* Go to project directory where pom is located and execute the following command. Or use IDE to build the project.

> `mvn clean install`

# Rest API call example

> _This API call will load the list of products in Pos Terminal._ 
> _Assumption : Created static list of products on java side. This API must be called before scanning products_

* http://localhost:8080/grocerymarket/pos-terminal/products

> _This API call scan the product and calculate ongoing total._ 
> _Assumption : Return product not found message if product is not available. Return Pos Terminal error if products are not loaded_

* http://localhost:8080/grocerymarket/pos-terminal/scan/{productCode}

> _This API call will return the total for all scanned products._ 
> _Assumption : Return 0 as total in case of error_

* http://localhost:8080/grocerymarket/pos-terminal/total

# Assumptions

* Products must be loaded before scanning.
* Calculating Total will reset total for next session to 0.
* Product list can be passed as Json Input. I chose to load it from java code.