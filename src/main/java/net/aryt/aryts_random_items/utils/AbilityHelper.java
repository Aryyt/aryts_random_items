package net.aryt.aryts_random_items.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.concurrent.CopyOnWriteArrayList;

public class AbilityHelper {
    private static CopyOnWriteArrayList<Handler> Handlers = new CopyOnWriteArrayList<Handler>();

    public static void add(Handler handler){
        if (!Handlers.contains(handler)){
            Handlers.add(handler);
        }
    }
    public static void remove(Handler handler){
        Handlers.remove(handler);
    }
    public static Handler getByUUID(Player pPlayer){
        for (Handler h: Handlers) {
            if(h.getPlayer().getUUID() == pPlayer.getUUID()){
                return h;
            }
        }
        return null;
    }
}
