import redis.clients.jedis.Jedis;
 
public class RedisJava {
    public static void main(String[] args) {
        //���ӱ��ص� Redis ����
        Jedis jedis = new Jedis("192.168.21.130");
        System.out.println("���ӳɹ�");
        jedis.auth("hbwang");
        //�鿴�����Ƿ�����
        System.out.println("������������: "+jedis.ping());
    }
}