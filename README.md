# System Requirements:
* Scala 2.11 
* sbt
* docker-compose

# Compiling service :
`sbt clean ccompile test`

# Running Service :
* Start local mysql and elastic search service :
  1. `docker-compose up`
* Execute queries to create some test data
  0. `cd Boogle`
  1. ` mysql -h 127.0.0.1 -P 13306 -u root -proot Boogle < test/resources/create_tables.sql`
* Starting rest service :
  1. `sbt run`
  This starts service locally on port 9000
  
# Sample End points :
  * Search Pages with text 'Unbound':
  1. `http://localhost:9000/books/search_text?text=Unbound`
  #### The text search is case insensitive
  #### Response format is as follows :
  ```[
    {
        "search_id": "1",
        "page": {
            "id": 1,
            "book": "The Da Vinci Code",
            "author": "Dan Brown",
            "page_number": 3,
            "chapter": "Chapter 1",
            "content": "Robert Langdon awoke slowly. A telephone was ringing in the darkness-a tinny, unfamiliar ring. He fumbled for the bedside lamp and turned it on. Squinting at his surroundings he saw a plush Renaissance bedroom with Louis XVI furniture, hand-frescoed walls, and a colossal mahogany four-poster bed. Where the hell am I? The jacquard bathrobe hanging on his bedpost bore the monogram: HOTEL RITZ PARIS. Slowly, the fog began to lift."
        }
    }
]
```
  * Search Pages with book name and page number :
  1. `http://localhost:9000/books/search_page?book=The Da Vinci Code&number=3`
  
     Response for this endpoint is as above
  * Index Page needs page_id from the database which in  mysql. Indexing assumes page is already present in database :
  1. `http://localhost:9000/index/page/2`
  
  The page_id from the database is used as the id to index in elastic search too, therefore indexing the same page multiple times will not create multiple indices in elastic search)
  
   Response is the string ```Indexed page with id: ${id}```
       

## Mysql can be accessed using :
`mysql -h 127.0.0.1 -P 13306 -u root -proot`

### Elastic search can be checked if running properly using below url :
`curl http://127.0.0.1:9200/_cat/health`

### The configuration required for mysql and elastic search is in conf/application.conf

## Logic :
  On application startup, all the pages from the database are indexed in elastic search. All the requests to the api, go to elastic search. For now, the end points are only for searching by text and searching by book-pagenumber, however this can be easily extended to any field in the page object by leveraging the flexibility given by elastic search.
  The config for elastic search and mysql is in application.conf
  The application on startup, ooads entire data from the db to ensure all pages are indexed. So, everytime the rest service is started, indices are refreshed. 
  As page_id from the database is used as the id for the index to, one page has only one index in elastic search even if it is created multiple times
  The login for de-index (still not implemented completely), is to ave an extra field `index_bool` in mysql database for table `Pages` which is a flag that tells whether a particular page can be indexed or not. The value is true by default for all pages. Calling de-index end-point will mark this value for that page as false and also call index function of elastic index with this new page data. When searching, the service returns only those pages from elastic search for which 'index_bool' is true. Note that the page is index as far as elastic serach is concerned btu for the client of the srevice, this page is always hidden as the 'index_bool' value is false. Updating the 'index_bool' value in database also makes sure that on service re-start, the pages are indexed with correct 'index_bool' value.
  
  
## TO DO :
  * De-index endpoint is is process 
  * Adding more test cases
  * Dockerise the service so that elastic-serach, mysql and the play service start simultaneously. Currently, mysql and elastic search have to be started in a separate window using docker-compose
  * Proper error response of the api
  * Use ORM instead of raw queries of mysql to get data
  * Add some load tests
  
  
