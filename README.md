# RAG 

## Start ollama
    make start-ollama

## Run Model
    make run-model

## Start the application (Spring Boot Application and Docker Compose: Postgres)
    make start-app

## Load data
    make data-load

## Query
    make call-api-movie
    make call-api-newsletter
    make call-api-joke
    make call-api-chatbot

## Queries - RAG

### What is ETL pipeline? (RAG)
    make data-query-with-no-rag

### What is ETL pipeline? (No RAG)
    make data-query-with-no-rag

### What is Spring Academy? (RAG)
    make data-query-with-rag-spring-academy

### What is Spring Academy? (No RAG)
    make data-query-with-no-rag-spring-academy
