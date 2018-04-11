
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPool;
import redis.clients.jedis.JedisPoolConfig;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.ZParams;

/**
 * redis����api
 * 
 * @author Kazz
 *
 */
public class RedisDemo {

	private static JedisPool jedisPool = null;

	public static void main(String[] args) throws Exception {

		// �������򵥵�redis����ʾ�����������Ƽ����Ƽ�ʹ�����ݿ����ӳ�
		 //Jedis jedis = new Jedis("192.168.21.130", 6379);// ���� Redis ����
		 //jedis.auth("123456"); // ��������
		 //System.out.println("Server is running: " + jedis.ping());
		// �鿴�����Ƿ�����

		init();
		string();

		/*list();
		
		set();
		
		sets();
		
		hash();

		zset();

		zsets();

		publisher();

		subscribe();*/
	}

	/**
	 * ��ʼ��redis���ӳ�
	 */
	private static void init() {
		JedisPoolConfig config = new JedisPoolConfig(); // Jedis���ӳ�
		config.setMaxIdle(8); // ������������
		config.setMaxWait(5000); // ��ȡ�����ǵ����ȴ�ʱ�䣬�����ʱ���׳��쳣
		config.setTestOnBorrow(false);// ��borrowһ��jedisʵ��ʱ���Ƿ���ǰ����validate���������Ϊtrue����õ���jedisʵ�����ǿ��õģ�
		config.setTestOnReturn(true);
		jedisPool = new JedisPool(config, "192.168.21.130", 6379, 10000, "hbwang", 0); // ���á�ip���˿ڡ����ӳ�ʱʱ�䡢���롢���ݿ��ţ�0~15��
	}

	/**
	 * string���͵Ļ���������string��redis��������������ͣ��ܶ�����������������������õģ���del��exists��expire
	 * 
	 * @throws Exception
	 * 
	 */
	private static void string() throws Exception {
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB(); // ������ݿ�

		jedis.set("testString", "123"); // ��redis�з����ַ���
		System.out.println("��redis�л�ȡ�ոշŽ�ȥ��testString��" + jedis.get("testString"));

		jedis.incr("testString"); // ������������testInt�����������1����������ַ����������ᱨJedisDataException
		System.out.println("��redis�л�ȡ�������testString��" + jedis.get("testString"));

		jedis.decr("testString"); // �Լ���Ч��ͬ����
		System.out.println("��redis�л�ȡ�Լ����testString��" + jedis.get("testString"));
		// incrby���������Զ�Ҫ���Ӷ���

		jedis.append("testString", "456abcd"); // �ں���׷��
		System.out.println("��redis�л�ȡ׷�Ӻ��testString��" + jedis.get("testString"));

		String sub = jedis.substr("testString", 2, 6); // �и��ַ���
		System.out.println("substr�����ķ���ֵ��" + sub);
		System.out.println("��redis�л�ȡ�и���testString��" + jedis.get("testString")); // ���Կ�����substr�����������ƻ�ԭ��ֵ��ֻ��ȡ�����ӹ�����

		jedis.rename("testString", "newString"); // �ֶθ�����ֵ�����
		System.out.println("testString������newString��ֵΪ��" + jedis.get("newString"));

		String type = jedis.type("newString");// ��ȡ����������
		System.out.println("newString�����������ǣ�" + type);

		long length = jedis.strlen("newString"); // ��ȡ�ַ�������
		System.out.println("newString���ַ�������Ϊ��" + length);

		jedis.set("testString6", "����");
		jedis.set("testString7", "�Ǻ�");
		jedis.set("testString8", "helloword");
		jedis.set("testString99", "SMSP");
		Set<String> keys = jedis.keys("*"); // ��ȡ���з��������ļ�
		System.out.println("����redis�����еļ���" + keys);
		keys = jedis.keys("*String?");
		System.out.println("����redis�������������*String?�ļ���" + keys);

		jedis.del("testString"); // �ַ���ɾ��
		System.out.println("��redisɾ��testInt��testInt�Ƿ񻹴��ڣ�" + jedis.exists("testString"));
		System.out.println();

		jedis.set("testString2", "��ð�������");
		jedis.expire("testString2", 2); // ������Ч�ڣ���λ����
		System.out.println("��redis�л�ȡtestString2��ֵΪ��" + jedis.get("testString2"));
		Thread.sleep(3000);
		System.out.println("3����redis�л�ȡtestString2��ֵΪ��" + jedis.get("testString2")); // �����ˣ����Ҳ������ֶΣ�����null
		// ttl�������Է���ʣ����Чʱ�䣬expire���������ָ��ʱ�䣬���ǽ����ֶ���Ч����Ϊ����
		System.out.println();
		System.out.println();
	}

