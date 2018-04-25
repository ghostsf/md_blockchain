package com.mindata.blockchain.socket.client;

import com.mindata.blockchain.ApplicationContextProvider;
import com.mindata.blockchain.core.queue.base.BaseEvent;
import com.mindata.blockchain.core.queue.base.MessageProducer;
import com.mindata.blockchain.socket.base.AbstractAioHandler;
import com.mindata.blockchain.socket.packet.BlockPacket;
import com.mindata.blockchain.socket.packet.NextBlockPacketBuilder;
import org.tio.client.intf.ClientAioHandler;
import org.tio.core.ChannelContext;
import org.tio.core.intf.Packet;

/**
 * @author wuweifeng wrote on 2018/3/12.
 */
public class BlockClientAioHandler extends AbstractAioHandler implements ClientAioHandler {
    //private static Map<Byte, AbstractBlockHandler<?>> handlerMap = new HashMap<>();
    //private Logger logger = LoggerFactory.getLogger(getClass());
    //
    //static {
    //    handlerMap.put(PacketType.GENERATE_BLOCK_RESPONSE, new GenerateBlockResponseHandler());
    //    handlerMap.put(PacketType.TOTAL_BLOCK_INFO_RESPONSE, new TotalBlockInfoResponseHandler());
    //    handlerMap.put(PacketType.LAST_BLOCK_INFO_RESPONSE, new LastBlockInfoResponseHandler());
    //    handlerMap.put(PacketType.NEXT_BLOCK_INFO_RESPONSE, new NextBlockResponseHandler());
    //    handlerMap.put(PacketType.GENERATE_COMPLETE_RESPONSE, new GenerateCompleteResponseHandler());
    //}

    @Override
    public BlockPacket heartbeatPacket() {
        //心跳包的内容就是隔一段时间向别的节点获取一次下一步区块（带着自己的最新Block获取别人的next Block）
        return NextBlockPacketBuilder.build();
    }

    /**
     * server端返回的响应会先进到该方法，先做统一的预处理，然后再分发给对应的handler处理各自的<p>
     * 这里是所有server端给响应的入口，会有性能瓶颈，我猜的，我要用Disruptor来解决性能问题
     */
    @Override
    public void handler(Packet packet, ChannelContext channelContext) throws Exception {
        BlockPacket blockPacket = (BlockPacket) packet;

        //使用Disruptor来publish消息，进入队列
        ApplicationContextProvider.getBean(MessageProducer.class).publish(new BaseEvent(blockPacket, channelContext));

        //BaseBody baseBody = Json.toBean(new String(blockPacket.getBody()), BaseBody.class);
        //logger.info("收到来自于<" + baseBody.getAppId() + ">针对msg<" + baseBody.getResponseMsgId() + ">的回应");
        //
        //String appId = baseBody.getAppId();
        //if (StrUtil.equals(AppId.value, appId)) {
        //    //是本机
        //    //return;
        //}
        //String msgId = baseBody.getResponseMsgId();
        //List<BaseResponse> responseList = RequestResponseMap.get(msgId);
        ////如果回应的消息msgId key自己不曾保存过，或者已被删除，说明针对该消息已有结果，则不再处理
        //if (responseList == null) {
        //    return;
        //}
        //
        //Byte type = blockPacket.getType();
        //AbstractBlockHandler<?> blockHandler = handlerMap.get(type);
        //blockHandler.handler(blockPacket, channelContext);
    }
}
