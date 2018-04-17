#!/bin/sh

TARGET=README.md

cat > $TARGET <<EOF
# Terminology

## Example
\`\`\`scala
$(cat src/test/resources/example.sc)
\`\`\`
EOF
