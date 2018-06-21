# Schema #
Schema is the structure of tables in Dena platform. With schema, developers can get table structure ,add/remove columns and define constrains on columns (not implemented yet).

## Create Table ##
Request:  
Method: POST  
URL: /v1/<application-id>/schema/<table-name>

Headers:  
token: {user token after login in Dena platform}  
Content-Type: application/json

Body: {None}


Response:  
Number of created table

***Example:***

Request URL: https://dena-platform.com/<application-id>/schema/table1  

Request Body:  

	Empty


Response Body:


	{
	  "status":200,	
	  "timestamp": 1520504910721,
	  "create_table_count(s)": 1
	}


## Get All Table Schema ##
Method: GET  
URL: /v1/<application-id>/schema  

Headers:  
token: {user token after login in dena-platform}  

Body:  
{None}

Response   
Return the schema of table

***Example:***  
Request URL: https://dena-platform.com/<application-id>/schema 

Request Body:  

	{
	  "status":200,	
	  "timestamp": 1520504910721,
	  "count": 2,
	  "objects": [
	    {
	      "name": "table1",
		  "record_count":123
	    },
	    {
	      "name": "table2",
		  "record_count":123
	    }
	  ]
	}


## Delete Schema ##
Method: DELETE  
URL: /v1/<application-id>/schema/{table-name}  

Headers:  
token: {user token after login in dena-platform}  

Body:  
{None}

Response   
Number of deleted schema


***Example:***  
Request URL:https://dena-platform/<application-id>/schema/{table-name} 

Body:  
{None}  

Response:  

	{
	  "timestamp": 1520504910721,
	  "count": 1
	}


# Working With Object #

----------

## Create Objects ##
Create new object in Dena data store.  

**Create Single Object**

Return: Created object(s) count. 

Method: POST

URL: /v1/<application-id>/<table-name>/?loadRelation=false 


Headers:  
Content-Type:application/json 

Body:  
{JSON}

Optional Parameter  
- **loadRelation**: Whether load relation after creating object.   

***Example:***

Request Body: 

    {
      "name": "javad",
      "job": "developer"
    }


Response Body:

      {
        "timestamp": 1520504910721,
        "count": 1,
        "objects": [
         {
           "object_id": "5aa1104e99d0b323487d38a1",
           "object_uri": "/posts/5aa1104e99d0b323487d38a1",
           "update_time": null,
           "creation_time": 1520504910672,
         }
        ]
      }


**Create Bulk Objects**  
This API is same is above with the exception that the request can contain 
multiple request.

Return: Created object(s) count. 

Method: POST

URL: /v1/<application-id>/<table-name>?loadRelation=false

Headers:  
Content-Type:application/json 

Body:  
{JSON Array}

Optional Parameter  
- **loadRelation**: Whether load relation after creating object.   


***Example:***

Request Body: 

    [
      {
        "field1": "javad",
        "field2": "developer",
        "related_objects": [
          {
            "relation_name": "comments_rel",
            "target_name": "comments",
            "ids": [
              "5aa52335d41a3b18c8bdbe28"
            ]
          }
        ]
      },
      {
        "field1": "ali",
        "field2": "developer",
        "related_objects": [
          {
            "relation_name": "comments_rel",
            "target_name": "comments",
            "ids": [
              "5aa52335d41a3b18c8bdbe28"
            ]
          }
        ]
      }
    ]



Response Body:

    {
        "timestamp": 1521833800182,
        "create_object_count(s)": 2,
        "objects": [
            {
                "object_id": "5ab557484611681f7c07a6dd",
                "object_uri": "/post/5ab557484611681f7c07a6dd",
                "update_time": null,
                "create_time": 1521833800007,
                "field1": "javad",
                "field2": "developer"
            },
            {
                "object_id": "5ab557484611681f7c07a6de",
                "object_uri": "/post/5ab557484611681f7c07a6de",
                "update_time": null,
                "create_time": 1521833800099,
                "field3": "javad",
                "field4": "developer"
            }
        ]
    } 

----------
## Create Relation ##
Related object should exist before calling this API.

There is two approach to create relation between objects.

1. Create relation when requesting create new object.
2. Use separate API for creating relation between objects.

**Create relation when requesting create new object** 

Relation specifies in related_objects field. 

> 
Begin Implementation Detail

related_objects contain three field

1. relation_name: name of relation in Dena platform. we store it so when we retrieve a Dena object we can recognize field. 
2. target_type: name of destination relation type.To recognize destination type for example when we want show destination type in panel.   
3. id: ids of destination type. 

In response to client Dena do not return relation objects because of object because of performance.


> 
End Implementation Detail



Method: POST

URL: /v1/<application-id>/<type-name>

Headers:

Content-Type:application/json

Body:

{JSON}

***Example:***

