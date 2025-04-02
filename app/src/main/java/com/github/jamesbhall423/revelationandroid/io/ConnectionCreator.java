package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

import java.io.*;
import java.net.*;

public class ConnectionCreator {
    public static final String IDENTIFIER = "com.github.jamesbhall423.revelationandroid.io.ConnectionCreator";
    public static final JSONSerializer SERIALIZER = JSONSerializer.REVELATION_SERIALIZER;
    private static final int PORT = 4111;
    public static BoxModel createHost(String gameFile) throws IOException, IllegalAccessException {
        CMap map = CMap.read(gameFile,SERIALIZER);
        ServerSocket welcomeSocket = new ServerSocket(PORT);
        Socket socket = welcomeSocket.accept();
        RevelationOutputStream ostream = new RevelationOutputStream(socket.getOutputStream(),SERIALIZER,true);
        RevelationInputStream istream = new RevelationInputStream(socket.getInputStream(),SERIALIZER);
        if (!istream.readString().equals(IDENTIFIER)) {
            welcomeSocket.close();
            socket.close();
            throw new IOException("Not Connection Creater");
        }
        ostream.writeCMap(map);
        int playerNum = istream.readInt();
        welcomeSocket.close();
        return new BoxModel(map,playerNum,new InetBuffer(istream,ostream,socket));
    }
    public static BoxModel createClient(String hostIP, int playerNum) throws IOException, IllegalAccessException {
        InetAddress address = InetAddress.getAllByName(hostIP)[0];
        Socket socket = new Socket(address,PORT);
        RevelationInputStream istream = new RevelationInputStream(socket.getInputStream(),SERIALIZER);
        RevelationOutputStream ostream = new RevelationOutputStream(socket.getOutputStream(),SERIALIZER,true);
        ostream.writeString(IDENTIFIER);
        CMap map=istream.readCMap();
        ostream.writeInt(1-playerNum);
        return new BoxModel(map,playerNum,new InetBuffer(istream,ostream,socket));
    }
}
