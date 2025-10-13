<#
    Cliente mínimo en PowerShell que:
    - Espera hasta que server_ip y server_port tengan contenido
    - Compila Client.java si falta Client.class
    - Ejecuta java Client con los parámetros obtenidos
    - Guarda la salida en resultadoHost.txt
#>

# Ruta del script
$ScriptDir  = Split-Path -Parent -Path $MyInvocation.MyCommand.Path
$CodigosDir = $ScriptDir   # suponemos que el script está dentro de codigos
$ipPath     = Join-Path $CodigosDir 'server_ip'
$portPath   = Join-Path $CodigosDir 'server_port'
$OutputFile = Join-Path $CodigosDir 'resultadoHost.txt'

# Loop de espera
$ServerIp   = $null
$ServerPort = $null
$Timeout    = 60  # segundos de espera máximo

for ($i = 1; $i -le $Timeout; $i++) {
    if ((Test-Path $ipPath) -and (Test-Path $portPath)) {
        $ipContent   = (Get-Content $ipPath -Raw)
        $portContent = (Get-Content $portPath -Raw)

        if ($ipContent -and $portContent) {
            $ServerIp   = $ipContent.Trim()
            $ServerPort = $portContent.Trim()
            break
        }
    }
    Write-Host "Esperando IP/puerto del servidor... intento $i"
    Start-Sleep -Seconds 1
}

if (-not $ServerIp -or -not $ServerPort) {
    Write-Error "No se pudo obtener IP/puerto del servidor después de $Timeout segundos."
    exit 1
}

# Validar que el puerto sea un número
if (-not ($ServerPort -as [int])) {
    Write-Error "Puerto inválido: '$ServerPort'"
    exit 1
}

Write-Host "Conectando finalmente a $ServerIp : $ServerPort..."

# Compilar Client.java si falta la clase
$ClientClass = Join-Path $CodigosDir 'Client.class'
$ClientJava  = Join-Path $CodigosDir 'Client.java'

if (-not (Test-Path $ClientClass) -and (Test-Path $ClientJava)) {
    Write-Host "Compilando Client.java..."
    pushd $CodigosDir | Out-Null
    javac Client.java
    popd | Out-Null
}

# Ejecutar el cliente y guardar la salida
Write-Host "Ejecutando Client..."
"Ejecutando Client contra $ServerIp : $ServerPort" | Out-File -FilePath $OutputFile -Append -Encoding UTF8

java -cp $CodigosDir Client $ServerIp $ServerPort "1" 2>&1 | Out-File -FilePath $OutputFile -Append -Encoding UTF8

Write-Host "Listo. Resultado en $OutputFile"
