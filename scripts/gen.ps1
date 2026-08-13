# Script de generation du code gRPC Python depuis proto/distributeur.proto
$ErrorActionPreference = "Stop"

Write-Host "Generation des fichiers gRPC Python..." -ForegroundColor Cyan

# Detection de l'executable Python (priorite a .venv si present)
$pythonCmd = "python"
if (Test-Path ".\.venv\Scripts\python.exe") {
    $pythonCmd = ".\.venv\Scripts\python.exe"
}

$targets = @("sandbox/adebayo", "app/common")

foreach ($target in $targets) {
    if (-not (Test-Path $target)) {
        New-Item -ItemType Directory -Path $target -Force | Out-Null
    }
    & $pythonCmd -m grpc_tools.protoc -Iproto --python_out=$target --grpc_python_out=$target proto/distributeur.proto
    Write-Host "  Code genere avec succes dans : $target" -ForegroundColor Green
}

Write-Host "Generation terminee avec succes !" -ForegroundColor Cyan
