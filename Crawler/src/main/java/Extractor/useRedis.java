package Extractor;

import org.apache.commons.pool2.impl.GenericObjectPoolConfig;
import redis.clients.jedis.*;
import redis.clients.jedis.exceptions.JedisException;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class useRedis {
    private static final String redisHost = "localhost";
    private static final Integer redisPort = 6379;
    private static JedisPool pool = null;
    private static final String password = "abc@123";
    public useRedis() {


        //configure our pool connection
        pool = new JedisPool(redisHost, redisPort);

    }
    public void putHash() {
        Jedis jedis = new Jedis(redisHost,redisPort);
        jedis.connect();
        jedis.auth(password);
        jedis.flushAll();
        for(int i =1;i<=10000;i++){
            jedis.set(""+i,""+i*i);
        }

    }

    public void addHash() {
        Jedis jedis = new Jedis(redisHost,redisPort);
        jedis.connect();
        jedis.auth(password);
        jedis.flushAll();
        //add some values in Redis HASH
        String key = "test";
        Map<String, String> map = new HashMap<>();
        for(int i =1;i<=10000;i++){
            map.put(""+i,""+i*i);
        }
        jedis.hmset(key, map);

        /*Jedis jedis;
        jedis = pool.getResource();*/
        /*try {
            //save to redis


            //after saving the data, lets retrieve them to be sure that it has really added in redis
            Map<String, String> retrieveMap = jedis.hgetAll(key);
            for (String keyMap : retrieveMap.keySet()) {
                System.out.println(keyMap + " " + retrieveMap.get(keyMap));
            }

        } catch (JedisException e) {
            //if something wrong happen, return it back to the pool
            if (null != jedis) {
                pool.returnBrokenResource(jedis);
                jedis = null;
            }
        } finally {
            ///it's important to return the Jedis instance to the pool once you've finished using it
            if (null != jedis)
                pool.returnResource(jedis);
        }*/
    }
}
