package com.xlabm.tmservice;

import android.app.Service;
import android.content.Intent;
import android.os.*;
import android.os.Process;
import android.util.Log;
import android.widget.Toast;
import com.xlabm.tmservice.tmutils.Trigger;

import java.util.HashMap;

/**
 * The type Trigger manager service.
 */
public class TriggerManagerService extends Service {

    private static final int SUCCESS_MSG = 2;
    private static final int START_MSG = 0;
    private static final int RUNNING_MSG = 1;
    private static final String TAG = "TriggerManagerService";
    //We only want one activeTrigger at a time; make it a static object
    protected static Trigger sActiveTrigger;
    private static String mFormID;
    private static String mFormName;
    private static TriggerXMLProcessor sProcessor;
    final RemoteCallbackList<ITMServiceCallback> mCallbacks
            = new RemoteCallbackList<ITMServiceCallback>();
    /**
     * Our Handler used to execute operations on the main thread.  This is used
     * to schedule increments of our value.
     */
    private final Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case SUCCESS_MSG: {
                    // Broadcast to all clients
                    final int N = mCallbacks.beginBroadcast();
                    for (int i = 0; i < N; i++) {
                        try {
                            mCallbacks.getBroadcastItem(i).updateAnswer(msg.obj.toString());
                        } catch (RemoteException e) {
                            // The RemoteCallbackList will take care of removing
                            // the dead object for us.
                        }
                    }
                    mCallbacks.finishBroadcast();

                }
                break;
                default:
                    super.handleMessage(msg);
            }
        }
    };
    //Implementation of remote interface:
    private final ITMService.Stub mBinder = new ITMService.Stub() {

        @Override
        public void setTrigger(String qid) throws
                RemoteException {
            sActiveTrigger = triggers.get(qid);
            new Thread(triggerProcessor).start();


            //     displayNotificationMessage("Set Trigger QID:", qid);
            setTriggerFlag(1);
        }

        public void registerCallback(ITMServiceCallback cb) {
            if (cb != null) mCallbacks.register(cb);

        }

        public void unregisterCallback(ITMServiceCallback cb) {
            if (cb != null) mCallbacks.unregister(cb);
        }

        public int getPid() {
            return Process.myPid();
        }
    };
    // NotificationManager mNM;
    Runnable triggerProcessor = new Runnable() {
        @Override
        public void run() {
            sActiveTrigger.execute();
            mHandler.sendMessage(mHandler.obtainMessage
                    (START_MSG, "Started"));
            while (sActiveTrigger.getStatus() == RUNNING_MSG) {
                if (sActiveTrigger.halt()) {
                    mHandler.sendMessage(mHandler.obtainMessage
                            (SUCCESS_MSG, "Success"));
                    break;
                } else {
                    mHandler.sendMessage(mHandler.obtainMessage
                            (RUNNING_MSG, "Running"));
                }

            }

        }
    };

    //    /**
//     * Display notification message based on current state and the question
//     * ID. Or hack to debug as you see fit.
//     *
//     * @param state the state
//     * @param qid   the mQid
//     */
//    public void displayNotificationMessage(String state, String qid) {
//
//        mNM = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//        Uri mOdkFormUri = ContentUris.withAppendedId(FormsProviderAPI
//                .FormsColumns.CONTENT_URI, Long.getLong(mFormID));
//
//        int icon = R.drawable.icon;
//        CharSequence tickerText = "A Friendly Reminder from XLab Mobile";
//        long when = System.currentTimeMillis();
//
//        //Begin setting the
//        Notification notification = new Notification(icon, tickerText, when);
//        notification.flags = Notification.FLAG_AUTO_CANCEL;
//        notification.defaults = Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
//
//        CharSequence contentTitle = "A Friendly Reminder from XLab Mobile";
//        CharSequence contentText = "A new question now available for survey:" +
//                mFormName;
//
//        //Begin setting the intent here
//        Intent mIntent = new Intent();
//        mIntent.setAction("android.intent.action.EDIT");
//        mIntent.addCategory("android.intent.category.DEFAULT");
//        mIntent.setComponent(new ComponentName("org.odk.collect.android",
//                "org.odk.collect.android.activities.TriggerManagerActivity"));
//        mIntent.setDataAndType(mOdkFormUri, FormsProviderAPI.FormsColumns.CONTENT_TYPE);
//
//
//        PendingIntent contentIntent = PendingIntent.getActivity(this, 0,
//                new Intent(this, Trigger.class), 0);
//        notification.setLatestEventInfo(this,
//                contentTitle,
//                contentText, contentIntent);
//    }
    //TODO: Populate with parsed xml contents
    HashMap<String, Trigger> triggers;

    public void setTriggerFlag(int flag) {
        Log.v(TAG, "Trigger flag set to " + flag);
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //TODO: Debug... remove when finished

        Log.v(TAG, "onCreate() called");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);
        Log.v(TAG, "onStart() called");
        sProcessor = new TriggerXMLProcessor();
        triggers = sProcessor.getTriggers();

        return START_STICKY;
    }

    //Return IBinder representing remotable object
    @Override
    public IBinder onBind(Intent intent) {
        //  if (sProcessor.getStatus().equals(AsyncTask.Status.FINISHED)) {
//        mFormID = sProcessor.getFormID();
        //      mFormName = sProcessor.getFormName();
        android.os.Debug.waitForDebugger();
        Log.v(TAG, "onBind() called");
        if (ITMService.class.getName().equals(intent.getAction())) {
            return mBinder;
            //   } else if (sProcessor.getStatus().equals(AsyncTask.Status.RUNNING)) {
            //     Log.v(TAG, "Try again");
            //   return null;
        } else return null;
    }

    @Override
    public void onDestroy() {
        Log.v(TAG, "onDestroy()");
        Toast.makeText(this, "Remote Service Stopped",
                Toast.LENGTH_SHORT).show();
        // Unregister all callbacks.
        mCallbacks.kill();

        // Remove the next pending message to increment the counter, stopping
        // the increment loop.
        mHandler.removeMessages(RUNNING_MSG);
    }

}
