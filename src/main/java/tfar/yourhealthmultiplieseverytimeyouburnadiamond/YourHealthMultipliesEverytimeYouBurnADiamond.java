package tfar.yourhealthmultiplieseverytimeyouburnadiamond;

import net.minecraft.entity.item.ItemEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.item.Items;
import net.minecraft.util.DamageSource;
import net.minecraft.world.server.ServerWorld;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.event.entity.item.ItemTossEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.event.server.FMLServerStartingEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.UUID;

// The value here should match an entry in the META-INF/mods.toml file
@Mod(YourHealthMultipliesEverytimeYouBurnADiamond.MODID)
public class YourHealthMultipliesEverytimeYouBurnADiamond {

    public static final String MODID = "yourhealthmultiplieseverytimeyouburnadiamond";

    public YourHealthMultipliesEverytimeYouBurnADiamond() {
        MinecraftForge.EVENT_BUS.addListener(this::onItemToss);
        MinecraftForge.EVENT_BUS.addListener(this::playerRespawn);
    }

    public void onItemToss(ItemTossEvent event) {
        if (event.getEntityItem().getItem().getItem() == Items.DIAMOND) {
            event.getEntityItem().getPersistentData().putUniqueId(MODID,event.getPlayer().getGameProfile().getId());
        }
    }

    public static void diamondBurnHook(ItemEntity itemEntity, DamageSource source) {
        if (source.isFireDamage() && itemEntity.getPersistentData().hasUniqueId(YourHealthMultipliesEverytimeYouBurnADiamond.MODID)) {
            UUID uuid = itemEntity.getPersistentData().getUniqueId(YourHealthMultipliesEverytimeYouBurnADiamond.MODID);
            ServerPlayerEntity serverPlayerEntity = itemEntity.getEntityWorld().getServer().getPlayerList().getPlayerByUUID(uuid);
            PersistantData.getDefaultInstance(serverPlayerEntity.getServerWorld()).addDiamond(serverPlayerEntity);
        }
    }


    public void playerRespawn(EntityJoinWorldEvent e) {
        if (e.getEntity() instanceof PlayerEntity && !e.getWorld().isRemote) {
            PersistantData.getDefaultInstance((ServerWorld) e.getWorld()).updateHearts((PlayerEntity) e.getEntity());
        }
    }
}
