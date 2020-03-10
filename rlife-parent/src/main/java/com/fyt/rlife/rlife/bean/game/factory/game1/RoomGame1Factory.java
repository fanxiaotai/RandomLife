package com.fyt.rlife.rlife.bean.game.factory.game1;

import com.fyt.rlife.rlife.bean.game.common.Room;
import com.fyt.rlife.rlife.bean.room.SwordRoom;
import com.fyt.rlife.rlife.bean.room.TreatmentRoom;

/**
 * @Author: fanyitai
 * @Date: 2020/3/6 15:00
 * @Version 1.0
 */
public class RoomGame1Factory {

    public static Room getSwordRoom(){
        Room room = new SwordRoom();
        room.setId(1+"");
        room.setRoomName("刀剑房");
        return room;
    }

    public static Room getTreatmentRoom(){
        Room room = new TreatmentRoom();
        room.setId(2+"");
        room.setRoomName("回血房");
        return room;
    }
}
