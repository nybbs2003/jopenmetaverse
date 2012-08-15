package com.ngt.jopenmetaverse.shared.testclients;

import java.util.Observable;

import org.junit.Test;

import com.ngt.jopenmetaverse.shared.sim.AgentManager.ChatType;
import com.ngt.jopenmetaverse.shared.sim.GridClient;
import com.ngt.jopenmetaverse.shared.sim.events.EventObserver;
import com.ngt.jopenmetaverse.shared.sim.login.LoginProgressEventArgs;
import com.ngt.jopenmetaverse.shared.sim.login.LoginStatus;
    public class MyFirstBotTest
    {
        public static GridClient Client = new GridClient();
 
        private static String first_name = "jitendra";
        private static String last_name = "chauhan81";
        private static String password = "jchauhan";

        @Test
        public void LoginTest() throws Exception
        {
            Client.network.RegisterLoginProgressCallback(new EventObserver<LoginProgressEventArgs>()
            		{
						@Override
						public void handleEvent(Observable o,
								LoginProgressEventArgs arg) {
							Network_LoginProgress(o, arg);
							
						}
            		});
            if (Client.network.Login(first_name, last_name, password, "My First Bot", "Your name"))
            {
               System.out.println("I logged into Second Life!");
            }
            else
            {
               System.out.println("I couldn't log in, here is why: " + Client.network.getLoginMessage());
               System.out.println("press enter to close...");
            }
        }
 
        static void Network_LoginProgress(Object sender, LoginProgressEventArgs e)
        {
            if (e.getStatus() == LoginStatus.Success)
            {
               System.out.println("I'm connected to the simulator, going to greet everyone around me");
                Client.self.Chat("Hello World!", 0, ChatType.Normal);
                try {
					Thread.sleep(60000);
				} catch (InterruptedException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
               System.out.println("Now I am going to logout of SL.. Goodbye!");
                Client.network.Logout();
               System.out.println("I am Loged out please press enter to close...");
            }
        }
    }
