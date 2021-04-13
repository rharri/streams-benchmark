# Loops vs. Streams Benchmark

A simple micro-benchmark to compare the performance of external iteration methods, such as the for-loop, to streams.
The test generates _n_ random numbers which are used for the iterations. Each random number is doubled, and the maximum 
value is determined.

Built with [Maven](https://maven.apache.org/) and [JMH](https://openjdk.java.net/projects/code-tools/jmh/). 
Inspired by "Mastering Lambdas: Java Programming in a Multicore World" by Maurice Naftalin (Oracle Press, 2015).

## Executing the Benchmark

_Tested with: Maven 3.8.1 and Oracle JDK 16_

* Step 1: ``$ git clone git://github.com/rharri/streams-benchmark.git``
* Step 2: ``$ cd streams-benchmark``  
* Step 3: ``$ mvn clean verify``
* Step 4: ``$ java -jar target/benchmarks.jar -t 1``

**Note:** If you encounter heap space errors, try using `java -XX:MaxRAMPercentage=50.0 -jar target/benchmarks.jar -t 1` to 
allow the JVM to allocate more memory. The `-XX:MaxRAMPercentage=` option accepts a `double` value representing the 
desired percentage of total memory to be made available to the JVM.

## Test Machine

|                         | MacBook Pro                       |
|-------------------------|-----------------------------------|
| **Operating System**    | macOS Big Sur</br>                |
| **CPU**                 | 2.5 GHz Quad-Core Intel Core i7   |
| **RAM**                 | 16 GB 1600 MHz DDR3               |
| **JVM**                 | Java HotSpot(TM) 64-Bit Server VM (build 16+36-2231) |

## Results

The `streamSize` represents the number of random numbers generated, and thus number of elements to be iterated by each 
method. In this case: 1 Million, 10 Million, and 100 Million.

| Benchmark                       | (streamSize)  | Mode  | Cnt |   Score |   Error |  Units |
|---------------------------------|---------------| ------|-----|---------|---------|--------|
| _getMaxNumberWithLoops          |      1000000  | avgt  |  6  |   2.078 | ± 0.021 |  ms/op |
| _getMaxNumberWithLoops          |     10000000  | avgt  |  6  |  23.501 | ± 0.525 |  ms/op |
| _getMaxNumberWithLoops          |    100000000  | avgt  |  6  | 303.414 | ± 2.349 |  ms/op |
| _getMaxNumberWithParallelStream |      1000000  | avgt  |  6  |   0.887 | ± 0.006 |  ms/op |
| _getMaxNumberWithParallelStream |     10000000  | avgt  |  6  |   8.909 | ± 0.277 |  ms/op |
| _getMaxNumberWithParallelStream |    100000000  | avgt  |  6  | 124.410 | ± 1.162 |  ms/op |
| _getMaxNumberWithStream         |      1000000  | avgt  |  6  |   1.482 | ± 0.004 |  ms/op |
| _getMaxNumberWithStream         |     10000000  | avgt  |  6  |  15.631 | ± 0.162 |  ms/op |
| _getMaxNumberWithStream         |    100000000  | avgt  |  6  | 193.641 | ± 9.973 |  ms/op |

_avgt = Average Time_