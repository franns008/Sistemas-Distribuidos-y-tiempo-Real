#include <stdio.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <netdb.h>
#include <string.h>  // Para strlen y otras funciones de cadenas
#include <unistd.h>  // Para read, write y otras funciones de E/S
#include <stdlib.h> 
int main(){

    socket = socket(AF_INET,SOCK_STREAM);
    
    bind(socket,"127.0.0.1:5000",sizeof("127.0.0.1:5000"))
    while (true)
    {
        listen(socket,5);
        conexion = accept()
    }
    
}