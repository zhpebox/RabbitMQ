package com.bean.testModelsend;

import java.io.ByteArrayOutputStream;
import java.io.ObjectOutputStream;

import com.bean.BookBean;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class Send
{
	//队列名称
	private final static String QUEUE_NAME = "ty";

	public static void main(String[] args) throws java.io.IOException
	{
		/**
		 * 创建连接连接到MabbitMQ
		 */
		ConnectionFactory factory = new ConnectionFactory();
		//设置MabbitMQ所在主机ip或者主机名
		factory.setHost("localhost");
		factory.setPort(5672);
		factory.setUsername("admin");
		factory.setPassword("admin");
		
		//创建一个连接
		Connection connection = factory.newConnection();
		//创建一个频道
		Channel channel = connection.createChannel();
		//指定一个队列
		channel.queueDeclare(QUEUE_NAME, false, false, false, null);
		
		//发送的消息
//		String message = "hello V world!zhp";
		
		BookBean bean = new BookBean();
		bean.setBookId(5);
		bean.setBookName("水浒传");
		bean.setBookEditer(new String[]{"吴承恩","施耐庵"});
		
		ByteArrayOutputStream outputByte = new ByteArrayOutputStream();
		ObjectOutputStream objectObjectStream = new ObjectOutputStream(outputByte);
		objectObjectStream.writeObject(bean);
		String serStr = outputByte.toString("ISO-8859-1");
		serStr = java.net.URLEncoder.encode(serStr,"UTF-8");
		objectObjectStream.close();
		objectObjectStream.close();
		
		//往队列中发出一条消息
		channel.basicPublish("", QUEUE_NAME, null, serStr.getBytes());
		//关闭频道和连接
		System.out.println("send is "+serStr.getBytes());
		channel.close();
		connection.close();
	 }
}
