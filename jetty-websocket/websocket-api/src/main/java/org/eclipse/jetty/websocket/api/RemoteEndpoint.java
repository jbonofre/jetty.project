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

package org.eclipse.jetty.websocket.api;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.concurrent.Future;

public interface RemoteEndpoint
{
    /**
     * Send a binary message, returning when all of the message has been transmitted.
     * 
     * @param data
     *            the message to be sent
     */
    void sendBytes(ByteBuffer data) throws IOException;

    /**
     * Initiates the asynchronous transmission of a binary message. This method returns before the message is transmitted. Developers may provide a callback to
     * be notified when the message has been transmitted, or may use the returned Future object to track progress of the transmission. Errors in transmission
     * are given to the developer in the WriteResult object in either case.
     * 
     * @param data
     *            the data being sent
     * @param completion
     *            handler that will be notified of progress
     * @return the Future object representing the send operation.
     */
    Future<WriteResult> sendBytesByFuture(ByteBuffer data);

    /**
     * Send a binary message in pieces, blocking until all of the message has been transmitted. The runtime reads the message in order. Non-final pieces are
     * sent with isLast set to false. The final piece must be sent with isLast set to true.
     * 
     * @param fragment
     *            the piece of the message being sent
     */
    void sendPartialBytes(ByteBuffer fragment, boolean isLast) throws IOException;

    /**
     * Send a text message in pieces, blocking until all of the message has been transmitted. The runtime reads the message in order. Non-final pieces are sent
     * with isLast set to false. The final piece must be sent with isLast set to true.
     * 
     * @param fragment
     *            the piece of the message being sent
     */
    void sendPartialString(String fragment, boolean isLast) throws IOException;

    /**
     * Send a Ping message containing the given application data to the remote endpoint. The corresponding Pong message may be picked up using the
     * MessageHandler.Pong handler.
     * 
     * @param applicationData
     *            the data to be carried in the ping request
     */
    void sendPing(ByteBuffer applicationData);

    /**
     * Allows the developer to send an unsolicited Pong message containing the given application data in order to serve as a unidirectional heartbeat for the
     * session.
     * 
     * @param applicationData
     *            the application data to be carried in the pong response.
     */
    void sendPong(ByteBuffer applicationData);

    /**
     * Send a text message, blocking until all of the message has been transmitted.
     * 
     * @param text
     *            the message to be sent
     */
    void sendString(String text) throws IOException;

    /**
     * Initiates the asynchronous transmission of a text message. This method returns before the message is transmitted. Developers may provide a callback to be
     * notified when the message has been transmitted, or may use the returned Future object to track progress of the transmission. Errors in transmission are
     * given to the developer in the WriteResult object in either case.
     * 
     * @param text
     *            the text being sent
     * @param completion
     *            the handler which will be notified of progress
     * @return the Future object representing the send operation.
     */
    Future<WriteResult> sendStringByFuture(String text);
}
