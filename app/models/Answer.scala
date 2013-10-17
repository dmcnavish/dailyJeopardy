package models

import play.api.Logger

import play.api.data.Forms._
import play.api.data.format.Formats._
import play.api.data.validation.Constraints._
import play.api.libs.json.Json
import reactivemongo.bson.BSONObjectID
import play.modules.reactivemongo.json.BSONFormats._
import org.joda.time.DateTime
import play.api.data._

case class Answer(
	_id: Option[BSONObjectID],
	userId: Option[BSONObjectID], 
	answer: String, 
	points: Option[Int], 
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
			"userId" -> optional(of[String] verifying pattern(
				"""[a-fA-F0-9]{24}""".r,
				"constraint.objectId",
				"error.objectId")),
			"answer" -> nonEmptyText,
			"points" -> optional(of[Int]),
			"createDate" -> optional(of[Long]),
			"modifiedDate" -> optional(of[Long])
		) {(id,userId,answer,points,createDate,modifiedDate) =>
			Answer(
				id.map(new BSONObjectID(_)),
				userId.map(new BSONObjectID(_)),
				answer,
				points,
				createDate.map(new DateTime(_)),
				modifiedDate.map(new DateTime(_)))
		} {answer => 
			Some(
				(answer._id.map(_.toString),
					answer.userId.map(_.toString),
					answer.answer,
					answer.points,
					answer.createDate.map(_.getMillis),
					answer.modifiedDate.map(_.getMillis))
				)
			}
		)
	
}	