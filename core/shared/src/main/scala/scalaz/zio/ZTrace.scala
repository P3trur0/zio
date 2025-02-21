/*
 * Copyright 2017-2019 John A. De Goes and the ZIO Contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package scalaz.zio

import scalaz.zio.internal.stacktracer.ZTraceElement

final case class ZTrace(
  fiberId: FiberId,
  executionTrace: List[ZTraceElement],
  stackTrace: List[ZTraceElement],
  parentTrace: Option[ZTrace]
) {
  final def prettyPrint: String = {
    val execTrace  = this.executionTrace.nonEmpty
    val stackTrace = this.stackTrace.nonEmpty

    val stackPrint =
      if (stackTrace)
        s"Fiber:$fiberId was supposed to continue to:" ::
          this.stackTrace.map(loc => s"  a future continuation at " + loc.prettyPrint)
      else
        s"Fiber:$fiberId was supposed to continue to: <empty trace>" :: Nil

    val execPrint =
      if (execTrace)
        s"Fiber:$fiberId execution trace:" ::
          executionTrace.map(loc => "  at " + loc.prettyPrint)
      else s"Fiber:$fiberId ZIO Execution trace: <empty trace>" :: Nil

    val ancestry: List[String] =
      parentTrace.map { trace =>
        s"Fiber:$fiberId was spawned by:\n" :: trace.prettyPrint :: Nil
      }.getOrElse(s"Fiber:$fiberId was spawned by: <empty trace>" :: Nil)

    (stackPrint ++ ("" :: execPrint) ++ ("" :: ancestry)).mkString("\n")
  }
}
