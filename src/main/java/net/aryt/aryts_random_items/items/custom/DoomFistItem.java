package net.aryt.aryts_random_items.items.custom;

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
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.client.model.obj.ObjMaterialLibrary;

public class DoomFistItem extends AxeItem {
    public DoomFistItem(Tier pTier, float pAttackDamageModifier, float pAttackSpeedModifier, Properties pProperties) {
        super(pTier, pAttackDamageModifier, pAttackSpeedModifier, pProperties);

    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        if (!pLevel.isClientSide()){
            BlockPos posBelow = pPlayer.getOnPos();
            if (pLevel.getBlockState(posBelow).getBlock() == Blocks.AIR){
                pPlayer.sendSystemMessage(Component.literal("IS IN AIR"));
                Vec3 end = new Vec3(0,-5,0);
                pPlayer.setDeltaMovement(end);
                pPlayer.hurtMarked = true;
                pPlayer.fallDistance = 0;
                pPlayer.sendSystemMessage(Component.literal("FELL"));
            }
            else{
                pPlayer.sendSystemMessage(Component.literal("IS ON GROUND"));

                Vec3 lookVec = pPlayer.getLookAngle();
                Vec3 end = lookVec.multiply(3.5,2,3.5);
                end = end.add(0,0.5,0);
                pPlayer.setDeltaMovement(end);
                pPlayer.hurtMarked = true;
                pPlayer.sendSystemMessage(Component.literal("LAUNCHED"));
            }
        }
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
