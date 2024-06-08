package net.aryt.aryts_random_items.utils;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.player.Player;

import java.util.ArrayList;

public class Handler {

    private ArrayList<BlockPos> blockPositions;
    private Player player;

    public Handler(ArrayList<BlockPos> blockPos, Player pPlayer){
        blockPositions = blockPos;
        player = pPlayer;
    }

    public Player getPlayer() {
        return player;
    }

    public ArrayList<BlockPos> getBlockPositions() {
        return blockPositions;
    }
}
