package net.aryt.aryts_random_items.items.custom;

import net.aryt.aryts_random_items.utils.AbilityHelper;
import net.aryt.aryts_random_items.utils.Handler;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.AxeItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Tier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;

public class TestItem extends AxeItem {
    public TestItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()){
            BlockPos posBelow = pPlayer.getOnPos();
            Block bellowBlock = pLevel.getBlockState(posBelow).getBlock();
            if (bellowBlock == Blocks.DIRT){
                ArrayList<BlockPos> positions = new ArrayList<BlockPos>();
                positions.add(posBelow);
                Handler test = new Handler(positions,pPlayer);
                AbilityHelper.add(test);
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
