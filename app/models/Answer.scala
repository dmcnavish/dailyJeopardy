package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.Logger

case class Answer(id: Pk[Long] = NotAssigned, userId: Long, answer: String, points: Int)

object Answer{
	
	val answer = {
		get[Pk[Long]]("id") ~
		get[Long]("userId") ~
		get[String]("answer") ~
		get[Int]("points") map {
			case id~userId~answer~points=> Answer(id, userId, answer, points)
		}
	}

	def submitAnswer(userId: Long, answer: String){
		Logger.debug("Submitting Answer for userId: " + userId + " Answer: " + answer)
		DB.withConnection{implicit c =>
			SQL("insert into answer (userId, answer) values ({userId}, {answer})").on(
					'userId -> userId,
					'answer -> answer
				).executeUpdate()
		}
	}
}