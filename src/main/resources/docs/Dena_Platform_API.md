# Working With Object #

----------

## Storing Object ##

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
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

    

----------
## Updating Objects ##

**Update One Object**

if there is a new field or new relation add to existing fields or relations. if field is exist then update field. 

return updated object(s) with all field except its reference to other object. 

Method: PUT

URL: /v1/<application-id>/<type-name>/<object-id>

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

URL: /v1/<application-id>/bulk/<type-name>

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

URL: /v1/<application-id>/<type-name>/<object-id>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
**Delete Bulk Objects**

Method: DELETE

URL: /v1/<application-id>/<type-name>/<object-id-1=id1>,<object-id=2>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
**Delete Relation (With All Child)**

Method: DELETE

URL: /v1/<application-id>/<type-name-1>/<object-id-1>/relation/<type-name-2>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
**Delete Relation With Specified Child Object**

Method: DELETE

URL: /v1/<application-id>/<type-name-1>/<object-id-1>/relation/<type-name-2>/<object-id-2>

Request Body: None

Return Value:

      {
       "deletion_time" : timestamp in milliseconds,
       "deleted_item_count" : number of deleted object(s)
      }

----------
## Count Object ##
We can count number of objects three way:


1. Object count for an relation
2. Object count for a type
2. Object count using query (TO-DO)


**Object Count For An Relation**

Method: GET

URL: /v1/<application-id>/<type-name-1>/relation/<type-name-2>

Request Body: None

Return Value:

      {
       "count" : number of item retrived
      }

