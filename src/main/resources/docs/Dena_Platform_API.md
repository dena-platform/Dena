# Working With Object #

----------

## Create Object ##

**Create Single Object**

Relation specifies in related_objects field. 

Method: POST

URL: /v1/<application-id>/<type-name>

Request Body: 

    {
      "related_objects": [
        {
          "id": "123123",
          "type": "para"
        }
      ],
      "object_values": [
        {
          "field1": "javad",
          "field2": "developer"
        }
      ]
    }

Return Value:

      {
       "creation_timestamp" : timestamp in milliseconds,
       "count" : number of created object(s),
       "URI":"/<type-name>/<object-ids>", 
       "object_values": [
        {
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

URL: /v1/<application-id>/<type-names>/<object-id>

Headers::application/json

Request Body: 

    {
      "related_objects": [
        {
          "id": "123234123",
          "type": "Customer"
        }
      ],
      "object_values": [
        {
          "object_id": "232342424234",
          "field3": "new value",
          "field4": "developer",
          "new_field":"field" 
        }
      ]
    }

Return Value:

      {
       "update_time" : timestamp in milliseconds,
       "update_item_count" : number of updated object(s),
  	   "object_values": [
         {
           "field3": "new value",
           "field4": "developer",
           "new_field":"field" 
         }
      	]
      }

----------

**Update Bulk Objects**

if there is a new field or new relation add to existing fields or relations. if field is exist then update field. 

return updated object(s) with all field except its reference to other object.

Method: PUT

URL: /v1/<application-id>/<type-names>

Headers::application/json

Request Body: 

    [
     {
       "object_id":"232342424234",
       "related_objects": [
         {
           "id": "123234123",
           "type": "Customer"
         }
       ],
       "object_values": [
         {
           "field3": "javad",
           "field4": "developer"
         }
       ]
     },
     {
       "object_id":"2323424243424",
       "related_objects": [
         {
           "id": "123234123",
           "type": "Customer"
         }
       ],
       "object_values": [
         {
           "field5": "javad",
           "field6": "developer"
         }
       ]
     },
	]

Return Value:

    {
      "update_time": 46313132,
      "update_item_count": 10,
      "object_values": [
         {
           "object_id": "232986544",
           "field3": "javad",
           "field4": "developer"
         },
         {
           "object_id": "232986544",
           "field5": "javad54",
           "field6": "developer312"
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
      "object_values": [
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
      "object_values": [
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
      "object_values": [
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
                                     1
Return Value:

      {
       "count" : number of item retrived
      }





