package cn.edu.bjtu.ebosservmgmt.service;

public interface MqFactory {
    MqProducer createProducer();
    MqConsumer createConsumer(String topic);
}
