package com.namal.arch;

import com.namal.arch.utils.Configuration;
import org.apache.catalina.Server;
import org.springframework.boot.context.embedded.EmbeddedServletContainerInitializedEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.management.*;

/**
 * Created by namalgac on 4/7/16.
 */
@Component
public class MyListener implements ApplicationListener<EmbeddedServletContainerInitializedEvent> {

    @Override
    public void onApplicationEvent(final EmbeddedServletContainerInitializedEvent event) {

        try {
            MBeanServer mBeanServer = MBeanServerFactory.findMBeanServer(null).get(0);
            ObjectName name = new ObjectName("Tomcat", "type", "Server");
            Server server = (Server) mBeanServer.getAttribute(name, "managedResource");
            Configuration.setUrlBase(server.getAddress(),event.getEmbeddedServletContainer().getPort());
            System.out.println("DONE !!!!!!!" + Configuration.getUrlBase());
        } catch (ReflectionException | InstanceNotFoundException | MBeanException | AttributeNotFoundException | MalformedObjectNameException e) {
            e.printStackTrace();
        }
    }
}