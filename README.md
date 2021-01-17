TBC

The OAuth is implemented on POST http://interview.agileengine.com/auth using a different contract on the tag names.
                            Body: { "apiKey": "23567b218376f79d9415" }
                            Response: { "token": "ce09287c97bf310284be3c97619158cfed026004" }
                            
I've created a class that wraps the Rest Client functionality in order to execute the calls with the same header regarding to jwt.

I've created AccessToken and Context in order to implement IOC over two spring sec classes. AccessToken implements OAuth2AccessToken. Context implements OAuth2ClientContext.
These classes will be wrapping the request headers in order to change the tag names.

I've added a redis server to use for caching, it's configured using docker-compose.

PhotoServiceController is exposing a search endpoint. It's public. And the app internally uses jwt to connect to the photo storage endpoint.
                                                                                                                  

From the photo ingestion project folder execute:
```mvn clean install.```
From the root folder execute:
```docker-compose up --build```