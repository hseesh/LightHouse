package com.hse.yatoufang.appUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;

public class SocketServiceUtil {

	StreamData sreamData;

	public SocketServiceUtil(int requestCode, String content) {
		sreamData = new StreamData(requestCode, content);
	}

	public SocketServiceUtil(int requestCode, String content,String addtionalData) {
		sreamData = new StreamData(requestCode, content, addtionalData);
	}
	
	public SocketServiceUtil(int requestCode) {
		sreamData = new StreamData(requestCode);
	}

	public SocketServiceUtil(String content) {
		String[] str = content.split("==");
		sreamData = new StreamData(Integer.valueOf(str[0]), str[1]);
	}

	public byte[] getStreambBytes() throws IOException {
		ByteArrayOutputStream byteStream = new ByteArrayOutputStream();
		ObjectOutputStream outStream = new ObjectOutputStream(byteStream);
		outStream.writeObject(sreamData);
		byte[] data = byteStream.toByteArray();
		byte[] length = intToBytes(data.length);
		byte[] buffer = new byte[data.length + 4];
		System.arraycopy(length, 0, buffer, 0, length.length);
		System.arraycopy(data, 0, buffer, length.length, data.length);
		return buffer;
	}

	private byte[] intToBytes(int value) {// int is 32 bit , per 8 bit 1 byte
		byte[] src = new byte[4];
		src[3] = (byte) ((value >> 24) & 0xFF);
		src[2] = (byte) ((value >> 16) & 0xFF);
		src[1] = (byte) ((value >> 8) & 0xFF);
		src[0] = (byte) (value & 0xFF);
		return src;
	}

}
