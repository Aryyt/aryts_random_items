package net.aryt.aryts_random_items.items.custom;

import net.aryt.aryts_random_items.utils.RayCast;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class VibraslapItem extends Item {
    public VibraslapItem(Properties pProperties) {
        super(pProperties);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level pLevel, Player pPlayer, InteractionHand pUsedHand) {
        double length = 50;
        if(!pLevel.isClientSide()){
            Entity receiver = RayCast.rayTraceEyes(pPlayer, length, false);

            if (receiver != null){
                Vec3 posP = pPlayer.position();
                Vec3 posE = receiver.position();

                pPlayer.teleportTo(posE.x, posE.y, posE.z);
                receiver.teleportTo(posP.x, posP.y, posP.z);
            }
        }
        pPlayer.getCooldowns().addCooldown(this, 60);

        pPlayer.fallDistance = 0F;
        return super.use(pLevel, pPlayer, pUsedHand);
    }
}
