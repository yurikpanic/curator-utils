package com.elsyton.curator

import scala.concurrent.{Future, Promise}
import scala.language.implicitConversions
import org.apache.curator.framework.CuratorFramework
import org.apache.curator.framework.api._
import scala.util.Try

object BackgroundConversions {
  implicit def funToBackgroundCallback[R](fun: (CuratorEvent) => R): BackgroundCallback =
    new BackgroundCallback {
      override def processResult(client: CuratorFramework, event: CuratorEvent): Unit =
        fun(event)
    }

  class PathableWithPromise[T, R](bgRes: Pathable[T], promise: Promise[R]) {
    def forPath(path: String): Future[R] = {
      bgRes.forPath(path)
      promise.future
    }
  }

  class PathAndBytesableWithPromise[T, R](bgRes: PathAndBytesable[T], promise: Promise[R]) {
    def forPath(path: String, data: Array[Byte]): Future[R] = {
      bgRes.forPath(path, data)
      promise.future
    }
  }

  implicit class BackgroundPathableWithFuture[T](backgroundPathable: BackgroundPathable[T]) {
    def inFuture[R](fun: (CuratorEvent) => R) = {
      val p = Promise[R]()
      new PathableWithPromise(backgroundPathable.inBackground({ event: CuratorEvent =>
        p.complete(Try(fun(event)))
      }: BackgroundCallback), p)
    }
  }

  implicit class BackgroundPathAndByteableWithFuture[T](backgroundPathAndBytesable: BackgroundPathAndBytesable[T]) {
    def inFuture[R](fun: (CuratorEvent) => R) = {
      val p = Promise[R]()
      new PathAndBytesableWithPromise(backgroundPathAndBytesable.inBackground({ event: CuratorEvent =>
        p.complete(Try(fun(event)))
      }: BackgroundCallback), p)
    }
  }
}
