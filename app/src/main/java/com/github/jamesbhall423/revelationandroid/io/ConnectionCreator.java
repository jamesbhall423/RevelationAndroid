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
        Socket socket = welcomeSocket.accept();
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream istream = new ObjectInputStream(socket.getInputStream());
//        Socket clientSocket = new Socket(hostSocket.getInetAddress(),PORT);
//        InetBuffer iBuffer = new InetBuffer(clientSocket);
//        InetReceiver iReceiver = new InetReceiver(hostSocket);
        ostream.writeObject(map);
        welcomeSocket.close();
//        return createBoxModel(map,iBuffer,iReceiver,0);
        return new BoxModel(map,0,new InetBuffer2(istream,ostream));
    }
    public static BoxModel createClient(String hostIP) throws IOException, ClassNotFoundException {
        InetAddress address = InetAddress.getAllByName(hostIP)[0];
//        ServerSocket welcomeSocket = new ServerSocket(PORT);
        Socket socket = new Socket(address,PORT);
//        Socket clientSocket = welcomeSocket.accept();
        ObjectInputStream istream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
//        InetReceiver iReceiver = new InetReceiver(clientSocket);
//        InetBuffer iBuffer = new InetBuffer(hostSocket);
        CMap map=(CMap)istream.readObject();
//        welcomeSocket.close();
//        return createBoxModel(map,iBuffer,iReceiver,1);
        return new BoxModel(map,1,new InetBuffer2(istream,ostream));
    }
    private static BoxModel createBoxModel(CMap map, InetBuffer iBuffer, InetReceiver iReceiver, int player) {

        SelfBuffer[] buffers = new SelfBuffer[map.players.length];
        buffers[player]=new SelfBuffer();
        buffers[1-player]=iBuffer;
        iReceiver.buffer = buffers[player];
        SelfBuffer.setLinks(buffers);
        new Thread(iReceiver).start();
        return new BoxModel(map,player,buffers[player]);
    }
}
