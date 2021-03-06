package dataflow

import akka.dataflow._
import financial.MockServices

import scala.concurrent.ExecutionContext.Implicits.global

/**
 * Created by IntelliJ IDEA.
 * Author: Steve Levine
 * Date: 3/23/15
 */
object SimpleDataFlow extends App with MockServices {

  val marketCap = flow {
    val q = flow {quote("tsla")}
    val os = flow {outstandingShares("tsla")}
    calculateMarketCap(q(), os())
  }

  val u = flow {updateQuoteCache()}

  marketCap onComplete {mc ⇒ logger.debug(s"market cap = ${mc.get}")}

  while (!u.isCompleted) {}
}
