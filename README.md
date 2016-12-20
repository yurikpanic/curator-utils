# curator-utils

A really thin wrapper over apache curator background functions to make them more friendly to Scala futures. It could be a gist, actually.

## Usage

```scala
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.api.CuratorEvent
import com.elsyton.curator.BackgroundConversions._

import scala.concurrent.Future

...

val curator: CuratorFramework = ???

val res1: Future[CuratorEvent] = curator.create().inFuture(identity).forPath("/some/path", Array.emptyByteArray)

val res2: Future[String] = curator.getData.inFuture(e => new String(e.getData)).forPath("/some/other/path")
```

