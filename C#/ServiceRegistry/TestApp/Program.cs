using System;
using System.Collections.Generic;
using System.Linq;
using System.Text;
using System.Threading.Tasks;
using ServiceRegistry;

namespace TestApp
{
    class Program
    {
        static void Main(string[] args)
        {
            ServiceStatus.RegisterService("test","test");
            ServiceStatus.RegisterService("test2","test");
            ServiceStatus.RegisterService("test3","test");
            ServiceStatus.UpdateStatus("test","test",4);
            ServiceStatus.UpdateStatus("test2","test",1);
        }
    }
}
