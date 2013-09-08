package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current

case class Answer(id: Pk[Long] = NotAssigned, userId: Long, answer: String)

object Answer{
	get[Pk[Long]]("id") ~
	get[Long]("userId") ~
	get[String]("answer") map {
		case id~userId~answer=> Answer(id, userId, answer)
	}
}