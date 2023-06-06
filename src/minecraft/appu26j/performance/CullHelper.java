package appu26j.performance;

public class CullHelper
{
    public static boolean isCulled(CullType type, boolean culled)
    {
        return culled;
    }

    public static final class CullType
    {
        public static final CullType BLOCK_ENTITY = new CullType();
        public static final CullType ENTITY = new CullType();
        private static final CullType[] VALUES;
        
        public static CullType[] values()
        {
            return VALUES.clone();
        }
        
        private static CullType[] values2() 
        {
            return new CullType[] {BLOCK_ENTITY, ENTITY};
        }
        
        static 
        {
            VALUES = CullType.values2();
        }
    }

    @FunctionalInterface
    public static interface CullFunction<T>
    {
        public boolean apply(T var1);
    }
}

