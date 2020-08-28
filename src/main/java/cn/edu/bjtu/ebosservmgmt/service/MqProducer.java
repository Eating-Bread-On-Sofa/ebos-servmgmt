package cn.edu.bjtu.ebosservmgmt.service;

public interface MqProducer {
    void publish(String topic, String message);
}
