#!/bin/sh

TARGET=README.md

cat > $TARGET <<EOF
# Terminology

[![CircleCI](https://circleci.com/gh/toknapp/terminology/tree/master.svg?style=svg)](https://circleci.com/gh/toknapp/terminology/tree/master)

## Example
\`\`\`scala
$(cat src/test/resources/example.sc)
\`\`\`
EOF
