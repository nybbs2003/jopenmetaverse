package com.ngt.jopenmetaverse.shared.cap.http;

import java.net.URI;
import java.security.cert.X509Certificate;

import com.ngt.jopenmetaverse.shared.structureddata.OSD;
import com.ngt.jopenmetaverse.shared.structureddata.OSDFormat;
import com.ngt.jopenmetaverse.shared.structureddata.OSDParser;
import com.ngt.jopenmetaverse.shared.structureddata.llsd.BinaryLLSDOSDParser;


 public class CapsHttpClient
    {
//        public delegate void DownloadProgressCallback(CapsHttpClient client, int bytesReceived, int totalBytesToReceive);
//        public delegate void CompleteCallback(CapsHttpClient client, OSD result, Exception error);
//
//        public event DownloadProgressCallback OnDownloadProgress;
//        public event CompleteCallback OnComplete;
//
//        public Object UserData;
//
//        protected URI _Address;
//        protected byte[] _PostData;
//        protected X509Certificate _ClientCert;
//        protected String _ContentType;
//        protected HttpWebRequest _Request;
//        protected OSD _Response;
//        protected AutoResetEvent _ResponseEvent = new AutoResetEvent(false);
//
//        public CapsHttpClient(URI capability)
//        {
//            this(capability, null);
//        }
//
//        public CapsHttpClient(URI capability, X509Certificate clientCert)
//        {
//            _Address = capability;
//            _ClientCert = clientCert;
//        }
//
//        public void BeginGetResponse(int millisecondsTimeout)
//        {
//            BeginGetResponse(null, null, millisecondsTimeout);
//        }
//
//        public void BeginGetResponse(OSD data, OSDFormat format, int millisecondsTimeout)
//        {
//            byte[] postData;
//            String contentType;
//
//            switch (format)
//            {
//                case Xml:
//                    postData = OSDParser.SerializeLLSDXmlBytes(data);
//                    contentType = "application/llsd+xml";
//                    break;
//                case Binary:
//                    postData = BinaryLLSDOSDParser.SerializeLLSDBinary(data);
//                    contentType = "application/llsd+binary";
//                    break;
//                case Json:
//                default:
//                    postData = System.Text.Encoding.UTF8.GetBytes(OSDParser.SerializeJsonString(data));
//                    contentType = "application/llsd+json";
//                    break;
//            }
//
//            BeginGetResponse(postData, contentType, millisecondsTimeout);
//        }
//
//        public void BeginGetResponse(byte[] postData, string contentType, int millisecondsTimeout)
//        {
//            _PostData = postData;
//            _ContentType = contentType;
//
//            if (_Request != null)
//            {
//                _Request.Abort();
//                _Request = null;
//            }
//
//            if (postData == null)
//            {
//                // GET
//                //Logger.Log.Debug("[CapsClient] GET " + _Address);
//                _Request = CapsBase.DownloadStringAsync(_Address, _ClientCert, millisecondsTimeout, DownloadProgressHandler,
//                    RequestCompletedHandler);
//            }
//            else
//            {
//                // POST
//                //Logger.Log.Debug("[CapsClient] POST (" + postData.Length + " bytes) " + _Address);
//                _Request = CapsBase.UploadDataAsync(_Address, _ClientCert, contentType, postData, millisecondsTimeout, null,
//                    DownloadProgressHandler, RequestCompletedHandler);
//            }
//        }
//
//        public OSD GetResponse(int millisecondsTimeout)
//        {
//            BeginGetResponse(millisecondsTimeout);
//            _ResponseEvent.WaitOne(millisecondsTimeout, false);
//            return _Response;
//        }
//
//        public OSD GetResponse(OSD data, OSDFormat format, int millisecondsTimeout)
//        {
//            BeginGetResponse(data, format, millisecondsTimeout);
//            _ResponseEvent.WaitOne(millisecondsTimeout, false);
//            return _Response;
//        }
//
//        public OSD GetResponse(byte[] postData, string contentType, int millisecondsTimeout)
//        {
//            BeginGetResponse(postData, contentType, millisecondsTimeout);
//            _ResponseEvent.WaitOne(millisecondsTimeout, false);
//            return _Response;
//        }
//
//        public void Cancel()
//        {
//            if (_Request != null)
//                _Request.Abort();
//        }
//
//        void DownloadProgressHandler(HttpWebRequest request, HttpWebResponse response, int bytesReceived, int totalBytesToReceive)
//        {
//            _Request = request;
//
//            if (OnDownloadProgress != null)
//            {
//                try { OnDownloadProgress(this, bytesReceived, totalBytesToReceive); }
//                catch (Exception ex) { Logger.Log(ex.Message, Helpers.LogLevel.Error, ex); }
//            }
//        }
//
//        void RequestCompletedHandler(HttpWebRequest request, HttpWebResponse response, byte[] responseData, Exception error)
//        {
//            _Request = request;
//
//            OSD result = null;
//
//            if (responseData != null)
//            {
//                try { result = OSDParser.Deserialize(responseData); }
//                catch (Exception ex) { error = ex; }
//            }
//
//            FireCompleteCallback(result, error);
//        }
//
//        private void FireCompleteCallback(OSD result, Exception error)
//        {
//            CompleteCallback callback = OnComplete;
//            if (callback != null)
//            {
//                try { callback(this, result, error); }
//                catch (Exception ex) { Logger.Log(ex.Message, Helpers.LogLevel.Error, ex); }
//            }
//
//            _Response = result;
//            _ResponseEvent.Set();
//        }
    }