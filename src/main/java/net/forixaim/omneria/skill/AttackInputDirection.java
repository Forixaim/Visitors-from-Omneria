package net.forixaim.omneria.skill;

import net.minecraft.nbt.CompoundTag;

public enum AttackInputDirection {
    NONE,
    FRONT, BACK, LEFT, RIGHT,
    FRONT_LEFT, FRONT_RIGHT, BACK_LEFT, BACK_RIGHT,
    UP, DOWN,
    UP_FRONT, UP_BACK, UP_LEFT, UP_RIGHT,
    UP_FRONT_LEFT, UP_FRONT_RIGHT, UP_BACK_LEFT, UP_BACK_RIGHT,
    DOWN_FRONT, DOWN_BACK, DOWN_LEFT, DOWN_RIGHT,
    DOWN_FRONT_LEFT, DOWN_FRONT_RIGHT, DOWN_BACK_LEFT, DOWN_BACK_RIGHT;

    public static AttackInputDirection getSimple(CompoundTag tag)
    {
        if (tag == null) return NONE;
    }

    public static AttackInputDirection fromTag(CompoundTag tag) {
        if (tag == null) return NONE;

        int fb = tag.getInt("front_back");  // 1 = Front, -1 = Back
        int lr = tag.getInt("left_right");  // 1 = Left, -1 = Right
        int ud = tag.getInt("up_down");     // 1 = Up, -1 = Down

        if (ud == 1) return getUpDirection(fb, lr);
        if (ud == -1) return getDownDirection(fb, lr);
        return getLevelDirection(fb, lr);
    }

    private static AttackInputDirection getUpDirection(int fb, int lr) {
        return getAttackInputDirection(fb, lr, UP_FRONT_LEFT, UP_FRONT_RIGHT, UP_FRONT, UP_BACK_LEFT, UP_BACK_RIGHT, UP_BACK, UP_LEFT, UP_RIGHT, UP);
    }

    private static AttackInputDirection getAttackInputDirection(int fb, int lr, AttackInputDirection attackInputDirection, AttackInputDirection attackInputDirection2, AttackInputDirection attackInputDirection3, AttackInputDirection attackInputDirection4, AttackInputDirection attackInputDirection5, AttackInputDirection attackInputDirection6, AttackInputDirection attackInputDirection7, AttackInputDirection attackInputDirection8, AttackInputDirection attackInputDirection9) {
        if (fb == 1) {
            if (lr == 1) return attackInputDirection;
            if (lr == -1) return attackInputDirection2;
            return attackInputDirection3;
        }
        if (fb == -1) {
            if (lr == 1) return attackInputDirection4;
            if (lr == -1) return attackInputDirection5;
            return attackInputDirection6;
        }
        if (lr == 1) return attackInputDirection7;
        if (lr == -1) return attackInputDirection8;
        return attackInputDirection9;
    }

    private static AttackInputDirection getDownDirection(int fb, int lr) {
        return getAttackInputDirection(fb, lr, DOWN_FRONT_LEFT, DOWN_FRONT_RIGHT, DOWN_FRONT, DOWN_BACK_LEFT, DOWN_BACK_RIGHT, DOWN_BACK, DOWN_LEFT, DOWN_RIGHT, DOWN);
    }

    private static AttackInputDirection getLevelDirection(int fb, int lr) {
        return getAttackInputDirection(fb, lr, FRONT_LEFT, FRONT_RIGHT, FRONT, BACK_LEFT, BACK_RIGHT, BACK, LEFT, RIGHT, NONE);
    }
}