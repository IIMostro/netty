package org.ilmostro.buffer;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import org.junit.jupiter.api.Test;

/**
 * @author li.bowei
 */
public class ByteBufTests {


	@Test
	void test(){
		final ByteBuf buffer = ByteBufAllocator.DEFAULT.directBuffer(6, 10);
		printByteBufInfo("ByteBufAllocator.DEFAULT.directBuffer(6, 10)", buffer);
	}

	private static void printByteBufInfo(String step, ByteBuf buffer) {
		System.out.println("------" + step + "-----");
		System.out.println("readerIndex(): " + buffer.readerIndex());
		System.out.println("writerIndex(): " + buffer.writerIndex());
		System.out.println("isReadable(): " + buffer.isReadable());
		System.out.println("isWritable(): " + buffer.isWritable());
		System.out.println("readableBytes(): " + buffer.readableBytes());
		System.out.println("writableBytes(): " + buffer.writableBytes());
		System.out.println("maxWritableBytes(): " + buffer.maxWritableBytes());
		System.out.println("capacity(): " + buffer.capacity());
		System.out.println("maxCapacity(): " + buffer.maxCapacity());

	}
}
