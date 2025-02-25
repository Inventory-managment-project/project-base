#  Run in Docker

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


# Credentials

Change the default credentials  `.env` file to your prefer password, user and database

Like for example 

```
POSTGRES_USER=admin
POSTGRES_PASSWORD=password
POSTGRES_DB=postgres

```
