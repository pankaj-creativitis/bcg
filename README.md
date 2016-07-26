## Business-Code-Generator
This project aims to achieve opinionated automated handling of impedance mismatch between database model and views or UI data models. It takes database pojos and UI model json views as an input and generates the intermediate translation code (DTO, Dao & Service classes).

## What's different about Business-Code-Generator?
1. At present generated code from other tools is tightly coupled to the database model, which is hardly the case in real world. In real-world projects we need a mix of different database models to be shown in any view. This tool primarily attempts to solve that problem.
2. Fetches only the required data(DTO fields) from the back-end in dao layer itself, thus reducing memory footprint. These DTOs can be directly sent to the views. Also generates the minimal code required.
3. Simplicity of use: Make your pojos from your favorite tool, upload the pojos in the form of a zip file. Plan your views and make the json or xml versions of view objects or DTOs. Similarly upload the zip of the json files. That's all is needed by our tool to make the remaining intermediate code.
4. These dao methods from dao can be easily converted into search filter methods by automatic code tool.
5. Case of an unknown field (say a calculated field or a formula field): In this case a set of TODOs with relavant message can be inserted in the code. So that it is easier for the developer to fill in the gaps.
6. Spring or Hibernate framework independent code can be generated.

## How to run
1. Compile the project; Open command prompt; Navigate to your project location.
2. Run the command "mvn clean install" and then run "mvn tomcat7:run". 
3. Make your pojos from your favorite tool & then make a zip of those pojos.
	
	Sample POJO file:
	
	```... <imports and package skipped>
	public class Star {
	
	private String starId;
	private String starName;
	private String starType;
	private Integer starSize;
	private List<Planet> starPlanets;
	... <getters & setters skipped>```
	
4. Plan your UI views and make corresponding JSON files; Make a zip of same json files.
	Make sure that the json field name in any of the json file is same as pojo field name in uploaded pojo files. For example, notice in the below file "starName" field in JSON corresponds to "star.starName" (<class name>.<field name>) format in the POJO. Always keep the first character lowercase and follow camelCase notation.
	
	Sample JSON file:
	
	```{
		"planetId":"planet.planetId",
		"planetName":"planet.planetName",
		"planetHabitable":"planet.planetHabitable",
		"planetSize":"planet.planetSize",
		"starName":"star.starName",
		"starType":"star.starType"
	}```

	In case there is a JSON which has a join field from multiple disconnected/dis-associated/unrelated tables, please follow below format. For example, notice that "planetName" is a join field and corresponds to format["planet.planetName","asteroid.asteroidNearBodyName"] that is <class name-1>.<field name> , <class name-2>.<field name>, ... <class name-n>.<field name>:
	
	Sample JSON file:
	
	```{
		"asteroidId":"asteroid.asteroidId",
		"planetName":["planet.planetName","asteroid.asteroidNearBodyName"],
		"asteroidName":"asteroid.asteroidName"
	}```

5. Make a config file (txt file format) with following details:

	
	```pojozip = <path to your pojo zip>
	jsonzip = <path to your json zip>
	projectroot = <path to your project root>
	basepackage = <base package in your project>```

	Sample config file:
	
	```pojozip = /home/ngadmin/Desktop/domain_pojos.zip
	jsonzip = /home/ngadmin/Desktop/json_views.zip
	projectroot = /home/ngadmin/eclipseworkspace/ClientProject
	basepackage = com.client.project```

	The project expects above 4 parameters (pojozip, jsonzip, projectroot, basepackage) from the config file.

6. Make POST call on URI: http://localhost:8080/bcg/webapi/generateCode/uploadConfigFile/<full path to your config.txt file>
	Sample call
	http://localhost:8080/bcg/webapi/generateCode/uploadConfigFile//home/ngadmin/Desktop/config.txt

	After Making this call all the required files (pojos, dtos, daos and service) along with proper packages will be generated in the client project location. At-present the project only generates Spring and Hibernate compatible code. So please make sure the your blank project is a spring-hibernate maven project.

# Note: 
At-present the project only generates Spring and Hibernate compatible code. So please make sure the your blank project is a spring-hibernate maven project.
