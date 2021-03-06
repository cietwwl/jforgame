package com.kingston.jforgame.net.socket.codec.reflect;

import org.apache.mina.core.buffer.IoBuffer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.kingston.jforgame.net.socket.codec.IMessageDecoder;
import com.kingston.jforgame.net.socket.codec.reflect.serializer.Serializer;
import com.kingston.jforgame.net.socket.message.Message;
import com.kingston.jforgame.net.socket.message.MessageFactory;

public class ReflectDecoder implements IMessageDecoder {

	private static Logger logger = LoggerFactory.getLogger(ReflectDecoder.class);

	@Override
	public Message readMessage(short module, short cmd, byte[] body) {
		IoBuffer in = IoBuffer.allocate(body.length);
		in.put(body);
		in.flip();
		Class<?> msgClazz = MessageFactory.INSTANCE.getMessage(module, cmd);
		try {
			Serializer messageCodec = Serializer.getSerializer(msgClazz);
			Message message = (Message) messageCodec.decode(in, msgClazz, null);
			return message;
		} catch (Exception e) {
			logger.error("读取消息出错,模块号{}，类型{},异常{}", new Object[]{module, cmd ,e});
		}
		return null;
	}

}
