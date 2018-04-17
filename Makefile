README.md: test example
	./readme.sh

test:
	sbt test

example:
	sbt "test:run src/test/resources/example.sc"

.PHONY: README.md test
