#!/bin/sh

TARGET=README.md

cat > $TARGET <<EOF
# Terminology

## Example
\`\`\`scala
$(tail -n+3 example.sc)
\`\`\`
EOF
