package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._

import anorm._

import models.User
import models.Answer

object Jeopardy extends Controller{
	val answerForm = Form(
		mapping(
			"id" -> ignored(NotAssigned:Pk[Long]),
			"userId" -> of[Long],
			"answer" -> nonEmptyText
		)(Answer.apply)(Answer.unapply)
	)

	def form = Action{
		//Redirect(routes.Jeopardy.answers)
		Ok(views.html.jeopardy(answerForm))
	}

	/**
	 * Save the user's answer
	 */
	def submitAnswer(userId: Long, answer: String) = Action{
		//TODO save answer
		Ok(views.html.jeopardy(answerForm))
	}

	/**
	 * Lock all answer form submits for the day
	 */
	def lockAnswers = TODO
}