## Quoqo Health Insurance system (SpringBoot REST)

### Architecture:

A distributed health insurance quotation system with a client entry-point, 3 health insurance provider services, and a broker service to facilitate communication between the 4.
The REST architectural style was used across the system, facilitating distributed and asynchonous communication between services.

The SpringBoot framework was used, specifically the starter web module which provides Spring MCV, REST functionality, and a Tomcat web server.
The aim of this project was to showcase the use SpringBoot for creating distributed RESTful services. _Please also see my similar JMS repo which showcases the use of Java Message Service for creating distributed services._

The system contains a client service that provides relevant client information (name, age, weight, health-issues). This client makes requests to the Broker service which manages 2 REST controllers (BrokerController and RegistrationController). BrokerController listens to 3 endpoints (2 GET and 1 POST) that handle insurance quote requests from 
the client, and resulting responses from the loan provider service(s). Similarly, RegistrationController listens to 2 GET and 1 POST endpoints, however these are 
responsible for the registration and listing of the loan provider service(s).

There are 3 loan provider services (auldfellas, dodgygeezers, girlsallowed) however this can be reduced or increased, and the system is tolerant of individual or multiple
failures.

Each service in the system has been dockerised and a compose file has been created to enable straightforward initiation of the overall system.

---


**To Run:**

Ensure docker is installed on your system and running.
Clone the project and open a terminal in the root directory.
Run the command 'docker compose up'

Note:
The system needs a 5 seconds wait before calling client.
This ensures that the broker is up and running and the insurance services have completed their registration.

---

#### User Interface:

This system is CLI based and does not take user input. Qoute requests are made automatically to showcase the system at work. Each request has the following format:

{
"name" : "John Doe",
"gender" : "M",
"age": 49,
"height": 1.549,
"weight": 80,
"smoker" : false,
"medicalIssues": false
}

A series of simimilar objects are sent by the client. And a text based reponse is presented to the user on the command-line.
In future a GUI-based solution could be introduced.