	/**
	 * list��Ļ���������������ظ�
	 * 
	 */
	private static void list() {
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB(); // ������ݿ�

		// �б�Ĳ������ȡ(�����ظ�)
		jedis.lpush("testList", "Redis"); // ����߲���
		jedis.lpush("testList", "Mongodb");
		jedis.lpush("testList", "Mysql");
		jedis.lpush("testList", "Mysql");
		jedis.rpush("testList", "DB2"); // ���ұ߲���

		List<String> list = jedis.lrange("testList", 0, -1); // �����ұ�����3�������ֱ��ǣ�key����ʼλ�ã�����λ�ã�-1�������
		for (int i = 0; i < list.size(); i++) {
			System.out.printf("��redis�л�ȡ�ոշŽ�ȥ��testList[%d]: %s\n", i, list.get(i));
		}

		System.out.println();
		String lpop = jedis.lpop("testList"); // ɾ������ߵ��Ǹ�
		String rpop = jedis.rpop("testList"); // ɾ�����ұߵ��Ǹ�
		System.out.printf("��ɾ�����Ԫ���ǣ�%s����ɾ���ұ�Ԫ���ǣ�%s\n", lpop, rpop);

		list = jedis.lrange("testList", 0, -1);
		for (int i = 0; i < list.size(); i++) {
			System.out.printf("��redis�л�ȡ��ɾ�����testList[%d]: %s\n", i, list.get(i));
		}

		System.out.println();
		jedis.ltrim("testList", 1, 2); // �ü��б����������ֱ��ǣ�key����ʼλ�ã�����λ��
		list = jedis.lrange("testList", 0, -1);
		for (int i = 0; i < list.size(); i++) {
			System.out.printf("��redis�л�ȡ���ü����testList[%d]: %s\n", i, list.get(i));
		}

		jedis.del("testList"); // ɾ���б�
		System.out.println("��redisɾ��testList��testList�Ƿ񻹴��ڣ�" + jedis.exists("testList"));
		System.out.println();
		System.out.println();
	}

	/**
	 * �������͵Ļ��������������ظ�
	 */
	private static void set() {
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB(); // ������ݿ�

		jedis.sadd("testSet", "lida", "wch", "chf", "lxl", "wch"); // ���Ԫ�أ������ظ�

		Set<String> set = jedis.smembers("testSet"); // ��ȡ�����е�ȫ��Ԫ��
		System.out.println("��testSet�л�ȡ��Ԫ�أ�" + set);

		long length = jedis.scard("testSet"); // �󼯺ϵĳ���
		System.out.println("\n��ȡtestSet�ĳ��ȣ�" + length);
		System.out.println();

		jedis.srem("testSet", "wch"); // ��testSet�Ƴ�wch
		set = jedis.smembers("testSet");
		System.out.println("��testSet�л�ȡ�Ƴ���ĵ�Ԫ�أ�" + set);
		System.out.println();

		boolean exist = jedis.sismember("testSet", "lida"); // �ж�Ԫ���Ƿ�����ڸü�����
		System.out.println("���lida�Ƿ������testSet�У�" + exist);
		System.out.println();

		String spop = jedis.spop("testSet");// ������Ƴ�spop�е�һ��Ԫ�أ���������
		System.out.println("testSet�б�����Ƴ���Ԫ���ǣ�" + spop);
		System.out.println();

		jedis.del("testSet"); // ɾ����������
		System.out.println("ɾ����testSet�Ƿ��Ǵ��ڣ�" + jedis.exists("testSet"));
		System.out.println();
		System.out.println();

	}

