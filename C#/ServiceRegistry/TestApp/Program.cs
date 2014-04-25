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
            ServiceStatus.RegisterService("test2","test");
            ServiceStatus.UpdateStatus("test","test",2);
            ServiceStatus.UpdateStatus("test2","test",2);
        }
    }
}
