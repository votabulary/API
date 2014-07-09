package com.votabulary.api

import com.votabulary.model.DBConfig
import com.votabulary.model.member.{MemberDAL, Member}
import spray.json.DefaultJsonProtocol

import spray.http.StatusCodes._

object MemberJSON extends DefaultJsonProtocol {
  implicit val memberFormat = jsonFormat10(Member)
}

/**
 * Created by jason on 6/29/14.
 */
trait MemberService extends BaseService {// with DBConfig {
//  this: DBConfig =>

  import MemberJSON._


  val memberRoute = {
    import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
    import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

    path("members") {
      get { ctx =>
        ctx.complete {
          val result: Set[Member] = MemberDAL.all
          println(s"Got all member: $result")
          result
        }
      } ~
        post {
          entity(as[Member]) { member =>
            // Check for existing email?
            val result = MemberDAL.insert(member)
            println(s"Inserted member $member")
            complete(result)
          }
        }
    } ~
      path("members" / LongNumber) { id =>
        rejectEmptyResponse {
          get {
            complete(MemberDAL.get(id) match {
              case Some(x) => x
              case _ => NotFound
            })
          } ~
            put {
              entity(as[Member]) { member =>
                val r = MemberDAL.get(id)
                r match {
                  case Some(x) => complete(MemberDAL.update(member.copy(id = Some(id))))
                  case None => complete(NotFound)
                }
              }
            }
        }
      } ~
      path("members" / Rest) { email =>
        rejectEmptyResponse {
          get {
            complete(MemberDAL.get(email) match {
              case Some(x) => x
              case _ => NotFound
            })
          }
        } ~
          put {
            entity(as[Member]) { member =>
              val r = MemberDAL.get(email)
              r match {
                case Some(x) => println(s"Found member $x")
                  complete(x)
                case None => println(s"Could not find for email $email")
                  complete(NotFound)
              }
            }
          }
      }
  }

}
