/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/mac/xlab/xlm-platform/src/com/xlabm/tmservice/ITMServiceCallback.aidl
 */
package com.xlabm.tmservice;

/**
 * XLAB Mobile Version 0.1 Alpha Release
 * User: SID@XLABM
 * Date: 5/21/13
 * Time: 5:23 PM
 * Responsibility of Class:
 */
public interface ITMServiceCallback extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements com.xlabm.tmservice.ITMServiceCallback {
        private static final java.lang.String DESCRIPTOR = "com.xlabm.tmservice.ITMServiceCallback";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.xlabm.tmservice.ITMServiceCallback interface,
         * generating a proxy if needed.
         */
        public static com.xlabm.tmservice.ITMServiceCallback asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.xlabm.tmservice.ITMServiceCallback))) {
                return ((com.xlabm.tmservice.ITMServiceCallback) iin);
            }
            return new com.xlabm.tmservice.ITMServiceCallback.Stub.Proxy(obj);
        }

        @Override
        public android.os.IBinder asBinder() {
            return this;
        }

        @Override
        public boolean onTransact(int code, android.os.Parcel data, android.os.Parcel reply, int flags) throws android.os.RemoteException {
            switch (code) {
                case INTERFACE_TRANSACTION: {
                    reply.writeString(DESCRIPTOR);
                    return true;
                }
                case TRANSACTION_updateAnswer: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.updateAnswer(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements com.xlabm.tmservice.ITMServiceCallback {
            private android.os.IBinder mRemote;

            Proxy(android.os.IBinder remote) {
                mRemote = remote;
            }

            @Override
            public android.os.IBinder asBinder() {
                return mRemote;
            }

            public java.lang.String getInterfaceDescriptor() {
                return DESCRIPTOR;
            }

            @Override
            public void updateAnswer(java.lang.String answer) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(answer);
                    mRemote.transact(Stub.TRANSACTION_updateAnswer, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_updateAnswer = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
    }

    public void updateAnswer(java.lang.String answer) throws android.os.RemoteException;
}
