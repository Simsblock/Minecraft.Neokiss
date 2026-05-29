package com.porcelanrose.neokiss;

import java.util.Objects;
import java.util.UUID;
import net.minecraft.ChatFormatting;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.MutableComponent;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

public class KissUtils {
    public static final ChatFormatting DARK_RED = ChatFormatting.DARK_RED;
    public static final ChatFormatting YELLOW = ChatFormatting.YELLOW;
    public static final ChatFormatting LIGHT_PURPLE = ChatFormatting.LIGHT_PURPLE;
    public static final ChatFormatting GRAY = ChatFormatting.GRAY;

    public static boolean isSamePlayer(ServerPlayer p1, ServerPlayer p2) {
        return p1.getUUID().equals(p2.getUUID());
    }

    public static boolean hasVisibleNearbyPlayers(ServerPlayer player, ServerLevel level, double radius, double maxViewAngleDegree) {
        if (radius <= 0.0 || maxViewAngleDegree <= 0.0) {
            return false;
        }
        UUID self = player.getUUID();
        Vec3 pos = player.position();
        AABB area = new AABB(pos.add(-radius, -6.0, -radius), pos.add(radius, 6.0, radius));
        for (ServerPlayer other : level.players()) {
            if (other.getUUID().equals(self) || !area.contains(other.position()) || !KissUtils.isPlayerInViewAndCanSee(player, other, maxViewAngleDegree)) continue;
            return true;
        }
        return false;
    }

    public static boolean isPlayerInViewAndCanSee(ServerPlayer observer, ServerPlayer target, double maxAngleDegree) {
        double maxAngleRad;
        Vec3 dirToTarget;
        if (!Objects.equals(observer.serverLevel(), target.serverLevel())) {
            return false;
        }
        Vec3 lookVec = observer.getLookAngle().normalize();
        double dot = lookVec.dot(dirToTarget = target.position().subtract(observer.position()).normalize());
        double angleRad = Math.acos(dot);
        return angleRad <= (maxAngleRad = Math.toRadians(maxAngleDegree));
    }

    public static void spawnHeartParticles(ServerLevel level, ServerPlayer player, int count, double offset, double speed) {
        Vec3 lookVec = player.getLookAngle().multiply(0.3, 0.3, 0.3);
        Vec3 pos = player.position();
        double x = pos.x + lookVec.x;
        double y = pos.y + 1.62;
        double z = pos.z + lookVec.z;
        level.sendParticles((ParticleOptions)ParticleTypes.HEART, x, y, z, count, offset, offset, offset, speed);
    }

    public static void sendError(CommandSourceStack source, String message, ChatFormatting color) {
        source.sendFailure((Component)Component.literal((String)message).withStyle(color));
    }

    public static MutableComponent buildMessage(String template, Component playerName, ChatFormatting color) {
        try {
            String[] parts = template.split("%s", -1);
            MutableComponent result = Component.literal((String)parts[0]);
            for (int i = 1; i < parts.length; ++i) {
                result = result.append(playerName).append((Component)Component.literal((String)parts[i]));
            }
            return result.withStyle(color);
        }
        catch (Exception e) {
            return Component.literal((String)playerName.getString()).withStyle(color);
        }
    }
}