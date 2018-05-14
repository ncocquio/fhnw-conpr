package observables

import rx.lang.scala.Observable
import rx.lang.scala.Observer
import rx.lang.scala.Subscriber

object RxExample extends App {
  // The most general way to create an observable.
  val observable = Observable[Int]((obs: Subscriber[Int]) => {
    new Thread() {
      override def run() {
        var cnt = 0
        while(!obs.isUnsubscribed) {
          obs.onNext(cnt)
          cnt += 1
        }
        obs.onCompleted()
      }
    }.start
  })
  
  observable.map(_ * 2).subscribe(i => println(i))
}