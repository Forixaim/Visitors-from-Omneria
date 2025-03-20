package net.forixaim.vfo.animations.types;

import com.mojang.logging.LogUtils;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import yesman.epicfight.api.animation.AnimationManager;
import yesman.epicfight.api.animation.AnimationVariables;
import yesman.epicfight.api.animation.property.AnimationEvent;
import yesman.epicfight.api.animation.types.SelectiveAnimation;
import yesman.epicfight.api.animation.types.StaticAnimation;
import yesman.epicfight.api.asset.AssetAccessor;
import yesman.epicfight.api.client.animation.Layer;
import yesman.epicfight.world.capabilities.entitypatch.LivingEntityPatch;

import java.util.List;
import java.util.function.Function;

public class DebuggableSelectiveAnimation extends StaticAnimation
{
    public static final AnimationVariables.IndependentAnimationVariableKey<Integer> PREVIOUS_STATE = AnimationVariables.independent(() -> -1, true);
    private final Function<LivingEntityPatch<?>, Integer> selector;
    private final List<AssetAccessor<? extends StaticAnimation>> selectOptions;

    @SafeVarargs
    public DebuggableSelectiveAnimation(Function<LivingEntityPatch<?>, Integer> selector, AnimationManager.AnimationAccessor<? extends DebuggableSelectiveAnimation> accessor, AssetAccessor<? extends StaticAnimation>... selectOptions)
    {
        super(0.15F, false, accessor, null);
        this.selector = selector;
        this.selectOptions = List.of(selectOptions);

        for (AssetAccessor<? extends StaticAnimation> subAnimations : this.selectOptions)
        {
            subAnimations.get().addEvents(AnimationEvent.SimpleEvent.create((entitypatch, animation, params) ->
            {
                int result = this.selector.apply(entitypatch);
                try {
                    if (entitypatch.getAnimator().getVariables().get(PREVIOUS_STATE, this.getAccessor()) != result)
                    {
                        entitypatch.getAnimator().playAnimation(this.selectOptions.get(result), 0.0F);
                        entitypatch.getAnimator().getVariables().put(PREVIOUS_STATE, this.getAccessor(), result);
                    }
                }
                catch (Exception e)
                {
                    LogUtils.getLogger().warn(e.getMessage());
                    entitypatch.getAnimator().playAnimation(this.selectOptions.get(0), 0.0F);
                }


            }, AnimationEvent.Side.BOTH));
        }

    }

    public void begin(LivingEntityPatch<?> entitypatch)
    {
        super.begin(entitypatch);
        int result = (Integer) this.selector.apply(entitypatch);
        entitypatch.getAnimator().playAnimation((AssetAccessor) this.selectOptions.get(result), 0.0F);
        entitypatch.getAnimator().getVariables().put(PREVIOUS_STATE, this.getAccessor(), result);
    }

    public void tick(LivingEntityPatch<?> entitypatch)
    {
        super.tick(entitypatch);
    }

    public boolean isMetaAnimation()
    {
        return true;
    }

    public List<AssetAccessor<? extends StaticAnimation>> getSubAnimations()
    {
        return this.selectOptions;
    }

    @OnlyIn(Dist.CLIENT)
    public Layer.Priority getPriority()
    {
        return ((StaticAnimation) ((AssetAccessor) this.selectOptions.get(0)).get()).getPriority();
    }

    @OnlyIn(Dist.CLIENT)
    public Layer.LayerType getLayerType()
    {
        return ((StaticAnimation) ((AssetAccessor) this.selectOptions.get(0)).get()).getLayerType();
    }
}
