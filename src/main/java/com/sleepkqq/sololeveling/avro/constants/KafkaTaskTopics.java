package com.sleepkqq.sololeveling.avro.constants;

import lombok.experimental.UtilityClass;

@UtilityClass
public class KafkaTaskTopics {

  public static final String SAVE_TASKS_TOPIC = "save-tasks";
  public static final String GENERATE_TASKS_TOPIC = "generate-tasks";
  public static final String GET_NEW_TASKS_TOPIC = "get-new-tasks";
  public static final String CREATED_TASKS_TOPIC = "created-tasks";
}
