Start PostgreSQL Container
```
docker-compose up -d
```
Check Logs (Optional)
```
docker logs my_postgres
```
Access PostgreSQL
```
docker exec -it my_postgres psql -U myuser -d mydatabase
```
Stop the Database
```
docker-compose down
```
