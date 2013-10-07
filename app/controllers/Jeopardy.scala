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

import reactivemongo.api._
import play.modules.reactivemongo._
import play.modules.reactivemongo.json.collection._

import scala.concurrent.Future

import models.JsonFormats._

import services.UserDao


object Jeopardy extends Controller with MongoController{

  /*
   * Get a JSONCollection (a Collection implementation that is designed to work
   * with JsObject, Reads and Writes.)
   * Note that the `collection` is not a `val`, but a `def`. We do _not_ store
   * the collection reference to avoid potential problems in development with
   * Play hot-reloading.
   */
	def collection: JSONCollection = db.collection[JSONCollection]{"answers"}

	val answerForm = Form(
		mapping(
			//"id" -> ignored(NotAssigned:Pk[Long]),
			"userId" -> of[Long],
			"answer" -> nonEmptyText,
			"points" -> number,
			"created" -> of[Long]
		)(Answer.apply)(Answer.unapply)
	)

	def form = Action{
		//Redirect(routes.Jeopardy.answers)
		Ok(views.html.jeopardy(Nil, answerForm))
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
					// Answer.submitAnswer(userId.toLong, answer)
					//Ok(Json.obj("status" -> "OK", "message" -> "Answer submitted."))

					val answerObj = Answer(userId.toLong, answer, 0, new java.util.Date().getTime())	
					val futureResult = collection.insert(answerObj)
					Async{
						//when the insert is performed, send a OK 200 result
						futureResult.map{r => Ok(Json.obj("status" -> "OK", "message" -> "%s".format(r)))}
					}
				}
		}.recoverTotal{
      		e => {
      			Logger.error("Error processing answer:" + e)
      			BadRequest("Detected error:"+ JsError.toFlatJson(e))
      		}
    	}
	}

	def answers = Action{
		Async{
			val cursor: Cursor[Answer] = collection.find(Json.obj("points" -> 0)).cursor[Answer]
			
			//gather all the JsObjects in a list
			val futureAnswerList: Future[List[Answer]] = cursor.toList
			futureAnswerList.map{a => Logger.debug("a: " + a.toString)}
			//everythings ok, reply with the array
			futureAnswerList.map{a => Ok(views.html.answers(a))}
		}
		// Ok(views.html.answers(Answer.listAnswers(0,0)))
	}

	/**
	 * Lock all answer form submits for the day
	 */
	def lockAnswers = TODO
}