# Working With Object #

----------

## Create Object ##

**Create Single Object**

Relation specifies in related_objects field. 

Method: POST

URL: /v1/objects

Headers:

Content-Type:application/json


Request Body: 

    {
      "app_id":"app1",
	  "type_name":"a",	
      "field1": "javad",
      "field2": "developer",
      "related_objects": [
    	 {
      	  "id": "123123",
      	  "type": "para"
    	 }
      ]
    }


Return Value:

      {
       "app_id":"app1",
	   "type_name":"a",
       "creation_timestamp" : timestamp in milliseconds,
       "count" : number of created object(s),
       "objects": [
        {
          "URI":"/<type-name>/<object-ids>",
          "object_id": "232342424234",
          "field1": "javad",
          "field2": "developer"
          
        }
       ] 
      }


**Create Bulk Objects**
<TO-DO>    

----------
## Updating Objects ##

**Update One Object**

if there is a new field or new relation add to existing fields or relations. if field is exist then update field. 

return updated object(s) with all field except its references to other object. 

Method: PUT

URL: /v1/objects

Headers:

Content-Type:application/json

Request Body: 

    {
      "app_id":"app1",
	  "type_name":"a",
      "object_id":"6543213584", 
      "field1": "javad",
      "field2": "developer",
      "related_objects": [
    	 {
      	  "id": "123123",
      	  "type": "para"
    	 }
      ]
    }

Return Value:

      {
       "app_id":"app1",
       "type_name":"a",
       "update_time" : timestamp in milliseconds,
       "update_item_count" : number of updated object(s),
  	   "objects": [
         {
           "object_id":"232342424234",
           "field3": "new value",
           "field4": "developer",
           "new_field":"field" 
         }
      	]
      }

----------

**Bulk Update Objects**

if there is a new field or new relation add to existing fields or relations. if field is exist then update field. 

return updated object(s) with all field except its reference to other object.

Method: PUT

URL: /v1/objects

Headers:

Content-Type:application/json


Request Body: 

    [
      {
	    "app_id":"app1",
	    "type_name":"a",
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
	    "app_id":"app1",
        "type_name":"a",
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
      "app_id":"app1",
	  "type_name":"a",
      "update_time": 46313132,
      "update_item_count": 2,
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

Method: DELETE

URL: /v1/<application-id>/<type-names>/<object-id>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
**Delete Bulk Objects**

Method: DELETE

URL: /v1/<application-id>/<type-names>/<object-id-1=id1>,<object-id=2>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
**Delete Relation With Type**

This only delete relation between parent and child and do not remove child.


Method: DELETE

URL: /v1/<application-id>/<type-names-1>/<object-id-1>/relation/<type-names-2>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
**Delete Relation With Specified Child Objects**

Method: DELETE

URL: /v1/<application-id>/<type-names-1>/<object-id-1>/relation/<type-names-2>/<object-id-2>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
## Read Object##

1. Read object by id
2. Read object by type supporting search clause 
3. Read object by relation supporting search clause

**Read object By Id**

In this case this method return only one object.

Method: GET

URL: /v1/<application-id>/<type-names>/<object-id>

Request Body: None

Return Value:

    {
      "count": 1,
      "objects": [
        {
          "object_id": "232986544",
          "field3": "javad",
          "field4": "developer"
        }
      ]
    }


**Read Objects By Type Supporting Search Clause**

Method: GET

URL: /v1/<application-id>/<type-name>?itempPerPage=50&page=4&where=???

Optional Parameter

- **itempPerPage**: item per page. default is 50.
- **page**: starting page of result(start with 0). default is 0.
- **where**: see search object section 


 
Request Body: None

Return Value:

    {
      "count": 50,
      "total_page":30,
      "page":4,
      "objects": [
        {
          "object_id": "232986544",
          "field3": "javad",
          "field4": "developer"
        },
         {
           "object_id": "232986554",
           "field5": "javad54",
           "field6": "developer312"
         }
      ]
    }


**Read Object By Relation Supporting Search Clause**

Method: GET

URL: /v1/<application-id>/<type-names-1>/relation/<type-names-2>?itempPerPage=50&page=4&where=???

Optional Parameter

- **itempPerPage**: item per page. default is 50.
- **page**: starting page of result(start with 0). default is 0.
- **where**: see search object section 


 
Request Body: None

Return Value:

    {
      "count": 50,
      "total_page":30,
      "page":4,
      "objects": [
        {
          "object_id": "232986544",
          "field3": "javad",
          "field4": "developer"
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
       "count" : number of item retrived
      }

**Object Count For Type**

Method: GET

URL: /v1/<application-id>/<type-names>

Request Body: None
                                     
Return Value:

      {
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



