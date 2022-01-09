package net.sorenon.mcxr.play;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteOrder;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class MCXRPojavCompat {
    public static long freeNativeBuffer() {
        try (RandomAccessFile file = new RandomAccessFile(new File("contextvm.dat"), "r"))
        {
            //Get file channel in read-only mode
            FileChannel fileChannel = file.getChannel();

            //Get direct byte buffer access using channel.map() operation
            MappedByteBuffer buffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, fileChannel.size());
            buffer.order(ByteOrder.nativeOrder());
            System.out.println("Buffer Direct Mode: " + buffer.isDirect());
            return buffer.getLong();
        }  catch (IOException e) {
            System.out.println(e);
        }
        return 0;
    }
}
