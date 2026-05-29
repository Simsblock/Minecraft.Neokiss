package com.porcelanrose.neokiss;

import com.porcelanrose.neokiss.Config;
import com.porcelanrose.neokiss.KissUtils;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;

public class KissCommand {
    private static final Map<UUID, Long> lastKissTimes = new HashMap<UUID, Long>();

    public static void register(CommandDispatcher<CommandSourceStack> dispatcher) {
        if (!Config.isKissCommandEnabled()) {
            return;
        }
        dispatcher.register((LiteralArgumentBuilder)Commands.literal((String)"kiss").then(Commands.argument((String)"player", (ArgumentType)EntityArgument.player()).executes(KissCommand::executeKiss)));
    }

    private static int executeKiss(CommandContext<CommandSourceStack> context) throws CommandSyntaxException {
        ServerPlayer targetPlayer;
        CommandSourceStack source = (CommandSourceStack)context.getSource();
        ServerPlayer sourcePlayer = source.getPlayerOrException();
        if (KissUtils.isSamePlayer(sourcePlayer, targetPlayer = EntityArgument.getPlayer(context, (String)"player"))) {
            KissUtils.sendError(source, Config.getSelfKissErrorMessage(), KissUtils.DARK_RED);
            return 0;
        }
        if (!KissCommand.checkCooldown(sourcePlayer)) {
            long remaining = KissCommand.getRemainingCooldownSeconds(sourcePlayer);
            KissUtils.sendError(source, String.format(Config.getCooldownErrorMessage(), remaining), KissUtils.YELLOW);
            return 0;
        }
        KissCommand.sendKiss(sourcePlayer, targetPlayer);
        KissCommand.updateCooldown(sourcePlayer);
        return 1;
    }

    private static void sendKiss(ServerPlayer source, ServerPlayer target) {
        MutableComponent msgToTarget = KissUtils.buildMessage(Config.getKissMessage(), source.getDisplayName(), KissUtils.LIGHT_PURPLE);
        MutableComponent msgToSource = KissUtils.buildMessage(Config.getKissPromptMessage(), target.getDisplayName(), KissUtils.GRAY);
        source.sendSystemMessage((Component)msgToSource);
        target.sendSystemMessage((Component)msgToTarget);
        source.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        target.playSound(SoundEvents.EXPERIENCE_ORB_PICKUP, 1.0f, 1.0f);
        KissUtils.spawnHeartParticles(target.serverLevel(), target, 3, 0.3, 0.1);
    }

    private static boolean checkCooldown(ServerPlayer player) {
        long lastTime;
        long now = System.currentTimeMillis();
        return now - (lastTime = lastKissTimes.getOrDefault(player.getUUID(), 0L).longValue()) >= (long)Config.getCommandCooldown() * 1000L;
    }

    private static long getRemainingCooldownSeconds(ServerPlayer player) {
        long now = System.currentTimeMillis();
        long lastTime = lastKissTimes.getOrDefault(player.getUUID(), 0L);
        long remaining = (long)Config.getCommandCooldown() * 1000L - (now - lastTime);
        return Math.max(0L, remaining / 1000L);
    }

    private static void updateCooldown(ServerPlayer player) {
        lastKissTimes.put(player.getUUID(), System.currentTimeMillis());
    }
}