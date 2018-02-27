# Working With Object #

----------

## Create Object ##

**Create Single Object**

Relation specifies in related_objects field. 

Method: POST

URL: /v1/<application-id>/<type-name>

Headers:

Content-Type:application/json


Request Body: 

    {
      "field1": "javad",
      "field2": "developer",
      "related_objects": [
    	 {
      	  "id": "5a206dc2cc2a9b26e483d664",
      	  "type": "denaObjects"
    	 }
      ]
    }


Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of created object(s),
       "objects": [
        {
          "URI":"/<type-name>/<object-id>",
          "object_id": "5a206dc2cc2a9b26e483d664",
          "field1": "javad",
          "field2": "developer"
          "related_objects": [
    	       {
      	       "id": "5a206dc2cc2a9b26e483d664",
      	       "type": "denaObjects"
    	       }
          ]          
        }
       ] 
      }


**Create Bulk Objects**
Request Body: 

    [
      {
        "field1": "javad",
        "field2": "developer",
        "related_objects": [
      	 {
       	  "id": "123123",
       	  "type": "denaObjects"
      	 }
       ]
      },
      {
        "field1": "ali",
        "field2": "developer",
        "related_objects": [
      	 {
       	  "id": "43345",
       	  "type": "denaObjects"
      	 }
       ]
      }
    ]



Return Value:

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
## Updating Objects ##

**Update One Object**

If there is a new field or new relation(new type) add to existing fields or relations. if field is exist then update field. 

If relation with same type exist then replace with new specified relation. 

If object_id for updating object is not found then return bad request error.

If data in relation is invalid for example object_id not exist or type not found return bad request error.


return: 

success: Updated object(s) count.


Method: PUT

URL: /v1/<application-id>/<type-names>

Headers:

Content-Type:application/json

Request Body: 

    {
      "object_id":"5a316b1b4e5f450104c31909",
      "field3": "javad",
      "field4": "developer",
      "new_field":"field",
      "related_objects": [
    	 {
      	  "id": "5a5896bbe61445229c7c62f9",
      	  "type": "Post"
    	 }
      ]
    }

Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of updated object(s),
  	   "objects": [
         {
           "object_id": "5a316b1b4e5f450104c31909",
           "field3": "javad",
           "field4": "developer",
           "new_field":"field",
		       "related_objects": [
    	  	  {
      	  	    "id": "5a5896bbe61445229c7c62f9",
      	  	    "type": "Post"
    	      }
      		]
         }
      	]
      }

----------

**Bulk Update Objects**

if there is a new field or new relation(new type) add to existing fields or relations. if field is exist then update field.

If relation with same type exist then replace with new specified relation. 

return updated object(s) with only updated fields and relation.

Method: PUT

URL: /v1/<application-id>/<type-names>

Headers:

Content-Type:application/json


Request Body: 

    [
      {
        "object_id":"2323424242001",
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
        "object_id":"2323424242002",
        "field5": "javad1",
        "field6": "developer2",
        "related_objects": [
      	 {
        	  "id": "123123",
        	  "type": "para"
      	 }
        ]
     }

    ]

Return Value:

    {
      "timestamp": timestamp in milliseconds,
      "count":number of updated object(s),
      "objects": [
         {
           "object_id": "2323424242001",
           "field3": "javad",
           "field4": "developer"
         },
         {
           "object_id": "2323424242002",
           "field5": "javad1",
           "field6": "developer2"
         }
      ]
    }
    

----------

## Delete Objects ##

**Delete One Object**
This API remove object completely from Dena storage. If the object is successfully delete, the API returns the timestamp of deletion time in milliseconds and number of deleted object count.

Method: DELETE

URL: /v1/<application-id>/<type-names>/<object-id>

Request Body: None

Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of deleted object(s)
      }

----------
**Delete Bulk Objects**

Method: DELETE

URL: /v1/<application-id>/<type-names>/<object-id-1,object-id-2>

Request Body: None

Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of deleted object(s)
      }

----------
**Delete Relation With Type**

This only delete relation between parent and child and do not remove child.


Method: DELETE

URL: /v1/<application-id>/<parent-type-name>/<parent-object-id>/relation/<child-type-name>

Request Body: None

Return Value:

      {
       "timestamp" : timestamp in milliseconds,
       "count" : number of deleted object(s)
      }

----------
**Delete Relation With Specified Child Object**

This API only delete relation between parent and child and do not remove child.

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



