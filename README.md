System Requirements:
Scala 2.11 
Sbt
docker-compose

Mysql and Elastic search can be run as docker images using : docker-compose up


sbt update
Mysql can be accessed using :
mysql -h 127.0.0.1 -P 13306 -u root -proot

Elastic search can be checked if running properly using below url :
curl http://127.0.0.1:9200/_cat/health

The configuration required for mysql and elastic search is in conf/application.conf
