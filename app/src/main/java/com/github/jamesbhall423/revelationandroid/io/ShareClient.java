package com.github.jamesbhall423.revelationandroid.io;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

public class ShareClient {
    public static final JSONSerializer SERIALIZER = JSONSerializer.REVELATION_SERIALIZER;
    private static final int PORT = ShareServer.PORT;
    public static boolean publish(String serverIP, File file, String fileName) {
        try {
            InetAddress address = InetAddress.getAllByName(serverIP)[0];
            Socket socket = new Socket(address,PORT);
            RevelationOutputStream ostream = new RevelationOutputStream(socket.getOutputStream(),SERIALIZER,true);
            ostream.writeString(ShareServer.IDENTIFIER);
            ostream.writeString(fileName);
            ostream.writeCMap(CMap.read(file.getAbsolutePath(), SERIALIZER));
            socket.close();
            return true;
        } catch (IOException | IllegalAccessException e) {

        }
        return false;
    }
}