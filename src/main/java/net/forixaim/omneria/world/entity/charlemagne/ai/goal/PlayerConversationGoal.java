package net.forixaim.omneria.world.entity.charlemagne.ai.goal;

import net.forixaim.omneria.world.entity.types.plugins.DialogueNPC;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.LookAtPlayerGoal;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.trading.Merchant;

import java.util.EnumSet;

/**
 * This goal makes an NPC stop in place when talking with a player.或交易时
 */
public class PlayerConversationGoal<T extends Mob & DialogueNPC> extends LookAtPlayerGoal {
    private final T npc;

    public PlayerConversationGoal(T npc) {
        super(npc, Player.class, 8.0F);
        this.npc = npc;
        this.setFlags(EnumSet.of(Goal.Flag.JUMP, Flag.LOOK, Goal.Flag.MOVE));
    }

    @Override
    public boolean canUse() {
        if(this.npc instanceof Merchant merchant && merchant.getTradingPlayer() != null){
            this.lookAt = merchant.getTradingPlayer();
            return true;
        }
        if (this.npc.getConversingPlayer() != null && this.npc.getConversingPlayer().isAlive() && !this.npc.hurtMarked && this.npc.distanceToSqr(this.npc.getConversingPlayer()) <= 64.0F) {
            this.lookAt = this.npc.getConversingPlayer();
            return true;
        }
        return false;
    }

    @Override
    public boolean canContinueToUse() {
        return this.canUse();
    }

    /**
     * Stops the NPC from moving.
     */
    @Override
    public void start() {
        super.start();
        this.npc.getNavigation().stop();
    }

    /**
     * Makes the NPC stop talking to the player.
     */
    @Override
    public void stop() {
        super.stop();
        this.npc.setConversingPlayer(null);
    }
}