package net.forixaim.vfo.util;

public class ColorUtils
{
    public static int fromRGB(int r, int g, int b)
    {
        return 0xFF000000 | (r << 16) | (g << 8) | b;
    }
}
