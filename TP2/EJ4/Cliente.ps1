<#
 Script mínimo: leer IP y puerto del servidor desde ./codigos/server_ip y server_port
 y ejecutar UNA vez el cliente Java. Sin loops extras ni lógica compleja.
#>

$ScriptDir  = Split-Path -Parent -Path $MyInvocation.MyCommand.Path
$CodigosDir = Join-Path $ScriptDir 'codigos'
$ipPath     = Join-Path $CodigosDir 'server_ip'
$portPath   = Join-Path $CodigosDir 'server_port'
$OutputFile = Join-Path $ScriptDir 'resultadoHost.txt'

if (-not (Test-Path $ipPath) -or -not (Test-Path $portPath)) {
    Write-Error "Faltan server_ip o server_port en $CodigosDir"
    exit 1
}

$ServerIp   = (Get-Content $ipPath -Raw).Trim()
$ServerPort = (Get-Content $portPath -Raw).Trim()
if (-not ($ServerPort -as [int])) {
    Write-Error "Puerto inválido: '$ServerPort'"
    exit 1
}

if (-not $ServerIp -or -not $ServerPort) {
    Write-Error "IP o puerto vacíos"
    exit 1
}

# Compila sólo si falta la clase (opcional, mínimo)
if (-not (Test-Path (Join-Path $CodigosDir 'Client.class')) -and (Test-Path (Join-Path $CodigosDir 'Client.java'))) {
    pushd $CodigosDir | Out-Null
    javac Client.java
    popd | Out-Null
}

"Ejecutando Client contra ${ServerIp}:${ServerPort}" | Out-File -FilePath $OutputFile -Append -Encoding UTF8
# Ejecutar el cliente y garantizar que la salida se agrega en UTF-8 (evitando mezcla UTF-16)
java -cp $CodigosDir Client $ServerIp $ServerPort 2>&1 | Out-File -FilePath $OutputFile -Append -Encoding UTF8

Write-Host "Listo. Resultado en $OutputFile"

"-------------------------------------" | Out-File -FilePath $OutputFile -Append -Encoding UTF8