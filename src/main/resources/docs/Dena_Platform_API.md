# Working With Object #

----------

## Create Object ##

**Create Single Object**


Method: POST

URL: /v1/<application-id>/<type-name>

Body:

{JSON}


Headers:

Content-Type:application/json

***Example:***

Request Body: 

    {
      "name": "javad",
      "job": "developer",
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

Method: POST

URL: /v1/<application-id>/<type-name>

Body:

{JSON Array}


Headers:

Content-Type:application/json

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
     	  }]
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
          }]
  		 }
      ]



Response Body:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of created object(s),
       "objects": [
        {
          "URI":"/<type-names>/<object-id>",
          "object_id": "232342424234",
          "field1": "javad",
          "field2": "developer",
          "related_objects": [
    	       {
      	         "id": "123123",
      	         "type": "denaObjects"
    	       }
        },
        {
          "URI":"/<type-names>/<object-id>",
          "object_id": "232342424234",
          "field1": "ali",
          "field2": "developer",
          "related_objects": [
    	       {
      	         "id": "43345",
      	         "type": "denaObjects"
    	       }

          
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

**Update One Object**

- If there is a new field or relation then create it. 
- if field is exist then update it. 
- If relation with same name exist in data store and related object not stored before then add, otherwise ignore it. 
- If object id is not found then return bad request error.
- If data in relation is invalid for example object id not exist or target type not found then return bad request error.
 
 

Return: Updated object(s) count.

Method: PUT

URL: /v1/<application-id>/<type-names>

Headers:

Content-Type:application/json

Body:

{JSON}

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

----------

**Bulk Update Objects**

if there is a new field or new relation(new type) add to existing fields or relations. if field is exist then update field.

If relation with same type exist then replace with new specified relation. 

Return: updated object(s) with only updated fields and relation.

Method: PUT

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
**Delete Relation With Type**

Remove all relation between parent and child.

**Note:** This only delete relation between parent and child and do not remove child objects.


Method: DELETE

URL: /v1/<application-id>/<parent-type-name>/<parent-object-id>/relation/<child-type-name>

Request Body: None

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

Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of deleted object(s)
      }

----------
## Find Object##
By default when you get an object from Dena platform, related objects not included in the response because it may cause load performance on server or client memory.Therefore you should get related object in separate request.   

1. Find object by id
2. Find object by type supporting search clause 
3. Find object by relation supporting search clause

**Find object By Id**

// Todo: remove related_objects in response. when in future we implemented auto reload feature then add it.

In this case this method return only one object.


Method: GET

URL: /v1/<application-id>/<type-name>/<object-id>

Request Body: None

Return Value:

    {
      "timestamp" : "1513233158180",
      "count": 1,
      "objects": [
        {
          "object_id": "5a316b1b4e5f450104c31909",
          "object_uri":"/denaTestCollection/5a316b1b4e5f450104c31909",
          "field3": "javad",
          "field4": "developer",
          "related_objects": [
    	      {
      	      "id": "123123",
      	      "type": "para"
    	      }
        }
      ]
    }



**Find Objects By Type Supporting Search Clause (TO-DO)** 

Method: GET

URL: /v1/<application-id>/<type-name>?itempPerPage=50&page=4&where=???

Optional Parameter

- **itempPerPage**: item per page. default is 50.
- **page**: starting page of result(start with 0). default is 0.
- **where**: see search object section 


 
Request Body: None

Return Value:

    {
      "timestamp" : timestamp in milliseconds,
      "count": 50,
      "total_page":30,
      "page":4,
      "objects": [
        {
          "object_id": "5a316b1b4e5f450104c31909",
          "field3": "javad",
          "field4": "developer",
          "related_objects": [
    	       {
      	     "id": "5a316b1b4e5f450104c31801",
      	     "type": "person"
    	       }
          ]
         },
         {
           "object_id": "5a316b1b4e5f450105c31910",
           "field5": "javad54",
           "field6": "developer312",
           "related_objects": [
    	       {
      	      "id": "5a316b1b4e5f450104c31800",
      	      "type": "person"
    	       }
            ]

         }
      ]
    }


**Find Object Relation Supporting Search Clause**

// Todo: remove related_objects in response. when in future we implemented auto reload feature then add it.

Suppose we have relation between two object for example in many-many or one-many relation and we want to retrieve related object. in this case we want only get 
a portion of related object not all (because of too many object) so we use paging.

Consider following Pseudo-Code:

    User 1<-->* Transaction
    User user = findUser(userId)
    user.getTransaction() // find all transaction relation in user
    
Method: GET

URL: /v1/<application-id>/<parent-type-name>/<parent-object-id>/relation/<child-type-name>?itemPerPage=50&page=4&where=???

Optional Parameter

- **itempPerPage**: item per page. default is 50.
- **page**: starting page of result(start with 0). default is 0.
- **where**: see search object section 


 
Request Body: None

Return Value:

    {
      "timestamp" : timestamp in milliseconds,
      "count": 50,
      "total_count":30,
      "page":4,
      "objects": [
        {
          "object_id": "232986544",
          "field3": "javad",
          "field4": "developer",
          "related_objects": [
    	       {
      	     	"id": "123123",
      	     	"type": "para"
    	       }
          ]
 
        },
         {
           "object_id": "232986554",
           "field5": "javad54",
           "field6": "developer312"
         }
      ]
    }

----------
## Where Clause ##
TO-DO


----------

## Count Object ##
We can count number of objects three way:


1. Object count for relation
2. Object count for type
2. Object count using query (TO-DO)


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

----------
## Error Response ##
When error occurred in service then the following json return
 
    {
      "status":400,
      "error_code":"123",
      "messages":["Parameter is invalid", "Relation can not be empty"]
    
    }



