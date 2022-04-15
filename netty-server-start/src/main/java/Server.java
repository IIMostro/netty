import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.ChannelMatcher;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.codec.http.HttpObjectAggregator;
import io.netty.handler.codec.http.HttpServerCodec;
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketCloseStatus;
import io.netty.handler.codec.http.websocketx.WebSocketFrame;
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler;
import io.netty.handler.codec.http.websocketx.extensions.compression.WebSocketServerCompressionHandler;
import io.netty.handler.stream.ChunkedWriteHandler;
import io.netty.util.concurrent.GlobalEventExecutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author li.bowei
 */
public class Server extends SimpleChannelInboundHandler<WebSocketFrame>{

    private static final Logger logger = LoggerFactory.getLogger(Server.class);
    private static final ChannelGroup channels = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);

    public static void main(String[] args) throws InterruptedException {
        EventLoopGroup bootstrapGroup = new NioEventLoopGroup();
        EventLoopGroup workGroup = new NioEventLoopGroup();
        try {
            ServerBootstrap serverBootstrap = new ServerBootstrap();
            final Server server = new Server();
            serverBootstrap.group(bootstrapGroup, workGroup);
            serverBootstrap.channel(NioServerSocketChannel.class);
            serverBootstrap.localAddress("0.0.0.0", 8081);
            serverBootstrap.childHandler(new ChannelInitializer<SocketChannel>(){
                @Override
                protected void initChannel(SocketChannel ch) {
                    ChannelPipeline pipeline = ch.pipeline();
                    pipeline.addLast(new HttpServerCodec());
                    pipeline.addLast(new ChunkedWriteHandler());
                    pipeline.addLast(new HttpObjectAggregator(65536));
                    pipeline.addLast(new WebSocketServerCompressionHandler());
                    pipeline.addLast(new WebSocketServerProtocolHandler("/channel", null, true, 65536 * 2));
                    pipeline.addLast(server);
                }
            });
            Channel channel = serverBootstrap.bind().sync().channel();
            logger.info("websocket 服务启动，ip={},port={}", "0.0.0.0", 8081);
            channel.closeFuture().sync();
        } finally {
            bootstrapGroup.shutdownGracefully();
            workGroup.shutdownGracefully();
        }
    }

    @Override
    protected void channelRead0(ChannelHandlerContext ctx, WebSocketFrame msg) throws Exception {
        if (msg instanceof TextWebSocketFrame) {
            // 业务层处理数据
            logger.info("path:{}监听到信息:{}", ctx.channel().remoteAddress(), msg);
            // 响应客户端
            for (Channel channel : channels) {
                channel.writeAndFlush(new TextWebSocketFrame("我收到了你的消息：" + ((TextWebSocketFrame) msg).text() + System.currentTimeMillis()));
            }
        } else {
            // 不接受文本以外的数据帧类型
            ctx.channel().writeAndFlush(WebSocketCloseStatus.INVALID_MESSAGE_TYPE).addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        super.channelInactive(ctx);
        logger.info("连接断开:{}", ctx.channel().remoteAddress());
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        final Channel currentChannel = ctx.channel();
        logger.info("建立连接:{}", currentChannel.remoteAddress());
        channels.writeAndFlush(new TextWebSocketFrame("欢迎:" + currentChannel.id()), new ChannelMatcher(){
            @Override
            public boolean matches(Channel channel) {
                return !channel.equals(currentChannel);
            }
        });
        channels.add(currentChannel);
    }
}
