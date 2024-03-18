start-ollama:
	ollama start

run-model:
	ollama run mistral

start-app:
	./mvnw spring-boot:run

call-api:
	curl -X GET --location "http://localhost:8080/ai/simple"

call-api-topic:
	curl -X GET --location "http://localhost:8080/ai/generate/joke/chickens"

call-api-topic-prompt:
	curl -X GET --location "http://localhost:8080/ai/generate/prompt/tennis" | jq .