Request Body: 

    {
      "field1": "javad",
      "field2": "developer",
      "related_objects": [
    	 {
          "relation_name":"comments_rel",
          "target_name":"comments", 
      	  "id": ["5a206dc2cc2a9b26e483d664", "5a206dc2cc2a9b26e483d634"]
     	 }
      ]
    }


Response Body:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of created object(s),
       "objects": [
        {
          "URI":"/<type-name>/<object-id>",
          "object_id": "5a206dc2cc2a9b26e483d664",
          "field1": "javad",
          "field2": "developer"               
        }
       ] 
      }


**Use separate API for creating relation between objects** (TO-DO)

 

----------

## Updating Objects ##

**Update (Merge) One Object**

- If there is a new field or relation then create it. 
- if field is exist then update it. 
- If relation with same name exist in data store and related object not stored before then add, otherwise ignore it. 
- If object id is not found then return bad request error.
- If data in relation is invalid for example object id not exist or target type not found then return bad request error.

 

Return: Updated object(s) count. 
 
Method: PATCH  

URL: /v1/<application-id>/<table-name>?loadRelation=false  

Headers:  
Content-Type:application/json 
 
Body:  
{JSON}

Optional Parameter   
- **loadRelation**: Whether load relation after updating object.   

***Example:***

Request Body: 

	{
  		"object_id": "5aa69e99d41a3b2480ec35b0",
  		"field3": "javad",
  		"new_field": "new_field_v1",
  		"related_objects": [
   	 		{
   	   		  "relation_name": "comments_rel",
   	   		  "target_name": "comments",
   	   		  "ids": [
   	     	     "5aa9348ed41a3b25b8b49b79"
   	          ]
   	 	    }
  		]
	}


Response Body:

    {
        "timestamp": 1521095130350,
        "status":200,
        "update_object_count": 1,
        "objects": [
            {
                "object_id": "5aaa0ae6ecb1ef188094eed0",
                "object_uri": "/post/5aaa0ae6ecb1ef188094eed0",
                "update_time": 1521095130344,
                "create_time": 1521093350371,
                "field1": "javad",
                "new_field": "new_field_v1",
                "field3": "javad",
                "field2": "developer"
            }
        ]
    }

**Update (Replace) One Object**  
Completely replace object in Dena data-store. remove all previous data and add new requested data .

Return: Updated object(s) count.  
Method: PATCH  
URL: /v1/<application-id>/<table-name>?loadRelation=false   
Headers: Content-Type:application/json  
Body:  
{JSON}

Optional Parameter  

- **loadRelation**: Whether load relation.   


***Example:***

Request Body: 

	{
  		"object_id": "5aa69e99d41a3b2480ec35b0",
  		"field3": "javad",
  		"new_field": "new_field_v1",
  		"related_objects": [
   	 		{
   	   		  "relation_name": "comments_rel",
   	   		  "target_name": "comments",
   	   		  "ids": [
   	     	     "5aa9348ed41a3b25b8b49b79"
   	          ]
   	 	    }
  		]
	}


Response Body:

    {
        "timestamp": 1521095130350,
        "status":200,
        "update_object_count": 1,
        "objects": [
            {
                "object_id": "5aaa0ae6ecb1ef188094eed0",
                "object_uri": "/post/5aaa0ae6ecb1ef188094eed0",
                "update_time": 1521095130344,
                "create_time": 1521093350371,
                "new_field": "new_field_v1",
                "field3": "javad"                
            }
        ]
    }

----------

**Bulk (Merge) Update Objects**  
if there is a new field or new relation(new type) add to existing fields or relations. if field is exist then update field.

If relation with same type exist then replace with new specified relation. 

Return: updated object(s) with only updated fields and relation.

Method: PATCH

URL: /v1/<application-id>/<type-names>

Headers:

Content-Type:application/json

Body:

{JSON}


***Example:***

Request Body: 

    [
      {
        "field1": "javad",
        "field2": "developer",
        "related_objects": [
          {
            "relation_name": "comment_relation",
            "relation_type": "ONE-TO-ONE",
            "target_name": "comment",
            "ids": [
              "5aaa095eecb1ef188094eece"
            ]
          }
        ]
      },
      {
        "field3": "javad",
        "field4": "developer",
        "related_objects": [
          {
            "relation_name": "comment_relation",
            "relation_type": "ONE-TO-ONE",
            "target_name": "comment",
            "ids": [
              "5aaa0982ecb1ef188094eecf"
            ]
          }
        ]
      }
    ]


