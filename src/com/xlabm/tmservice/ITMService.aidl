package com.xlabm.tmservice;
import com.xlabm.tmservice.ITMServiceCallback;
import java.lang.String;

interface ITMService {
  void registerCallback(ITMServiceCallback callback);
  void unregisterCallback(ITMServiceCallback callback);
  void setTrigger(in String qid);
  int getPid();
}
