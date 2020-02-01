package example.count.client;

import io.netty.buffer.Unpooled;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.transport.netty.client.TcpClientTransport;
import io.rsocket.util.DefaultPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigInteger;
import java.util.concurrent.CountDownLatch;

/**
 * RSocket client that requests a stream of integers from the count service.
 */
public class CountClient {
    private static final Logger LOG = LoggerFactory.getLogger(CountClient.class);

    public static void main(String... args) throws Exception {
        RSocket rSocket = RSocketFactory.connect()
                .transport(TcpClientTransport.create(7000))
                .start()
                .block();

        CountDownLatch latch = new CountDownLatch(1);

        rSocket.requestStream(DefaultPayload.create(Unpooled.EMPTY_BUFFER))
                .limitRate(10)  // limit the count service to emitting 10 values at a time
                .doOnComplete(() -> {
                    LOG.info("Done");
                    latch.countDown();
                })
                .subscribe(payload -> {
                    byte[] bytes = new byte[payload.data().readableBytes()];
                    payload.data().readBytes(bytes);

                    LOG.info("Received: {}", new BigInteger(bytes).intValue());
                });

        latch.await();
    }
}
