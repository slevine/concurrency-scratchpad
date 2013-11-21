import financial.MockServices
import scala.concurrent.{Future, Promise}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.util.Try

/**
 * A simple example of how Promises and Futures can
 * make a program more concurrent.
 *
 * This impl uses a more imperative style onSuccess{..}
 *
 * Will be the subject of a future blog post.
 */
object SimplePromises extends App with MockServices {

  val quotePromise = Promise[Int]()
  val quotefuture = quotePromise.future

  val updatePromise = Promise[Boolean]()

  val quoteProducer = Future {
    logger.debug("quoteProducer spawned")
    Future {quote("tsla")} onSuccess {
      case q ⇒
        logger.debug("quoteProducer answered quotePromise")
        quotePromise success q
    }
    updateQuoteCache()
    updatePromise.complete(Try(true))
  }

  val calculator = Future {
    logger.debug("calculator spawned")
    val o = outstandingShares("tsla")
    quotefuture onSuccess {
      case q ⇒
        logger.debug("calculator received quotePromise value")
        logger.debug(s"market cap = ${calculateMarketCap(q, o)}")
    }
  }

  // Prevent the program from exiting before the proper conclusion
  while(!updatePromise.isCompleted) {}
}
