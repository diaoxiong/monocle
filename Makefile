JAR_PATH=target/monocle-executable.jar

.PHONY: compile
compile:
	mvn clean package

.PHONY: run
run:
	nohup java -jar ${JAR_PATH} &

.PHONY: down
down:
	@pid=$$(ps -ef | grep ${JAR_PATH} | grep -v grep | awk '{print $$2}'); \
	if [ "$$pid" != "" ]; then \
		kill -9 "$$pid"; echo "${JAR_PATH} killed"; \
	else \
		echo "${JAR_PATH} not running"; \
	fi;