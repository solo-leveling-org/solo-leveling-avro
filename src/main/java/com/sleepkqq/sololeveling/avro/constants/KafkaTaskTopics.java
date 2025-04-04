package com.sleepkqq.sololeveling.avro.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
@SuppressWarnings("unused")
public class KafkaTaskTopics {

  public static final String SAVE_TASKS_TOPIC = "save-tasks";
  public static final String GENERATE_TASKS_TOPIC = "generate-tasks";
  public static final String SEND_NOTIFICATION_TOPIC = "send-notification";
  public static final String TG_NOTIFICATION_TOPIC = "tg-notification";
  public static final String UI_NOTIFICATION_TOPIC = "ui-notification";
}
