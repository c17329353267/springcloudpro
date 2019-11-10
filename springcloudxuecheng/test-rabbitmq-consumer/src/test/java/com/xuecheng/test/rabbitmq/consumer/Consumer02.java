package com.xuecheng.test.rabbitmq.consumer;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class Consumer02 {

    //定义队列名称
    public static final String RABBIT_QUEUE = "rabbit_queue";
    public static void main(String[] args) throws IOException, TimeoutException {

        Connection connction = null;
        Channel channel = null;
        //创建连接工程
        ConnectionFactory connectionFactory = new ConnectionFactory();
        connectionFactory.setHost("6.25.2.244");
        connectionFactory.setPort(5672);
        connectionFactory.setUsername("admin");
        connectionFactory.setPassword("admin");
        connectionFactory.setVirtualHost("/");

        //从连接工厂中获取连接

            connction = connectionFactory.newConnection();
            //从连接中获取管道
            channel = connction.createChannel();

            //监听队列
            //声明队列，如果队列在mq 中没有则要创建
            //参数：String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
            /**
             * 参数明细
             * 1、queue 队列名称
             * 2、durable 是否持久化，如果持久化，mq重启后队列还在
             * 3、exclusive 是否独占连接，队列只允许在该连接中访问，如果connection连接关闭队列则自动删除,如果将此参数设置true可用于临时队列的创建
             * 4、autoDelete 自动删除，队列不再使用时是否自动删除此队列，如果将此参数和exclusive参数设置为true就可以实现临时队列（队列不用了就自动删除）
             * 5、arguments 参数，可以设置一个队列的扩展参数，比如：可设置存活时间
             */
            DefaultConsumer defaultConsumer = new DefaultConsumer(channel) {

                public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body) throws UnsupportedEncodingException {
                    //获取交换机
                    String exchange = envelope.getExchange();
                    //获取路由键key
                    String routingKey = envelope.getRoutingKey();
                    //获取消息内容id
                    long deliveryTag = envelope.getDeliveryTag();
                    String msg = new String(body,"utf-8");
                    System.out.println("receive message :"+msg);
                }
            };
            //监听队列
            //参数：String queue, boolean autoAck, Consumer callback
            /**
             * 参数明细：
             * 1、queue 队列名称
             * 2、autoAck 自动回复，当消费者接收到消息后要告诉mq消息已接收，如果将此参数设置为tru表示会自动回复mq，如果设置为false要通过编程实现回复
             * 3、callback，消费方法，当消费者接收到消息要执行的方法
             */
            channel.basicConsume(RABBIT_QUEUE,true,defaultConsumer);
    }
}
