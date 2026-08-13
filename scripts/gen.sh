#!/usr/bin/env bash
# Script de generation du code gRPC Python pour Linux / macOS
set -e

echo "Generation des fichiers gRPC Python..."

PYTHON_CMD="python"
if [ -f "./.venv/bin/python" ]; then
    PYTHON_CMD="./.venv/bin/python"
fi

TARGETS=("sandbox/adebayo" "app/common")

for target in "${TARGETS[@]}"; do
    mkdir -p "$target"
    $PYTHON_CMD -m grpc_tools.protoc -Iproto --python_out="$target" --grpc_python_out="$target" proto/distributeur.proto
    echo "  Code genere avec succes dans : $target"
done

echo "Generation terminee avec succes !"
