package com.lelann.auth.utils;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.util.UUID;

public class ByteOutputStream extends OutputStream {
	private final OutputStream out;
	
	public ByteOutputStream(OutputStream out){
		this.out = out;
	}
	
	public void writeByte(byte value) throws IOException {
		out.write((byte) value);
	}
	
	public void writeUnsignedByte(int value) throws IOException {
		writeByte((byte) value);
	}
	
	public void writeBytes(byte... values) throws IOException {
		for(byte value : values)
			writeByte(value);
	}
	
	public void writeBytes(Byte... values) throws IOException {
		for(byte value : values)
			writeByte(value);
	}
	
	public void writeShort(short value) throws IOException {
		writeByte((byte) (value >>> 8));
		writeByte((byte) value);
	}
	
	public void writeUnsignedShort(int value) throws IOException {
		writeShort((short)(value & 0xFF));
	}
	
	public void writeInt(int value) throws IOException {
		int part;
        while(true) {
            part = value & 0x7F;

            value >>>= 7;
            if(value != 0){
                part |= 0x80;
            }

            writeByte((byte) part);

            if(value == 0) {
                break;
            }
        }
	}
	
	public int intLength(int value) throws IOException {
		int result = 0;
        while(true) {
            value >>>= 7;
            result++;
            
            if(value == 0) {
                break;
            }
        }
        
        return result;
	}
	
	public void writeIntComplete(int value) throws IOException {
		writeBytes(_writeBigNumber(value, 4));
	}
	
	public void writeLong(long value) throws IOException {
		writeBytes(_writeBigNumber(value, 8));
	}
	
	public void writeFloat(float value) throws IOException {
		writeInt(Float.floatToIntBits(value));
	}
	
	public void writeDouble(double value) throws IOException {
		writeLong(Double.doubleToLongBits(value));
	}
	
	public void writeBoolean(boolean value) throws IOException {
		writeByte(value ? (byte) 1 : (byte) 0);
	}
	
	public void writeUUID(UUID uniqueId) throws IOException {
		writeLong(uniqueId.getMostSignificantBits());
		writeLong(uniqueId.getLeastSignificantBits());
	}
	
	public void writeUTF(String value) throws IOException {
		byte[] bytes = value.getBytes(Charset.forName("utf-8"));
		
		writeInt(bytes.length);
		writeBytes(bytes);
	}
	
	public void writeArrayUTF(String[] value) throws IOException {
		writeInt(value.length);
		for(String str : value){
			writeUTF(str);
		}
	}
	
	private static byte[] _writeBigNumber(long value, int bytes){
		byte[] result = new byte[bytes];
		
		for(int i=0;i<bytes;i++)
			result[bytes - i - 1] = (byte)((value >> 8 * i) & 0xFF);
		
		return result;
	}
	
	@Override
	public void write(int b) throws IOException {
		out.write(b);
	}
}
