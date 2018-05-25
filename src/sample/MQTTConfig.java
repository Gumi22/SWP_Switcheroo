package sample;

import org.eclipse.paho.client.mqttv3.MqttConnectOptions;

import java.util.UUID;

public class MQTTConfig {

    private String topic;
    private int qos;
    private String broker;
    private String clientId;
    private MqttConnectOptions connOpts;

    public MQTTConfig(){
        this.clientId   = UUID.randomUUID().toString().replace("-", "");
        topic           = "SWP/IOT/Example";
        qos             = 1;
        broker          = "tcp://iot.eclipse.org:1883";
        setConnOpts();
    }

    public MQTTConfig(String clientID){
        this.clientId   = clientID;
        topic           = "SWP/IOT/Example";
        qos             = 1;
        broker          = "tcp://iot.eclipse.org:1883";
        setConnOpts();
    }

    public MQTTConfig(String clientID, int qos){
        this.clientId   = clientID;
        this.topic      = "SWP/IOT/Example";
        this.qos        = (qos > 2 || qos < 0) ? 1 : qos;
        this.broker     = "tcp://iot.eclipse.org:1883";
        setConnOpts();
    }

    public MQTTConfig(String clientID, String topic, int qos){
        this.clientId   = clientID;
        this.topic      = topic;
        this.qos        = (qos > 2 || qos < 0) ? 1 : qos;
        this.broker     = "tcp://iot.eclipse.org:1883";
        setConnOpts();
    }

    public MQTTConfig(String clientID, String broker, String topic, int qos){
        this.clientId   = clientID;
        this.topic      = topic;
        this.qos        = (qos > 2 || qos < 0) ? 1 : qos;
        this.broker     = broker;
        setConnOpts();
    }

    public String getTopic() {
        return topic;
    }

    public int getQos() {
        return qos;
    }

    public String getBroker() {
        return broker;
    }

    public String getClientId() {
        return clientId;
    }

    public MqttConnectOptions getConnOpts(){
        return connOpts;
    }

    private void setConnOpts(){
        connOpts = new MqttConnectOptions();
        connOpts.setCleanSession(true);
        connOpts.setConnectionTimeout(60);
        connOpts.setKeepAliveInterval(60);
        connOpts.setMqttVersion(MqttConnectOptions.MQTT_VERSION_3_1_1);
    }
}
