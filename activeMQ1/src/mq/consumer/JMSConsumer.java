package mq.consumer;
import javax.jms.Connection;
import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageConsumer;
import javax.jms.MessageListener;
import javax.jms.Session;
import javax.jms.TextMessage;

import org.apache.activemq.ActiveMQConnection;
import org.apache.activemq.ActiveMQConnectionFactory;
/**
 * ��Ϣ�������ߣ������ߣ�
 * @author liang
 *
 */
public class JMSConsumer implements MessageListener{
	private static final String USERNAME = ActiveMQConnection.DEFAULT_USER;//Ĭ�������û���
    private static final String PASSWORD = ActiveMQConnection.DEFAULT_PASSWORD;//Ĭ����������
    private static final String BROKEURL = ActiveMQConnection.DEFAULT_BROKER_URL;//Ĭ�����ӵ�ַ

    public static void main(String[] args) {
        new JMSConsumer().receive();
    }

	public void receive() {
		ConnectionFactory connectionFactory;//���ӹ���
        Connection connection = null;//����

        Session session;//�Ự ���ܻ��߷�����Ϣ���߳�
        Destination destination;//��Ϣ��Ŀ�ĵ�

        MessageConsumer messageConsumer;//��Ϣ��������

        //ʵ�������ӹ���
        //connectionFactory = new ActiveMQConnectionFactory(JMSConsumer.USERNAME, JMSConsumer.PASSWORD, JMSConsumer.BROKEURL);
        connectionFactory = new ActiveMQConnectionFactory("dingsheng1", "123456", "tcp://localhost:61616");

        try {
            //ͨ�����ӹ�����ȡ����
            connection = connectionFactory.createConnection();
            //��������
            connection.start();
            //����session
            session = connection.createSession(false, Session.AUTO_ACKNOWLEDGE);
            //����һ������HelloWorld����Ϣ����
            destination = session.createQueue("HelloWorld");
            //������Ϣ������
            messageConsumer = session.createConsumer(destination);
            messageConsumer.setMessageListener(this);
        } catch (JMSException e) {
            e.printStackTrace();
        }
            /**
             * ����һ��ʵ��
             */
//            while (true) {
//                TextMessage textMessage = (TextMessage) messageConsumer.receive(100000);
//                if(textMessage != null){
//                    System.out.println("�յ�����Ϣ:" + textMessage.getText());
//                }else {
//                    break;
//                }
//            }
        
	}
	@Override
	public void onMessage(Message message) {
		// TODO Auto-generated method stub
		//�����Ϣ��TextMessage
		if(message instanceof TextMessage) {
			TextMessage textMsg = (TextMessage)message;
			try {
				System.out.println("�յ�����Ϣ--��"+textMsg.getStringProperty("name")+"����" + textMsg.getText());
			}catch(JMSException e) {
				e.printStackTrace();
			}
		}
	}
}
