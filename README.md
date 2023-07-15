# ToDo
## Description
ToDo is a web application where a registered user has access to his personal todo-notebook.
### Functionality
* **task creation:** input title and description, add tags, set priority and status
* **tag creation:** input tag's name, choose color
* **interaction with a task:** edit/delete, view more information and leave a comment
### Limitation
* a user has access only to his tasks
* tags and comments cannot be edited or deleted
* user data cannot be changed by a user
### Features
* when creating a task, all data entered is saved until a user clicks "Add new task" or "log out" button (does not work for the task edit page)
* the "back" button link on the tag management page depends on which page the user came from
## Brief description of main packages
* **java/../../**   
  - ```command``` - objects that are used to transfer data necessary for creating/updating in the database   
  - ```config``` - database configuration    
  - ```dao``` - repositories for interacting with databases   
  - ```dto``` - objects which encapsulate entities parameters and used to transfer to the user  
  - ```enums``` - jsp, task priorities and statuses constants      
  - ```exception``` - custom application exceptions  
  - ```filter``` - classes for pre-checking and filtering requests    
  - ```listener``` - context listener, application entry point  
  - ```mapper``` - mapstruct interfaces    
  - ```model```- entities that are stored in the database     
  - ```service``` - connecting chain between dao and servlets    
  - ```servlet``` - connecting chain between UI and business logic   
  - ```utils``` - constants and other auxiliary classes     
* **resources** - log4j configuration   
* **webapp** - jsp, css and javascript files
## Launch
1. download and extract **build/clady_todo.zip** archive that contains all the necessary files to launch
2. run Docker
3. open the command line and go to the "clady_todo" folder
4. enter the command: **docker-compose up --build -d**
5. after launch open the browser and go to the link: **localhost:8888**
## Technologies
* Java 16, Maven, Docker
* Hibernate, PostgreSQL, H2 in-memory DB
* Servlets, log4j, mapstruct, junit, mockito, lombok
* HTML, CSS, JavaScript, jstl, Bootstrap
