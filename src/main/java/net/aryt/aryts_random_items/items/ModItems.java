package net.aryt.aryts_random_items.items;

import net.aryt.aryts_random_items.ArytsRandomItemsMod;
import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ArytsRandomItemsMod.MOD_ID);

    public static final RegistryObject<Item> VIBRASLAP = ITEMS.register("vibraslap",
            () -> new Item(new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
