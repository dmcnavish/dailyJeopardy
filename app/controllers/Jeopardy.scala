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
import play.api.Play.current


import scala.concurrent.Future

import play.api.libs.concurrent.Execution.Implicits._
import services.UserDao
import services.AnswerDao
import reactivemongo.bson.BSONObjectID


object Jeopardy extends Controller{

	def form = Action{
		Async{
			for{
				users <- UserDao.findAll(0,100)
			} yield{
				Logger.debug("users: " + users)
				Ok(views.html.jeopardy(users, Answer.form))
			}
		}
	}

	/**
	 * Save the answer submitted
	 */
	 def submitAnswer = Action{implicit request =>
	 	Answer.form.bindFromRequest.fold(
	 		errors => {
	 			Logger.error("Errors: " + errors)
	 			Async{
					for{
						users <- UserDao.findAll(0, 100) //TODO:add paging to UI
					} yield {
						BadRequest(views.html.jeopardy(users, errors))
					}
				}
	 			},
	 		{case (answer) => {
	 			Logger.debug("answer: " + answer)
	 			AnswerDao.save(answer)
	 			Async{
					for{
						users <- UserDao.findAll(0, 100) //TODO:add paging to UI
					} yield {
						Ok(views.html.jeopardy(users, Answer.form))
					}
				}

	 		}}
	 	)
	 }

	def answers = Action{
		Logger.trace("getting all users")

		Async{
			for{
				// users <- UserDao.findAll(0,100)
				answers <- AnswerDao.findAll()
			} yield{
				Logger.debug("Answers: " + answers)
				Ok(views.html.answers(answers, Answer.form))
			}
		}
	}

	/**
	 * Lock all answer form submits for the day
	 */
	def lockAnswers = TODO
}