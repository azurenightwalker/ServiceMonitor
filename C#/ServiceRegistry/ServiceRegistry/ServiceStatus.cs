using System;
using System.Collections.Generic;
using System.Collections.Specialized;
using System.Data;
using System.Linq;
using System.Text;

namespace ServiceRegistry
{
    public static class ServiceStatus
    {
        private const string BaseUrl = "https://mobile-messaging.appspot.com/_ah/api/services/v1.2/";
        private const string UpdateStatusString = "void/{0}/{1}/{2}";
        private const string ResgisterServiceString = "registerService/{0}/{1}";

        public static void RegisterService(string serviceId, string serviceGroup)
        {
            RestClient.Post(BaseUrl +
                string.Format(ResgisterServiceString, serviceId, serviceGroup));
        }

        public static void UpdateStatus(string serviceId, string serviceGroup, int status)
        {
            RestClient.Put(BaseUrl +
                string.Format(UpdateStatusString, serviceId, serviceGroup, status));
        }
    }
}
