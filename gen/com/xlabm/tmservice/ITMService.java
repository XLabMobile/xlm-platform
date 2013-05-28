/*___Generated_by_IDEA___*/

/*
 * This file is auto-generated.  DO NOT MODIFY.
 * Original file: /Users/mac/xlab/xlm-platform/src/com/xlabm/tmservice/ITMService.aidl
 */
package com.xlabm.tmservice;

public interface ITMService extends android.os.IInterface {
    /**
     * Local-side IPC implementation stub class.
     */
    public static abstract class Stub extends android.os.Binder implements com.xlabm.tmservice.ITMService {
        private static final java.lang.String DESCRIPTOR = "com.xlabm.tmservice.ITMService";

        /**
         * Construct the stub at attach it to the interface.
         */
        public Stub() {
            this.attachInterface(this, DESCRIPTOR);
        }

        /**
         * Cast an IBinder object into an com.xlabm.tmservice.ITMService interface,
         * generating a proxy if needed.
         */
        public static com.xlabm.tmservice.ITMService asInterface(android.os.IBinder obj) {
            if ((obj == null)) {
                return null;
            }
            android.os.IInterface iin = obj.queryLocalInterface(DESCRIPTOR);
            if (((iin != null) && (iin instanceof com.xlabm.tmservice.ITMService))) {
                return ((com.xlabm.tmservice.ITMService) iin);
            }
            return new com.xlabm.tmservice.ITMService.Stub.Proxy(obj);
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
                case TRANSACTION_registerCallback: {
                    data.enforceInterface(DESCRIPTOR);
                    com.xlabm.tmservice.ITMServiceCallback _arg0;
                    _arg0 = com.xlabm.tmservice.ITMServiceCallback.Stub.asInterface(data.readStrongBinder());
                    this.registerCallback(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_unregisterCallback: {
                    data.enforceInterface(DESCRIPTOR);
                    com.xlabm.tmservice.ITMServiceCallback _arg0;
                    _arg0 = com.xlabm.tmservice.ITMServiceCallback.Stub.asInterface(data.readStrongBinder());
                    this.unregisterCallback(_arg0);
                    reply.writeNoException();
                    return true;
                }
                case TRANSACTION_setTrigger: {
                    data.enforceInterface(DESCRIPTOR);
                    java.lang.String _arg0;
                    _arg0 = data.readString();
                    this.setTrigger(_arg0);
                    reply.writeNoException();
                    return true;
                }
            }
            return super.onTransact(code, data, reply, flags);
        }

        private static class Proxy implements com.xlabm.tmservice.ITMService {
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
            public void registerCallback(com.xlabm.tmservice.ITMServiceCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((callback != null)) ? (callback.asBinder()) : (null)));
                    mRemote.transact(Stub.TRANSACTION_registerCallback, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void unregisterCallback(com.xlabm.tmservice.ITMServiceCallback callback) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeStrongBinder((((callback != null)) ? (callback.asBinder()) : (null)));
                    mRemote.transact(Stub.TRANSACTION_unregisterCallback, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }

            @Override
            public void setTrigger(java.lang.String qid) throws android.os.RemoteException {
                android.os.Parcel _data = android.os.Parcel.obtain();
                android.os.Parcel _reply = android.os.Parcel.obtain();
                try {
                    _data.writeInterfaceToken(DESCRIPTOR);
                    _data.writeString(qid);
                    mRemote.transact(Stub.TRANSACTION_setTrigger, _data, _reply, 0);
                    _reply.readException();
                } finally {
                    _reply.recycle();
                    _data.recycle();
                }
            }
        }

        static final int TRANSACTION_registerCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 0);
        static final int TRANSACTION_unregisterCallback = (android.os.IBinder.FIRST_CALL_TRANSACTION + 1);
        static final int TRANSACTION_setTrigger = (android.os.IBinder.FIRST_CALL_TRANSACTION + 2);
    }

    public void registerCallback(com.xlabm.tmservice.ITMServiceCallback callback) throws android.os.RemoteException;

    public void unregisterCallback(com.xlabm.tmservice.ITMServiceCallback callback) throws android.os.RemoteException;

    public void setTrigger(java.lang.String qid) throws android.os.RemoteException;
}
