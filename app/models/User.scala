package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.Logger

case class User(id: Pk[Long] = NotAssigned, firstName: String, lastName: String, email: String)

object User{

	val user = {
		get[Pk[Long]]("id") ~
		get[String]("firstName") ~ 
		get[String]("lastName") ~
		get[String]("email") map {
			case id~firstName~lastName~email => User(id, firstName, lastName,email)
		}
	}

	def all(): List[User] = DB.withConnection {implicit c =>
		SQL("select * from user").as(user *)
	}

	def userAndIds(): Seq[(String, String)] = {
		Logger.info("Users: " + User.all())
		User.all().foldLeft( Seq[(String, String)]()){ (s, current) => (s:+(current.id.toString,current.firstName)) }
	}

	def create(firstName: String, lastName: String, email: String) {
		DB.withConnection {implicit c =>
			SQL("insert into user (firstName, lastName, email) values ({firstName}, {lastName}, {email})").on(
				'firstName -> firstName,
				'lastName -> lastName,
				'email -> (if(email == Nil) "" else email)
			).executeUpdate()
		}
	}

	def delete(id: Long) {
		DB.withConnection {implicit c =>
			SQL("delete from user where id = {id}").on(
				'id -> id
				).executeUpdate()
		}
	}

}