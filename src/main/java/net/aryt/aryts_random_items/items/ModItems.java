package net.aryt.aryts_random_items.items;

import net.aryt.aryts_random_items.ArytsRandomItemsMod;
import net.aryt.aryts_random_items.items.custom.DoomFistItem;
import net.aryt.aryts_random_items.items.custom.TestItem;
import net.aryt.aryts_random_items.items.custom.VibraslapItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Tiers;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class ModItems {
    public static final DeferredRegister<Item> ITEMS =
            DeferredRegister.create(ForgeRegistries.ITEMS, ArytsRandomItemsMod.MOD_ID);

    public static final RegistryObject<Item> VIBRASLAP = ITEMS.register("vibraslap",
            () -> new VibraslapItem(new Item.Properties()));

    public static final RegistryObject<Item> DOOMFIST = ITEMS.register("doomfist",
            () -> new DoomFistItem(Tiers.DIAMOND, (float) 4., (float)3., new Item.Properties()));
    public static final RegistryObject<Item> TESTITEM = ITEMS.register("testitem",
            () -> new TestItem(Tiers.DIAMOND, (float) 4., (float)3., new Item.Properties()));

    public static void register(IEventBus eventBus){
        ITEMS.register(eventBus);
    }
}
