package services

import org.joda.time.DateTime
import play.modules.reactivemongo.ReactiveMongoPlugin
import play.modules.reactivemongo.json.collection.JSONCollection
import play.modules.reactivemongo.json.BSONFormats._
import play.api.Play.current
import models._
import models.User._
import scala.concurrent.Future
import play.api.libs.concurrent.Execution.Implicits._
import play.api.libs.json.Json
import reactivemongo.api.QueryOpts
import reactivemongo.core.commands.Count
import reactivemongo.bson.BSONObjectID
import reactivemongo.bson.BSONDocument

import play.api.Logger

object AnswerDao{

	private def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("answers")

	/**
	 * Save an answer
	 */	
	 def save(answer: Answer): Future[Answer]={
	 	if(answer.userId == None){
	 		Logger.debug("userId required!")
	 		throw new RuntimeException("userId required")
	 	}

	 	collection.save(answer).map{
	 		case ok if ok.ok => {
	 			Logger.debug("answer saved: " + answer)
	 			answer
	 		}
	 		case error => {
	 			Logger.error("Error saving answer: " + answer)
	 			throw new RuntimeException(error.message)
	 		}
	 	}
	 }

	 def findAll():Future[List[Answer]] ={
	 	collection.find(Json.obj())
	 		// .options(QueryOpts())
	 		.sort(Json.obj("_id" -> -1))
	 		.cursor[Answer]
	 		.toList()
	 }

}