package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

// import anorm._

import models.User


import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import services.UserDao
import reactivemongo.bson.BSONObjectID
// import play.api.libs.functional.syntax._
// import play.api.Play.current
// import reactivemongo.api._
// import reactivemongo.bson._
// import reactivemongo.bson.handlers.DefaultBSONHandlers._
// import play.modules.reactivemongo._
// import play.modules.reactivemongo.json.collection._

// import scala.concurrent.Future

// import models.JsonFormats._

object SignUp extends Controller{ //with MongoController{

	// def collection: JSONCollection = db.collection[JSONCollection]{"users"}

	// implicit val rds = {
	// 	(__ \ 'firstName).read[String] and
	// 	(__ \ 'lastName).read[String] and
	// 	(__ \ 'displayName).read[String] and
	// 	(__ \ 'email).read[String]
	// }tupled


	// val userForm = Form(
	// 	mapping(
	// 		 "id" -> Long,//ignored(NotAssigned:Pk[Long]),
	// 		"firstName" -> nonEmptyText,
	// 		"lastName" -> nonEmptyText,
	// 		"displayName" -> text,
	// 		"email" -> nonEmptyText
	// 	)(User.apply)(User.unapply)
	// )

	def index = Action {
		Redirect(routes.SignUp.users)
	}

	/** 
	 * List all of the existing users
	 */
	def users = Action{
		Logger.debug("getting all users")
		Async{
			for{
				users <- UserDao.findAll(0, 100) //TODO:add paging to UI
			} yield {
				Logger.debug("users: " + users)
				Ok(views.html.signup(users,User.form))
			}
		}
	}

	/**
	 * Create a new User
	 */
	def newUser = Action {implicit request =>
		User.form.bindFromRequest.fold(
			errors=> {
				Logger.info("Errors: " + errors)
				BadRequest(views.html.signup(Nil, errors))
			},
			{case (user) =>{
				Logger.info("first Name: " + user.firstName + " last name: " + user.lastName + " email: " + user.email)
				UserDao.save(user)
				Redirect(routes.SignUp.users)
				} 
			}
		)
	}

	/**
	 * Delete an existing User
	 */
	def deleteUser(id: String) = Action {
		Logger.debug("Deleting User with id: " + id)
		UserDao.delete(id)
		Redirect(routes.SignUp.users)
	}

}