package com.votabulary.api

import spray.http.MediaTypes._
import spray.routing.HttpService

/**
 * Created by jason on 6/29/14.
 */
trait MemberService extends HttpService {

  val memberRoute =
    get {
      path("member") {
        respondWithMediaType(`text/html`) { // XML is marshalled to `text/xml` by default, so we simply override here
          complete {
            <html>
              <body>
                <h1>Votabulary member registration!</h1>
              </body>
            </html>
          }
        }
      }
    }

}
