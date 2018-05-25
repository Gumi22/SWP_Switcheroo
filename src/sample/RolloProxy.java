package sample;

import javafx.util.Callback;
import org.eclipse.paho.client.mqttv3.*;

public class RolloProxy implements Rollo, MqttCallback {

    private MQTTConfig conf;
    private MqttClient client;
    private Callback cb = null;

    private String status = "0";

    public RolloProxy(){
        conf = new MQTTConfig();

        try {
            client = new MqttClient(conf.getBroker(), conf.getClientId());

            client.setCallback(this);

            client.connect(conf.getConnOpts());

            //subscribing Client to Topic
            client.subscribe(conf.getTopic());
        } catch (MqttException e) {
            System.out.println("Error starting MqttClient " + e.getCause());
            e.printStackTrace();
        }
    }

    @Override
    public void Up() {
        sendMessage(">0");
    }

    @Override
    public void Down() {
        sendMessage("<0");
    }

    @Override
    public String getStatus() {
        return status;
    }

    @Override
    public void setCallback(Callback cb) {
        this.cb = cb;
    }

    private void sendMessage(String message){
        //create new Message
        MqttMessage msq = new MqttMessage();
        msq.setPayload(message.getBytes());
        msq.setQos(conf.getQos());

        //send message
        System.out.println("Sending Message to Server: " + message);
        try {
            client.publish(conf.getTopic(), msq);
            //current status is message if it worked
            status = message;
        } catch (MqttException e) {
            System.out.println("Sending Message failed!");
            e.printStackTrace();
        }
    }

    @Override
    public void connectionLost(Throwable throwable) {
        System.out.println("Connection lost");
    }

    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        status = new String(mqttMessage.getPayload());
        cb.call(this);
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        System.out.println("Delivery complete");
    }
}
