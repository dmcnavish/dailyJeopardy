package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import anorm._

import models.User
import models.Answer

import play.api.libs.json._
import play.api.libs.functional.syntax._

object Jeopardy extends Controller{
	val answerForm = Form(
		mapping(
			"id" -> ignored(NotAssigned:Pk[Long]),
			"userId" -> of[Long],
			"answer" -> nonEmptyText,
			"points" -> number
		)(Answer.apply)(Answer.unapply)
	)

	def form = Action{
		//Redirect(routes.Jeopardy.answers)
		Ok(views.html.jeopardy(User.userAndIds(), answerForm))
	}

	/**
	 * Used to read values from the Json request
	 * __ is an alias for JsPath companion object
	 */
	implicit val rds = (
			(__ \ 'userId).read[String] and 
			(__ \ 'answer).read[String]
		)tupled

	/**
	 * Save the answer submitted
	 */
	def submitAnswer = Action(parse.json){request =>
		request.body.validate[(String, String)].map{
				case(userId, answer) => {
					Logger.debug("userId: " + userId + " " + "answer: " + answer)
					Answer.submitAnswer(userId.toLong, answer)
					Ok(Json.obj("status" -> "OK", "message" -> "Answer submitted."))
				}
		}.recoverTotal{
      		e => {
      			Logger.error("Error processing answer:" + e)
      			BadRequest("Detected error:"+ JsError.toFlatJson(e))
      		}
    	}
	}

	/**
	 * Lock all answer form submits for the day
	 */
	def lockAnswers = TODO
}