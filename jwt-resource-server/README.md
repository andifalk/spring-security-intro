# Resource Server using JSON Web Tokens (JWT)

## Starting the server

### With Spring Authorization Server

To simulate a production like environment you need to start a real identity provider.
In this case a customized spring authorization server.
You can that from here: https://github.com/andifalk/custom-spring-authorization-server.
Start the corresponding server and then start this application here.

If you use IntelliJ then you then can try the api request by running `requests/call_with_real_token.http`, make sure you selected the `prod` environment. 

If you are using any other IDE then you have to use a separate tool like Postman.

### With Local Testing

To test without the need of an identity provider, a testing endpoint for getting a test JWT is implemented by  a local rest controller.

If you use IntelliJ then you then can try the api request by running `requests/call_with_test_token.http`, make sure you selected the `local` environment.

If you are using any other IDE then you have to use a separate tool like Postman.
