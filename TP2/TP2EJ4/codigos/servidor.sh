#!/bin/bash

set -euo pipefail

cd /codigos || cd "$(dirname "$0")"

echo -e "\e[1;31m=============================="
echo -e "   ¡EL SCRIPT DEL SERVIDOR SE EJECUTÓ!"
echo -e "==============================\e[0m"

ls -l

echo -e "\e[1;32m🎉 FELICIDADES SOS UN SERVIDOR 🎉\e[0m"

# =====================
# Detección simple de IP (un solo entorno/interfaz esperada)
# Reglas mínimas:
# 1. Si defino SERVER_IP (env) la uso.
# 2. Si paso primer argumento y es IPv4 válida, la uso.
# 3. Si existe interfaz adicional (eth1/enp0s8/ens33/etc) con IP privada 192.168/172.16-31/10.x la tomo en ese orden.
# 4. Si no, uso la IP de la ruta por defecto.
# 5. Fallback: 127.0.0.1

is_ipv4() { echo "$1" | grep -Eq '^[0-9]{1,3}(\.[0-9]{1,3}){3}$'; }

ARG_IP="${1:-}"
ARG_PORT="${2:-}"

# Puerto
SERVER_PORT="${SERVER_PORT:-${ARG_PORT:-5000}}"

if [ -z "${SERVER_IP:-}" ] && is_ipv4 "$ARG_IP"; then
  SERVER_IP="$ARG_IP"
fi

if [ -z "${SERVER_IP:-}" ]; then
  # Capturo todas las IPv4 no loopback
  mapfile -t IPS < <(ip -o -4 addr show | awk '$2!~"lo" {print $4}' | cut -d/ -f1)
  # Prioridad por patrones
  pick_ip() {
    for pat in '^192\.168\.' '^172\.(1[6-9]|2[0-9]|3[0-1])\.' '^10\.'; do
      for ip in "${IPS[@]}"; do
        if echo "$ip" | grep -Eq "$pat"; then
          echo "$ip"; return 0
        fi
      done
    done
    return 1
  }
  SEL=$(pick_ip || true)
  if [ -n "$SEL" ]; then
    SERVER_IP="$SEL"
  fi
fi

if [ -z "${SERVER_IP:-}" ]; then
  SERVER_IP=$(ip -o route get 1.1.1.1 2>/dev/null | awk '{for(i=1;i<=NF;i++) if($i=="src") {print $(i+1); exit}}') || true
fi

[ -z "${SERVER_IP:-}" ] && SERVER_IP=127.0.0.1

echo "[IP] Usando IP del servidor: $SERVER_IP"

echo "$SERVER_IP" > /codigos/server_ip
echo "$SERVER_PORT" > /codigos/server_port
echo "Server escuchando en ${SERVER_IP}:${SERVER_PORT} (guardado en /codigos/server_ip y /codigos/server_port)"

# Ejecutar el servidor con classpath actual
if [ -f Server.class ]; then
	echo -e "\n"
	echo -e "======= Se ejecuta el test para el ejercio 3 ======="
	echo -e "======= Se ejecuta el test para el ejercio 3 =======" >> resultadoServidor.txt
	echo -e "\n" >> resultadoServidor.txt
	java -cp . Server "$SERVER_PORT" >> resultadoServidor.txt 
else
	echo "No se encontró Server.class en $(pwd). Contenido:" >&2
	ls -l >&2
	exit 1
fi