package actors

/**
 * Created by IntelliJ IDEA.
 * Author: Steve Levine
 * Date: 4/7/14
 */

import actors.Market.{CalcMarketCap, GetFutureQuote, GetQuote, _}
import akka.actor._
import akka.event.LoggingReceive
import akka.pattern.{ask, pipe}
import akka.util.Timeout
import financial.MockServices

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent._
import scala.concurrent.duration._

/**
 * Created by IntelliJ IDEA.
 * Author: Steve Levine
 * Date: 4/7/14
 */

object Market {

  case class Quote(symbol: String, bid: Double, ask: Double, price: Double)

  case class Result(symbol: String, price: Double)

  case class GetQuote(symbol: String)

  case class GetOutstandingShares(symbol: String)

  case class GetFutureQuote(symbol: String)

  case class CalcMarketCap(symbol: String)

  case class CalcMarketCapFlat(symbol: String)

}

class QuoteService extends Actor with ActorLogging with MockServices {

  def receive = LoggingReceive {
    case GetQuote(symbol) ⇒ sender ! quote(symbol)
    case GetFutureQuote(ticker) ⇒ future {quote(ticker)} pipeTo sender
  }

}

class MarketService extends Actor with ActorLogging with MockServices {
  Thread.sleep(100)

  def receive = LoggingReceive {
    case GetOutstandingShares(symbol) ⇒ sender ! outstandingShares(symbol)
  }
}

class CalcSvc extends Actor with ActorLogging with MockServices {
  implicit val timeout = Timeout(15 seconds)

  val quoteSvc = context.actorOf(Props[QuoteService], "quoteSvc")
  val marketSvc = context.actorOf(Props[MarketService], "marketSvc")

  def receive = LoggingReceive {
    case CalcMarketCap(symbol) ⇒
      val fq = quoteSvc ? GetQuote(symbol)
      val fos = marketSvc ? GetOutstandingShares(symbol)
      val result = for {
        q ← fq.mapTo[Int]
        mc ← fos.mapTo[Int]
      } yield calculateMarketCap(q, mc)
      sender ! result
    case CalcMarketCapFlat(symbol) ⇒
      val fq = quoteSvc ? GetQuote(symbol)
      val fos = marketSvc ? GetOutstandingShares(symbol)
      val result = fq.mapTo[Int] flatMap (q ⇒ fos.mapTo[Int] map (os ⇒ calculateMarketCap(q,os)))
      sender ! result
  }

}

class SimpleActor extends Actor with ActorLogging {
  log.info("Starting Main")
  val calcSvc = context.actorOf(Props[CalcSvc], "calcSvc")
  calcSvc ! CalcMarketCap("tsla")
  calcSvc ! CalcMarketCapFlat("tsla")

  def receive = LoggingReceive {
    case x: Promise[_] ⇒
      val result = Await.result(x.future, 20 seconds)
      log.info(s"Market Cap = $result")
      context.system.shutdown()
  }
}

