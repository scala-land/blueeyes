package blueeyes.bkka

import akka.actor.Actor
import akka.dispatch.Future
import akka.util.Duration
import scala.collection.immutable.Queue
import com.weiglewilczek.slf4s.Logging

/**
 * A base trait for typeclass instances that describe how to stop a given type of service.
 * TODO: make polymorphic in the type of the Future, perhaps using an ADT such as StopResult { StopSuccess / StopFailure }
 * 
 * In order for implementations of this trait to work properly in a graph where the same service is a dependant of multiple other services, 
 * it should be safe for stop to be called multiple times with the same target.
 */
trait Stop[-A] {
  def stop(a: A): Future[Any]
}

/** 
 * A DAG of things that can be stopped. Dependents are traversed in breadth-first order.
 */
sealed trait Stoppable {
  protected def stop: Future[Any]
  def dependents: List[Stoppable]
}

object Stoppable extends Logging {
  def apply[A](a: A, deps: List[Stoppable] = Nil)(implicit stopa: Stop[A], timeout: Actor.Timeout): Stoppable = new Stoppable {
    protected def stop = {
      Future(logger.info("About to stop " + a)) flatMap { _ =>
        stopa.stop(a).onResult {
          case v => logger.info("Stopped " + a + " with result " + v)
        } recover { case ex => 
          logger.info("Stop failed: ")
          ex.printStackTrace
        } onTimeout { future =>
          logger.info("Stop timed out for " + a + " after timeout of " + (future.timeoutInNanos / 1000 / 1000) + " ms")
        }
      }
    }

    def dependents = deps
  }

  /**
   * Stops the specified stoppable, returning a future containing a list of the results
   * of the stoppable graph in breadth-first order. When this future is completed, 
   * everything will be stopped.
   *
   * TODO: Make it possible to specify whether failure to stop any given service
   * should prevent the stopping of its dependants. At present, any exception encountered
   * in stopping will stop the stopping process, leaving the system in a potentially 
   * indeterminate state.
   */
  implicit def stoppableStop(implicit timeout: akka.actor.Actor.Timeout): Stop[Stoppable] = new Stop[Stoppable] {
    def stop(stoppable: Stoppable) = {
      def _stop(q: Queue[List[Stoppable]]): Future[List[Any]] = {
        if (q.isEmpty) Future(Nil)
        else {
          val (xs, remainder) = q.dequeue
          if (xs.isEmpty) _stop(remainder)
          else {
            val stopFutures = xs.map(_.stop)
            Future.sequence(stopFutures, (stopFutures.map(_.timeoutInNanos).max / 1000 / 1000) min timeout.duration.toMillis)
                  .flatMap(r => _stop(remainder ++ xs.map(_.dependents)).map(r ::: _))
          }
        }
      }
      
      _stop(Queue(List(stoppable)))
    }
  }

  def stop(stoppable: Stoppable)(implicit timeout: akka.actor.Actor.Timeout) = stoppableStop(timeout).stop(stoppable)
}


// vim: set ts=4 sw=4 et: