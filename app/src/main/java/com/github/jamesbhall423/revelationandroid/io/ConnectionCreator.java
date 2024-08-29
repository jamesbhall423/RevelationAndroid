package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.SelfBuffer;

import java.io.*;
import java.net.*;

public class ConnectionCreator {
    private static final int PORT = 4111;
    public static BoxModel createHost(String gameFile) throws IOException {
        CMap map = CMap.read(CMap.expand(gameFile));
        ServerSocket welcomeSocket = new ServerSocket(PORT);
        Socket hostSocket = welcomeSocket.accept();
        Socket clientSocket = new Socket(hostSocket.getInetAddress(),PORT);
        InetBuffer iBuffer = new InetBuffer(hostSocket);
        InetReceiver iReceiver = new InetReceiver(clientSocket);
        iBuffer.ostream.writeObject(map);
        welcomeSocket.close();
        return createBoxModel(map,iBuffer,iReceiver,0);
    }
    public static BoxModel createClient(String hostIP) throws IOException, ClassNotFoundException {
        InetAddress address = InetAddress.getAllByName(hostIP)[0];
        ServerSocket welcomeSocket = new ServerSocket(PORT);
        Socket hostSocket = new Socket(address,PORT);
        Socket clientSocket = welcomeSocket.accept();
        InetReceiver iReceiver = new InetReceiver(hostSocket);
        InetBuffer iBuffer = new InetBuffer(clientSocket);
        CMap map=(CMap)iReceiver.istream.readObject();
        welcomeSocket.close();
        return createBoxModel(map,iBuffer,iReceiver,1);
    }
    private static BoxModel createBoxModel(CMap map, InetBuffer iBuffer, InetReceiver iReceiver, int player) {
        SelfBuffer[] buffers = new SelfBuffer[map.players.length];
        buffers[1-player]=new SelfBuffer();
        buffers[player]=iBuffer;
        SelfBuffer.setLinks(buffers);
        new Thread(iReceiver).start();
        return new BoxModel(map,player,buffers[player]);
    }
}
