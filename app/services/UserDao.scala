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

//DAO for users backed by a mongoDB collection
object UserDao{

	private def collection = ReactiveMongoPlugin.db.collection[JSONCollection]("users")

	/**
	 * Save a user
	 */
	 def save(user: User): Future[User]={
	 	collection.save(user).map{
	 		case ok if ok.ok =>
	 			Logger.debug("user saved: " + user)
	 			user
	 		case error => {
	 			Logger.error("Error saving user: " + user)
	 			throw new RuntimeException(error.message)
	 		}
	 	}
	 }

	 /**
	  * Find all users
	  */
	 def findAll(page: Int, perPage: Int):Future[List[User]] = {
	  	collection.find(Json.obj())
	  		.options(QueryOpts(skipN = page * perPage))
	  		.sort(Json.obj("_id" -> -1))
	  		.cursor[User]
	  		.toList(perPage)
	  }

	  /**
	   * Delete a user
	   */
	def delete(id: String):Unit={
	  	collection.remove(BSONDocument("_id" -> BSONObjectID(id)))
	  	// .onComplete{
  			//case Failure(e) => throw e
  			// case Success(_) => {
  			//  	Logger.debug("Deleted user with id: " + id)
  			// }
	  	// }
	}

	  /**
	   * Get a count of all of the users in the db
	   */
	  def count: Future[Int] = {
	  	ReactiveMongoPlugin.db.command(Count(collection.name))
	  }
}