# Setup - Step 1
start-ollama:
	ollama start

# Setup - Step 2
run-model:
	ollama run mistral

# Setup - Step 3
start-app:
	./mvnw spring-boot:run

# Movie  Controller
call-api-movie:
	curl -X GET --location "http://localhost:8080/ai/movie/generate?category=drama&year=1999" | jq .

# Newsletter Controller
call-api-newsletter:
	curl -X GET --location "http://localhost:8080/ai/newsletter/generate/tennis" | jq .

# Simple Joke Controller
call-api-joke:
	curl -X GET --location "http://localhost:8080/ai/generate/joke/chickens" | jq .

# Simple ChatBot Controller - what is the history of the eiffel tower?
call-api-chatbot:
	curl -X GET --location "http://localhost:8080/ai/simple?message=what%20is%20the%20history%20of%20the%20eiffel%20tower%3F" | jq .

# RAG  - Retrieval Augmented Generation
data-delete:
	curl -X POST --location "http://localhost:8080/data/delete"

data-count:
	curl -X GET --location "http://localhost:8080/data/count"

data-load: data-delete data-count
	curl -X POST --location "http://localhost:8080/data/load"

# What is ETL pipeline? (RAG)
data-query-with-rag:
	curl -X GET --location "http://localhost:8080/qa?question=What%20is%20ETL%20pipeline%3F&stuffit=true"  | jq .

# What is ETL pipeline? (No RAG)
data-query-with-no-rag:
	curl -X GET --location "http://localhost:8080/qa?question=What%20is%20ETL%20pipeline%3F&stuffit=false"  | jq .

# What is Spring Academy? (No RAG)
data-query-with-no-rag-spring-academy:
	curl -X GET --location "http://localhost:8080/qa?question=What%20is%20Spring%20Academy%3F&stuffit=false"  | jq .

# What is Spring Academy? (RAG)
data-query-with-rag-spring-academy:
	curl -X GET --location "http://localhost:8080/qa?question=What%20is%20Spring%20Academy%3F&stuffit=true"  | jq .