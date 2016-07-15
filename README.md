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
1. Run the application on tomcat. 
2. Make a zip of sample pojos in package "BCG5.bcg.business.client.pojos" and upload it using POST call on URI: http://localhost:8080/bcg/webapi/generateCode/uploadPojoZip/path_to_your_pojo_zip_file
3. Check the sample client pojo files in "BCG5.bcg.business.client.pojos" package and make corresponding json text files based on the field names of the pojos. For example: 
    PlanetChartView.json
    {
    "planetId":"planet01",
    "planetName":"earth",
    "planetSize":3,
    "starName":"start.starName"
    }
    OR
    PlanetView.json
    {
    "planetId":"planet01",
    "planetName":"earth",
    "planetHabitable":true,
    "planetSize":3,
    "starName":"sol",
    "starType":"Yellow Dwarf"
    }

4. Make a zip of all such json files created and upload it  POST call on URI: http://localhost:8080/bcg/webapi/generateCode/uploadJsonZip/path_to_your_json_zip_file

After following these steps the code (dtos, daos and services) will be generated in respective client packages in the same project. Future plan is to generate the code files in the client specified project location.

# Note: 
The URI http://localhost:8080/bcg/webapi/generateCode/uploadPojoZip/path_to_your_pojo_zip_file is partially working. First we need to manually insert the required pojos in the package "BCG5.bcg.business.client.pojos" and then make a zip of the same pojos to upload on the above URI. Work in progress to make a single call which uploads pojos in project as well as processes the pojos.

Same thing can be tested, by replacing the existing sample pojos in the project by your created pojos and respective json files zip. The plan is to upload the pojos zip and json zip together in the same call and the code is generated instantaneously. The call for uploading pojos is also present, but not completely functional. So please follow the above mentioned process in note.

