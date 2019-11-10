package com.xuecheng.test.rabbitmq.productor;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class Producer03_routing {
        //队列名称
        private static final String QUEUE_INFORM_EMAIL = "queue_inform_email";
        private static final String QUEUE_INFORM_SMS = "queue_inform_sms";
        private static final String EXCHANGE_ROUTING_INFORM="exchange_routing_inform";
        private static final String ROUTINGKEY_EMAIL="inform_email";
        private static final String ROUTINGKEY_SMS="inform_sms";
        public static void main(String[] args) {
            //通过连接工厂创建新的连接和mq建立连接
            ConnectionFactory connectionFactory = new ConnectionFactory();
            connectionFactory.setHost("127.0.0.1");
            connectionFactory.setPort(5672);//端口
            connectionFactory.setUsername("guest");
            connectionFactory.setPassword("guest");
            //设置虚拟机，一个mq服务可以设置多个虚拟机，每个虚拟机就相当于一个独立的mq
            connectionFactory.setVirtualHost("/");

            Connection connection = null;
            Channel channel = null;

            //System.out.println("connectionFactory:"+connectionFactory);
            try {
                connection = connectionFactory.newConnection();
                channel = connection.createChannel();

                channel.queueDeclare(QUEUE_INFORM_EMAIL,true,false,false,null);
                channel.queueDeclare(QUEUE_INFORM_SMS,true,false,false,null);
                //声明一个交换机
                //参数：String exchange, String type
                /**
                 * 参数明细：
                 * 1、交换机的名称
                 * 2、交换机的类型
                 * fanout：对应的rabbitmq的工作模式是 publish/subscribe
                 * direct：对应的Routing	工作模式
                 * topic：对应的Topics工作模式
                 * headers： 对应的headers工作模式
                 */
                channel.exchangeDeclare(EXCHANGE_ROUTING_INFORM, BuiltinExchangeType.DIRECT);
                //进行交换机和队列绑定
                //参数：String queue, String exchange, String routingKey
                /**
                 * 参数明细：
                 * 1、queue 队列名称
                 * 2、exchange 交换机名称
                 * 3、routingKey 路由key，作用是交换机根据路由key的值将消息转发到指定的队列中，在发布订阅模式中调协为空字符串
                 */

                channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,"inform");
                channel.queueBind(QUEUE_INFORM_EMAIL,EXCHANGE_ROUTING_INFORM,ROUTINGKEY_EMAIL);
                channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,ROUTINGKEY_SMS);
                channel.queueBind(QUEUE_INFORM_SMS,EXCHANGE_ROUTING_INFORM,"inform");

               /* for(int i=0;i<5;i++){
                //发送消息的时候指定routingKey
                String message = "send email inform message to user";
                channel.basicPublish(EXCHANGE_ROUTING_INFORM,ROUTINGKEY_EMAIL,null,message.getBytes());
                System.out.println("send to mq "+message);
                }
                for(int i=0;i<5;i++){
                    //发送消息的时候指定routingKey
                    String message = "send sms inform message to user";
                    channel.basicPublish(EXCHANGE_ROUTING_INFORM,ROUTINGKEY_SMS,null,message.getBytes());
                    System.out.println("send to mq "+message);
                }*/
                for(int i=0;i<5;i++){
                    //发送消息的时候指定routingKey
                    String message = "send inform message to user";
                    channel.basicPublish(EXCHANGE_ROUTING_INFORM,"inform",null,message.getBytes());
                    System.out.println("send to mq "+message);
                }

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                //关闭连接
                //先关闭通道
                try {
                    channel.close();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
                try {
                    connection.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
}
