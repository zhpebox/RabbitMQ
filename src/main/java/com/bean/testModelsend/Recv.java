package com.bean.testModelsend;

import java.io.ByteArrayInputStream;
import java.io.ObjectInputStream;

import com.bean.BookBean;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.QueueingConsumer;
import com.sun.xml.internal.messaging.saaj.util.ByteInputStream;

public class Recv
{
	//队列名称
	private final static String QUEUE_NAME = "ty";

	public static void main(String[] argv) throws java.io.IOException,
			java.lang.InterruptedException, ClassNotFoundException
	{
		//打开连接和创建频道，与发送端一样
		ConnectionFactory factory = new ConnectionFactory();
		factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");
		
		Connection connection = factory.newConnection();
		Channel channel = connection.createChannel();
		//声明队列，主要为了防止消息接收者先运行此程序，队列还不存在时创建队列。
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
		
		//创建队列消费者
		QueueingConsumer consumer = new QueueingConsumer(channel);
		//指定消费队列
		channel.basicConsume(QUEUE_NAME, true, consumer);
		while (true)
		{
			//nextDelivery是一个阻塞方法（内部实现其实是阻塞队列的take方法）
			QueueingConsumer.Delivery delivery = consumer.nextDelivery();
			String message = new String(delivery.getBody());
			String redStr = java.net.URLDecoder.decode(message,"UTF-8");
			
			ByteArrayInputStream input = new ByteArrayInputStream(redStr.getBytes("ISO-8859-1"));
			ObjectInputStream objectIn = new ObjectInputStream(input);
			BookBean obj = (BookBean)objectIn.readObject();
				
			System.out.println(obj.getBookId());
			System.out.println(obj.getBookName());
			System.out.println(obj.getBookEditer()[1]);
			
			System.out.println(" [x] Received '" + message + "'");
		}

	}
}
