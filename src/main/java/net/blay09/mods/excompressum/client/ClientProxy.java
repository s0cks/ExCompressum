package net.blay09.mods.excompressum.client;

import com.google.common.collect.Sets;
import com.mojang.authlib.GameProfile;
import cpw.mods.fml.client.registry.ClientRegistry;
import cpw.mods.fml.client.registry.RenderingRegistry;
import cpw.mods.fml.common.event.FMLPostInitializationEvent;
import cpw.mods.fml.common.event.FMLPreInitializationEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import exnihilo.blocks.models.ModelSieve;
import exnihilo.blocks.models.ModelSieveMesh;
import net.blay09.mods.excompressum.CommonProxy;
import net.blay09.mods.excompressum.ExCompressum;
import net.blay09.mods.excompressum.ModBlocks;
import net.blay09.mods.excompressum.ModItems;
import net.blay09.mods.excompressum.client.render.entity.RenderAngryChicken;
import net.blay09.mods.excompressum.client.render.item.*;
import net.blay09.mods.excompressum.client.render.tile.*;
import net.blay09.mods.excompressum.entity.EntityAngryChicken;
import net.blay09.mods.excompressum.registry.ChickenStickRegistry;
import net.blay09.mods.excompressum.tile.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.model.ModelChicken;
import net.minecraft.client.renderer.entity.RenderChicken;
import net.minecraft.item.Item;
import net.minecraft.util.IIcon;
import net.minecraft.util.Session;
import net.minecraftforge.client.MinecraftForgeClient;
import net.minecraftforge.client.event.TextureStitchEvent;
import net.minecraftforge.common.MinecraftForge;

import java.util.Set;

@SuppressWarnings("unused")
public class ClientProxy extends CommonProxy {

    private final Set<GameProfile> skinRequested = Sets.newHashSet();
    public static IIcon iconEmptyBookSlot;
    public static IIcon iconEmptyHammerSlot;
    public static IIcon iconEmptyCompressedHammerSlot;

    @Override
    public void preInit(FMLPreInitializationEvent event) {
        MinecraftForge.EVENT_BUS.register(this);

        ModelSieve sieve = new ModelSieve();
        ModelSieveMesh mesh = new ModelSieveMesh();
        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityHeavySieve.class, new RenderHeavySieve(sieve, mesh));
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.heavySieve), new ItemRenderHeavySieve(sieve, mesh));

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieveRF.class, new RenderAutoSieve());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoSieve), new ItemRenderAutoSieve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoSieveMana.class, new RenderManaSieve());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.manaSieve), new ItemRenderManaSieve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoHeavySieveRF.class, new RenderAutoHeavySieve());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoHeavySieve), new ItemRenderAutoHeavySieve());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityWoodenCrucible.class, new RenderWoodenCrucible());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.woodenCrucible), new ItemRenderWoodenCrucible());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoHammer.class, new RenderAutoHammer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoHammer), new ItemRenderAutoHammer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityAutoCompressedHammer.class, new RenderAutoCompressedHammer());
        MinecraftForgeClient.registerItemRenderer(Item.getItemFromBlock(ModBlocks.autoCompressedHammer), new ItemRenderAutoCompressedHammer());

        ClientRegistry.bindTileEntitySpecialRenderer(TileEntityBait.class, new RenderBait());

        RenderingRegistry.registerEntityRenderingHandler(EntityAngryChicken.class, new RenderAngryChicken(new ModelChicken(), 0.3f));
    }

    @Override
    public void postInit(FMLPostInitializationEvent event) {
        String customName = ChickenStickRegistry.chickenStickNames.get(Minecraft.getMinecraft().getSession().getUsername().toLowerCase());
        if(customName == null) {
            customName = ChickenStickRegistry.chickenStickNames.get("*");
        }
        if(customName != null) {
            ChickenStickRegistry.setChickenStickName(customName);
        }
    }

    @Override
    public void preloadSkin(GameProfile customSkin) {
        if(!skinRequested.contains(customSkin)) {
            Minecraft.getMinecraft().func_152342_ad().func_152790_a(customSkin, null, true);
            skinRequested.add(customSkin);
        }
    }

    @SubscribeEvent
    public void onTextureStitch(TextureStitchEvent.Pre event) {
        if(event.map.getTextureType() == 1) {
            iconEmptyBookSlot = event.map.registerIcon(ExCompressum.MOD_ID + ":empty_enchanted_book_slot");
            iconEmptyHammerSlot = event.map.registerIcon(ExCompressum.MOD_ID + ":empty_hammer_slot");
            iconEmptyCompressedHammerSlot = event.map.registerIcon(ExCompressum.MOD_ID + ":empty_compressed_hammer_slot");
        }
    }
}
