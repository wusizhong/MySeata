public class TxManagerMain {
    public static void main(String[] args) {
        //启动netty服务
        NettyServer nettyServer = new NettyServer();
        nettyServer.start("localhost", 8080);
        System.out.println("netty server start");
    }
}
