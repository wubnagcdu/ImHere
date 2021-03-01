#include "ih_udp.h"


#define  RECV_SIZE          512
#define  DEF_PORT           27350

int  *sock_fd=0x0;

int init_udp()
{
    int sockfd=socket(AF_INET,SOCK_DGRAM,0);
    struct sockaddr_in addr;
    addr.sin_family =AF_INET;
    addr.sin_port =htons(1324);
    addr.sin_addr.s_addr=inet_addr("127.0.0.1");

    int ret =bind(sockfd,(struct sockaddr*)&addr,sizeof(addr));
    if(0>ret)
    {
        printf("bind\n");
        return -1;
    }
    struct sockaddr_in cli;
    socklen_t len=sizeof(cli);
    while(1)
    {
        char buf =0;
        recvfrom(sockfd,&buf,sizeof(buf),0,(struct sockaddr*)&cli,&len);
        printf("recv num =%hhd\n",buf);
        buf =66;
        sendto(sockfd,&buf,sizeof(buf),0,(struct sockaddr*)&cli,len);
    }
    close(sockfd);
}

int send_udp(char *send)
{
}

int recv_udp(char *recv)
{
}