	/**
	 * ����֮������㣬�������������
	 */
	private static void sets() {
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB(); // ������ݿ�
		jedis.sadd("set1", "a", "b", "c", "d");
		jedis.sadd("set2", "b", "c", "e");

		Set<String> set = jedis.sdiff("set1", "set2"); // ���������ϵĲ��ֻ�᷵�ش�����1����2�����ڵģ�
		System.out.println("�����������֮��Ĳ��" + set); // �����a��d
		// ����һ��sdiffstore��api�����԰�sdiff�ļ�������ֵ����һ��set�У�����Ľ����Ͳ���Ҳ����
		System.out.println();

		set = jedis.sinter("set1", "set2"); // ���������ϵĽ���
		System.out.println("�����������֮��Ľ�����" + set); // �����b��c
		System.out.println();

		set = jedis.sunion("set1", "set2"); // ���������ϵĲ���
		System.out.println("�����������֮��Ĳ�����" + set);
		System.out.println();
		System.out.println();

	}

	/**
	 * ɢ�еĻ�������,��ֵ�����滹�м�ֵ�ԣ����������洢����ֶ���Ϣ��Ҳ�������Ϊ���һ��map��ɢ����redis�Ĵ洢ԭ��
	 */
	private static void hash() {
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB(); // ������ݿ�

		Map<String, String> map = new HashMap<String, String>();
		map.put("k1", "v1");
		map.put("k2", "v2");
		map.put("k3", "v3");
		map.put("k4", "123");
		jedis.hmset("hash1", map); // ���һ��ɢ��

		Map<String, String> getMap = jedis.hgetAll("hash1"); // ��redis��ȡ����
		System.out.println("��redis��ȡ�ص�hash1ɢ�У�" + getMap.toString());
		System.out.println();

		List<String> hmget = jedis.hmget("hash1", "k1", "k3"); // ��ɢ����ȡ��һ�������ֶ���Ϣ
		System.out.println("��hash1ɢ���������ֶ���������" + hmget);
		System.out.println();

		jedis.hdel("hash1", "k1"); // ɾ��ɢ���е�һ�����߶���ֶ�
		getMap = jedis.hgetAll("hash1");
		System.out.println("��redis��ȡ�صı�ɾ�����hash1ɢ�У�" + getMap);
		System.out.println();

		long length = jedis.hlen("hash1"); // ������ϵĳ���
		System.out.println("ɢ��hash1�ĳ���Ϊ��" + length);
		System.out.println();

		boolean exists = jedis.hexists("hash1", "k5"); // �ж�ĳ���ֶ��Ƿ������ɢ����
		System.out.println("k5�ֶ��Ƿ������ɢ���У�" + exists);
		System.out.println();

		Set<String> keys = jedis.hkeys("hash1"); // ��ȡɢ�е������ֶ���
		System.out.println("hash1�������ֶ�����" + keys);
		System.out.println();

		List<String> values = jedis.hvals("hash1"); // ��ȡɢ�е������ֶ�ֵ��ʵ�ʵķ���ʵ�֣����������hkeys������hmget
		System.out.println("hash1�������ֶ�ֵ��" + values);
		System.out.println();

		jedis.hincrBy("hash1", "k4", 10); // ��ɢ�е�ĳ���ֶν��мӷ�����
		System.out.println("ִ�мӷ����к��hash1ɢ�У�" + jedis.hgetAll("hash1"));
		System.out.println();

		jedis.del("hash1"); // ɾ��ɢ��
		System.out.println("ɾ��hash1��hash1�Ƿ񻹴���redis�У�" + jedis.exists("hash1"));
		System.out.println();
		System.out.println();

	}

