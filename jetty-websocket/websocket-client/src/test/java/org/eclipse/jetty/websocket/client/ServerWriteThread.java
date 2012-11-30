//
//  ========================================================================
//  Copyright (c) 1995-2012 Mort Bay Consulting Pty. Ltd.
//  ------------------------------------------------------------------------
//  All rights reserved. This program and the accompanying materials
//  are made available under the terms of the Eclipse Public License v1.0
//  and Apache License v2.0 which accompanies this distribution.
//
//      The Eclipse Public License is available at
//      http://www.eclipse.org/legal/epl-v10.html
//
//      The Apache License v2.0 is available at
//      http://www.opensource.org/licenses/apache2.0.php
//
//  You may elect to redistribute this code under either of these licenses.
//  ========================================================================
//

package org.eclipse.jetty.websocket.client;

import java.io.IOException;
import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.jetty.util.log.Log;
import org.eclipse.jetty.util.log.Logger;
import org.eclipse.jetty.websocket.client.blockhead.BlockheadServer.ServerConnection;
import org.eclipse.jetty.websocket.common.WebSocketFrame;

public class ServerWriteThread extends Thread
{
    private static final Logger LOG = Log.getLogger(ServerWriteThread.class);
    private final ServerConnection conn;
    private Exchanger<String> exchanger;
    private int slowness = -1;
    private int messageCount = 100;
    private String message = "Hello";

    public ServerWriteThread(ServerConnection conn)
    {
        this.conn = conn;
    }

    public Exchanger<String> getExchanger()
    {
        return exchanger;
    }

    public String getMessage()
    {
        return message;
    }

    public int getMessageCount()
    {
        return messageCount;
    }

    public int getSlowness()
    {
        return slowness;
    }

    @Override
    public void run()
    {
        final AtomicInteger m = new AtomicInteger();

        try
        {
            while (m.get() < messageCount)
            {
                conn.write(WebSocketFrame.text(message));

                if (exchanger != null)
                {
                    // synchronized on exchange
                    exchanger.exchange(message);
                }

                m.incrementAndGet();

                if (slowness > 0)
                {
                    TimeUnit.MILLISECONDS.sleep(slowness);
                }
            }
        }
        catch (InterruptedException | IOException e)
        {
            LOG.warn(e);
        }
    }

    public void setExchanger(Exchanger<String> exchanger)
    {
        this.exchanger = exchanger;
    }

    public void setMessage(String message)
    {
        this.message = message;
    }

    public void setMessageCount(int messageCount)
    {
        this.messageCount = messageCount;
    }

    public void setSlowness(int slowness)
    {
        this.slowness = slowness;
    }
}
