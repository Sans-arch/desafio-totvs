# Build the Java project
build:
	./mvnw clean package -DskipTests

# Completely clean everything (containers, volumes, and .jars)
clean:
	docker-compose down -v --remove-orphans
	./mvnw clean

# Build Docker images from scratch
docker-build:
	docker-compose build --no-cache

# Start containers in the background (detached)
docker-up:
	docker-compose up -d

# Stop and remove containers
docker-down:
	docker-compose down

# Show container logs
logs:
	docker-compose logs -f

# Run tests
test:
	./mvnw test

# Restart everything from scratch (reset total)
restart: clean build docker-build docker-up

# First-time setup (if project has never been built)
start: build docker-build docker-up

# Start the application without losing data (no rebuild, no volume deletion)
run: docker-up

# Stop containers without removing them
stop:
	docker-compose stop

# Update only the application code without losing database data
update:
	./mvnw package -DskipTests
	docker-compose build app
	docker-compose up -d app