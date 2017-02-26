package com.chris.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.chris.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.Transaction;

import java.util.List;
import java.util.Set;

/**
 * Created by YaoQi on 2017/2/23.
 */
@Service
public class JedisAdapter implements InitializingBean {
    private static final Logger logger = LoggerFactory.getLogger(JedisAdapter.class);
    private JedisPool pool = null;
    private static void print(int index, Object obj) {
        System.out.println(String.format("%d: %s", index, obj));
    }

    public static void mainx(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/10");
        jedis.set("hello", "world");
        print(1, jedis.get("hello"));

        String userKey = "userxx";
        jedis.lpush(userKey, "hello", "world", "springboot", "mybatis");
        print(2, jedis.lrange(userKey, 0, 3));

        String key2 = "user2";
        jedis.hset(key2, "name", "Jimmy");
        jedis.hset(key2, "age", "20");
        jedis.hset(key2, "gender", "male");
        print(3, jedis.hgetAll(key2));
        jedis.hset("user2", "senior", "false");
        print(4, jedis.hgetAll(key2));


        jedis.set("pv", "100");
        print(5, jedis.get("pv"));

        //redis pool
        JedisPool pool = new JedisPool();
        for(int i = 0; i < 10; i++) {
            Jedis j = pool.getResource();
            j.set("pv", "100");
            print(6, j.get("pv"));
            j.close();
        }

        // redis cache
        User user = new User();
        user.setName("Jimmy");
        user.setPassword("hello");
        user.setSalt("sjdk");
        user.setHeadUrl("a.jpg");
        user.setId(13);
        jedis.set("user1", JSONObject.toJSONString(user));

        //get the serialized object
        String value = jedis.get("user1");
        User user1 = JSON.parseObject(value, User.class);
    }

    public static void main(String[] args) {
        Jedis jedis = new Jedis("redis://localhost:6379/10");
        jedis.flushDB();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        pool = new JedisPool("redis://localhost:6379/10");
    }

    public Jedis getJedis() {
        return pool.getResource();
    }
    //用set数据结构来实现add, remove, ismember等功能
    public long sadd(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sadd(key, value);

        } catch (Exception e) {
            logger.error("增加KV时发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long srem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.srem(key, value);
        } catch (Exception e) {
            logger.error("删除KV时发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public long scard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.scard(key);

        } catch (Exception e) {
            logger.error("统计K的数量时发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public boolean sismember(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.sismember(key, value);

        } catch (Exception e) {
            logger.error("sismember()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return false;
    }

    public void lpush(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            jedis.lpush(key, value);

        } catch (Exception e) {
            logger.error("lpush()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public List<String> brpop(int timeout, String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.brpop(timeout, key);
        } catch (Exception e) {
            logger.error("brpush()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public List<String> lrange(String key, int start, int stop) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.lrange(key, start, stop);
        } catch (Exception e) {
            logger.error("lrange()发生异常" + e.getMessage());
            return null;
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
    }

    public Transaction multi(Jedis jedis) {
        try {
            Transaction tx = jedis.multi();
            return tx;
        } catch (Exception e) {
            logger.error("multi()发生异常" + e.getMessage());
        } finally {
            //Is it okay not to close the jedis?
//            if (jedis != null) {
//                jedis.close();
//            }
        }
        return null;
    }

    public List<Object> exec(Transaction tx, Jedis jedis) {
        try {
            return tx.exec();
        } catch (Exception e) {
            logger.error("exec()发生异常" + e.getMessage());
        } finally {
            if (tx != null) {
                try {
                    tx.close();
                } catch (Exception e) {
                    logger.error("failed to close tx" + e.getMessage());
                }
            }

            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zadd(String key, double score, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zadd(key, score, value);
        } catch (Exception e) {
            logger.error("zadd()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0L;
    }

    public long zrem(String key, String value) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrem(key, value);
        } catch (Exception e) {
            logger.error("zrem()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0L;
    }

    public Set<String> zrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrange(key, start, end);
        } catch (Exception e) {
            logger.error("zrange()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public Set<String> zrevrange(String key, int start, int end) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zrevrange(key, start, end);
        } catch (Exception e) {
            logger.error("zrevrange()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }

    public long zcard(String key) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zcard(key);
        } catch (Exception e) {
            logger.error("zcard()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return 0;
    }

    public Double zscore(String key, String member) {
        Jedis jedis = null;
        try {
            jedis = pool.getResource();
            return jedis.zscore(key, member);
        } catch (Exception e) {
            logger.error("zscore()发生异常" + e.getMessage());
        } finally {
            if (jedis != null) {
                jedis.close();
            }
        }
        return null;
    }
}
