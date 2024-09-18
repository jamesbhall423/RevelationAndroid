package com.github.jamesbhall423.revelationandroid.io;

import com.github.jamesbhall423.revelationandroid.model.BoxModel;
import com.github.jamesbhall423.revelationandroid.model.CMap;
import com.github.jamesbhall423.revelationandroid.model.SelfBuffer;

import java.io.*;
import java.net.*;

public class ConnectionCreator {
    private static final int PORT = 4111;
    public static BoxModel createHost(String gameFile) throws IOException {
        CMap map = CMap.read(gameFile);
        ServerSocket welcomeSocket = new ServerSocket(PORT);
        Socket socket = welcomeSocket.accept();
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        ObjectInputStream istream = new ObjectInputStream(socket.getInputStream());
        ostream.writeObject(map);
        int playerNum = istream.readInt();
        welcomeSocket.close();
        return new BoxModel(map,playerNum,new InetBuffer2(istream,ostream));
    }
    public static BoxModel createClient(String hostIP, int playerNum) throws IOException, ClassNotFoundException {
        InetAddress address = InetAddress.getAllByName(hostIP)[0];
        Socket socket = new Socket(address,PORT);
        ObjectInputStream istream = new ObjectInputStream(socket.getInputStream());
        ObjectOutputStream ostream = new ObjectOutputStream(socket.getOutputStream());
        CMap map=(CMap)istream.readObject();
        ostream.writeInt(1-playerNum);
        ostream.flush();
        return new BoxModel(map,playerNum,new InetBuffer2(istream,ostream));
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
