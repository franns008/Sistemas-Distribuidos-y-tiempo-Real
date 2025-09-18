#!/bin/bash

set -euo pipefail

cd /codigos || cd "$(dirname "$0")"

echo -e "\e[1;31m=============================="
echo -e "   ¡EL SCRIPT DEL SERVIDOR SE EJECUTÓ!"
echo -e "==============================\e[0m"
echo -e "\e[1;32m🎉 FELICIDADES SOS UN SERVIDOR 🎉\e[0m"

# =====================
# Detección robusta de IP evitando IPs de NAT de Vagrant
# Reglas:
# 1. Si SERVER_IP (env) está seteada y es válida -> usar.
# 2. Si primer argumento es IPv4 válida -> usar.
# 3. Si SERVER_IFACE está seteada -> tomar IP de esa interfaz.
# 4. Si SERVER_NET (prefijo, ej 192.168.0.) está seteado -> priorizar IP que matchee.
# 5. Excluir explícitamente: 10.0.2.15 (NAT default), 127.0.0.1, 169.254.* (link-local).
# 6. Preferir interfaces típicas de bridge/host-only: enp0s8, eth1, eth2, enp0s9.
# 7. Orden de prefijos privados: 192.168.* luego 172.16-31.* luego 10.* (resto de 10.* excepto 10.0.2.15).
# 8. Fallback: IP de la ruta por defecto, luego 127.0.0.1.
# Variable opcional DEBUG_IP=1 para ver diagnóstico.

is_ipv4() { echo "$1" | grep -Eq '^[0-9]{1,3}(\.[0-9]{1,3}){3}$'; }

ARG_IP="${1:-}"
ARG_PORT="${2:-}"

# Puerto
SERVER_PORT="${SERVER_PORT:-${ARG_PORT:-5000}}"

if [ -z "${SERVER_IP:-}" ] && is_ipv4 "$ARG_IP"; then
  SERVER_IP="$ARG_IP"
fi

if [ -z "${SERVER_IP:-}" ]; then
  # SERVER_IFACE forzado
  if [ -n "${SERVER_IFACE:-}" ]; then
    CAND=$(ip -o -4 addr show dev "$SERVER_IFACE" 2>/dev/null | awk '{print $4}' | cut -d/ -f1 | head -n1 || true)
    if [ -n "$CAND" ]; then SERVER_IP="$CAND"; fi
  fi
fi

if [ -z "${SERVER_IP:-}" ]; then
  # Listar (iface ip)
  mapfile -t LINES < <(ip -o -4 addr show | awk '{print $2, $4}')
  [ "${DEBUG_IP:-}" = "1" ] && {
    echo "[IP][DEBUG] Interfaces detectadas:" >&2
    printf '  %s\n' "${LINES[@]}" >&2
  }
  # Filtrar y construir lista de candidatos
  CANDIDATES=()
  for line in "${LINES[@]}"; do
    iface=${line%% *}
    ipcidr=${line#* }
    ip=${ipcidr%%/*}
    # Excluir loopback, link-local, NAT vagrant default
    if [[ $iface == lo ]] || [[ $ip == 127.* ]] || [[ $ip == 10.0.2.15 ]] || [[ $ip == 169.254.* ]]; then
      continue
    fi
    # Opcional: si SERVER_NET está definido, solo acepto las que matchean prefijo
    if [ -n "${SERVER_NET:-}" ] && [[ $ip != ${SERVER_NET}* ]]; then
      continue
    fi
    CANDIDATES+=("$iface:$ip")
  done

  [ "${DEBUG_IP:-}" = "1" ] && {
    echo "[IP][DEBUG] Candidatos tras filtro:" >&2
    printf '  %s\n' "${CANDIDATES[@]}" >&2
  }

  pick_from_candidates() {
    local pattern iface ip entry
    local preferred_ifaces="enp0s8 eth1 eth2 enp0s9"
    # 1. Preferir interfaces conocidas
    for iface in $preferred_ifaces; do
      for entry in "${CANDIDATES[@]}"; do
        [[ $entry == $iface:* ]] || continue
        ip=${entry#*:}
        echo "$ip"; return 0
      done
    done
    # 2. Por rangos privados ordenados
    for pattern in '^192\.168\.' '^172\.(1[6-9]|2[0-9]|3[0-1])\.' '^10\.'; do
      for entry in "${CANDIDATES[@]}"; do
        ip=${entry#*:}
        if echo "$ip" | grep -Eq "$pattern"; then
          echo "$ip"; return 0
        fi
      done
    done
    return 1
  }

  SEL=$(pick_from_candidates || true)
  if [ -n "$SEL" ]; then
    SERVER_IP="$SEL"
  fi
fi

if [ -z "${SERVER_IP:-}" ]; then
  SERVER_IP=$(ip -o route get 1.1.1.1 2>/dev/null | awk '{for(i=1;i<=NF;i++) if($i=="src") {print $(i+1); exit}}') || true
fi

[ -z "${SERVER_IP:-}" ] && SERVER_IP=127.0.0.1

echo "[IP] Usando IP del servidor: $SERVER_IP"
if [ "${DEBUG_IP:-}" = "1" ]; then
  ip -o -4 addr show | sed 's/^/[IP][DEBUG] /'
fi

echo "$SERVER_IP" > /codigos/server_ip
echo "$SERVER_PORT" > /codigos/server_port
echo "Server escuchando en ${SERVER_IP}:${SERVER_PORT} (guardado en /codigos/server_ip y /codigos/server_port)"

# Ejecutar el servidor con classpath actual
if [ -f Server.class ]; then
	echo -e "\n"
	echo -e "======= Se ejecuta el test ======="
  echo -e "El servidor se está ejecutando. Salida en resultadoServidor.txt"
	echo -e "======= Se ejecuta el test para el ejercio 3 =======" >> resultadoServidor.txt
	echo -e "\n" >> resultadoServidor.txt
	java -cp . Server "$SERVER_PORT" >> resultadoServidor.txt 
else
	echo "No se encontró Server.class en $(pwd). Contenido:" >&2
	ls -l >&2
	exit 1
fi