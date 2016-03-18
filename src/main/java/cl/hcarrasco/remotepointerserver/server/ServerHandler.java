package cl.hcarrasco.remotepointerserver.server;

import org.apache.log4j.Logger;

import cl.hcarrasco.remotepointerserver.messageactions.MessageActions;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ServerHandler extends ChannelInboundHandlerAdapter {
	
	final static Logger logger = Logger.getLogger(ServerHandler.class);

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel: "+ctx.channel().localAddress()+" is now ACTIVE to receive data");
	}
	
	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel: "+ctx.channel().localAddress()+" is now INACTIVE");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		
		int maxBufferMessage = 250;
        ByteBuf in = (ByteBuf) msg;
        byte[] data = new byte[maxBufferMessage];
        int i = 0;
        try {
            while (in.isReadable()) {
                data[i] = in.readByte();
                i++;
            }
        } catch (Exception e) {
        	logger.info("Error reading the message : "+e.getMessage());
        }
        finally { ReferenceCountUtil.release(msg); }
        String msgFromDevice = new String (data);
        msgFromDevice = msgFromDevice.trim();
        
        logger.info("New message received: "+msgFromDevice);
        
        MessageActions messageActions = new MessageActions();
        messageActions.hubMessages(msgFromDevice);
        
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.info("Error: "+cause.getMessage()+" reading channel "+ctx.channel().localAddress());
	}
    
}
