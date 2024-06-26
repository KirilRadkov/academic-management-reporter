
### Summary

This is an application for storing Students and Teachers information like name, age, group and
courses. The application is able to add remove or modify students and teachers. 
This application can generate JSON and CSV reports for:
* Student count
* Teacher count
* Course count
* List of Students which participate in specific course
* List of students which participate in a specific group
* List of teachers and students which are in specific course and group
* List of students which are older than specific age and participate in specific course


### How to build and run the service
To build this Maven project via the command line, you need to use the **mvn** command.The command must be executed in the directory which contains the relevant pom file

```
mvn clean install 
```

### How to test the service 

1. Start the application vie command line

```
mvn spring-boot:run
```
2. Open your browser and go to http://localhost:8080/swagger-ui/index.html
 * to Add, Remove or Modify Students use: http://localhost:8080/swagger-ui/index.html#/student-controller
 * to Add, Remove or Modify Teachers use: http://localhost:8080/swagger-ui/index.html#/teacher-controller
 * to generate Reports use: http://localhost:8080/swagger-ui/index.html#/reporting-controller
   * to generate CSV Report under /reports folder use the boolean flag "generateCSVReport". This flag is not availiable for the "count"-endpoints
   


