package depInject

import javax.inject.Inject
import play.api.http.FileMimeTypes
import play.api.i18n.{ Langs, MessagesApi }
import play.api.mvc._
import service.{ AdminActionBuilder, UserActionBuilder }

case class SecuredControllerComponents @Inject() (
  userActionBuilder: UserActionBuilder,
  adminActionBuilder: AdminActionBuilder,
  actionBuilder: DefaultActionBuilder,
  parsers: PlayBodyParsers,
  messagesApi: MessagesApi,
  langs: Langs,
  fileMimeTypes: FileMimeTypes,
  executionContext: scala.concurrent.ExecutionContext
) extends ControllerComponents
