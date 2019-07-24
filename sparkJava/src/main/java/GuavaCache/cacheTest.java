package GuavaCache;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import java.util.concurrent.TimeUnit;

public class cacheTest {
    private static LoadingCache<Integer,Integer> paramCache;
    static {
        paramCache = CacheBuilder.newBuilder()
                .maximumSize(100)
                .expireAfterAccess(10, TimeUnit.SECONDS)     //refresh cache after 30 secs without request
                .build(
                        new CacheLoader<Integer, Integer>() {
                            @Override
                            public Integer load(Integer param) throws Exception {
                                return getValue(param);
                            }
                        }
                );
    }

    public static LoadingCache<Integer,Integer> getParamCache(){
        return paramCache;
    }

    public static Integer getValue(int param){
        System.out.println("Executing getValue");
        return param*param;
    }


}
