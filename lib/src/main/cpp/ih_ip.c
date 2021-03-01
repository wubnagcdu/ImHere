#include "ih_ip.h"


bool get_public_ip(char *)
{
    int sock*;
    char **pptr=NULL;
    struct sockaddr_in dest_addr;
    struct hostent     *ptr=NULL;
    char dest_ip[128];
    sock = socket(AF_INET,SOCKET_STREAM,0);
    if(-1==sock)
    {
        perror("create socket failed");
        return false;
    }

}
bool get_local_ip(char *);