package com.porcelanrose.neokiss;
import net.neoforged.neoforge.common.ModConfigSpec;
public class Config {
    private static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec.ConfigValue<String> KISS_MESSAGE = BUILDER.comment("Message shown to the kissed player (use %s for player name)").define("kissMessage", "%s kissed you!");
    public static final ModConfigSpec.ConfigValue<String> KISS_PROMPT_MESSAGE = BUILDER.comment("Message shown to the kissing player (use %s for player name)").define("kissPromptMessage", "You kissed %s!");
    public static final ModConfigSpec.ConfigValue<String> SELF_KISS_ERROR_MESSAGE = BUILDER.comment("Error message when trying to kiss yourself").define("selfKissErrorMessage", "You can't kiss yourself!");
    public static final ModConfigSpec.ConfigValue<String> COOLDOWN_ERROR_MESSAGE = BUILDER.comment("Error message when command is on cooldown (use %d for seconds)").define("cooldownErrorMessage", "Wait %d seconds before kissing again!");
    public static final ModConfigSpec.IntValue COMMAND_COOLDOWN = BUILDER.comment("Cooldown between kisses in seconds").defineInRange("commandCooldown", 10, 1, 3600);
    public static final ModConfigSpec.IntValue MAX_SNEAK_PARTICLES = BUILDER.comment("Maximum number of heart particles when sneaking").defineInRange("maxSneakParticles", 6, 1, 20);
    public static final ModConfigSpec.DoubleValue SNEAK_TRIGGER_RADIUS = BUILDER.comment("Radius for detecting nearby players when sneaking").defineInRange("sneakTriggerRadius", 16.0, 1.0, 128.0);
    public static final ModConfigSpec.DoubleValue MAX_VIEW_ANGLE_DEGREE = BUILDER.comment("Maximum view angle for detecting players (in degrees)").defineInRange("maxViewAngleDegree", 90.0, 1.0, 360.0);
    public static final ModConfigSpec.BooleanValue ENABLE_KISS_COMMAND = BUILDER.comment("Enable the /kiss command").define("enableKissCommand", true);
    static final ModConfigSpec SPEC = BUILDER.build();
    public static String getKissMessage() {
        return KISS_MESSAGE.get();
    }
    public static String getKissPromptMessage() {
        return KISS_PROMPT_MESSAGE.get();
    }
    public static String getSelfKissErrorMessage() {
        return SELF_KISS_ERROR_MESSAGE.get();
    }
    public static String getCooldownErrorMessage() {
        return COOLDOWN_ERROR_MESSAGE.get();
    }
    public static int getCommandCooldown() {
        return COMMAND_COOLDOWN.get();
    }
    public static int getMaxSneakParticles() {
        return MAX_SNEAK_PARTICLES.get();
    }
    public static double getSneakTriggerRadius() {
        return SNEAK_TRIGGER_RADIUS.get();
    }
    public static double getMaxViewAngleDegree() {
        return MAX_VIEW_ANGLE_DEGREE.get();
    }
    public static boolean isKissCommandEnabled() {
        return ENABLE_KISS_COMMAND.get();
    }
}