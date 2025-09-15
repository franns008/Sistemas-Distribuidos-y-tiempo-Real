#!/bin/bash

set -euo pipefail

cd /codigos || cd "$(dirname "$0")"

echo -e "\e[1;31m=============================="
echo -e "   ¡EL SCRIPT DEL CLIENTE SE EJECUTÓ!"
echo -e "==============================\e[0m"

ls -l

echo -e "\e[2;31m🎉 FELICIDADES SOS UN CLIENTE🎉\e[0m"
echo -e "\e[2;31m ahora podes mandar cosas \e[0m"

# Obtener IP/puerto desde args o archivos compartidos
SERVER_IP=${1:-${SERVER_IP:-}}
SERVER_PORT=${2:-${SERVER_PORT:-}}
if [ -z "$SERVER_IP" ] || [ -z "$SERVER_PORT" ]; then
	echo "Esperando IP/puerto del servidor..."
	for i in $(seq 1 60); do
		if [ -f /codigos/server_ip ] && [ -f /codigos/server_port ]; then
			SERVER_IP=$(cat /codigos/server_ip)
			SERVER_PORT=$(cat /codigos/server_port)
			break
		fi
		sleep 1
	done
fi

if [ -z "$SERVER_IP" ] || [ -z "$SERVER_PORT" ]; then
	echo "No se pudo obtener IP/puerto del servidor. Abortando." >&2
	exit 1
fi
echo "Conectando a ${SERVER_IP}:${SERVER_PORT}"

# Ejecutar el cliente con classpath actual; cuidado con el nombre/clase exacta
if [ -f Client.class ]; then
	echo -e "\n"
	echo -e "======= Se ejecuta el test para el ejercio 3 ======= "
	echo -e "======= Se ejecuta el test para el ejercio 3 ======= " >> resultadoCliente.txt
	echo -e "\n" >> resultadoCliente.txt
	java -cp . Client "$SERVER_IP" "$SERVER_PORT" >> resultadoCliente.txt
else
	echo "No se encontró Client.class en $(pwd)." >&2
	exit 1
fi
