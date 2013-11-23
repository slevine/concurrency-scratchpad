package async

import financial.MockServices

import scala.concurrent.ExecutionContext.Implicits.global
import scala.async.Async.{async, await}

/**
 * Created by IntelliJ IDEA.
 * Author: Steve Levine
 * Date: 11/23/13
 */
object SimpleAsync extends App with MockServices {

  val marketCap = async {
    logger.debug("marketCap calc spawned")
    val q = async(quote("tsla"))
    val o = async(outstandingShares("tsla"))
    logger.debug("calculator spawned")
    calculateMarketCap(await(q), await(o))
  }
  val u = async(updateQuoteCache())

  marketCap onSuccess {
    case mc ⇒ logger.debug(s"market cap = $mc")
  }

  // Prevent the program from exiting before the proper conclusion
  while (!u.isCompleted) {}
}