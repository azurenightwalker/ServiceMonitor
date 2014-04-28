using System;
using System.Collections.Generic;
using System.IO;
using System.Linq;
using System.Net;
using System.Text;
using System.Threading.Tasks;
using Newtonsoft.Json;

namespace ServiceRegistry
{
    public static class RestClient
    {
        public static T Get<T>(string url)
        {
            return JsonConvert.DeserializeObject<T>(_doGet(url));
        }

        public static void Post(string url, object data)
        {
            _doSend(url, "POST", data);
        }

        public static void Put(string url, object data)
        {
            _doSend(url,"PUT", data);
        }

        private static string _doSend(string url, string action, object data)
        {
            var ascii = new ASCIIEncoding();
            byte[] postBytes = ascii.GetBytes(JsonConvert.SerializeObject(data));

            var req = (HttpWebRequest)WebRequest.Create(url);
            req.Method = action;
            req.KeepAlive = false;
            req.ContentType = "application/json";
            req.ContentLength = postBytes.Length;

            var postStream = req.GetRequestStream();
            postStream.Write(postBytes, 0, postBytes.Length);
            postStream.Close();

            var resp = "";
            using (var respstream = req.GetResponse().GetResponseStream())
            using (var reader = new StreamReader(respstream))
            {
                resp = reader.ReadToEnd();
            }
            return resp;
        }

        private static string _doGet(string url)
        {
            var req = (HttpWebRequest)WebRequest.Create(url);
            req.Method = "GET";
            req.KeepAlive = false;
            req.ContentLength = 0;
            var resp = "";
            using (var respstream = req.GetResponse().GetResponseStream())
            using (var reader = new StreamReader(respstream))
            {
                resp = reader.ReadToEnd();
            }
            return resp;
        }
    }
}
