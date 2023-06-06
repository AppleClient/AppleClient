package appu26j.performance;

public interface CullingTargetAccessor
{
    public boolean isLastCullingVisible();

    public void setLastCullingVisible(boolean var1, boolean var2);

    public boolean isLastCheckEvenTick();

    public long getLastTimeChecked();

    public double getMinX();

    public double getMinY();

    public double getMinZ();

    public double getMaxX();

    public double getMaxY();

    public double getMaxZ();
}
