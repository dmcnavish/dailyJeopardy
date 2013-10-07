package models

import play.api.Logger

import org.joda.time.DateTime
import play.api.data._
import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._

case class User(
	_id: Option[BSONObjectID],
	firstName: String,
	lastName: String,
	displayName: Option[String],
	email: String,
	createDate: Option[DateTime]
)

object User{
	//used by both JSON library and reactive mongo to serialize/deserialize a user
	implicit val userFormat = Json.format[User]

	val form = Form(
		mapping(
			"id" -> optional(of[String] verifying pattern(
				"""[a-fA-F0-9]{24}""".r,
				"constraint.objectId",
				"error.objectId")),
			"firstName" -> nonEmptyText,
			"lastName" -> nonEmptyText,
			"displayName" -> optional(of[String]),
			"email" -> nonEmptyText,
			"createDate" -> optional(of[Long])			
			) {(id,fistName, lastName, displayName, email, createDate) => 
				User(
					id.map(new BSONObjectID(_)),
					fistName,
					lastName,
					displayName,
					email,
					createDate.map(new DateTime(_)))
		} { user =>
			Some(
				(user._id.map(_.toString),
					user.firstName,
					user.lastName,
					user.displayName,
					user.email,
					user.createDate.map(_.getMillis))
				)
			}
		)
}



// case class User(firstName: String, lastName: String, displayName: String, email: String)

// object User{

// 	val user = {
// 		get[Pk[Long]]("id") ~
// 		get[String]("firstName") ~ 
// 		get[String]("lastName") ~
// 		get[String]("displayName") ~
// 		get[String]("email") map {
// 			case id~firstName~lastName~displayName~email => User(id, firstName, lastName,displayName, email)
// 		}
// 	}

// 	def all(): List[User] = DB.withConnection {implicit c =>
// 		SQL("select * from user").as(user *)
// 	}

// 	def userAndIds(): Seq[(String, String)] = {
// 		Logger.info("Users: " + User.all())
// 		User.all().foldLeft( Seq[(String, String)]()){ (s, current) => (s:+(current.id.toString,current.firstName)) }
// 	}

// 	def create(firstName: String, lastName: String, email: String) {
// 		DB.withConnection {implicit c =>
// 			SQL("insert into user (firstName, lastName, email) values ({firstName}, {lastName}, {email})").on(
// 				'firstName -> firstName,
// 				'lastName -> lastName,
// 				'displayName -> (firstName + "." + lastName),
// 				'email -> (if(email == Nil) "" else email)
// 			).executeUpdate()
// 		}
// 	}

// 	def delete(id: Long) {
// 		DB.withConnection {implicit c =>
// 			SQL("delete from user where id = {id}").on(
// 				'id -> id
// 				).executeUpdate()
// 		}
// 	}

// }