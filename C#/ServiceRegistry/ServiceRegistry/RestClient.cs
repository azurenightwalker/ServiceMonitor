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
            return JsonConvert.DeserializeObject<T>(_doSend(url,"GET"));
        }

        public static void Post(string url)
        {
            _doSend(url, "POST");
        }

        public static void Put(string url)
        {
            _doSend(url,"PUT");
        }

        private static string _doSend(string url, string action)
        {
            var req = (HttpWebRequest)WebRequest.Create(url);
            req.Method = action;
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
