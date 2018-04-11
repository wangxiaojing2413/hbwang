import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;

public class RedisUtils {  
  
     //Redis������IP  
     private static String ADDR = "192.168.21.130";  
      //Redis�Ķ˿ں�  
     private static int PORT = 6379;  
     //��������ʵ���������Ŀ��Ĭ��ֵΪ8��  
     //�����ֵΪ-1�����ʾ�����ƣ����pool�Ѿ�������maxActive��jedisʵ�������ʱpool��״̬Ϊexhausted(�ľ�)��  
     private static int MAX_ACTIVE = 1024;  
     //����һ��pool����ж��ٸ�״̬Ϊidle(���е�)��jedisʵ����Ĭ��ֵҲ��8��  
     private static int MAX_IDLE = 200;  
     //�ȴ��������ӵ����ʱ�䣬��λ���룬Ĭ��ֵΪ-1����ʾ������ʱ����������ȴ�ʱ�䣬��ֱ���׳�JedisConnectionException��  
     private static int MAX_WAIT = 10000;  
           
     //��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�  
     private static boolean TEST_ON_BORROW = true;  
     private static JedisPool jedisPool = null;  
     /** 
      * ��ʼ��Redis���ӳ� 
      */  
      static {  
          try {  
               JedisPoolConfig config = new JedisPoolConfig();  
               config.setMaxActive(MAX_ACTIVE);
               config.setMaxIdle(MAX_IDLE);  //������������
               config.setMaxWait(MAX_WAIT);
               config.setTestOnBorrow(TEST_ON_BORROW);
               config.setTestOnReturn(TEST_ON_BORROW);
              //jedisPool = new JedisPool(config, ADDR, PORT);  
               jedisPool = new JedisPool(config, "192.168.21.130", 6379, 10000, "hbwang", 0); // ���á�ip���˿ڡ����ӳ�ʱʱ�䡢���롢���ݿ��ţ�0~15��
          } catch (Exception e) {  
               e.printStackTrace();  
          }  
      }  
        
      /** 
       * ��ȡJedisʵ�� 
       * @return 
       */  
      public synchronized static Jedis getJedis() {  
          try {  
              if (jedisPool != null) {  
                  Jedis resource = jedisPool.getResource();  
                  return resource;  
              } else {  
                  return null;  
              }  
          } catch (Exception e) {  
              e.printStackTrace();  
              return null;  
          }  
      }  
             
      /** 
       * �ͷ�jedis��Դ 
       * @param jedis 
       */  
       public static void returnResource(final Jedis jedis) {  
           if (jedis != null) {  
                jedisPool.returnResourceObject(jedis);  
           }  
       }  
}