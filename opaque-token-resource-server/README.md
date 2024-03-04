# Resource Server using Opaque Tokens

## Starting the server

### With Spring Authorization Server

To simulate a production like environment you need to start a real identity provider.
In this case a customized spring authorization server.
You can that from here: https://github.com/andifalk/custom-spring-authorization-server.
Start the corresponding server and then start this application here.

If you use IntelliJ then you then can try the api request by running `requests/call_with_real_token.http`, make sure you selected the `prod` environment. 

If you are using any other IDE then you have to use a seperate tool like Postman.

### With Local Testing

To test without the need of an identity provider, the required introspection endpoint is implemented by  a local rest controlle.

If you use IntelliJ then you then can try the api request by running `requests/call_with_dummy_token.http`, make sure you selected the `local` environment.

If you are using any other IDE then you have to use a separate tool like Postman.
