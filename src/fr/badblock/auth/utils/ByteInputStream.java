package fr.badblock.auth.utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

public class ByteInputStream extends InputStream {
	private final InputStream stream;

	public ByteInputStream(InputStream stream){
		this.stream = stream;
	}

	public byte readByte() throws IOException {
		return (byte) stream.read();
	}

	public int readUnsignedByte() throws IOException {
		return readByte() & 0xFF;
	}

	public byte[] readBytes(int length) throws IOException{
		byte[] result = new byte[length];

		for(int i=0;i<length;i++)
			result[i] = readByte();

		return result;
	}

	public int readShort() throws IOException {
        int low = readUnsignedShort();
        int high = 0;
        if ( ( low & 0x8000 ) != 0 )
        {
            low = low & 0x7FFF;
            high = readUnsignedByte();
        }
        
        return ( ( high & 0xFF ) << 15 ) | low;
	}
	
	public int readUnsignedShort() throws IOException {
		byte[] bytes = readBytes(2);
		short result = 0;
		
		for(int i=0;i<bytes.length;i++){
			result <<= 8;
			result |= (int)bytes[i] & 0xFF;
		}
		
		return result & 0xffff;
	}

	public int readInt() throws IOException {
		return readInt(5);
	}
	
	public int readInt(int maxBytes) throws IOException {
		int result = 0, bytes = 0;
        byte in;
        
        while(true) {
            in = readByte();
            result |= (in & 0x7F) << (bytes++ * 7);

            if(bytes > maxBytes) {
                throw new RuntimeException( "VarInt too big" );
            }
            
            if((in & 0x80) != 0x80) break;
        }
        
        return result;
	}

	public long readLong() throws IOException {
		byte[] bytes = readBytes(8);
		long result = 0;
		
		for(int i=0;i<bytes.length;i++){
			result <<= 8;
			result |= (int)bytes[i] & 0xFF;
		}
		
		return result;
	}

	public float readFloat() throws IOException {
		return Float.intBitsToFloat(readInt());
	}

	public double readDouble() throws IOException {
		return Double.longBitsToDouble(readLong());
	}

	public boolean readBoolean() throws IOException {
		return readByte() == (byte) 1;
	}

	public UUID readUUID() throws IOException {
		return new UUID(readLong(), readLong());
	}

	public String readUTF() throws IOException {
		int length = readInt();
		return new String(readBytes(length));
	}
	
	public String[] readArrayUTF() throws IOException {
		int length = readInt();
		String[] result = new String[length];
		
		for(int i=0;i<length;i++)
			result[i] = readUTF();
		
		return result;
	}
	
	public byte[] readBytesArray() throws IOException {
		byte[] result = new byte[readInt()];
		for(int i=0;i<result.length;i++){
			result[i] = readByte();
		}
		
		return result;
	}
	
	@Override
	public int read() throws IOException {
		return stream.read();
	}
}
