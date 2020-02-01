package example.count.service;

import io.rsocket.AbstractRSocket;
import io.rsocket.ConnectionSetupPayload;
import io.rsocket.Payload;
import io.rsocket.RSocket;
import io.rsocket.RSocketFactory;
import io.rsocket.SocketAcceptor;
import io.rsocket.frame.decoder.PayloadDecoder;
import io.rsocket.transport.netty.server.TcpServerTransport;
import io.rsocket.util.DefaultPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigInteger;

/**
 * RSocket service that returns a stream of integers from 1 to 10,000 as fast as the consumer can handle them.
 */
public class CountService {
    private static final Logger LOG = LoggerFactory.getLogger(CountService.class);

    public static void main(String... args) throws Exception {
        RSocketFactory.receive()
                .frameDecoder(PayloadDecoder.DEFAULT)
                .acceptor(new SocketAcceptor() {
                    @Override
                    public Mono<RSocket> accept(ConnectionSetupPayload setup, RSocket sendingSocket) {
                        return Mono.just(new AbstractRSocket() {
                            @Override
                            public Flux<Payload> requestStream(Payload payload) {
                                return Flux.range(1, 10_000)
                                        .doOnRequest(value -> {
                                            LOG.info("Received Request For: {}", value);
                                        })
                                        .map(i -> {
                                            LOG.info("Sending: {}", i);
                                            return DefaultPayload.create(BigInteger.valueOf(i).toByteArray());
                                        });
                            }
                        });
                    }
                })
                .transport(TcpServerTransport.create(7000))
                .start()
                .block();

        LOG.info("RSocket server started on port: 7000");

        Thread.currentThread().join();
    }
}
