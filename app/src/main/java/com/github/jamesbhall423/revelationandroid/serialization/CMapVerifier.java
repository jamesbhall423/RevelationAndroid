package com.github.jamesbhall423.revelationandroid.serialization;
import java.util.HashMap;
import java.util.Map;

import com.github.jamesbhall423.revelationandroid.model.CMap;

public class CMapVerifier {
    public static boolean verifyCMap(CMap map) {
        if (map.players==null||map.squares==null) return false;
        if (map.players.length!=2) return false;
        for (int i = 0; i < map.players.length; i++) {
            if (map.players[i]==null) return false;
            if (map.players[i].numReverts<0) return false;
            if (map.players[i].scans<0) return false;
        }
        if (map.players[0].side==map.players[1].side) return false;
        if (map.squares.length!=8) return false;
        Map<Integer, Integer> counts = new HashMap<>();
        for (int y = 0; y < map.squares.length; y++) {
            if (map.squares[y]==null || map.squares[y].length!=8) return false;
            for (int x = 0; x < map.squares[y].length; x++) {
                if (map.squares[y][x]==null) return false;
                if (map.squares[y][x].road<0) return false;
                if (map.squares[y][x].road>=16) return false;
                if (map.squares[y][x].type==null) return false;
                if (map.squares[y][x].contents>1) return false;
                if (map.squares[y][x].contents<-1) return false;
                if (map.squares[y][x].teleporter!=null&&map.squares[y][x].teleporter.length!=3) return false;
                if (map.squares[y][x].teleporter!=null) {
                    if (map.squares[y][x].teleporter[2]<0)return false;
                    if (map.squares[y][x].teleporter[2]>9)return false;
                    if (map.squares[y][x].teleporter[1]<0)return false;
                    if (map.squares[y][x].teleporter[1]>=8)return false;
                    if (map.squares[y][x].teleporter[0]<0)return false;
                    if (map.squares[y][x].teleporter[0]>=8)return false;
                    int xt = map.squares[y][x].teleporter[0];
                    int yt = map.squares[y][x].teleporter[1];
                    if (map.squares[yt][xt].teleporter==null) return false;
                    if (map.squares[yt][xt].teleporter.length!=3) return false;
                    if (map.squares[yt][xt].teleporter[2]!=map.squares[y][x].teleporter[2]) return false;
                    if (map.squares[yt][xt].teleporter[0]!=x) return false;
                    if (map.squares[yt][xt].teleporter[1]!=y) return false;
                    if (!counts.containsKey(map.squares[y][x].teleporter[2])) counts.put(map.squares[y][x].teleporter[2],1);
                    else counts.put(map.squares[y][x].teleporter[2],counts.get(map.squares[y][x].teleporter[2])+1);
                }
            }
        }
        for (int count: counts.values()) if (count!=2) return false; 
        return true;
    }
}
