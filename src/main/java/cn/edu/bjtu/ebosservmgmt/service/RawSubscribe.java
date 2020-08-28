package cn.edu.bjtu.ebosservmgmt.service;

import cn.edu.bjtu.ebosservmgmt.controller.ServiceController;
import cn.edu.bjtu.ebosservmgmt.util.ApplicationContextProvider;

public class RawSubscribe implements Runnable{
    private String subTopic;

    private MqFactory mqFactory = ApplicationContextProvider.getBean(MqFactory.class);

    public RawSubscribe(String subTopic){
        this.subTopic = subTopic;
    }

    @Override
    public void run() {
        try{
            MqConsumer mqConsumer = mqFactory.createConsumer(subTopic);

            while (true){
                try {
                    String msg = mqConsumer.subscribe();
                    if(!ServiceController.check(subTopic)){
                        break;
                    }
                    System.out.println("收到"+subTopic+msg);
                }catch (Exception e){e.printStackTrace();break;}
            }
        }catch (Exception e){e.printStackTrace();}
    }

    public String getSubTopic() {
        return subTopic;
    }

    public void setSubTopic(String subTopic) {
        this.subTopic = subTopic;
    }

}
