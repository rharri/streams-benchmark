/*
 * Copyright (c) 2014, Oracle America, Inc.
 * All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  * Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  * Redistributions in binary form must reproduce the above copyright
 *    notice, this list of conditions and the following disclaimer in the
 *    documentation and/or other materials provided with the distribution.
 *
 *  * Neither the name of Oracle nor the names of its contributors may be used
 *    to endorse or promote products derived from this software without
 *    specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF
 * THE POSSIBILITY OF SUCH DAMAGE.
 */

package com.ryanharri.streams.bench;

import org.openjdk.jmh.annotations.*;

import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.IntStream;

@Threads(-1)
@Fork(2)
@Warmup(iterations = 2, time = 1)
@Measurement(iterations = 3, time = 1)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class StreamBenchmark {

    @State(Scope.Benchmark)
    public static class RandomNumberData {
        @Param({"1000000", "10000000", "100000000"})
        long streamSize;

        int[] randomNumbers;

        @Setup
        public void setUp() {
            Random random = new Random();
            randomNumbers = random.ints(streamSize).toArray();
        }
    }

    @State(Scope.Benchmark)
    public static class SplittableRandomNumberData {
        @Param({"1000000", "10000000", "100000000"})
        long streamSize;

        int[] randomNumbers;

        @Setup
        public void setUp() {
            SplittableRandom random = new SplittableRandom();
            randomNumbers = random.ints(streamSize).toArray();
        }
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public int benchmark_getMaxNumberWithLoops(RandomNumberData d) {
        final int[] data = Arrays.copyOf(d.randomNumbers, d.randomNumbers.length);

        final int[] numbersDoubled = new int[data.length];
        for (int i = 0; i < data.length; i++) {
            numbersDoubled[i] = data[i] << 1;
        }

        var maxNumber = Integer.MIN_VALUE;
        for (final int number : numbersDoubled) {
            maxNumber = Math.max(number, maxNumber);
        }

        return maxNumber;
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public int benchmark_getMaxNumberWithStream(SplittableRandomNumberData d) {
        final int[] data = Arrays.copyOf(d.randomNumbers, d.randomNumbers.length);
        final IntStream randomNumbers = Arrays.stream(data);

        OptionalInt maxNumber = randomNumbers
                .map(n -> n << 1)
                .max();

        return maxNumber.getAsInt();
    }

    @Benchmark
    @BenchmarkMode(Mode.AverageTime)
    public int benchmark_getMaxNumberWithParallelStream(SplittableRandomNumberData d) {
        final int[] data = Arrays.copyOf(d.randomNumbers, d.randomNumbers.length);
        final IntStream randomNumbers = Arrays.stream(data);

        OptionalInt maxNumber = randomNumbers
                .parallel()
                .map(n -> n << 1)
                .max();

        return maxNumber.getAsInt();
    }
}
