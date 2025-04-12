FROM gradle:8.13

WORKDIR /app

EXPOSE 8181 

CMD ["gradle", "run" ]
