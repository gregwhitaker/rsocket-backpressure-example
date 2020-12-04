# rsocket-backpressure-example
![Build](https://github.com/gregwhitaker/rsocket-backpressure-example/workflows/Build/badge.svg)

An example of backpressure between an [RSocket](http://rsocket.io) client and service.

In this example the `count-client` requests a stream of integers from the `count-service` and specifies that it can only handle
`10` requests at a time.

## Building the Example
Run the following command to build the example:

    ./gradlew clean build
    
## Running the Example
Follow the steps below to run the example:

1. Run the following command to start the `count-service`:

        ./gradlew :count-service:run
        
    If the service has started successfully you will see the following in the terminal:
    
        > Task :count-service:run
        [main] INFO example.count.service.CountService - RSocket server started on port: 7000
        
2. In a new terminal, run the following command to start streaming integers with the `count-client`:

        ./gradlew :count-client:run
        
    If successful, you will see in the terminal that it processes `10,000` integers:

        [reactor-tcp-nio-1] INFO example.count.client.CountClient - Received: 9995
        [reactor-tcp-nio-1] INFO example.count.client.CountClient - Received: 9996
        [reactor-tcp-nio-1] INFO example.count.client.CountClient - Received: 9997
        [reactor-tcp-nio-1] INFO example.count.client.CountClient - Received: 9998
        [reactor-tcp-nio-1] INFO example.count.client.CountClient - Received: 9999
        [reactor-tcp-nio-1] INFO example.count.client.CountClient - Received: 10000
        [reactor-tcp-nio-1] INFO example.count.client.CountClient - Done
        
    In the `count-service` terminal, notice that it is receiving requests for `8` numbers at a time:
    
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9984
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9985
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9986
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Received Request For: 8
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9987
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9988
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9989
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9990
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9991
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9992
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9993
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9994
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Received Request For: 8
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9995
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9996
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9997
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9998
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 9999
        [reactor-tcp-nio-2] INFO example.count.service.CountService - Sending: 10000
        
    The `8` items comes from the `limitRate(10)` on the client. The limitRate algorithm proactively fills the buffer when it is 75% exhausted. That is why you are seeing it request 8 instead of 10.

## Bugs and Feedback
For bugs, questions, and discussions please use the [Github Issues](https://github.com/gregwhitaker/rsocket-backpressure-example/issues).

## License
MIT License

Copyright (c) 2020 Greg Whitaker

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
