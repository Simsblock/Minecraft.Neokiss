package com.porcelanrose.neokiss;

import com.porcelanrose.neokiss.Config;
import com.porcelanrose.neokiss.KissCommand;
import com.porcelanrose.neokiss.KissUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.logging.LogUtils;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.ModContainer;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.config.IConfigSpec;
import net.neoforged.fml.config.ModConfig;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterCommandsEvent;
import net.neoforged.neoforge.event.tick.ServerTickEvent;
import org.slf4j.Logger;

@Mod(value="neokiss")
public class NeoKiss {
    public static final String MODID = "neokiss";
    private static final Logger LOGGER = LogUtils.getLogger();
    private static final int TRIGGER_COUNT = 3;
    private static final int TIME_WINDOW_TICKS = 40;
    private final Map<UUID, SneakData> playerSneakData = new HashMap<UUID, SneakData>();

    public NeoKiss(IEventBus modEventBus, ModContainer modContainer) {
        modEventBus.addListener(this::commonSetup);
        NeoForge.EVENT_BUS.addListener(this::onServerTick);
        NeoForge.EVENT_BUS.register((Object)this);
        modContainer.registerConfig(ModConfig.Type.COMMON, (IConfigSpec)Config.SPEC);
    }

    private void commonSetup(FMLCommonSetupEvent event) {
        NeoForge.EVENT_BUS.addListener(this::onRegisterCommands);
    }

    private void onRegisterCommands(RegisterCommandsEvent event) {
        KissCommand.register((CommandDispatcher<CommandSourceStack>)event.getDispatcher());
    }

    @SubscribeEvent
    public void onServerTick(ServerTickEvent.Post event) {
        for (ServerLevel level : event.getServer().getAllLevels()) {
            for (ServerPlayer player : level.players()) {
                this.handleSneak(player, level);
            }
        }
    }

    private void handleSneak(ServerPlayer player, ServerLevel level) {
        UUID uuid = player.getUUID();
        SneakData data = this.playerSneakData.computeIfAbsent(uuid, u -> new SneakData());
        boolean isSneaking = player.isCrouching();
        if (isSneaking && !data.wasSneaking) {
            int delta = (int)(level.getGameTime() - data.lastTime);
            data.count = delta <= 40 ? data.count + 1 : 1;
            data.lastTime = level.getGameTime();
            if (data.count >= 3) {
                if (KissUtils.hasVisibleNearbyPlayers(player, level, Config.getSneakTriggerRadius(), Config.getMaxViewAngleDegree())) {
                    data.particleLevel = Math.min(Config.getMaxSneakParticles(), data.particleLevel + 1);
                    KissUtils.spawnHeartParticles(level, player, data.particleLevel + 1, 0.3, 0.1);
                }
                data.count = 0;
            } else if (KissUtils.hasVisibleNearbyPlayers(player, level, 6.0, Config.getMaxViewAngleDegree())) {
                KissUtils.spawnHeartParticles(level, player, 1, 0.1, 0.05);
            }
        }
        if (!isSneaking && data.wasSneaking) {
            data.particleLevel = Math.max(1, data.particleLevel - 2);
        }
        data.wasSneaking = isSneaking;
    }

    private static class SneakData {
        int count = 0;
        long lastTime = -80L;
        int particleLevel = 1;
        boolean wasSneaking = false;

        private SneakData() {
        }
    }
}