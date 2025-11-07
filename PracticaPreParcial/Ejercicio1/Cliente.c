#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>  // Para strlen y otras funciones de cadenas
#include <unistd.h>  // Para read, write y otras funciones de E/S
#include <stdlib.h> 

int main(){
    printf("Hola, soy el cliente!\n");
    
    socket = Socket(AF_INET,SOCK_STREAM)
    connect(socket,dir_server,sizeof(dir_server))
    char bufferEnvio[256];
    char bufferResp[256];
    buffer = "ping"
    write(socket,bufferEnvio,256)
    read(socket,bufferResp,256)
    printf(bufferResp)
    close(socket)

    return 0;
}