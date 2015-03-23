package async

import financial.MockServices

import scala.async.Async.{async, await}
import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by IntelliJ IDEA.
 * Author: Steve Levine
 * Date: 11/23/13
 */
object SimpleAsync extends App with MockServices {

  val marketCap = async {
    val q = async(quote("tsla"))
    val o = async(outstandingShares("tsla"))
    calculateMarketCap(await(q), await(o))
  }

  val u = async(updateQuoteCache())

  marketCap onSuccess {case mc ⇒ logger.debug(s"market cap = $mc")}

  // Prevent the program from exiting before the proper conclusion
  while (!u.isCompleted) {}
}
