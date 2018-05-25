package sample;

import org.eclipse.paho.client.mqttv3.*;

import java.util.Objects;

public class ServerBackend implements MqttCallback{

    private MQTTConfig conf;
    private MqttClient client;
    private Rollo rollo;

    public ServerBackend(){
        conf = new MQTTConfig();
        rollo = new RolloImpl();

        rollo.setCallback( r -> {
            Rollo ro = (Rollo)r;
            if(Objects.equals(ro.getStatus(), "0") ){
                sendMessage();
            }
            return null;
        });

        try {
            client = new MqttClient(conf.getBroker(), conf.getClientId());

            client.setCallback(this);

            client.connect(conf.getConnOpts());

            //subscribing Client to Topic
            client.subscribe(conf.getTopic(), 2);
        } catch (MqttException e) {
            System.out.println("Error starting MqttClient " + e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        String msg = new String(mqttMessage.getPayload());
        System.out.println("Message arrived at Server: " + msg);

        if(msg.equals("<0")){
            rollo.Down();
            System.out.println("down is done in backend");
        }else if(msg.equals(">0")){
            rollo.Up();
            System.out.println("up is done in backend");
        }
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery complete");
    }

    //always send 0 if you are ready
    private void sendMessage(){
        //create new Message
        MqttMessage msq = new MqttMessage();
        msq.setPayload("0".getBytes());
        msq.setQos(conf.getQos());

        //send message
        System.out.println("Sending Message to Client: " + "0");
        try {
            client.publish(conf.getTopic(), msq);
        } catch (MqttException e) {
            System.out.println("Sending Message failed!");
            e.printStackTrace();
        }
    }
}
