package tfar.yourhealthmultiplieseverytimeyouburnadiamond;

import net.minecraft.entity.ai.attributes.AttributeModifier;
import net.minecraft.entity.ai.attributes.Attributes;
import net.minecraft.entity.ai.attributes.ModifiableAttributeInstance;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.nbt.INBT;
import net.minecraft.nbt.ListNBT;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;
import net.minecraft.world.storage.WorldSavedData;
import net.minecraftforge.common.util.Constants;

import java.util.*;

public class PersistantData extends WorldSavedData {

    private final Map<UUID, Integer> persistentInventoryStorage = new HashMap<>();

    private static final String ID = "diamonds_burned";

    private static final UUID uuid = UUID.fromString("06a2a472-3c34-4e18-9cf6-7f7afd16b1cb");

    public PersistantData(String name) {
        super(name);
    }

    public static PersistantData getDefaultInstance(ServerWorld world) {
        return world.getServer().getWorld(World.OVERWORLD).getSavedData().getOrCreate(() -> new PersistantData(ID), ID);//overworld storage
    }

    private static final String ID1 = "inventory_storage";

    @Override
    public void read(CompoundNBT nbt) {
        readInvData(nbt, "level", persistentInventoryStorage);
    }

    public void addDiamond(PlayerEntity player) {
        int level = persistentInventoryStorage.getOrDefault(player.getGameProfile().getId(),0);
        level++;
        persistentInventoryStorage.put(player.getGameProfile().getId(),level);
        updateHearts(player);
        markDirty();
    }

    public void updateHearts(PlayerEntity player) {
        ModifiableAttributeInstance iattributeinstance = player.getAttribute(Attributes.MAX_HEALTH);
        if (iattributeinstance.getModifier(uuid) != null) {
            iattributeinstance.removeModifier(uuid);
        }
        iattributeinstance.applyPersistentModifier( new AttributeModifier(uuid,"Extra Hearts", Math.pow(2,getDiamondsBurned(player)) - 1, AttributeModifier.Operation.MULTIPLY_TOTAL));
    }

    public int getDiamondsBurned(PlayerEntity player) {
        return persistentInventoryStorage.getOrDefault(player.getUniqueID(),0);
    }

    public void readInvData(CompoundNBT nbt, String key, Map<UUID, Integer> map) {
        ListNBT list = nbt.getList(key, Constants.NBT.TAG_COMPOUND);
        for (INBT nbtBase : list) {
            CompoundNBT compound = (CompoundNBT) nbtBase;
            UUID uuid = compound.getUniqueId("uuid");
            int level = compound.getInt("level");
            map.put(uuid, level);
        }
    }

    @Override
    public CompoundNBT write(CompoundNBT nbt) {
        writeInvData(nbt, "level", persistentInventoryStorage);
        return nbt;
    }

    public void writeInvData(CompoundNBT nbt, String key, Map<UUID, Integer> map) {
        ListNBT list = new ListNBT();
        for (Map.Entry<UUID, Integer> entry : map.entrySet()) {
            UUID uuid = entry.getKey();
            int storage = entry.getValue();
            CompoundNBT compound1 = new CompoundNBT();
            compound1.putUniqueId("uuid", uuid);
            compound1.putInt("level", storage);
            list.add(compound1);
        }
        nbt.put(key, list);
    }
}
