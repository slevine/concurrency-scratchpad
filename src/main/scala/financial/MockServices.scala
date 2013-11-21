package financial

import com.typesafe.scalalogging.slf4j.Logging


/**
 * A bunch of Mock functions to simulate a below average financial platform
 * Created by IntelliJ IDEA.
 * Author: Steve Levine
 * Date: 11/21/13
 */


trait Services extends Logging {

  /**
   * Get a quote for a given symbol from the Cache
   * @param symbol to obtain quote for
   * @return hardcoded Int
   */
  def quote(symbol: String): Int

  /**
   * Approx number of shares outstanding
   * @param symbol to obtain outstanding shares
   * @return hardcoded Int
   */
  def outstandingShares(symbol: String): Int

  /**
   * Calculate the Market Cap
   * Takes 2s
   * @param price current price
   * @param shares outstanding shares
   * @return Market Cap
   */
  def calculateMarketCap(price: Int, shares: Int): Int

  /**
   * Update the quote cache
   */
  def updateQuoteCache(): Unit
}

trait MockServices extends Services {

  /**
   * Get a quote for a given symbol
   * This fun takes 5s to exe
   * @param symbol to obtain quote for
   * @return hardcoded Int
   */
  override def quote(symbol: String) = {
    logger.debug("obtaining quote...")
    Thread.sleep(5000)
    symbol match {
      case "aapl" ⇒ 517
      case "tsla" ⇒ 122
    }
  }

  /**
   * Approx number of shares outstanding for 2 co's
   * This fun takes 3s to exe
   * @param symbol to obtain outstanding shares
   * @return hardcoded Int
   */
  override def outstandingShares(symbol: String) = {
    logger.debug("getting outstandigShares...")
    Thread.sleep(3000)
    symbol match {
      case "aapl" ⇒ 899740000
      case "tsla" ⇒ 122590000
    }
  }

  /**
   * Calculate the Market Cap
   * Takes 2s
   * @param price
   * @param shares
   * @return
   */
  override def calculateMarketCap(price: Int, shares: Int) = {
    logger.debug("calculatingMarketCap...")
    Thread.sleep(2000)
    price * shares
  }

  /**
   * In this impl, there is no "cache" so this fun does nothing
   * but log a message
   */
  override def updateQuoteCache() {
    (1 to 10) foreach {
      x ⇒
        Thread.sleep(1000)
        logger.debug(s"updateQuoteCache, on iteration $x")
    }
  }
}