Response Body:

    {
        "timestamp": 1521095304269,
        "update_object_count": 2,
        "objects": [
            {
                "object_id": "5aaa0ae6ecb1ef188094eed0",
                "object_uri": "/post/5aaa0ae6ecb1ef188094eed0",
                "update_time": 1521095304258,
                "create_time": 1521093350371,
                "field1": "javad",
                "new_field": "new_field_v1",
                "field3": "javad",
                "field2": "developer"
            },
            {
                "object_id": "5aaa0c05ecb1ef188094eed3",
                "object_uri": "/post/5aaa0c05ecb1ef188094eed3",
                "update_time": 1521095304263,
                "create_time": 1521093637106,
                "field1": "javad",
                "new_field": "new_field_v1",
                "field3": "javad",
                "field2": "developer"
            }
        ]
    }   

----------

## Delete Objects ##

**Delete One Object**
This API remove object completely from Dena storage. If the object is successfully delete, the API returns the timestamp of deletion time in milliseconds and number of deleted object count.

Method: DELETE

URL: /v1/<application-id>/<type-names>/<object-id>

Body: None

***Example:***

Response Body:

    {
        "timestamp": 1521104923089,
        "delete_object_count(s)": 1
    }

----------
**Delete Bulk Objects**

Method: DELETE

URL: /v1/<application-id>/<type-names>/<object-id-1,object-id-2>

Request Body: None

Response Body:

      {
       "timestamp" : 1521104923089,
       "delete_object_count(s)": 4
      }

----------
**Delete Relation**

Remove all relation between parent and child.

**Note:** This only delete relation between parent and child and do not remove child objects.


Method: DELETE

URL: /v1/<application-id>/<parent-type-name>/<parent-object-id>/relation/<child-type-name>

Request Body: None

***Example:***

Response Body:

    {
        "timestamp": 1521108111690,
        "delete_relation_count(s)": 1
    }

----------
**Delete Relation With Specified Child Object**

Remove relation between parent and specified child object.

**Note:** This API only delete relation between parent and child and do not remove child.

Method: DELETE

URL: /v1/<application-id>/<parent-type-names>/<parent-object-id>/relation/<child-type-names>/<child-object-id>

Request Body: None

***Example:***

Response Body:

    {
        "timestamp": 1521108111690,
        "delete_relation_count(s)": 1
    }

----------
## Find Object ##
By default when you get an object from Dena platform, related objects not included in the response because it may cause load performance on server or client memory. Therefore by default we get related object in a separate request.   

1. Find object by id
2. Find objects in table
3. Find objects for relation

**Find object By Id**  
This API provides the functionality that retrieve specified object in table.


Method: GET

URL: /v1/<application-id>/<table-name>/<object-id>?loadRelation=false

Optional Parameter  

- **loadRelation**: Whether load relation.   

Request Body: None

***Example:***

Response Body:

    {
        "timestamp": 1522349375351,
        "found_object_count(s)": 1,
        "objects": [
            {
                "object_id": "5aaa8234bb19df25acce463e",
                "object_uri": "/post/5aaa8234bb19df25acce463e",
                "update_time": null,
                "create_time": 1521123892553,
                "field3": "javad",
                "field4": "developer"
            }
        ]
    }



**Find All Objects In Table**  
This API provides the functionality that retrieve all object of specified table data.

Method: GET

URL: /v1/<application-id>/<table-name>?startIndex=45&pageSize=6

Optional Parameter

- **startIndex**: The start row number from which return result to client.starting from 0 and default to 0.
- **pageSize**: The number of records to retrieve in a single page. default to 50.


*Note:* for more information see [Pagination](#pagination) section

Request Body: None

***Example:***

/v1/denaQA/post?startIndex=0&pageSize=2

Response Body:

    {
        "timestamp": 1522448368270,
        "found_object_count(s)": 2,
        "objects": [
            {
                "object_id": "5aaa11d2ecb1ef188094eed6",
                "object_uri": "/post/5aaa11d2ecb1ef188094eed6",
                "update_time": null,
                "create_time": 1521095122362,
                "field3": "javad",
                "field4": "developer"
            },
            {
                "object_id": "5aaa445ebb19df061c79f8f0",
                "object_uri": "/post/5aaa445ebb19df061c79f8f0",
                "update_time": null,
                "create_time": 1521108062035,
                "field1": "javad",
                "field2": "developer"
            }
        ]
    }


**Find Relation Objects**

Suppose we have relation between two object for example in many-many or one-many relation and we want to retrieve related object, in this case we use this API to retrieve all objects of a relation.

Consider following Pseudo-Code:

    User 1<-->* Transaction
    User user = findUser(userId)
    user.getTransaction() // find all transaction relation in user

Method: GET

URL: /v1/<application-id>/<parent-table-name>/<parent-object-id>/relation/<relation-name>?startIndex=45&pageSize=6

Optional Parameter

- **startIndex**: The start row number from which return result to client.starting from 0 and default to 0.
- **pageSize**: The number of records to retrieve in a single page. default to 50.

*Note:* for more information see [Pagination](#pagination) section



Request Body: None

***Example:***

/v1/denaQA/post?startIndex=0&pageSize=2

Response Body:

    {
        "timestamp": 1522485942626,
        "found_object_count(s)": 2,
        "objects": [
            {
                "object_id": "5aaa095eecb1ef188094eece",
                "object_uri": "/comment/5aaa095eecb1ef188094eece",
                "update_time": null,
                "create_time": 1521092958714,
                "comment_text": "this is a comment"
            },
            {
                "object_id": "5aaa0982ecb1ef188094eecf",
                "object_uri": "/comment/5aaa0982ecb1ef188094eecf",
                "update_time": null,
                "create_time": 1521092994863,
                "comment_text": "this is a comment 2"
            }
        ]
    }

----------

## Count Object ##
We can count number of objects three way:


1. Object count for relation
2. Object count for type
3. Object count using query (TO-DO)


**Object Count For Relation**

Method: GET

URL: /v1/<application-id>/<type-names-1>/relation/<type-names-2>

Request Body: None

Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of item retrived
      }

**Object Count For Type**

Method: GET

URL: /v1/<application-id>/<type-names>

Request Body: None
                                     
Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of item retrived
      }


