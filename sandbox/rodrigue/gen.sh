#!/usr/bin/env bash
set -e
cd "$(dirname "$0")"
PY=python3
[ -x ../../.venv/bin/python ] && PY=../../.venv/bin/python
[ -x .venv/bin/python ] && PY=.venv/bin/python

# ---------------------------------------------------------------- Python -----
${PY} -m grpc_tools.protoc -Iproto \
    --python_out=. --grpc_python_out=. --pyi_out=. \
    proto/suivi_colis.proto

echo "Stubs Python generes."

# -------------------------------------------------------------------- Go -----
# Les plugins protoc-gen-go / protoc-gen-go-grpc s'installent avec :
#   go install google.golang.org/protobuf/cmd/protoc-gen-go@latest
#   go install google.golang.org/grpc/cmd/protoc-gen-go-grpc@latest
# ils atterrissent dans $(go env GOPATH)/bin, qui doit etre dans le PATH.
if command -v go >/dev/null 2>&1; then
    export PATH="$PATH:$(go env GOPATH)/bin"
fi

if ! command -v protoc-gen-go >/dev/null 2>&1; then
    echo "protoc-gen-go introuvable : generation Go ignoree (voir README)."
    exit 0
fi

# On reutilise le protoc embarque dans grpc_tools plutot que d'exiger un
# protoc systeme : c'est le meme compilateur, il sait charger nos plugins.
# go_package = "./colispb" dans le .proto -> les fichiers sortent dans go/colispb/.
mkdir -p go
${PY} -m grpc_tools.protoc -Iproto \
    --go_out=go --go_opt=Mproto/suivi_colis.proto=./colispb \
    --go-grpc_out=go --go-grpc_opt=Mproto/suivi_colis.proto=./colispb \
    proto/suivi_colis.proto

echo "Stubs Go generes."
