FROM gradle:8.13

WORKDIR /app

EXPOSE 8080

CMD ["gradle", "run" ]
