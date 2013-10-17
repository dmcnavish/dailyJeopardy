package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import models.User


import play.api.libs.json.Json
import play.api.libs.concurrent.Execution.Implicits._
import services.UserDao
import reactivemongo.bson.BSONObjectID


object SignUp extends Controller{ 

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