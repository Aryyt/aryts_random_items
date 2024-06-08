package net.aryt.aryts_random_items.event;

import net.aryt.aryts_random_items.ArytsRandomItemsMod;
import net.aryt.aryts_random_items.items.ModItems;
import net.aryt.aryts_random_items.utils.AbilityHelper;
import net.aryt.aryts_random_items.utils.Handler;
import net.minecraft.network.chat.Component;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.damagesource.DamageSources;
import net.minecraft.world.damagesource.DamageTypes;
import net.minecraft.world.entity.player.Player;
import net.minecraftforge.event.entity.living.LivingFallEvent;
import net.minecraftforge.eventbus.api.Event;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = ArytsRandomItemsMod.MOD_ID)
public class ModEvents {
    @SubscribeEvent
    public static void onLivingFallEvent(LivingFallEvent event){
        if ( !(event.getEntity() instanceof Player) ){
            return;
        }
        Player pPlayer = (Player) event.getEntity();
//        pPlayer.sendSystemMessage(Component.literal("TESTING"));
        if (pPlayer.getMainHandItem().getItem().equals(ModItems.DOOMFIST.get())){
            event.setDistance(0);
        }
        if (pPlayer.getMainHandItem().getItem().equals(ModItems.TESTITEM.get())){
            Handler h = AbilityHelper.getByUUID(pPlayer);
            if (h != null){
                h.getPlayer().sendSystemMessage(h.getPlayer().getName());
                h.getPlayer().hurt(pPlayer.damageSources().playerAttack(pPlayer), 4);
                AbilityHelper.remove(h);
            }
        }
    }
}
