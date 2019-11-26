/*
 * Copyright DataStax, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.datastax.oss.driver.internal.core.cql;

import com.datastax.oss.driver.shaded.guava.common.util.concurrent.MoreExecutors;
import io.netty.util.concurrent.SingleThreadEventExecutor;

public class DirectThreadEventExecutor extends SingleThreadEventExecutor {

  public static final DirectThreadEventExecutor INSTANCE = new DirectThreadEventExecutor();

  private DirectThreadEventExecutor() {
    super(null, MoreExecutors.directExecutor(), true);
  }

  @Override
  protected void run() {
    // copied from DefaultEventExecutor
    for (; ; ) {
      Runnable task = takeTask();
      if (task != null) {
        task.run();
        updateLastExecutionTime();
      }
      if (confirmShutdown()) {
        break;
      }
    }
  }

  @Override
  public boolean inEventLoop() {
    return true;
  }

  @Override
  public boolean inEventLoop(Thread thread) {
    return true;
  }
}
