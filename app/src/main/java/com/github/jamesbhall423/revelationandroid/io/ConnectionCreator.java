package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.serialization.JSONSerializer;

import java.io.*;
import java.net.*;

public class ConnectionCreator {
    public static final JSONSerializer SERIALIZER = JSONSerializer.getRevelationSerializer();
    private static final int PORT = 4111;
    public static BoxModel createHost(String gameFile) throws IOException, IllegalAccessException {
        CMap map = CMap.read(gameFile,SERIALIZER);
        ServerSocket welcomeSocket = new ServerSocket(PORT);
        Socket socket = welcomeSocket.accept();
        RevelationOutputStream ostream = new RevelationOutputStream(socket.getOutputStream(),SERIALIZER);
        RevelationInputStream istream = new RevelationInputStream(socket.getInputStream(),SERIALIZER);
        ostream.writeCMap(map);
        int playerNum = istream.readInt();
        welcomeSocket.close();
        return new BoxModel(map,playerNum,new InetBuffer(istream,ostream,socket));
    }
    public static BoxModel createClient(String hostIP, int playerNum) throws IOException, IllegalAccessException {
        InetAddress address = InetAddress.getAllByName(hostIP)[0];
        Socket socket = new Socket(address,PORT);
        RevelationInputStream istream = new RevelationInputStream(socket.getInputStream(),SERIALIZER);
        RevelationOutputStream ostream = new RevelationOutputStream(socket.getOutputStream(),SERIALIZER);
        CMap map=istream.readCMap();
        validateCMap(map);
        ostream.writeInt(1-playerNum);
        return new BoxModel(map,playerNum,new InetBuffer(istream,ostream,socket));
    }
    public static void validateCMap(CMap cmap) {
        try {
            String json = SERIALIZER.serializeObject(cmap);
            CMap result = SERIALIZER.deserializeCMap(json);
            String newJson = SERIALIZER.serializeObject(result);
            System.out.println("CMap valid: "+json.equals(newJson));
        } catch (IllegalAccessException e) {
            System.out.println(e.getMessage());
        }
    }
}
