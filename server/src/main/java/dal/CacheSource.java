package dal;

import io.lettuce.core.RedisClient;
import io.lettuce.core.api.async.RedisAsyncCommands;

public class CacheSource {
  private static RedisAsyncCommands<String, String> connection;

  static {
    RedisClient redisClient = RedisClient.create(System.getenv("REDIS_INFO"));
    connection = redisClient.connect().async();
  }

  public static RedisAsyncCommands<String, String> getConnection() {
    return connection;
  }
}
