package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

import anorm._

import models.User

object SignUp extends Controller{
	val userForm = Form(
		mapping(
			"id" -> ignored(NotAssigned:Pk[Long]),
			"firstName" -> nonEmptyText,
			"lastName" -> nonEmptyText,
			"email" -> nonEmptyText
		)(User.apply)(User.unapply)
	)

	def index = Action {
		Redirect(routes.SignUp.users)
	}

	/** 
	 * List all of the existing users
	 */
	def users = Action{
		Ok(views.html.signup(User.all(), userForm))
	}

	/**
	 * Create a new User
	 */
	def newUser = Action {implicit request =>
		userForm.bindFromRequest.fold(
			errors=> BadRequest(views.html.signup(User.all(), errors)),
			{case (user) =>{
				Logger.info("first Name: " + user.firstName + " last name: " + user.lastName + " email: " + user.email)
				User.create(user.firstName, user.lastName, user.email)
				Redirect(routes.SignUp.users)
				} 
			}
		)
	}

	/**
	 * Delete an existing User
	 */
	def deleteUser(id: Long) = Action {
		User.delete(id)
		Redirect(routes.SignUp.users)
	}

}