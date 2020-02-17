# Micronaut Mongo App

This is a server management application that can do basic tasks like:

  - Adding a new server
  - Remove a server
  - Search for servers

### Tech

Dillinger uses a number of open source projects to work properly:

* [VueJS](https://vuejs.org/) - HTML enhanced for web apps!
* [Micronaut](https://micronaut.io/) - A modern, JVM-based, full-stack framework
* [Axios](https://github.com/axios/axios) - Promise based HTTP client for the browser and node.js
* [Docker](https://www.docker.com/) - Containerization software
* [Mongodb](https://www.mongodb.com/) - A general purpose, document-based, distributed database
* [Swagger](https://swagger.io/) - API documentaion and testing software
* [Dillinger](https://dillinger.io/) - Markdown Editor

#### Building with Docker
```sh
$ git clone https://github.com/Naman1997/Micronaut-Mongo-App.git
$ cd Micronaut-Mongo-App
$ sudo docker-compose up -d

```
Now if you open http://localhost:8080/ on your browser of choice, you should be able to access the front-end of the application.

#### Building from source
Install the dependencies and start the server.
In this case I'm assuming that you have a mongodb server already running.
Since the source was designed with mongo running in a container and not on the localhost, you will have to change some configurations.

```sh
$ git clone https://github.com/Naman1997/Micronaut-Mongo-App.git

$ vim Micronaut-Mongo-App/src/main/resources/application.yml
#Change the following line FROM
mongodb:
  uri: "mongodb://mongodb:27017"
# TO
mongodb:
  uri: "mongodb://localhost:27017" #Or the url where your mongo server is running
  
$ vim src/main/java/Database/Connection.java
#Change the following lines FROM
com.mongodb.client.MongoClient mongoClient1 = MongoClients.create("mongodb://mongodb:27017");
#TO
com.mongodb.client.MongoClient mongoClient1 = MongoClients.create("mongodb://localhost:27017"); #Or the url where your mongo server is running

```


```sh
$ cd Micronaut-Mongo-App #Return to the main directory
$ ./gradlew run
```

Now if you open http://localhost:8080/ on your browser of choice, you should be able to access the front-end of the application.

### Front-end Endpoints(GUI)
This application has 4 front-end endpoints that can be seen on the browser at of the time of writing.

| Description | Endpoint |
| ------ | ------ |
| Home screen | http://localhost:8080 |
| Search screen | http://localhost:8080/search/add.html |
| Swagger-ui docs | http://localhost:8080/swagger/views/swagger-ui/index.html |
| Swagger RAW file | http://localhost:8080/swagger/kaiburr-api-1.0.yml |

### API Endpoints

You can access the API documentation of the app at [Swagger](https://app.swaggerhub.com/apis-docs/Naman1997/Kaiburr-Crud/1.0) OR you can access the Swagger endpoint after running the server!

This application has 4 API endpoints at of the time of writing.

| Method | Endpoint |
| ------ | ------ |
| PUT(Add new servers) | localhost:8080/servers |
| GET(Get all records) | localhost:8080/getall |
| GET(Get records filtered by name) | localhost:8080/getbyname?name=serverName |
| DELETE(Delete records by id) | localhost:8080/delete?id=serverID |

License
----

MIT
