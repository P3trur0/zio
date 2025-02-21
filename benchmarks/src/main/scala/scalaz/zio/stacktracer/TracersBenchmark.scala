package scalaz.zio.stacktracer

import java.util.concurrent.TimeUnit

import org.openjdk.jmh.annotations._
import scalaz.zio.internal.stacktracer.impl.{ AkkaLineNumbers, AkkaLineNumbersTracer }
import scalaz.zio.stacktracer.TracersBenchmark.{ akkaTracer, asmTracer }
import scalaz.zio.stacktracer.impls.AsmTracer

@BenchmarkMode(Array(Mode.AverageTime))
@OutputTimeUnit(TimeUnit.MILLISECONDS)
@Warmup(iterations = 20, time = 1)
@Measurement(iterations = 20, time = 1)
@Fork(1)
class TracersBenchmark {

  @Benchmark
  def akkaLineNumbers1000Lambdas() = {
    var i = 0
    while (i != 1000) {
      AkkaLineNumbers((a: Any) => a)
      i += 1
    }
  }

  @Benchmark
  def akkaTracer1000Lambdas() = {
    var i = 0
    while (i != 1000) {
      akkaTracer.traceLocation((a: Any) => a)
      i += 1
    }
  }

  @Benchmark
  def asmTracer1000Lambdas() = {
    var i = 0
    while (i != 1000) {
      asmTracer.traceLocation((a: Any) => a)
      i += 1
    }
  }

}

object TracersBenchmark {
  val akkaTracer = new AkkaLineNumbersTracer
  val asmTracer  = new AsmTracer
}
