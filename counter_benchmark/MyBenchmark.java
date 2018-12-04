/*
 * Copyright (c) 2005, 2014, Oracle and/or its affiliates. All rights reserved.
 * DO NOT ALTER OR REMOVE COPYRIGHT NOTICES OR THIS FILE HEADER.
 *
 * This code is free software; you can redistribute it and/or modify it
 * under the terms of the GNU General Public License version 2 only, as
 * published by the Free Software Foundation.  Oracle designates this
 * particular file as subject to the "Classpath" exception as provided
 * by Oracle in the LICENSE file that accompanied this code.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU General Public License
 * version 2 for more details (a copy is included in the LICENSE file that
 * accompanied this code).
 *
 * You should have received a copy of the GNU General Public License version
 * 2 along with this work; if not, write to the Free Software Foundation,
 * Inc., 51 Franklin St, Fifth Floor, Boston, MA 02110-1301 USA.
 *
 * Please contact Oracle, 500 Oracle Parkway, Redwood Shores, CA 94065 USA
 * or visit www.oracle.com if you need additional information or have any
 * questions.
 */

package my;

import my.treeCounter.CombiningTree;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.BenchmarkParams;
import org.openjdk.jmh.results.Result;
import org.openjdk.jmh.results.RunResult;
import org.openjdk.jmh.results.format.ResultFormatType;
import org.openjdk.jmh.runner.Runner;
import org.openjdk.jmh.runner.RunnerException;
import org.openjdk.jmh.runner.options.Options;
import org.openjdk.jmh.runner.options.OptionsBuilder;
import org.openjdk.jmh.runner.options.TimeValue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@State(Scope.Benchmark)
@BenchmarkMode(Mode.Throughput)
@OutputTimeUnit(TimeUnit.MILLISECONDS)
public class MyBenchmark {

    private SynchronizedCounter synchronizedCounter;
    private AtomicCounter atomicCounter;
    private CombiningTree treeCounter8, treeCounter16, treeCounter32, treeCounter64;
    private AtomicInteger atomicInteger;

    @Setup
    public void prepare() {
        synchronizedCounter = new SynchronizedCounter();
        atomicCounter = new AtomicCounter();
	atomicInteger = new AtomicInteger(0);
        treeCounter8 = new CombiningTree(8);
        treeCounter16 = new CombiningTree(16);
	treeCounter32 = new CombiningTree(32);
	treeCounter64 = new CombiningTree(64);
    }


    @Benchmark
    public int synchronizedCounter() {
        return synchronizedCounter.getAndIncrement();
    }

    @Benchmark
    public int atomicCounter() {
        return atomicCounter.getAndIncrement();
    }
    @Benchmark
    public int atomicInteger() {
        return atomicInteger.getAndIncrement();
    }

    @Benchmark
    public int treeCounter8() throws InterruptedException {
        return treeCounter8.getAndIncrement();
    }

    @Benchmark
    public int treeCounter16() throws InterruptedException {
        return treeCounter16.getAndIncrement();
    }

    @Benchmark
    public int treeCounter32() throws InterruptedException {
        return treeCounter32.getAndIncrement();
    }

    @Benchmark
    public int treeCounter64() throws InterruptedException {
        return treeCounter64.getAndIncrement();
    }


    public static void main(String[] args) throws RunnerException {

        Options opt = new OptionsBuilder()
                .include(MyBenchmark.class.getSimpleName())
                .forks(1)
                .warmupIterations(3)
                .measurementTime(new TimeValue(1, TimeUnit.SECONDS))
                .measurementIterations(10)
                .threads(2)
                //.resultFormat(ResultFormatType.TEXT)
                //.output("logg.txt")
                .build();
        new Runner(opt).run();
//        Options opt;
//        int t = 1;
//        ArrayList<Collection<RunResult>> list = new ArrayList<Collection<RunResult>>(30);
//        while (t <= 100) {
//            opt = new OptionsBuilder()
//                    .include(MyBenchmark.class.getSimpleName())
//                    .forks(1)
//                    .warmupIterations(3)
//                    .measurementTime(new TimeValue(3, TimeUnit.SECONDS))
//                    .measurementIterations(10)
//                    .threads(t)
//                    .build();
//            Collection<RunResult> results = new Runner(opt).run();
//            list.add(results);
//            if (t < 10) {
//                t += 1;
//            } else if (t <= 20) {
//                t += 2;
//            } else if (t <= 50) {
//                t += 5;
//            } else {
//                t += 10;
//            }
//        }
//        for (Collection<RunResult> runResults : list) {
//            for (RunResult res : runResults) {
//                BenchmarkParams params = res.getParams();
//                Result r = res.getPrimaryResult();
//                System.out.println(params.getThreads() + "\t" + r.getScore());
//
//            }
//        }
    }
}
