package com.axing.points_ms;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

/**
 * @projectName: points_management_system
 * @package: com.axing.points_ms
 * @className: RedisMessageQueueExample
 * @author: Axing
 * @description: TODO
 * @date: 2023/7/14 9:59
 * @version: 1.0
 */

public class RedisMessageQueueExample {

    private static final String REDIS_HOST = "localhost";
    private static final int REDIS_PORT = 6379;
    private static final String MESSAGE_QUEUE_KEY = "message_queue";

    private JedisPool jedisPool;

    public RedisMessageQueueExample() {
        JedisPoolConfig poolConfig = new JedisPoolConfig();
        poolConfig.setMaxTotal(100);
        this.jedisPool = new JedisPool(poolConfig, REDIS_HOST, REDIS_PORT);
    }

    public void sendMessage(String message) {
        try (Jedis jedis = jedisPool.getResource()) {
            jedis.lpush(MESSAGE_QUEUE_KEY, message);
        }
    }

    public String receiveMessage() {
        try (Jedis jedis = jedisPool.getResource()) {
            return jedis.rpop(MESSAGE_QUEUE_KEY);
        }
    }

    public static void main(String[] args) {
        RedisMessageQueueExample queue = new RedisMessageQueueExample();
        queue.sendMessage("Hello, Redis!");
        String message = queue.receiveMessage();
        System.out.println(message); // prints "Hello, Redis!"
    }
}
