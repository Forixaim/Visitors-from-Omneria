package net.forixaim.omneria.skill.battle_style.genesis_wyrm;

import net.forixaim.battle_arts_api.battle_arts_skills.BattleArtsSkillSlots;
import net.forixaim.omneria.animations.battle_style.genesis_wyrm.GenesisWyrmAnimations;
import net.forixaim.omneria.skill.DatakeyRegistry;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.Entity;
import yesman.epicfight.client.ClientEngine;
import yesman.epicfight.skill.Skill;
import yesman.epicfight.skill.SkillBuilder;
import yesman.epicfight.skill.SkillContainer;
import yesman.epicfight.skill.SkillDataManager;
import yesman.epicfight.world.capabilities.EpicFightCapabilities;
import yesman.epicfight.world.capabilities.entitypatch.EntityPatch;
import yesman.epicfight.world.capabilities.entitypatch.HurtableEntityPatch;
import yesman.epicfight.world.entity.eventlistener.PlayerEventListener;

import java.util.UUID;

public class CosmicChaser extends Skill {
    private static final UUID EVENT_UUID = UUID.fromString("8711857a-4401-4465-b901-782c6845d1b8");

    public CosmicChaser(SkillBuilder<? extends Skill> builder) {
        super(builder);
    }

    @Override
    public void onInitiate(SkillContainer container) {
        super.onInitiate(container);
        container.getExecutor().getEventListener().addEventListener(PlayerEventListener.EventType.MOVEMENT_INPUT_EVENT, EVENT_UUID, event ->
        {
            if (event.getInputState().jumping())
            {
                container.sendCastRequest(event.getPlayerPatch(), ClientEngine.getInstance().controlEngine);
            }
        });
    }

    @Override
    public boolean canExecute(SkillContainer container) {
        return super.canExecute(container) && !container.getExecutor().getOriginal().onGround();
    }

    @Override
    public void executeOnServer(SkillContainer container, FriendlyByteBuf args) {
        super.executeOnServer(container, args);
        SkillDataManager cosmicFocusDataManager = container.getServerExecutor().getSkill(BattleArtsSkillSlots.SPECIAL_ART).getDataManager();
        if (cosmicFocusDataManager != null && cosmicFocusDataManager.hasData(DatakeyRegistry.FOCUSED_TARGET.get())) {
            int opponent = cosmicFocusDataManager.getDataValue(DatakeyRegistry.FOCUSED_TARGET.get());
            if (opponent < 0)
            {
                Entity trueOpponent = container.getServerExecutor().getOriginal().level().getEntity(opponent);
                if (EpicFightCapabilities.getEntityPatch(trueOpponent, EntityPatch.class) instanceof HurtableEntityPatch<?> hurtableEntityPatch)
                {
                    if (container.getExecutor().getOriginal().distanceTo(trueOpponent) > 20)
                    {
                        container.getExecutor().playAnimationSynchronized(GenesisWyrmAnimations.COSMIC_CHASER, 0);
                    }
                }
            }
        }
    }
}