	/**
	 * ���򼯺ϵĻ���ʹ�ã�zset��set�������棬������Ļ����ϣ�������һ��Ȩ�أ�ʹ������<br/>
	 * ��һ����⣬zset��hash������棬һ���Ĵ��һЩ��ֵ�ԣ��������ֵֻ�������֣��������ַ���<br/>
	 * zset�㷺Ӧ����������ĳ���
	 */
	private static void zset() {
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB(); // ������ݿ�

		Map<Double, String> map = new HashMap<Double, String>();
		map.put(24.3,"wch"); // ������С���Ա����������ʾ
		map.put( 30.0,"lida");
		map.put(23.5,"chf");
		map.put(22.1,"lxl" );
		map.put(24.3,"wch" ); // ������ᱻ���룬Ӧ���ظ���

		jedis.zadd("zset1", map); // ���һ��zset

		Set<String> range = jedis.zrange("zset1", 0, -1); // ��С�������򣬷������г�Ա������������������ʼλ�á�����λ�ã�-1����ȫ����
		// zrange�������кܶ������ķ�������zrangeByScore�ȣ�ֻ�Ƕ���һЩ������ɸѡ��Χ���ѣ��Ƚϼ򵥣��Լ�����api��֪����
		System.out.println("zset���ص����д�С������ĳ�Ա��" + range);
		System.out.println("");

		Set<String> revrange = jedis.zrevrange("zset1", 0, -1); // �Ӵ�С�������������range
		System.out.println("zset���ص���������ĳ�Ա��" + revrange);
		System.out.println("");

		long length = jedis.zcard("zset1"); // ����Ч����
		System.out.println("zset1�ĳ��ȣ�" + length);
		System.out.println();

		long zcount = jedis.zcount("zset1", 22.1, 30.0); // ���zset�У�������Ա������֮�ע�ⲻ���󳤶ȣ�
		System.out.println("zset1�У�22.1��30.0����" + zcount + "��");
		System.out.println();

		long zrank = jedis.zrank("zset1", "wch"); // ���zset��ĳ��Ա����λ��ע���һ�Ǵ�0��ʼ��
		System.out.println("wch��zset1��������" + zrank);
		System.out.println();

		double zscore = jedis.zscore("zset1", "lida"); // ��ȡzset��ĳ��Ա��ֵ
		System.out.println("zset1��lida��ֵΪ��" + zscore);
		System.out.println();

		jedis.zincrby("zset1", 10, "lxl"); // ��zset�е�ĳ��Ա���ӷ�����
		System.out.println("zset1��lxl��10���������Ϊ��" + jedis.zrange("zset1", 0, -1));
		System.out.println();

		jedis.zrem("zset1", "chf"); // ɾ��zset��ĳ����Ա
		// zrem����������zremByScore��zremByRank���ֱ���ɾ��ĳ�������������������ĳ�Ա
		System.out.println("zset1ɾ��chf��ʣ�£�" + jedis.zrange("zset1", 0, -1));
		System.out.println();

	}

