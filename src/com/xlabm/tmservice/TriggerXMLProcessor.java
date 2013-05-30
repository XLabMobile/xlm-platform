package com.xlabm.tmservice;

import android.os.AsyncTask;
import android.util.Log;
import android.util.Xml;
import com.xlabm.tmservice.tmutils.Trigger;
import com.xlabm.tmservice.tmutils.TriggerFactory;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;

/**
 * The type Trigger processor.
 */
public class TriggerXMLProcessor {
    private static final String ns = null;
    private static final String tUrlString = "http://www.ocf.berkeley.edu/~saf/Test.xml";
    private static final String TAG = "com.xlabm.tmservice.TriggerXMLProcessor";
    private static String mFormID;
    private static String mFormName;
    private static DownloadXMLTask downloadXMLTask;

    public AsyncTask.Status getStatus() {
        return downloadXMLTask.getStatus();
    }


    TriggerFactory triggerFactory = new TriggerFactory();
    public static HashMap<String, Trigger> mTriggers = new HashMap<String, Trigger>();

    /**
     * Downloads and returns the triggers. Call is done asynchronously.
     *
     * @return the triggers
     */
    public HashMap<String, Trigger> getTriggers() {

        downloadXMLTask = new DownloadXMLTask();
        downloadXMLTask.execute();
        return mTriggers;

    }

    /**
     * Gets form iD.
     *
     * @return the form iD
     */
    //TODO#Later: Use a form object from ODK?
    public String getFormID() {
        return mFormID;
    }

    /**
     * Gets form name.
     *
     * @return the form name
     */
    public String getFormName() {
        return mFormName;
    }

    /**
     * Parse list.
     *
     * @param in the in
     * @return the list
     * @throws org.xmlpull.v1.XmlPullParserException
     *                             the xml pull parser exception
     * @throws java.io.IOException the iO exception
     */
    public HashMap<String, Trigger> parse(InputStream in) throws
            XmlPullParserException,
            IOException {
        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();
            return readFeed(parser);
        } finally {
            in.close();
        }
    }

    private HashMap<String, Trigger> readFeed(XmlPullParser parser) throws
            XmlPullParserException, IOException {
        parser.require(XmlPullParser.START_TAG, ns, "triggers");
        mFormID = parser.getAttributeValue(ns, "xFormID");
        mFormName = parser.getAttributeValue(ns, "xFormName");
        parser.nextTag();
        while (!parser.getName().equals("triggers")) {

            String name = parser.getName();

            //Starts by looking for the trigger tag
            while (name.equals("trigger")) {
                readTrigger(parser);
                parser.nextTag();
                name = parser.getName();

            }
        }
        return mTriggers;
    }


    //This should read and return a trigger type
    private Trigger readTrigger(XmlPullParser parser)
            throws
            IOException,
            XmlPullParserException {


        String qid;
        String type = "default";
        HashMap<String, String> params;
        params = new HashMap<String, String>();


        parser.require(XmlPullParser.START_TAG, ns, "trigger");
        //type and mQid are defined as attributes on the trigger tag

        qid = parser.getAttributeValue(null, "mQid");

        parser.nextTag();
        do {

            String tagName = parser.getName();


            if (tagName.equals("trigger")) {
                break;
            } else {
                type = parser.getName();
                for (int i = 0; i < parser.getAttributeCount(); i++) {
                    params.put(parser.getAttributeName(i), parser.getAttributeValue(i));
                }
                parser.nextTag();
            }
        } while (!parser.getName().equals("trigger"));
        Log.v(TAG, "Trigger: " + parser.getName() + " , " + qid + ", " + " , " + params.toString());
        Trigger trigger = triggerFactory.makeTrigger(qid, type, params);
        mTriggers.put(trigger.qid, trigger);
        return trigger;


    }

    //Subclass to download xml and return parsed triggers as string
    class DownloadXMLTask extends AsyncTask<String, Void, Integer> {

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);

        }

        @Override
        protected Integer doInBackground(String... urls) {


            try {
                mTriggers = getXmlFromUrl(tUrlString);
                return 1;
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return 0;
        }


        private HashMap<String, Trigger> getXmlFromUrl(String urlString)
                throws XmlPullParserException, IOException {
            InputStream stream = null;

            try {
                stream = downloadUrl(urlString);
                parse(stream);
            } finally {
                if (stream != null) {
                    stream.close();
                }
            }
            return mTriggers;
        }


        /**
         * Download xml from url.
         *
         * @param urlString the urlString
         * @return the xml from url
         */
        public InputStream downloadUrl(String urlString) throws IOException {

            URL url = new URL(urlString);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /*milliseconds*/);
            conn.setRequestMethod("GET");
            conn.setDoInput(true);
            conn.connect();
            return conn.getInputStream();


        }

    }
}


