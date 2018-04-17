SBT_OPTS ?=
SBT=sbt $(SBT_OPTS)

README.md: test example
	./readme.sh

test:
	$(SBT) test

example:
	$(SBT) example

.PHONY: README.md test