	/**
	 * ���򼯺ϵ����㣬��������������С������ܺͣ�
	 */
	private static void zsets() {
		Jedis jedis = jedisPool.getResource();
		jedis.flushDB(); // ������ݿ�

		Map<Double, String> map1 = new HashMap<Double, String>();
		map1.put(24.3,"wch"); // ������С���Ա����������ʾ
		map1.put(30.0,"lida" );
		map1.put(23.5,"chf" );
		map1.put(22.1,"lxl" );

		Map<Double, String> map2 = new HashMap<Double, String>();
		map2.put(24.3,"wch");
		map2.put(29.6,"lly");
		map2.put(23.5,"chf");
		map2.put(21.3,"zjl");

		jedis.zadd("zset1", map1);
		jedis.zadd("zset2", map2);

		System.out.println("zset1��ֵ�У�" + jedis.zrangeWithScores("zset1", 0, -1));
		System.out.println("zset2��ֵ�У�" + jedis.zrangeWithScores("zset2", 0, -1));
		System.out.println();

		jedis.zinterstore("zset_inter", "zset1", "zset2"); // ���������Ͻ��н������㣬��������ֵ��zset_inter��
		System.out.println("��������zset������������" + jedis.zrangeWithScores("zset_inter", 0, -1));

		jedis.zunionstore("zset_union", "zset1", "zset2");// ���������Ͻ��в������㣬��������ֵ��zset_union��
		System.out.println("��������zset������������" + jedis.zrangeWithScores("zset_union", 0, -1));
		System.out.println("���Կ�����zset�Ľ����Ͳ������㣬Ĭ�ϻ������zset��score���");

		ZParams zParams = new ZParams();
		zParams.aggregate(ZParams.Aggregate.MAX);
		jedis.zinterstore("zset_inter", zParams, "zset1", "zset2"); // ͨ��ָ��ZParams�����ü��������score������MAX MIN SUM��������ѡ��Ĭ����SUM
		System.out.println("��������zset����max��������" + jedis.zrangeWithScores("zset_inter", 0, -1));

		//zrangeWithScores���ص���һ��Set<Tuple>���ͣ����ֱ�Ӱ�������ϴ�ӡ���������zset��keyת��ascii�룬��������ֱ�ۣ����黹��ʹ��foreach֮��ı�����ÿ�һЩ
		
	}

	/**
	 * ������Ϣ��������mq��������
	 */
	private static void publisher() {

		new Thread() {
			public void run() {
				try {
					Thread.sleep(1000); // ����һ�£��ö������г����ʱ��ȥ����
					Jedis jedis = jedisPool.getResource();
					jedis.flushAll();

					for (int i = 0; i < 10; i++) {
						jedis.publish("channel", "Ҫ���͵���Ϣ����" + i); // ÿ��һ������һ����Ϣ
						System.out.printf("�ɹ���channel������Ϣ��%s\n", i);
						Thread.sleep(1000);
					}


				} catch (Exception e) {
					e.printStackTrace();
				}

			};
		}.start();
	}

	/**
	 * ������Ϣ��������mq��������
	 * 
	 */
	private static void subscribe(){
		Jedis jedis = jedisPool.getResource();
		jedis.flushAll();
		JedisListener listener = new JedisListener();
		listener.proceed(jedis.getClient(), "channel"); // ��ʼ����channelƵ������Ϣ
		//listener.unsubscribe(); //ȡ������
	}

	/**
	 * ��д��������һЩ��Ҫ������JedisPubSub�������Щ�ص��������ǿյģ�����д��ʲô�¶����ᷢ��
	 * 
	 * @author Kazz
	 *
	 */
	private static class JedisListener extends JedisPubSub {

		/**
		 * �յ���Ϣ��Ļص�
		 */
		@Override
		public void onMessage(String channel, String message) {
			System.out.println("onMessage: �յ�Ƶ��[" + channel + "]����Ϣ[" + message + "]");
		}

		@Override
		public void onPMessage(String pattern, String channel, String message) {
			System.out.println("onPMessage: channel[" + channel + "], message[" + message + "]");
		}

		/**
		 * �ɹ�����Ƶ����Ļص�
		 */
		@Override
		public void onSubscribe(String channel, int subscribedChannels) {
			System.out
					.println("onSubscribe: �ɹ�����[" + channel + "]," + "subscribedChannels[" + subscribedChannels + "]");
		}

		/**
		 * ȡ������Ƶ���Ļص�
		 */
		@Override
		public void onUnsubscribe(String channel, int subscribedChannels) {
			System.out.println(
					"onUnsubscribe: �ɹ�ȡ������[" + channel + "], " + "subscribedChannels[" + subscribedChannels + "]");
		}

		@Override
		public void onPUnsubscribe(String pattern, int subscribedChannels) {
			System.out.println(
					"onPUnsubscribe: pattern[" + pattern + "]," + "subscribedChannels[" + subscribedChannels + "]");
		}

		@Override
		public void onPSubscribe(String pattern, int subscribedChannels) {
			System.out.println(
					"onPSubscribe: pattern[" + pattern + "], " + "subscribedChannels[" + subscribedChannels + "]");
		}

	}
}