## <a name="pagination"></a> Pagination
When dena platform receive request for getting objects, it limit objects count for return to client for performance reason. So we use a mechanism for pagination result to client.

Pagination using in the REST API is implemented with the `startIndex` and `pageSize` parameters in the objects retrieval API: 

`startIndex`: The start row number from which return result to client. starting from 0 and default to 0.

`pageSize`: The number of records to retrieve in a single page. Minimum allowed value is 1. Default to 50. 

**Sample Request**

`/v1/<application-id>/<parent-table-name>/<parent-object-id>/relation/<relation-name>?startIndex=45&pageSize=6`

`/v1/<application-id>/<table-name>?startIndex=0&pageSize=2` 

----------
# User Management #
This API provides the functionality related to the user management such as user registrations, login and logout.


## Register New User ##

This API can be used to create new user in the application. When user created, by default its status is active.

***Note:***  
Email and password fields is required in registration new user. Additional property can also included in the request 
body.

Return: Created user account information.  

Method: POST  
URL: /v1/<application-id>/users/register  

Headers:  
Content-Type : application/json

Body:   
{JSON}

***Example:***

Request Body: 

    {
      "email": "user1@dena-platform.com",
      "password": "123456",
      "name":"javad",
      "family":"alimohammadi"
      ... other fields
    }


Response Body:

      {
        "timestamp": 1520504910721,
        "count": 1,
        "objects": [
         {
           "object_id": "5aa1104e99d0b323487d38a1",
           "creation_time": 1520504910672,
           "update_time": null,
           "email": "user1@dena-platform.com" ,
           "password": "$2a$10$lkjro.gqyjKA3/PCBPFBauPr69V5eYE8p9kDYh9yY07dRwKPeHiFu",
           "name": "javad",
           "family": "alimohammadi",          
           "is_active": true,
           ... other fields
         }
        ]
      }



## Login ##

With this API user can login in Dena Platform. after user successfully login then in subsequent request access token should be included.

Method: POST

URL: /v1/<application-id>/users/login

Body:

{JSON}


Headers:

Content-Type:application/json

Headers:

Content-Type:application/json

***Example:***

Request Body: 

    {
      "email": "user2@denaplatform.com",
      "password": "123456"
    }

Response Body:

    {
      "timestamp": 1520504910721,
      "count": 1,
      "objects": [
        {
          "object_id": "5aa1104e99d0b323487d38a1",
          "email": "user1@dena-platform.com" ,
          "access-token": "46s4f6sf4s6d5fd2s3df1sd3f1sdf5weredvx",
          "name": "javad",
          "family": "alimohammadi",
          "is_active": true,
          ... other fields
        }
      ]
    }

----------

# Application Management #
With this API user can manage application (create, edit, find application)

## Create New Application ##

Method: POST

URL: /v1/app/register

Body:

{JSON}


Headers:

Content-Type:application/json

Headers:

Content-Type:application/json

***Example:***

Request Body: 

    {
      "application_name": "great_app",
      "creator_id": "developer@dena.com"
    }

Response Body:

    {
      "timestamp": 1520504910721,
      "count": 1,
      "objects": [
        {
          "object_id": "5aa1104e99d0b323487d38a1",
          "application_name": "great_app" ,
          "creator_id": "developer@dena.com",
          "application_id": "8a0d747e-8b4a-418a-a658-57dd995945a4",
          "secret_key": "05c977f0-14be-4f2e-960d-1103fa3646bd"
        }
      ]
    }



## Error Response ##
When error occurred in service then the following json return

    {
      "status":400,
      "error_code":"123",
      "messages":["Parameter is invalid", "Relation can not be empty"]
    
    }



