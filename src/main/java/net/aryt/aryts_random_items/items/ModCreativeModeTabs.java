package net.aryt.aryts_random_items.items;

import net.aryt.aryts_random_items.ArytsRandomItemsMod;
import net.aryt.aryts_random_items.block.ModBlocks;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;

public class ModCreativeModeTabs {
    public static final DeferredRegister<CreativeModeTab> CREATIVE_MODE_TABS =
            DeferredRegister.create(Registries.CREATIVE_MODE_TAB, ArytsRandomItemsMod.MOD_ID);

    public static final RegistryObject<CreativeModeTab> ARIM_TAB = CREATIVE_MODE_TABS.register("arim_tab",
            () -> CreativeModeTab.builder().icon(() -> new ItemStack(ModItems.VIBRASLAP.get())).title(Component.translatable("creativetab.arim_tab"))
                    .displayItems((pParameters, pOutput) -> {
                        pOutput.accept(ModItems.VIBRASLAP.get());
//                        pOutput.accept(ModBlocks.DOMAIN_BLOCK.get());
                        pOutput.accept(ModBlocks.SAPPHIRE_BLOCK.get());
                        pOutput.accept(ModBlocks.TEST_BLOCK.get());
                    })
                    .build());

    public static void register(IEventBus eventBus){
        CREATIVE_MODE_TABS.register(eventBus);
    }
}
