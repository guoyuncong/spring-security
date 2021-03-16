package com.yc.security.base.utils;

import cn.hutool.core.util.StrUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

/**
 * @author caods
 * @date 2020/5/21
 * @description: yagena
 */
@Component
public class RedisUtil {

    @Autowired
    private RedisTemplate redisTemplate;

    private static RedisTemplate template;

    @PostConstruct
    public void init() {
        template = this.redisTemplate;
    }

    //- - - - - - - - - - - - - - - - - - - - -  公共方法 - - - - - - - - - - - - - - - - - - - -

    /**
     * 给一个指定的 key 值附加过期时间
     *
     * @param key
     * @param time
     * @return
     */
    public static boolean expire(String key, long time) {
        return template.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 根据key 获取过期时间
     *
     * @param key
     * @return
     */
    public static long getExpireTime(String key) {
        return template.getExpire(key, TimeUnit.SECONDS);
    }

    /**
     * 判断key 是否存在
     *
     * @param key
     * @return
     */
    public static boolean hasKey(String key) {
        return template.hasKey(key);
    }

    /**
     * 移除指定key 的过期时间
     *
     * @param key
     * @return
     */
    public static boolean persist(String key) {
        return template.boundValueOps(key).persist();
    }

    /**
     * 删除key 以及对应的缓存数据
     *
     * @param key
     */
    public static void deleteKey(String key) {

        template.delete(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  String类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 根据key获取值
     *
     * @param key 键
     * @return 值
     */
    public static String get(String key) {
        if (StrUtil.isEmpty(key)) {
            return null;
        }
        Object result = template.opsForValue().get(key);
        return result == null ? null : result.toString();
    }

    /**
     * 将值放入缓存
     *
     * @param key   键
     * @param value 值
     * @return true成功 false 失败
     */
    public static void set(String key, String value) {
        template.opsForValue().set(key, value);
    }

    /**
     * 将值放入缓存并设置过期时间
     *
     * @param key       键
     * @param value     值
     * @param time      过期时间
     * @param timeUnit  时间单位
     */
    public static void set(String key, String value, long time, TimeUnit timeUnit) {
        if (time > 0) {
            template.opsForValue().set(key, value, time, timeUnit);
        } else {
            template.opsForValue().set(key, value);
        }
    }

    /**
     * 批量添加 key (重复的键会覆盖)
     *
     * @param keyAndValue
     */
    public static void batchSet(Map<String, String> keyAndValue) {
        template.opsForValue().multiSet(keyAndValue);
    }

    /**
     * 批量添加 key-value 只有在键不存在时,才添加
     * map 中只要有一个key存在,则全部不添加
     *
     * @param keyAndValue
     */
    public static void batchSetIfAbsent(Map<String, String> keyAndValue) {
        template.opsForValue().multiSetIfAbsent(keyAndValue);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是长整型 ,将报错
     *
     * @param key
     * @param number
     */
    public static Long increment(String key, long number) {
        return template.opsForValue().increment(key, number);
    }

    /**
     * 对一个 key-value 的值进行加减操作,
     * 如果该 key 不存在 将创建一个key 并赋值该 number
     * 如果 key 存在,但 value 不是 纯数字 ,将报错
     *
     * @param key
     * @param number
     */
    public static Double increment(String key, double number) {
        return template.opsForValue().increment(key, number);
    }

    //- - - - - - - - - - - - - - - - - - - - -  set类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 随机获取并删除元素
     *
     * @return
     */
    public static String randGetAndDel(String key) {
        Object obj = template.opsForSet().pop(key);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    /**
     * 将数据放入set缓存
     *
     * @param key 键
     * @return
     */
    public static void sSet(String key, String... values) {
        template.opsForSet().add(key, values);
    }

    /**
     * 批量增加
     *
     * @return
     */
    public static Long batchSet(String key, Set<Object> values) {
        if (CollectionUtils.isEmpty(values)) {
            return null;
        }
        Object[] objs = values.toArray();
        return template.opsForSet().add(key, objs);
    }

    /**
     * 获取变量中的值
     *
     * @param key 键
     * @return
     */
    public static Set<Object> members(String key) {
        return template.opsForSet().members(key);
    }

    /**
     * 随机获取变量中指定个数的元素
     *
     * @param key   键
     * @param count 值
     * @return
     */
    public static void randomMembers(String key, long count) {
        template.opsForSet().randomMembers(key, count);
    }

    /**
     * 随机获取变量中的元素
     *
     * @param key 键
     * @return
     */
    public static Object randomMember(String key) {
        return template.opsForSet().randomMember(key);
    }

    /**
     * 弹出变量中的元素
     *
     * @param key 键
     * @return
     */
    public static Object pop(String key) {
        return template.opsForSet().pop(key);
    }

    /**
     * 获取变量中值的长度
     *
     * @param key 键
     * @return
     */
    public static long size(String key) {
        return template.opsForSet().size(key);
    }

    /**
     * 根据value从一个set中查询,是否存在
     *
     * @param key   键
     * @param value 值
     * @return true 存在 false不存在
     */
    public static boolean sHasKey(String key, Object value) {
        return template.opsForSet().isMember(key, value);
    }

    /**
     * 检查给定的元素是否在变量中。
     *
     * @param key 键
     * @param obj 元素对象
     * @return
     */
    public static boolean isMember(String key, Object obj) {
        return template.opsForSet().isMember(key, obj);
    }

    /**
     * 转移变量的元素值到目的变量。
     *
     * @param key     键
     * @param value   元素对象
     * @param destKey 元素对象
     * @return
     */
    public static boolean move(String key, String value, String destKey) {
        return template.opsForSet().move(key, value, destKey);
    }

    /**
     * 批量移除set缓存中元素
     *
     * @param key    键
     * @param values 值
     * @return
     */
    public static void remove(String key, Object... values) {
        template.opsForSet().remove(key, values);
    }

    /**
     * 通过给定的key求2个set变量的差值
     *
     * @param key     键
     * @param destKey 键
     * @return
     */
    public static Set<Set> difference(String key, String destKey) {
        return template.opsForSet().difference(key, destKey);
    }


    //- - - - - - - - - - - - - - - - - - - - -  hash类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 加入缓存
     *
     * @param key 键
     * @param map 键
     * @param map 键
     * @return
     */
    public static void add(String key, Map<String, String> map, Long time) {
        template.opsForHash().putAll(key, map);
        template.expire(key, time, TimeUnit.SECONDS);
    }

    /**
     * 获取 key 下的 所有  hashkey 和 value
     *
     * @param key 键
     * @return
     */
    public static Map<Object, Object> getHashEntries(String key) {
        return template.opsForHash().entries(key);
    }

    /**
     * 验证指定 key 下 有没有指定的 hashkey
     *
     * @param key
     * @param hashKey
     * @return
     */
    public static boolean hashKey(String key, String hashKey) {
        return template.opsForHash().hasKey(key, hashKey);
    }

    /**
     * 获取指定key的值string
     *
     * @param key  键
     * @param key2 键
     * @return
     */
    public static String getMapString(String key, String key2) {
        Object obj = template.opsForHash().get(key, key2);
        if (obj != null) {
            return obj.toString();
        }
        return null;
    }

    /**
     * 获取指定的值Int
     *
     * @param key  键
     * @param key2 键
     * @return
     */
    public static Integer getMapInt(String key, String key2) {
        return (Integer) template.opsForHash().get("map1", "key1");
    }

    /**
     * 弹出元素并删除
     *
     * @param key 键
     * @return
     */
    public static String popValue(String key) {
        return template.opsForSet().pop(key).toString();
    }

    /**
     * 删除指定 hash 的 HashKey
     *
     * @param key
     * @param hashKeys
     * @return 删除成功的 数量
     */
    public static Long delete(String key, String... hashKeys) {
        return template.opsForHash().delete(key, hashKeys);
    }

    /**
     * 给指定 hash 的 hashkey 做增减操作
     *
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public static Long increment(String key, String hashKey, long number) {
        return template.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 给指定 hash 的 hashkey 做增减操作
     *
     * @param key
     * @param hashKey
     * @param number
     * @return
     */
    public static Double increment(String key, String hashKey, Double number) {
        return template.opsForHash().increment(key, hashKey, number);
    }

    /**
     * 获取 key 下的 所有 hashkey 字段
     *
     * @param key
     * @return
     */
    public static Set<Object> hashKeys(String key) {
        return template.opsForHash().keys(key);
    }

    /**
     * 获取指定 hash 下面的 键值对 数量
     *
     * @param key
     * @return
     */
    public static Long hashSize(String key) {
        return template.opsForHash().size(key);
    }

    //- - - - - - - - - - - - - - - - - - - - -  list类型 - - - - - - - - - - - - - - - - - - - -

    /**
     * 在变量左边添加元素值
     *
     * @param key
     * @param value
     * @return
     */
    public static void leftPush(String key, Object value) {
        template.opsForList().leftPush(key, value);
    }

    /**
     * 获取集合指定位置的值。
     *
     * @param key
     * @param index
     * @return
     */
    public static Object index(String key, long index) {
        return template.opsForList().index("list", 1);
    }

    /**
     * 获取指定区间的值。
     *
     * @param key
     * @param start
     * @param end
     * @return
     */
    public static List<Object> range(String key, long start, long end) {
        return template.opsForList().range(key, start, end);
    }

    /**
     * 把最后一个参数值放到指定集合的第一个出现中间参数的前面，
     * 如果中间参数值存在的话。
     *
     * @param key
     * @param pivot
     * @param value
     * @return
     */
    public static void leftPush(String key, String pivot, String value) {
        template.opsForList().leftPush(key, pivot, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key
     * @param values
     * @return
     */
    public static void leftPushAll(String key, String... values) {
//        template.opsForList().leftPushAll(key,"w","x","y");
        template.opsForList().leftPushAll(key, values);
    }

    /**
     * 向集合最右边添加元素。
     *
     * @param key
     * @param value
     * @return
     */
    public static void leftPushAll(String key, String value) {
        template.opsForList().rightPush(key, value);
    }

    /**
     * 向左边批量添加参数元素。
     *
     * @param key
     * @param values
     * @return
     */
    public static void rightPushAll(String key, String... values) {
        template.opsForList().rightPushAll(key, values);
    }

    /**
     * 以集合方式向右边添加元素。
     *
     * @param key
     * @param values
     * @return
     */
    public static void rightPushAll(String key, Collection values) {
        template.opsForList().rightPushAll(key, values);
    }

    /**
     * 向已存在的集合中添加元素。
     *
     * @param key
     * @param value
     * @return
     */
    public static void rightPushIfPresent(String key, Object value) {
        template.opsForList().rightPushIfPresent(key, value);
    }

    /**
     * 向已存在的集合中添加元素。
     *
     * @param key
     * @return
     */
    public static long listLength(String key) {
        return template.opsForList().size(key);
    }

    /**
     * 移除集合中的左边第一个元素。
     *
     * @param key
     * @return
     */
    public static void leftPop(String key) {
        template.opsForList().leftPop(key);
    }

    /**
     * 移除集合中左边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key
     * @return
     */
    public static void leftPop(String key, long timeout, TimeUnit unit) {
        template.opsForList().leftPop(key, timeout, unit);
    }

    /**
     * 移除集合中右边的元素。
     *
     * @param key
     * @return
     */
    public static void rightPop(String key) {
        template.opsForList().rightPop(key);
    }

    /**
     * 移除集合中右边的元素在等待的时间里，如果超过等待的时间仍没有元素则退出。
     *
     * @param key
     * @return
     */
    public static void rightPop(String key, long timeout, TimeUnit unit) {
        template.opsForList().rightPop(key, timeout, unit);
    }

}
