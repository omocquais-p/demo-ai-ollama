# Setup - Step 1
start-ollama:
	ollama start

# Setup - Step 2
run-model-dolphin-phi:
	ollama run dolphin-phi

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
	curl -X GET --location "http://localhost:8080/ai/generate/joke/chickens"

# Simple ChatBot Controller - what is the history of the eiffel tower?
call-api-chatbot:
	curl -X GET --location "http://localhost:8080/ai/simple?message=what%20is%20the%20history%20of%20the%20eiffel%20tower%3F"

# RAG  - Retrieval Augmented Generation
data-delete:
	curl -X POST --location "http://localhost:8080/data/delete"

data-load: data-delete
	curl -X POST --location "http://localhost:8080/data/load"

data-count:
	curl -X GET --location "http://localhost:8080/data/count"

# What is ETL pipeline?
data-query-rag:
	curl -X GET --location "http://localhost:8080/qa?question=What%20is%20ETL%20pipeline%3F"  | jq .
