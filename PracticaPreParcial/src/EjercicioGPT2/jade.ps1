# PowerShell script to compile and run Java programs with JADE

# Parametros
param (
    [string]$target
)

# Variables
$JAVAC = "javac"
$JAVA = "java"
$CLASSPATH = "lib/jade.jar"
$SRC_DIR = "Codigos"
$OUT_DIR = "classes"
$MAIN_CLASS = "EjercicioGPT2.Codigos.AgenteA"

# Function to compile Java code
function Compile {
    Write-Output "Compiling Java code..."
    & $JAVAC -classpath $CLASSPATH -d $OUT_DIR "$SRC_DIR/*.java"
}

# Function to run JADE in GUI mode
function Gui {
    Write-Output "Starting JADE with GUI..."
    & $JAVA -cp $CLASSPATH jade.Boot -gui
}

# Function to run an agent on Windows
function Agente_Win {
    Write-Output "Arrancando  agente JADE en Windows..."
    & $JAVA -cp "lib/jade.jar;$OUT_DIR" jade.Boot -container -container-name contenedorEjercicio1 -host localhost -agents "AgenteA:$MAIN_CLASS"
}

# Function to run an agent on a remote Windows machine 
function Agente_win_remoto{
    Write-Output "Arrancando  agente JADE en Windows..."
    & $JAVA -cp "lib/jade.jar;$OUT_DIR" jade.Boot -container -container-name contenedorEjercicio1 -host 192.168.0.6 -local-host 192.168.0.11 -agents "AgenteA:$MAIN_CLASS"
}
#Funcion para crear contenedores locales en background
function crear_contenedores_locales {
    Write-Output "Creando contenedores locales en background (sin interrumpir la consola)..."
    $cp = "$CLASSPATH;$OUT_DIR"
    $args = @(
        '-cp', $cp,
        'jade.Boot',
        '-container',
        '-host', 'localhost'
    )
    Start-Process -FilePath $JAVA -ArgumentList $args -WindowStyle Hidden
}
# Main logic to call functions based on input parameter
switch ($target) {
    "compile" { Compile }
    "gui" { Gui }
    "agente" { Agente_Win }
    "remoto" { Agente_win_remoto }
    "crear" {crear_contenedores_locales}
    default { Write-Output "Usage: .\jade.ps1 <target> (compile, gui, agente)" }
}
