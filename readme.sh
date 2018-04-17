#!/bin/sh

TARGET=README.md

cat > $TARGET <<EOF
# Terminology

[![BuildStatus](https://circleci.com/gh/toknapp/terminology.svg?style=svg)](https://circleci.com/gh/toknapp/terminology)

## Example
\`\`\`scala
$(cat src/test/resources/example.sc)
\`\`\`
EOF
