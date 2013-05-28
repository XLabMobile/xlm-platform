package com.xlabm.tmservice;
import com.xlabm.tmservice.ITMServiceCallback;

interface ITMService {
  void registerCallback(ITMServiceCallback callback);
  void unregisterCallback(ITMServiceCallback callback);
  void setTrigger(in String qid);
}
