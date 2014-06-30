package com.votabulary.api

import com.votabulary.model.DBConfig
import com.votabulary.model.member.Member
import spray.json.DefaultJsonProtocol
import spray.routing.HttpService
import spray.http.StatusCodes._

object MemberJSON extends DefaultJsonProtocol {
  implicit val memberFormat = jsonFormat10(Member)
}

/**
 * Created by jason on 6/29/14.
 */
trait MemberService extends HttpService with DBConfig {
  this: DBConfig =>

  import MemberJSON._

  val memberRoute = {
    import spray.httpx.SprayJsonSupport.sprayJsonMarshaller
    import spray.httpx.SprayJsonSupport.sprayJsonUnmarshaller

    path("members") {
      get { ctx =>
        ctx.complete {
          val result: List[Member] = m.allMembers
          println(s"Got all member: $result")
          result
        }
      } ~
      post {
        entity(as[Member]) { member =>
          val result = m.addMember(member)
          println(s"Inserted member $member")
          complete(result)
        }
      }
    } ~
      path("members" / LongNumber) { id =>
        rejectEmptyResponse {
          get { ctx =>
            ctx.complete {
              val result: Option[Member] = m.getMember(id)
              result match {
                case Some(x) => println(s"Got member $x")
                  complete(x)
                case _ => println(s"Cound not find member for ID ($id)")
                  complete(NotFound)
              }
            }
          } ~
            put {
              entity(as[Member]) { member =>
                val r = m.getMember(id)
                r match {
                  case Some(x) => complete(m.updateMember(x))
                  case None => complete(NotFound)
                }
              }
            }
        }
      } ~
      path("members" / Rest) { email =>
        rejectEmptyResponse {
          get { ctx =>
            ctx.complete {
              val result: Option[Member] = m.getMember(email)
              result match {
                case Some(x) => println(s"Got member $x")
                  complete(x)
                case _ => println(s"Could not find member for email ($email)")
                  complete(NotFound)
              }
            }
          } ~
          put {
            entity(as[Member]) { member =>
              val r = m.getMember(email)
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

}
