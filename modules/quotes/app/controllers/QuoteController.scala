// Should be added controllers for the play routes
package controllers.quotes

import com.krishna.model.Genre.Genre
import com.krishna.response.ResponseResult
import com.krishna.util.Logging
import depInject.{ SecuredController, SecuredControllerComponents }

import javax.inject._
import model.UserDetail
import play.api.mvc._
import service.QuoteQueryService
import config.DecodeHeader
import play.api.Configuration

import scala.concurrent.ExecutionContext

/**
  * This controller creates an 'Action' to handle HTTP requests to the
  * application's quotes from 'quotes' table.
  */
@Singleton
class QuoteController @Inject() (
  quoteService: QuoteQueryService,
  scc: SecuredControllerComponents
)(implicit executionContext: ExecutionContext, config: Configuration)
    extends SecuredController(scc)
    with ResponseResult
    with Logging {

  /**
    * A REST endpoint that gets a random quote as a JSON from quotes table.
    * Anyone can call this API endpoint.
    * This API endpoint is not used in mobile app
    */
  def getRandomQuote: Action[AnyContent] = Action.async { implicit request =>
    log.info("Executing getRandomQuote in QuoteController.")
    quoteService.randomQuoteService(1)
  }

  /**
    * A REST endpoint that gets a quote of the day as JSON from quotes table.
    * It should be trigger after midnight on every day using cron job.
    * It should stores the past 5 quotes of the day in the Redis cache storage.
    * Anyone can call this API endpoint.
    * This API endpoint is not used in mobile app
    * @param date : Can take milliseconds date format as a path parameter to gets the
    *             previous 5 days quote of the day
    */
  def getQuoteOfTheDay(date: Option[String]): Action[AnyContent] = Action.async {
    implicit request =>
      log.info("Executing getQuoteOfTheDay in QuoteController.")
      quoteService.quoteOfTheDayService(date)
  }

  /**
    * Get all the cached quotes from last 5 days
    * All of the quotes csv id are stored in the Redis storage
    * @return last 5 quote of the day
    */
  def getCachedQuotes: Action[AnyContent] = Action.async { implicit request =>
    log.info("Executing get last five quotes of the day in QuoteController.")
    DecodeHeader(request.headers) match {
      case Left(_) =>
        log.info("Getting cached quotes for users that are not logged in")
        quoteService.cachedQuotesService(None)
      case Right(user: UserDetail) =>
        quoteService.cachedQuotesService(Some(user))
    }
  }

  /**
    * A REST endpoint that gets all the quotes as JSON from quotes table.
    * Response will be in pagination with the limit of 100 and offset value of 0,
    * which will be passed as query parameter
    * Only Admin can perform this action.
    */
  def getAllQuotes: Action[AnyContent] = AdminAction.async { implicit request =>
    log.info("Executing getAllQuotes in QuoteController.")

    val limit: Int =
      request.getQueryString("limit").map(_.toInt).getOrElse(100)
    val offset: Int =
      request.getQueryString("offset").map(_.toInt).getOrElse(0)

    quoteService.allQuotesService(limit, offset)
  }

  /**
    * A REST endpoint that gets random 10 quotes as JSON from quotes table.
    *  Anyone can call this API endpoint.
    */
  def getFirst10Quotes: Action[AnyContent] = Action.async { implicit request =>
    log.info("Executing getFirst10Quotes in QuoteController.")
    quoteService.random10QuoteService(10)
  }

  /**
    * A REST endpoint that creates or altered the fav tag in the fav_quotes table.
    * Only the logged user can perform this action and should be stored to user's id only
    */
  def favQuote(csvId: String): Action[AnyContent] = UserAction.async { implicit request =>
    DecodeHeader(request.headers) match {
      case Right(user) =>
        log.info(s"Executing favQuote by user: ${ user.email } in QuoteController.")
        quoteService.updateFavQuoteService(csvId: String, user)
      case Left(errorMsg) => responseErrorResult(errorMsg)
    }

  }

  /**
    * A REST endpoint that gets all favorite quotes as JSON from fav_quotes table.
    * Only the logged user can perform this action and should retrieve their own fav quotes only
    */
  def getFavQuotes: Action[AnyContent] = UserAction.async { implicit request =>
    DecodeHeader(request.headers) match {
      case Right(user) =>
        log.info(s"Executing getFavQuotes by user: ${ user.email } in QuoteController.")
        quoteService.getFavQuotesService(user.id)
      case Left(errorMsg) => responseErrorResult(errorMsg)
    }
  }

  /**
    * A REST endpoint that gets a random quote as per selected genre from the table from quotes table.
    * Returns 400 response code when the invalid genre is used
    * Anyone can do perform this action
    */
  def getGenreQuote(genre: Genre): Action[AnyContent] = Action.async { implicit request =>
    log.info("Executing getGenreQuote in QuoteController.")
    quoteService.genreQuoteService(genre)
  }

  /**
    * A REST endpoint that gets 10 matched autocomplete list from the searched parameter
    */
  def getAuthorsAutocomplete(parameter: String): Action[AnyContent] = UserAction.async {
    implicit request =>
      log.info("Executing getAuthorsAutocomplete in QuoteController.")
      responseSeqString(quoteService.searchAuthorsSql(parameter))
  }

}
