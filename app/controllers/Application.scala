package controllers

import play.api._
import play.api.mvc._
import play.api.data._
import play.api.data.Forms._

object Application extends Controller {
  

  	//TODO: Change to point to the question page
	def index = Action {
		Redirect(routes.Jeopardy.form)
	}
	
}