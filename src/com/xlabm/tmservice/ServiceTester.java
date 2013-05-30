package com.xlabm.tmservice;

import android.app.Activity;
import android.content.*;
import android.os.*;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

/*
 * Copyright (C) 2007 The Android Open Source Project
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

// Need the following import to get access to the app resources, since this
// class is in a sub-package.

/**
 * Controller to start and stop a service. The serivce will update a status bar
 * notification every 5 seconds for a minute.
 */
public class ServiceTester extends Activity {
    private static final String TAG = "TriggerManagerActivity";
    private static final int REPORT_MSG = 1;

    private boolean mIsBound;
    private boolean mIsStarted;


    ITMService mService;

    TextView mCallbackText;
    Button mKillButton;


    //TriggerManagerServiceConnection connection;

    String mAnswer;
    String mQid;

    //This is an example of Callback from Android Documentation
    //The external service will broadcast updateAnswer(value) to all the binded activities
    private ITMServiceCallback mCallback = new ITMServiceCallback.Stub() {
        /**
         * This is called by the remote service regularly to tell us about
         * new values.  note that IPC calls are dispatched through a thread
         * pool running in each process, so the code executing here will
         * NOT be running in our main thread like most other things -- so,
         * to update the UI, we need to use a Handler to hop over there.
         */
        public void updateAnswer(String value) {
            mHandler.sendMessage(mHandler.obtainMessage(REPORT_MSG, value));
        }
    };
    //This is an example of Callback from Android Documentation
    //Handle the message whose request code is REPORT_MSG
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case REPORT_MSG:
                    mAnswer = "Received from service: " + msg.obj;
                    returnClearance();
                    break;
                default:
                    super.handleMessage(msg);
            }
        }
    };

    private OnClickListener mStartListener = new OnClickListener() {
        public void onClick(View v) {
            startService(new Intent("com.xlabm.tmservice.REMOTE_SERVICE"));
        }
    };

    private OnClickListener mKillListener = new OnClickListener() {
        public void onClick(View v) {
            // To kill the process hosting our service,
            // we need to know its PID. Conveniently our service has a call
            // that will return to us that information
            if (mService != null) {
                try {
                    int pid = mService.getPid();
                    mCallbackText.setText("Killed service process.");

                } catch (RemoteException ex) {
                    Toast.makeText(ServiceTester.this, "Remote call failed",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
    };

    private OnClickListener mBindListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            bindService(new Intent(ITMService.class
                    .getName()
            ),
                    mConnection,
                    Context.BIND_AUTO_CREATE);
            mIsBound = true;
            mCallbackText.setText("Binding");

        }
    };

    private OnClickListener mUnBindListener = new OnClickListener() {
        @Override
        public void onClick(View v) {
            if (mIsBound) {
                //If we have received the service, and hance registered with
                //it, then now is the time to unregister.
                if (mService != null) {
                    try {
                        mService.unregisterCallback(mCallback);
                    } catch (RemoteException e) {

                    }
                }
                // Detach our existing connection
                unbindService(mConnection);
                mKillButton.setEnabled(false);
                mIsBound = false;
                mCallbackText.setText("Unbinding");
            }
        }
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //Get the question ID that launches this activity
        Intent intent = getIntent();
        intent.putExtra("mQid", "7");
        mQid = intent.getStringExtra("mQid");


        setContentView(R.layout.main);

        Button button = (Button) findViewById(R.id.notifyStart);
        button.setOnClickListener(mStartListener);

        button = (Button) findViewById(R.id.bindService);
        button.setOnClickListener(mBindListener);

        button = (Button) findViewById(R.id.unBindService);
        button.setOnClickListener(mUnBindListener);

        mKillButton = (Button) findViewById(R.id.notifyStop);
        mKillButton.setOnClickListener(mKillListener);
        mKillButton.setEnabled(false);

        mCallbackText = (TextView) findViewById(R.id
                .header_text_view);
        mCallbackText.setText("Not attached. QID: " + mQid);

        //Check the trigger status associating with the current question
        SharedPreferences mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        switch (mSharedPreferences.getInt(mQid, 0)) {
            //Trigger has not been set
            case 0:
                Log.d(TAG, "case:0 mQid:" + mQid);
                //Bind to the external TMService and set the trigger
                break;

            //Trigger has been set
            case 1:
                Log.d(TAG, "case:1 mQid:" + mQid);
                break;

            //Trigger completed
            case 2:
                Log.d(TAG, "case:2 mQid:" + mQid);
                mAnswer = "Case 2";
                returnClearance();
                break;
            default:
        }
    }

    /*
    private void releaseService() {
        if (connection != null) {
            unbindService(connection);
            connection = null;
        }
        Log.d(TAG, "releaseService() called");
    }
    */

   /*
   //Change the Intent's name to the external TMService's name
    public void bindTMService(Context context) {
        connection = new TriggerManagerServiceConnection();
        Intent i = new Intent("com.xlabm.tmservice.TriggerManagerService");
        context.startService(i);
        boolean ret = bindService(i, connection, Context.BIND_AUTO_CREATE);
        Log.d(TAG, "Binding success? " + ret);


        //The status of the trigger is set to 1
        SharedPreferences mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ServiceTester.this);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mQid, 1);

        editor.commit();
    }
    */

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    //Only return when trigger completed
    private void returnClearance() {
        //The status of the trigger is set to 2
        //It should be reset to 0 after user finishes the form
        SharedPreferences mSharedPreferences = PreferenceManager
                .getDefaultSharedPreferences(ServiceTester.this);
        SharedPreferences.Editor editor = mSharedPreferences.edit();
        editor.putInt(mQid, 2);
        editor.commit();

        Intent intent = new Intent();
        intent.putExtra("value", mAnswer);
        setResult(RESULT_OK, intent);
        finish();
    }

    //Prevent user from going back to the current trigger question
    @Override
    public void onBackPressed() {
        Toast.makeText(ServiceTester.this, "You can't go back.  Please press " +
                "HOME and wait for notification.",
                Toast.LENGTH_LONG).show();
    }

    private ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            mService = ITMService.Stub.asInterface(service);
            mKillButton.setEnabled(true);
            mCallbackText.setText("Attached.");

            try {
                mService.registerCallback(mCallback);
            } catch (RemoteException e) {
//sdfklj
            }

            Toast.makeText(ServiceTester.this, "Service Connected",
                    Toast.LENGTH_SHORT).show();
        }

        /**
         * Called when a connection to the Service has been lost.  This typically
         * happens when the process hosting the service has crashed or been killed.
         * This does <em>not</em> remove the ServiceConnection itself -- this
         * binding to the service will remain active, and you will receive a call
         * to {@link #onServiceConnected} when the Service is next running.
         *
         * @param name The concrete component name of the service whose
         *             connection has been lost.
         */

        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mKillButton.setEnabled(false);
            mCallbackText.setText("Disconnected");

            Toast.makeText(ServiceTester.this, "Service Disconnected",
                    Toast.LENGTH_SHORT).show();
        }
    };


    /*
    A connection to bind to the service
    public class TriggerManagerServiceConnection implements ServiceConnection {
        @Override
        public void onServiceConnected(ComponentName name, IBinder boundService) {

            //service is an interface to interact with methods in the external service's binder object
            service = ITMService.Stub.asInterface((IBinder) boundService);
            Log.d(TAG, "onServiceConnected() called");
            Toast.makeText(ServiceTester.this, "Service connected",
                    Toast.LENGTH_LONG).show();

            //registerCallback and setTrigger are methods of the external service's binder object
            try {
                service.registerCallback(mCallback);
                service.setTrigger(mQid);
            } catch (RemoteException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            //unregisterCallback is a method of the external service's binder object
            try {
                service.unregisterCallback(mCallback);
            } catch (RemoteException e) {
                e.printStackTrace();
            }

            service = null;
            Log.d(TAG, "onServiceDisconnected() called");
            Toast.makeText(ServiceTester.this,
                    "Service disconnected",
                    Toast.LENGTH_LONG).show();
        }
    }
    */

}
