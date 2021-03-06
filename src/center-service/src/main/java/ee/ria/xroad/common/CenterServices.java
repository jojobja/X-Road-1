/**
 * The MIT License
 * Copyright (c) 2015 Estonian Information System Authority (RIA), Population Register Centre (VRK)
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */
package ee.ria.xroad.common;

import ee.ria.xroad.signer.protocol.SignerClient;

import akka.actor.ActorSystem;
import com.typesafe.config.ConfigFactory;
import scala.concurrent.Await;
import scala.concurrent.duration.Duration;

import static ee.ria.xroad.common.SystemProperties.CONF_FILE_CENTER;
import static ee.ria.xroad.common.SystemProperties.CONF_FILE_SIGNER;

/**
 * Contains the center actor system.
 */
public final class CenterServices {

    static {
        SystemPropertiesLoader.create().withCommonAndLocal()
            .with(CONF_FILE_CENTER)
            .with(CONF_FILE_SIGNER)
            .load();
    }

    private static ActorSystem actorSystem;

    private CenterServices() {
    }

    private static void init() throws Exception {
        if (actorSystem == null) {
            actorSystem = ActorSystem.create("CenterService",
                    ConfigFactory.load().getConfig("centerservice")
                        .withFallback(ConfigFactory.load()));
        }
    }

    /**
     * Initializes the center actor system.
     * @throws Exception in case of any errors
     */
    public static void start() throws Exception {
        init();

        SignerClient.init(actorSystem);
    }

    /**
     * Stops the center actor system.
     * @throws Exception in case of any errors
     */
    public static void stop() throws Exception {
        if (actorSystem != null) {
            Await.ready(actorSystem.terminate(), Duration.Inf());
        }
    }

}
