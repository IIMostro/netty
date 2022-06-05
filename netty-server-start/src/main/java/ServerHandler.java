import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author li.bowei
 */
public class ServerHandler implements ChannelInboundHandler {

	private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

	@Override
	public void handlerAdded(ChannelHandlerContext ctx) throws Exception {
		logger.info("handler:[{}] added", ctx.channel().id().toString());
	}

	@Override
	public void handlerRemoved(ChannelHandlerContext ctx) throws Exception {
		logger.info("handler:[{}] removed", ctx.channel().id().toString());
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel:[{}] registered", ctx.channel().id().toString());
	}

	@Override
	public void channelUnregistered(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel:[{}] unregistered", ctx.channel().id().toString());
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel:[{}] active", ctx.channel().id().toString());
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel:[{}] inactive", ctx.channel().id().toString());
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		logger.info("channel:[{}] read", ctx.channel().id().toString());
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		logger.info("channel:[{}] complete", ctx.channel().id().toString());
	}

	@Override
	public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {

	}

	@Override
	public void channelWritabilityChanged(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.info("handler:[{}] exception", ctx.channel().id().toString());
	}
}
