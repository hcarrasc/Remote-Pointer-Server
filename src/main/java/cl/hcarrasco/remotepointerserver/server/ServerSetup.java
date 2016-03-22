package cl.hcarrasco.remotepointerserver.server;

import org.apache.log4j.Logger;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.DelimiterBasedFrameDecoder;

public class ServerSetup implements Runnable{
	
	final static Logger logger = Logger.getLogger(ServerSetup.class);
	
	private EventLoopGroup bossGroup = new NioEventLoopGroup();
	private EventLoopGroup workerGroup = new NioEventLoopGroup();
	private ChannelFuture channelFuture ;
	private String serverStatusFlag = "off";
	private int port = 1235;
	
	public void run() {
		
		logger.info("TCP Server starting...");
		try {
		    ServerBootstrap b = new ServerBootstrap();
		    b.group(bossGroup, workerGroup)
            .channel(NioServerSocketChannel.class) 
            .childHandler(new ChannelInitializer<SocketChannel>() { 
            	
                public void initChannel(SocketChannel ch) throws Exception {
                	
                	logger.info("New Channel inbound "+ch.toString()+" .");
           	        byte[] HEX = {Byte.valueOf(String.valueOf(Integer.parseInt("3C", 16)))};
           	        ByteBuf delimiter = Unpooled.copiedBuffer(HEX);
                    ch.pipeline().addLast(new DelimiterBasedFrameDecoder(65*1024, delimiter));
                    ch.pipeline().addLast(new ServerHandler());
                }
            })
            .childOption(ChannelOption.SO_KEEPALIVE, true);		    
			channelFuture = b.bind(port).sync();
			logger.info("TCP Server started, listening at port "+getPort());
			channelFuture.channel().closeFuture().sync();
		} 
		catch (InterruptedException e) {
			e.printStackTrace();
		} 
		finally {
			workerGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
            logger.info("TCP Server stoped.");
		}	
	}
	
	public void killServer(){
		workerGroup.shutdownGracefully();
        bossGroup.shutdownGracefully();
        logger.info("TCP Server stoped.");
	}
	
	public String getServerStatusFlag() {
		return serverStatusFlag;
	}
	public void setServerStatusFlag(String serverStatusFlag) {
		this.serverStatusFlag = serverStatusFlag;
		logger.info("Server status flag changed to "+serverStatusFlag);
	}
	
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	
}
