# NeoKiss

NeoKiss is a fork of the MIT licensed kiss mod. \
Specifically a forked of the [1.2.0 Neoforge](https://modrinth.com/mod/kiss-fabric/version/plUmdfHo) Version.

> [Recaf](https://github.com/Col-E/Recaf/releases/tag/4.0.0-alpha) was our tool of choice for decompiling the jar.

This fork aims to fix slight issues with the mod, fix some major version breaking bugs and to make it available to the public.

_**What if we kissed on our Aeronautics Contraption,,??**_

## How to use

Rapidly sneak close to another player and hearts will start appearing. 

Alernatively u can use the `/kiss <player>` command to keep your long distance relationships going, but be ware: Selflove is prohibited \>:3c

## Config

The config is pretty self explanatory :3

```
#Message shown to the kissed player (use %s for player name)
kissMessage = "%s kissed you!"

#Message shown to the kissing player (use %s for player name)
kissPromptMessage = "You kissed %s!"

#Error message when trying to kiss yourself
selfKissErrorMessage = "You can't kiss yourself!"

#Error message when command is on cooldown (use %d for seconds)
cooldownErrorMessage = "Wait %d seconds before kissing again!"

#Cooldown between kisses in seconds
# Default: 10
# Range: 1 ~ 3600
commandCooldown = 10

#Maximum number of heart particles when sneaking
# Default: 6
# Range: 1 ~ 20
maxSneakParticles = 6

#Radius for detecting nearby players when sneaking
# Default: 16.0
# Range: 1.0 ~ 128.0
sneakTriggerRadius = 1.0

#Maximum view angle for detecting players (in degrees)
# Default: 90.0
# Range: 1.0 ~ 360.0
maxViewAngleDegree = 90.0

#Enable the /kiss command
enableKissCommand = true
````