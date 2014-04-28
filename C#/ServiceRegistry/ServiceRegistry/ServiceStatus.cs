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
        private const string BaseUrl = "https://mobile-messaging.appspot.com/_ah/api/services/v1.3/";
        private const string RegisterServiceString = "services/{0}";
        private const string UpdateStatusString = "services/{0}/{1}";

        public static void RegisterService(string serviceId, string serviceGroup)
        {
            RestClient.Post(BaseUrl + string.Format(RegisterServiceString, serviceGroup),
                new Service
                {
                    serviceId = serviceId,
                    serviceGroup = serviceGroup,
                    status = 2
                });
        }

        public static void UpdateStatus(string serviceId, string serviceGroup, int status)
        {
            RestClient.Put(BaseUrl + string.Format(UpdateStatusString, serviceGroup, serviceId), new Service
            {
                status = status
            });
        }

        public class Service
        {
            public string serviceId { get; set; }
            public string serviceGroup { get; set; }
            public int status { get; set; }
            public long lastUpdate { get; set; }
        }
    }
}
