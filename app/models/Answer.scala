package models

import anorm._
import anorm.SqlParser._
import play.api.db._
import play.api.Play.current
import play.api.Logger

//id: Pk[Long] = NotAssigned, 
case class Answer(
	_id: Option[BSONObjectID],
	userId: Long, 
	answer: String, 
	points: Int, 
	createDate: Option[DateTime],
	modifiedDate: Option[DateTime])

object Answer{
	implicit val answerFormat = Json.format[Answer]

	val form = Form(
		mapping(
			"id" -> optional(of[String] verifying pattern(
				"""[a-fA-F0-9]{24}""".r,
				"constraint.objectId",
				"error.objectId")),
			"userId" -> BSONObjectID,
			"answer" -> nonEmptyText,
			"points" -> Int,
			"createDate" -> optional(of[Long]),
			"modifiedDate" -> optional(of[Long])
		) {id,userId,answer,points,createdDate,modifiedDate) =>
			Answer(
				id.map(new BSONObjectId(_)),
				userId,
				answer,
				points,
				createDate.map(new DateTime(_),
				modifiedDate.map(new DateTime(_)))
		}

		)

		)
	
}	
// 	val answer = {
// 		get[Pk[Long]]("id") ~
// 		get[Long]("userId") ~
// 		get[String]("answer") ~
// 		get[Int]("points") map {
// 			case id~userId~answer~points=> Answer(id, userId, answer, points)
// 		}
// 	}

// 	def submitAnswer(userId: Long, answer: String){
// 		Logger.debug("Submitting Answer for userId: " + userId + " Answer: " + answer)
		
// 		// DB.withConnection{implicit c =>
// 		// 	SQL("insert into answer (userId, answer) values ({userId}, {answer})").on(
// 		// 			'userId -> userId,
// 		// 			'answer -> answer
// 		// 		).executeUpdate()
// 		// }
// 	}

// 	/**
// 	 * List answers for all users by page
// 	 */
// 	def listAnswers(page: Int, size: Int): List[Answer] ={
// 		Logger.trace("list answers for page: " + page)
// 		DB.withConnection{ implicit c =>
// 			SQL("select * from answer").as(answer *)
// 		}
// 	}
// }

// object JsonFormats{
// 	import play.api.libs.json.Json
// 	import play.api.data._
// 	import play.api.data.Forms._

// 	implicit val answerFormat = Json.format[Answer]
// }