package net.forixaim.omneria.util;

import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.network.protocol.game.ClientboundLevelParticlesPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.level.Level;

public class ParticleUtil
{
    public static <T extends ParticleOptions> int sendAlwaysVisibleParticles(ServerLevel level, T pType, double pPosX, double pPosY, double pPosZ, int pParticleCount, double pXOffset, double pYOffset, double pZOffset, double pSpeed) {
        int i = 0;
        for(int j = 0; j < level.players().size(); ++j) {
            ServerPlayer serverplayer = level.players().get(j);
            if (level.sendParticles(serverplayer, pType, true, pPosX, pPosY, pPosZ, pParticleCount, pXOffset, pYOffset, pZOffset, pSpeed)) {
                ++i;
            }
        }
        return i;
    }
}
