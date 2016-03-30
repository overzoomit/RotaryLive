#RotaryLive REST Api install
...


#RotaryLive REST Api authentication

RotaryLive provides access to its own features via a set of REST APIs, accessible for authenticated users. 
Every exposed endpoint needs to be queried setting up an *HTTP header* which will be used to ensure you are authorize to perform the requested operation.

Under the hood, RotaryLive REST API uses the **OAuth2** mechanism along with a **Basic Authentication** method to enforce user login. 

A typical workflow to retrieve the token to be used in all next authenticated requests can be summarized as follows:

    curl -X POST -vu rotarylive:HgjF3RAxWBKJq6qb http://localhost:8383/oauth/token -H "Accept: application/json" -d "password=v9y7xaeHWnc99BMg&username=flavio&grant_type=password&scope=read&client_secret=HgjF3RAxWBKJq6qb&client_id=rotarylive"

which give you the following response:

    {
      "access_token": "<access_token>",
      "token_type": "bearer",
      "refresh_token": "<refresh_token>",
     "expires_in": <expire_time_in_seconds>,
     "scope": "read"
    }

Using the returned *access_token* value you can access to the protected endpoints setting the *Authorization* header in the HTTP request as follows:

    curl http://localhost:8383/api/user/1 -H "Authorization: Bearer <access_token>".

You can also POST, PUT or DELETE a record data:    
    
    curl -X POST http://localhost:8383/api/user -H "Authorization: Bearer <access_token>" -H "Content-Type: application/json" -d '{"id":2,"name":"Flavio Troia", "username":"flavio", "password":"*******"}'
    
    curl -X PUT http://localhost:8383/api/user/2 -H "Authorization: Bearer <access_token>" -H "Content-Type: application/json" -d '{"name":"Flavio Troia", "username":"flavio.troia", "password":"*******"}'
    
    curl -X DELETE http://localhost:8383/api/user/2 -H "Authorization: Bearer <access_token>" -H "Content-Type: application/json"
  
Refer to the [doc](http://%3CRotaryLiveRESTAPIUrl%3E:8282/v2/api-docs) to get the exact model serialization of the previous REST call.

To renew an expired *access_token* use this:

    curl -X POST -vu rotarylive:HgjF3RAxWBKJq6qb http://localhost:8383/oauth/token -H "Accept: application/json" -d "grant_type=refresh_token&refresh_token=<refresh_token>&client_secret=HgjF3RAxWBKJq6qb&client_id=rotarylive"


----------
Stasbranger Dev Team